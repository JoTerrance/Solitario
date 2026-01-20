package Ejercicio007;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;


public class Solitary extends JPanel {
	// Primero intenta cargar desde src/main/resources, luego desde ruta absoluta
	public static final String[] DIRECTION_OPTIONS = {
		"src/main/resources/imagenes/Cartas/",
		"imagenes/Cartas/",
		"C:/imagenes/Cartas/",
		System.getProperty("user.home") + "/imagenes/Cartas/"
	};
	private String names[] = { "_of_clubs.png", "_of_diamonds.png", "_of_hearts.png", "_of_spades.png" };
    public static final int NUM_CARDS = 52;
	public static final int CPD = 13;
	public static final int SUITS = 4;

    Image imagen;
    Graphics hidden;
	Image cardImgs[];
	Image cardBack;
	Deck deck;
	Rectangle cardBackRectangle;
	SecondDeck secondDeck;
	Card activeCard;
	SuitDeck[] suitDecks;
	private boolean imagesLoaded = false;
    
    public Solitary() {
		setBackground(Color.GREEN);
		
		// Cargar imágenes
		cardImgs = new Image[NUM_CARDS];
		imagesLoaded = loadImages();
		
		if (!imagesLoaded) {
			System.out.println("==============================================");
			System.out.println("No se encontraron imágenes de cartas.");
			System.out.println("Para jugar con imágenes, colócalas en una de estas rutas:");
			for (String dir : DIRECTION_OPTIONS) {
				System.out.println("  - " + dir);
			}
			System.out.println("\nFormato esperado: 1_of_clubs.png, 2_of_clubs.png, etc.");
			System.out.println("También necesitas: reverso.png");
			System.out.println("\nEl juego continuará sin imágenes (modo texto).");
			System.out.println("==============================================");
			
			// Crear imágenes dummy
			createDummyImages();
		}
		
		cardBackRectangle = new Rectangle(20, 20, Card.WIDTH, Card.HEIGHT);
		deck = new Deck(cardImgs);
		deck.shuffle();
		secondDeck = new SecondDeck();
		suitDecks = new SuitDeck[SUITS];
		for (int i = 0; i < SUITS; i++)
			suitDecks[i] = new SuitDeck((i * 100) + 400);

		// Event listeners
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseDown(e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseUp(e.getX(), e.getY());
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				handleMouseDrag(e.getX(), e.getY());
			}
		});
    }
	
	private boolean loadImages() {
		for (String baseDir : DIRECTION_OPTIONS) {
			try {
				// Intentar cargar una imagen de prueba
				File testFile = new File(baseDir + "1" + names[0]);
				if (!testFile.exists()) {
					continue;
				}
				
				// Si existe, cargar todas las imágenes
				for (int i = 0; i < SUITS; i++) {
					for(int j = 0; j < CPD; j++) {
						String fileName = baseDir + (j + 1) + names[i];
						cardImgs[(i * CPD) + j] = ImageIO.read(new File(fileName));
					}
				}
				cardBack = ImageIO.read(new File(baseDir + "reverso.png"));
				System.out.println("Imágenes cargadas desde: " + baseDir);
				return true;
			} catch (IOException e) {
				// Intentar siguiente ruta
			}
		}
		return false;
	}
	
	private void createDummyImages() {
		// Crear imágenes simples con colores
		for (int i = 0; i < NUM_CARDS; i++) {
			cardImgs[i] = new java.awt.image.BufferedImage(
				Card.WIDTH, Card.HEIGHT, java.awt.image.BufferedImage.TYPE_INT_RGB);
			Graphics g = cardImgs[i].getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, Card.WIDTH, Card.HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, Card.WIDTH - 1, Card.HEIGHT - 1);
			
			// Dibujar número de carta
			int suit = i / CPD;
			int value = (i % CPD) + 1;
			String valueStr = value == 1 ? "A" : value == 11 ? "J" : value == 12 ? "Q" : value == 13 ? "K" : String.valueOf(value);
			g.setColor(suit == 1 || suit == 2 ? Color.RED : Color.BLACK);
			g.drawString(valueStr, 10, 20);
		}
		
		// Crear reverso
		cardBack = new java.awt.image.BufferedImage(
			Card.WIDTH, Card.HEIGHT, java.awt.image.BufferedImage.TYPE_INT_RGB);
		Graphics g = cardBack.getGraphics();
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, Card.WIDTH, Card.HEIGHT);
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, Card.WIDTH - 1, Card.HEIGHT - 1);
	}

    @Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// suitDecks rectangles
		for (int i = 0; i < SUITS; i++)
			suitDecks[i].draw(g, this);
		// Deck to take cards
		if (cardBack != null)
			g.drawImage(cardBack, 20, 20, Card.WIDTH, Card.HEIGHT, this);
		// Deck where you show cards
		secondDeck.showCard(g, this);
		if (activeCard != null)
			activeCard.draw(g, this);
    }

	private void handleMouseDown(int x, int y) {
		if (cardBackRectangle.contains(x, y)) {
			secondDeck.addCard(deck.takeCard());
			secondDeck.relocateCard();
			repaint();
		}
		if (secondDeck.extractCard().contains(x, y)) {
			activeCard = secondDeck.extractCard();
		}
	}

	private void handleMouseDrag(int x, int y) {
		if (activeCard != null) {
			activeCard.x = x - (Card.WIDTH / 2);
			activeCard.y = y - (Card.HEIGHT / 2);
			repaint();
		}
	}

	private void handleMouseUp(int x, int y) {
		if (activeCard != null) {
			for (int i = 0; i < SUITS; i++)
				if (activeCard.intersects(suitDecks[i]))
					if (suitDecks[i].addSuitCard(activeCard)) {
						secondDeck.removeCard();
					}
			secondDeck.relocateCard();
			activeCard = null;
			repaint();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Solitario");
		Solitary game = new Solitary();
		
		frame.add(game);
		frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}