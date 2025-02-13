package org.silnith.game.solitaire.move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;
import org.silnith.util.Pair;


/**
 * A move that takes a card from the stock pile and puts it onto a column.
 * 
 * <p>If the column is empty, the card must be a {@link org.silnith.deck.Value#KING}.
 * If the column is not empty, the card must follow the rules of a run.</p>
 */
public class StockPileToColumnMove implements SolitaireMove {
	
	/**
	 * Finds all moves where a card is drawn from the stock pile to a column run.
	 * 
	 * @param board the board to examine
	 * @return a collection of moves
	 */
	public static Collection<StockPileToColumnMove> findMoves(final Board board) {
		if (board.getStockPileIndex() > 0) {
			final Collection<StockPileToColumnMove> moves = new ArrayList<>(7);
			final Card card = board.getStockPileCard();
			final List<Card> run = Collections.singletonList(card);
			final List<Column> columns = board.getColumns();
			final ListIterator<Column> iter = columns.listIterator();
			while (iter.hasNext()) {
				final int index = iter.nextIndex();
				final Column column = iter.next();
				if (column.canAddRun(run)) {
					moves.add(new StockPileToColumnMove(index, board));
					// TODO: Short-circuit if the card is a king?
				}
			}
			return moves;
		}
		return Collections.emptySet();
	}
    
    /**
     * The index into the stock pile from which the card is taken.
     */
    private final int sourceIndex;
    
    /**
     * The index into the board of the column to which the card is being moved.
     */
    private final int destinationColumnIndex;
    
    /**
     * The card being moved.
     */
    private final Card card;
    
    /**
     * Creates a new move of a card from the stock pile to a column on the board.
     * 
     * @param sourceIndex the stock pile index of the card being moved
     * @param destinationColumnIndex the index into the board of the destination column
     * @param card the card being moved
     */
    public StockPileToColumnMove(final int sourceIndex, final int destinationColumnIndex, final Card card) {
        super();
        this.sourceIndex = sourceIndex;
        this.destinationColumnIndex = destinationColumnIndex;
        this.card = card;
    }
    
    /**
     * Creates a new move of a card from the stock pile to a column on the board.
     * 
     * @param destinationColumnIndex the index into the board of the destination column
     * @param board the board from which to get the card
     * @throws IndexOutOfBoundsException if no card is available to be drawn from the board stock pile
     */
    public StockPileToColumnMove(final int destinationColumnIndex, final Board board) {
    	this(board.getStockPileIndex(), destinationColumnIndex, board.getStockPileCard());
    }
    
    /**
     * Returns the index into the stock pile of the card being moved.
     * 
     * @return the stock pile index
     */
    public int getSourceIndex() {
        return sourceIndex;
    }
    
    /**
     * Returns the card being moved.
     * 
     * @return the card being moved
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
        return true;
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
        return true;
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
    public int getSourceColumnIndex() {
        throw new IllegalStateException("Not a move from a column.");
    }

    @Override
    public boolean isToFoundation() {
        return false;
    }

    @Override
    public boolean isToColumn() {
        return true;
    }

    @Override
    public boolean isToColumn(int columnIndex) {
        return columnIndex == destinationColumnIndex;
    }

    @Override
    public int getDestinationColumnIndex() {
        return destinationColumnIndex;
    }

	@Override
    public Board apply(final Board board) {
    	final Pair<Card, List<Card>> pair = board.extractStockPileCard();
        final Card card = pair.getFirst();
		final List<Card> newStockPile = pair.getSecond();
		
		final int stockPileIndex = board.getStockPileIndex();
		final int newStockPileIndex = stockPileIndex - 1;
		
		final List<Column> columns = board.getColumns();
		final Column column = columns.get(destinationColumnIndex);
		final Column newColumn = column.withCard(card);
		
		final List<Column> newColumns = new ArrayList<>(columns);
		newColumns.set(destinationColumnIndex, newColumn);
		
		final Map<Suit, List<Card>> foundation = board.getFoundation();
		return new Board(newColumns, newStockPile, newStockPileIndex, foundation);
    }
    
    @Override
    public int hashCode() {
        return sourceIndex ^ destinationColumnIndex ^ card.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof StockPileToColumnMove) {
            final StockPileToColumnMove move = (StockPileToColumnMove) obj;
            return sourceIndex == move.sourceIndex && destinationColumnIndex == move.destinationColumnIndex && card.equals(move.card);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Move " + card + " from stock pile index " + sourceIndex + " to column " + destinationColumnIndex + ".";
    }
    
}
