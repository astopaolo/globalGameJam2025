package it.gamejam.truncate.bubblenap.core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import com.github.psambit9791.jdsp.transform.FastFourier;

public class RealTimePitchDetector {

	 private static final int SAMPLE_RATE = 44100; // Audio sample rate
	    private static final int FRAME_SIZE = 2048;   // Size of audio frame for processing
	    private static final double YIN_THRESHOLD = 0.1; // Threshold for YIN pitch detection
	    private static final double SILENCE_THRESHOLD = 0.02; //Threshold for silence
	    
	    private final AudioFormat FORMAT;
	    private final DataLine.Info INFO;
	    private final TargetDataLine MICROPHONE;

	    private final byte[] DATA_BUFFER = new byte[FRAME_SIZE * 2]; // Buffer to hold microphone data
	    private final double[] AUDIO_FRAME = new double[FRAME_SIZE]; // Audio frame for processing
	    
	    public RealTimePitchDetector() throws LineUnavailableException {
            this.FORMAT = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
            this.INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
            this.MICROPHONE = (TargetDataLine) AudioSystem.getLine(this.INFO);
	    }
	    
	    public double readPitch() {
	    	 // Read audio data into buffer
            MICROPHONE.read(DATA_BUFFER, 0, DATA_BUFFER.length);

            // Convert byte data to double (normalize values between -1 and 1)
            for (int i = 0; i < FRAME_SIZE; i++) {
            	AUDIO_FRAME[i] = ((DATA_BUFFER[2 * i] << 8) | (DATA_BUFFER[2 * i + 1] & 0xFF)) / 32768.0;
            }

            if(isSilent(AUDIO_FRAME)) return -1;

            // Detect pitch using the YIN algorithm
            double detectedPitch = detectPitchYin(AUDIO_FRAME);
            
            
            return detectedPitch;
	    }

	    public void start() {
	        try {
	        	MICROPHONE.open(FORMAT);
	        	MICROPHONE.start();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void stop() {
	    	MICROPHONE.close();
	    }

	    /**
	     * Perform FFT using jDSP and return the magnitude spectrum.
	     */
	    private static double[] performFFT(double[] audioFrame) {
	        FastFourier fft = new FastFourier(audioFrame);
	        fft.transform(); // Execute FFT transform
	        return fft.getMagnitude(true); // Return magnitude spectrum
	    }

	    /**
	     * Detect pitch using the YIN algorithm.
	     */
	    private static double detectPitchYin(double[] audioFrame) {
	        int tau = -1; // Lag variable
	        double[] difference = computeDifference(audioFrame);
	        double[] normalizedDifference = cumulativeMeanNormalizedDifference(difference);

	        // Find the first minimum below the threshold
	        for (int t = 2; t < normalizedDifference.length; t++) {
	            if (normalizedDifference[t] < YIN_THRESHOLD) {
	                tau = t;
	                break;
	            }
	        }

	        if (tau == -1) {
	            // No pitch detected
	            return -1;
	        }

	        // Refine the pitch estimation using parabolic interpolation
	        tau = parabolicInterpolation(normalizedDifference, tau);

	        // Convert the lag (period) to frequency
	        return SAMPLE_RATE / tau;
	    }

	    /**
	     * Step 1: Compute the difference function for the YIN algorithm.
	     */
	    private static double[] computeDifference(double[] signal) {
	        int length = signal.length;
	        double[] difference = new double[length];
	        for (int tau = 0; tau < length; tau++) {
	            for (int i = 0; i < length - tau; i++) {
	                difference[tau] += Math.pow(signal[i] - signal[i + tau], 2);
	            }
	        }
	        return difference;
	    }

	    /**
	     * Step 2: Compute the cumulative mean normalized difference function for the YIN algorithm.
	     */
	    private static double[] cumulativeMeanNormalizedDifference(double[] difference) {
	        int length = difference.length;
	        double[] normalized = new double[length];
	        double sum = 0;
	        normalized[0] = 1; // First value is undefined, set to 1 by convention
	        for (int tau = 1; tau < length; tau++) {
	            sum += difference[tau];
	            normalized[tau] = difference[tau] / ((1.0 / tau) * sum);
	        }
	        return normalized;
	    }

	    /**
	     * Step 3: Refine the pitch estimation using parabolic interpolation.
	     */
	    private static int parabolicInterpolation(double[] normalizedDifference, int tau) {
	        if (tau <= 0 || tau >= normalizedDifference.length - 1) {
	            return tau; // No interpolation possible
	        }
	        double x0 = normalizedDifference[tau - 1];
	        double x1 = normalizedDifference[tau];
	        double x2 = normalizedDifference[tau + 1];
	        double a = (x0 - 2 * x1 + x2) / 2.0;
	        double b = (x2 - x0) / 2.0;
	        if (a == 0) {
	            return tau;
	        }
	        return (int) (tau - b / (2 * a));
	    }
	    
	    public static double calculateRMS(double[] audioFrame) {
	        double sum = 0.0;
	        for (double sample : audioFrame) {
	            sum += sample * sample; // Square each sample
	        }
	        return Math.sqrt(sum / audioFrame.length); // Square root of mean
	    }
	    
	    public static boolean isSilent(double[] audioFrame) {
	        double rmsValue = calculateRMS(audioFrame);
	        System.out.println("rms: " + rmsValue);
	        return rmsValue < SILENCE_THRESHOLD;
	    }
	    
	}
