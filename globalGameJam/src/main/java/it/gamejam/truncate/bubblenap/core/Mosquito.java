package it.gamejam.truncate.bubblenap.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Mosquito extends MovingObject {
	public Mosquito() {
		super(0, 0, 10, 10, 1, 1);
		BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(3, 3, 5, 5);
		setImage(image);
	}

}
