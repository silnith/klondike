package org.silnith.game.solitaire.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;


/**
 * A move that deals a fresh deck of cards into a Klondike solitaire board.
 */
public class DealMove implements SolitaireMove {
    
    /**
     * The number of columns in the board.  In a typical game, this will be 7.
     */
    private final int numberOfColumns;
    
    /**
     * The deck of cards to deal.
     */
    private final List<Card> cards;
    
    /**
     * Creates a new move that deals a fresh deck of cards.
     * 
     * @param cards the deck of cards to deal
     * @param numberOfColumns the number of columns on the board.  This is always seven.
     */
    public DealMove(final List<Card> cards, final int numberOfColumns) {
        super();
        int cardsRequired = 0;
        for (int i = 1; i <= numberOfColumns; i++) {
        	cardsRequired += i;
        }
        if (cards.size() < cardsRequired) {
        	throw new IllegalArgumentException("A deck of size " + cardsRequired + " is required to deal " + numberOfColumns + " columns.");
        }
        this.numberOfColumns = numberOfColumns;
        this.cards = cards;
    }
    
    /**
     * Returns the number of columns dealt on the board.
     * 
     * <p>In normal games, this is always seven.  But the code is written to
     * support any value.</p>
     * 
     * @return the number of columns on the board
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
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
    	// The parameter is completely ignored.
        int remaining = cards.size();
        final List<List<Card>> stacks = new ArrayList<>(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++ ) {
            stacks.add(new ArrayList<Card>(i + 1));
        }
        final Iterator<Card> iter = cards.iterator();
        for (int i = 0; i < numberOfColumns; i++ ) {
            for (int j = i; j < numberOfColumns; j++ ) {
                final Card card = iter.next();
                remaining-- ;
                stacks.get(j).add(card);
            }
        }
        
        final List<Column> columns = new ArrayList<>(numberOfColumns);
        for (final List<Card> stack : stacks) {
            columns.add(new Column(stack, null));
        }
        
        final List<Card> stockPile = new ArrayList<>(remaining);
        while (iter.hasNext()) {
            final Card card = iter.next();
            stockPile.add(card);
        }
        
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        for (final Suit suit : Suit.values()) {
            final List<Card> cards = Collections.emptyList();
            foundation.put(suit, cards);
        }
        
        return new Board(columns, stockPile, 0, foundation);
    }
    
    @Override
    public int hashCode() {
        return Integer.rotateLeft(numberOfColumns, 16) ^ cards.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DealMove) {
            final DealMove move = (DealMove) obj;
            return numberOfColumns == move.numberOfColumns && cards.size() == move.cards.size() && cards.equals(move.cards);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Deal " + numberOfColumns + " columns using deck " + cards + ".";
    }
    
}
