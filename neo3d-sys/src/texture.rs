use wgpu::util::DeviceExt;

use crate::error::InvalidGLFormatError;

pub struct Texture {
    pub texture: wgpu::Texture,
    pub view: wgpu::TextureView,
    pub sampler: wgpu::Sampler,
    pub size: wgpu::Extent3d,
    pub mip_level_count: u32,
}

impl Texture {
    pub fn format_gl_to_wgpu(format: i32) -> anyhow::Result<wgpu::TextureFormat> {
        match format {
            6408 => Ok(wgpu::TextureFormat::Rgba8Unorm),
            33319 => Ok(wgpu::TextureFormat::Rg8Unorm),
            6403 => Ok(wgpu::TextureFormat::R8Unorm),
            _ => Err(InvalidGLFormatError { format }.into()),
        }
    }

    pub fn new(
        device: &wgpu::Device,
        format: wgpu::TextureFormat,
        mip_level_count: u32,
        width: u32,
        height: u32,
        usage: wgpu::TextureUsages,
        blur: bool,
        clamp: bool,
    ) -> Self {
        let size = wgpu::Extent3d {
            width,
            height,
            depth_or_array_layers: 1,
        };
        let texture = device.create_texture(&wgpu::TextureDescriptor {
            label: None,
            size,
            mip_level_count,
            sample_count: 1,
            dimension: wgpu::TextureDimension::D2,
            format,
            usage,
            view_formats: &[],
        });
        let view = texture.create_view(&wgpu::TextureViewDescriptor::default());
        let sampler = Self::create_sampler(device, blur, clamp, Some(mip_level_count));
        Self {
            texture,
            view,
            sampler,
            mip_level_count,
            size,
        }
    }

    pub fn create_white_texture(device: &wgpu::Device, queue: &wgpu::Queue) -> Self {
        let instance = Self::new(
            &device,
            wgpu::TextureFormat::Rgba8Unorm,
            1,
            1,
            1,
            wgpu::TextureUsages::COPY_DST | wgpu::TextureUsages::COPY_SRC,
            false,
            false,
        );
        instance.write_sub_texture(queue, 0, &[0xff, 0xff, 0xff, 0xff], 0, 1, 1);
        instance
    }

    fn create_sampler(
        device: &wgpu::Device,
        blur: bool,
        clamp: bool,
        mip_level_count: Option<u32>,
    ) -> wgpu::Sampler {
        let filter = if blur {
            wgpu::FilterMode::Linear
        } else {
            wgpu::FilterMode::Nearest
        };
        let address_mode = if clamp {
            wgpu::AddressMode::ClampToEdge
        } else {
            wgpu::AddressMode::Repeat
        };
        let mip = if let Some(mip_level_count) = mip_level_count {
            (wgpu::FilterMode::Linear, mip_level_count as f32)
        } else {
            (wgpu::FilterMode::Nearest, 0.0)
        };
        device.create_sampler(&wgpu::SamplerDescriptor {
            address_mode_u: address_mode,
            address_mode_v: address_mode,
            address_mode_w: address_mode,
            mag_filter: filter,
            min_filter: filter,
            mipmap_filter: mip.0,
            lod_max_clamp: mip.1,
            ..Default::default()
        })
    }

    pub fn update_sampler(&mut self, device: &wgpu::Device, blur: bool, clamp: bool, mipmap: bool) {
        self.sampler = Self::create_sampler(
            device,
            blur,
            clamp,
            if mipmap {
                Some(self.mip_level_count)
            } else {
                None
            },
        );
    }

    pub fn write_sub_texture(
        &self,
        queue: &wgpu::Queue,
        mip_level: u32,
        buffer: &[u8],
        offset: u64,
        buffer_width: u32,
        buffer_height: u32,
    ) {
        queue.write_texture(
            wgpu::ImageCopyTexture {
                texture: &self.texture,
                mip_level,
                origin: wgpu::Origin3d::ZERO,
                aspect: wgpu::TextureAspect::All,
            },
            buffer,
            wgpu::ImageDataLayout {
                offset,
                bytes_per_row: Some(
                    buffer_width
                        * self
                            .texture
                            .format()
                            .block_size(Some(wgpu::TextureAspect::All))
                            .unwrap(),
                ),
                rows_per_image: Some(buffer_height),
            },
            self.size,
        )
    }

    pub fn read_texture(
        &self,
        queue: &wgpu::Queue,
        device: &wgpu::Device,
        format_size: u32,
    ) -> Vec<u8> {
        let buffer = device.create_buffer(&wgpu::BufferDescriptor {
            label: None,
            usage: wgpu::BufferUsages::COPY_DST | wgpu::BufferUsages::MAP_READ,
            size: (self.size.width * self.size.height * format_size) as u64,
            mapped_at_creation: false,
        });
        {
            let mut encoder =
                device.create_command_encoder(&wgpu::CommandEncoderDescriptor { label: None });
            encoder.copy_texture_to_buffer(
                self.texture.as_image_copy(),
                wgpu::ImageCopyBuffer {
                    buffer: &buffer,
                    layout: wgpu::ImageDataLayout {
                        offset: 0,
                        bytes_per_row: Some(self.size.width * format_size),
                        rows_per_image: Some(self.size.height),
                    },
                },
                self.size,
            );
            queue.submit(Some(encoder.finish()));
        }
        let slice = buffer.slice(..);
        let (tx, rx) = std::sync::mpsc::channel();
        slice.map_async(wgpu::MapMode::Read, move |result| {
            tx.send(result).unwrap();
        });
        device.poll(wgpu::Maintain::Wait);
        rx.recv().unwrap().unwrap();

        let vec = slice.get_mapped_range().to_vec();
        vec
    }
}
