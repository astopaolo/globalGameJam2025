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

	private final float LOWER = 100;

	private final float UPPER = 500;

	private AtomicBoolean running = new AtomicBoolean(false);

	public GameManager() {
		setBubble(new Bubble(100.0, 200, 200));
	}

	public void addMovingObject(MovingObject mo) {
		getObjects().add(mo);
	}

	private double computeRadius(final List<Float> samples) {
		double d = ((samples.getLast() - LOWER) / (UPPER - LOWER)) - 0.5;
//		double d = (samples.stream().mapToDouble(f -> f).average().getAsDouble() - LOWER) / UPPER - 1;
		System.out.println(d);
		return bubble.getRadius() + (d * 69);
	}

	protected void gameOver() {
		System.out.println("GameManager.gameOver()");
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

	public void setBubble(final Bubble bubble) {
		this.bubble = bubble;
	}

	public void setRepaintable(final Repaintable repaintable) {
		this.repaintable = repaintable;
	}

	public void startGame() {
//		startSound();
		running.set(true);
		long rate = 1000 / 60;
		Runnable updater = new Runnable() {

			@Override
			public void run() {
				System.out.println("GameManager.startGame()");
				while (running.get()) {
					getObjects().forEach(MovingObject::updatePosition);
					getObjects().forEach(o -> {
						if (o.collide(bubble)) {
							gameOver();
						}
					});
					repaintable.update();
					try {
						Thread.sleep(rate);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(updater).start();
		addMovingObject(new Mosquito());
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
					bubble.setRadius(computeRadius(samples));
					repaintable.update();
					System.out.println("Detected pitch: " + pitch + " Hz");
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
