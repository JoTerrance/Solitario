package Ejercicio007;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SecondDeckTest {
	private SecondDeck secondDeck;
	private Card testCard1;
	private Card testCard2;
	private Card testCard3;

	@BeforeEach
	void setUp() {
		secondDeck = new SecondDeck();
		testCard1 = new Card(new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB), 
							 5, Card.RED, Card.HEARTS);
		testCard2 = new Card(new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB), 
							 8, Card.BLACK, Card.SPADES);
		testCard3 = new Card(new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB), 
							 13, Card.RED, Card.DIAMONDS);
	}

	@Test
	void testConstructor() {
		assertNotNull(secondDeck);
		// El mazo debería estar vacío al inicio
		assertThrows(IndexOutOfBoundsException.class, () -> secondDeck.extractCard());
	}

	@Test
	void testConstants() {
		assertEquals(110, SecondDeck.CARDPOSX);
		assertEquals(20, SecondDeck.CARDPOSY);
	}

	@Test
	void testAddCard() {
		secondDeck.addCard(testCard1);
		
		Card extractedCard = secondDeck.extractCard();
		assertSame(testCard1, extractedCard);
	}

	@Test
	void testAddMultipleCards() {
		secondDeck.addCard(testCard1);
		secondDeck.addCard(testCard2);
		secondDeck.addCard(testCard3);
		
		// extractCard devuelve la última carta añadida
		Card extractedCard = secondDeck.extractCard();
		assertSame(testCard3, extractedCard);
	}

	@Test
	void testExtractCard() {
		secondDeck.addCard(testCard1);
		secondDeck.addCard(testCard2);
		
		// extractCard debe devolver la última carta sin eliminarla
		Card extracted = secondDeck.extractCard();
		assertSame(testCard2, extracted);
		
		// Debería devolver la misma carta si se llama otra vez
		Card extracted2 = secondDeck.extractCard();
		assertSame(testCard2, extracted2);
	}

	@Test
	void testRemoveCard() {
		secondDeck.addCard(testCard1);
		secondDeck.addCard(testCard2);
		secondDeck.addCard(testCard3);
		
		// Verificar que la última carta es testCard3
		assertSame(testCard3, secondDeck.extractCard());
		
		// Remover la última carta
		secondDeck.removeCard();
		
		// Ahora la última debería ser testCard2
		assertSame(testCard2, secondDeck.extractCard());
		
		secondDeck.removeCard();
		assertSame(testCard1, secondDeck.extractCard());
	}

	@Test
	void testRelocateCard() {
		testCard1.x = 500;
		testCard1.y = 400;
		
		secondDeck.addCard(testCard1);
		secondDeck.relocateCard();
		
		assertEquals(SecondDeck.CARDPOSX, testCard1.x);
		assertEquals(SecondDeck.CARDPOSY, testCard1.y);
	}

	@Test
	void testRelocateCardWithMultipleCards() {
		testCard1.x = 100;
		testCard1.y = 100;
		testCard2.x = 200;
		testCard2.y = 200;
		testCard3.x = 300;
		testCard3.y = 300;
		
		secondDeck.addCard(testCard1);
		secondDeck.addCard(testCard2);
		secondDeck.addCard(testCard3);
		
		secondDeck.relocateCard();
		
		// Solo la última carta debería moverse
		assertEquals(100, testCard1.x);
		assertEquals(100, testCard1.y);
		assertEquals(200, testCard2.x);
		assertEquals(200, testCard2.y);
		assertEquals(SecondDeck.CARDPOSX, testCard3.x);
		assertEquals(SecondDeck.CARDPOSY, testCard3.y);
	}

	@Test
	void testShowCard() {
		Graphics mockGraphics = Mockito.mock(Graphics.class);
		ImageObserver mockObserver = Mockito.mock(ImageObserver.class);
		
		secondDeck.addCard(testCard1);
		secondDeck.addCard(testCard2);
		
		secondDeck.showCard(mockGraphics, mockObserver);
		
		// Verificar que se llama draw para cada carta
		Mockito.verify(mockGraphics, Mockito.times(2))
			.drawImage(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), 
					   Mockito.eq(Card.WIDTH), Mockito.eq(Card.HEIGHT), Mockito.eq(mockObserver));
	}

	@Test
	void testShowCardWithEmptyDeck() {
		Graphics mockGraphics = Mockito.mock(Graphics.class);
		ImageObserver mockObserver = Mockito.mock(ImageObserver.class);
		
		// No debería causar error con mazo vacío
		secondDeck.showCard(mockGraphics, mockObserver);
		
		// No debería llamar a drawImage
		Mockito.verify(mockGraphics, Mockito.never())
			.drawImage(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), 
					   Mockito.anyInt(), Mockito.anyInt(), Mockito.any());
	}

	@Test
	void testExtractCardThrowsWhenEmpty() {
		assertThrows(IndexOutOfBoundsException.class, () -> secondDeck.extractCard());
	}

	@Test
	void testRemoveCardThrowsWhenEmpty() {
		assertThrows(IndexOutOfBoundsException.class, () -> secondDeck.removeCard());
	}

	@Test
	void testRelocateCardThrowsWhenEmpty() {
		assertThrows(IndexOutOfBoundsException.class, () -> secondDeck.relocateCard());
	}

	@Test
	void testSequentialOperations() {
		// Añadir cartas
		secondDeck.addCard(testCard1);
		secondDeck.addCard(testCard2);
		
		// Extraer y verificar
		assertEquals(testCard2, secondDeck.extractCard());
		
		// Remover y verificar siguiente
		secondDeck.removeCard();
		assertEquals(testCard1, secondDeck.extractCard());
		
		// Añadir otra carta
		secondDeck.addCard(testCard3);
		assertEquals(testCard3, secondDeck.extractCard());
		
		// Reubicar
		secondDeck.relocateCard();
		assertEquals(SecondDeck.CARDPOSX, testCard3.x);
		assertEquals(SecondDeck.CARDPOSY, testCard3.y);
	}
}
