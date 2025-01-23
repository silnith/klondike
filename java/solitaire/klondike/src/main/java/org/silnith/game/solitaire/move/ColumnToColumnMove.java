package org.silnith.game.solitaire.move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;
import org.silnith.util.Pair;


/**
 * A move that takes a run of cards from one column and puts it on top of
 * another run on a different column.
 * 
 * <p>A run can consist of a single card, or it can consist of many cards.</p>
 */
public class ColumnToColumnMove implements SolitaireMove {
	
	/**
	 * Finds all moves where a run is moved from one column to another.
	 * 
	 * @param board the board to examine
	 * @return a collection of moves
	 */
	public static Collection<ColumnToColumnMove> findMoves(final Board board) {
		final Collection<ColumnToColumnMove> moves = new ArrayList<>();
		final List<Column> columns = board.getColumns();
		final ListIterator<Column> outerIterator = columns.listIterator();
		while (outerIterator.hasNext()) {
			final int sourceIndex = outerIterator.nextIndex();
			final Column sourceColumn = outerIterator.next();
			if (!sourceColumn.hasFaceUpCards()) {
				continue;
			}
			final List<Card> sourceRun = sourceColumn.getFaceUpCards();
			final int sourceRunLength = sourceRun.size();
			final Card sourceTopCard = sourceRun.get(sourceRunLength - 1);
			final Card sourceBottomCard = sourceRun.get(0);
			final int sourceRunMinValue = sourceTopCard.getValue().getValue();
			final int sourceRunMaxValue = sourceBottomCard.getValue().getValue();
			
			final ListIterator<Column> innerIterator = columns.listIterator();
			while (innerIterator.hasNext()) {
				final int destinationIndex = innerIterator.nextIndex();
				final Column destinationColumn = innerIterator.next();
				// Note that this must be tested after the call to iterator.next().
				if (sourceIndex == destinationIndex) {
					// Cannot move from a column to itself.
					continue;
				}
				
				if (destinationColumn.hasFaceUpCards()) {
					// Destination has cards already.
					final Card destinationTopCard = destinationColumn.getTopCard();
					final int runStartValue = destinationTopCard.getValue().getValue() - 1;
					
					if (sourceRunMinValue <= runStartValue && sourceRunMaxValue >= runStartValue) {
						final int runLength = runStartValue - sourceRunMinValue + 1;
						
						final List<Card> run = sourceColumn.getTopCards(runLength);
						if (destinationTopCard.getSuit().getColor() != run.get(0).getSuit().getColor()) {
							moves.add(new ColumnToColumnMove(sourceIndex, destinationIndex, runLength, run));
						}
					}
				} else {
					// Destination is empty, only a King may be moved to it.
					if (sourceBottomCard.getValue() == Value.KING) {
						moves.add(new ColumnToColumnMove(sourceIndex, destinationIndex, sourceRunLength, sourceRun));
					}
				}
			}
		}
		return moves;
	}
    
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
     * @throws IllegalArgumentException if the source and destination columns are the same
     */
    public ColumnToColumnMove(final int sourceColumn, final int destinationColumn, final int numberOfCards, final List<Card> cards) {
        super();
        if (sourceColumn == destinationColumn) {
		    throw new IllegalArgumentException("Source and destination column are the same.");
		}
		
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
    public ColumnToColumnMove(final int sourceColumn, final int destinationColumn, final int numberOfCards, final Board board) {
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
    	assert sourceColumn != destinationColumn;
    	
		final List<Column> columns = board.getColumns();
		final List<Card> stockPile = board.getStockPile();
		final int stockPileIndex = board.getStockPileIndex();
		final Map<Suit, List<Card>> foundation = board.getFoundation();
		
		final Column fromColumn = columns.get(sourceColumn);
		final Column toColumn = columns.get(destinationColumn);
		final Pair<List<Card>, Column> pair = fromColumn.extractRun(numberOfCards);
		final List<Card> run = pair.getFirst();
		final Column newFromColumn = pair.getSecond();
		final Column newToColumn = toColumn.withCards(run);
		
		final List<Column> newColumns = new ArrayList<>(columns);
		newColumns.set(sourceColumn, newFromColumn);
		newColumns.set(destinationColumn, newToColumn);
		return new Board(newColumns, stockPile, stockPileIndex, foundation);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(sourceColumn, 8) ^ Integer.rotateLeft(destinationColumn, 16)
                ^ Integer.rotateLeft(numberOfCards, 24) ^ cards.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ColumnToColumnMove) {
            final ColumnToColumnMove move = (ColumnToColumnMove) obj;
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
