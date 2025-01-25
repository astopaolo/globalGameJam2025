package it.gamejam.truncate.bubblenap.core;

import java.util.ArrayList;
import java.util.List;

public class Level {
	private String base;
	private int bpm;
	private double[] pitches;
	private List<Sample> entities = new ArrayList<>();

	public String getBase() {
		return base;
	}

	public int getBpm() {
		return bpm;
	}

	public List<Sample> getEntities() {
		return entities;
	}

	public double[] getPitches() {
		return pitches;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public void setEntities(List<Sample> entities) {
		this.entities = entities;
	}

	public void setPitches(double[] pitches) {
		this.pitches = pitches;
	}

}
