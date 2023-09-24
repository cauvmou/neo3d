package net.neo3d.mixin.texture;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTextureSelector;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {

    @Shadow @Final private DynamicTexture texture;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;bind()V"))
    private void overlay(DynamicTexture instance) {

        NeoTextureSelector.setOverlayTexture(((INeoAbstractTexture)this.texture).getTexture());
        NeoTextureSelector.setActiveTexture(2);
    }

//    @Inject(method = "<init>", at = @At(value = "RETURN", target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;bindTexture()V"))
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void overlay(CallbackInfo ci) {
        NeoTextureSelector.setActiveTexture(0);
    }

    @Inject(method = "setupOverlayColor", at = @At(value = "HEAD"), cancellable = true)
    private void setupOverlay(CallbackInfo ci) {
        NeoTextureSelector.setOverlayTexture(((INeoAbstractTexture)this.texture).getTexture());
        ci.cancel();
    }
}
