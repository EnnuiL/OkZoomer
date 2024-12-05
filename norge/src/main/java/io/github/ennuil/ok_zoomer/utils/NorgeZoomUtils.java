package io.github.ennuil.ok_zoomer.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.function.Predicate;

public class NorgeZoomUtils {
	// TODO - Bad! We need client tags for this!
	public static final Predicate<ItemStack> IS_VALID_SPYGLASS = stack -> stack.is(ZoomUtils.ZOOM_DEPENDENCIES_TAG);

	public static void addInitialPredicates() {
		ZoomUtils.addSpyglassProvider(player -> player.getInventory().contains(IS_VALID_SPYGLASS));
	}

	public static void defineSafeSmartOcclusion() {
		// If Sodium is enabled, it's 100% safe to enable Smart Occlusion by default!
		if (ModList.get().isLoaded("sodium")) {
			ZoomUtils.enableSafeSmartOcclusion();
		}
	}
}
