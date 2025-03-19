package org.silnith.game.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;
import org.silnith.game.util.LinkedNode;

/**
 * A game tree search algorithm implementation that performs a depth-first
 * search running in parallel across multiple threads.
 * 
 * @param <M> the move type for the game
 * @param <B> the board type for the game
 */
public class WorkerThreadDepthFirstSearch<M extends Move<B>, B>
        extends GameTreeSearcher<M, B> {

	private final List<Worker> workers;
	private final BlockingDeque<LinkedNode<GameState<M, B>>> queue;
	private final Collection<List<GameState<M, B>>> wins;
	private volatile boolean cancelled;

	/**
	 * Initializes a depth-first search across the specified number of threads.
	 * 
	 * @param game the game
	 * @param initialState the initial game state from which to begin the search
	 * @param numThreads the number of threads to use for searching the game tree
	 */
	public WorkerThreadDepthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState,
			final int numThreads) {
		super(game);
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state cannot be null.");
		}
		this.workers = new ArrayList<>(numThreads);
		this.queue = new LinkedBlockingDeque<>();
		this.wins = new ConcurrentLinkedQueue<>();
		this.cancelled = false;

		this.queue.addLast(new LinkedNode<>(initialState));

		for (int i = 0; i < numThreads; i++) {
		    this.workers.add(new Worker());
		}
	}

	@Override
    public void close() {
	    cancelled = true;
    }

	@Override
    public Collection<List<GameState<M, B>>> call() throws InterruptedException {
        final ThreadGroup threadGroup = new ThreadGroup("SearcherWorkers");
        final List<Thread> threads = new ArrayList<>(workers.size());
        for (final Worker worker : workers) {
            final Thread thread = new Thread(threadGroup, worker, "Worker-" + threads.size());
            threads.add(thread);
            thread.start();
        }
        
        boolean finished;
        do {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            
            finished = queue.isEmpty() && !isWorking();
        } while (!finished && !cancelled);
        
        cancelled = true;
        
        for (final Thread thread : threads) {
            thread.join();
        }
        
        return Collections.unmodifiableCollection(wins);
    }

    @Override
    protected int getQueueSize() {
        return queue.size();
    }

    @Override
    protected int getWinCount() {
        return wins.size();
    }

    @Override
    protected void queueNode(final LinkedNode<GameState<M, B>> node) {
        queue.addLast(node);
    }

    @Override
    protected void addWin(final List<GameState<M, B>> node) {
        wins.add(node);
    }

    private boolean isWorking() {
        for (final Worker worker : workers) {
            if (worker.isWorking()) {
                return true;
            }
        }
        return false;
    }

	private class Worker implements Runnable {

	    private volatile boolean working = false;

	    public boolean isWorking() {
            return working;
        }

		@Override
		public void run() {
		    try {
	            LinkedNode<GameState<M, B>> node = queue.pollLast(1, TimeUnit.SECONDS);
	            while (node != null && !cancelled) {
	                working = true;
	                examineNode(node);
	                working = false;

	                node = queue.pollLast(1, TimeUnit.SECONDS);
	            }
		    } catch (final InterruptedException e) {
                e.printStackTrace();
            }
		}

	}

}
