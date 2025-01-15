package org.silnith.game.solitaire;

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
    
    @Test
    public void testMoveRun() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final Board actual = board.moveRun(0, 1, 1);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveRunTooManyCards() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> board.moveRun(0, 1, 2));
    }
    
    @Test
    public void testMoveRunTooFewCards() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> board.moveRun(0, 1, 0));
    }
    
    @Test
    public void testMoveRunSameFromTo() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> board.moveRun(0, 0, 1));
    }
    
    @Test
    public void testMoveRunBig() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, HEART),
        		new Card(QUEEN, CLUB),
        		new Card(JACK, HEART),
                new Card(TEN, CLUB),
                new Card(NINE, HEART));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final Board actual = board.moveRun(0, 1, 5);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveRunOntoAnother() {
        final List<Card> topRun = Arrays.asList(
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB));
        final List<Card> bottomRun = Arrays.asList(
        		new Card(FIVE, HEART),
        		new Card(FOUR, CLUB),
        		new Card(THREE, HEART));
        final List<Card> combinedRun = Arrays.asList(
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB),
                new Card(FIVE, HEART),
                new Card(FOUR, CLUB),
                new Card(THREE, HEART));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, topRun));
        columns.set(4, new Column(null, bottomRun));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final Board actual = board.moveRun(4, 2, 3);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(2, new Column(null, combinedRun));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveRunPartialOntoAnother() {
        final List<Card> topRun = Arrays.asList(
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB));
        final List<Card> bottomRun = Arrays.asList(
        		new Card(SIX, SPADE),
        		new Card(FIVE, HEART),
        		new Card(FOUR, CLUB),
                new Card(THREE, HEART));
        final List<Card> combinedRun = Arrays.asList(
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB),
                new Card(FIVE, HEART),
                new Card(FOUR, CLUB),
                new Card(THREE, HEART));
        final List<Card> remainingRun = Arrays.asList(
        		new Card(SIX, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(5, new Column(null, topRun));
        columns.set(1, new Column(null, bottomRun));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final Board actual = board.moveRun(1, 5, 3);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(5, new Column(null, combinedRun));
        expectedColumns.set(1, new Column(null, remainingRun));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveRunKeepsStockPile() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        final List<Card> stockPile = Arrays.asList(
        		new Card(TWO, CLUB),
        		new Card(TEN, SPADE),
        		new Card(THREE, SPADE),
                new Card(THREE, HEART));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, stockPile, 0, emptyFoundation);
        
        final Board actual = board.moveRun(0, 1, 1);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, stockPile, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveRunKeepsStockPileIndex() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        final List<Card> stockPile = Arrays.asList(
        		new Card(TWO, CLUB),
        		new Card(TEN, SPADE),
        		new Card(THREE, SPADE),
                new Card(THREE, HEART));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, stockPile, 4, emptyFoundation);
        
        final Board actual = board.moveRun(0, 1, 1);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, stockPile, 4, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveRunKeepsFoundation() {
        final List<Card> run = Arrays.asList(new Card(KING, SPADE));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND),
        		new Card(THREE, DIAMOND)));
        foundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE)));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, foundation);
        
        final Board actual = board.moveRun(0, 1, 1);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, foundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveCardToFoundation() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, Arrays.asList(
        		new Card(ACE, CLUB))));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final Board actual = board.moveCardToFoundation(2);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB)));
        final Board expected = new Board(emptyColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveCardToFoundationFromEmptyColumn() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> board.moveCardToFoundation(2));
    }
    
    @Test
    public void testMoveCardToFoundationNonEmptyFoundation() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, Arrays.asList(
        		new Card(FOUR, CLUB))));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB)));
        final Board board = new Board(columns, emptyListOfCards, 0, foundation);
        
        final Board actual = board.moveCardToFoundation(2);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB),
        		new Card(FOUR, CLUB)));
        final Board expected = new Board(emptyColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawStockPileCardToColumn() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(KING, CLUB));
        final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
        
        final Board actual = board.drawStockPileCardToColumn(4);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(4, new Column(null, Arrays.asList(
        		new Card(KING, CLUB))));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawStockPileCardToColumnEmpty() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> board.drawStockPileCardToColumn(4));
    }
    
    @Test
    public void testDrawStockPileCardToColumnUnderflow() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(KING, CLUB));
        final Board board = new Board(emptyColumns, stockPile, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> board.drawStockPileCardToColumn(4));
    }
    
    @Test
    public void testDrawStockPileCardToColumnNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(FOUR, SPADE));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        final List<Card> run = Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND));
		columns.set(4, new Column(null, run));
        final Board board = new Board(columns, stockPile, 1, emptyFoundation);
        
        final Board actual = board.drawStockPileCardToColumn(4);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        final List<Card> expectedRun = Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
                new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND),
                new Card(FOUR, SPADE));
		expectedColumns.set(4, new Column(null, expectedRun));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawStockPileCardToColumnFromBeginningNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(SIX, SPADE),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(4, new Column(null, Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND))));
        final Board board = new Board(columns, stockPile, 1, emptyFoundation);
        
        final Board actual = board.drawStockPileCardToColumn(4);

        final List<Card> expectedStockPile = Arrays.asList(
        		new Card(SIX, SPADE),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB));
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(4, new Column(null, Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
                new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND),
                new Card(FOUR, SPADE))));
        final Board expected = new Board(expectedColumns, expectedStockPile, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawStockPileCardToColumnFromMiddleNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(SIX, SPADE),
        		new Card(FOUR, SPADE),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(4, new Column(null, Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND))));
        final Board board = new Board(columns, stockPile, 2, emptyFoundation);
        
        final Board actual = board.drawStockPileCardToColumn(4);

        final List<Card> expectedStockPile = Arrays.asList(
        		new Card(SIX, SPADE),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB));
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(4, new Column(null, Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
                new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND),
                new Card(FOUR, SPADE))));
        final Board expected = new Board(expectedColumns, expectedStockPile, 1, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawStockPileCardToColumnFromEndNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(SIX, SPADE),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB),
        		new Card(FOUR, SPADE));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(4, new Column(null, Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND))));
        final Board board = new Board(columns, stockPile, 4, emptyFoundation);
        
        final Board actual = board.drawStockPileCardToColumn(4);

        final List<Card> expectedStockPile = Arrays.asList(
        		new Card(SIX, SPADE),
        		new Card(JACK, CLUB),
        		new Card(QUEEN, CLUB));
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(4, new Column(null, Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
                new Card(SIX, CLUB),
                new Card(FIVE, DIAMOND),
                new Card(FOUR, SPADE))));
        final Board expected = new Board(expectedColumns, expectedStockPile, 3, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawStockPileCardToColumnKeepsFoundation() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND),
        		new Card(THREE, DIAMOND)));
        final List<Card> stockPile = Arrays.asList(
        		new Card(KING, CLUB));
        final Board board = new Board(emptyColumns, stockPile, 1, foundation);
        
        final Board actual = board.drawStockPileCardToColumn(4);

        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(4, new Column(null, Arrays.asList(
        		new Card(KING, CLUB))));
        final Board expected = new Board(columns, emptyListOfCards, 0, foundation);
        
        assertEquals(expected, actual);
    }
    
}
