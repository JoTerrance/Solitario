package Ejercicio007;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeckTest {
	private Deck deck;
	private Image[] testImages;

	@BeforeEach
	void setUp() {
		// Crear imágenes de prueba
		testImages = new Image[52];
		for (int i = 0; i < 52; i++) {
			testImages[i] = new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB);
		}
	}

	@Test
	void testConstructorWithNull() {
		assertThrows(NullPointerException.class, () -> {new Deck(null);});
	}
	
	@Test
	void testConstructorWithImages() {
		Deck deck2 = new Deck(testImages);
		assertEquals(52, deck2.getMainDeck().size());
		
		// Verificar que las cartas se crearon correctamente
		Card firstCard = deck2.getMainDeck().get(0);
		assertNotNull(firstCard);
		assertEquals(1, firstCard.getValue()); // Primera carta es As
		assertEquals(Card.BLACK, firstCard.getColor()); // Primeras 13 son negras (picas)
	}
	
	@Test
	void testConstructorCreatesCorrectCards() {
		Deck deck = new Deck(testImages);
		ArrayList<Card> cards = deck.getMainDeck();
		
		// Verificar valores de las cartas (1-13 se repite 4 veces)
		for (int i = 0; i < 52; i++) {
			int expectedValue = (i % 13) + 1;
			assertEquals(expectedValue, cards.get(i).getValue());
		}
		
		// Verificar colores: picas(0) y tréboles(3) son negras, diamantes(1) y corazones(2) son rojas
		assertEquals(Card.BLACK, cards.get(0).getColor()); // Picas
		assertEquals(Card.RED, cards.get(13).getColor()); // Diamantes
		assertEquals(Card.RED, cards.get(26).getColor()); // Corazones
		assertEquals(Card.BLACK, cards.get(39).getColor()); // Tréboles
	}
	
	@Test
	void testTakeCard() {
		deck = new Deck();
		ArrayList<Card> mainDeck = new ArrayList<Card>();
		Card card = new Card(null, 1, Card.RED, Card.HEARTS);
		mainDeck.add(card);
		deck.setMainDeck(mainDeck);
		
		Card takenCard = deck.takeCard();
		assertEquals(card, takenCard);
		assertEquals(1, takenCard.getValue());
		assertEquals(Card.RED, takenCard.getColor());
		assertEquals(Card.HEARTS, takenCard.getSuit());
		assertSame(card, takenCard);
		assertTrue(deck.getMainDeck().isEmpty());
	}
	
	@Test
	void testTakeCardReducesDeckSize() {
		Deck deck = new Deck(testImages);
		int initialSize = deck.getMainDeck().size();
		
		deck.takeCard();
		assertEquals(initialSize - 1, deck.getMainDeck().size());
		
		deck.takeCard();
		assertEquals(initialSize - 2, deck.getMainDeck().size());
	}
	
	@Test
	void testDraw() {
		Deck deck = new Deck(testImages);
		int initialSize = deck.getMainDeck().size();
		
		Card drawnCard = deck.draw();
		assertNotNull(drawnCard);
		assertEquals(initialSize - 1, deck.getMainDeck().size());
	}
	
	@Test
	void testShuffle() {
		Deck deck = new Deck(testImages);
		ArrayList<Card> originalOrder = new ArrayList<>(deck.getMainDeck());
		
		deck.shuffle();
		
		// Verificar que el tamaño no cambió
		assertEquals(52, deck.getMainDeck().size());
		
		// Verificar que el orden cambió (estadísticamente muy probable)
		boolean orderChanged = false;
		for (int i = 0; i < 52; i++) {
			if (originalOrder.get(i) != deck.getMainDeck().get(i)) {
				orderChanged = true;
				break;
			}
		}
		assertTrue(orderChanged, "El mazo debería estar mezclado");
	}
	
	@Test
	void testGetAndSetMainDeck() {
		deck = new Deck();
		ArrayList<Card> newDeck = new ArrayList<Card>();
		Card card1 = new Card(null, 5, Card.RED, Card.DIAMONDS);
		Card card2 = new Card(null, 10, Card.BLACK, Card.SPADES);
		newDeck.add(card1);
		newDeck.add(card2);
		
		deck.setMainDeck(newDeck);
		assertEquals(newDeck, deck.getMainDeck());
		assertEquals(2, deck.getMainDeck().size());
	}
}
