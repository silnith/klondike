package org.silnith.game.search;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;
import org.silnith.util.LinkedNode;

/**
 * A game tree search algorithm implementation that performs a depth-first
 * search running sequentially on a single thread.
 * 
 * @param <M> the move type for the game
 * @param <B> the board type for the game
 */
public class SequentialDepthFirstSearch<M extends Move<B>, B> extends GameTreeSearcher<M, B> {

    private final Deque<LinkedNode<GameState<M, B>>> stack;

    private final Collection<List<GameState<M, B>>> wins;

	/**
	 * Initializes a sequential single-threaded depth-first search.
	 * 
	 * @param game the game
	 * @param initialState the initial game state
	 */
	public SequentialDepthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState) {
		super(game);
		this.stack = new ArrayDeque<>();
		this.wins = new LinkedList<>();
		
		this.stack.addLast(new LinkedNode<GameState<M,B>>(initialState));
	}

	@Override
    public void close() {
    }

    @Override
    public Collection<List<GameState<M, B>>> call() {
        LinkedNode<GameState<M, B>> node = stack.pollLast();
        while (node != null) {
            examineNode(node);
            
            node = stack.pollLast();
        }
        
        return Collections.unmodifiableCollection(wins);
    }

    @Override
    protected int getQueueSize() {
        return stack.size();
    }

    @Override
    protected int getWinCount() {
        return wins.size();
    }

    @Override
    protected void queueNode(final LinkedNode<GameState<M, B>> node) {
        stack.addLast(node);
    }

    @Override
    protected void addWin(final List<GameState<M, B>> node) {
        wins.add(node);
    }

}
