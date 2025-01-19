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
import org.silnith.util.Pair;


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
    private final int sourceColumn;
    
    /**
     * The card being moved.
     */
    private final Card card;
    
    /**
     * Creates a new move that takes a face-up card from a column and puts it
     * into the Foundation.
     * 
     * @param sourceColumn the index into the board of the column from which the
     *         card is taken
     * @param card the card being moved
     */
    public ColumnToFoundationMove(final int sourceColumn, final Card card) {
        super();
        this.sourceColumn = sourceColumn;
        this.card = card;
    }
    
    /**
     * Creates a new move that takes a face-up card from a column and puts it
     * into the foundation.
     * 
     * @param sourceColumn the index into the board of the column from which the
     *         card is taken
     * @param board the board containing the card to move
     * @throws IndexOutOfBoundsException if the source column is out of range
     * @throws IllegalArgumentException if the source column has no card to move
     */
    public ColumnToFoundationMove(final int sourceColumn, final Board board) {
    	this(sourceColumn, board.getColumns().get(sourceColumn).getTopCard());
    }
    
    /**
     * Returns the index into the board of the column from which the card is taken.
     * 
     * @return the index of the column in the board
     */
    public int getSourceColumn() {
        return sourceColumn;
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
    public Board apply(final Board board) {
        final List<Column> columns = board.getColumns();
		final Column column = columns.get(sourceColumn);
		final Pair<Card, Column> pair = column.extractCard();
		final Card card = pair.getFirst();
		final Column newColumn = pair.getSecond();
		
		final List<Column> newColumns = new ArrayList<>(columns);
		newColumns.set(sourceColumn, newColumn);
		
		final List<Card> stockPile = board.getStockPile();
		final int stockPileIndex = board.getStockPileIndex();
		final Map<Suit, List<Card>> newFoundation = board.getFoundationPlusCard(card);
		
		return new Board(newColumns, stockPile, stockPileIndex, newFoundation);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(sourceColumn, 16) ^ card.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ColumnToFoundationMove) {
            final ColumnToFoundationMove move = (ColumnToFoundationMove) obj;
            return sourceColumn == move.sourceColumn && card.equals(move.card);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Move " + card + " from column " + sourceColumn + " to foundation.";
    }
    
}
