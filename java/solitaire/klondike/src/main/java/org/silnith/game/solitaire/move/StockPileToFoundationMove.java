package org.silnith.game.solitaire.move;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.solitaire.Board;
import org.silnith.game.util.Pair;


/**
 * A move that takes a card from the stock pile and puts it into the Foundation.
 */
public class StockPileToFoundationMove implements SolitaireMove {
	
	/**
	 * Finds all moves where a card is drawn from the stock pile to the foundation.
	 * 
	 * <p>This will either contain one move or zero.</p>
	 * 
	 * @param board the board to examine
	 * @return a collection of moves
	 */
	public static Collection<StockPileToFoundationMove> findMoves(final Board board) {
		if (board.getStockPileIndex() > 0) {
			final Card card = board.getStockPileCard();
			if (board.canAddToFoundation(card)) {
				return Collections.singleton(new StockPileToFoundationMove(board));
			}
		}
		return Collections.emptySet();
	}
    
    /**
     * The index into the stock pile from which the card is being taken.
     */
    private final int sourceIndex;
    
    /**
     * The card being moved.
     */
    private final Card card;
    
    /**
     * Creates a new move that takes a card from the stock pile and puts it into
     * the Foundation.
     * 
     * @param sourceIndex the index into the stock pile of the card being moved
     * @param card the card being moved
     */
    public StockPileToFoundationMove(final int sourceIndex, final Card card) {
        super();
        this.sourceIndex = sourceIndex;
        this.card = card;
    }
    
    /**
     * Creates a new move that takes a card from the stock pile and puts it into
     * the foundation.
     * 
     * <p>This takes the parameters from the current state of the provided board.</p>
     * 
     * @param board the board from which to get the source index and card
     * @throws IndexOutOfBoundsException if the board stock pile index is
     *         {@code 0} or greater than the size of the stock pile
     */
    public StockPileToFoundationMove(final Board board) {
    	this(board.getStockPileIndex(), board.getStockPileCard());
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
        final Pair<Card, List<Card>> pair = board.extractStockPileCard();
		final Card card = pair.getFirst();
		final List<Card> newStockPile = pair.getSecond();
		
		final int newStockPileIndex = board.getStockPileIndex() - 1;
		
		final Map<Suit, List<Card>> newFoundation = board.getFoundationPlusCard(card);
		
		return new Board(board.getColumns(), newStockPile, newStockPileIndex, newFoundation);
    }
    
    @Override
    public int hashCode() {
        return sourceIndex ^ card.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof StockPileToFoundationMove) {
            final StockPileToFoundationMove move = (StockPileToFoundationMove) obj;
            return sourceIndex == move.sourceIndex && card.equals(move.card);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Move " + card + " from stock pile " + sourceIndex + " to foundation.";
    }
    
}
