package globalGameJam;

import javax.sound.sampled.LineUnavailableException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class testMicrophone {
	public static void main(final String[] args) {
		// Define the microphone capture parameters
		int sampleRate = 44100; // Common audio sample rate
		int bufferSize = 1024; // Size of each audio buffer
		int overlap = 100; // Overlap between buffers

		try {
			// Create an AudioDispatcher for capturing audio from the microphone
			AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, overlap);

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
			System.out.println("Pitch detection started. Press Ctrl+C to stop.");

		} catch (LineUnavailableException e) {
			System.err.println("Error accessing the microphone: " + e.getMessage());
		}
	}
}
