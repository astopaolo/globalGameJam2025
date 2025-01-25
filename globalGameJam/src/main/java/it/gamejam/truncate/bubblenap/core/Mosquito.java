package it.gamejam.truncate.bubblenap.core;

import java.awt.Image;

import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class Mosquito extends MovingObject {
	public Mosquito(final int x, final int y, final double dx, final double dy, final double targetX,
			final double targetY) {
		super(x, y, 10, 10, dx, dy, targetX, targetY);
		Image mosquitoImage = ImageLoader.getMosquito();

		setImage(mosquitoImage);
	}

}
