package it.gamejam.truncate.bubblenap.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.LineUnavailableException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import it.gamejam.truncate.bubblenap.ui.Repaintable;

public class GameManager {
	private Bubble bubble;
	private Repaintable repaintable;
	private List<MovingObject> objects = new CopyOnWriteArrayList<>();

	private final float LOWER = 30;

	private final float UPPER = 500;

	private AtomicBoolean running = new AtomicBoolean(false);
	private AtomicBoolean gameOver = new AtomicBoolean(false);

	private int level = 1;
	private long points = 0;
	private long deathTimer = 0;

	private double minFrequency = 0;

	public void setMinFrequency(double minFrequency) {
		this.minFrequency = minFrequency;
		if (bubble != null) {
			bubble.setMinFrequency(minFrequency);
		}
	}

	private double maxFrequency = 0;

	public void setMaxFrequency(double maxFrequency) {
		this.maxFrequency = maxFrequency;
		if (bubble != null) {
			bubble.setMaxFrequency(maxFrequency);
		}
	}

	public GameManager() {
		setBubble(new Bubble(100.0, 578, 380, 50.0, 160.0));
	}

	public void addMovingObject(MovingObject mo) {
		getObjects().add(mo);
	}

	private double computeRadius(final List<Double> list) {
//		double d = ((samples.getLast() - LOWER) / (UPPER - LOWER)) - 0.5;
//		double d = (samples.stream().mapToDouble(f -> f).average().getAsDouble() - LOWER) / UPPER - 1;

//		final double lastSample = Math.min(Math.max(LOWER, samples.getLast()), UPPER);
		final double avg = list.stream().mapToDouble(e -> e).average().orElse(0);
		final double lastSample = Math.min(Math.max(LOWER, avg), UPPER);
//		double d = ((samples.getLast() - LOWER) / (UPPER - LOWER)) - 0.5;
		double d = ((avg - LOWER) / (UPPER - LOWER)) - 0.5;

//		final double retVal = ((lastSample - bubble.getMinFrequency()) * (bubble.getMaxRadius() - bubble.getMinRadius())
//				/ (bubble.getMaxFrequency() - bubble.getMinFrequency())) + bubble.getMinRadius();
		final double retVal = lastSample + (d * 30);

//		System.out.println("min f: " + bubble.getMinFrequency());
//		System.out.println("max f: " + bubble.getMaxFrequency());
//		System.out.println("samples.getLast(): " + samples.getLast());
//		System.out.println("avg: " + avg);
//		System.out.println("lastSample: " + lastSample);
//		System.out.println("retVAl: " + retVal);

//		double retVal = Math.max(bubble.getMinRadius(), Math.min(bubble.getMaxRadius(), bubble.getRadius() + (d * 69)));
		return retVal;
	}

	private void gameOver() {
		System.out.println("GameManager.gameOver()");
		running.set(false);
		gameOver.set(true);
	}

	public Bubble getBubble() {
		return bubble;
	}

	public List<MovingObject> getObjects() {
		return objects;
	}

	public Repaintable getRepaintable() {
		return repaintable;
	}

	public boolean isGameOver() {
		return gameOver.get();
	}

	public void setBubble(final Bubble bubble) {
		this.bubble = bubble;
	}

	public void setRepaintable(final Repaintable repaintable) {
		this.repaintable = repaintable;
	}

	public void startGame() {
		startSound();
		running.set(true);
		gameOver.set(false);
		points = 0;
		deathTimer = 15_000;
		long rate = 1000 / 100;
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				long last = -1;
				while (running.get()) {
					if (last == -1) {
						last = System.currentTimeMillis();
						continue;
					}
					long elapsed = System.currentTimeMillis() - last;
					getObjects().forEach(t -> t.updatePosition(elapsed));
					getObjects().forEach(o -> {
						if (o.collide(bubble)) {
							gameOver();
						}
					});
					repaintable.update();
					last += elapsed;
					points += elapsed;
					deathTimer -= elapsed;
					try {
						Thread.sleep(rate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(updater).start();
		SamplePlayer player = new SamplePlayer();
		player.setGameManager(this);
		player.loadLevel(level++);
//		player.setSamples(new ArrayList<Sample>(Arrays.asList(new Sample(1000), new Sample(2000), new Sample(3000),
//				new Sample(4000), new Sample(5000))));
		player.start();

	}

	private void startSound() {

		RealTimePitchDetector rpd;
		try {
			rpd = new RealTimePitchDetector();
			rpd.start();
			Thread audioThread = new Thread(
					() -> {
						while(true) {
							double pitch = rpd.readPitch();
							System.out.println(pitch);
							if(pitch <= 2) continue;
//							double radius = computeRadius(List.of(pitch));
							bubble.setRadius(pitch);
						}
					}
				);
				audioThread.setDaemon(true);
				audioThread.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
