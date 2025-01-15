package org.silnith.game.solitaire.move;

import java.util.Collections;
import java.util.List;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.solitaire.Board;


/**
 * A move that takes a card from the Foundation and puts it onto a column run.
 * 
 * <p>If the column is empty, only a {@link org.silnith.deck.Value#KING} may be
 * moved.  If the column is not empty, then the card must adhere to the rules of
 * a run.</p>
 */
public class FoundationToColumnMove implements SolitaireMove {
    
    /**
     * The index in the board of the destination column for the card.
     */
    private final int destinationColumn;
    
    /**
     * The card being moved.
     */
    private final Card card;
    
    /**
     * Creates a move that takes a card from the Foundation and puts it on top
     * of a column.
     * 
     * @param destinationColumn the index of the column into the board
     * @param card the card being moved
     */
    public FoundationToColumnMove(final int destinationColumn, final Card card) {
        super();
        this.destinationColumn = destinationColumn;
        this.card = card;
    }
    
    /**
     * Creates a move that takes a card from the Foundation and puts it on top
     * of a column.
     * 
     * @param destinationColumn the index of the column into the board
     * @param suit the suit of card to pull from the foundation
     * @param board the board to get the card from
     * @throws IndexOutOfBoundsException if the board foundation has no cards for the suit
     */
    public FoundationToColumnMove(final int destinationColumn, final Suit suit, final Board board) {
    	this(destinationColumn, board.getTopOfFoundation(suit));
    }
    
    /**
     * Returns the index into the board of the column that is the destination of
     * the card.
     * 
     * @return the index into the board of the column
     */
    public int getDestinationColumn() {
        return destinationColumn;
    }
    
    /**
     * Returns the card being moved.
     * 
     * @return the card moved
     * @see #getCards()
     */
    public Card getCard() {
        return card;
    }
    
    @Override
    public boolean hasCards() {
        return true;
    }
    
    @Override
    public List<Card> getCards() {
        return Collections.singletonList(card);
    }
    
    @Override
    public Board apply(final Board board) {
        return board.moveCardFromFoundation(card.getSuit(), destinationColumn);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(destinationColumn, 8) ^ card.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof FoundationToColumnMove) {
            final FoundationToColumnMove move = (FoundationToColumnMove) obj;
            return destinationColumn == move.destinationColumn && card.equals(move.card);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Move " + card + " from foundation to column " + destinationColumn + ".";
    }
    
}
