package net.neo3d.vulkan.queue;

import org.lwjgl.system.MemoryStack;

public class PresentQueue extends Queue {

    public PresentQueue(MemoryStack stack, int familyIndex) {
        super(stack, familyIndex, false);
    }
}
