package org.silnith.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class ValueTest {
    
    @Test
    public void testGetAceValue() {
        assertEquals(1, Value.ACE.getValue());
    }
    
    @Test
    public void testGetTwoValue() {
        assertEquals(2, Value.TWO.getValue());
    }
    
    @Test
    public void testGetThreeValue() {
        assertEquals(3, Value.THREE.getValue());
    }
    
    @Test
    public void testGetFourValue() {
        assertEquals(4, Value.FOUR.getValue());
    }
    
    @Test
    public void testGetFiveValue() {
        assertEquals(5, Value.FIVE.getValue());
    }
    
    @Test
    public void testGetSixValue() {
        assertEquals(6, Value.SIX.getValue());
    }
    
    @Test
    public void testGetSevenValue() {
        assertEquals(7, Value.SEVEN.getValue());
    }
    
    @Test
    public void testGetEightValue() {
        assertEquals(8, Value.EIGHT.getValue());
    }
    
    @Test
    public void testGetNineValue() {
        assertEquals(9, Value.NINE.getValue());
    }
    
    @Test
    public void testGetTenValue() {
        assertEquals(10, Value.TEN.getValue());
    }
    
    @Test
    public void testGetJackValue() {
        assertEquals(11, Value.JACK.getValue());
    }
    
    @Test
    public void testGetQueenValue() {
        assertEquals(12, Value.QUEEN.getValue());
    }
    
    @Test
    public void testGetKingValue() {
        assertEquals(13, Value.KING.getValue());
    }
    
    @Test
    public void testAceLessThanTwo() {
        assertEquals( -1, Integer.signum(Value.ACE.compareTo(Value.TWO)));
    }
    
    @Test
    public void testTwoGreaterThanAce() {
        assertEquals(1, Integer.signum(Value.TWO.compareTo(Value.ACE)));
    }
    
    @Test
    public void testTenLessThanJack() {
        assertEquals( -1, Integer.signum(Value.TEN.compareTo(Value.JACK)));
    }
    
    @Test
    public void testJackGreaterThanTen() {
        assertEquals(1, Integer.signum(Value.JACK.compareTo(Value.TEN)));
    }
    
    @Test
    public void testJackLessThanQueen() {
        assertEquals( -1, Integer.signum(Value.JACK.compareTo(Value.QUEEN)));
    }
    
    @Test
    public void testQueenGreaterThanJack() {
        assertEquals(1, Integer.signum(Value.QUEEN.compareTo(Value.JACK)));
    }
    
    @Test
    public void testQueenLessThanKing() {
        assertEquals( -1, Integer.signum(Value.QUEEN.compareTo(Value.KING)));
    }
    
    @Test
    public void testKingGreaterThanQueen() {
        assertEquals(1, Integer.signum(Value.KING.compareTo(Value.QUEEN)));
    }
    
}
