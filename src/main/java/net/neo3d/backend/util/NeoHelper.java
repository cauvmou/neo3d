package net.neo3d.backend.util;

public class NeoHelper {

    public static boolean isSwapChainFormatBGRA() {
        return false;
    }

    public static long getSwapChainColorAttachmentId() {
        return 0;
    }

    public static int BGRAtoRGBA(int bgra) {
        int a = bgra & 0xff;
        int bgr = bgra >> 8;
        return Integer.reverse(bgr) | a;
    }
}
