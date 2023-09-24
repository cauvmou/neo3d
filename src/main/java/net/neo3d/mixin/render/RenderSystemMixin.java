package net.neo3d.mixin.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.neo3d.backend.NeoRenderSystem;
import net.neo3d.interfaces.VAbstractTextureI;
import net.neo3d.vulkan.Renderer;
import net.neo3d.vulkan.VRenderSystem;
import net.neo3d.vulkan.texture.VTextureSelector;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

import static com.mojang.blaze3d.systems.RenderSystem.*;

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {

    @Shadow private static Matrix4f projectionMatrix;
    @Shadow private static Matrix4f savedProjectionMatrix;
    @Shadow private static PoseStack modelViewStack;
    @Shadow private static Matrix4f modelViewMatrix;
    @Shadow private static Matrix4f textureMatrix;
    @Shadow @Final private static int[] shaderTextures;
    @Shadow @Final private static float[] shaderColor;
    @Shadow @Final private static Vector3f[] shaderLightDirections;

    @Shadow
    public static void assertOnGameThreadOrInit() {
    }

    @Shadow @Final private static float[] shaderFogColor;

    @Shadow private static @Nullable Thread renderThread;

    /**
     * @author
     */
    @Overwrite
    public static void _setShaderTexture(int i, ResourceLocation location) {
        if (i >= 0 && i < shaderTextures.length) {
            TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
            AbstractTexture abstracttexture = texturemanager.getTexture(location);
            //abstracttexture.bindTexture();
            VTextureSelector.bindTexture(i, ((VAbstractTextureI)abstracttexture).getVulkanImage());

            //shaderTextures[i] = abstracttexture.getId();
        }

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void initRenderer(int debugVerbosity, boolean debugSync) {
        NeoRenderSystem.initRenderer();

        renderThread.setPriority(Thread.NORM_PRIORITY + 2);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void setupDefaultState(int x, int y, int width, int height) { }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void enableColorLogicOp() {
        assertOnGameThread();
        //GlStateManager._enableColorLogicOp();
        //Vulkan
        NeoRenderSystem.enableColorLogicOp();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void disableColorLogicOp() {
        assertOnGameThread();
        //GlStateManager._disableColorLogicOp();
        //Vulkan
        NeoRenderSystem.disableColorLogicOp();
    }

    /**
     * @author
     */
    @Overwrite
    public static void logicOp(GlStateManager.LogicOp op) {
        assertOnGameThread();
        //GlStateManager._logicOp(op.value);
        //Vulkan
        NeoRenderSystem.logicOp(op);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void activeTexture(int texture) {}

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void glGenBuffers(Consumer<Integer> consumer) {}

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void glGenVertexArrays(Consumer<Integer> consumer) {}

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static int maxSupportedTextureSize() {
        return NeoRenderSystem.maxSupportedTextureSize();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void clear(int mask, boolean getError) {
        NeoRenderSystem.clear(mask);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void flipFrame(long window) {
        org.lwjgl.glfw.GLFW.glfwPollEvents();
        RenderSystem.replayQueue();
        Tesselator.getInstance().getBuilder().clear();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void viewport(int x, int y, int width, int height) {
        Renderer.setViewport(x, y, width, height);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void enableScissor(int x, int y, int width, int height) {
        Renderer.setScissor(x, y, width, height);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void disableScissor() {
        Renderer.resetScissor();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void disableDepthTest() {
        assertOnGameThread();
        //GlStateManager._disableDepthTest();
        NeoRenderSystem.disableDepthTest();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void enableDepthTest() {
        assertOnGameThreadOrInit();
        //GlStateManager._enableDepthTest();
        NeoRenderSystem.enableDepthTest();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void depthFunc(int i) {
        assertOnGameThread();
        //GlStateManager._depthFunc(i);
        NeoRenderSystem.depthFunc(i);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void depthMask(boolean b) {
        assertOnGameThread();
        //GlStateManager._depthMask(b);
        NeoRenderSystem.depthMask(b);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        NeoRenderSystem.colorMask(red, green, blue, alpha);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void blendEquation(int i) {
        assertOnRenderThread();
        //TODO
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void enableBlend() {
        NeoRenderSystem.enableBlend();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void disableBlend() {
        NeoRenderSystem.disableBlend();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void blendFunc(GlStateManager.SourceFactor sourceFactor, GlStateManager.DestFactor destFactor) {
        NeoRenderSystem.blendFunc(sourceFactor, destFactor);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void blendFunc(int srcFactor, int dstFactor) {
        NeoRenderSystem.blendFunc(srcFactor, dstFactor);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void blendFuncSeparate(GlStateManager.SourceFactor p_69417_, GlStateManager.DestFactor p_69418_, GlStateManager.SourceFactor p_69419_, GlStateManager.DestFactor p_69420_) {
        NeoRenderSystem.blendFuncSeparate(p_69417_, p_69418_, p_69419_, p_69420_);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void blendFuncSeparate(int srcFactorRGB, int dstFactorRGB, int srcFactorAlpha, int dstFactorAlpha) {
        NeoRenderSystem.blendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void enableCull() {
        assertOnGameThread();
        //GlStateManager._enableCull();
        //Vulkan
        NeoRenderSystem.enableCull();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void disableCull() {
        assertOnGameThread();
        //GlStateManager._disableCull();
        //Vulkan
        NeoRenderSystem.disableCull();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void enablePolygonOffset() {
        assertOnGameThread();
//      GlStateManager._enablePolygonOffset();
        //Vulkan
        NeoRenderSystem.enablePolygonOffset();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void disablePolygonOffset() {
        assertOnGameThread();
//      GlStateManager._disablePolygonOffset();
        //Vulkan
        NeoRenderSystem.disablePolygonOffset();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void polygonOffset(float p_69864_, float p_69865_) {
        assertOnGameThread();
//      GlStateManager._polygonOffset(p_69864_, p_69865_);
        //Vulkan
        NeoRenderSystem.polygonOffset(p_69864_, p_69865_);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void clearColor(float p_69425_, float p_69426_, float p_69427_, float p_69428_) {
        assertOnGameThreadOrInit();
        NeoRenderSystem.clearColor(p_69425_, p_69426_, p_69427_, p_69428_);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void _setShaderLights(Vector3f p_157174_, Vector3f p_157175_) {
        shaderLightDirections[0] = p_157174_;
        shaderLightDirections[1] = p_157175_;

        //Vulkan
        NeoRenderSystem.lightDirection0.buffer.putFloat(0, p_157174_.x());
        NeoRenderSystem.lightDirection0.buffer.putFloat(4, p_157174_.y());
        NeoRenderSystem.lightDirection0.buffer.putFloat(8, p_157174_.z());

        NeoRenderSystem.lightDirection1.buffer.putFloat(0, p_157175_.x());
        NeoRenderSystem.lightDirection1.buffer.putFloat(4, p_157175_.y());
        NeoRenderSystem.lightDirection1.buffer.putFloat(8, p_157175_.z());
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    private static void _setShaderColor(float r, float g, float b, float a) {
        shaderColor[0] = r;
        shaderColor[1] = g;
        shaderColor[2] = b;
        shaderColor[3] = a;

        //Vulkan
        NeoRenderSystem.setShaderColor(r, g, b, a);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    private static void _setShaderFogColor(float f, float g, float h, float i) {
        shaderFogColor[0] = f;
        shaderFogColor[1] = g;
        shaderFogColor[2] = h;
        shaderFogColor[3] = i;

        NeoRenderSystem.setShaderFogColor(f, g, h, i);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void renderCrosshair(int p_69882_) {
        assertOnGameThread();
        //GLX._renderCrosshair(p_69882_, true, true, true);
        //Vulkan
        NeoRenderSystem.renderCrosshair(p_69882_, true, true, true);
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void setProjectionMatrix(Matrix4f projectionMatrix, VertexSorting vertexSorting) {
        Matrix4f matrix4f = new Matrix4f(projectionMatrix);
        if (!isOnRenderThread()) {
            recordRenderCall(() -> {
                RenderSystemMixin.projectionMatrix = matrix4f;
                //Vulkan
                NeoRenderSystem.applyProjectionMatrix(matrix4f);
                NeoRenderSystem.calculateMVP();
            });
        } else {
            RenderSystemMixin.projectionMatrix = matrix4f;
            //Vulkan
            NeoRenderSystem.applyProjectionMatrix(matrix4f);
            NeoRenderSystem.calculateMVP();
        }

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void setTextureMatrix(Matrix4f matrix4f) {
        Matrix4f matrix4f2 = new Matrix4f(matrix4f);
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                textureMatrix = matrix4f2;
                NeoRenderSystem.setTextureMatrix(matrix4f);
            });
        } else {
            textureMatrix = matrix4f2;
            NeoRenderSystem.setTextureMatrix(matrix4f);
        }
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void resetTextureMatrix() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> textureMatrix.identity());
        } else {
            textureMatrix.identity();
            NeoRenderSystem.setTextureMatrix(textureMatrix);
        }
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void applyModelViewMatrix() {
        Matrix4f matrix4f = new Matrix4f(modelViewStack.last().pose());
        if (!isOnRenderThread()) {
            recordRenderCall(() -> {
                modelViewMatrix = matrix4f;
                //Vulkan
                NeoRenderSystem.applyModelViewMatrix(matrix4f);
                NeoRenderSystem.calculateMVP();
            });
        } else {
            modelViewMatrix = matrix4f;
            //Vulkan
            NeoRenderSystem.applyModelViewMatrix(matrix4f);
            NeoRenderSystem.calculateMVP();
        }

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    private static void _restoreProjectionMatrix() {
        projectionMatrix = savedProjectionMatrix;
        //Vulkan
        NeoRenderSystem.applyProjectionMatrix(projectionMatrix);
        NeoRenderSystem.calculateMVP();
    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void texParameter(int target, int pname, int param) {
        //TODO
    }
}
