package org.silnith.game.solitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.silnith.deck.Card;


/**
 * A column on the Solitaire board.
 * 
 * <p>A column consists of a number of face-down cards and a number of face-up
 * cards.  The face-down cards are initialized when the board is first dealt.
 * When there are no face-up cards, the top face-down card is flipped to face up.
 * If there are no cards remaining, the column is empty and any {@link org.silnith.deck.Value#KING King}
 * may be moved to it.</p>
 * 
 * <p>Face-up cards in a column must obey the rules of a run.  Cards may be
 * stacked on top of other face-up cards to make a run provided that a card is
 * only placed on top of a card of the opposite color and of one value higher.
 * For example, a {@link org.silnith.deck.Value#TWO two} of {@link org.silnith.deck.Suit#CLUB clubs}
 * may be placed on top of a {@link org.silnith.deck.Value#THREE three}
 * of {@link org.silnith.deck.Suit#HEART hearts}.</p>
 */
public class Column {
    
    private final List<Card> faceDown;
    
    private final List<Card> faceUp;
    
    /**
     * Constructs a new column with the provided face-down and face-up cards.
     * 
     * @param faceDownCards the face-down cards
     * @param faceUpCards the face-up run of cards
     */
    public Column(List<? extends Card> faceDownCards, List<? extends Card> faceUpCards) {
        super();
        if (faceDownCards != null && !faceDownCards.isEmpty()
        		&& (faceUpCards == null || faceUpCards.isEmpty())) {
            /*
             * This is the special case where there are face-down cards,
             * but no face-up cards.  This flips the top face-down card
             * to make it face-up.
             */
        	assert faceDownCards.size() >= 1;
            final int faceDownIndexToFlip = faceDownCards.size() - 1;
            final Card newTopCard = faceDownCards.get(faceDownIndexToFlip);
            this.faceDown = new ArrayList<>(faceDownCards.subList(0, faceDownIndexToFlip));
            this.faceUp = Collections.singletonList(newTopCard);
        } else {
            if (faceDownCards == null) {
                this.faceDown = Collections.emptyList();
            } else {
            	this.faceDown = new ArrayList<Card>(faceDownCards);
            }
            if (faceUpCards == null) {
                this.faceUp = Collections.emptyList();
            } else {
            	this.faceUp = new ArrayList<Card>(faceUpCards);
            }
        }
        /*
         * It actually performs significantly better to copy the lists than to
         * use the provided lists.
         */
    }
    
    /**
     * Returns {@code true} if the column has any face-down cards.
     * 
     * @return {@code true} if the column has any face-down cards
     */
    public boolean hasFaceDownCards() {
        return !faceDown.isEmpty();
    }
    
    /**
     * Returns {@code true} if the column has any face-up cards.
     * 
     * @return {@code true} if the column has any face-up cards
     */
    public boolean hasFaceUpCards() {
        return !faceUp.isEmpty();
    }
    
    /**
     * Returns the number of face-down cards in the column.
     * 
     * @return the number of face-down cards in the column
     */
    public int getNumberOfFaceDownCards() {
        return faceDown.size();
    }
    
    /**
     * Returns the number of face-up cards in the column.
     * 
     * @return the number of face-up cards in the column
     */
    public int getNumberOfFaceUpCards() {
        return faceUp.size();
    }
    
    /**
     * Returns the face-up cards.
     * 
     * <p>If there are more than one, they must obey the rules of a run.</p>
     * 
     * @return the face-up run of cards
     */
    public List<Card> getFaceUpCards() {
        return faceUp;
    }
    
    /**
     * Returns the top face-up card in the column.
     * 
     * @return the top card
     * @throws IllegalArgumentException if there are no face-up cards in the column
     */
    public Card getTopCard() {
    	if (faceUp.isEmpty()) {
    		throw new IllegalArgumentException("No face-up cards in the column.");
    	}
    	return faceUp.get(faceUp.size() - 1);
    }
    
    /**
     * Returns a run of cards from the column.
     * 
     * @param numberOfCards the number of cards to take from the current column run
     * @return the run of cards
     * @throws IllegalArgumentException if the number of cards is less than {@code 1},
     *         or exceeds the number of face-up cards
     */
    public List<Card> getTopCards(final int numberOfCards) {
        if (numberOfCards < 1) {
            throw new IllegalArgumentException();
        }
        if (numberOfCards > faceUp.size()) {
            throw new IllegalArgumentException();
        }
        
        final int end = faceUp.size();
        final int start = end - numberOfCards;
        assert start >= 0;
        return faceUp.subList(start, end);
    }
    
    /**
     * Returns a copy of this column with a run of cards removed.
     * 
     * @param numberOfCards the number of cards to take from the current column run
     * @return a copy of the current column, with a run of the requested size removed.
     *         This may end up causing a face-down card to be flipped.
     * @throws IllegalArgumentException if the number of cards is less than {@code 1},
     *         or exceeds the number of face-up cards
     */
    public Column getWithoutTopCards(final int numberOfCards) {
        if (numberOfCards < 1) {
            throw new IllegalArgumentException();
        }
        if (numberOfCards > faceUp.size()) {
            throw new IllegalArgumentException();
        }
        
        final int end = faceUp.size();
        final int start = end - numberOfCards;
        assert start >= 0;
        return new Column(faceDown, faceUp.subList(0, start));
    }
    
    /**
     * Returns a copy of this column with the given run of cards added.
     * 
     * @param newCards the new run of cards
     * @return a copy of the column with the new run of cards added
     * @throws IllegalArgumentException if the run is {@code null} or empty
     */
    public Column getWithCards(final List<Card> newCards) {
        if (newCards == null || newCards.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final List<Card> newFaceUp = new ArrayList<>(faceUp.size() + newCards.size());
        newFaceUp.addAll(faceUp);
        newFaceUp.addAll(newCards);
        return new Column(faceDown, newFaceUp);
    }
    
    /**
     * Returns a copy of this column with the given card added to the run.
     * 
     * @param newCard the card to add
     * @return a copy of the column with the added card
     * @throws IllegalArgumentException if the card is {@code null}
     */
    public Column getWithCard(final Card newCard) {
        if (newCard == null) {
            throw new IllegalArgumentException();
        }
        final List<Card> newFaceUp = new ArrayList<>(faceUp.size() + 1);
        newFaceUp.addAll(faceUp);
        newFaceUp.add(newCard);
        return new Column(faceDown, newFaceUp);
    }
    
    @Override
    public int hashCode() {
        return 0x1280ce7a ^ faceDown.hashCode() ^ Integer.rotateLeft(faceUp.hashCode(), 16);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Column) {
            final Column column = (Column) obj;
            if (faceDown.size() != column.faceDown.size()) {
                return false;
            }
            if (faceUp.size() != column.faceUp.size()) {
                return false;
            }
            return faceUp.equals(column.faceUp) && faceDown.equals(column.faceDown);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "Column (down: " + faceDown + ", up: " + faceUp + ")";
    }
    
}
