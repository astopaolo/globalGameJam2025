package it.gamejam.truncate.bubblenap.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

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
		setBackground(Color.WHITE);
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

	public void startGame() {
		gameManager.startGame();
	}

	@Override
	public void update() {
		repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {
//		System.out.println("GamePanel.paintComponent() " + bubble.getRadius());
		super.paintComponent(g);
		g.fillOval(bubble.getX() - (int) bubble.getRadius(), bubble.getY() - (int) bubble.getRadius(),
				(int) bubble.getRadius() * 2, (int) bubble.getRadius() * 2);
		gameManager.getObjects().forEach(o -> {

			Graphics2D g2d = (Graphics2D) g;

			// Make a backup so that we can reset our graphics object after using it.
			AffineTransform backup = g2d.getTransform();
			// rx is the x coordinate for rotation, ry is the y coordinate for rotation, and
			// angle
			// is the angle to rotate the image. If you want to rotate around the center of
			// an image,
			// use the image's center x and y coordinates for rx and ry.
			AffineTransform a = AffineTransform.getRotateInstance(45, o.getWidth() / 2.0, o.getHeight() / 2.0);
			// Set our Graphics2D object to the transform
			g2d.setTransform(a);
			// Draw our image like normal
			g2d.drawImage(o.getImage(), o.getX(), o.getY(), null);
			// Reset our graphics object so we can draw with it again.
			g2d.setTransform(backup);

			// g2d.rotate(o.getRotationAngle(), o.getWidth() / 2, o.getHeight() / 2);

			// g2d.drawImage(o.getImage(), (o.getX() - o.getWidth()) / 2, (o.getY() -
			// o.getHeight()) / 2, null);

		});

		if (gameManager.isGameOver()) {
			g.drawImage(ImageLoader.getGameover(), 0, 0, getWidth(), getHeight(), null);
		}
	}

}
