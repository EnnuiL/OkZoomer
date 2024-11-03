package io.github.ennuil.ok_zoomer.mixin.common;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.zoom.Zoom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderStateShard.class)
public abstract class RenderStateShardMixin {
	@Inject(
		method = {
			"method_62269",
			"lambda$static$14()V"
		},
		at = @At("TAIL"),
		allow = 1,
		remap = false
	)
	private static void fadeCrosshair(CallbackInfo ci) {
		if (OkZoomerConfigManager.CONFIG.tweaks.hideCrosshair.value()) {
			float fade = 1.0F - Zoom.getTransitionMode().getFade(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true));
			RenderSystem.setShaderColor(fade, fade, fade, fade);
		}
	}

	@Inject(
		method = {
			"method_62268",
			"lambda$static$13()V"
		},
		at = @At("HEAD"),
		allow = 1,
		remap = false
	)
	private static void resetCrosshairFade(CallbackInfo ci) {
		if (OkZoomerConfigManager.CONFIG.tweaks.hideCrosshair.value()) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
