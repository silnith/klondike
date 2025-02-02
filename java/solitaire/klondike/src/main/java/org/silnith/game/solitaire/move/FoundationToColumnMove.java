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
 * A move that takes a card from the Foundation and puts it onto a column run.
 * 
 * <p>If the column is empty, only a {@link org.silnith.deck.Value#KING} may be
 * moved.  If the column is not empty, then the card must adhere to the rules of
 * a run.</p>
 */
public class FoundationToColumnMove implements SolitaireMove {
	
	/**
	 * Finds all moves from the foundation to a column run.
	 * 
	 * @param board the board to examine
	 * @return a collection of moves
	 */
	public static Collection<FoundationToColumnMove> findMoves(final Board board) {
		final Collection<FoundationToColumnMove> moves = new ArrayList<>();
		final Map<Suit, List<Card>> foundation = board.getFoundation();
		final List<Column> columns = board.getColumns();
		for (final Suit suit : Suit.values()) {
			final List<Card> foundationForSuit = foundation.get(suit);
			if (!foundationForSuit.isEmpty()) {
				final Card topOfFoundation = foundationForSuit.get(foundationForSuit.size() - 1);
				final List<Card> run = Collections.singletonList(topOfFoundation);
				final ListIterator<Column> iter = columns.listIterator();
				while (iter.hasNext()) {
					final int i = iter.nextIndex();
					final Column column = iter.next();
					if (column.canAddRun(run)) {
						moves.add(new FoundationToColumnMove(i, topOfFoundation));
					}
				}
			}
		}
		return moves;
	}
    
    /**
     * The index in the board of the destination column for the card.
     */
    private final int destinationColumnIndex;
    
    /**
     * The card being moved.
     */
    private final Card card;
    
    /**
     * Creates a move that takes a card from the Foundation and puts it on top
     * of a column.
     * 
     * @param destinationColumnIndex the index of the column into the board
     * @param card the card being moved
     */
    public FoundationToColumnMove(final int destinationColumnIndex, final Card card) {
        super();
        this.destinationColumnIndex = destinationColumnIndex;
        this.card = card;
    }
    
    /**
     * Creates a move that takes a card from the Foundation and puts it on top
     * of a column.
     * 
     * @param destinationColumnIndex the index of the column into the board
     * @param suit the suit of card to pull from the foundation
     * @param board the board to get the card from
     * @throws IndexOutOfBoundsException if the board foundation has no cards for the suit
     */
    public FoundationToColumnMove(final int destinationColumnIndex, final Suit suit, final Board board) {
    	this(destinationColumnIndex, board.getTopOfFoundation(suit));
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
    public boolean isStockPileModification() {
        return false;
    }

    @Override
    public boolean isFromStockPile() {
        return false;
    }

    @Override
    public boolean isFromFoundation() {
        return true;
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
        final Pair<Card, Map<Suit, List<Card>>> pair = board.extractCardFromFoundation(card.getSuit());
		final Card card = pair.getFirst();
		final Map<Suit, List<Card>> newFoundation = pair.getSecond();
		
		final List<Column> columns = board.getColumns();
		final Column column = columns.get(destinationColumnIndex);
		final Column newColumn = column.withCard(card);
		
		final List<Column> newColumns = new ArrayList<>(columns);
		newColumns.set(destinationColumnIndex, newColumn);
		
		return new Board(newColumns, board.getStockPile(), board.getStockPileIndex(), newFoundation);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(destinationColumnIndex, 8) ^ card.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof FoundationToColumnMove) {
            final FoundationToColumnMove move = (FoundationToColumnMove) obj;
            return destinationColumnIndex == move.destinationColumnIndex && card.equals(move.card);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Move " + card + " from foundation to column " + destinationColumnIndex + ".";
    }
    
}
