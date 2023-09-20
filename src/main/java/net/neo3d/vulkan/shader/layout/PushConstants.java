package net.neo3d.vulkan.shader.layout;

import java.util.List;

public class PushConstants extends AlignedStruct {

    protected PushConstants(List<Field.FieldInfo> infoList, int size) {
        super(infoList, size);
    }

}
