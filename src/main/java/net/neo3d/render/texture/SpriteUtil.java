package net.neo3d.render.texture;

import net.neo3d.vulkan.queue.CommandPool;
import net.neo3d.vulkan.texture.VulkanImage;

import java.util.HashSet;
import java.util.Set;

public abstract class SpriteUtil {

    private static boolean doUpload = false;

    private static Set<VulkanImage> transitionedLayouts = new HashSet<>();

    public static void setDoUpload(boolean b) {
        doUpload = b;
    }

    public static boolean shouldUpload() {
        return doUpload;
    }

    public static void addTransitionedLayout(VulkanImage image) {
        transitionedLayouts.add(image);
    }

    public static void transitionLayouts(CommandPool.CommandBuffer commandBuffer) {
        transitionedLayouts.forEach(image -> image.readOnlyLayout(commandBuffer));

        transitionedLayouts.clear();
    }
}
