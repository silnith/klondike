package org.silnith.game.solitaire.move;

import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;


/**
 * A move that advances the stock pile by an increment.
 * 
 * <p>In a typical game, the advance is three unless there are fewer than
 * three cards left in the stock pile.</p>
 */
public class AdvanceStockPileMove implements SolitaireMove {
    
    /**
     * The index into the stock pile before the advance move happens.
     */
    private final int beginningIndex;
    
    /**
     * The number of cards that the move advances the stock pile index.
     * 
     * <p>If the advanced index needs to be clamped, that happens when
     * the move is applied.</p>
     */
    private final int increment;
    
    /**
     * Creates a new move that advances the stock pile index.
     * 
     * @param beginningIndex the stock pile index before the move is applied
     * @param increment the number of cards that the stock pile index advances
     */
    public AdvanceStockPileMove(final int beginningIndex, final int increment) {
        super();
        this.beginningIndex = beginningIndex;
        this.increment = increment;
    }
    
    /**
     * Creates a new move that advances the stock pile index.
     * 
     * @param increment the number of cards that the stock pile index advances
     * @param board the board from which to take the beginning index
     */
    public AdvanceStockPileMove(final int increment, final Board board) {
    	this(board.getStockPileIndex(), increment);
    }
    
    /**
     * Returns the stock pile index before the move is applied.
     * 
     * @return the stock pile index before the move happens
     */
    public int getBeginningIndex() {
        return beginningIndex;
    }
    
    /**
     * Returns the number of cards that the stock pile index is advanced by the move.
     * 
     * @return the number of cards advanced in the stock pile
     */
    public int getIncrement() {
        return increment;
    }
    
    /**
     * Returns a new move that combines this stock pile advance with another
     * stock pile advance.  The returned move is the sum of both advances,
     * as if only one advance occurred.
     * 
     * @param next the move to combine with this move
     * @return a single move that can replace both moves
     */
    public AdvanceStockPileMove coalesce(final AdvanceStockPileMove next) {
        assert beginningIndex + increment == next.beginningIndex;
        return new AdvanceStockPileMove(beginningIndex, increment + next.increment);
    }
    
    @Override
    public boolean hasCards() {
        return false;
    }
    
    @Override
    public List<Card> getCards() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Board apply(final Board board) {
        if (increment < 1) {
		    throw new IllegalArgumentException();
		}
		
		final List<Card> stockPile = board.getStockPile();
		final int newIndex = Math.min(board.getStockPileIndex() + increment, stockPile.size());
		return new Board(board.getColumns(), stockPile, newIndex, board.getFoundation());
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(beginningIndex, 24) ^ Integer.rotateLeft(increment, 8);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AdvanceStockPileMove) {
            final AdvanceStockPileMove move = (AdvanceStockPileMove) obj;
            return beginningIndex == move.beginningIndex && increment == move.increment;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Advance stock pile from " + beginningIndex
        		+ " by " + increment + ".";
    }
    
}
