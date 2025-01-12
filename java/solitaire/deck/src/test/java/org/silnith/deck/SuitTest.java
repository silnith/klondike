package org.silnith.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SuitTest {
    
    @Test
    public void testGetClubColor() {
        assertEquals(Suit.Color.BLACK, Suit.CLUB.getColor());
    }
    
    @Test
    public void testGetDiamondColor() {
        assertEquals(Suit.Color.RED, Suit.DIAMOND.getColor());
    }
    
    @Test
    public void testGetHeartColor() {
        assertEquals(Suit.Color.RED, Suit.HEART.getColor());
    }
    
    @Test
    public void testGetSpadeColor() {
        assertEquals(Suit.Color.BLACK, Suit.SPADE.getColor());
    }
    
    @Test
    public void testToSymbolClub() {
        assertNotNull(Suit.CLUB.toSymbol());
    }
    
    @Test
    public void testToSymbolDiamond() {
        assertNotNull(Suit.DIAMOND.toSymbol());
    }
    
    @Test
    public void testToSymbolHeart() {
        assertNotNull(Suit.HEART.toSymbol());
    }
    
    @Test
    public void testToSymbolSpade() {
        assertNotNull(Suit.SPADE.toSymbol());
    }
    
}
