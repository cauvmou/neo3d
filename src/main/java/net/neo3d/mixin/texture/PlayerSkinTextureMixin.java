package net.neo3d.mixin.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;

@Mixin(HttpTexture.class)
public class PlayerSkinTextureMixin {

    /**
     * @author
     */
    @Overwrite
    private void upload(NativeImage nativeImage) {
        NeoTexture texture = null;
        try {
            texture = new NeoTexture(nativeImage.getWidth(), nativeImage.getHeight(), nativeImage.asByteArray(), 1, false, true, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((INeoAbstractTexture)this).setTexture(texture);
        ((INeoAbstractTexture)this).bindTexture();
    }
}
