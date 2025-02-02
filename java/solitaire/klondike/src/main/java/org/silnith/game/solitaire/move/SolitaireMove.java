package org.silnith.game.solitaire.move;

import java.util.List;

import org.silnith.deck.Card;
import org.silnith.game.move.Move;
import org.silnith.game.solitaire.Board;


/**
 * The common interface for game moves.
 * 
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public interface SolitaireMove extends Move<Board> {
    
    /**
     * Returns whether the move involves one or more cards.
     * 
     * @return {@code true} if the move involves cards
     */
    public boolean hasCards();
    
    /**
     * Returns the cards that have been moved.
     * Returns an empty list if there were no cards moved.
     * 
     * @return the cards moved
     */
    public List<Card> getCards();
    
    /**
     * Returns {@code true} if this move changes the stock pile index.
     * Moves that advance or recycle the stock pile will return {@code true}.
     * Drawing from the stock pile also adjusts the index, so will also
     * return {@code true} for this method.     
     * 
     * @return {@code true} if this move adjusts the stock pile index
     */
    public boolean isStockPileModification();
    
    /**
     * Returns {@code true} if this move draws a card from the stock pile.
     * 
     * @return {@code true} if this move draws a card from the stock pile
     */
    public boolean isFromStockPile();
    
    /**
     * Returns {@code true} if this move takes a card from the foundation.
     * 
     * @return {@code true} if this move takes a card from the foundation
     */
    public boolean isFromFoundation();
    
    /**
     * Returns {@code true} if this move takes cards from a column.
     * 
     * @return {@code true} if this move takes cards from a column
     */
    public boolean isFromColumn();
    
    /**
     * Returns {@code true} if this move takes cards from the specific column.
     * 
     * @param columnIndex the column to check whether the move takes cards from
     * @return {@code true} if this move takes cards from the specific column
     */
    public boolean isFromColumn(int columnIndex);
    
    /**
     * Returns the index of the column from which this move takes cards.
     * Throws an exception if either {@link #hasCards()} or {@link #isFromColumn()}
     * returns {@code false}.
     * 
     * @return the index of the column from which this move takes cards
     */
    public int getFromColumnIndex();
    
    /**
     * Returns {@code true} if this move puts a card into the foundation.
     * 
     * @return {@code true} if this move puts a card into the foundation
     */
    public boolean isToFoundation();
    
    /**
     * Returns {@code true} if this move puts cards onto a column run.
     * 
     * @return {@code true} if this move puts cards onto a column run
     */
    public boolean isToColumn();
    
    /**
     * Returns {@code true} if this move puts cards onto the specific column.
     * 
     * @param columnIndex the column to check whether receives cards
     * @return {@code true} if this move puts cards onto the specific column
     */
    public boolean isToColumn(int columnIndex);

    /**
     * Returns the index of the column to which this move adds cards.
     * Throws an exception if either {@link #hasCards()} or {@link #isToColumn()}
     * returns {@code false}.
     * 
     * @return the index of the column to which this move adds cards
     */
    public int getToColumnIndex();
    
}
