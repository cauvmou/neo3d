package net.neo3d.mixin.screen;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neo3d.config.OptionScreenV;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    @Shadow @Final private Screen lastScreen;

    @Shadow @Final private Options options;

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "method_19828", at = @At("HEAD"), cancellable = true)
    private void injectVideoOptionScreen(CallbackInfoReturnable<Screen> cir) {
        cir.setReturnValue(new OptionScreenV(Component.literal("Video Setting"), this));
    }
}
