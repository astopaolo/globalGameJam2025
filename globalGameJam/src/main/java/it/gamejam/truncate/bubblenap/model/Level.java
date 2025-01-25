package it.gamejam.truncate.bubblenap.model;

import java.util.List;

public class Level {
	
	private String base;

	private Integer bpm;

	private List<Double> pitches;

	private List<Entity> entities;

	public Level(String base, Integer bpm, List<Double> pitches, List<Entity> entities) {
		this.base = base;
		this.bpm = bpm;
		this.pitches = pitches;
		this.entities = entities;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public Integer getBpm() {
		return bpm;
	}

	public void setBpm(Integer bpm) {
		this.bpm = bpm;
	}

	public List<Double> getPitches() {
		return pitches;
	}

	public void setPitches(List<Double> pitches) {
		this.pitches = pitches;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

}
