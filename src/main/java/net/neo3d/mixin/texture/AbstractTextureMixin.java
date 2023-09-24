package net.neo3d.mixin.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.neo3d.backend.interfaces.INeoAbstractTexture;
import net.neo3d.backend.texture.NeoTexture;
import net.neo3d.backend.texture.NeoTextureSelector;
import net.neo3d.backend.gl.GlTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractTexture.class)
public abstract class AbstractTextureMixin implements INeoAbstractTexture {
    @Shadow protected boolean blur;
    @Shadow protected boolean mipmap;
    @Shadow protected int id;

    @Shadow public abstract int getId();

    protected NeoTexture texture;

    /**
     * @author
     */
    @Overwrite
    public void bind() {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(this::bindTexture);
        } else {
            this.bindTexture();
        }
    }

    /**
     * @author
     */
    @Overwrite
    public void releaseId() {
        if(this.texture != null) {
            this.texture.release();
            this.texture = null;
        }
//        else
//            System.out.println("trying to free null image");

        TextureUtil.releaseTextureId(this.id);
    }

    public void setId(int i) {
        this.id = i;
    }

    /**
     * @author
     */
    @Overwrite
    public void setFilter(boolean blur, boolean mipmap) {
        if(blur != this.blur || mipmap != this.mipmap) {
            this.blur = blur;
            this.mipmap = mipmap;

            texture.updateSampler(this.blur, false, this.mipmap);
        }
    }

    @Override
    public void bindTexture() {
        GlTexture.bindTexture(this.id);

        if (texture != null)
            NeoTextureSelector.bindTexture(texture);
        else
            NeoTextureSelector.bindTexture(NeoTextureSelector.getWhiteTexture());
    }

    public NeoTexture getTexture() {
        return texture;
    }

    public void setTexture(NeoTexture texture) {
        this.texture = texture;

        if(this.id == -1)
            this.getId();
        GlTexture.setTexture(this.id, this.texture);
    }
}
