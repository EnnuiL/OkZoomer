package io.github.ennuil.ok_zoomer.zoom.overlays;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ennuil.ok_zoomer.zoom.transitions.TransitionMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

// Implements the zoom overlay
public class ZoomerZoomOverlay implements ZoomOverlay {
	private final ResourceLocation textureId;
	private boolean active;

	public ZoomerZoomOverlay(ResourceLocation textureId) {
		this.textureId = textureId;
		this.active = false;
	}

	@Override
	public boolean getActive() {
		return this.active;
	}

	@Override
	public void renderOverlay(GuiGraphics graphics, TransitionMode transitionMode) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		float fade = transitionMode.getFade(Minecraft.getInstance().getFrameTime());
		graphics.setColor(fade, fade, fade, 1.0F);
		graphics.blit(this.textureId, 0, 0, -90, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight());
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.defaultBlendFunc();
	}

	@Override
	public void tick(boolean active, double divisor, TransitionMode transitionMode) {
		if (active || !transitionMode.getActive()) {
			this.active = active;
		}
	}
}
