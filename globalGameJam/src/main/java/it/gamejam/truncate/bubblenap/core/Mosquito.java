package it.gamejam.truncate.bubblenap.core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class Mosquito extends MovingObject {
	private Image transformedImage = null;

	public Mosquito(final int x, final int y, final double dx, final double dy, final double targetX,
			final double targetY) {
		super(x, y, 10, 10, dx, dy, targetX, targetY);
		Image mosquitoImage = ImageLoader.getMosquito();

		setImage(mosquitoImage);
	}

	@Override
	protected void applyEffect(GameManager gameManager) {
		gameManager.gameOver();
	}

	@Override
	public Image getTransformedImage() {

		if (transformedImage != null) {
			return transformedImage;
		}

		int width = image.getWidth(null);
		int height = image.getHeight(null);

		double rotationAngle = (Math.PI / 2) + Math.atan2(targetY - y, targetX - x);

		int newWidth = (int) Math.abs(width * Math.cos(rotationAngle))
				+ (int) Math.abs(height * Math.sin(rotationAngle));
		int newHeight = (int) Math.abs(height * Math.cos(rotationAngle))
				+ (int) Math.abs(width * Math.sin(rotationAngle));

		BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		AffineTransform transform = new AffineTransform();
		transform.rotate(rotationAngle, newWidth / 2, newHeight / 2);

		transform.translate((newWidth - width) / 2, (newHeight - height) / 2);

		Graphics2D g2d = outputImage.createGraphics();
		g2d.setTransform(transform);
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		transformedImage = outputImage;
		this.width = newWidth;
		this.height = newHeight;

		return transformedImage;

	}
}
