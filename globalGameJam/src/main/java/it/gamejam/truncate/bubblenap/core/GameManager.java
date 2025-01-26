package it.gamejam.truncate.bubblenap.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.LineUnavailableException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
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

	private double maxFrequency = 0;

	private List<MovingObject> toRemove = new ArrayList<>();

	public GameManager() {
		setBubble(new Bubble(100.0, 612, 365, 100.0, 200.0));
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
//		startSound();
		running.set(true);
		gameOver.set(false);
		points = 0;
		deathTimer = 20_000;
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
		// Define the microphone capture parameters
		int sampleRate = 44100; // Common audio sample rate
		int bufferSize = 2092; // Size of each audio buffer
		int overlap = 0; // Overlap between buffers
		List<Float> samples = new ArrayList<>();

		try {
			// Create an AudioDispatcher for capturing audio from the microphone
			AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, overlap);
//			BandPass bandPassFilter = new BandPass(200, 1000, sampleRate); // Center at 440Hz with 1000Hz bandwidth
//			dispatcher.addAudioProcessor(bandPassFilter);
//			dispatcher.addAudioProcessor(new LowPassSP(1000, sampleRate)); // Low-pass filter at 1kHz
//			dispatcher.addAudioProcessor(new HighPass(300, sampleRate)); // High-pass filter at 300Hz
			// Add a PitchProcessor to detect the pitch
			PitchDetectionHandler pitchHandler = (pitchDetectionResult, audioEvent) -> {
				float pitch = pitchDetectionResult.getPitch();
				if ((pitch != -1)) {
					if (pitch < LOWER) {
						pitch = LOWER;
					}
					if (pitch > UPPER) {
						pitch = UPPER;
					}
					samples.add(pitch);
					if (samples.size() > 3) {
						samples.remove(0);
					}
					System.out.println("############################");
					System.out.println("detected pitch: " + pitchDetectionResult.getPitch());
					bubble.setRadius(computeRadius(samples));
//					repaintable.update();
					System.out.println();
				}
			};

			dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_PITCH, // Choose
					// an
					// algorithm
					sampleRate, bufferSize, pitchHandler));

			// Start the dispatcher in a separate thread
			Thread audioThread = new Thread(dispatcher);
			audioThread.start();
		} catch (LineUnavailableException e) {
			System.err.println("Error accessing the microphone: " + e.getMessage());
		}
	}
}
