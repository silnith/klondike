package org.silnith.game.move;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.silnith.game.GameState;

/**
 * A common interface for predicates used to filter the game search tree.
 * 
 * @param <M> the move type for the game
 * @param <B> the board type for the game
 * @see GameState
 */
public interface MoveFilter<M extends Move<B>, B> {

    /**
     * Returns a key that can be used in a {@link Map} for gathering statistics
     * about how this filter performs.  This key should be <dfn>consistent with equals</dfn>.
     * 
     * <p>It is helpful if the object has a {@link #toString()} implementation that
     * produces a meaningful and user-readable string.</p>
     * 
     * <p>Returning a static string works very well for implementations.</p>
     * 
     * @return a map key for storing statistical information about use of the filter
     */
    public Object getStatisticsKey();

    /**
     * Returns {@code true} if the game state should be pruned from the
     * search tree of possible moves for the game.
     * 
     * <p>The list provided as the parameter will be a singly-linked list
     * with linear access time for elements.  Implementations are strongly
     * encouraged to use an {@link Iterator} to access elements, and to
     * only traverse the list once if possible.</p>
     * 
     * <p>Element {@code gameStateHistory.get(0)} will be the current move
     * and resulting board.  Element
     * {@code gameStateHistory.get(gameStateHistory.size() - 1)} will be the
     * very first move of the game.</p>
     * 
     * @param gameStateHistory a sequence of game states, beginning with
     *         the most recent move and resulting board
     * @return {@code true} if the node should be pruned from the search tree
     */
    public boolean shouldFilter(List<GameState<M, B>> gameStateHistory);

}
