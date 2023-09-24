package net.neo3d.mixin.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.neo3d.backend.NeoRenderSystem;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.IntBuffer;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _bindTexture(int i) {
        GlTexture.bindTexture(i);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _disableBlend() {
        RenderSystem.assertOnRenderThread();
        NeoRenderSystem.disableBlend();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _enableBlend() {
        RenderSystem.assertOnRenderThread();
        NeoRenderSystem.enableBlend();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _blendFunc(int i, int j) {
        RenderSystem.assertOnRenderThread();
        NeoRenderSystem.blendFunc(i, j);

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _blendFuncSeparate(int i, int j, int k, int l) {
        RenderSystem.assertOnRenderThread();
        NeoRenderSystem.blendFuncSeparate(i, j, k, l);

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _disableScissorTest() {
        Renderer.resetScissor();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _enableScissorTest() {}

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _viewport(int x, int y, int width, int height) {
        Renderer.setViewport(x, y, width, height);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _scissorBox(int x, int y, int width, int height) {
        Renderer.setScissor(x, y, width, height);
    }

    //TODO
    /**
     * @author
     */
    @Overwrite(remap = false)
    public static int _getError() {
        return 0;
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, @Nullable IntBuffer pixels) {
        GlTexture.texImage2D(target, level, internalFormat, width, height, border, format, type, pixels != null ? MemoryUtil.memByteBuffer(pixels) : null);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _texSubImage2D(int target, int level, int offsetX, int offsetY, int width, int height, int format, int type, long pixels) {

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _texParameter(int i, int j, int k) {
        //TODO
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _texParameter(int i, int j, float k) {
        //TODO
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _pixelStore(int pname, int param) {

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static int _genTexture() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GlTexture.genTextureId();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _deleteTexture(int i) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlTexture.glDeleteTextures(i);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        RenderSystem.assertOnRenderThread();
        NeoRenderSystem.colorMask(red, green, blue, alpha);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _depthFunc(int i) {
        RenderSystem.assertOnRenderThreadOrInit();
        NeoRenderSystem.depthFunc(i);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _clearColor(float f, float g, float h, float i) {
        RenderSystem.assertOnRenderThreadOrInit();
        NeoRenderSystem.clearColor(f, g, h, i);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _clear(int mask, boolean bl) {
        RenderSystem.assertOnRenderThreadOrInit();
        NeoRenderSystem.clear(mask);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _glUseProgram(int i) {}

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _disableDepthTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        NeoRenderSystem.disableDepthTest();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _enableDepthTest() {
        RenderSystem.assertOnRenderThreadOrInit();
        NeoRenderSystem.enableDepthTest();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _depthMask(boolean bl) {
        RenderSystem.assertOnRenderThread();
        NeoRenderSystem.depthMask(bl);

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static int glGenFramebuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GlFramebuffer.genFramebufferId();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static int glGenRenderbuffers() {
        //TODO
        RenderSystem.assertOnRenderThreadOrInit();
        return GlFramebuffer.genFramebufferId();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _glBindFramebuffer(int i, int j) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.bindFramebuffer(i, j);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _glFramebufferTexture2D(int i, int j, int k, int l, int m) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.glFramebufferTexture2D(i, j, k, l, m);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _glBindRenderbuffer(int i, int j) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.bindRenderbuffer(i, j);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _glFramebufferRenderbuffer(int i, int j, int k, int l) {
        //TODO
        RenderSystem.assertOnRenderThreadOrInit();
        GlFramebuffer.glFramebufferRenderbuffer(i, j, k, l);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _glRenderbufferStorage(int i, int j, int k, int l) {
        RenderSystem.assertOnRenderThreadOrInit();
//        GL30.glRenderbufferStorage(i, j, k, l);
        GlFramebuffer.glRenderbufferStorage(i, j, k, l);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static int glCheckFramebufferStatus(int i) {
        RenderSystem.assertOnRenderThreadOrInit();
//        return GL30.glCheckFramebufferStatus(i);
        return GlFramebuffer.glCheckFramebufferStatus(i);
    }
}
