package org.silnith.game.solitaire.move;

import static org.junit.jupiter.api.Assertions.*;
import static org.silnith.deck.Suit.CLUB;
import static org.silnith.deck.Suit.DIAMOND;
import static org.silnith.deck.Suit.HEART;
import static org.silnith.deck.Suit.SPADE;
import static org.silnith.deck.Value.ACE;
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
import org.silnith.deck.Value;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;

public class RunMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();

    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);

    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public RunMoveTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }

	@Test
	public void testGetSourceColumn() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move = new RunMove(2, 5, 3, run);
		
		assertEquals(2, move.getSourceColumn());
	}

	@Test
	public void testGetDestinationColumn() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move = new RunMove(2, 5, 3, run);
		
		assertEquals(5, move.getDestinationColumn());
	}

	@Test
	public void testGetNumberOfCards() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move = new RunMove(2, 5, 3, run);
		
		assertEquals(3, move.getNumberOfCards());
	}

	@Test
	public void testHasCards() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move = new RunMove(2, 5, 3, run);
		
		assertTrue(move.hasCards());
	}

	@Test
	public void testGetCards() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move = new RunMove(2, 5, 3, run);
		
		assertEquals(run, move.getCards());
	}

	@Test
	public void testConstructorGetCards() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final List<Column> columns = new ArrayList<>(emptyColumns);
		columns.set(2, new Column(null, run));
		final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
		
		final RunMove move = new RunMove(2, 5, 3, board);
		
		assertEquals(run, move.getCards());
	}
    
    @Test
    public void testApply() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final RunMove move = new RunMove(0, 1, 1, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyTooManyCards() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);

        final RunMove move = new RunMove(0, 1, 2, run);
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyTooFewCards() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final RunMove move = new RunMove(0, 1, 0, run);
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplySameFromTo() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, SPADE));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final RunMove move = new RunMove(0, 0, 1, run);
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyBig() {
        final List<Card> run = Arrays.asList(
        		new Card(KING, HEART),
        		new Card(QUEEN, CLUB),
        		new Card(JACK, HEART),
                new Card(TEN, CLUB),
                new Card(NINE, HEART));
        
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(0, new Column(null, run));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);

        final RunMove move = new RunMove(0, 1, 5, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyOntoAnother() {
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

        final RunMove move = new RunMove(4, 2, 3, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(2, new Column(null, combinedRun));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyPartialOntoAnother() {
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
        
        final RunMove move = new RunMove(1, 5, 3, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(5, new Column(null, combinedRun));
        expectedColumns.set(1, new Column(null, remainingRun));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsStockPile() {
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

        final RunMove move = new RunMove(0, 1, 1, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, stockPile, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsStockPileIndex() {
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

        final RunMove move = new RunMove(0, 1, 1, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, stockPile, 4, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsFoundation() {
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

        final RunMove move = new RunMove(0, 1, 1, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(1, new Column(null, run));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, foundation);
        
        assertEquals(expected, actual);
    }

	@Test
	public void testEquals() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move1 = new RunMove(2, 5, 3, run);
		final RunMove move2 = new RunMove(2, 5, 3, run);
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move1 = new RunMove(2, 5, 3, run);
		final RunMove move2 = new RunMove(2, 5, 3, run);
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentSourceColumn() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move1 = new RunMove(2, 5, 3, run);
		final RunMove move2 = new RunMove(1, 5, 3, run);
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentDestinationColumn() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move1 = new RunMove(2, 5, 3, run);
		final RunMove move2 = new RunMove(2, 4, 3, run);
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentNumberOfCards() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move1 = new RunMove(2, 5, 3, run);
		// Note that the number of cards and the run length differ.
		final RunMove move2 = new RunMove(2, 5, 2, run);
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentCards() {
		final List<Card> run1 = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final List<Card> run2 = Arrays.asList(
				new Card(Value.THREE, Suit.SPADE),
				new Card(Value.TWO, Suit.DIAMOND),
				new Card(Value.ACE, Suit.CLUB));
		final RunMove move1 = new RunMove(2, 5, 3, run1);
		final RunMove move2 = new RunMove(2, 5, 3, run2);
		
		assertFalse(move1.equals(move2));
	}

}
