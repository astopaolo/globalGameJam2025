package it.gamejam.truncate.bubblenap.core;

import java.awt.Image;

import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class Mosquito extends MovingObject {
	public Mosquito(final int x, final int y, final double dx, final double dy) {
		super(x, y, 10, 10, dx, dy);
		Image mosquitoImage = ImageLoader.getMosquito();

		setImage(mosquitoImage);
	}

}
