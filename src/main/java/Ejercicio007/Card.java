package Ejercicio007;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Card extends Rectangle {
    public static final int WIDTH = 70;
    public static final int HEIGHT = 120;
    public static final int RED = 1;
    public static final int BLACK = 2;
    public static final int SPADES = 0;
    public static final int DIAMONDS = 1;
    public static final int HEARTS = 2;
    public static final int CLUBS = 3;
    
    private Image image;
    private int value;
    private int color;
	private int suit;
    
	public Card(Image img, int value, int color, int suit) {
		super(200, 200, WIDTH, HEIGHT);
        this.image = img;
        this.value = value;
        this.color = color;
		this.suit = suit;
    }
    
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

	public int getSuit() {
		return suit;
    }

	public void setSuit(int suit) {
		this.suit = suit;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }

	public void draw(Graphics g, ImageObserver observer) {
		g.drawImage(image, x, y, WIDTH, HEIGHT, observer);
	}
}