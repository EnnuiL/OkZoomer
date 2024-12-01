package io.github.ennuil.ok_zoomer.mixin.common;

import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.zoom.Zoom;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SectionOcclusionGraph;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow
	@Final
	private SectionOcclusionGraph sectionOcclusionGraph;

	@Unique
	private int prevZoomDivisor = 1;

	@Inject(
		method = "setupRender",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/renderer/LevelRenderer;prevCamX:D",
			ordinal = 1
		)
	)
	private void accountForZooming(Camera camera, Frustum frustum, boolean hasCapturedFrustum, boolean isSpectator, CallbackInfo ci) {
		if (OkZoomerConfigManager.CONFIG.tweaks.smartOcclusion.value()) {
			int divisor = Zoom.isZooming() ? Mth.floor(Zoom.getZoomDivisor()) : 1;

			if (divisor != this.prevZoomDivisor) {
				this.sectionOcclusionGraph.invalidate();
			}

			this.prevZoomDivisor = divisor;
		}
	}
}
