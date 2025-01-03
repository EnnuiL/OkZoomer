package io.github.ennuil.ok_zoomer.compat;

import dev.emi.trinkets.api.TrinketsApi;
import io.github.ennuil.ok_zoomer.utils.FabricZoomUtils;
import io.github.ennuil.ok_zoomer.utils.ZoomUtils;

public class TrinketsCompat {
	public static void init() {
		ZoomUtils.addSpyglassProvider(player -> {
			// Trinkets inventory is an AutoSyncedComponent and therefore safe to query on the client
			//return player.getComponent(TrinketsApi.TRINKET_COMPONENT).isEquipped(FabricZoomUtils.IS_VALID_SPYGLASS);
			return true;
		});
	}
}
