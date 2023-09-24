package net.neo3d.mixin.texture;

import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextureAtlas.class)
public class SpriteAtlasTextureMixin {

    @Redirect(method = "upload", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/TextureUtil;prepareImage(IIII)V"))
    private void redirect(int id, int maxLevel, int width, int height) {
        NeoTexture texture = new NeoTexture(width, height, new byte[0], maxLevel+1, null);
        ((INeoAbstractTexture)(this)).setTexture(texture);
        ((INeoAbstractTexture)(this)).bindTexture();
    }

    /**
     * @author
     */
    @Overwrite
    public void updateFilter(SpriteLoader.Preparations data) {
        //this.setFilter(false, data.maxLevel > 0);
    }
}
