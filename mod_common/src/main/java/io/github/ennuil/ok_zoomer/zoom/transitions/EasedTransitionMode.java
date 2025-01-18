package io.github.ennuil.ok_zoomer.zoom.transitions;

import net.minecraft.util.Mth;

public class EasedTransitionMode implements TransitionMode {
	private boolean active;
	private int ticks;
	private int targetTicks;
	private float lastZoomMultiplier;
	private float targetZoomMultiplier;
	private float targetFadeMultiplier;
	private float internalMultiplier;
	private float lastInternalMultiplier;
	private float internalFade;
	private float lastInternalFade;
	private boolean finished;

	public EasedTransitionMode() {
		this.active = false;
		this.ticks = 0;
		this.lastZoomMultiplier = 1.0F;
		this.targetZoomMultiplier = 1.0F;
		this.targetFadeMultiplier = 0.0F;
		this.internalMultiplier = 1.0F;
		this.lastInternalMultiplier = 1.0F;
		this.internalFade = 0.0F;
		this.lastInternalFade = 0.0F;
		this.finished = false;
	}

	@Override
	public boolean getActive() {
		return this.active || this.ticks != this.targetTicks;
	}

	@Override
	public float applyZoom(float fov, float tickDelta) {
		return fov * Mth.lerp(tickDelta, this.lastInternalMultiplier, this.internalMultiplier);
	}

	@Override
	public float getFade(float tickDelta) {
		return Mth.lerp(tickDelta, this.lastInternalFade, this.internalFade);
	}

	// Once logic is finished? Throw this mess into a profiler
	@Override
	public void tick(boolean active, double divisor) {
		float zoomMultiplier = (float) (1.0 / divisor);
		float fadeMultiplier = active ? 1.0F : 0.0F;

		this.targetTicks = this.finished ? 5 : active ? 10 : 15;

		if (this.ticks < this.targetTicks) {
			this.ticks += 1;
		}

		if (this.active != active) {
			this.ticks = 0;
			this.finished = false;
			if (!active) {
				this.targetZoomMultiplier = this.internalMultiplier;
				this.targetFadeMultiplier = this.internalFade;
			}
		}

		this.lastInternalMultiplier = this.internalMultiplier;
		this.lastInternalFade = this.internalFade;

		double progress = 1.0 - Math.pow(2.0, -10.0 * (this.ticks / (double) this.targetTicks));

		if (active) {
			if (this.ticks == this.targetTicks) {
				this.targetZoomMultiplier = zoomMultiplier;
				this.targetFadeMultiplier = 1.0F;
				if (this.lastZoomMultiplier != (float) divisor) {
					this.ticks = 0;
					this.finished = true;
				}
			} else {
				if (!finished) {
					this.targetZoomMultiplier = 1.0F;
					this.targetFadeMultiplier = 0.0F;
				}
			}

			this.internalMultiplier = (float) Mth.lerp(progress, this.targetZoomMultiplier, zoomMultiplier);
			this.internalFade = (float) Mth.lerp(progress, this.targetFadeMultiplier, fadeMultiplier);
		} else {
			this.internalMultiplier = (float) Mth.lerp(progress, this.targetZoomMultiplier, 1.0);
			this.internalFade = (float) Mth.lerp(progress, this.targetFadeMultiplier, 0.0);
		}

		this.lastZoomMultiplier = (float) divisor;

		this.active = active;
	}

	@Override
	public double getInternalMultiplier() {
		return this.internalMultiplier;
	}
}
