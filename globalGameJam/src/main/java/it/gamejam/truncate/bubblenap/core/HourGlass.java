package it.gamejam.truncate.bubblenap.core;

import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class HourGlass extends MovingObject {

	private boolean applied = false;

	public HourGlass(final int x, final int y, final double dx, final double dy, final double targetX,
			final double targetY) {
		super(x, y, 10, 10, dx, dy, targetX, targetY);
		setImage(ImageLoader.getHourGlass());
	}

	@Override
	protected void applyEffect(GameManager gameManager) {
		if (!applied) {
			gameManager.addTimerSeconds(5);
			gameManager.markToRemove(this);
			applied = true;
		}
	}

}
