package org.silnith.game.solitaire;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.util.Pair;


/**
 * The board for Klondike solitaire.
 * 
 * <p>The board consists of seven columns, the foundation, and the stock pile.</p>
 */
public class Board {
    
    private final List<Column> columns;
    
    private final List<Card> stockPile;
    
    private final int stockPileIndex;
    
    private final Map<Suit, List<Card>> foundation;
    
    /**
     * Creates a new board.
     * 
     * <p>Parameters should be immutable.  This is not enforced in code.</p>
     * 
     * @param columns the columns for the new board
     * @param stockPile the stock pile
     * @param stockPileIndex the index into the stock pile of the current draw card.
     *         {@code 0} means no card is available to be drawn, {@code stockPile.size()}
     *         means all cards have been advanced and the last card is available to be drawn.
     * @param foundation the foundation
     */
    public Board(final List<Column> columns, final List<Card> stockPile, final int stockPileIndex,
            final Map<Suit, List<Card>> foundation) {
        super();
        if (stockPileIndex < 0) {
        	throw new IllegalArgumentException("Stock pile index must be non-negative.");
        }
        if (stockPileIndex > stockPile.size()) {
            throw new IllegalArgumentException("Stock pile index outside of stock pile.");
        }
        this.columns = columns;
        this.stockPile = stockPile;
        this.stockPileIndex = stockPileIndex;
        this.foundation = foundation;
    }
    
    /**
     * Returns the columns.
     * 
     * @return the columns
     */
    public List<Column> getColumns() {
        return columns;
    }
    
    /**
     * Returns the given column.
     * 
     * @param index the index of the column
     * @return the column
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Column getColumn(int index) {
        return columns.get(index);
    }
    
    /**
     * Returns the stock pile.
     * 
     * @return the stock pile
     */
    public List<Card> getStockPile() {
        return stockPile;
    }
    
    /**
     * Returns the current index into the stock pile.
     * This is zero if the stock pile has not been advanced,
     * meaning no cards are available to be drawn.
     * When this reaches the size of the stock pile, it is
     * eligible to be recycled.
     * 
     * @return the current index into the stock pile.
     *         This is the number of cards that have been
     *         advanced.
     */
    public int getStockPileIndex() {
        return stockPileIndex;
    }
    
    /**
     * Returns the foundation.  The game is won when all
     * cards have been moved to the foundation.
     * 
     * @return the foundation
     */
    public Map<Suit, List<Card>> getFoundation() {
        return foundation;
    }
    
    /**
     * Returns {@code true} if the stock pile can be advanced.
     * If the stock pile is empty, or if the stock pile index
     * is beyond the end of the stock pile, this is {@code false}.
     * 
     * <p>If the stock pile is empty, it cannot be advanced or recycled.
     * Otherwise, either this or {@link #canRecycleStockPile()}
     * will be {@code true}, and the other {@code false}.</p>
     * 
     * @return {@code true} if the stock pile can be advanced
     */
    public boolean canAdvanceStockPile() {
        return stockPileIndex < stockPile.size();
    }
    
    /**
     * Returns {@code true} if the stock pile can be recycled.
     * If the stock pile is empty, or if the stock pile index
     * is not currently beyond the end of the stock pile, this is {@code false}.
     * 
     * <p>If the stock pile is empty, it cannot be advanced or recycled.
     * Otherwise, either this or {@link #canAdvanceStockPile()}
     * will be {@code true}, and the other {@code false}.</p>
     * 
     * @return {@code true} if the stock pile can be recycled
     */
    public boolean canRecycleStockPile() {
        // 0 represents no cards flipped, so offset by one
        return !stockPile.isEmpty() && stockPileIndex >= stockPile.size();
    }
    
    /**
     * Returns the current card that can be drawn from the stock pile.
     * 
     * @return the current card available from the stock pile
     * @throws IndexOutOfBoundsException if no card is available
     *         to be drawn from the stock pile
     */
    public Card getStockPileCard() {
        return stockPile.get(stockPileIndex - 1);
    }
    
    /**
     * Returns the top card on the foundation for the given suit.
     * 
     * @param suit the suit
     * @return the current top card for the suit in the foundation
     * @throws IndexOutOfBoundsException if the foundation has no cards for the given suit
     */
    public Card getTopOfFoundation(final Suit suit) {
        final List<Card> foundationForSuit = foundation.get(suit);
        return foundationForSuit.get(foundationForSuit.size() - 1);
    }
    
    /**
     * Returns a copy of the foundation with the given card added to it.
     * 
     * <p>This does no validation that the move is legal.</p>
     * 
     * @param card the card to add to the foundation
     * @return a copy of the foundation with one card added
     */
    public Map<Suit, List<Card>> getFoundationPlusCard(final Card card) {
        final Map<Suit, List<Card>> newFoundation = new EnumMap<>(foundation);
        final Suit suit = card.getSuit();
        final List<Card> stackForSuit = newFoundation.get(suit);
        final List<Card> newStackForSuit = new ArrayList<>(13);
        newStackForSuit.addAll(stackForSuit);
        newStackForSuit.add(card);
        newFoundation.put(suit, newStackForSuit);
        
        return newFoundation;
    }
    
    /**
     * Extracts the top card from the foundation for the given suit, and returns
     * both the card and the remaining foundation missing the card.
     * 
     * @param suit the suit of the foundation from which to remove the top card
     * @return a pair of the card and the remaining foundation
     */
    public Pair<Card, Map<Suit, List<Card>>> extractCardFromFoundation(final Suit suit) {
    	final List<Card> foundationForSuit = foundation.get(suit);
		final int suitCountMinusOne = foundationForSuit.size() - 1;
		final Card card = foundationForSuit.get(suitCountMinusOne);
		final Map<Suit, List<Card>> newFoundation = new EnumMap<>(foundation);
		final List<Card> newStackForSuit = new ArrayList<>(foundationForSuit.subList(0, suitCountMinusOne));
		newFoundation.put(suit, newStackForSuit);
		return new Pair<>(card, newFoundation);
    }
    
    /**
     * Extracts the currently visible card from the stock pile, and returns
     * both the card and the remaining stock pile missing the card.
     * 
     * @return a pair of the card, and the remaining stock pile
     * @throws IndexOutOfBoundsException if no card is available
     *         to be drawn from the stock pile
     */
    public Pair<Card, List<Card>> extractStockPileCard() {
    	final Card card = getStockPileCard();
		/*
		 * StockPile: [a, b, c]
		 * StockPileIndex: 2
		 * 
		 * Result: [a, c]
		 */
		final int size = stockPile.size();
		final List<Card> newStockPile = new ArrayList<>(size - 1);
		newStockPile.addAll(stockPile.subList(0, stockPileIndex - 1));
		newStockPile.addAll(stockPile.subList(stockPileIndex, size));
		return new Pair<Card, List<Card>>(card, newStockPile);
    }
    
    /**
     * Returns whether it would be legal to add the given card to the foundation.
     * 
     * @param card the card to check
     * @return {@code true} if it is legal to add the card to the foundation
     */
    public boolean canAddToFoundation(final Card card) {
    	return card.getValue().getValue() == 1 + foundation.get(card.getSuit()).size();
    }
    
    private void printCardTo(final PrintStream out, final Card card) {
    	out.format(Locale.US, "%2s%s", card.getValue().toSymbol(), card.getSuit().toSymbol());
    }
    
    /**
     * Prints an ASCII art representation of the board to the given output stream.
     * 
     * @param out the output stream
     */
    public void printTo(final PrintStream out) {
    	for (final Map.Entry<Suit, List<Card>> entry : foundation.entrySet()) {
    		out.format(Locale.US, "%2s", "");
    		final List<Card> cards = entry.getValue();
    		if (cards.isEmpty()) {
    			out.format(Locale.US, "%3s", "--");
    		} else {
    			printCardTo(out, cards.get(cards.size() - 1));
    		}
    	}
    	out.format(Locale.US, "%3s", "");
    	out.format(Locale.US, "(%2d/%2d)", stockPileIndex, stockPile.size());
    	out.format(Locale.US, "%2s", "");
    	if (stockPileIndex > 0) {
    		printCardTo(out, stockPile.get(stockPileIndex - 1));
    	} else {
    		out.format(Locale.US, "%3s", "");
    	}
    	out.println();
    	final List<Iterator<Card>> iterators = new ArrayList<Iterator<Card>>(columns.size());
    	for (final Column column : columns) {
        	out.format(Locale.US, "%2s", "");
        	out.format(Locale.US, "(%d)", column.getNumberOfFaceDownCards());
    		iterators.add(column.getFaceUpCards().iterator());
    	}
    	out.println();
    	boolean printedSomething;
    	do {
    		printedSomething = false;
    		for (final Iterator<Card> iterator : iterators) {
    			out.format(Locale.US, "%2s", "");
    			if (iterator.hasNext()) {
    				final Card card = iterator.next();
    				printCardTo(out, card);
    				printedSomething = true;
    			} else {
    				out.format(Locale.US, "%3s", "");
    			}
    		}
    		out.println();
    	} while (printedSomething);
    }
    
    @Override
    public int hashCode() {
        return 0xc284f7a1 ^ columns.hashCode() ^ Integer.rotateLeft(stockPile.hashCode(), 8)
                ^ Integer.rotateLeft(stockPileIndex, 16) ^ Integer.rotateLeft(foundation.hashCode(), 24);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Board) {
            final Board board = (Board) obj;
            // Put stockPileIndex first since it changes a lot.
            if (stockPileIndex != board.stockPileIndex) {
            	return false;
            }
            if (stockPile.size() != board.stockPile.size()) {
                return false;
            }
            for (final Suit suit : Suit.values()) {
                if (foundation.get(suit).size() != board.foundation.get(suit).size()) {
                    return false;
                }
            }
            return columns.equals(board.columns)
            		&& foundation.equals(board.foundation)
                    && stockPile.equals(board.stockPile);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Board {columns: " + columns + ", stockPile: " + stockPile + ", stockPileIndex: " + stockPileIndex + ", foundation: " + foundation
                + "}";
    }
    
}
