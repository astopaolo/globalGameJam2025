package it.gamejam.truncate.bubblenap.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SamplePlayer {

	public static void main(String[] args) {
		SamplePlayer player = new SamplePlayer();
		player.setSamples(new ArrayList<Sample>(Arrays.asList(new Sample(1010), new Sample(2000))));
		player.start();

	}

	private List<Sample> samples;

	public List<Sample> getSamples() {
		return samples;
	}

	protected void playSample(Sample sample) {
		System.err.println(sample.getStartTime());
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
		Collections.sort(this.samples);
	}

	public void start() {
		long start = System.currentTimeMillis();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (!samples.isEmpty()) {
					long current = System.currentTimeMillis();
					System.out.println(current - start);
					if (current - start >= samples.get(0).getStartTime()) {
						playSample(samples.remove(0));
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(runnable).start();
	}

}
