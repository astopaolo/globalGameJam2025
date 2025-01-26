package it.gamejam.truncate.bubblenap.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.LineUnavailableException;

import it.gamejam.truncate.bubblenap.ui.Repaintable;

public class GameManager {
	private static final double INITIAL_BUBBLE_RADIUS = 100.0;
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

	private double maxFrequency = 0;

	private List<MovingObject> toRemove = new ArrayList<>();
	private RealTimePitchDetector rpd;

	public GameManager() {
		setBubble(new Bubble(INITIAL_BUBBLE_RADIUS, 612, 365, 50.0, 300.0));
		try {
			rpd = new RealTimePitchDetector();
			rpd.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void addMovingObject(MovingObject mo) {
		getObjects().add(mo);
	}

	public void addTimerSeconds(int i) {
		deathTimer += i * 1000;
	}

	private double computeRadius(final List<Float> samples) {
//		double d = ((samples.getLast() - LOWER) / (UPPER - LOWER)) - 0.5;
//		double d = (samples.stream().mapToDouble(f -> f).average().getAsDouble() - LOWER) / UPPER - 1;

		final double lastSample = Math.min(Math.max(LOWER, samples.getLast()), UPPER);
		double d = ((samples.getLast() - LOWER) / (UPPER - LOWER)) - 0.5;

//		final double retVal = ((lastSample - bubble.getMinFrequency()) * (bubble.getMaxRadius() - bubble.getMinRadius())
//				/ (bubble.getMaxFrequency() - bubble.getMinFrequency())) + bubble.getMinRadius();
		final double retVal = lastSample + (d * 15);

		System.out.println("min f: " + bubble.getMinFrequency());
		System.out.println("max f: " + bubble.getMaxFrequency());
		System.out.println("samples.getLast(): " + samples.getLast());
		System.out.println("lastSample: " + lastSample);
		System.out.println("retVAl: " + retVal);

//		double retVal = Math.max(bubble.getMinRadius(), Math.min(bubble.getMaxRadius(), bubble.getRadius() + (d * 69)));
		return retVal;
	}

	public void gameOver() {
		System.out.println("GameManager.gameOver()");
		running.set(false);
		gameOver.set(true);
	}

	public Bubble getBubble() {
		return bubble;
	}

	public long getDeathTimer() {
		return deathTimer;
	}

	public List<MovingObject> getObjects() {
		return objects;
	}

	public long getPoints() {
		return points;
	}

	public Repaintable getRepaintable() {
		return repaintable;
	}

	public boolean isGameOver() {
		return gameOver.get();
	}

	public void markToRemove(MovingObject object) {
		toRemove.add(object);
	}

	public void setBubble(final Bubble bubble) {
		this.bubble = bubble;
	}

	public void setMaxFrequency(double maxFrequency) {
		this.maxFrequency = maxFrequency;
		if (bubble != null) {
			bubble.setMaxFrequency(maxFrequency);
		}
	}

	public void setMinFrequency(double minFrequency) {
		this.minFrequency = minFrequency;
		if (bubble != null) {
			bubble.setMinFrequency(minFrequency);
		}
	}

	public void setRepaintable(final Repaintable repaintable) {
		this.repaintable = repaintable;
	}

	public void startGame() {
		startSound();
		running.set(true);
		gameOver.set(false);
		points = 0;
		deathTimer = 20_000;
		bubble.setRadius(INITIAL_BUBBLE_RADIUS);
		objects.clear();
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
							o.applyEffect(GameManager.this);
						}
					});
					objects.removeAll(toRemove);
					toRemove.clear();
					repaintable.update();
					last += elapsed;
					points += elapsed;
					deathTimer -= elapsed;
					if (deathTimer <= 0) {
						running.set(false);
						gameOver.set(true);
					}
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
		player.loadLevel(level);
//		player.setSamples(new ArrayList<Sample>(Arrays.asList(new Sample(1000), new Sample(2000), new Sample(3000),
//				new Sample(4000), new Sample(5000))));
		player.start();

	}

	private void startSound() {

		Thread audioThread = new Thread(() -> {
			while (running.get()) {
				double pitch = rpd.readPitch();
//							System.out.println(pitch);
				if (pitch <= 2) {
					continue;
				}
//							double radius = computeRadius(List.of(pitch));
				bubble.setRadius(pitch);
			}
		});
		audioThread.setDaemon(true);
		audioThread.start();
	}
}
