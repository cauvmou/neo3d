package net.neo3d.mixin.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.neo3d.backend.util.NeoHelper;
import net.neo3d.backend.texture.NeoTexture;
import net.neo3d.backend.texture.NeoTextureSelector;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin(NativeImage.class)
public abstract class NativeImageMixin {

    @Shadow private long pixels;
    @Shadow private long size;

    @Shadow public abstract void close();


    @Shadow @Final private NativeImage.Format format;

    @Shadow public abstract int getWidth();

    @Shadow @Final private int width;
    @Shadow @Final private int height;

    @Shadow public abstract int getHeight();

    @Shadow public abstract void setPixelRGBA(int i, int j, int k);

    @Shadow public abstract int getPixelRGBA(int i, int j);

    private ByteBuffer buffer;

    @Inject(method = "<init>(Lcom/mojang/blaze3d/platform/NativeImage$Format;IIZ)V", at = @At("RETURN"))
    private void constr(NativeImage.Format format, int width, int height, boolean useStb, CallbackInfo ci) {
        if(this.pixels != 0) {
            buffer = MemoryUtil.memByteBuffer(this.pixels, (int)this.size);
        }
    }

    @Inject(method = "<init>(Lcom/mojang/blaze3d/platform/NativeImage$Format;IIZJ)V", at = @At("RETURN"))
    private void constr(NativeImage.Format format, int width, int height, boolean useStb, long pixels, CallbackInfo ci) {
        if(this.pixels != 0) {
            buffer = MemoryUtil.memByteBuffer(this.pixels, (int)this.size);
        }
    }

    /**
     * @author
     */
    @Overwrite
    private void _upload(int level, int xOffset, int yOffset, int unpackSkipPixels, int unpackSkipRows, int widthIn, int heightIn, boolean blur, boolean clamp, boolean mipmap, boolean autoClose) {
        RenderSystem.assertOnRenderThreadOrInit();

        NeoTextureSelector.uploadSubTexture(level, widthIn, heightIn, xOffset, yOffset, unpackSkipRows, unpackSkipPixels, this.getWidth(), this.buffer);

        if (autoClose) {
            this.close();
        }
    }

    /**
     * @author
     */
    @Overwrite
    public void downloadTexture(int level, boolean removeAlpha) {
        RenderSystem.assertOnRenderThread();

        NeoTexture.readTexture(this.width, this.height, 4, this.buffer, NeoHelper.getSwapChainColorAttachmentId());

        if (removeAlpha && this.format.hasAlpha()) {
            for (int i = 0; i < this.height; ++i) {
                for (int j = 0; j < this.getWidth(); ++j) {
                    int v = this.getPixelRGBA(j, i);

                    if(NeoHelper.isSwapChainFormatBGRA())
                        v = NeoHelper.BGRAtoRGBA(v);

                    this.setPixelRGBA(j, i, v | 255 << this.format.alphaOffset());
                }
            }
        }

    }

}
