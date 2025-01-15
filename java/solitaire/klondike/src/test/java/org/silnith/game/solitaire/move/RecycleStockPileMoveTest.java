package org.silnith.game.solitaire.move;

import static org.junit.jupiter.api.Assertions.*;

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

public class RecycleStockPileMoveTest {

	@Test
	public void testGetSourceIndex() {
		final RecycleStockPileMove move = new RecycleStockPileMove(5);
		
		assertEquals(5, move.getSourceIndex());
	}

	@Test
	public void testHasCards() {
		final RecycleStockPileMove move = new RecycleStockPileMove(5);
		
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
		
		final RecycleStockPileMove move = new RecycleStockPileMove(stockPileIndex);
		
		final Board actual = move.apply(board);
		
		final Board expected = new Board(columns, stockPile, 0, foundation);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testEquals() {
		final RecycleStockPileMove move1 = new RecycleStockPileMove(5);
		final RecycleStockPileMove move2 = new RecycleStockPileMove(5);
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final RecycleStockPileMove move1 = new RecycleStockPileMove(5);
		final RecycleStockPileMove move2 = new RecycleStockPileMove(5);
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentIndex() {
		final RecycleStockPileMove move1 = new RecycleStockPileMove(5);
		final RecycleStockPileMove move2 = new RecycleStockPileMove(4);
		
		assertFalse(move1.equals(move2));
	}

}
