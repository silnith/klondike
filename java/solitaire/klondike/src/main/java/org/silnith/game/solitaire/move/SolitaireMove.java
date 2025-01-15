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
     * 
     * @return the cards moved
     * @throws RuntimeException if {@link #hasCards()} returns {@code false}
     */
    public List<Card> getCards();
    
}
