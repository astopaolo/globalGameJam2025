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

			g.fillRect(o.getX(), o.getY(), 10, 10);

			Graphics2D g2d = (Graphics2D) o.getImage().getGraphics();

			// Make a backup so that we can reset our graphics object after using it.
			// AffineTransform backup = g2d.getTransform();

			int imageWidth = o.getImage().getWidth(null);
			int imageHeight = o.getImage().getHeight(null);

			// Center of the image
			int imageCenterX = imageWidth / 2;
			int imageCenterY = imageHeight / 2;

			// Create an AffineTransform for rotation
			AffineTransform transform = new AffineTransform();
			// Translate to the center of the image
			transform.translate(imageCenterX, imageCenterY);

//			double angle = Math.atan2(o.getTargetY() - imageCenterY, o.getTargetX() - imageCenterX);
			double angle = Math.PI;

			// Rotate around the center
			transform.rotate(angle);
			// Translate back to draw the image properly
			// transform.translate(-imageWidth / 2, -imageHeight / 2);
			g2d.setTransform(transform);
			// Draw the rotated image

			g2d.drawImage(o.getImage(), o.getX(), o.getY(), null);

			g2d.dispose();
		});

		if (gameManager.isGameOver()) {
			g.drawImage(ImageLoader.getGameover(), 0, 0, getWidth(), getHeight(), null);
		}
	}

}
