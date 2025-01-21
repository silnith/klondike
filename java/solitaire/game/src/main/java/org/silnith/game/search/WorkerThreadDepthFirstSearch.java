package org.silnith.game.search;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;

public class WorkerThreadDepthFirstSearch<M extends Move<B>, B> {

	private final Game<M, B> game;
	private final List<Thread> threads;
	private final Deque<GameState<M, B>> queue;
	private final Collection<GameState<M, B>> wins;
	private final AtomicLong gameStatesExamined;
	private final AtomicLong gameStatesPruned;
	private volatile boolean cancelled;

	public WorkerThreadDepthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState, final int numThreads) {
		super();
		if (game == null) {
			throw new IllegalArgumentException("Game cannot be null.");
		}
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state cannot be null.");
		}
		this.game = game;
		this.threads = new ArrayList<>(numThreads);
		this.queue = new ConcurrentLinkedDeque<>();
		this.wins = new ConcurrentLinkedQueue<>();
		this.gameStatesExamined = new AtomicLong();
		this.gameStatesPruned = new AtomicLong();
		this.cancelled = false;
		
		this.queue.addLast(initialState);
		
		for (int i = 0; i < numThreads; i++) {
			threads.add(new Thread(new Worker()));
		}
	}

	public void printStatistics(final PrintStream out) {
		out.printf(Locale.US,
				"Nodes examined: %,d\n"
				+ "Nodes pruned: %,d\n"
				+ "Queue size: %,d\n"
				+ "Wins: %,d\n",
				gameStatesExamined.get(),
				gameStatesPruned.get(),
				queue.size(),
				wins.size());
		out.flush();
	}

	public Future<Collection<GameState<M, B>>> search() {
		for (final Thread thread : threads) {
			thread.start();
		}
		return new Watcher();
	}
	
	private final class Watcher implements Future<Collection<GameState<M, B>>> {

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			cancelled = true;
			if (mayInterruptIfRunning) {
				for (final Thread thread : threads) {
					thread.interrupt();
				}
			}
			return false;
		}

		@Override
		public boolean isCancelled() {
			return cancelled;
		}

		@Override
		public boolean isDone() {
			// TODO: Return false if not yet started.
			for (final Thread thread : threads) {
				if (thread.isAlive()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public Collection<GameState<M, B>> get() throws InterruptedException, ExecutionException {
			for (final Thread thread : threads) {
				thread.join();
			}
			return wins;
		}

		@Override
		public Collection<GameState<M, B>> get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException, TimeoutException {
			for (final Thread thread : threads) {
				// TODO: Subtract elapsed time from subsequent invocations.
				unit.timedJoin(thread, timeout);
			}
			return wins;
		}
		
	}

	private final class Worker implements Runnable {
		
		@Override
		public void run() {
			GameState<M, B> gameState = queue.pollLast();
			while (gameState != null && !cancelled) {
				gameStatesExamined.incrementAndGet();
				final Collection<M> moves = game.findAllMoves(gameState);
				for (final M move : moves) {
					final GameState<M, B> potentialGameState = new GameState<>(gameState, move);
					final GameState<M, B> filteredGameState = game.pruneGameState(potentialGameState);
					if (filteredGameState == null) {
						gameStatesPruned.incrementAndGet();
						continue;
					}
					
					if (game.isWin(filteredGameState)) {
						wins.add(filteredGameState);
					} else {
						queue.addLast(filteredGameState);
					}
				}
				
				gameState = queue.pollLast();
			}
		}
		
	}

}
