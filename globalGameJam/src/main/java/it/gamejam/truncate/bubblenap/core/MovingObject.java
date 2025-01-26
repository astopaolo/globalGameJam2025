package it.gamejam.truncate.bubblenap.core;

import java.awt.Image;

public abstract class MovingObject {

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected double dx;
	protected double dy;
	protected Image image;
	protected double targetX;
	protected double targetY;

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

	protected abstract void applyEffect(GameManager gameManager);

	public boolean collide(final Bubble bubble) {
		double d = Math
				.sqrt(Math.pow(bubble.getX() - (x + (width / 2)), 2) + Math.pow(bubble.getY() - (y + (height / 2)), 2));
		return d <= bubble.getRadius() + (width + height) / 16.0;
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

	public double getTargetX() {
		return targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public Image getTransformedImage() {

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
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
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
