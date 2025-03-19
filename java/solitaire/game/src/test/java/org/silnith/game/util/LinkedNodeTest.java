package org.silnith.game.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;


public class LinkedNodeTest {
    
    /*
     * Methods from java.lang.Object
     */
    
    @Test
    public void testEquals() {
        final LinkedNode<String> list1 = new LinkedNode<String>("foo");
        final LinkedNode<String> list2 = new LinkedNode<String>("foo");
        
        assertTrue(list1.equals(list2));
    }
    
    @Test
    public void testHashCode() {
        final LinkedNode<String> list1 = new LinkedNode<String>("foo");
        final LinkedNode<String> list2 = new LinkedNode<String>("foo");
        
        assertEquals(list1.hashCode(), list2.hashCode());
    }
    
    @Test
    public void testEqualsUsingDifferentConstructor() {
        final LinkedNode<String> list1 = new LinkedNode<String>("foo");
        final LinkedNode<String> list2 = new LinkedNode<String>("foo", null);
        
        assertTrue(list1.equals(list2));
    }
    
    @Test
    public void testHashCodeUsingDifferentConstructor() {
        final LinkedNode<String> list1 = new LinkedNode<String>("foo");
        final LinkedNode<String> list2 = new LinkedNode<String>("foo", null);
        
        assertEquals(list1.hashCode(), list2.hashCode());
    }
    
    @Test
    public void testToString() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertEquals("[foo]", list.toString());
    }
    
    /*
     * Methods from java.lang.Iterable
     */
    
    @Test
    public void testIteratorExists() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testIteratorHasNext() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testIteratorHasNextIsIdempotent() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        for (int i = 0; i < 10; i++ ) {
            assertTrue(iterator.hasNext(), "iteration " + i);
        }
    }
    
    @Test
    public void testIteratorNext() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        assertEquals("foo", iterator.next());
    }
    
    @Test
    public void testIteratorNextRemoveFails() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }
    
    @Test
    public void testIteratorSecondHasNext() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        iterator.next();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIteratorSecondNextFails() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        final Iterator<String> iterator = list.iterator();
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }
    
    /*
     * Methods in java.util.Collection
     */
    
    @Test
    public void testSize() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertEquals(1, list.size());
    }
    
    @Test
    public void testIsEmpty() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertFalse(list.isEmpty());
    }
    
    @Test
    public void testContains() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertTrue(list.contains("foo"));
    }
    
    @Test
    public void testContainsMiss() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertFalse(list.contains("bar"));
    }
    
    @Test
    public void testToArray() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertArrayEquals(new Object[] { "foo" }, list.toArray());
    }
    
    @Test
    public void testToTypedArray() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertArrayEquals(new String[] { "foo" }, list.toArray(new String[0]));
    }
    
    @Test
    public void testAdd() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.add("bar"));
    }
    
    @Test
    public void testRemove() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.remove("foo"));
    }
    
    @Test
    public void testRemoveMiss() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertFalse(list.remove("bar"));
    }
    
    @Test
    public void testContainsAll() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertTrue(list.containsAll(Collections.singleton("foo")));
    }
    
    @Test
    public void testAddAll() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.addAll(Collections.singleton("bar")));
    }
    
    @Test
    public void testRemoveAll() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.removeAll(Collections.singleton("foo")));
    }
    
    @Test
    public void testRemoveAllMiss() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertFalse(list.removeAll(Collections.singleton("bar")));
    }
    
    @Test
    public void testRetainAll() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertFalse(list.retainAll(Collections.singleton("foo")));
    }
    
    @Test
    public void testRetainAllMiss() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.retainAll(Collections.singleton("bar")));
    }
    
    @Test
    public void testClear() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.clear());
    }
    
    /*
     * Methods from java.util.List
     */
    
    @Test
    public void testAddAllAtIndex() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.addAll(0, Collections.singleton("bar")));
    }
    
    @Test
    public void testGet() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertEquals("foo", list.get(0));
    }
    
    @Test
    public void testGetWithUnderflow() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(IndexOutOfBoundsException.class, () -> list.get( -1));
    }
    
    @Test
    public void testGetWithOverflow() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }
    
    @Test
    public void testSet() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.set(0, "bar"));
    }
    
    @Test
    public void testAddAtIndex() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.add(0, "bar"));
    }
    
    @Test
    public void testRemoveIndex() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(UnsupportedOperationException.class, () -> list.remove(0));
    }
    
    @Test
    public void testIndexOf() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertEquals(0, list.indexOf("foo"));
    }
    
    @Test
    public void testLastIndexOf() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertEquals(0, list.lastIndexOf("foo"));
    }
    
    @Test
    public void testListIterator() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertNotNull(list.listIterator());
    }
    
    @Test
    public void testListIteratorAtIndex() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertNotNull(list.listIterator(0));
    }
    
    @Test
    public void testListIteratorAtIndexEnd() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertNotNull(list.listIterator(1));
    }
    
    @Test
    public void testListIteratorAtIndexOverflow() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(2));
    }
    
    @Test
    public void testListIteratorAtIndexUnderflow() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator( -1));
    }
    
    @Test
    public void testListIteratorAtIndexEndWithLongList() {
        final LinkedNode<String> list =
                new LinkedNode<String>("foo", new LinkedNode<String>("bar", new LinkedNode<String>("baz")));
                
        assertNotNull(list.listIterator(3));
    }
    
    @Test
    public void testListIteratorAtIndexOverflowWithLongList() {
        final LinkedNode<String> list =
                new LinkedNode<String>("foo", new LinkedNode<String>("bar", new LinkedNode<String>("baz")));
                
        assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(4));
    }
    
    @Test
    public void testListIteratorAtIndexUnderflowWithLongList() {
        final LinkedNode<String> list =
                new LinkedNode<String>("foo", new LinkedNode<String>("bar", new LinkedNode<String>("baz")));
                
        assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator( -1));
    }
    
    @Test
    public void testSubList() {
        final LinkedNode<String> list = new LinkedNode<String>("foo");
        
        assertNotNull(list.subList(0, 1));
    }
    
    @Test
    public void testSizeForSingletonConstructorWithNullValue() {
        final LinkedNode<String> list = new LinkedNode<String>(null);
        
        assertEquals(1, list.size());
    }
    
    @Test
    public void testSizeForConsConstructorWithNullList() {
        final LinkedNode<String> list = new LinkedNode<String>("foo", null);
        
        assertEquals(1, list.size());
    }
    
    @Test
    public void testSizeForConsConstructorWithNullValueAndNullList() {
        final LinkedNode<String> list = new LinkedNode<String>(null, null);
        
        assertEquals(1, list.size());
    }
    
}
