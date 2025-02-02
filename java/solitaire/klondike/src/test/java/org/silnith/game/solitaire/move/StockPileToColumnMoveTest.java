package org.silnith.game.solitaire.move;

import static org.junit.jupiter.api.Assertions.*;
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

public class StockPileToColumnMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();

    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);

    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public StockPileToColumnMoveTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }
    
    @Test
    public void testFindMovesEmptyBoard() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	final Collection<StockPileToColumnMove> actual = StockPileToColumnMove.findMoves(board);
    	
    	final Collection<StockPileToColumnMove> expected = Collections.emptySet();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testFindMovesKingToEmptyBoard() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(KING, CLUB));
    	final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
    	
    	final Collection<StockPileToColumnMove> actual = StockPileToColumnMove.findMoves(board);
    	
    	final Collection<StockPileToColumnMove> expected = Arrays.asList(
    			new StockPileToColumnMove(1, 0, new Card(KING, CLUB)),
    			new StockPileToColumnMove(1, 1, new Card(KING, CLUB)),
    			new StockPileToColumnMove(1, 2, new Card(KING, CLUB)),
    			new StockPileToColumnMove(1, 3, new Card(KING, CLUB)),
    			new StockPileToColumnMove(1, 4, new Card(KING, CLUB)),
    			new StockPileToColumnMove(1, 5, new Card(KING, CLUB)),
    			new StockPileToColumnMove(1, 6, new Card(KING, CLUB)));
    	assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }
    
    @Test
    public void testFindMoves() {
    	final List<Column> columns = new ArrayList<>(emptyColumns);
    	columns.set(1, new Column(null, Arrays.asList(
    			new Card(EIGHT, CLUB))));
    	columns.set(3, new Column(null, Arrays.asList(
    			new Card(NINE, CLUB),
    			new Card(EIGHT, DIAMOND))));
    	columns.set(4, new Column(null, Arrays.asList(
    			new Card(TEN, HEART),
    			new Card(NINE, SPADE),
    			new Card(EIGHT, HEART))));
    	columns.set(6, new Column(null, Arrays.asList(
    			new Card(SIX, DIAMOND),
    			new Card(FIVE, CLUB))));
    	final List<Card> stockPile = Arrays.asList(
    			new Card(FOUR, DIAMOND),
    			new Card(SEVEN, CLUB),
    			new Card(THREE, DIAMOND));
    	final Board board = new Board(columns, stockPile, 2, emptyFoundation);
    	
    	final Collection<StockPileToColumnMove> actual = StockPileToColumnMove.findMoves(board);
    	
    	final Collection<StockPileToColumnMove> expected = Arrays.asList(
    			new StockPileToColumnMove(2, 3, new Card(SEVEN, CLUB)),
    			new StockPileToColumnMove(2, 4, new Card(SEVEN, CLUB)));
    	
    	assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

	@Test
	public void testGetSourceIndex() {
		final StockPileToColumnMove move = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(2, move.getSourceIndex());
	}

	@Test
	public void testConstructorGetSourceIndex() {
		final List<Card> stockPile = Arrays.asList(
				new Card(Value.ACE, Suit.CLUB),
				new Card(Value.TWO, Suit.HEART),
				new Card(Value.THREE, Suit.SPADE));
		final Board board = new Board(emptyColumns, stockPile, 2, emptyFoundation);
		
		final StockPileToColumnMove move = new StockPileToColumnMove(5, board);
		
		assertEquals(2, move.getSourceIndex());
	}

	@Test
	public void testGetDestinationColumnIndex() {
		final StockPileToColumnMove move = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(5, move.getDestinationColumnIndex());
	}

	@Test
	public void testGetCard() {
		final StockPileToColumnMove move = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(new Card(Value.ACE, Suit.CLUB), move.getCard());
	}

	@Test
	public void testConstructorGetCard() {
		final List<Card> stockPile = Arrays.asList(
				new Card(Value.ACE, Suit.CLUB),
				new Card(Value.TWO, Suit.HEART),
				new Card(Value.THREE, Suit.SPADE));
		final Board board = new Board(emptyColumns, stockPile, 2, emptyFoundation);
		
		final StockPileToColumnMove move = new StockPileToColumnMove(5, board);
		
		assertEquals(new Card(Value.TWO, Suit.HEART), move.getCard());
	}

	@Test
	public void testHasCards() {
		final StockPileToColumnMove move = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertTrue(move.hasCards());
	}

	@Test
	public void testGetCards() {
		final StockPileToColumnMove move = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(Collections.singletonList(new Card(Value.ACE, Suit.CLUB)), move.getCards());
	}

	@Test
	public void testConstructorGetCards() {
		final List<Card> stockPile = Arrays.asList(
				new Card(Value.ACE, Suit.CLUB),
				new Card(Value.TWO, Suit.HEART),
				new Card(Value.THREE, Suit.SPADE));
		final Board board = new Board(emptyColumns, stockPile, 2, emptyFoundation);
		
		final StockPileToColumnMove move = new StockPileToColumnMove(5, board);
		
		assertEquals(Collections.singletonList(new Card(Value.TWO, Suit.HEART)), move.getCards());
	}
    
    @Test
    public void testApply() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(KING, CLUB));
        final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
        
        final StockPileToColumnMove move = new StockPileToColumnMove(4, board);
        
        final Board actual = move.apply(board);

        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(4, new Column(null, Arrays.asList(
        		new Card(KING, CLUB))));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyEmpty() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);

        final StockPileToColumnMove move = new StockPileToColumnMove(0, 4, new Card(ACE, CLUB));
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyUnderflow() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(KING, CLUB));
        final Board board = new Board(emptyColumns, stockPile, 0, emptyFoundation);

        final StockPileToColumnMove move = new StockPileToColumnMove(0, 4, new Card(KING, CLUB));
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyNonEmpty() {
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

        final StockPileToColumnMove move = new StockPileToColumnMove(4, board);
        
        final Board actual = move.apply(board);

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
    public void testApplyFromBeginningNonEmpty() {
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

        final StockPileToColumnMove move = new StockPileToColumnMove(4, board);
        
        final Board actual = move.apply(board);

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
    public void testApplyFromMiddleNonEmpty() {
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

        final StockPileToColumnMove move = new StockPileToColumnMove(4, board);
        
        final Board actual = move.apply(board);

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
    public void testApplyFromEndNonEmpty() {
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

        final StockPileToColumnMove move = new StockPileToColumnMove(4, board);
        
        final Board actual = move.apply(board);

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
    public void testApplyKeepsFoundation() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND),
        		new Card(THREE, DIAMOND)));
        final List<Card> stockPile = Arrays.asList(
        		new Card(KING, CLUB));
        final Board board = new Board(emptyColumns, stockPile, 1, foundation);

        final StockPileToColumnMove move = new StockPileToColumnMove(4, board);
        
        final Board actual = move.apply(board);

        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(4, new Column(null, Arrays.asList(
        		new Card(KING, CLUB))));
        final Board expected = new Board(columns, emptyListOfCards, 0, foundation);
        
        assertEquals(expected, actual);
    }

	@Test
	public void testEquals() {
		final StockPileToColumnMove move1 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		final StockPileToColumnMove move2 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final StockPileToColumnMove move1 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		final StockPileToColumnMove move2 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentSourceIndex() {
		final StockPileToColumnMove move1 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		final StockPileToColumnMove move2 = new StockPileToColumnMove(1, 5, new Card(Value.ACE, Suit.CLUB));
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentDestinationColumn() {
		final StockPileToColumnMove move1 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		final StockPileToColumnMove move2 = new StockPileToColumnMove(2, 4, new Card(Value.ACE, Suit.CLUB));
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentCard() {
		final StockPileToColumnMove move1 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.CLUB));
		final StockPileToColumnMove move2 = new StockPileToColumnMove(2, 5, new Card(Value.ACE, Suit.HEART));
		
		assertFalse(move1.equals(move2));
	}

}
