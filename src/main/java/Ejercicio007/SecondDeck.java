package Ejercicio007;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class SecondDeck {
	public static final int CARDPOSX = 110;
	public static final int CARDPOSY = 20;
	private ArrayList<Card> secondDeck;

	public SecondDeck() {
        secondDeck = new ArrayList<Card>();
	}

	public void addCard(Card cardAdded) {
		secondDeck.add(cardAdded);
	}

	public Card extractCard() {
		return secondDeck.get(secondDeck.size() - 1);
	}

	public void removeCard() {
		secondDeck.remove(secondDeck.size() - 1);
	}

	public void relocateCard() {
		secondDeck.get(secondDeck.size() - 1).x = CARDPOSX;
		secondDeck.get(secondDeck.size() - 1).y = CARDPOSY;
	}

	public void showCard(Graphics g, ImageObserver observer) {
		for (int i = 0; i < secondDeck.size(); i++)
			secondDeck.get(i).draw(g, observer);
	}
}
