package org.silnith.game.solitaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.silnith.deck.Suit.CLUB;
import static org.silnith.deck.Suit.DIAMOND;
import static org.silnith.deck.Suit.HEART;
import static org.silnith.deck.Suit.SPADE;
import static org.silnith.deck.Value.ACE;
import static org.silnith.deck.Value.EIGHT;
import static org.silnith.deck.Value.FIVE;
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.JACK;
import static org.silnith.deck.Value.KING;
import static org.silnith.deck.Value.NINE;
import static org.silnith.deck.Value.QUEEN;
import static org.silnith.deck.Value.SEVEN;
import static org.silnith.deck.Value.SIX;
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


public class BoardTest {
    
    private final List<Card> emptyListOfCards = Collections.emptyList();
    
    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);
    
    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public BoardTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }
    
    @Test
    public void testCanAddToFoundationEmptyAce() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	assertTrue(board.canAddToFoundation(new Card(ACE, CLUB)));
    }
    
    @Test
    public void testCanAddToFoundationEmptyTwo() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	assertFalse(board.canAddToFoundation(new Card(TWO, CLUB)));
    }
    
    @Test
    public void testCanAddToFoundationEmptyKing() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	assertFalse(board.canAddToFoundation(new Card(KING, CLUB)));
    }
    
    @Test
    public void testCanAddToFoundationLessThan() {
    	final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
    	foundation.put(CLUB, Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(TWO, CLUB),
    			new Card(THREE, CLUB),
    			new Card(FOUR, CLUB)));
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, foundation);
    	
    	assertFalse(board.canAddToFoundation(new Card(THREE, CLUB)));
    }
    
    @Test
    public void testCanAddToFoundationEquals() {
    	final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
    	foundation.put(CLUB, Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(TWO, CLUB),
    			new Card(THREE, CLUB),
    			new Card(FOUR, CLUB)));
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, foundation);
    	
    	assertFalse(board.canAddToFoundation(new Card(FOUR, CLUB)));
    }
    
    @Test
    public void testCanAddToFoundationValid() {
    	final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
    	foundation.put(CLUB, Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(TWO, CLUB),
    			new Card(THREE, CLUB),
    			new Card(FOUR, CLUB)));
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, foundation);
    	
    	assertTrue(board.canAddToFoundation(new Card(FIVE, CLUB)));
    }
    
    @Test
    public void testCanAddToFoundationGreaterThan() {
    	final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
    	foundation.put(CLUB, Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(TWO, CLUB),
    			new Card(THREE, CLUB),
    			new Card(FOUR, CLUB)));
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, foundation);
    	
    	assertFalse(board.canAddToFoundation(new Card(SIX, CLUB)));
    }
    
    @Test
    public void testEqualsEmpty() {
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeEmpty() {
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testEqualsOneStack() {
        final List<Card> stack = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, SPADE),
                new Card(TEN, HEART));
                
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(6, new Column(null, stack));
        
        final Board board1 = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeOneStack() {
        final List<Card> stack = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, SPADE),
                new Card(TEN, HEART));
                
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(6, new Column(null, stack));
        
        final Board board1 = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testEqualsTwoStacks() {
        final List<Card> stack1 = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, SPADE),
                new Card(TEN, HEART));
        final List<Card> stack2 = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(FIVE, DIAMOND));
        
        final List<Column> columns1 = new ArrayList<>(emptyColumns);
        columns1.set(2, new Column(stack2, null));
        columns1.set(6, new Column(null, stack1));
        
        final List<Column> columns2 = new ArrayList<>(emptyColumns);
        columns2.set(2, new Column(stack2.subList(0, 1), stack2.subList(1, 2)));
        columns2.set(6, new Column(null, stack1));
        
        final Board board1 = new Board(columns1, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(columns2, emptyListOfCards, 0, emptyFoundation);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeTwoStacks() {
        final List<Card> stack1 = Arrays.asList(new Card(KING, CLUB), new Card(QUEEN, DIAMOND), new Card(JACK, SPADE),
                new Card(TEN, HEART));
        final List<Card> stack2 = Arrays.asList(new Card(TWO, SPADE), new Card(FIVE, DIAMOND));
        
        final List<Column> columns1 = new ArrayList<>(emptyColumns);
        columns1.set(6, new Column(null, stack1));
        columns1.set(2, new Column(stack2, null));
        
        final List<Column> columns2 = new ArrayList<>(emptyColumns);
        columns2.set(6, new Column(null, stack1));
        columns2.set(2, new Column(stack2.subList(0, 1), stack2.subList(1, 2)));
        
        final Board board1 = new Board(columns1, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(columns2, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testEqualsTwoStacksDifferentColumns() {
        final List<Card> stack1 = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, SPADE),
                new Card(TEN, HEART));
        final List<Card> stack2 = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(FIVE, DIAMOND));
        
        final List<Column> columns1 = new ArrayList<>(emptyColumns);
        columns1.set(2, new Column(stack2, null));
        columns1.set(6, new Column(null, stack1));
        
        final List<Column> columns2 = new ArrayList<>(emptyColumns);
        columns2.set(3, new Column(stack2.subList(0, 1), stack2.subList(1, 2)));
        columns2.set(6, new Column(null, stack1));
        
        final Board board1 = new Board(columns1, emptyListOfCards, 0, emptyFoundation);
        final Board board2 = new Board(columns2, emptyListOfCards, 0, emptyFoundation);
        
        assertFalse(board1.equals(board2));
    }
    
    @Test
    public void testEqualsStockPile() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
        		new Card(EIGHT, HEART),
                new Card(EIGHT, CLUB),
                new Card(EIGHT, DIAMOND),
                new Card(KING, DIAMOND),
                new Card(QUEEN, CLUB),
                new Card(ACE, HEART));
                
        final Board board1 = new Board(emptyColumns, stockPile, 0, emptyFoundation);
        final Board board2 = new Board(emptyColumns, stockPile, 0, emptyFoundation);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeStockPile() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
        		new Card(EIGHT, HEART),
                new Card(EIGHT, CLUB),
                new Card(EIGHT, DIAMOND),
                new Card(KING, DIAMOND),
                new Card(QUEEN, CLUB),
                new Card(ACE, HEART));
                
        final Board board1 = new Board(emptyColumns, stockPile, 0, emptyFoundation);
        final Board board2 = new Board(emptyColumns, stockPile, 0, emptyFoundation);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testNotEqualsStockPile() {
        final List<Card> stockPile1 = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
        		new Card(EIGHT, HEART),
                new Card(EIGHT, CLUB),
                new Card(EIGHT, DIAMOND),
                new Card(KING, DIAMOND),
                new Card(QUEEN, CLUB),
                new Card(ACE, HEART));
        final List<Card> stockPile2 = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
        		new Card(EIGHT, CLUB),
                new Card(EIGHT, DIAMOND),
                new Card(KING, DIAMOND),
                new Card(QUEEN, CLUB),
                new Card(ACE, HEART));
                
        final Board board1 = new Board(emptyColumns, stockPile1, 0, emptyFoundation);
        final Board board2 = new Board(emptyColumns, stockPile2, 0, emptyFoundation);
        
        assertFalse(board1.equals(board2));
    }
    
    @Test
    public void testEqualsStockPileIndex() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
        		new Card(EIGHT, HEART),
                new Card(EIGHT, CLUB),
                new Card(EIGHT, DIAMOND),
                new Card(KING, DIAMOND),
                new Card(QUEEN, CLUB),
                new Card(ACE, HEART));
                
        final Board board1 = new Board(emptyColumns, stockPile, 8, emptyFoundation);
        final Board board2 = new Board(emptyColumns, stockPile, 8, emptyFoundation);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeStockPileIndex() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
        		new Card(EIGHT, HEART),
                new Card(EIGHT, CLUB),
                new Card(EIGHT, DIAMOND),
                new Card(KING, DIAMOND),
                new Card(QUEEN, CLUB),
                new Card(ACE, HEART));
                
        final Board board1 = new Board(emptyColumns, stockPile, 8, emptyFoundation);
        final Board board2 = new Board(emptyColumns, stockPile, 8, emptyFoundation);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testEqualsFoundation() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        foundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB),
        		new Card(FIVE, CLUB),
        		new Card(SIX, CLUB),
        		new Card(SEVEN, CLUB),
        		new Card(EIGHT, CLUB),
        		new Card(NINE, CLUB),
        		new Card(TEN, CLUB),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB)));
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation.put(SPADE, emptyListOfCards);
        
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, foundation);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, foundation);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeFoundation() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        foundation.put(CLUB,
                Arrays.asList(
                		new Card(ACE, CLUB),
                		new Card(TWO, CLUB),
                		new Card(THREE, CLUB),
                		new Card(FOUR, CLUB),
                        new Card(FIVE, CLUB),
                        new Card(SIX, CLUB),
                        new Card(SEVEN, CLUB),
                        new Card(EIGHT, CLUB),
                        new Card(NINE, CLUB),
                        new Card(TEN, CLUB),
                        new Card(JACK, CLUB),
                        new Card(QUEEN, CLUB)));
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation.put(SPADE, emptyListOfCards);
        
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, foundation);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, foundation);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testEqualsFoundationCopy() {
        final Map<Suit, List<Card>> foundation1 = new EnumMap<>(Suit.class);
        foundation1.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB),
        		new Card(FIVE, CLUB),
        		new Card(SIX, CLUB),
        		new Card(SEVEN, CLUB),
        		new Card(EIGHT, CLUB),
        		new Card(NINE, CLUB),
        		new Card(TEN, CLUB),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB)));
        foundation1.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation1.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation1.put(SPADE, emptyListOfCards);
        
        final Map<Suit, List<Card>> foundation2 = new EnumMap<>(Suit.class);
        foundation2.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB),
        		new Card(FIVE, CLUB),
        		new Card(SIX, CLUB),
        		new Card(SEVEN, CLUB),
        		new Card(EIGHT, CLUB),
        		new Card(NINE, CLUB),
        		new Card(TEN, CLUB),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB)));
        foundation2.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation2.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation2.put(SPADE, emptyListOfCards);
        
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, foundation1);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, foundation2);
        
        assertTrue(board1.equals(board2));
    }
    
    @Test
    public void testHashCodeFoundationCopy() {
        final Map<Suit, List<Card>> foundation1 = new EnumMap<>(Suit.class);
        foundation1.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB),
        		new Card(FIVE, CLUB),
        		new Card(SIX, CLUB),
        		new Card(SEVEN, CLUB),
        		new Card(EIGHT, CLUB),
        		new Card(NINE, CLUB),
        		new Card(TEN, CLUB),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB)));
        foundation1.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation1.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation1.put(SPADE, emptyListOfCards);
        
        final Map<Suit, List<Card>> foundation2 = new EnumMap<>(Suit.class);
        foundation2.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB),
        		new Card(FIVE, CLUB),
        		new Card(SIX, CLUB),
        		new Card(SEVEN, CLUB),
        		new Card(EIGHT, CLUB),
        		new Card(NINE, CLUB),
        		new Card(TEN, CLUB),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB)));
        foundation2.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation2.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation2.put(SPADE, emptyListOfCards);
        
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, foundation1);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, foundation2);
        
        assertEquals(board1.hashCode(), board2.hashCode());
    }
    
    @Test
    public void testNotEqualsFoundation() {
        final Map<Suit, List<Card>> foundation1 = new EnumMap<>(Suit.class);
        foundation1.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB),
        		new Card(FIVE, CLUB),
        		new Card(SIX, CLUB),
        		new Card(SEVEN, CLUB),
        		new Card(EIGHT, CLUB),
        		new Card(NINE, CLUB),
        		new Card(TEN, CLUB),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB)));
        foundation1.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND)));
        foundation1.put(HEART, Arrays.asList(
        		new Card(ACE, HEART),
        		new Card(TWO, HEART),
        		new Card(THREE, HEART),
                new Card(FOUR, HEART)));
        foundation1.put(SPADE, emptyListOfCards);
        
        final Map<Suit, List<Card>> foundation2 = new EnumMap<>(foundation1);
        foundation2.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        
        final Board board1 = new Board(emptyColumns, emptyListOfCards, 0, foundation1);
        final Board board2 = new Board(emptyColumns, emptyListOfCards, 0, foundation2);
        
        assertFalse(board1.equals(board2));
    }
    
}
