package it.gamejam.truncate.bubblenap.core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MovingObject {

	private double x;
	private double y;
	private int width;
	private int height;
	private double dx;
	private double dy;
	private Image image;
	private double targetX;
	private double targetY;

	private Image transformedImage = null;

	public MovingObject(final int x, final int y, final int width, final int height, final double dx, final double dy,
			final double targetX, final double targetY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		this.setTargetX(targetX);
		this.setTargetY(targetY);
	}

	public boolean collide(final Bubble bubble) {
		double d = Math
				.sqrt(Math.pow(bubble.getX() - (width / 2) - x, 2) + Math.pow(bubble.getY() - (height / 2) - y, 2));
		return d <= bubble.getRadius();
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public int getHeight() {
		return height;
	}

	public Image getImage() {
		return image;
	}

	public double getTargetX() {
		return targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public Image getTransformedImage() {

		if (this instanceof Mosquito) {

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

			return outputImage;
		}
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void setDx(final double dx) {
		this.dx = dx;
	}

	public void setDy(final double dy) {
		this.dy = dy;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public void setImage(final Image image) {
		this.image = image;
	}

	public void setTargetX(final double targetX) {
		this.targetX = targetX;
	}

	public void setTargetY(final double targetY) {
		this.targetY = targetY;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public void updatePosition(final long elapsed) {
		x += dx * elapsed * 1.6;
		y += dy * elapsed * 1.6;
	}
}
