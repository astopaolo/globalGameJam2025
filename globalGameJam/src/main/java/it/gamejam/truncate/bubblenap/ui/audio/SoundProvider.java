package it.gamejam.truncate.bubblenap.ui.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundProvider {
	private static byte[] menu;
	private static byte[] bubbleMenuClick;

	static {
		try {

			try (FileInputStream fis = new FileInputStream("resources/audio/menu.wav")) {
				menu = new byte[fis.available()];
				fis.read(menu);
			}
			try (FileInputStream fis = new FileInputStream("resources/audio/bubbleMenuClick.wav")) {
				bubbleMenuClick = new byte[fis.available()];
				fis.read(bubbleMenuClick);
			}

		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static byte[] getBubbleMenuClick() {
		return bubbleMenuClick;
	}

	public static byte[] getMenu() {
		return menu;
	}

	public static List<byte[]> getSamples(final File dir) throws IOException {
		final List<byte[]> samples = new ArrayList<>();
		final File[] sampleFiles = dir.listFiles();
		for (final File file : sampleFiles) {
			try (FileInputStream fis = new FileInputStream(file)) {
				final byte[] tmp = new byte[fis.available()];
				fis.read(tmp);
				samples.add(tmp);
			}
		}
		return samples;
	}

}
