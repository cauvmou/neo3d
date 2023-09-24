package net.neo3d.backend.interfaces;

import net.neo3d.backend.texture.NeoTexture;

public interface INeoAbstractTexture {

    NeoTexture getTexture();
    void setTexture(NeoTexture texture);

    void bindTexture();

    void setId(int i);
}
