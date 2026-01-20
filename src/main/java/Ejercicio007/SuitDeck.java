package Ejercicio007;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class SuitDeck extends Rectangle {
	java.util.ArrayList<Card> deck;
	public static final int POSITIONY = 20;
	int suit;

	public SuitDeck(int posx) {
		super(posx, POSITIONY, Card.WIDTH, Card.HEIGHT);
		deck = new java.util.ArrayList<Card>();
	}

	public void draw(Graphics g, ImageObserver observer) {
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		for (int i = 0; i < deck.size(); i++)
			deck.get(i).draw(g, observer);
	}

	public void relocateSuitCard() {
		deck.get(deck.size() - 1).x = x;
		deck.get(deck.size() - 1).y = y;
	}

	public boolean addSuitCard(Card suitCard) {
		if((deck.size() == 0) && (suitCard.getValue() == 1)) {
			deck.add(suitCard);
			relocateSuitCard();
			suit = suitCard.getSuit();
			return true;
		}
		if ((deck.size() != 0) && (suitCard.getSuit() == suit)
				&& (suitCard.getValue() == deck.get(deck.size() - 1).getValue() + 1))
		return false;
		return false;
	}
}
