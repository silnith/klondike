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
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.NINE;
import static org.silnith.deck.Value.SEVEN;
import static org.silnith.deck.Value.TEN;
import static org.silnith.deck.Value.THREE;
import static org.silnith.deck.Value.TWO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;


public class AdvanceStockPileMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();
    
    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);
    
    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public AdvanceStockPileMoveTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }
    
    @Test
    public void testZeroIncrement() {
    	assertThrows(IllegalArgumentException.class, () -> {
    		new AdvanceStockPileMove(0, 0);
    	});
    }
    
    @Test
    public void testNegativeIncrement() {
    	assertThrows(IllegalArgumentException.class, () -> {
    		new AdvanceStockPileMove(0, -1);
    	});
    }
    
    @Test
    public void testGetBeginningIndex() {
        final AdvanceStockPileMove move = new AdvanceStockPileMove(17, 34);
        
        assertEquals(17, move.getBeginningIndex());
    }
    
    @Test
    public void testGetIncrement() {
        final AdvanceStockPileMove move = new AdvanceStockPileMove(17, 34);
        
        assertEquals(34, move.getIncrement());
    }
    
    @Test
    public void testCoalesceBeginningIndex() {
        final AdvanceStockPileMove firstMove = new AdvanceStockPileMove(20, 4);
        final AdvanceStockPileMove secondMove = new AdvanceStockPileMove(24, 5);
        
        final AdvanceStockPileMove coalescedMove = firstMove.coalesce(secondMove);
        
        assertEquals(20, coalescedMove.getBeginningIndex());
    }
    
    @Test
    public void testCoalesceIncrement() {
        final AdvanceStockPileMove firstMove = new AdvanceStockPileMove(20, 4);
        final AdvanceStockPileMove secondMove = new AdvanceStockPileMove(24, 5);
        
        final AdvanceStockPileMove coalescedMove = firstMove.coalesce(secondMove);
        
        assertEquals(9, coalescedMove.getIncrement());
    }
    
    @Test
    public void testCoalesceNull() {
        final AdvanceStockPileMove move = new AdvanceStockPileMove(17, 34);
        
        assertThrows(NullPointerException.class, () -> move.coalesce(null));
    }
    
    @Test
    public void testHasCards() {
        final AdvanceStockPileMove move = new AdvanceStockPileMove(17, 34);
        
        assertFalse(move.hasCards());
    }
    
    @Test
    public void testGetCards() {
        final AdvanceStockPileMove move = new AdvanceStockPileMove(17, 34);
        
        assertThrows(RuntimeException.class, () -> move.getCards());
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
    	
        final AdvanceStockPileMove move = new AdvanceStockPileMove(3, board);
        
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
    	
        final AdvanceStockPileMove move = new AdvanceStockPileMove(3, board);
        
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
    	
        final AdvanceStockPileMove move = new AdvanceStockPileMove(3, board);
        
        final Board actual = move.apply(board);
        
        final Board expected = new Board(emptyColumns, stockPile, 5, foundation);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        final AdvanceStockPileMove move1 = new AdvanceStockPileMove(17, 34);
        final AdvanceStockPileMove move2 = new AdvanceStockPileMove(17, 34);
        
        assertTrue(move1.equals(move2));
    }
    
    @Test
    public void testHashCode() {
        final AdvanceStockPileMove move1 = new AdvanceStockPileMove(17, 34);
        final AdvanceStockPileMove move2 = new AdvanceStockPileMove(17, 34);
        
        assertEquals(move1.hashCode(), move2.hashCode());
    }
    
    @Test
    public void testEqualsNull() {
        final AdvanceStockPileMove move = new AdvanceStockPileMove(17, 34);
        
        assertFalse(move.equals(null));
    }
    
    @Test
    public void testEqualsDifferentBeginningIndex() {
        final AdvanceStockPileMove move1 = new AdvanceStockPileMove(17, 34);
        final AdvanceStockPileMove move2 = new AdvanceStockPileMove(20, 34);
        
        assertFalse(move1.equals(move2));
    }
    
    @Test
    public void testEqualsDifferentIncrement() {
        final AdvanceStockPileMove move1 = new AdvanceStockPileMove(17, 34);
        final AdvanceStockPileMove move2 = new AdvanceStockPileMove(17, 3);
        
        assertFalse(move1.equals(move2));
    }
    
    @Test
    public void testEqualsDifferent() {
        final AdvanceStockPileMove move1 = new AdvanceStockPileMove(17, 34);
        final AdvanceStockPileMove move2 = new AdvanceStockPileMove(20, 3);
        
        assertFalse(move1.equals(move2));
    }
    
}
