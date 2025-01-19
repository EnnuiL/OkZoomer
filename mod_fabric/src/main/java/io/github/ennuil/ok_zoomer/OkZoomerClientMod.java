package io.github.ennuil.ok_zoomer;

import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.events.*;
import io.github.ennuil.ok_zoomer.key_binds.ZoomKeyBinds;
import io.github.ennuil.ok_zoomer.sound.FabricSoundEvents;
import io.github.ennuil.ok_zoomer.utils.FabricZoomUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

// This class is responsible for registering the commands and packets
public class OkZoomerClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Initialize the config
		OkZoomerConfigManager.init();

		// Register all the key binds
		KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.ZOOM_KEY);
		if (ZoomKeyBinds.areExtraKeyBindsEnabled()) {
			KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.DECREASE_ZOOM_KEY);
			KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.INCREASE_ZOOM_KEY);
			KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.RESET_ZOOM_KEY);
		}

		// Initialize zoom sound events
		FabricSoundEvents.init();

		// Register events without entrypoints aughhhhhhhh
		ClientTickEvents.START_CLIENT_TICK.register(ManageZoomEvent::startClientTick);
		ClientTickEvents.START_CLIENT_TICK.register(ManageExtraKeysEvent::startClientTick);
		ClientLifecycleEvents.CLIENT_STARTED.register(ApplyLoadOnceOptionsEvent::readyClient);
		ClientTickEvents.END_CLIENT_TICK.register(OpenScreenEvent::endClientTick);
		ClientCommandRegistrationCallback.EVENT.register(RegisterCommands::registerCommands);

		FabricZoomUtils.defineSafeSmartOcclusion();
		FabricZoomUtils.addInitialPredicates();

		/*
		// mod compat
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			TrinketsCompat.init();
		}
		*/
	}
}
