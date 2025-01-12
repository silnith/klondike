package org.silnith.deck;

/**
 * A numeric value for a playing card.
 * 
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public enum Value {
    /**
     * The ace.  Usually the lowest card, but in some games is also the highest card.
     * Has a numeric value of {@code 1}.
     */
    ACE(1),
    /**
     * The value {@code 2}.
     */
    TWO(2),
    /**
     * The value {@code 3}.
     */
    THREE(3),
    /**
     * The value {@code 4}.
     */
    FOUR(4),
    /**
     * The value {@code 5}.
     */
    FIVE(5),
    /**
     * The value {@code 6}.
     */
    SIX(6),
    /**
     * The value {@code 7}.
     */
    SEVEN(7),
    /**
     * The value {@code 8}.
     */
    EIGHT(8),
    /**
     * The value {@code 9}.
     */
    NINE(9),
    /**
     * The value {@code 10}.
     */
    TEN(10),
    /**
     * The jack.  Has the value {@code 11}, though some games treat it as {@code 10}.
     */
    JACK(11),
    /**
     * The queen.  Has the value {@code 12}, though some games treat it as {@code 10}.
     */
    QUEEN(12),
    /**
     * The king.  Has the value {@code 13}, though some games treat it as {@code 10}.
     * The highest-value card, unless a game overrides this by treating the {@link #ACE ace}
     * as higher.
     */
    KING(13);
    
    private final int value;
    
    private Value(final int value) {
        this.value = value;
    }
    
    /**
     * Returns the numeric equivalent for the card value.
     * 
     * @return the numeric equivalent for the card value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Returns a short symbol representing the card value. This is usually a
     * single character, except for {@link #TEN ten}.
     * 
     * @return a short symbol representing the card value
     */
    public String toSymbol() {
        switch (this) {
        case ACE:
            return "A";
        case JACK:
            return "J";
        case QUEEN:
            return "Q";
        case KING:
            return "K";
        default:
            return String.valueOf(value);
        }
    }
    
    @Override
    public String toString() {
        switch (this) {
        case ACE:
            return "Ace";
        case TWO:
            return "Two";
        case THREE:
            return "Three";
        case FOUR:
            return "Four";
        case FIVE:
            return "Five";
        case SIX:
            return "Six";
        case SEVEN:
            return "Seven";
        case EIGHT:
            return "Eight";
        case NINE:
            return "Nine";
        case TEN:
            return "Ten";
        case JACK:
            return "Jack";
        case QUEEN:
            return "Queen";
        case KING:
            return "King";
        default:
            return super.toString();
        }
    }
    
}
