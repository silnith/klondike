package org.silnith.util;

import java.beans.ConstructorProperties;


/**
 * A {@link Pair} that is {@link Comparable}.
 * 
 * <p>This class implements a natural ordering that is <dfn>consistent with
 * equals</dfn> if and only if both types {@code S} and {@code T} are also
 * consistent with equals. An element that is {@code null} is sorted before any
 * non-{@code null} element.</p>
 * 
 * <p>The ordering imposed by this class is based on the first element of the pair,
 * and only if the first comparison is equal is the second element checked.</p>
 * 
 * @param <S> the type of the first element
 * @param <T> the type of the second element
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ComparablePair<S extends Comparable<? super S>, T extends Comparable<? super T>> extends Pair<S, T>
        implements Comparable<Pair<S, T>> {
        
    /**
     * Constructs a new pair with the given elements. Either or both elements
     * may be {@code null}.
     * 
     * @param first the first element
     * @param second the second element
     */
    @ConstructorProperties({ "first", "second" })
    public ComparablePair(final S first, final T second) {
        super(first, second);
    }
    
    @Override
    public int compareTo(final Pair<S, T> o) {
        final int firstResult = compareToOrNull(getFirst(), o.getFirst());
        if (firstResult != 0) {
            return firstResult;
        } else {
            return compareToOrNull(getSecond(), o.getSecond());
        }
    }
    
    private <U extends Comparable<? super U>> int compareToOrNull(final U a, final U b) {
        if (a == null) {
            if (b == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (b == null) {
                return 1;
            } else {
                return a.compareTo(b);
            }
        }
    }
    
    @Override
    public String toString() {
        return "ComparablePair<" + getFirst() + ", " + getSecond() + ">";
    }
    
}
