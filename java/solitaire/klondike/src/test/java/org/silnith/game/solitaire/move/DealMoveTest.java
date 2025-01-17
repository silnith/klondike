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
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;

public class DealMoveTest {
	
	private final int numberOfColumns = 7;

	private final List<Card> deck = Arrays.asList(
			new Card(SEVEN, HEART),
			new Card(EIGHT, DIAMOND),
			new Card(NINE, CLUB),
			new Card(KING, CLUB),
			new Card(KING, DIAMOND),
			new Card(KING, HEART),
			new Card(KING, SPADE),
			new Card(SEVEN, DIAMOND),
			new Card(EIGHT, CLUB),
			new Card(QUEEN, CLUB),
			new Card(QUEEN, DIAMOND),
			new Card(QUEEN, HEART),
			new Card(QUEEN, SPADE),
			new Card(SEVEN, CLUB),
			new Card(JACK, CLUB),
			new Card(JACK, DIAMOND),
			new Card(JACK, HEART),
			new Card(JACK, SPADE),
			new Card(TEN, CLUB),
			new Card(TEN, DIAMOND),
			new Card(TEN, HEART),
			new Card(TEN, SPADE),
			new Card(NINE, DIAMOND),
			new Card(NINE, HEART),
			new Card(NINE, SPADE),
			new Card(EIGHT, HEART),
			new Card(EIGHT, SPADE),
			new Card(SEVEN, SPADE),
			new Card(SIX, CLUB),
			new Card(SIX, DIAMOND),
			new Card(SIX, HEART),
			new Card(SIX, SPADE),
			new Card(FIVE, CLUB),
			new Card(FIVE, DIAMOND),
			new Card(FIVE, HEART),
			new Card(FIVE, SPADE),
			new Card(FOUR, CLUB),
			new Card(FOUR, DIAMOND),
			new Card(FOUR, HEART),
			new Card(FOUR, SPADE),
			new Card(THREE, CLUB),
			new Card(THREE, DIAMOND),
			new Card(THREE, HEART),
			new Card(THREE, SPADE),
			new Card(TWO, CLUB),
			new Card(TWO, DIAMOND),
			new Card(TWO, HEART),
			new Card(TWO, SPADE),
			new Card(ACE, CLUB),
			new Card(ACE, DIAMOND),
			new Card(ACE, HEART),
			new Card(ACE, SPADE));
	
	@Test
	public void testConstructorDeckTooSmall() {
		assertThrows(IllegalArgumentException.class, () -> {
			new DealMove(Collections.emptyList(), numberOfColumns);
		});
	}

	@Test
	public void testGetNumberOfColumns() {
		final DealMove move = new DealMove(deck, numberOfColumns);
		
		assertEquals(numberOfColumns, move.getNumberOfColumns());
	}

	@Test
	public void testGetDeck() {
		final DealMove move = new DealMove(deck, numberOfColumns);
		
		assertEquals(deck, move.getDeck());
	}

	@Test
	public void testHasCards() {
		final DealMove move = new DealMove(deck, numberOfColumns);
		
		assertTrue(move.hasCards());
	}

	@Test
	public void testGetCards() {
		final DealMove move = new DealMove(deck, numberOfColumns);
		
		assertEquals(deck, move.getCards());
	}

	@Test
	public void testApply() {
		final Board board = new Board(Collections.emptyList(), Collections.emptyList(), 0, Collections.emptyMap());
		
		final DealMove move = new DealMove(deck, numberOfColumns);
		
		final Board actual = move.apply(board);
		
		final List<Column> expectedColumns = new ArrayList<Column>(numberOfColumns);
		expectedColumns.add(new Column(
				Collections.emptyList(),
				Collections.singletonList(
						new Card(SEVEN, HEART))));
		expectedColumns.add(new Column(
				Arrays.asList(
						new Card(EIGHT, DIAMOND)),
				Collections.singletonList(
						new Card(SEVEN, DIAMOND))));
		expectedColumns.add(new Column(
				Arrays.asList(
						new Card(NINE, CLUB),
						new Card(EIGHT, CLUB)),
				Collections.singletonList(
						new Card(SEVEN, CLUB))));
		expectedColumns.add(new Column(
				Arrays.asList(
						new Card(KING, CLUB),
						new Card(QUEEN, CLUB),
						new Card(JACK, CLUB)),
				Collections.singletonList(
						new Card(TEN, CLUB))));
		expectedColumns.add(new Column(
				Arrays.asList(
						new Card(KING, DIAMOND),
						new Card(QUEEN, DIAMOND),
						new Card(JACK, DIAMOND),
						new Card(TEN, DIAMOND)),
				Collections.singletonList(
						new Card(NINE, DIAMOND))));
		expectedColumns.add(new Column(
				Arrays.asList(
						new Card(KING, HEART),
						new Card(QUEEN, HEART),
						new Card(JACK, HEART),
						new Card(TEN, HEART),
						new Card(NINE, HEART)),
				Collections.singletonList(
						new Card(EIGHT, HEART))));
		expectedColumns.add(new Column(
				Arrays.asList(
						new Card(KING, SPADE),
						new Card(QUEEN, SPADE),
						new Card(JACK, SPADE),
						new Card(TEN, SPADE),
						new Card(NINE, SPADE),
						new Card(EIGHT, SPADE)),
				Collections.singletonList(
						new Card(SEVEN, SPADE))));
		final List<Card> expectedStockPile = Arrays.asList(
				new Card(SIX, CLUB),
				new Card(SIX, DIAMOND),
				new Card(SIX, HEART),
				new Card(SIX, SPADE),
				new Card(FIVE, CLUB),
				new Card(FIVE, DIAMOND),
				new Card(FIVE, HEART),
				new Card(FIVE, SPADE),
				new Card(FOUR, CLUB),
				new Card(FOUR, DIAMOND),
				new Card(FOUR, HEART),
				new Card(FOUR, SPADE),
				new Card(THREE, CLUB),
				new Card(THREE, DIAMOND),
				new Card(THREE, HEART),
				new Card(THREE, SPADE),
				new Card(TWO, CLUB),
				new Card(TWO, DIAMOND),
				new Card(TWO, HEART),
				new Card(TWO, SPADE),
				new Card(ACE, CLUB),
				new Card(ACE, DIAMOND),
				new Card(ACE, HEART),
				new Card(ACE, SPADE));
		final Map<Suit, List<Card>> expectedFoundation = new EnumMap<>(Suit.class);
		for (final Suit suit : Suit.values()) {
			expectedFoundation.put(suit, Collections.emptyList());
		}
		final Board expected = new Board(expectedColumns, expectedStockPile, 0, expectedFoundation);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testEquals() {
		final DealMove move1 = new DealMove(deck, numberOfColumns);
		final DealMove move2 = new DealMove(deck, numberOfColumns);
		
		assertTrue(move1.equals(move2));
	}

	@Test
	public void testHashCode() {
		final DealMove move1 = new DealMove(deck, numberOfColumns);
		final DealMove move2 = new DealMove(deck, numberOfColumns);
		
		assertEquals(move1.hashCode(), move2.hashCode());
	}

	@Test
	public void testEqualsDifferentDeck() {
		final int size = deck.size();
		final int halfSize = size / 2;
		final List<Card> otherDeck = new ArrayList<Card>(size);
		otherDeck.addAll(deck.subList(halfSize, size));
		otherDeck.addAll(deck.subList(0, halfSize));
		final DealMove move1 = new DealMove(deck, numberOfColumns);
		final DealMove move2 = new DealMove(otherDeck, numberOfColumns);
		
		assertFalse(move1.equals(move2));
	}

	@Test
	public void testEqualsDifferentNumberOfColumns() {
		final DealMove move1 = new DealMove(deck, numberOfColumns);
		final DealMove move2 = new DealMove(deck, numberOfColumns - 1);
		
		assertFalse(move1.equals(move2));
	}

}
