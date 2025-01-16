package org.silnith.game.solitaire.move;

import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.solitaire.Board;


/**
 * A move that recycles the stock pile.  This sets the current index into the
 * stock pile back to zero.
 */
public class RecycleStockPileMove implements SolitaireMove {
    
    /**
     * The index into the stock pile before the move is applied.
     * 
     * <p>For a legal move, this will be equal to the size of the stock pile.</p>
     */
    private final int sourceIndex;
    
    /**
     * Creates a new move that recycles the stock pile.
     * 
     * @param sourceIndex the index into the stock pile before the move
     */
    public RecycleStockPileMove(final int sourceIndex) {
        super();
        this.sourceIndex = sourceIndex;
    }
    
    /**
     * Returns the index into the stock pile before the move is applied.
     * 
     * @return the stock pile index beforehand
     */
    public int getSourceIndex() {
        return sourceIndex;
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
        return new Board(board.columns, board.stockPile, 0, board.foundation);
    }
    
    @Override
    public int hashCode() {
        return 0x5f23bc91 ^ sourceIndex;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RecycleStockPileMove) {
            final RecycleStockPileMove move = (RecycleStockPileMove) obj;
            return sourceIndex == move.sourceIndex;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Recycle stock pile from index " + sourceIndex + " to the beginning.";
    }
    
}
