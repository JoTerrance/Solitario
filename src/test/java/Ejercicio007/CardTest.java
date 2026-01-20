package Ejercicio007;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CardTest {
	private Image testImage;
	private Card card;

	@BeforeEach
	void setUp() {
		testImage = new BufferedImage(Card.WIDTH, Card.HEIGHT, BufferedImage.TYPE_INT_RGB);
		card = new Card(testImage, 5, Card.RED, Card.HEARTS);
	}

	@Test
	void testConstructor() {
		assertNotNull(card);
		assertEquals(testImage, card.getImage());
		assertEquals(5, card.getValue());
		assertEquals(Card.RED, card.getColor());
		assertEquals(Card.HEARTS, card.getSuit());
		
		// Verificar posición y tamaño iniciales
		assertEquals(200, card.x);
		assertEquals(200, card.y);
		assertEquals(Card.WIDTH, card.width);
		assertEquals(Card.HEIGHT, card.height);
	}

	@Test
	void testConstants() {
		assertEquals(70, Card.WIDTH);
		assertEquals(120, Card.HEIGHT);
		assertEquals(1, Card.RED);
		assertEquals(2, Card.BLACK);
		assertEquals(0, Card.SPADES);
		assertEquals(1, Card.DIAMONDS);
		assertEquals(2, Card.HEARTS);
		assertEquals(3, Card.CLUBS);
	}

	@Test
	void testGetAndSetImage() {
		Image newImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		card.setImage(newImage);
		assertEquals(newImage, card.getImage());
	}

	@Test
	void testGetAndSetValue() {
		card.setValue(10);
		assertEquals(10, card.getValue());
		
		card.setValue(1); // As
		assertEquals(1, card.getValue());
		
		card.setValue(13); // Rey
		assertEquals(13, card.getValue());
	}

	@Test
	void testGetAndSetColor() {
		card.setColor(Card.BLACK);
		assertEquals(Card.BLACK, card.getColor());
		
		card.setColor(Card.RED);
		assertEquals(Card.RED, card.getColor());
	}

	@Test
	void testGetAndSetSuit() {
		card.setSuit(Card.SPADES);
		assertEquals(Card.SPADES, card.getSuit());
		
		card.setSuit(Card.DIAMONDS);
		assertEquals(Card.DIAMONDS, card.getSuit());
		
		card.setSuit(Card.CLUBS);
		assertEquals(Card.CLUBS, card.getSuit());
	}

	@Test
	void testDraw() {
		Graphics mockGraphics = Mockito.mock(Graphics.class);
		ImageObserver mockObserver = Mockito.mock(ImageObserver.class);
		
		card.x = 100;
		card.y = 150;
		
		card.draw(mockGraphics, mockObserver);
		
		// Verificar que se llamó al método drawImage con los parámetros correctos
		Mockito.verify(mockGraphics).drawImage(testImage, 100, 150, Card.WIDTH, Card.HEIGHT, mockObserver);
	}

	@Test
	void testInheritanceFromRectangle() {
		// Verificar que Card hereda de Rectangle y puede usar sus métodos
		card.x = 50;
		card.y = 75;
		
		assertTrue(card.contains(60, 85));
		assertFalse(card.contains(10, 10));
		
		Card otherCard = new Card(testImage, 7, Card.BLACK, Card.SPADES);
		otherCard.x = 55;
		otherCard.y = 80;
		
		assertTrue(card.intersects(otherCard));
	}

	@Test
	void testDifferentCardValues() {
		// Probar con As
		Card aceCard = new Card(testImage, 1, Card.RED, Card.HEARTS);
		assertEquals(1, aceCard.getValue());
		
		// Probar con figura (Jota)
		Card jackCard = new Card(testImage, 11, Card.BLACK, Card.SPADES);
		assertEquals(11, jackCard.getValue());
		
		// Probar con Rey
		Card kingCard = new Card(testImage, 13, Card.RED, Card.DIAMONDS);
		assertEquals(13, kingCard.getValue());
	}

	@Test
	void testAllSuits() {
		Card spadesCard = new Card(testImage, 5, Card.BLACK, Card.SPADES);
		assertEquals(Card.SPADES, spadesCard.getSuit());
		
		Card diamondsCard = new Card(testImage, 5, Card.RED, Card.DIAMONDS);
		assertEquals(Card.DIAMONDS, diamondsCard.getSuit());
		
		Card heartsCard = new Card(testImage, 5, Card.RED, Card.HEARTS);
		assertEquals(Card.HEARTS, heartsCard.getSuit());
		
		Card clubsCard = new Card(testImage, 5, Card.BLACK, Card.CLUBS);
		assertEquals(Card.CLUBS, clubsCard.getSuit());
	}
}
