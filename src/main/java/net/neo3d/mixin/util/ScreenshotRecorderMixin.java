package net.neo3d.mixin.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {

    /**
     * @author
     */
    @Overwrite
    public static NativeImage takeScreenshot(RenderTarget framebuffer) {
        int i = framebuffer.width;
        int j = framebuffer.height;

        NativeImage nativeimage = new NativeImage(i, j, false);
        //NeoRenderSystem.bindTexture(p_92282_.getColorTextureId());
        nativeimage.downloadTexture(0, true);
        //nativeimage.flipY();
        return nativeimage;
    }
}
