package org.silnith.game.move;

/**
 * The common interface for game moves.
 *
 * @param <T> the board type
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public interface Move<T> {
    
    /**
     * Applies this move to the given board, returning the new board.
     * 
     * @param board the board to which to apply the move
     * @return the new board after the move has been completed
     */
    T apply(T board);
    
}
