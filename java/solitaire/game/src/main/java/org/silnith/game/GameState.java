package org.silnith.game;

import org.silnith.game.move.Move;
import org.silnith.game.util.Pair;

/**
 * A single node in a game tree.
 * 
 * @param <M> the move type for the game. This can simply be {@code Move<B>},
 *            but the interface allows specifying a subtype in the case that a
 *            game-specific interface is needed.
 * @param <B> the board type for the game
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class GameState<M extends Move<B>, B> extends Pair<M, B> {

    /**
     * Constructs a game state with the given move and board.
     *
     * @param move  the move
     * @param board the board
     */
    public GameState(final M move, final B board) {
        super(move, board);
    }

    /**
     * Returns the move.
     *
     * @return the move
     */
    public M getMove() {
        return getFirst();
    }

    /**
     * Returns the board.
     *
     * @return the board
     */
    public B getBoard() {
        return getSecond();
    }

}
