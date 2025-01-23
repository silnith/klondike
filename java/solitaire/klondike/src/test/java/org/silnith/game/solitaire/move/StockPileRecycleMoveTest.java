package org.silnith.game.solitaire.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.silnith.deck.Suit.CLUB;
import static org.silnith.deck.Suit.DIAMOND;
import static org.silnith.deck.Suit.HEART;
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.SEVEN;
import static org.silnith.deck.Value.SIX;
import static org.silnith.deck.Value.THREE;

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

public class StockPileRecycleMoveTest {

    private final List<Card> emptyListOfCards = Collections.emptyList();
    
    private final EnumMap<Suit, List<Card>> emptyFoundation = new EnumMap<Suit, List<Card>>(Suit.class);
    
    private final List<Column> emptyColumns = new ArrayList<Column>(7);
    
    public StockPileRecycleMoveTest() {
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
		
		final Collection<StockPileRecycleMove> actual = StockPileRecycleMove.findMoves(board);
		
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testFindMovesStockPileAtBeginning() {
		final List<Card> stockPile = Arrays.asList(
				new Card(FOUR, CLUB),
				new Card(SEVEN, HEART),
				new Card(SIX, HEART),
				new Card(THREE, DIAMOND));
		final Board board = new Board(emptyColumns, stockPile, 0, emptyFoundation);
		
		final Collection<StockPileRecycleMove> actual = StockPileRecycleMove.findMoves(board);
		
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testFindMovesStockPileInMiddle() {
		final List<Card> stockPile = Arrays.asList(
				new Card(FOUR, CLUB),
				new Card(SEVEN, HEART),
				new Card(SIX, HEART),
				new Card(THREE, DIAMOND));
		final Board board = new Board(emptyColumns, stockPile, 2, emptyFoundation);
		
		final Collection<StockPileRecycleMove> actual = StockPileRecycleMove.findMoves(board);
		
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testFindMovesStockPileAtEnd() {
		final List<Card> stockPile = Arrays.asList(
				new Card(FOUR, CLUB),
				new Card(SEVEN, HEART),
				new Card(SIX, HEART),
				new Card(THREE, DIAMOND));
		final Board board = new Board(emptyColumns, stockPile, 4, emptyFoundation);
		
		final Collection<StockPileRecycleMove> actual = StockPileRecycleMove.findMoves(board);
		
		final Collection<StockPileRecycleMove> expected = Collections.singleton(
				new StockPileRecycleMove(4));
		assertEquals(new HashSet<>(expected), new HashSet<>(actual));
	}

	@Test
	public void testGetSourceIndex() {
		final StockPileRecycleMove move = new StockPileRecycleMove(5);
		
		assertEquals(5, move.getSourceIndex());
	}

	@Test
	public void testHasCards() {
		final StockPileRecycleMove move = new StockPileRecycleMove(5);
		
		assertFalse(move.hasCards());
	}

	@Test
	public void testApply() {
		final List<Column> columns = Arrays.asList(
				new Column(null, null),
				new Column(null, null),
				new Column(null, null),
				new Column(null, null),
				new Column(null, Arrays.asList(
						new Card(Value.KING, Suit.SPADE),
						new Card(Value.QUEEN, Suit.HEART))),
				new Column(null, null),
				new Column(null, null));
		final List<Card> stockPile = Arrays.asList(
				new Card(Value.THREE, Suit.CLUB),
				new Card(Value.JACK, Suit.SPADE));
		final int stockPileIndex = stockPile.size();
		final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
		foundation.put(Suit.CLUB, Arrays.asList(
				new Card(Value.ACE, Suit.CLUB),
				new Card(Value.TWO, Suit.CLUB)));
		foundation.put(Suit.DIAMOND, Collections.emptyList());
		foundation.put(Suit.HEART, Collections.emptyList());
		foundation.put(Suit.SPADE, Collections.emptyList());
		
		final Board board = new Board(columns, stockPile, stockPileIndex, foundation);
		
		final StockPileRecycleMove move = new StockPileRecycleMove(stockPileIndex);
		
		final Board actual = move.apply(board);
		
		final Board expected = new Board(columns, stockPile, 0, foundation);
		
		assertEquals(expected, actual);
	}
	
	// TODO: Add tests that illegal recycles throw exceptions.

	@Test
	public void testEquals() {
		final StockPileRecycleMove move1 = new StockPileRecycleMove(5);
		final StockPileRecycleMove move2 = new StockPileRecycleMove(5);
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final StockPileRecycleMove move1 = new StockPileRecycleMove(5);
		final StockPileRecycleMove move2 = new StockPileRecycleMove(5);
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentIndex() {
		final StockPileRecycleMove move1 = new StockPileRecycleMove(5);
		final StockPileRecycleMove move2 = new StockPileRecycleMove(4);
		
		assertFalse(move1.equals(move2));
	}

}
