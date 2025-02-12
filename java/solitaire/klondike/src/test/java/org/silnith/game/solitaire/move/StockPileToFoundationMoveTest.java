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
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;

public class StockPileToFoundationMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();

    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);

    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public StockPileToFoundationMoveTest() {
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
    	
    	final Collection<StockPileToFoundationMove> actual = StockPileToFoundationMove.findMoves(board);
    	
    	assertTrue(actual.isEmpty());
    }
    
    @Test
    public void testFindMovesEmptyFoundation() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(ACE, CLUB));
    	final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
    	
    	final Collection<StockPileToFoundationMove> actual = StockPileToFoundationMove.findMoves(board);
    	
    	final Collection<StockPileToFoundationMove> expected = Collections.singleton(
    			new StockPileToFoundationMove(1, new Card(ACE, CLUB)));
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testFindMoves() {
    	final List<Card> stockPile = Arrays.asList(
    			new Card(FOUR, CLUB),
    			new Card(FOUR, DIAMOND),
    			new Card(FOUR, HEART),
    			new Card(FOUR, SPADE));
    	final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
    	foundation.put(CLUB, Arrays.asList(
    			new Card(ACE, CLUB),
    			new Card(TWO, CLUB),
    			new Card(THREE, CLUB)));
    	final Board board = new Board(emptyColumns, stockPile, 1, foundation);
    	
    	final Collection<StockPileToFoundationMove> actual = StockPileToFoundationMove.findMoves(board);
    	
    	final Collection<StockPileToFoundationMove> expected = Collections.singleton(
    			new StockPileToFoundationMove(1, new Card(FOUR, CLUB)));
    	assertEquals(expected, actual);
    }

	@Test
	public void testGetSourceIndex() {
		final StockPileToFoundationMove move = new StockPileToFoundationMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(5, move.getSourceIndex());
	}

	@Test
	public void testGetSourceIndexFromBoard() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(ACE, SPADE));
        final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
        
		final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
		
		assertEquals(1, move.getSourceIndex());
	}

	@Test
	public void testGetCard() {
		final StockPileToFoundationMove move = new StockPileToFoundationMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(new Card(Value.ACE, Suit.CLUB), move.getCard());
	}

	@Test
	public void testGetCardFromBoard() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(ACE, SPADE));
        final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
        
		final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
		
		assertEquals(new Card(ACE, SPADE), move.getCard());
	}

	@Test
	public void testHasCards() {
		final StockPileToFoundationMove move = new StockPileToFoundationMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertTrue(move.hasCards());
	}

	@Test
	public void testGetCards() {
		final StockPileToFoundationMove move = new StockPileToFoundationMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(Collections.singletonList(new Card(Value.ACE, Suit.CLUB)), move.getCards());
	}

    @Test
    public void testApply() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(ACE, SPADE));
        final Board board = new Board(emptyColumns, stockPile, 1, emptyFoundation);
        
        final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE)));
        final Board expected = new Board(emptyColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyEmpty() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        
        final StockPileToFoundationMove move = new StockPileToFoundationMove(0, new Card(ACE, CLUB));
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyUnderflow() {
        final List<Card> stockPile = Arrays.asList(
        		new Card(ACE, SPADE));
        final Board board = new Board(emptyColumns, stockPile, 0, emptyFoundation);

        // This is not actually the correct card.
        final StockPileToFoundationMove move = new StockPileToFoundationMove(0, new Card(ACE, SPADE));
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyNonEmpty() {
        final List<Card> stockPile = Arrays.asList(new Card(FIVE, SPADE));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        foundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE)));
        final Board board = new Board(emptyColumns, stockPile, 1, foundation);

        final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        expectedFoundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE),
                new Card(FIVE, SPADE)));
        final Board expected = new Board(emptyColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyFromBeginningNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
                new Card(FIVE, SPADE),
                new Card(SIX, SPADE),
                new Card(JACK, CLUB),
                new Card(QUEEN, CLUB),
                new Card(TWO, HEART),
                new Card(TEN, DIAMOND));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        foundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE)));
        final Board board = new Board(emptyColumns, stockPile, 1, foundation);

        final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
        
        final Board actual = move.apply(board);

        final List<Card> expectedStockPile = Arrays.asList(
                new Card(SIX, SPADE),
                new Card(JACK, CLUB),
                new Card(QUEEN, CLUB),
                new Card(TWO, HEART),
                new Card(TEN, DIAMOND));
        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        expectedFoundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE),
                new Card(FIVE, SPADE)));
        final Board expected = new Board(emptyColumns, expectedStockPile, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyFromMiddleNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
                new Card(SIX, SPADE),
                new Card(JACK, CLUB),
                new Card(FIVE, SPADE),
                new Card(QUEEN, CLUB),
                new Card(TWO, HEART),
                new Card(TEN, DIAMOND));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        foundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE)));
        final Board board = new Board(emptyColumns, stockPile, 3, foundation);

        final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
        
        final Board actual = move.apply(board);

        final List<Card> expectedStockPile = Arrays.asList(
                new Card(SIX, SPADE),
                new Card(JACK, CLUB),
                new Card(QUEEN, CLUB),
                new Card(TWO, HEART),
                new Card(TEN, DIAMOND));
        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        expectedFoundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE),
                new Card(FIVE, SPADE)));
        final Board expected = new Board(emptyColumns, expectedStockPile, 2, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyFromEndNonEmpty() {
        final List<Card> stockPile = Arrays.asList(
                new Card(SIX, SPADE),
                new Card(JACK, CLUB),
                new Card(QUEEN, CLUB),
                new Card(TWO, HEART),
                new Card(TEN, DIAMOND),
                new Card(FIVE, SPADE));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        foundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE)));
        final Board board = new Board(emptyColumns, stockPile, 6, foundation);

        final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
        
        final Board actual = move.apply(board);

        final List<Card> expectedStockPile = Arrays.asList(
                new Card(SIX, SPADE),
                new Card(JACK, CLUB),
                new Card(QUEEN, CLUB),
                new Card(TWO, HEART),
                new Card(TEN, DIAMOND));
        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(DIAMOND, Arrays.asList(
        		new Card(ACE, DIAMOND),
        		new Card(TWO, DIAMOND)));
        expectedFoundation.put(SPADE, Arrays.asList(
        		new Card(ACE, SPADE),
        		new Card(TWO, SPADE),
        		new Card(THREE, SPADE),
                new Card(FOUR, SPADE),
                new Card(FIVE, SPADE)));
        final Board expected = new Board(emptyColumns, expectedStockPile, 5, expectedFoundation);
        
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
        		new Card(ACE, SPADE));
        final Board board = new Board(columns, stockPile, 1, emptyFoundation);

        final StockPileToFoundationMove move = new StockPileToFoundationMove(board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(SPADE, Arrays.asList(
                new Card(ACE, SPADE)));
        final Board expected = new Board(columns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
    	final StockPileToFoundationMove move1 = new StockPileToFoundationMove(5, new Card(ACE, CLUB));
    	final StockPileToFoundationMove move2 = new StockPileToFoundationMove(5, new Card(ACE, CLUB));
    	
    	assertTrue(move1.equals(move2));
    }
    
    @Test
    public void testHashCode() {
    	final StockPileToFoundationMove move1 = new StockPileToFoundationMove(5, new Card(ACE, CLUB));
    	final StockPileToFoundationMove move2 = new StockPileToFoundationMove(5, new Card(ACE, CLUB));
    	
    	assertEquals(move1.hashCode(), move2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentIndex() {
    	final StockPileToFoundationMove move1 = new StockPileToFoundationMove(5, new Card(ACE, CLUB));
    	final StockPileToFoundationMove move2 = new StockPileToFoundationMove(4, new Card(ACE, CLUB));
    	
    	assertFalse(move1.equals(move2));
    }
    
    @Test
    public void testEqualsDifferentCard() {
    	final StockPileToFoundationMove move1 = new StockPileToFoundationMove(5, new Card(ACE, CLUB));
    	final StockPileToFoundationMove move2 = new StockPileToFoundationMove(5, new Card(ACE, HEART));
    	
    	assertFalse(move1.equals(move2));
    }
    
}
