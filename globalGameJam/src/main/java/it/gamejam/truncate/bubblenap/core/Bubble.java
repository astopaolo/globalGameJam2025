package it.gamejam.truncate.bubblenap.core;

public class Bubble {

	private double radius = 100;
	private int x;
	private int y;
	private double minRadius;
	private double maxRadius;
	private double minFrequency;
	private double maxFrequency;

	public double getMinFrequency() {
		return minFrequency;
	}

	public void setMinFrequency(double minFrequency) {
		this.minFrequency = minFrequency;
	}

	public double getMaxFrequency() {
		return maxFrequency;
	}

	public void setMaxFrequency(double maxFrequency) {
		this.maxFrequency = maxFrequency;
	}

	public Bubble(double radius, int x, int y, double minRadius, double maxRadius, double minFrequency,
			double maxFrequency) {
		super();
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
		this.minFrequency = minFrequency;
		this.maxFrequency = maxFrequency;
	}

	public Bubble(double radius, int x, int y, double minRadius, double maxRadius) {
		this(radius, x, y, minRadius, maxRadius, 0, 0);
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

	public double getMinRadius() {
		return minRadius;
	}

	public double getMaxRadius() {
		return maxRadius;
	}

	public void setMinRadius(double minRadius) {
		this.minRadius = minRadius;
	}

	public void setMaxRadius(double maxRadius) {
		this.maxRadius = maxRadius;
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
