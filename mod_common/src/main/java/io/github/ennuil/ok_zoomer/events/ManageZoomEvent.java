package io.github.ennuil.ok_zoomer.events;

import io.github.ennuil.ok_zoomer.config.ConfigEnums.ZoomModes;
import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.key_binds.ZoomKeyBinds;
import io.github.ennuil.ok_zoomer.utils.Portals;
import io.github.ennuil.ok_zoomer.utils.ZoomUtils;
import io.github.ennuil.ok_zoomer.zoom.Zoom;
import net.minecraft.client.Minecraft;

// This event is responsible for managing the zoom signal.
public class ManageZoomEvent {
	// Used internally in order to make zoom toggling possible
	private static boolean lastZooming = false;

	// Used internally in order to make persistent zoom less buggy
	private static boolean persistentZoom = false;

	private static boolean lastZoomWasSpyglass = false;

	public static void startClientTick(Minecraft minecraft) {
		// We need the player for spyglass shenanigans
		if (minecraft.player == null) return;

		// If zoom is disabled, do not allow for zooming at all
		/* If you want to reimplement mandatory spyglass control? It's easy:
		 * Quilt Config has value overrides, use them. This mod is ARR but it's anti-vulture;
		 * You can make a third-party addon mod, I won't bite. */
		boolean disableZoom = switch (OkZoomerConfigManager.CONFIG.features.spyglassMode.value()) {
				case REQUIRE_ITEM, BOTH -> true;
				default -> false;
			} && !ZoomUtils.hasSpyglass(minecraft.player);

		if (disableZoom) {
			Zoom.setZooming(false);
			ZoomUtils.resetZoomDivisor(false);
			lastZooming = false;
			return;
		}

		// Handle zoom mode changes.
		if (!OkZoomerConfigManager.CONFIG.features.zoomMode.value().equals(ZoomModes.HOLD)) {
			if (!persistentZoom) {
				persistentZoom = true;
				lastZooming = true;
				ZoomUtils.resetZoomDivisor(false);
			}
		} else {
			if (persistentZoom) {
				persistentZoom = false;
				lastZooming = true;
			}
		}

		// Gathers all variables about if the press was with zoom key or with the spyglass
		boolean isUsingSpyglass = switch (OkZoomerConfigManager.CONFIG.features.spyglassMode.value()) {
			case REPLACE_ZOOM, BOTH -> true;
			default -> false;
		};
		boolean keyPress = ZoomKeyBinds.ZOOM_KEY.isDown();
		boolean spyglassUse = minecraft.player.isScoping();
		boolean zooming = keyPress || (isUsingSpyglass && spyglassUse);

		// If the press state is the same as the previous tick's, cancel the rest
		// This makes toggling usable and the zoom divisor adjustable
		if (zooming == lastZooming) return;

		boolean doSpyglassSound = OkZoomerConfigManager.CONFIG.tweaks.useSpyglassSounds.value();

		switch (OkZoomerConfigManager.CONFIG.features.zoomMode.value()) {
			case HOLD -> {
				// If the zoom needs to be held, then the zoom signal is determined by if the key is pressed or not
				Zoom.setZooming(zooming);
				ZoomUtils.resetZoomDivisor(false);
			}
			case TOGGLE -> {
				// If the zoom needs to be toggled, toggle the zoom signal instead
				if (zooming) {
					Zoom.setZooming(!Zoom.isZooming());
					ZoomUtils.resetZoomDivisor(false);
				} else {
					doSpyglassSound = false;
				}
			}
			case PERSISTENT -> {
				// If persistent zoom is enabled, just keep the zoom on
				Zoom.setZooming(true);
				ZoomUtils.keepZoomStepsWithinBounds();
			}
		}

		if (doSpyglassSound && (!spyglassUse && !lastZoomWasSpyglass)) {
			boolean soundDirection = !OkZoomerConfigManager.CONFIG.features.zoomMode.value().equals(ZoomModes.PERSISTENT)
				? Zoom.isZooming()
				: keyPress;

			minecraft.player.playSound(soundDirection ? Portals.getZoomInSound() : Portals.getZoomOutSound(), 1.0F, 1.0F);
		}

		// Set the previous zoom signal for the next tick
		lastZooming = zooming;

		lastZoomWasSpyglass = spyglassUse && !keyPress;
	}
}
