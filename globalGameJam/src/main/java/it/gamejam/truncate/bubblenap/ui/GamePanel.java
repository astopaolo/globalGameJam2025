package it.gamejam.truncate.bubblenap.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import it.gamejam.truncate.bubblenap.core.Bubble;
import it.gamejam.truncate.bubblenap.core.GameManager;
import it.gamejam.truncate.bubblenap.ui.img.ImageLoader;

public class GamePanel extends JPanel implements Repaintable {
	private static final long serialVersionUID = 1L;
	private final Bubble bubble;
	private final GameManager gameManager;

	public GamePanel(final GameManager gameManager) {
		this.gameManager = gameManager;
		this.bubble = gameManager.getBubble();
		setPreferredSize(new Dimension(1200, 800));
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
//		g.drawImage(ImageLoader.getBollaMuco(), bubble.getX() - 10, bubble.getY() - 10, 20, 20, null);
		g.drawImage(ImageLoader.getBollaMuco(), bubble.getX() - (int) bubble.getRadius(),
				bubble.getY() - (int) bubble.getRadius(), (int) bubble.getRadius() * 2, (int) bubble.getRadius() * 2,
				null);
		gameManager.getObjects().forEach(o -> g.drawImage(o.getTransformedImage(), o.getX(), o.getY(), null));

		if (gameManager.isGameOver()) {
			g.drawImage(ImageLoader.getGameover(), 0, 0, getWidth(), getHeight(), null);
		}
	}

	public void startGame() {
		gameManager.startGame();
	}

	@Override
	public void update() {
		repaint();
	}

}
