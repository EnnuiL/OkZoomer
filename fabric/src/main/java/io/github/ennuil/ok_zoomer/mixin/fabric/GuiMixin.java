package io.github.ennuil.ok_zoomer.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.zoom.Zoom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
	@Unique
	private float translation = 0.0F;

	@Unique
	private float scale = 0.0F;

	@WrapMethod(method = "render")
	private void zoomGui(GuiGraphics graphics, float partialTick, Operation<Void> original) {
		if (OkZoomerConfigManager.CONFIG.features.persistentInterface.value() || !Zoom.getTransitionMode().getActive()) {
			original.call(graphics, partialTick);
		} else {
			float fov = Zoom.getTransitionMode().applyZoom(1.0F, partialTick);
			translation = 2.0F / ((1.0F / fov) - 1.0F);
			scale = 1.0F / fov;
			graphics.pose().pushPose();
			graphics.pose().translate(-(graphics.guiWidth() / translation), -(graphics.guiHeight() / translation), 0.0F);
			graphics.pose().scale(scale, scale, 1.0F);
			original.call(graphics, partialTick);
			graphics.pose().popPose();
		}
	}

	@WrapMethod(method = "renderCrosshair")
	private void hideCrosshair(GuiGraphics graphics, Operation<Void> original) {
		boolean persistentInterface = OkZoomerConfigManager.CONFIG.features.persistentInterface.value();
		boolean hideCrosshair = OkZoomerConfigManager.CONFIG.tweaks.hideCrosshair.value();
		if (persistentInterface || hideCrosshair || !Zoom.isTransitionActive()) {
			if (hideCrosshair) {
				float fade = 1.0F - Zoom.getTransitionMode().getFade(Minecraft.getInstance().getFrameTime());
				RenderSystem.setShaderColor(fade, fade, fade, fade);
			}
			original.call(graphics);
			if (hideCrosshair) {
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
		} else {
			// TODO - This has been recycled once, this should become a method
			var lastPose = graphics.pose().last().pose();
			graphics.pose().popPose();
			graphics.pose().pushPose();
			graphics.pose().translate(0.0F, 0.0F, lastPose.getTranslation(new Vector3f()).z);
			original.call(graphics);
			graphics.pose().pushPose();
			graphics.pose().translate(-(graphics.guiWidth() / translation), -(graphics.guiHeight() / translation), 0.0F);
			graphics.pose().scale(scale, scale, 1.0F);
		}
	}

	// TODO - This is a very promising method to get individual HUDs persistent, but I'm not sure if it's bulletproof!
	// It doesn't crash with Sodium nor ImmediatelyFast though, and that's good
	@WrapOperation(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
		),
		allow = 1
	)
	private void ensureDebugHudVisibility(DebugScreenOverlay instance, GuiGraphics graphics, Operation<Void> original) {
		if (OkZoomerConfigManager.CONFIG.features.persistentInterface.value() || !Zoom.getTransitionMode().getActive()) {
			original.call(instance, graphics);
		} else {
			var lastPose = graphics.pose().last().pose();
			graphics.pose().popPose();
			graphics.pose().pushPose();
			graphics.pose().translate(0.0F, 0.0F, lastPose.getTranslation(new Vector3f()).z);
			original.call(instance, graphics);
			graphics.pose().pushPose();
			graphics.pose().translate(-(graphics.guiWidth() / translation), -(graphics.guiHeight() / translation), 0.0F);
			graphics.pose().scale(scale, scale, 1.0F);
		}
	}
}
