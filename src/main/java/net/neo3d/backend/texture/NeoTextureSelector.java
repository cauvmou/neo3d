package net.neo3d.backend.texture;

import java.nio.ByteBuffer;

public class NeoTextureSelector {
    private static NeoTexture boundTexture;
    private static NeoTexture boundTexture2;
    private static NeoTexture boundTexture3;
    private static NeoTexture lightTexture;
    private static NeoTexture overlayTexture;
    private static NeoTexture framebufferTexture;
    private static NeoTexture framebufferTexture2;

    private static final NeoTexture whiteTexture = NeoTexture.createWhiteTexture();

    private static int activeTexture = 0;

    public static void bindTexture(NeoTexture texture) {
        boundTexture = texture;
    }

    public static void bindTexture(int i, NeoTexture texture) {
        switch (i) {
            case 0 -> boundTexture = texture;
            case 1 -> lightTexture = texture;
            case 2 -> overlayTexture = texture;
        }
    }

    public static void bindTexture2(NeoTexture texture) {
        boundTexture2 = texture;
    }

    public static void bindTexture3(NeoTexture texture) {
        boundTexture3 = texture;
    }

    public static void bindFramebufferTexture(NeoTexture texture) {
        framebufferTexture = texture;
    }

    public static void bindFramebufferTexture2(NeoTexture texture) {
        framebufferTexture2 = texture;
    }

    public static void uploadSubTexture(int mipLevel, int width, int height, int xOffset, int yOffset, int bufferWidth, int bufferHeight, byte[] buffer) {
        NeoTexture texture;
        if(activeTexture == 0) texture = boundTexture;
        else if(activeTexture == 1) texture = lightTexture;
        else texture = overlayTexture;

        texture.writeSubTexture(mipLevel, width, height, xOffset, yOffset, bufferWidth, bufferHeight, buffer);
    }

    public static NeoTexture getTexture(String name) {
        return switch (name) {
            case "Sampler0" -> getBoundTexture();
            case "Sampler1" -> getOverlayTexture();
            case "Sampler2" -> getLightTexture();
            case "Sampler3" -> boundTexture2;
            case "Sampler4" -> boundTexture3;
            case "Framebuffer0" -> framebufferTexture;
            case "Framebuffer1" -> framebufferTexture2;
            default -> throw new RuntimeException("unknown sampler name: " + name);
        };
    }

    public static void setLightTexture(NeoTexture texture) {
        lightTexture = texture;
    }

    public static void setOverlayTexture(NeoTexture texture) { overlayTexture = texture; }

    public static void setActiveTexture(int activeTexture) {
        NeoTextureSelector.activeTexture = activeTexture;
    }

    public static NeoTexture getLightTexture() {
        return lightTexture;
    }

    public static NeoTexture getOverlayTexture() {
        return overlayTexture;
    }

    public static NeoTexture getBoundTexture() { return boundTexture; }

    public static NeoTexture getWhiteTexture() { return whiteTexture; }
}
