package org.silnith.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CardTest {
    
    @Test
    public void testCard() {
        new Card(Value.EIGHT, Suit.HEART);
    }
    
    @Test
    public void testCardNullValue() {
    	assertThrows(IllegalArgumentException.class, () -> new Card(null, Suit.HEART));
    }
    
    @Test
    public void testCardNullSuit() {
    	assertThrows(IllegalArgumentException.class, () -> new Card(Value.EIGHT, null));
    }
    
    @Test
    public void testCardNullValueAndSuit() {
    	assertThrows(IllegalArgumentException.class, () -> new Card(null, null));
    }
    
    @Test
    public void testGetValue() {
        final Card card = new Card(Value.EIGHT, Suit.HEART);
        
        assertEquals(Value.EIGHT, card.getValue());
    }
    
    @Test
    public void testGetSuit() {
        final Card card = new Card(Value.EIGHT, Suit.HEART);
        
        assertEquals(Suit.HEART, card.getSuit());
    }
    
    @Test
    public void testEquals() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.EIGHT, Suit.HEART);
        
        assertTrue(card1.equals(card2));
    }
    
    @Test
    public void testHashCode() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.EIGHT, Suit.HEART);
        
        assertEquals(card1.hashCode(), card2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentValue() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.SEVEN, Suit.HEART);
        
        assertFalse(card1.equals(card2));
    }
    
    @Test
    public void testHashCodeDifferentValue() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.SEVEN, Suit.HEART);
        
        assertFalse(card1.hashCode() == card2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentSuit() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.EIGHT, Suit.DIAMOND);
        
        assertFalse(card1.equals(card2));
    }
    
    @Test
    public void testHashCodeDifferentSuit() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.EIGHT, Suit.DIAMOND);
        
        assertFalse(card1.hashCode() == card2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentValueAndSuit() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.SEVEN, Suit.DIAMOND);
        
        assertFalse(card1.equals(card2));
    }
    
    @Test
    public void testHashCodeDifferentValueAndSuit() {
        final Card card1 = new Card(Value.EIGHT, Suit.HEART);
        final Card card2 = new Card(Value.SEVEN, Suit.DIAMOND);
        
        assertFalse(card1.hashCode() == card2.hashCode());
    }
    
    @SuppressWarnings("unlikely-arg-type")
	@Test
    public void testEqualsDifferentType() {
        final Card card = new Card(Value.EIGHT, Suit.HEART);
        
        assertFalse(card.equals("Eight of Hearts"));
    }
    
}
