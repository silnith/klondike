package org.silnith.game.solitaire.move;

import java.util.Collections;
import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.solitaire.Board;


/**
 * A move that takes a card from the stock pile and puts it into the Foundation.
 */
public class StockPileToFoundationMove implements SolitaireMove {
    
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
    public Board apply(final Board board) {
        return board.drawStockPileCardToFoundation();
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
