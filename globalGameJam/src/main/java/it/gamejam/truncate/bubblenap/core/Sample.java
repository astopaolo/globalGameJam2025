package it.gamejam.truncate.bubblenap.core;

public class Sample implements Comparable<Sample> {
	private long startTime;

	public Sample(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public int compareTo(Sample o) {
		return Long.compare(startTime, o.startTime);
	}

	public long getStartTime() {
		return startTime;
	}
}
