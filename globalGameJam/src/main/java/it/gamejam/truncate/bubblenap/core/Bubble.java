package it.gamejam.truncate.bubblenap.core;

public class Bubble {

	private double radius = 100;
	private int x;
	private int y;

	public Bubble(double radius, int x, int y) {
		super();
		this.radius = radius;
		this.x = x;
		this.y = y;
	}

	public double getRadius() {
		return radius;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
