package io.github.ennuil.ok_zoomer.events;

import io.github.ennuil.ok_zoomer.key_binds.ZoomKeyBinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = "ok_zoomer")
public class RegisterModEvents {
	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		ZoomKeyBinds.ZOOM_KEY.setKeyConflictContext(KeyConflictContext.IN_GAME);
		event.register(ZoomKeyBinds.ZOOM_KEY);
		if (ZoomKeyBinds.areExtraKeyBindsEnabled()) {
			ZoomKeyBinds.DECREASE_ZOOM_KEY.setKeyConflictContext(KeyConflictContext.IN_GAME);
			ZoomKeyBinds.INCREASE_ZOOM_KEY.setKeyConflictContext(KeyConflictContext.IN_GAME);
			ZoomKeyBinds.RESET_ZOOM_KEY.setKeyConflictContext(KeyConflictContext.IN_GAME);
			event.register(ZoomKeyBinds.DECREASE_ZOOM_KEY);
			event.register(ZoomKeyBinds.INCREASE_ZOOM_KEY);
			event.register(ZoomKeyBinds.RESET_ZOOM_KEY);
		}
	}
}
