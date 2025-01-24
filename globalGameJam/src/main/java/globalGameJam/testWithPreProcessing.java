package globalGameJam;

import javax.sound.sampled.LineUnavailableException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class testWithPreProcessing {
	public static void main(final String[] args) {
		// Define the microphone capture parameters
		int sampleRate = 44100; // Common audio sample rate
		int bufferSize = 1024; // Size of each audio buffer
		int overlap = 0; // Overlap between buffers

		try {
			// Create an AudioDispatcher for capturing audio from the microphone
			AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, overlap);

			// Add a BandPass filter as pre-processing
			BandPass bandPassFilter = new BandPass(800, 2000, sampleRate); // Center at 440Hz with 1000Hz bandwidth
			dispatcher.addAudioProcessor(bandPassFilter);

			// Add a custom AudioProcessor for additional pre-processing
			dispatcher.addAudioProcessor(new AudioProcessor() {
				@Override
				public boolean process(final AudioEvent audioEvent) {
					float[] buffer = audioEvent.getFloatBuffer();
					// Example: Simple gain adjustment (amplify signal)
					for (int i = 0; i < buffer.length; i++) {
						buffer[i] *= 0.8; // Reduce volume to 80%
					}
					return true; // Pass audio to the next processor
				}

				@Override
				public void processingFinished() {
					System.out.println("Custom pre-processing finished.");
				}
			});

			// Add a PitchProcessor to detect the pitch
			PitchDetectionHandler pitchHandler = (pitchDetectionResult, audioEvent) -> {
				float pitch = pitchDetectionResult.getPitch();
				if (pitch != -1) {
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
			System.out.println("Pitch detection with pre-processing started. Press Ctrl+C to stop.");

		} catch (LineUnavailableException e) {
			System.err.println("Error accessing the microphone: " + e.getMessage());
		}
	}
}
