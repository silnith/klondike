package org.silnith.util;

import java.beans.ConstructorProperties;
import java.util.Objects;


/**
 * A simple class that can hold two elements. Instances of this class are
 * immutable.
 * 
 * <p>This is a two-element tuple. Either or both elements may be {@code null}.
 * This class overrides the {@link #equals(Object)} and {@link #hashCode()}
 * methods to be consistent and based strictly on the corresponding methods of
 * the contained elements. That means if both elements are safe to use as keys
 * in a {@link java.util.HashMap}, then a {@code Pair} of those elements is also
 * safe.</p>
 * 
 * @param <S> the type of the first element
 * @param <T> the type of the second element
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class Pair<S, T> {
    
    private final S first;
    
    private final T second;
    
    /**
     * Constructs a new pair with the given elements. Either or both elements
     * may be {@code null}.
     * 
     * @param first the first element
     * @param second the second element
     */
    @ConstructorProperties({ "first", "second" })
    public Pair(final S first, final T second) {
        super();
        this.first = first;
        this.second = second;
    }
    
    /**
     * Returns the first element.
     * 
     * @return the first element
     */
    public S getFirst() {
        return first;
    }
    
    /**
     * Returns the second element.
     * 
     * @return the second element
     */
    public T getSecond() {
        return second;
    }
    
    @Override
    public int hashCode() {
        final int firstHash;
        if (first == null) {
            firstHash = 0x91b3c861;
        } else {
            firstHash = first.hashCode();
        }
        final int secondHash;
        if (second == null) {
            secondHash = 0xfe28b012;
        } else {
            secondHash = second.hashCode();
        }
        return Integer.rotateLeft(firstHash, 16) ^ secondHash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Pair) {
            final Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Pair<" + first + ", " + second + ">";
    }
    
}
