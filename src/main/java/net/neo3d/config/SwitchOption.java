package net.neo3d.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.neo3d.config.widget.OptionWidget;
import net.neo3d.config.widget.SwitchOptionWidget;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SwitchOption extends Option<Boolean> {
    public SwitchOption(String name, Consumer<Boolean> setter, Supplier<Boolean> getter) {
        super(name, setter, getter);
    }

    @Override
    public AbstractWidget createWidget(int x, int y, int width, int height) {
        return null;
    }

    @Override
    public OptionWidget createOptionWidget(int x, int y, int width, int height) {
        return new SwitchOptionWidget(this, x, y, width, height, this.name);
    }

    @Override
    public void setValue(Boolean aBoolean) {
        this.newValue = aBoolean;
    }

}
