package it.gamejam.truncate.bubblenap.core;

import java.awt.Image;

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
