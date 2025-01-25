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

	public MovingObject(int x, int y, int width, int height, double dx, double dy) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
	}

	public boolean collide(Bubble bubble) {
		double d = Math.sqrt(Math.pow(bubble.getX() - width / 2 - x, 2) + Math.pow(bubble.getY() - height / 2 - y, 2));
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

	public int getWidth() {
		return width;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void updatePosition(long elapsed) {
		x += dx * elapsed * 1.6;
		y += dy * elapsed * 1.6;
	}
}
