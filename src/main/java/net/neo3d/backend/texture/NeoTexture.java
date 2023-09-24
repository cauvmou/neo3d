package net.neo3d.backend.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.neo3d.backend.NeoNative;
import net.neo3d.backend.NeoRenderSystem;
import net.neo3d.deprecated.vulkan.texture.VulkanImage;

import static org.lwjgl.vulkan.VK10.*;

public class NeoTexture {

    public class TextureUsages {
        public static final int COPY_SRC = 1;
        public static final int COPY_DST = 1 << 1;
        public static final int TEXTURE_BINDING = 1 << 2;
        public static final int STORAGE_BINDING = 1 << 3;
        public static final int RENDER_ATTACHMENT = 1 << 4;
    }

    long texture;
    public int format, mipLevels, width, height, usage, formatSize;
    public boolean blur, clamp;
    public static NativeImage.InternalGlFormat DEFAULT_FORMAT = NativeImage.InternalGlFormat.RGBA;

    public NeoTexture(int format, int mipLevels, int width, int height, int usage, int formatSize, boolean blur, boolean clamp) {
        this.format = format;
        this.mipLevels = mipLevels;
        this.width = width;
        this.height = height;
        this.usage = usage;
        this.formatSize = formatSize;
        this.blur = blur;
        this.clamp = clamp;
        this.texture = NeoNative.Texture.createTexture(NeoRenderSystem.NATIVE_STATE_HANDLE, format, mipLevels, width, height, usage, formatSize, blur, clamp);
    }

    public static NeoTexture createWhiteTexture() {
        var instance = new NeoTexture(NativeImage.InternalGlFormat.RGBA.glFormat(), 1, 1, 1, TextureUsages.COPY_DST | TextureUsages.COPY_SRC, 4, false, false);
        instance.writeSubTexture(0, instance.width, instance.height, 0, 0, instance.width, instance.height, new byte[]{ (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff });
        return instance;
    }

    public void release() {}

    public void updateSampler(boolean blur, boolean clamp, boolean mipmap) {}

    public void writeSubTexture(int mipLevel, int width, int height, int xOffset, int yOffset, int bufferWidth, int bufferHeight, byte[] buffer) {
    }
    public static void readTexture(int width, int height, int formatSize, byte[] buffer, long image) {
    }

    public void free() {
    }

    public static class Builder {
        final int width;
        final int height;

        NativeImage.InternalGlFormat format = NeoTexture.DEFAULT_FORMAT;
        int mipLevels = 1;
        int usage = VK_IMAGE_USAGE_TRANSFER_DST_BIT | VK_IMAGE_USAGE_SAMPLED_BIT;

        boolean linearFiltering = false;
        boolean clamp = false;

        public Builder(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public Builder setFormat(NativeImage.InternalGlFormat format) {
            this.format = format;
            return this;
        }

        public Builder setMipLevels(int n) {
            this.mipLevels = n;
            return this;
        }

        public Builder setUsage(int usage) {
            this.usage = usage;
            return this;
        }

        public Builder setLinearFiltering(boolean b) {
            this.linearFiltering = b;
            return this;
        }

        public Builder setClamp(boolean b) {
            this.clamp = b;
            return this;
        }

        public NeoTexture build() {
            int formatSize = switch (this.format) {
                case RGBA -> 4;
                case RGB -> throw new RuntimeException("Bruh IDFK ... YET");
                case RG -> 2;
                case RED -> 1;
            };
            return new NeoTexture(this.format.glFormat(), this.mipLevels, this.width, this.height, this.usage, formatSize, this.linearFiltering, this.clamp);
        }
    }
}
