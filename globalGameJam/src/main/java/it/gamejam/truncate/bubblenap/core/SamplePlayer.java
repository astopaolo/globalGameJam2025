package it.gamejam.truncate.bubblenap.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.gamejam.truncate.bubblenap.ui.audio.SimpleAudioPlayer;
import it.gamejam.truncate.bubblenap.ui.audio.SoundProvider;

public class SamplePlayer {

	public static double calculateAngle(double x1, double y1, double x2, double y2, double x3, double y3) {
		// Vettore BA
		double vectorBAx = x1 - x2;
		double vectorBAy = y1 - y2;

		// Vettore BC
		double vectorBCx = x3 - x2;
		double vectorBCy = y3 - y2;

		// Prodotto scalare dei vettori BA e BC
		double dotProduct = vectorBAx * vectorBCx + vectorBAy * vectorBCy;

		// Lunghezze dei vettori BA e BC
		double magnitudeBA = Math.sqrt(vectorBAx * vectorBAx + vectorBAy * vectorBAy);
		double magnitudeBC = Math.sqrt(vectorBCx * vectorBCx + vectorBCy * vectorBCy);

		// Calcolo del coseno dell'angolo
		double cosTheta = dotProduct / (magnitudeBA * magnitudeBC);

		// Assicurarsi che il valore sia nel range [-1, 1] per evitare errori di calcolo
		cosTheta = Math.max(-1, Math.min(1, cosTheta));

		// Calcolo dell'angolo in radianti e conversione in gradi
		double angleRadians = Math.acos(cosTheta);
		return Math.toDegrees(angleRadians);
	}

	private List<Sample> samples;

	private GameManager gameManager;

	private List<int[]> points;

	private List<double[]> mosquitoPoints = new ArrayList<double[]>();
	private List<double[]> hourglassPoints = new ArrayList<double[]>();
	private final Random random = new Random();
	private Map<String, byte[]> audioSamples;

	private Level level;

//	public void loadLevel(final int levelNumber) {
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			level = mapper.readValue(new File("resources/levels/" + levelNumber + "/level_" + levelNumber + ".json"),
//					Level.class);
//			samples = level.getEntities();
//			samples.forEach(s -> s.setup(level.getBpm()));
//			audioSamples = SoundProvider.getSamples(new File("resources/levels/" + levelNumber));
//			double[] pitches = level.getPitches();
//
//			radiusArray = new double[pitches.length];
//
//			radiusArray[0] = gameManager.getBubble().getMinRadius();
//			radiusArray[pitches.length - 1] = gameManager.getBubble().getMaxRadius();
//
//			double deltaRadius = gameManager.getBubble().getMaxRadius() - gameManager.getBubble().getMinRadius();
//			double radiusStep = deltaRadius / pitches.length;
//
//			for (int i = 1; i < (pitches.length - 1); ++i) {
//				radiusArray[i] = gameManager.getBubble().getMinRadius() + (radiusStep * i);
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	private double[] radiusArray;

	public SamplePlayer() {

	}

	private double[] findTangentPoints(final double circleX, final double circleY, final double radius,
			final double pointX, final double pointY, List<double[]> refPoints) {
		double max = -1000;
		int index = 0;
		int i = 0;
		for (double[] refP : refPoints) {
			double angle = calculateAngle(circleX, circleY, pointX, pointY, refP[0], refP[1]);
			if (angle > max) {
				index = i;
				max = angle;
			}
			i++;
		}
		return refPoints.get(index);
	}

	private List<Sample> generateRandomEntites(
			final int howManyEntitesWouldYouLikeMeToGenerateExactlyPleaseTellMeSoThatICanFulfillYourRequestOk,
			final int maxPitchIndex) {
		final List<Sample> entities = new ArrayList<>();
		int startMeasure = random.nextInt(2);
		for (int i = 0; i < howManyEntitesWouldYouLikeMeToGenerateExactlyPleaseTellMeSoThatICanFulfillYourRequestOk; i++) {

			final Sample sample = new Sample();
			sample.setId(random.nextInt(2));
			sample.setStartMeasure(startMeasure);
			sample.setStartSub(random.nextInt(8));
			sample.setEndMeasure(startMeasure + random.nextInt(2) + 1);
			sample.setEndSub(random.nextInt(8));
			sample.setPitchIndex(random.nextInt(maxPitchIndex));
			entities.add(sample);

			startMeasure += random.nextInt(2) + 1;
		}

		return entities;
	}

	private List<int[]> getPointAtDistance(final int x, final int y, final int distance) {
		List<int[]> validPoints = new ArrayList<>();

		// Genera tutte le combinazioni di punti a distanza fissa
		for (int dx = -distance; dx <= distance; dx++) {
			int dySquared = (distance * distance) - (dx * dx);
			if (dySquared >= 0) {
				int dy = (int) Math.sqrt(dySquared);

				// Aggiungi entrambe le soluzioni positive e negative per dy
				if ((dy * dy) == dySquared) {
					validPoints.add(new int[] { x + dx, y + dy });
					if (dy != 0) { // Evita duplicati quando dy = 0
						validPoints.add(new int[] { x + dx, y - dy });
					}
				}
			}
		}

		return validPoints;
	}

	public List<Sample> getSamples() {
		return samples;
	}

	public void loadLevel(int levelNumber) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			level = mapper.readValue(new File("resources/levels/" + levelNumber + "/level_" + levelNumber + ".json"),
					Level.class);

			double[] pitches = level.getPitches();

//			samples = level.getEntities();
			samples = generateRandomEntites(500, pitches.length);
			samples.forEach(s -> s.setup(level.getBpm()));
			audioSamples = SoundProvider.getSamples(new File("resources/levels/" + levelNumber));

//			double[] pitches = Arrays.stream(level.getPitches()).map(e -> 2 * e).toArray();

			gameManager.setMinFrequency(pitches[0]);
			gameManager.setMaxFrequency(pitches[pitches.length - 1]);

			radiusArray = new double[pitches.length];

			radiusArray[0] = gameManager.getBubble().getMinRadius();
			radiusArray[pitches.length - 1] = gameManager.getBubble().getMaxRadius();

			double deltaRadius = gameManager.getBubble().getMaxRadius() - gameManager.getBubble().getMinRadius();
			double radiusStep = deltaRadius / pitches.length;

			for (int i = 1; i < pitches.length - 1; ++i) {
				radiusArray[i] = gameManager.getBubble().getMinRadius() + radiusStep * i;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void playSample(Sample sample) {
		System.err.println(sample.getStartTime());
		if (gameManager != null) {
			new Thread() {
				@Override
				public void run() {
					SimpleAudioPlayer.playSyncSoundOnce(audioSamples.get("sample_" + sample.getId() + ".wav"), 3f);
				};
			}.start();
			List<double[]> refPoints = new ArrayList<>();
			switch (sample.getId()) {
			case 0:
				refPoints = mosquitoPoints;
				break;
			case 1:
				refPoints = hourglassPoints;
				break;
			}
			int[] p = points.get(random.nextInt(points.size()));
			double[] ds = findTangentPoints(gameManager.getBubble().getX(), gameManager.getBubble().getY(),
					radiusArray[sample.getPitchIndex()], p[0], p[1], refPoints);

			double dx = ds[0] - p[0];
			double dy = ds[1] - p[1];
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			double speed = distance / (sample.getEndTime() - sample.getStartTime());
			double ddx = (dx / distance) * speed;
			double ddy = (dy / distance) * speed;

			System.err.println(Math.atan(dx / distance));

			switch (sample.getId()) {
			case 0:
				gameManager.addMovingObject(new Mosquito(p[0], p[1], ddx, ddy, ds[0], ds[1]));
				break;
			case 1:
				gameManager.addMovingObject(new HourGlass(p[0], p[1], ddx, ddy, ds[0], ds[1]));
				break;
			}

		}
	}

	public void setGameManager(final GameManager gameManager) {
		this.gameManager = gameManager;
		points = getPointAtDistance(gameManager.getBubble().getX(), gameManager.getBubble().getY(), 500);
		Bubble bubble = gameManager.getBubble();
		int mosquitoDelta = 150;
		int hourglassDelta = 250;
		mosquitoPoints.add(new double[] { bubble.getX(), bubble.getY() + mosquitoDelta });
		mosquitoPoints.add(new double[] { bubble.getX() + mosquitoDelta, bubble.getY() });
		mosquitoPoints.add(new double[] { bubble.getX(), bubble.getY() - mosquitoDelta });
		mosquitoPoints.add(new double[] { bubble.getX() - mosquitoDelta, bubble.getY() });

		hourglassPoints.add(new double[] { bubble.getX(), bubble.getY() + hourglassDelta });
		hourglassPoints.add(new double[] { bubble.getX() + hourglassDelta, bubble.getY() });
		hourglassPoints.add(new double[] { bubble.getX(), bubble.getY() - hourglassDelta });
		hourglassPoints.add(new double[] { bubble.getX() - hourglassDelta, bubble.getY() });
	}

	public void setSamples(final List<Sample> samples) {
		this.samples = samples;
		Collections.sort(this.samples);
	}

	public void start() {
		long start = System.currentTimeMillis();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					SimpleAudioPlayer simpleAudioPlayer = new SimpleAudioPlayer(audioSamples.get(level.getBase()), 3f);
					simpleAudioPlayer.playLoop();

					while (!samples.isEmpty() && !gameManager.isGameOver()) {
						long current = System.currentTimeMillis();
//					System.out.println(current - start);
						if ((current - start) >= samples.get(0).getStartTime()) {
							playSample(samples.remove(0));
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					simpleAudioPlayer.stop();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
	}

}
