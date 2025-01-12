package org.silnith.game;

import java.beans.ConstructorProperties;

import org.silnith.game.move.Move;
import org.silnith.util.LinkedNode;
import org.silnith.util.Pair;


/**
 * A single node in a game tree. This keeps track of the history of a game, both
 * the sequence of moves made from the beginning and the state of the board
 * after every move.
 * 
 * <p>It is assumed that {@code node.getMoves().size() == node.getBoards().size()}.</p>
 *
 * @param <M> the move type for the game.  This can simply be {@code Move<B>},
 *         but the interface allows specifying a subtype in the case that a
 *         game-specific interface is needed.
 * @param <B> the board type for the game
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class GameState<M extends Move<B>, B> extends Pair<LinkedNode<M>, LinkedNode<B>> {
    
    /**
     * Constructs a new game state with the given list of moves and associated
     * board states.
     *
     * @param moves the list of moves
     * @param boards the list of boards
     */
    @ConstructorProperties({ "moves", "boards" })
    public GameState(final LinkedNode<M> moves, final LinkedNode<B> boards) {
        super(moves, boards);
    }
    
    /**
     * Constructs an initial game state with the given initial move and board.
     *
     * @param initialMove the initial move. This is often a form of "deal deck".
     * @param initialBoard the initial board
     */
    public GameState(final M initialMove, final B initialBoard) {
        this(new LinkedNode<>(initialMove), new LinkedNode<>(initialBoard));
    }
    
    /**
     * Constructs a new game state by appending the given move and board to the
     * previous game state represented by <var>parent</var>.
     *
     * @param parent the previous game state
     * @param move the new move to append
     * @param board the new board to append
     */
    public GameState(final GameState<M, B> parent, final M move, final B board) {
        this(new LinkedNode<>(move, parent.getMoves()), new LinkedNode<>(board, parent.getBoards()));
    }
    
    /**
     * Constructs a new game state by applying the given move to the most recent
     * board in the given <var>parent</var> game state.
     *
     * @param parent the previous game state
     * @param move the new move to apply to the game state
     */
    public GameState(final GameState<M, B> parent, final M move) {
        this(parent, move, move.apply(parent.getBoards().get(0)));
    }
    
    /**
     * Returns the list of moves.
     *
     * @return the list of moves
     */
    public LinkedNode<M> getMoves() {
        return getFirst();
    }
    
    /**
     * Returns the list of board states.
     *
     * @return the list of board states
     */
    public LinkedNode<B> getBoards() {
        return getSecond();
    }
    
}
