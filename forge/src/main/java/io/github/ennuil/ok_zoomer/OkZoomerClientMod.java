package io.github.ennuil.ok_zoomer;

import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.config.metadata.WidgetSize;
import io.github.ennuil.ok_zoomer.config.screen.OkZoomerConfigScreen;
import io.github.ennuil.ok_zoomer.events.ApplyLoadOnceOptionsEvent;
import io.github.ennuil.ok_zoomer.utils.NorgeZoomUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.quiltmc.config.api.annotations.ConfigFieldAnnotationProcessor;

@Mod(value = "ok_zoomer")
public class OkZoomerClientMod {
	public OkZoomerClientMod() {
		ConfigFieldAnnotationProcessor.register(WidgetSize.class, new WidgetSize.Processor());
		OkZoomerConfigManager.init();

		ApplyLoadOnceOptionsEvent.readyClient(Minecraft.getInstance());

		MinecraftForge.registerConfigScreen((mod2, screen) -> new OkZoomerConfigScreen(screen));

		NorgeZoomUtils.defineSafeSmartOcclusion();
		NorgeZoomUtils.addInitialPredicates();
	}
}
