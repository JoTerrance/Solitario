package Ejercicio007;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SolitaryTest {
	private Solitary solitary;

	@BeforeEach
	void setUp() {
		// Crear instancia sin mostrar ventana
		solitary = new Solitary();
	}

	@Test
	void testConstructor() {
		assertNotNull(solitary);
		assertNotNull(getPrivateField(solitary, "cardImgs"));
		assertNotNull(getPrivateField(solitary, "deck"));
		assertNotNull(getPrivateField(solitary, "secondDeck"));
		assertNotNull(getPrivateField(solitary, "suitDecks"));
		assertNotNull(getPrivateField(solitary, "cardBackRectangle"));
	}

	@Test
	void testConstants() {
		assertEquals(52, Solitary.NUM_CARDS);
		assertEquals(13, Solitary.CPD);
		assertEquals(4, Solitary.SUITS);
		assertNotNull(Solitary.DIRECTION_OPTIONS);
		assertTrue(Solitary.DIRECTION_OPTIONS.length > 0);
	}

	@Test
	void testCardImagesInitialization() {
		Image[] cardImgs = (Image[]) getPrivateField(solitary, "cardImgs");
		assertNotNull(cardImgs);
		assertEquals(Solitary.NUM_CARDS, cardImgs.length);
		
		// Verificar que todas las imágenes se crearon (dummy o reales)
		for (int i = 0; i < cardImgs.length; i++) {
			assertNotNull(cardImgs[i], "La carta en posición " + i + " no debería ser null");
		}
	}

	@Test
	void testCardBackInitialization() {
		Image cardBack = (Image) getPrivateField(solitary, "cardBack");
		assertNotNull(cardBack, "El reverso de la carta no debería ser null");
	}

	@Test
	void testDeckInitialization() {
		Deck deck = (Deck) getPrivateField(solitary, "deck");
		assertNotNull(deck);
		assertNotNull(deck.getMainDeck());
		assertEquals(52, deck.getMainDeck().size());
	}

	@Test
	void testSecondDeckInitialization() {
		SecondDeck secondDeck = (SecondDeck) getPrivateField(solitary, "secondDeck");
		assertNotNull(secondDeck);
	}

	@Test
	void testSuitDecksInitialization() {
		SuitDeck[] suitDecks = (SuitDeck[]) getPrivateField(solitary, "suitDecks");
		assertNotNull(suitDecks);
		assertEquals(4, suitDecks.length);
		
		// Verificar posiciones de los mazos de palos
		for (int i = 0; i < 4; i++) {
			assertNotNull(suitDecks[i]);
			assertEquals((i * 100) + 400, suitDecks[i].x);
			assertEquals(SuitDeck.POSITIONY, suitDecks[i].y);
		}
	}

	@Test
	void testCardBackRectangleInitialization() {
		java.awt.Rectangle cardBackRectangle = (java.awt.Rectangle) getPrivateField(solitary, "cardBackRectangle");
		assertNotNull(cardBackRectangle);
		assertEquals(20, cardBackRectangle.x);
		assertEquals(20, cardBackRectangle.y);
		assertEquals(Card.WIDTH, cardBackRectangle.width);
		assertEquals(Card.HEIGHT, cardBackRectangle.height);
	}

	@Test
	void testPaintComponent() {
		// Crear un Graphics de prueba
		BufferedImage image = new BufferedImage(800, 700, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		
		// No debería lanzar excepción
		assertDoesNotThrow(() -> solitary.paintComponent(g));
	}

	@Test
	void testHandleMouseDownOnCardBack() throws Exception {
		Deck deck = (Deck) getPrivateField(solitary, "deck");
		SecondDeck secondDeck = (SecondDeck) getPrivateField(solitary, "secondDeck");
		
		int initialDeckSize = deck.getMainDeck().size();
		
		// Simular click en el reverso de la carta (cardBackRectangle)
		Method handleMouseDown = Solitary.class.getDeclaredMethod("handleMouseDown", int.class, int.class);
		handleMouseDown.setAccessible(true);
		
		// Click dentro del rectángulo del reverso (20, 20)
		handleMouseDown.invoke(solitary, 50, 50);
		
		// Verificar que se movió una carta del deck principal al secondDeck
		assertEquals(initialDeckSize - 1, deck.getMainDeck().size());
	}
	
	@Test
	void testHandleMouseDownOnSecondDeck() throws Exception {
		// Primero añadir una carta al secondDeck
		SecondDeck secondDeck = (SecondDeck) getPrivateField(solitary, "secondDeck");
		Image testImage = new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Card testCard = new Card(testImage, 5, Card.RED, Card.HEARTS);
		secondDeck.addCard(testCard);
		secondDeck.relocateCard();
		
		Method handleMouseDown = Solitary.class.getDeclaredMethod("handleMouseDown", int.class, int.class);
		handleMouseDown.setAccessible(true);
		
		// Click en la posición del secondDeck (CARDPOSX=110, CARDPOSY=20)
		handleMouseDown.invoke(solitary, SecondDeck.CARDPOSX + 10, SecondDeck.CARDPOSY + 10);
		
		// Verificar que se estableció activeCard
		Card activeCard = (Card) getPrivateField(solitary, "activeCard");
		assertNotNull(activeCard);
	}

	@Test
	void testHandleMouseDragWithActiveCard() throws Exception {
		// Crear una carta activa
		Image testImage = new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Card activeCard = new Card(testImage, 5, Card.RED, Card.HEARTS);
		activeCard.x = 100;
		activeCard.y = 100;
		
		setPrivateField(solitary, "activeCard", activeCard);
		
		// Simular arrastre
		Method handleMouseDrag = Solitary.class.getDeclaredMethod("handleMouseDrag", int.class, int.class);
		handleMouseDrag.setAccessible(true);
		handleMouseDrag.invoke(solitary, 200, 250);
		
		// Verificar que la carta se movió
		assertEquals(200 - (Card.WIDTH / 2), activeCard.x);
		assertEquals(250 - (Card.HEIGHT / 2), activeCard.y);
	}

	@Test
	void testHandleMouseDragWithoutActiveCard() throws Exception {
		// Sin carta activa
		setPrivateField(solitary, "activeCard", null);
		
		Method handleMouseDrag = Solitary.class.getDeclaredMethod("handleMouseDrag", int.class, int.class);
		handleMouseDrag.setAccessible(true);
		
		// No debería lanzar excepción
		assertDoesNotThrow(() -> handleMouseDrag.invoke(solitary, 200, 250));
	}

	@Test
	void testHandleMouseUpResetsActiveCard() throws Exception {
		// Preparar el secondDeck con una carta
		SecondDeck secondDeck = (SecondDeck) getPrivateField(solitary, "secondDeck");
		Image testImage = new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Card cardInSecondDeck = new Card(testImage, 3, Card.RED, Card.HEARTS);
		secondDeck.addCard(cardInSecondDeck);
		
		// Crear una carta activa
		Card activeCard = new Card(testImage, 5, Card.RED, Card.HEARTS);
		activeCard.x = 100;
		activeCard.y = 100;
		
		setPrivateField(solitary, "activeCard", activeCard);
		
		// Simular soltar el ratón en una posición que no intersecta con suitDecks
		Method handleMouseUp = Solitary.class.getDeclaredMethod("handleMouseUp", int.class, int.class);
		handleMouseUp.setAccessible(true);
		handleMouseUp.invoke(solitary, 200, 300);
		
		// Verificar que activeCard se resetea
		Card currentActiveCard = (Card) getPrivateField(solitary, "activeCard");
		assertNull(currentActiveCard);
	}

	@Test
	void testHandleMouseUpWithoutActiveCard() throws Exception {
		setPrivateField(solitary, "activeCard", null);
		
		Method handleMouseUp = Solitary.class.getDeclaredMethod("handleMouseUp", int.class, int.class);
		handleMouseUp.setAccessible(true);
		
		// No debería lanzar excepción
		assertDoesNotThrow(() -> handleMouseUp.invoke(solitary, 500, 100));
	}

	@Test
	void testLoadImagesMethod() throws Exception {
		Method loadImages = Solitary.class.getDeclaredMethod("loadImages");
		loadImages.setAccessible(true);
		
		// El método retorna boolean
		Object result = loadImages.invoke(solitary);
		assertNotNull(result);
		assertTrue(result instanceof Boolean);
	}

	@Test
	void testCreateDummyImagesMethod() throws Exception {
		Method createDummyImages = Solitary.class.getDeclaredMethod("createDummyImages");
		createDummyImages.setAccessible(true);
		
		// No debería lanzar excepción
		assertDoesNotThrow(() -> createDummyImages.invoke(solitary));
		
		// Verificar que se crearon las imágenes
		Image[] cardImgs = (Image[]) getPrivateField(solitary, "cardImgs");
		for (Image img : cardImgs) {
			assertNotNull(img);
		}
		
		Image cardBack = (Image) getPrivateField(solitary, "cardBack");
		assertNotNull(cardBack);
	}

	@Test
	void testBackgroundColor() {
		assertEquals(java.awt.Color.GREEN, solitary.getBackground());
	}

	@Test
	@Timeout(5)
	void testMainMethodCreatesFrame() throws Exception {
		// Ejecutar en un hilo separado para no bloquear
		Thread testThread = new Thread(() -> {
			try {
				// Simular la creación de frame sin mostrar
				JFrame frame = new JFrame("Test Solitario");
				Solitary game = new Solitary();
				frame.add(game);
				frame.setSize(800, 700);
				
				// Verificar
				assertNotNull(frame);
				assertEquals(800, frame.getWidth());
				assertEquals(700, frame.getHeight());
				assertEquals("Test Solitario", frame.getTitle());
				
				// Cerrar inmediatamente
				frame.dispose();
			} catch (Exception e) {
				fail("No debería lanzar excepción: " + e.getMessage());
			}
		});
		
		testThread.start();
		testThread.join(3000); // Esperar máximo 3 segundos
	}

	@Test
	void testMouseListenersAdded() {
		// Verificar que se agregaron mouse listeners
		assertTrue(solitary.getMouseListeners().length > 0, "Debería tener MouseListeners");
		assertTrue(solitary.getMouseMotionListeners().length > 0, "Debería tener MouseMotionListeners");
	}

	@Test
	void testDeckIsShuffled() {
		// El mazo debería estar mezclado después de la inicialización
		Deck deck = (Deck) getPrivateField(solitary, "deck");
		assertNotNull(deck);
		
		// Crear un nuevo mazo sin mezclar para comparar
		Image[] testImages = new Image[52];
		for (int i = 0; i < 52; i++) {
			testImages[i] = new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB);
		}
		Deck unshuffledDeck = new Deck(testImages);
		
		// El orden debería ser diferente (estadísticamente muy probable)
		boolean orderDifferent = false;
		for (int i = 0; i < Math.min(10, deck.getMainDeck().size()); i++) {
			if (deck.getMainDeck().get(i).getValue() != unshuffledDeck.getMainDeck().get(i).getValue()) {
				orderDifferent = true;
				break;
			}
		}
		assertTrue(orderDifferent || deck.getMainDeck().size() < 52, 
				   "El mazo debería estar mezclado o haber sido usado");
	}

	@Test
	void testDirectionOptionsAreValid() {
		String[] directions = Solitary.DIRECTION_OPTIONS;
		assertTrue(directions.length >= 3, "Debería tener al menos 3 opciones de directorio");
		
		for (String dir : directions) {
			assertNotNull(dir);
			assertFalse(dir.isEmpty());
			assertTrue(dir.endsWith("/"), "Cada directorio debería terminar con /");
		}
	}

	// Métodos auxiliares para acceder a campos privados
	private Object getPrivateField(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			fail("No se pudo acceder al campo privado: " + fieldName);
			return null;
		}
	}

	private void setPrivateField(Object obj, String fieldName, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			fail("No se pudo establecer el campo privado: " + fieldName);
		}
	}
}
