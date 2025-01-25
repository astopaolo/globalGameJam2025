package it.gamejam.truncate.bubblenap.ui.img;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	private static Image creditsBed;

	private static Image credits;
	private static Image creditsPressed;

	private static Image exit;
	private static Image exitPressed;

	private static Image background;
	private static Image background_blur;

	private static Image play;
	private static Image playPressed;

	private static Image titleImage;

	private static Image back;
	private static Image backPressed;

	private static Image playBackPressed;
	private static Image playBackRed;

	private static Image mosquito;

	static {
		try {

			mosquito = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/game/mosquito.png"));

			creditsBed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/CreditsBed.png"));

			background = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Background.png"));
			background_blur = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Background_Blur.png"));

			credits = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Credits.png"));

			creditsPressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Credits_Pressed.png"));

			exit = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Exit.png"));

			exitPressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Exit_Pressed.png"));

			play = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Play.png"));

			playPressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Play_Pressed.png"));

			titleImage = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Title.png"));
			back = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Back.png"));

			backPressed = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("img/menu/Back_Pressed.png"));

		} catch (final IOException e) {

			e.printStackTrace();
		}
	}

	public static Image getCreditsBed() {
		return creditsBed;
	}

	public static Image getImageBack() {
		return back;
	}

	public static Image getImageBackground() {
		return background;
	}

	public static Image getImageBackground_Blur() {
		return background_blur;
	}

	public static Image getImageBackPressed() {
		return backPressed;
	}

	public static Image getImageCredits() {
		return credits;
	}

	public static Image getImageCreditsPressed() {
		return creditsPressed;
	}

	public static Image getImageExit() {
		return exit;
	}

	public static Image getImageExit_Pressed() {
		return exitPressed;
	}

	public static Image getImagePlay() {
		return play;
	}

	public static Image getImagePlayPressed() {

		return playPressed;
	}

	public static Image getImageTitle() {
		return titleImage;
	}

	public static Image getMosquito() {
		return mosquito;
	}

	public static Image getPlayBackPressed() {
		return playBackPressed;
	}

	public static Image getPlayBackWhite() {
		return playBackRed;
	}

}
