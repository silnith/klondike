package org.silnith.game.solitaire.move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;
import org.silnith.game.util.Pair;


/**
 * A move that takes a face-up card from a column and puts it into the
 * Foundation.
 */
public class ColumnToFoundationMove implements SolitaireMove {
	
	/**
	 * Finds all column to foundation moves for a given board.
	 * 
	 * <p>This could potentially find a move for each suit.</p>
	 * 
	 * @param board the board to examine
	 * @return a collection of moves
	 */
	public static Collection<ColumnToFoundationMove> findMoves(final Board board) {
		final Collection<ColumnToFoundationMove> moves = new ArrayList<ColumnToFoundationMove>(Suit.values().length);
		final List<Column> columns = board.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			final Column column = columns.get(i);
			if (column.hasFaceUpCards()) {
				final Card card = column.getTopCard();
				if (board.canAddToFoundation(card)) {
					moves.add(new ColumnToFoundationMove(i, card));
				}
			}
		}
		return moves;
	}
    
    /**
     * The index in the board of the column from which the card is taken.
     */
    private final int sourceColumnIndex;
    
    /**
     * The card being moved.
     */
    private final Card card;
    
    /**
     * Creates a new move that takes a face-up card from a column and puts it
     * into the Foundation.
     * 
     * @param sourceColumnIndex the index into the board of the column from which the
     *         card is taken
     * @param card the card being moved
     */
    public ColumnToFoundationMove(final int sourceColumnIndex, final Card card) {
        super();
        this.sourceColumnIndex = sourceColumnIndex;
        this.card = card;
    }
    
    /**
     * Creates a new move that takes a face-up card from a column and puts it
     * into the foundation.
     * 
     * @param sourceColumnIndex the index into the board of the column from which the
     *         card is taken
     * @param board the board containing the card to move
     * @throws IndexOutOfBoundsException if the source column is out of range
     * @throws IllegalArgumentException if the source column has no card to move
     */
    public ColumnToFoundationMove(final int sourceColumnIndex, final Board board) {
    	this(sourceColumnIndex, board.getColumn(sourceColumnIndex).getTopCard());
    }
    
    /**
     * Returns the card being moved.
     * 
     * @return the moved card
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
    public boolean isStockPileModification() {
        return false;
    }

    @Override
    public boolean isStockPileAdvance() {
        return false;
    }

    @Override
    public boolean isStockPileRecycle() {
        return false;
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
        return true;
    }

    @Override
    public boolean isFromColumn(int columnIndex) {
        return columnIndex == sourceColumnIndex;
    }

    @Override
    public int getSourceColumnIndex() {
        return sourceColumnIndex;
    }

    @Override
    public boolean isToFoundation() {
        return true;
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
    public int getDestinationColumnIndex() {
        throw new IllegalStateException("Not a move to a column.");
    }

    @Override
    public Board apply(final Board board) {
        final List<Column> columns = board.getColumns();
		final Column column = columns.get(sourceColumnIndex);
		final Pair<Card, Column> pair = column.extractCard();
		final Card card = pair.getFirst();
		final Column newColumn = pair.getSecond();
		
		final List<Column> newColumns = new ArrayList<>(columns);
		newColumns.set(sourceColumnIndex, newColumn);
		
		final List<Card> stockPile = board.getStockPile();
		final int stockPileIndex = board.getStockPileIndex();
		final Map<Suit, List<Card>> newFoundation = board.getFoundationPlusCard(card);
		
		return new Board(newColumns, stockPile, stockPileIndex, newFoundation);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(sourceColumnIndex, 16) ^ card.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ColumnToFoundationMove) {
            final ColumnToFoundationMove move = (ColumnToFoundationMove) obj;
            return sourceColumnIndex == move.sourceColumnIndex && card.equals(move.card);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Move " + card + " from column " + sourceColumnIndex + " to foundation.";
    }
    
}
