package net.neo3d.interfaces;

import net.neo3d.vulkan.texture.VulkanImage;

public interface VAbstractTextureI {

    public void bindTexture();

    void setId(int i);

    public VulkanImage getVulkanImage();

    public void setVulkanImage(VulkanImage image);
}
