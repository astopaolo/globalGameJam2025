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

	private List<Sample> samples;

	private GameManager gameManager;
	private List<int[]> points;

	private final Random random = new Random();

	private Map<String, byte[]> audioSamples;

	private Level level;

	private double[] radiusArray;

	public List<Sample> getSamples() {
		return samples;
	}

	public void loadLevel(final int levelNumber) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			level = mapper.readValue(new File("resources/levels/" + levelNumber + "/level_" + levelNumber + ".json"),
					Level.class);
			samples = level.getEntities();
			samples.forEach(s -> s.setup(level.getBpm()));
			audioSamples = SoundProvider.getSamples(new File("resources/levels/" + levelNumber));
			double[] pitches = level.getPitches();

			radiusArray = new double[pitches.length];

			radiusArray[0] = gameManager.getBubble().getMinRadius();
			radiusArray[pitches.length - 1] = gameManager.getBubble().getMaxRadius();

			double deltaRadius = gameManager.getBubble().getMaxRadius() - gameManager.getBubble().getMinRadius();
			double radiusStep = deltaRadius / pitches.length;

			for (int i = 1; i < (pitches.length - 1); ++i) {
				radiusArray[i] = gameManager.getBubble().getMinRadius() + (radiusStep * i);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setGameManager(final GameManager gameManager) {
		this.gameManager = gameManager;
		points = getPointAtDistance(gameManager.getBubble().getX(), gameManager.getBubble().getY(), 500);
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
					new SimpleAudioPlayer(audioSamples.get(level.getBase()), 3f).playLoop();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (!samples.isEmpty()) {
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
			}
		};
		new Thread(runnable).start();
	}

	private List<double[]> findTangentPoints(final double circleX, final double circleY, final double radius,
			final double pointX, final double pointY) {
		List<double[]> tangentPoints = new ArrayList<>();

		// Calcola la distanza dal punto al centro della circonferenza
		double correction = 2.6;
		double distance = Math.sqrt(Math.pow(pointX - circleX, 2) + Math.pow(pointY - circleY, 2));

		// Controlla se il punto è esterno alla circonferenza
		if (distance < radius) {
			return tangentPoints; // Nessun punto di tangenza
		}

		// Calcola l'angolo tra il punto e il centro della circonferenza
		double angleToPoint = Math.atan2(pointY - circleY, pointX - circleX);

		// Calcola l'angolo di offset per i punti di tangenza
		double offsetAngle = Math.asin(radius / distance);

		// Calcola i due angoli dei punti di tangenza
		double angle1 = angleToPoint + offsetAngle;
		double angle2 = angleToPoint - offsetAngle;

		// Calcola le coordinate dei punti di tangenza
		double tangentX1 = circleX + (radius * Math.cos(angle1) * correction);
		double tangentY1 = circleY + (radius * Math.sin(angle1) * correction);

		double tangentX2 = circleX + (radius * Math.cos(angle2) * correction);
		double tangentY2 = circleY + (radius * Math.sin(angle2) * correction);

		tangentPoints.add(new double[] { tangentX1, tangentY1 });
		tangentPoints.add(new double[] { tangentX2, tangentY2 });

		return tangentPoints;
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

	private void playSample(final Sample sample) {
		System.err.println(sample.getStartTime());
		if (gameManager != null) {
			new Thread() {
				@Override
				public void run() {
					SimpleAudioPlayer.playSyncSoundOnce(audioSamples.get("sample_" + sample.getId() + ".wav"), 3f);
				};
			}.start();
			int[] p = points.get(random.nextInt(points.size()));
			double[] ds = findTangentPoints(gameManager.getBubble().getX(), gameManager.getBubble().getY(),
					radiusArray[sample.getPitchIndex()], p[0], p[1]).get(random.nextInt(2));

			double dx = ds[0] - p[0];
			double dy = ds[1] - p[1];
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			double speed = distance / (sample.getEndTime() - sample.getStartTime());
			double ddx = (dx / distance) * speed;
			double ddy = (dy / distance) * speed;

			System.err.println(Math.atan(dx / distance));

			gameManager.addMovingObject(new Mosquito(p[0], p[1], ddx, ddy, Math.atan(dx / distance)));

		}
	}

}
