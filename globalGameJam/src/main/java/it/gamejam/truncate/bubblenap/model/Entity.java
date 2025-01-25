package it.gamejam.truncate.bubblenap.model;

public class Entity {

	private Integer id;

	private Integer startMeasure;

	private Integer startSub;

	private Integer endMeasure;

	private Integer endSub;

	private Integer pitchIndex;

	public Entity(Integer id, Integer startMeasure, Integer startSub, Integer endMeasure, Integer endSub,
			Integer pitchIndex) {
		this.id = id;
		this.startMeasure = startMeasure;
		this.startSub = startSub;
		this.endMeasure = endMeasure;
		this.endSub = endSub;
		this.pitchIndex = pitchIndex;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStartMeasure() {
		return startMeasure;
	}

	public void setStartMeasure(Integer startMeasure) {
		this.startMeasure = startMeasure;
	}

	public Integer getStartSub() {
		return startSub;
	}

	public void setStartSub(Integer startSub) {
		this.startSub = startSub;
	}

	public Integer getEndMeasure() {
		return endMeasure;
	}

	public void setEndMeasure(Integer endMeasure) {
		this.endMeasure = endMeasure;
	}

	public Integer getEndSub() {
		return endSub;
	}

	public void setEndSub(Integer endSub) {
		this.endSub = endSub;
	}

	public Integer getPitchIndex() {
		return pitchIndex;
	}

	public void setPitchIndex(Integer pitchIndex) {
		this.pitchIndex = pitchIndex;
	}

}
