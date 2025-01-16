package org.silnith.game.solitaire.move;

import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.solitaire.Board;


/**
 * A move that takes a run of cards from one column and puts it on top of
 * another run on a different column.
 * 
 * <p>A run can consist of a single card, or it can consist of many cards.</p>
 */
public class RunMove implements SolitaireMove {
    
    /**
     * The index into the board of the source column.
     */
    private final int sourceColumn;
    
    /**
     * The index into the board of the destination column.
     */
    private final int destinationColumn;
    
    /**
     * The number of cards being moved.
     */
    private final int numberOfCards;
    
    /**
     * The cards being moved.
     */
    private final List<Card> cards;
    
    /**
     * Creates a new move of a run of cards from one column to another.
     * 
     * @param sourceColumn the index into the board of the source column
     * @param destinationColumn the index into the board of the destination column
     * @param numberOfCards the number of cards being moved
     * @param cards the cards being moved
     */
    public RunMove(final int sourceColumn, final int destinationColumn, final int numberOfCards, final List<Card> cards) {
        super();
        this.sourceColumn = sourceColumn;
        this.destinationColumn = destinationColumn;
        this.numberOfCards = numberOfCards;
        this.cards = cards;
    }
    
    /**
     * Creates a new move of a run of cards from one column to another.
     * 
     * @param sourceColumn the index into the board of the source column
     * @param destinationColumn the index into the board of the destination column
     * @param numberOfCards the number of cards being moved
     * @param board the board from which to get the cards being moved
     * @throws IllegalArgumentException if the number of cards is less than {@code 1},
     *         or exceeds the available cards
     * @throws IndexOutOfBoundsException if the source column is out of bounds
     */
    public RunMove(final int sourceColumn, final int destinationColumn, final int numberOfCards, final Board board) {
    	this(sourceColumn, destinationColumn, numberOfCards, board.getColumns().get(sourceColumn).getTopCards(numberOfCards));
    }
    
    /**
     * Returns the index into the board of the column from which the cards are
     * taken.
     * 
     * @return the index into the board of the source column
     */
    public int getSourceColumn() {
        return sourceColumn;
    }
    
    /**
     * Returns the index into the board of the column to which the cards are
     * being moved.
     * 
     * @return the index into the board of the destination column
     */
    public int getDestinationColumn() {
        return destinationColumn;
    }
    
    /**
     * Returns the number of cards being moved.
     * 
     * @return the number of cards moved
     * @see #getCards()
     */
    public int getNumberOfCards() {
        return numberOfCards;
    }
    
    @Override
    public boolean hasCards() {
        return true;
    }
    
    @Override
    public List<Card> getCards() {
        return cards;
    }
    
    @Override
    public Board apply(final Board board) {
        return board.moveRun(sourceColumn, destinationColumn, numberOfCards);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(sourceColumn, 8) ^ Integer.rotateLeft(destinationColumn, 16)
                ^ Integer.rotateLeft(numberOfCards, 24) ^ cards.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RunMove) {
            final RunMove move = (RunMove) obj;
            return sourceColumn == move.sourceColumn && destinationColumn == move.destinationColumn
                    && numberOfCards == move.numberOfCards && cards.equals(move.cards);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        if (numberOfCards == 1) {
            return "Move " + cards.get(0) + " from column " + sourceColumn + " to column " + destinationColumn + ".";
        } else {
            return "Move stack " + cards + " from column " + sourceColumn + " to column " + destinationColumn + ".";
        }
    }
    
}
