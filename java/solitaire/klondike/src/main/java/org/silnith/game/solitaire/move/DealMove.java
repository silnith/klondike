package org.silnith.game.solitaire.move;

import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.solitaire.Board;


/**
 * A move that deals a fresh deck of cards into a Klondike solitaire board.
 */
public class DealMove implements SolitaireMove {
    
    /**
     * The number of columns in the board.  In a typical game, this will be 7.
     */
    private final int numberOfColumns;
    
    /**
     * The deck of cards to deal.
     */
    private final List<Card> deck;
    
    /**
     * Creates a new move that deals a fresh deck of cards.
     * 
     * @param deck the deck of cards to deal
     * @param numberOfColumns the number of columns on the board.  This is always seven.
     */
    public DealMove(final List<Card> deck, final int numberOfColumns) {
        super();
        this.numberOfColumns = numberOfColumns;
        this.deck = deck;
    }
    
    /**
     * Returns the number of columns dealt on the board.
     * 
     * <p>In normal games, this is always seven.  But the code is written to
     * support any value.</p>
     * 
     * @return the number of columns on the board
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }
    
    /**
     * Returns the deck of cards dealt onto the board.
     * 
     * @return the deck of cards
     * @see #getCards()
     */
    public List<Card> getDeck() {
        return deck;
    }
    
    @Override
    public boolean hasCards() {
        return true;
    }
    
    @Override
    public List<Card> getCards() {
        return deck;
    }
    
    @Override
    public Board apply(final Board board) {
        return new Board(deck, numberOfColumns);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(numberOfColumns, 16) ^ deck.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DealMove) {
            final DealMove move = (DealMove) obj;
            return numberOfColumns == move.numberOfColumns && deck.size() == move.deck.size() && deck.equals(move.deck);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Deal " + numberOfColumns + " columns using deck " + deck + ".";
    }
    
}
