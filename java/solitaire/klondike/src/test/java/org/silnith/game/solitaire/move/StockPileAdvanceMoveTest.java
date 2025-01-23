package org.silnith.game.solitaire.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.silnith.deck.Suit.CLUB;
import static org.silnith.deck.Suit.DIAMOND;
import static org.silnith.deck.Suit.HEART;
import static org.silnith.deck.Suit.SPADE;
import static org.silnith.deck.Value.ACE;
import static org.silnith.deck.Value.EIGHT;
import static org.silnith.deck.Value.FIVE;
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.KING;
import static org.silnith.deck.Value.NINE;
import static org.silnith.deck.Value.SEVEN;
import static org.silnith.deck.Value.TEN;
import static org.silnith.deck.Value.THREE;
import static org.silnith.deck.Value.TWO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;


public class StockPileAdvanceMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();
    
    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);
    
    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public StockPileAdvanceMoveTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }
    
    @Test
    public void testFindMovesEmptyStockPile() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	final Collection<StockPileAdvanceMove> actual = StockPileAdvanceMove.findMoves(3, board);
    	
    	final Collection<StockPileAdvanceMove> expected = Collections.emptySet();
		assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void testFindMovesStockPileAtBeginning() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(FIVE, DIAMOND),
    			new Card(KING, SPADE));
    	final Board board = new Board(emptyColumns, stockPile, 0, emptyFoundation);
    	
    	final Collection<StockPileAdvanceMove> actual = StockPileAdvanceMove.findMoves(3, board);
    	
    	final Collection<StockPileAdvanceMove> expected = Collections.singleton(
    			new StockPileAdvanceMove(0, 3));
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void testFindMovesStockPileInMiddle() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(FIVE, DIAMOND),
    			new Card(KING, SPADE));
    	final Board board = new Board(emptyColumns, stockPile, 2, emptyFoundation);
    	
    	final Collection<StockPileAdvanceMove> actual = StockPileAdvanceMove.findMoves(3, board);
    	
    	final Collection<StockPileAdvanceMove> expected = Collections.singleton(
    			new StockPileAdvanceMove(2, 3));
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void testFindMovesStockPileAtEnd() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(FIVE, DIAMOND),
    			new Card(KING, SPADE));
    	final Board board = new Board(emptyColumns, stockPile, 3, emptyFoundation);
    	
    	final Collection<StockPileAdvanceMove> actual = StockPileAdvanceMove.findMoves(3, board);
    	
    	final Collection<StockPileAdvanceMove> expected = Collections.emptySet();
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void testZeroIncrement() {
    	assertThrows(IllegalArgumentException.class, () -> {
    		new StockPileAdvanceMove(0, 0);
    	});
    }
    
    @Test
    public void testNegativeIncrement() {
    	assertThrows(IllegalArgumentException.class, () -> {
    		new StockPileAdvanceMove(0, -1);
    	});
    }
    
    @Test
    public void testGetBeginningIndex() {
        final StockPileAdvanceMove move = new StockPileAdvanceMove(17, 34);
        
        assertEquals(17, move.getBeginningIndex());
    }
    
    @Test
    public void testGetIncrement() {
        final StockPileAdvanceMove move = new StockPileAdvanceMove(17, 34);
        
        assertEquals(34, move.getIncrement());
    }
    
    @Test
    public void testCoalesceBeginningIndex() {
        final StockPileAdvanceMove firstMove = new StockPileAdvanceMove(20, 4);
        final StockPileAdvanceMove secondMove = new StockPileAdvanceMove(24, 5);
        
        final StockPileAdvanceMove coalescedMove = firstMove.coalesce(secondMove);
        
        assertEquals(20, coalescedMove.getBeginningIndex());
    }
    
    @Test
    public void testCoalesceIncrement() {
        final StockPileAdvanceMove firstMove = new StockPileAdvanceMove(20, 4);
        final StockPileAdvanceMove secondMove = new StockPileAdvanceMove(24, 5);
        
        final StockPileAdvanceMove coalescedMove = firstMove.coalesce(secondMove);
        
        assertEquals(9, coalescedMove.getIncrement());
    }
    
    @Test
    public void testCoalesceNull() {
        final StockPileAdvanceMove move = new StockPileAdvanceMove(17, 34);
        
        assertThrows(NullPointerException.class, () -> move.coalesce(null));
    }
    
    @Test
    public void testHasCards() {
        final StockPileAdvanceMove move = new StockPileAdvanceMove(17, 34);
        
        assertFalse(move.hasCards());
    }
    
    @Test
    public void testGetCards() {
        final StockPileAdvanceMove move = new StockPileAdvanceMove(17, 34);
        
        assertEquals(Collections.emptyList(), move.getCards());
    }
    
    @Test
    public void testApply() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(Value.ACE, Suit.CLUB),
    			new Card(Value.TWO, Suit.CLUB),
    			new Card(Value.THREE, Suit.CLUB),
    			new Card(Value.FOUR, Suit.CLUB),
    			new Card(Value.FIVE, Suit.CLUB),
    			new Card(Value.SIX, Suit.CLUB));
    	final Board board = new Board(emptyColumns, stockPile, 2, emptyFoundation);
    	
        final StockPileAdvanceMove move = new StockPileAdvanceMove(3, board);
        
        final Board actual = move.apply(board);
        
        final Board expected = new Board(emptyColumns, stockPile, 5, emptyFoundation);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsColumns() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(1, new Column(null, Arrays.asList(
        		new Card(FOUR, DIAMOND),
        		new Card(THREE, SPADE),
                new Card(TWO, DIAMOND),
                new Card(ACE, SPADE))));
        columns.set(2, new Column(
        		Arrays.asList(
        				new Card(FOUR, CLUB),
        				new Card(TEN, DIAMOND)),
        		Arrays.asList(
        				new Card(TEN, CLUB),
                        new Card(NINE, HEART),
                        new Card(EIGHT, CLUB),
                        new Card(SEVEN, HEART))));
    	final List<Card> stockPile = Arrays.asList(
    			new Card(Value.ACE, Suit.CLUB),
    			new Card(Value.TWO, Suit.CLUB),
    			new Card(Value.THREE, Suit.CLUB),
    			new Card(Value.FOUR, Suit.CLUB),
    			new Card(Value.FIVE, Suit.CLUB),
    			new Card(Value.SIX, Suit.CLUB));
    	final Board board = new Board(columns, stockPile, 2, emptyFoundation);
    	
        final StockPileAdvanceMove move = new StockPileAdvanceMove(3, board);
        
        final Board actual = move.apply(board);
        
        final Board expected = new Board(columns, stockPile, 5, emptyFoundation);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsFoundation() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND),
        		new Card(THREE, DIAMOND)));
    	final List<Card> stockPile = Arrays.asList(
    			new Card(Value.ACE, Suit.CLUB),
    			new Card(Value.TWO, Suit.CLUB),
    			new Card(Value.THREE, Suit.CLUB),
    			new Card(Value.FOUR, Suit.CLUB),
    			new Card(Value.FIVE, Suit.CLUB),
    			new Card(Value.SIX, Suit.CLUB));
    	final Board board = new Board(emptyColumns, stockPile, 2, foundation);
    	
        final StockPileAdvanceMove move = new StockPileAdvanceMove(3, board);
        
        final Board actual = move.apply(board);
        
        final Board expected = new Board(emptyColumns, stockPile, 5, foundation);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        final StockPileAdvanceMove move1 = new StockPileAdvanceMove(17, 34);
        final StockPileAdvanceMove move2 = new StockPileAdvanceMove(17, 34);
        
        assertTrue(move1.equals(move2));
    }
    
    @Test
    public void testHashCode() {
        final StockPileAdvanceMove move1 = new StockPileAdvanceMove(17, 34);
        final StockPileAdvanceMove move2 = new StockPileAdvanceMove(17, 34);
        
        assertEquals(move1.hashCode(), move2.hashCode());
    }
    
    @Test
    public void testEqualsNull() {
        final StockPileAdvanceMove move = new StockPileAdvanceMove(17, 34);
        
        assertFalse(move.equals(null));
    }
    
    @Test
    public void testEqualsDifferentBeginningIndex() {
        final StockPileAdvanceMove move1 = new StockPileAdvanceMove(17, 34);
        final StockPileAdvanceMove move2 = new StockPileAdvanceMove(20, 34);
        
        assertFalse(move1.equals(move2));
    }
    
    @Test
    public void testEqualsDifferentIncrement() {
        final StockPileAdvanceMove move1 = new StockPileAdvanceMove(17, 34);
        final StockPileAdvanceMove move2 = new StockPileAdvanceMove(17, 3);
        
        assertFalse(move1.equals(move2));
    }
    
    @Test
    public void testEqualsDifferent() {
        final StockPileAdvanceMove move1 = new StockPileAdvanceMove(17, 34);
        final StockPileAdvanceMove move2 = new StockPileAdvanceMove(20, 3);
        
        assertFalse(move1.equals(move2));
    }
    
}
