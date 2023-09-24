package net.neo3d.backend;

/**
 * The neo3d-sys wrapper.
 * @author cauvmou
 */
public class NeoNative {
    static {
        System.loadLibrary("neo3d-sys");
    }

    public static class Texture {
        public static native long createTexture(long handle, int format, int mipLevels, int width, int height, int usage, int formatSize, boolean blur, boolean clamp);
        public static native long createWhiteTexture(long handle);
    }
}
