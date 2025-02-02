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

public class FoundationToColumnMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();
    
    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<Suit, List<Card>>(Suit.class);
    
    private final List<Column> emptyColumns = new ArrayList<Column>(7);
    
    public FoundationToColumnMoveTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }
    
    @Test
    public void testFindMoves() {
    	final List<Column> columns = new ArrayList<>(emptyColumns);
    	columns.set(3, new Column(null, Arrays.asList(
    			new Card(SIX, CLUB),
    			new Card(FIVE, DIAMOND),
    			new Card(FOUR, CLUB))));
    	columns.set(4, new Column(null, Arrays.asList(
    			// Please ignore that this card is duplicated.
    			new Card(FOUR, SPADE))));
    	final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
    	foundation.put(DIAMOND, Arrays.asList(
    			new Card(ACE, DIAMOND),
    			new Card(TWO, DIAMOND),
    			new Card(THREE, DIAMOND)));
    	foundation.put(HEART, Arrays.asList(
    			new Card(ACE, HEART),
    			new Card(TWO, HEART),
    			new Card(THREE, HEART)));
    	foundation.put(SPADE, Arrays.asList(
    			new Card(ACE, SPADE),
    			new Card(TWO, SPADE),
    			new Card(THREE, SPADE),
    			new Card(FOUR, SPADE),
    			new Card(FIVE, SPADE),
    			new Card(SIX, SPADE),
    			new Card(SEVEN, SPADE),
    			new Card(EIGHT, SPADE),
    			new Card(NINE, SPADE),
    			new Card(TEN, SPADE),
    			new Card(JACK, SPADE),
    			new Card(QUEEN, SPADE),
    			new Card(KING, SPADE)));
    	final Board board = new Board(columns, emptyListOfCards, 0, foundation);
    	
    	final Collection<FoundationToColumnMove> actual = FoundationToColumnMove.findMoves(board);
    	
    	final Collection<FoundationToColumnMove> expected = Arrays.asList(
    			new FoundationToColumnMove(0, new Card(KING, SPADE)),
    			new FoundationToColumnMove(1, new Card(KING, SPADE)),
    			new FoundationToColumnMove(2, new Card(KING, SPADE)),
    			new FoundationToColumnMove(3, new Card(THREE, DIAMOND)),
    			new FoundationToColumnMove(3, new Card(THREE, HEART)),
    			new FoundationToColumnMove(4, new Card(THREE, DIAMOND)),
    			new FoundationToColumnMove(4, new Card(THREE, HEART)),
    			new FoundationToColumnMove(5, new Card(KING, SPADE)),
    			new FoundationToColumnMove(6, new Card(KING, SPADE)));
    	assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void testFoundationToColumnMoveEmptyFoundation() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
        
        assertThrows(RuntimeException.class, () -> {
        	new FoundationToColumnMove(3, CLUB, board);
        });
    }

	@Test
	public void testGetDestinationColumnIndex() {
		final FoundationToColumnMove move = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(5, move.getDestinationColumnIndex());
	}

	@Test
	public void testGetCard() {
		final FoundationToColumnMove move = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(new Card(Value.ACE, Suit.CLUB), move.getCard());
	}

	@Test
	public void testHasCards() {
		final FoundationToColumnMove move = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertTrue(move.hasCards());
	}

	@Test
	public void testGetCards() {
		final FoundationToColumnMove move = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(Collections.singletonList(new Card(Value.ACE, Suit.CLUB)), move.getCards());
	}

    @Test
    public void testApply() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
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
        		new Card(QUEEN, CLUB),
        		new Card(KING, CLUB)));
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, foundation);
        
        final FoundationToColumnMove move = new FoundationToColumnMove(3, CLUB, board);
        
        final Board actualBoard = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
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
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(3, new Column(null, Arrays.asList(
        		new Card(KING, CLUB))));
        final Board expectedBoard = new Board(expectedColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expectedBoard, actualBoard);
    }
    
    @Test
    public void testApplyEmpty() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);

        final FoundationToColumnMove move = new FoundationToColumnMove(3, new Card(Value.ACE, Suit.CLUB));
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyNonEmptyColumn() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB)));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(3, new Column(
        		Arrays.asList(
        				new Card(FOUR, SPADE)),
                Arrays.asList(
                		new Card(SIX, HEART),
                		new Card(FIVE, SPADE),
                		new Card(FOUR, HEART))));
        final Board board = new Board(columns, emptyListOfCards, 0, foundation);

        final FoundationToColumnMove move = new FoundationToColumnMove(3, CLUB, board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB)));
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(3, new Column(
        		Arrays.asList(
        				new Card(FOUR, SPADE)),
        		Arrays.asList(
        				new Card(SIX, HEART),
        				new Card(FIVE, SPADE),
        				new Card(FOUR, HEART),
        				new Card(THREE, CLUB))));
        final Board expected = new Board(expectedColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsStockPile() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB)));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(3, new Column(
        		Arrays.asList(
        				new Card(FOUR, SPADE)),
                Arrays.asList(
                		new Card(SIX, HEART),
                		new Card(FIVE, SPADE),
                		new Card(FOUR, HEART))));
        final List<Card> stockPile = Arrays.asList(
        		new Card(JACK, CLUB),
        		new Card(TWO, DIAMOND),
        		new Card(SIX, HEART));
        final Board board = new Board(columns, stockPile, 0, foundation);

        final FoundationToColumnMove move = new FoundationToColumnMove(3, CLUB, board);
        
        final Board actual = move.apply(board);
        
        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB)));
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(3, new Column(
        		Arrays.asList(
        				new Card(FOUR, SPADE)),
        		Arrays.asList(
        				new Card(SIX, HEART),
        				new Card(FIVE, SPADE),
        				new Card(FOUR, HEART),
        				new Card(THREE, CLUB))));
        final Board expected = new Board(expectedColumns, stockPile, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsStockPileIndex() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB)));
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(3, new Column(
        		Arrays.asList(
        				new Card(FOUR, SPADE)),
                Arrays.asList(
                		new Card(SIX, HEART),
                		new Card(FIVE, SPADE),
                		new Card(FOUR, HEART))));
        final List<Card> stockPile = Arrays.asList(
        		new Card(JACK, CLUB),
        		new Card(TWO, DIAMOND),
        		new Card(SIX, HEART));
        final Board board = new Board(columns, stockPile, 2, foundation);

        final FoundationToColumnMove move = new FoundationToColumnMove(3, CLUB, board);
        
        final Board actual = move.apply(board);
        
        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB)));
        final List<Column> expectedColumns = new ArrayList<>(emptyColumns);
        expectedColumns.set(3, new Column(
        		Arrays.asList(
        				new Card(FOUR, SPADE)),
        		Arrays.asList(
        				new Card(SIX, HEART),
        				new Card(FIVE, SPADE),
        				new Card(FOUR, HEART),
        				new Card(THREE, CLUB))));
        final Board expected = new Board(expectedColumns, stockPile, 2, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
	@Test
	public void testEquals() {
		final FoundationToColumnMove move1 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		final FoundationToColumnMove move2 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final FoundationToColumnMove move1 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		final FoundationToColumnMove move2 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentColumn() {
		final FoundationToColumnMove move1 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		final FoundationToColumnMove move2 = new FoundationToColumnMove(4, new Card(Value.ACE, Suit.CLUB));
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentCard() {
		final FoundationToColumnMove move1 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.CLUB));
		final FoundationToColumnMove move2 = new FoundationToColumnMove(5, new Card(Value.ACE, Suit.HEART));
		
		assertFalse(move1.equals(move2));
	}

}
