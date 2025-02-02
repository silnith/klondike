package org.silnith.game.solitaire.move;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.solitaire.Board;


/**
 * A move that recycles the stock pile.  This sets the current index into the
 * stock pile back to zero.
 */
public class StockPileRecycleMove implements SolitaireMove {
	
	/**
	 * Finds all recycle stock pile moves for a given board.
	 * 
	 * <p>This will either contain one move or zero.</p>
	 * 
	 * @param board the board to examine
	 * @return a collection of moves
	 */
	public static Collection<StockPileRecycleMove> findMoves(final Board board) {
		if (board.canRecycleStockPile()) {
			return Collections.singleton(new StockPileRecycleMove(board.getStockPileIndex()));
		} else {
			return Collections.emptySet();
		}
	}
    
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
    public StockPileRecycleMove(final int sourceIndex) {
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
        return Collections.emptyList();
    }

    @Override
    public boolean isStockPileModification() {
        return true;
    }

    @Override
    public boolean isFromStockPile() {
        return false;
    }

    @Override
    public boolean isFromFoundation() {
        return false;
    }

    @Override
    public boolean isFromColumn() {
        return false;
    }

    @Override
    public boolean isFromColumn(int columnIndex) {
        return false;
    }

    @Override
    public int getFromColumnIndex() {
        throw new IllegalStateException("Not a move from a column.");
    }

    @Override
    public boolean isToFoundation() {
        return false;
    }

    @Override
    public boolean isToColumn() {
        return false;
    }

    @Override
    public boolean isToColumn(int columnIndex) {
        return false;
    }

    @Override
    public int getToColumnIndex() {
        throw new IllegalStateException("Not a move to a column.");
    }

	@Override
    public Board apply(final Board board) {
        return new Board(board.getColumns(), board.getStockPile(), 0, board.getFoundation());
    }
    
    @Override
    public int hashCode() {
        return 0x5f23bc91 ^ sourceIndex;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof StockPileRecycleMove) {
            final StockPileRecycleMove move = (StockPileRecycleMove) obj;
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
