package it.gamejam.truncate.bubblenap.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import it.gamejam.truncate.bubblenap.core.Bubble;
import it.gamejam.truncate.bubblenap.core.GameManager;
import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class GamePanel extends JPanel implements Repaintable {
	private static final long serialVersionUID = 1L;
	private final Bubble bubble;
	private final GameManager gameManager;
	private Font font;
	private MainFrame mainFrame;

	private AtomicBoolean startedGameOverThread = new AtomicBoolean(false);

	public GamePanel(final GameManager gameManager, final MainFrame frame) {
		this.gameManager = gameManager;
		this.mainFrame = frame;
		this.bubble = gameManager.getBubble();
		setPreferredSize(new Dimension(1280, 768));
		setBackground(Color.DARK_GRAY);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A: {
					bubble.setRadius(bubble.getRadius() - 15);
					break;
				}
				case KeyEvent.VK_S: {
					bubble.setRadius(bubble.getRadius() + 15);
					break;
				}
				case KeyEvent.VK_ESCAPE: {
					System.exit(0);
				}
				}
				repaint();
			}
		});

	}

	@Override
	protected void paintComponent(final Graphics g) {
//		System.out.println("GamePanel.paintComponent() " + bubble.getRadius());
		super.paintComponent(g);
		g.drawImage(ImageLoader.getGameScreen(), 0, 0, getWidth(), getHeight(), null);
		if (gameManager.isGameOver()) {

			g.drawImage(ImageLoader.getBollaMucoScoppiata(), bubble.getX() - (int) bubble.getRadius(),
					bubble.getY() - (int) bubble.getRadius(), (int) bubble.getRadius() * 2,
					(int) bubble.getRadius() * 2, null);
			if (!startedGameOverThread.get()) {
				startedGameOverThread.set(true);
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(700);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mainFrame.drawPanel(EnumPanel.GAME_OVER_VIDEO_PANEL);
					};
				}.start();
			}
		}
		g.drawImage(ImageLoader.getBollaMuco(), bubble.getX() - (int) bubble.getRadius(),
				bubble.getY() - (int) bubble.getRadius(), (int) bubble.getRadius() * 2, (int) bubble.getRadius() * 2,
				null);
		g.setColor(Color.RED);
		gameManager.getObjects().forEach(o -> {
			g.drawImage(o.getTransformedImage(), o.getX(), o.getY(), null);
//			g.drawRect(o.getX(), o.getY(), o.getWidth(), o.getHeight());
		});
		g.setColor(Color.CYAN);
		g.setFont(font);
		g.drawString("Points " + gameManager.getPoints(), 50, 50);
		g.drawString("Time " + (gameManager.getDeathTimer() / 1000), getWidth() - 350, 50);
	}

	public void startGame() {
		startedGameOverThread.set(false);
		gameManager.startGame();
		if (font == null) {
			try {
				final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/bubblegums.ttf")));
				font = new Font("bubblegums", Font.BOLD, 32);
			} catch (IOException | FontFormatException e) {
				// IGNORE
			}
		}

	}

	@Override
	public void update() {
		repaint();
	}

}
