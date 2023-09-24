package net.neo3d.backend.gl;

import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import net.neo3d.backend.texture.NeoTexture;
import net.neo3d.backend.texture.NeoTextureSelector;

import static org.lwjgl.vulkan.VK10.VK_FORMAT_R8G8B8A8_UNORM;

public class GlTexture {
    private static int ID_COUNT = 0;
    private static final Int2ReferenceOpenHashMap<GlTexture> map = new Int2ReferenceOpenHashMap<>();
    private static int boundTextureId = 0;
    private static GlTexture boundTexture;

    public static int genTextureId() {
        int id = ID_COUNT;
        map.put(id, new GlTexture(id));
        ID_COUNT++;
        return id;
    }

    public static void bindTexture(int i) {
        boundTextureId = i;
        boundTexture = map.get(i);

        if(boundTexture == null)
            throw new NullPointerException("bound texture is null");

        NeoTexture neoTexture = boundTexture.neoTexture;
        if(neoTexture != null)
            NeoTextureSelector.bindTexture(neoTexture);
    }

    public static void glDeleteTextures(int i) {
        map.remove(i);
    }

    public static GlTexture getTexture(int id) {
        return map.get(id);
    }

    public static void texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, byte[] pixels) {
        if(width == 0 || height == 0)
            return;

        if(width != boundTexture.neoTexture.width || height != boundTexture.neoTexture.height || vulkanFormat(format, type) != boundTexture.neoTexture.format) {
            boundTexture.createAndBindEmptyTexture(width, height);
        }

        boundTexture.uploadImage(pixels);
    }


    public static void texSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, byte[] pixels) {
        if(width == 0 || height == 0)
            return;

        NeoTextureSelector.uploadSubTexture(level, width, height, xOffset, yOffset, width, height, pixels);
    }

    public static void setTexture(int id, NeoTexture neoTexture) {
        GlTexture texture = map.get(id);

        texture.neoTexture = neoTexture;
    }

    final int id;
    NeoTexture neoTexture;

    public GlTexture(int id) {
        this.id = id;
    }

    private void createAndBindEmptyTexture(int width, int height) {
        if(this.neoTexture != null)
            this.neoTexture.free();

        this.neoTexture = new NeoTexture.Builder(width, height).build();
        NeoTextureSelector.bindTexture(this.neoTexture);
    }

    private void uploadImage(byte[] pixels) {
        int width = this.neoTexture.width;
        int height = this.neoTexture.height;

        if(pixels != null) {
//            if(pixels.remaining() != width * height * 4)
//                throw new IllegalArgumentException("buffer size does not match image size");

            this.neoTexture.writeSubTexture(0, width, height, 0, 0, width, height, pixels);
        }
        else {
            pixels = new byte[width * height * 4];
            this.neoTexture.writeSubTexture(0, width, height, 0, 0, width, height, pixels);
        }
    }

    private static int vulkanFormat(int glFormat, int type) {
        return switch (glFormat) {
            case 6408 ->
                    switch (type) {
                        case 5121 -> VK_FORMAT_R8G8B8A8_UNORM;
                        default -> throw new IllegalStateException("Unexpected value: " + type);
                    };

            default -> throw new IllegalStateException("Unexpected value: " + glFormat);
        };
    }

}
