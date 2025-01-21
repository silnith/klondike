package org.silnith.game;

import java.util.Collection;

import org.silnith.game.move.Move;


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
     * Returns whether the given game state is a winning game state for this game.
     * 
     * @param state the game state to check
     * @return {@code true} if the game state represents a win
     */
    boolean isWin(GameState<M, B> state);

    /**
     * Returns all the legal moves for the provided game state. The current game
     * board can be retrieved using {@code state.getBoards().get(0)}.
     * 
     * <p>The set of moves returned may vary depending on the configuration of the
     * game engine. See the individual engines for configuration parameters.</p>
     * 
     * @param state the game state to search for legal moves
     * @return a collection of legal moves for the given game state
     */
    Collection<M> findAllMoves(GameState<M, B> state);

    /**
     * Possibly prunes or modifies the given game state based on the state
     * history.  The returned value may be a different object than the input,
     * so callers should always use the return value if it is not {@code null}.
     * 
     * @param state the game state to check
     * @return {@code null} if the game state was pruned, otherwise a valid game
     *         state. This might not be the same game state as the parameter.
     */
    GameState<M, B> pruneGameState(GameState<M, B> state);

}
