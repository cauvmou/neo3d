package net.neo3d.mixin.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DynamicTexture.class)
public abstract class DynamicTextureMixin extends AbstractTexture {

    @Shadow private NativeImage pixels;

    @Shadow public abstract void upload();

    @Redirect(method = "<init>(IIZ)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/TextureUtil;prepareImage(III)V"))
    private void redirect(int id, int width, int height) {
        createTexture();
    }

    @Redirect(method = "<init>(Lcom/mojang/blaze3d/platform/NativeImage;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/TextureUtil;prepareImage(III)V"))
    private void redirect2(int id, int width, int height) {
        createTexture();
    }

//    @Redirect(method = "<init>*", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/TextureUtil;prepareImage(III)V"))
//    private void redirect(int id, int width, int height) {
//
//        createTexture();
//
//        //debug
//        System.out.println("Dynamic texture mixin");
//    }

//    @Redirect(method = "<init>(Lcom/mojang/blaze3d/platform/NativeImage;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/NeoRenderSystem;recordRenderCall(Lcom/mojang/blaze3d/pipeline/RenderCall;)V"))
//    private void redirect2(RenderCall renderCall) {
//
//        createTexture();
//    }

    @Redirect(method = "<init>(Lcom/mojang/blaze3d/platform/NativeImage;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;recordRenderCall(Lcom/mojang/blaze3d/pipeline/RenderCall;)V"))
    private void redirect2(RenderCall renderCall) {

        RenderSystem.recordRenderCall(() -> {
            createTexture();
            this.upload();
        });

    }

    private void createTexture() {
        INeoAbstractTexture texture = ((INeoAbstractTexture)(this));
        NeoTexture neoTexture = new NeoTexture(this.pixels.getWidth(), this.pixels.getHeight());
        texture.setTexture(neoTexture);
        texture.bindTexture();
//        texture.setId(TextureMap.getId(vulkanImage));
    }

}
