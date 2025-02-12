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
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.QUEEN;
import static org.silnith.deck.Value.SIX;
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
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;

public class ColumnToFoundationMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();
    
    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<>(Suit.class);
    
    private final List<Column> emptyColumns = new ArrayList<>(7);
    
    public ColumnToFoundationMoveTest() {
        for (int i = 0; i < 7; i++ ) {
            this.emptyColumns.add(new Column(emptyListOfCards, emptyListOfCards));
        }
        for (final Suit suit : Suit.values()) {
            this.emptyFoundation.put(suit, emptyListOfCards);
        }
    }
    
    @Test
    public void testFindMovesEmptyColumns() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	final Collection<ColumnToFoundationMove> actual = ColumnToFoundationMove.findMoves(board);
    	
    	final Collection<ColumnToFoundationMove> expected = Collections.emptyList();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testFindMoves() {
    	final List<Column> columns = new ArrayList<Column>(emptyColumns);
    	columns.set(0, new Column(
    			null,
    			Arrays.asList(
    					new Card(ACE, CLUB))));
    	columns.set(1, new Column(
    			null,
    			Arrays.asList(
    					new Card(ACE, DIAMOND))));
    	columns.set(2, new Column(
    			null,
    			Arrays.asList(
    					new Card(ACE, HEART))));
    	columns.set(3, new Column(
    			null,
    			Arrays.asList(
    					new Card(ACE, SPADE))));
    	final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
    	
    	final Collection<ColumnToFoundationMove> actual = ColumnToFoundationMove.findMoves(board);
    	
    	final Collection<ColumnToFoundationMove> expected = new HashSet<>(Arrays.asList(
    			new ColumnToFoundationMove(0, new Card(ACE, CLUB)),
    			new ColumnToFoundationMove(1, new Card(ACE, DIAMOND)),
    			new ColumnToFoundationMove(2, new Card(ACE, HEART)),
    			new ColumnToFoundationMove(3, new Card(ACE, SPADE))));
    	assertEquals(expected, new HashSet<>(actual));
    }
    
    @Test
    public void testConstructorSourceColumnOutOfRange() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	assertThrows(IndexOutOfBoundsException.class, () -> {
    		new ColumnToFoundationMove(8, board);
    	});
    }
    
    @Test
    public void testConstructorEmptySourceColumn() {
    	final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);
    	
    	assertThrows(IllegalArgumentException.class, () -> {
    		new ColumnToFoundationMove(4, board);
    	});
    }
    
	@Test
	public void testGetSourceColumnIndex() {
		final ColumnToFoundationMove move = new ColumnToFoundationMove(3, new Card(ACE, CLUB));
		
		assertEquals(3, move.getSourceColumnIndex());
	}

	@Test
	public void testGetCard() {
		final ColumnToFoundationMove move = new ColumnToFoundationMove(3, new Card(ACE, CLUB));
		
		assertEquals(new Card(ACE, CLUB), move.getCard());
	}

	@Test
	public void testHasCards() {
		final ColumnToFoundationMove move = new ColumnToFoundationMove(3, new Card(ACE, CLUB));
		
		assertTrue(move.hasCards());
	}

	@Test
	public void testGetCards() {
		final ColumnToFoundationMove move = new ColumnToFoundationMove(3, new Card(ACE, CLUB));
		
		assertEquals(Collections.singletonList(new Card(ACE, CLUB)), move.getCards());
	}
    
    @Test
    public void testApply() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, Arrays.asList(
        		new Card(ACE, CLUB))));
        final Board board = new Board(columns, emptyListOfCards, 0, emptyFoundation);
        
        final ColumnToFoundationMove move = new ColumnToFoundationMove(2, board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB)));
        final Board expected = new Board(emptyColumns, emptyListOfCards, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyFromEmptyColumn() {
        final Board board = new Board(emptyColumns, emptyListOfCards, 0, emptyFoundation);

        final ColumnToFoundationMove move = new ColumnToFoundationMove(2, new Card(ACE, CLUB));
        
        assertThrows(RuntimeException.class, () -> {
        	move.apply(board);
        });
    }
    
    @Test
    public void testApplyNonEmptyFoundation() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, Arrays.asList(
        		new Card(FOUR, CLUB))));
        final Map<Suit, List<Card>> foundation = new EnumMap<>(emptyFoundation);
        foundation.put(CLUB, Arrays.asList(
        		new Card(ACE, CLUB),
        		new Card(TWO, CLUB),
        		new Card(THREE, CLUB)));
        final Board board = new Board(columns, emptyListOfCards, 0, foundation);

        final ColumnToFoundationMove move = new ColumnToFoundationMove(2, board);
        
        final Board actual = move.apply(board);

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
    public void testApplyKeepsStockPile() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, Arrays.asList(
                new Card(ACE, CLUB))));
        final List<Card> stockPile = Arrays.asList(
                new Card(FOUR, SPADE),
                new Card(SIX, HEART),
                new Card(QUEEN, HEART));
        final Board board = new Board(columns, stockPile, 0, emptyFoundation);
        
        final ColumnToFoundationMove move = new ColumnToFoundationMove(2, board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
                new Card(ACE, CLUB)));
        final Board expected = new Board(emptyColumns, stockPile, 0, expectedFoundation);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testApplyKeepsStockPileIndex() {
        final List<Column> columns = new ArrayList<>(emptyColumns);
        columns.set(2, new Column(null, Arrays.asList(
                new Card(ACE, CLUB))));
        final List<Card> stockPile = Arrays.asList(
                new Card(FOUR, SPADE),
                new Card(SIX, HEART),
                new Card(QUEEN, HEART));
        final Board board = new Board(columns, stockPile, 2, emptyFoundation);
        
        final ColumnToFoundationMove move = new ColumnToFoundationMove(2, board);
        
        final Board actual = move.apply(board);

        final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(emptyFoundation);
        expectedFoundation.put(CLUB, Arrays.asList(
                new Card(ACE, CLUB)));
        final Board expected = new Board(emptyColumns, stockPile, 2, expectedFoundation);
        
        assertEquals(expected, actual);
    }

	@Test
	public void testEquals() {
		final ColumnToFoundationMove move1 = new ColumnToFoundationMove(4, new Card(ACE, CLUB));
		final ColumnToFoundationMove move2 = new ColumnToFoundationMove(4, new Card(ACE, CLUB));
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final ColumnToFoundationMove move1 = new ColumnToFoundationMove(4, new Card(ACE, CLUB));
		final ColumnToFoundationMove move2 = new ColumnToFoundationMove(4, new Card(ACE, CLUB));
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentColumn() {
		final ColumnToFoundationMove move1 = new ColumnToFoundationMove(5, new Card(ACE, CLUB));
		final ColumnToFoundationMove move2 = new ColumnToFoundationMove(4, new Card(ACE, CLUB));
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentCard() {
		final ColumnToFoundationMove move1 = new ColumnToFoundationMove(4, new Card(ACE, CLUB));
		final ColumnToFoundationMove move2 = new ColumnToFoundationMove(4, new Card(ACE, HEART));
		
		assertFalse(move1.equals(move2));
	}

}
