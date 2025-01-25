package it.gamejam.truncate.bubblenap.core;

public class Sample implements Comparable<Sample> {
	private long startTime;
	private int id;
	private int startMeasure;
	private int startSub;
	private int endMeasure;
	private int endSub;
	private int pitchIndex;

	public Sample() {
		// TODO Auto-generated constructor stub
	}

	public Sample(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public int compareTo(Sample o) {
		return Long.compare(startTime, o.startTime);
	}

	public int getEndMeasure() {
		return endMeasure;
	}

	public int getEndSub() {
		return endSub;
	}

	public int getId() {
		return id;
	}

	public int getPitchIndex() {
		return pitchIndex;
	}

	public int getStartMeasure() {
		return startMeasure;
	}

	public int getStartSub() {
		return startSub;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setEndMeasure(int endMeasure) {
		this.endMeasure = endMeasure;
	}

	public void setEndSub(int endSub) {
		this.endSub = endSub;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPitchIndex(int pitchIndex) {
		this.pitchIndex = pitchIndex;
	}

	public void setStartMeasure(int startMeasure) {
		this.startMeasure = startMeasure;
	}

	public void setStartSub(int startSub) {
		this.startSub = startSub;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setup(int bpm) {
		double x = 60.0 / bpm;
		startTime = (long) ((startMeasure * x * 4 + startSub * x / 2) * 1000);
	}
}
