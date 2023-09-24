package net.neo3d.mixin.texture;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTextureSelector;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Shadow @Final private DynamicTexture lightTexture;

    /**
     * @author
     */
    @Overwrite
    public void turnOnLightLayer() {
//        NeoRenderSystem.setShaderTexture(2, this.textureIdentifier);
//        this.client.getTextureManager().bindTexture(this.textureIdentifier);
//        NeoRenderSystem.texParameter(3553, 10241, 9729);
//        NeoRenderSystem.texParameter(3553, 10240, 9729);
        NeoTextureSelector.setLightTexture(((INeoAbstractTexture)this.lightTexture).getTexture());
//        NeoRenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
