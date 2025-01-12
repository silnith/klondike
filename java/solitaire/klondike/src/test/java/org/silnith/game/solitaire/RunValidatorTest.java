package org.silnith.game.solitaire;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;

public class RunValidatorTest {

	private RunValidator validator;

	@BeforeEach
	public void setUp() {
		validator = new RunValidator();
	}

	@AfterEach
	public void tearDown() {
		validator = null;
	}

	@Test
	public void testValidate() {
		final List<Card> run = Arrays.asList(
				new Card(Value.KING, Suit.SPADE),
				new Card(Value.QUEEN, Suit.HEART),
				new Card(Value.JACK, Suit.SPADE),
				new Card(Value.TEN, Suit.HEART),
				new Card(Value.NINE, Suit.SPADE),
				new Card(Value.EIGHT, Suit.HEART),
				new Card(Value.SEVEN, Suit.SPADE),
				new Card(Value.SIX, Suit.HEART),
				new Card(Value.FIVE, Suit.SPADE),
				new Card(Value.FOUR, Suit.HEART),
				new Card(Value.THREE, Suit.SPADE),
				new Card(Value.TWO, Suit.HEART),
				new Card(Value.ACE, Suit.SPADE));
		
		validator.validate(run);
	}

	@Test
	public void testRedOnRed() {
		final List<Card> run = Arrays.asList(
				new Card(Value.TWO, Suit.HEART),
				new Card(Value.ACE, Suit.DIAMOND));
		
		assertThrows(RuntimeException.class, () -> {
			validator.validate(run);
		});
	}

	@Test
	public void testBackwards() {
		final List<Card> run = Arrays.asList(
				new Card(Value.TWO, Suit.HEART),
				new Card(Value.THREE, Suit.SPADE));
		
		assertThrows(RuntimeException.class, () -> {
			validator.validate(run);
		});
	}

	@Test
	public void testSameValue() {
		final List<Card> run = Arrays.asList(
				new Card(Value.THREE, Suit.HEART),
				new Card(Value.THREE, Suit.SPADE));
		
		assertThrows(RuntimeException.class, () -> {
			validator.validate(run);
		});
	}

}
