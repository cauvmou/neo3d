package net.neo3d.mixin.texture;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;

@Mixin(SimpleTexture.class)
public class SimpleTextureMixin {

    /**
     * @author
     */
    @Overwrite
    private void doLoad(NativeImage nativeImage, boolean blur, boolean clamp) {

        NeoTexture texture = null;
        try {
            texture = new NeoTexture(nativeImage.getWidth(), nativeImage.getHeight(), nativeImage.asByteArray(), 1, blur, clamp, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((INeoAbstractTexture)this).setTexture(texture);
        ((INeoAbstractTexture)this).bindTexture();
    }
}
