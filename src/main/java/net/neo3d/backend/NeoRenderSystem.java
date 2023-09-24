package net.neo3d.backend;

import com.mojang.blaze3d.platform.GlStateManager;
import org.joml.Matrix4f;

public class NeoRenderSystem {
    public static long GLFW_WINDOW_HANDLE;
    public static long NATIVE_STATE_HANDLE;

    public static void initRenderer() {
    }

    public static void setWindow(long window) {
        NeoRenderSystem.GLFW_WINDOW_HANDLE = window;
    }

    public static void setVsync(boolean vsync) {
    }

    public static void resize(int width, int height) {
    }

    public static void clear(int mask) {
    }

    public static void clearColor(float r, float g, float b, float a) {
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    }

    public static int maxSupportedTextureSize() {
        return 0;
    }

    public static void enableColorLogicOp() {
    }

    public static void disableColorLogicOp() {
    }

    public static void logicOp(GlStateManager.LogicOp op) {
    }

    public static void disableDepthTest() {
    }

    public static void enableDepthTest() {
    }

    public static void depthFunc(int i) {
    }

    public static void depthMask(boolean b) {
    }

    public static void enableBlend() {
    }

    public static void disableBlend() {
    }

    public static void blendFunc(GlStateManager.SourceFactor sourceFactor, GlStateManager.DestFactor destFactor) {
    }

    public static void blendFunc(int srcFactor, int dstFactor) {
    }

    public static void blendFuncSeparate(GlStateManager.SourceFactor src, GlStateManager.DestFactor dst, GlStateManager.SourceFactor src2, GlStateManager.DestFactor dst2) {
    }

    public static void blendFuncSeparate(int srcFactorRGB, int dstFactorRGB, int srcFactorAlpha, int dstFactorAlpha) {
    }

    public static void enableCull() {
    }

    public static void disableCull() {
    }

    public static void enablePolygonOffset() {
    }

    public static void disablePolygonOffset() {
    }

    public static void polygonOffset(float p69864, float p69865) {
    }

    public static void setShaderFogColor(float f, float g, float h, float i) {
    }

    public static void renderCrosshair(int p69882, boolean b, boolean b1, boolean b2) {
    }

    public static void applyProjectionMatrix(Matrix4f matrix4f) {
    }

    public static void calculateMVP() {
    }

    public static void setShaderColor(float r, float g, float b, float a) {
    }
}
