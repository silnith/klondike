package org.silnith.deck;

import java.beans.ConstructorProperties;

import org.silnith.deck.Suit.Color;


/**
 * A playing card.
 * 
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class Card {
    
    private final Value value;
    
    private final Suit suit;
    
    /**
     * Constructs a new playing card with the given value and suit.
     * 
     * @param value the value for the card
     * @param suit the suit for the card
     */
    @ConstructorProperties({ "value", "suit" })
    public Card(final Value value, final Suit suit) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        if (suit == null) {
            throw new IllegalArgumentException("Suit cannot be null.");
        }
        this.value = value;
        this.suit = suit;
    }
    
    /**
     * Returns the value for the playing card.
     * 
     * @return the card value
     */
    public Value getValue() {
        return value;
    }
    
    /**
     * Returns the suit for the playing card.
     * 
     * @return the card suit
     */
    public Suit getSuit() {
        return suit;
    }
    
    /**
     * Returns the color for the playing card&rsquo;s suit.
     * 
     * @return the card color
     */
    public Color getColor() {
        return suit.getColor();
    }
    
    @Override
    public int hashCode() {
        return 0xac17b295 ^ value.hashCode() ^ suit.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Card) {
            final Card card = (Card) obj;
            return value.equals(card.value) && suit.equals(card.suit);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return value + " of " + suit + "s";
    }
    
}
