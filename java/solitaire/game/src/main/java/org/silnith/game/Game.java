package org.silnith.game;

import java.util.Collection;
import java.util.List;

import org.silnith.game.move.Move;
import org.silnith.game.move.MoveFilter;


/**
 * A generic interface for any type of game.
 *
 * @param <M> the move type for the game.  This can simply be {@code Move<B>},
 *         but the interface allows specifying a subtype in the case that a
 *         game-specific interface is needed.
 * @param <B> the board type for the game
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public interface Game<M extends Move<B>, B> {

    /**
     * Returns whether the given reverse list of game states culminates in a win for this game.
     * The list begins with the most recent move and resulting board, and ends with the initial
     * move and board of the game.
     * The most recent game state is {@code gameStates.get(0)}.
     * The current board is {@code gameStates.get(0).getBoard()}.
     * 
     * @param gameStates the reverse list of game states to check.  The list is guaranteed
     *         to have at least one game state in it.
     * @return {@code true} if the reverse sequence of game states culminates in a win
     */
    boolean isWin(List<GameState<M, B>> gameStates);

    /**
     * Returns all the legal moves for the provided game state history.
     * The most recent game state is {@code gameState.get(0)}.
     * 
     * <p>The set of moves returned may vary depending on the configuration of the
     * game engine. See the individual engines for configuration parameters.</p>
     * 
     * @param gameStates the game state history.  The list is guaranteed to have
     *         at least one game state in it.
     * @return a collection of legal moves for the given game state
     */
    Collection<M> findAllMoves(List<GameState<M, B>> gameStates);

    /**
     * Returns filters for pruning the game search space.
     * 
     * <p>The search space for any non-trivial game is massive.
     * Realistically no search will ever complete unless the search space
     * is pruned in some way.  This method provides a way for an implementation
     * of a game&rsquo;s logic to provide filters for pruning the search
     * space, in a way that is meaningful for the specifics of the game.</p>
     * 
     * <p>The search engine will run all the provided filters on every game state
     * in the search tree.  If any filter returns {@code true}, that game state
     * will be pruned and no further search of it will happen.</p>
     * 
     * <p>The collection of filters will only be queried when the search begins,
     * so there is no value in altering the collection of returned filters
     * beyond their initial creation.</p>
     * 
     * <p>If the game implementation does not wish to provide any filters,
     * it should return {@link java.util.Collections#emptySet()}.</p>
     * 
     * @return a collection of game state filters for pruning the search space
     */
    Collection<? extends MoveFilter<M, B>> getFilters();

}
