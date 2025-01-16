package org.silnith.game.solitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
     * Deals a deck of cards into a new solitaire board.
     * 
     * @param deck the deck of cards to deal
     * @param numColumns the number of columns on the board.  In any standard game,
     *         this is {@code 7}.
     * @throws NoSuchElementException if the deck does not have enough cards to fill
     *         all of the columns.  This could happen if the deck is partial, or if
     *         the number of columns is greater than {@code 7}.
     */
    public Board(final List<Card> deck, final int numColumns) {
        super();
        
        int remaining = deck.size();
        final List<List<Card>> stacks = new ArrayList<>(numColumns);
        for (int i = 0; i < numColumns; i++ ) {
            stacks.add(new ArrayList<Card>(i + 1));
        }
        final Iterator<Card> iter = deck.iterator();
        for (int i = 0; i < numColumns; i++ ) {
            for (int j = i; j < numColumns; j++ ) {
                final Card card = iter.next();
                remaining-- ;
                stacks.get(j).add(card);
            }
        }
        
        final List<Column> tempColumns = new ArrayList<>(numColumns);
        for (final List<Card> stack : stacks) {
            tempColumns.add(new Column(stack, null));
        }
        
        // this.columns = Collections.unmodifiableList(tempColumns);
        this.columns = tempColumns;
        
        final List<Card> tempStockPile = new ArrayList<>(remaining);
        while (iter.hasNext()) {
            final Card card = iter.next();
            tempStockPile.add(card);
        }
        // this.stockPile = Collections.unmodifiableList(tempStockPile);
        this.stockPile = tempStockPile;
        this.stockPileIndex = 0;
        
        final Map<Suit, List<Card>> tempFoundation = new EnumMap<>(Suit.class);
        for (final Suit suit : Suit.values()) {
            final List<Card> cards = Collections.emptyList();
            tempFoundation.put(suit, cards);
        }
        
        // this.foundation = Collections.unmodifiableMap(tempFoundation);
        this.foundation = tempFoundation;
    }
    
    /**
     * Constructs a new board. All parameters must be immutable.
     */
    public Board(final List<Column> columns, final List<Card> stockPile, final int stockPileIndex,
            final Map<Suit, List<Card>> foundation) {
        super();
        this.columns = columns;
        if (stockPileIndex < 0) {
        	throw new IllegalArgumentException("Stock pile index must be non-negative.");
        }
        if (stockPileIndex > stockPile.size()) {
            throw new IllegalArgumentException("Stock pile index outside of stock pile.");
        }
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
        return stockPileIndex > 0 && stockPileIndex >= stockPile.size();
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
     * Returns a copy of the current board with one card drawn from the stock pile
     * and put into the foundation.
     * 
     * @return a copy of the board with one card moved
     */
    public Board drawStockPileCardToFoundation() {
    	final Pair<Card, List<Card>> pair = extractStockPileCard();
        final Card card = pair.getFirst();
        final List<Card> newStockPile = pair.getSecond();
        
        final int newStockPileIndex = stockPileIndex - 1;
        
        final Map<Suit, List<Card>> newFoundation = getFoundationPlusCard(card);
        
        return new Board(columns, newStockPile, newStockPileIndex, newFoundation);
    }
    
    /**
     * Returns a copy of this board with one card moved from the foundation
     * to the specified column run.
     * 
     * @param suit the suit from which to take a card from the foundation
     * @param index the index of the column that will receive the card
     * @return a copy of this board with one card moved from the foundation
     *         to a column
     */
    public Board moveCardFromFoundation(final Suit suit, final int index) {
        final Card card = getTopOfFoundation(suit);
        
        final Map<Suit, List<Card>> newFoundation = removeFromFoundation(suit);
        
        final Column column = columns.get(index);
        final Column newColumn = column.addNewCard(card);
        
        final List<Column> newColumns = new ArrayList<>(columns);
        newColumns.set(index, newColumn);
        
        return new Board(newColumns, stockPile, stockPileIndex, newFoundation);
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
        // newFoundation.put(suit, Collections.unmodifiableList(newStackForSuit));
        newFoundation.put(suit, newStackForSuit);
        
        // return Collections.unmodifiableMap(newFoundation);
        return newFoundation;
    }
    
    /**
     * Returns a copy of the foundation with the top element removed from the specified suit.
     * 
     * @param suit the suit from which to remove one card
     * @return a copy of the foundation with one card missing
     */
    private Map<Suit, List<Card>> removeFromFoundation(final Suit suit) {
        final Map<Suit, List<Card>> newFoundation = new EnumMap<>(foundation);
        final List<Card> stackForSuit = newFoundation.get(suit);
        // final List<Card> newStackForSuit = stackForSuit.subList(0,
        // stackForSuit.size() - 1);
        final List<Card> newStackForSuit = new ArrayList<>(stackForSuit.subList(0, stackForSuit.size() - 1));
        newFoundation.put(suit, newStackForSuit);
        
        // return Collections.unmodifiableMap(newFoundation);
        return newFoundation;
    }
    
    /**
     * Extracts the currently visible card from the stock pile, and returns
     * both the card and the remaining stock pile missing the card.
     * 
     * @return a pair of the card, and the remaining stock pile
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
    
    @Override
    public int hashCode() {
        return 0xc284f7a1 ^ columns.hashCode() ^ Integer.rotateLeft(stockPile.hashCode(), 8)
                ^ Integer.rotateLeft(stockPileIndex, 16) ^ Integer.rotateLeft(foundation.hashCode(), 24);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Board) {
            final Board board = (Board) obj;
            if (stockPile.size() != board.stockPile.size()) {
                return false;
            }
            for (final Suit suit : Suit.values()) {
                if (foundation.get(suit).size() != board.foundation.get(suit).size()) {
                    return false;
                }
            }
            // Put stockPileIndex first since it changes a lot.
            return stockPileIndex == board.stockPileIndex && columns.equals(board.columns) && foundation.equals(board.foundation)
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
