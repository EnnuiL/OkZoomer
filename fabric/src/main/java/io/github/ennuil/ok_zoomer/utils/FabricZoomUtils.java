package io.github.ennuil.ok_zoomer.utils;

import net.fabricmc.fabric.api.tag.client.v1.ClientTags;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class FabricZoomUtils {
	public static final Predicate<ItemStack> IS_VALID_SPYGLASS = stack -> ClientTags.isInWithLocalFallback(ZoomUtils.ZOOM_DEPENDENCIES_TAG, stack.getItem());
	private static boolean openCommandScreen = false;

	public static boolean shouldOpenCommandScreen() {
		return FabricZoomUtils.openCommandScreen;
	}

	public static void setOpenCommandScreen(boolean openCommandScreen) {
		FabricZoomUtils.openCommandScreen = openCommandScreen;
	}

	public static void addInitialPredicates() {
		ZoomUtils.addSpyglassProvider(player -> player.getInventory().contains(IS_VALID_SPYGLASS));
	}
}
