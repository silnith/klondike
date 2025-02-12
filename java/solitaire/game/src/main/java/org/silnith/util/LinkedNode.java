package org.silnith.util;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * An immutable, forwards-linked list. Instances of this list are created by
 * concatenating a new element onto the beginning of an existing list. The
 * advantage is that multiple lists can share the same tail safely, which can
 * provide memory savings for certain specific use-cases.
 * 
 * <p>Lists of this type will always have at least one element if they are non-
 * {@code null}. An empty list cannot be represented by this implementation.</p>
 * 
 * <p>Users of the language Lisp will recognize this paradigm as similar to the
 * {@code car}, {@code cdr}, {@code cons} concept.</p>
 * 
 * @param <E> the type of elements in this list
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class LinkedNode<E> extends AbstractSequentialList<E> {
    
    /**
     * Returns a {@link LinkedNode} that contains the elements of the provided
     * collection as returned by the collection&rsquo;s iterator.
     * 
     * @param <T> the type of elements in the collection
     * @param c the collection to copy
     * @return a {@link LinkedNode} containing the elements of the collection
     */
    public static <T> LinkedNode<T> valueOf(final Collection<T> c) {
        final List<T> copy;
        if (c instanceof List) {
            copy = (List<T>) c;
        } else {
            copy = new ArrayList<>(c);
        }
        return valueOf(copy);
    }
    
    /**
     * Returns a {@link LinkedNode} that contains the elements of the provided
     * list, in order.
     * 
     * @param <T> the type of elements in the list
     * @param list the list to copy
     * @return a {@link LinkedNode} containing the elements of the list
     */
    public static <T> LinkedNode<T> valueOf(final List<T> list) {
        if (list instanceof LinkedNode) {
            // This is necessary because a LinkedNode cannot be iterated
            // backwards. Therefore we check for it and simply return
            // the LinkedNode itself, since it is immutable.
            final LinkedNode<T> node = (LinkedNode<T>) list;
            return valueOf(node);
        } else {
            final ListIterator<T> iter = list.listIterator(list.size());
            LinkedNode<T> tail = null;
            while (iter.hasPrevious()) {
                tail = new LinkedNode<T>(iter.previous(), tail);
            }
            return tail;
        }
    }
    
    /**
     * Returns a {@link LinkedNode} that contains the elements of the provided
     * list, in order.
     * 
     * <p>Since {@link LinkedNode} is immutable, this simply returns the input.</p>
     * 
     * @param <T> the type of elements in the list
     * @param node the list
     * @return a {@link LinkedNode} containing the elements of the list
     */
    public static <T> LinkedNode<T> valueOf(final LinkedNode<T> node) {
        return node;
    }
    
    private final E value;
    
    private final LinkedNode<E> next;
    
    private final int size;
    
    /**
     * Creates a new list with one element.
     * 
     * @param e the element to put in the list
     */
    public LinkedNode(final E e) {
        this(e, null);
    }
    
    /**
     * Prepends one element to the start of an existing list as a new list.
     * 
     * @param e the element to prepend to the list
     * @param list the list to extend
     */
    public LinkedNode(final E e, final LinkedNode<E> list) {
        super();
        this.value = e;
        this.next = list;
        if (list == null) {
            this.size = 1;
        } else {
            if (list.size == Integer.MAX_VALUE) {
                this.size = Integer.MAX_VALUE;
            } else {
                this.size = list.size + 1;
            }
        }
    }
    
    /**
     * Returns the first element of the list. This is equivalent to
     * {@code get(0)}.
     * 
     * @return the {@code car} of this list
     * @see #getNext()
     */
    public E getFirst() {
        return value;
    }
    
    /**
     * Returns the remainder of this list without the first element. May be
     * {@code null}.
     * 
     * @return the {@code cdr} of this node
     * @see #getFirst()
     */
    public LinkedNode<E> getNext() {
        return next;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
	public boolean isEmpty() {
    	/*
    	 * Overridden for efficiency, since this type of list can never be empty.
    	 */
		return false;
	}

	/**
     * Returns the index of the last occurrence of the specified element in this
     * list, or {@code -1} if this list does not contain the element. More
     * formally, returns the highest index <var>i</var> such that
     * {@code (o==null ? get(i)==null : o.equals(get(i)))}, or {@code -1} if
     * there is no such index.
     * 
     * <p>This implementation iterates forward over the entire list and returns the
     * index of the last element found. It must iterate the entire list because
     * this type of list cannot be iterated backwards.</p>
     * 
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public int lastIndexOf(final Object o) {
        final ListIterator<E> iter = listIterator();
        int index = -1;
        if (o == null) {
            while (iter.hasNext()) {
            	final E element = iter.next();
                if (element == null) {
                    index = iter.previousIndex();
                }
            }
        } else {
            while (iter.hasNext()) {
            	final E element = iter.next();
                if (o.equals(element)) {
                    index = iter.previousIndex();
                }
            }
        }
        return index;
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        return new LinkedNodeIterator<E>(index, this);
    }
    
    private static class LinkedNodeIterator<E> implements ListIterator<E> {
        
        private LinkedNode<E> position;
        
        private int index;
        
        public LinkedNodeIterator(final int index, final LinkedNode<E> list) {
            super();
            if (index < 0) {
                throw new IndexOutOfBoundsException();
            }
            this.position = list;
            this.index = 0;
            while (this.index < index) {
                if (this.position == null) {
                    throw new IndexOutOfBoundsException();
                }
                this.position = this.position.next;
                this.index++ ;
            }
        }
        
        @Override
        public boolean hasNext() {
            return position != null;
        }
        
        @Override
        public E next() {
            if (position == null) {
                throw new NoSuchElementException();
            }
            final E val = position.value;
            position = position.next;
            index++ ;
            return val;
        }
        
        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public E previous() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int nextIndex() {
            // What should the behavior be if index overflows?
            return index;
        }
        
        @Override
        public int previousIndex() {
            return index - 1;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void set(final E e) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final E e) {
            throw new UnsupportedOperationException();
        }
        
    }
    
}
