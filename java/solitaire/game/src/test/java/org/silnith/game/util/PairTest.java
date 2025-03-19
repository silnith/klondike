package org.silnith.game.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PairTest {
    
    private Pair<Integer, String> pair;
    
    @Test
    public void testGetFirst() {
        pair = new Pair<Integer, String>(5, "foo");
        
        assertEquals(Integer.valueOf(5), pair.getFirst());
    }
    
    @Test
    public void testGetFirst_Null() {
        pair = new Pair<Integer, String>(null, "foo");
        
        assertNull(pair.getFirst());
    }
    
    @Test
    public void testGetSecond() {
        pair = new Pair<Integer, String>(5, "foo");
        
        assertEquals("foo", pair.getSecond());
    }
    
    @Test
    public void testGetSecond_Null() {
        pair = new Pair<Integer, String>(5, null);
        
        assertNull(pair.getSecond());
    }
    
    @Test
    public void testEquals() {
        pair = new Pair<Integer, String>(5, "foo");
        final Pair<Integer, String> otherPair = new Pair<Integer, String>(5, "foo");
        
        assertTrue(pair.equals(otherPair));
        assertEquals(pair.hashCode(), otherPair.hashCode());
    }
    
    @Test
    public void testEquals_Null() {
        pair = new Pair<Integer, String>(5, "foo");
        
        assertFalse(pair.equals(null));
    }
    
    @Test
    public void testToString() {
        pair = new Pair<Integer, String>(5, "foo");
        
        assertEquals("Pair<5, foo>", pair.toString());
    }
    
    @Test
    public void testToString_FirstNull() {
        pair = new Pair<Integer, String>(null, "foo");
        
        assertEquals("Pair<null, foo>", pair.toString());
    }
    
    @Test
    public void testToString_SecondNull() {
        pair = new Pair<Integer, String>(5, null);
        
        assertEquals("Pair<5, null>", pair.toString());
    }
    
    @Test
    public void testToString_BothNull() {
        pair = new Pair<Integer, String>(null, null);
        
        assertEquals("Pair<null, null>", pair.toString());
    }
    
}
