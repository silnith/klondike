package org.silnith.game.solitaire;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;

public class FoundationValidatorTest {

    private FoundationValidator validator;
    
    @BeforeEach
    public void setUp() {
        validator = new FoundationValidator();
    }

    @AfterEach
    public void tearDown() {
        validator = null;
    }

    @Test
    public void testValidateEmpty() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(foundation);
        });
    }

    @Test
    public void testValidateNoCards() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        for (final Suit suit : Suit.values()) {
        	foundation.put(suit, Collections.emptyList());
        }

        validator.validate(foundation);
    }

    @Test
    public void testValidateAllCards() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        for (final Suit suit : Suit.values()) {
        	final List<Card> allCards = Arrays.asList(
        			new Card(Value.ACE, suit),
        			new Card(Value.TWO, suit),
        			new Card(Value.THREE, suit),
        			new Card(Value.FOUR, suit),
        			new Card(Value.FIVE, suit),
        			new Card(Value.SIX, suit),
        			new Card(Value.SEVEN, suit),
        			new Card(Value.EIGHT, suit),
        			new Card(Value.NINE, suit),
        			new Card(Value.TEN, suit),
        			new Card(Value.JACK, suit),
        			new Card(Value.QUEEN, suit),
        			new Card(Value.KING, suit));
        	foundation.put(suit, allCards);
        }

        validator.validate(foundation);
    }
    
    @Test
    public void testValidateWrongSuitAce() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        foundation.put(Suit.CLUB, Arrays.asList(
        		new Card(Value.ACE, Suit.HEART)));
        foundation.put(Suit.DIAMOND, Collections.emptyList());
        foundation.put(Suit.HEART, Collections.emptyList());
        foundation.put(Suit.SPADE, Collections.emptyList());
        
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(foundation);
        });
    }
    
    @Test
    public void testValidateWrongSuitNotAce() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        foundation.put(Suit.CLUB, Arrays.asList(
        		new Card(Value.ACE, Suit.CLUB),
        		new Card(Value.TWO, Suit.HEART)));
        foundation.put(Suit.DIAMOND, Collections.emptyList());
        foundation.put(Suit.HEART, Collections.emptyList());
        foundation.put(Suit.SPADE, Collections.emptyList());
        
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(foundation);
        });
    }
    
    @Test
    public void testValidateNotStartWithAce() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        foundation.put(Suit.CLUB, Arrays.asList(
        		new Card(Value.TWO, Suit.CLUB)));
        foundation.put(Suit.DIAMOND, Collections.emptyList());
        foundation.put(Suit.HEART, Collections.emptyList());
        foundation.put(Suit.SPADE, Collections.emptyList());
        
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(foundation);
        });
    }
    
    @Test
    public void testValidateOutOfOrder() {
        final Map<Suit, List<Card>> foundation = new EnumMap<>(Suit.class);
        foundation.put(Suit.CLUB, Arrays.asList(
        		new Card(Value.ACE, Suit.CLUB),
        		new Card(Value.THREE, Suit.CLUB)));
        foundation.put(Suit.DIAMOND, Collections.emptyList());
        foundation.put(Suit.HEART, Collections.emptyList());
        foundation.put(Suit.SPADE, Collections.emptyList());
        
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(foundation);
        });
    }

}
