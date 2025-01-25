package org.silnith.game.search;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;
import org.silnith.game.move.MoveFilter;
import org.silnith.util.LinkedNode;

public class WorkerThreadBreadthFirstSearch<M extends Move<B>, B> {

	private final Game<M, B> game;
	private final List<Thread> threads;
	private final Queue<LinkedNode<GameState<M, B>>> queue;
	private final Collection<List<GameState<M, B>>> wins;
	private final AtomicLong gameStatesExamined;
	private final AtomicLong gameStatesPrunedTotal;
	private final Map<Object, AtomicLong> gameStatesPruned;
	private volatile boolean cancelled;

	public WorkerThreadBreadthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState, final int numThreads) {
		super();
		if (game == null) {
			throw new IllegalArgumentException("Game cannot be null.");
		}
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state cannot be null.");
		}
		this.game = game;
		this.threads = new ArrayList<>(numThreads);
		this.queue = new ConcurrentLinkedQueue<>();
		this.wins = new ConcurrentLinkedQueue<>();
		this.gameStatesExamined = new AtomicLong();
		this.gameStatesPrunedTotal = new AtomicLong();
        final Map<Object, AtomicLong> tempMap = new HashMap<>();
        for (final MoveFilter<M, B> filter : this.game.getFilters()) {
            tempMap.put(filter.getStatisticsKey(), new AtomicLong());
        }
        this.gameStatesPruned = Collections.unmodifiableMap(tempMap);
		this.cancelled = false;
		
		this.queue.add(new LinkedNode<>(initialState));
		
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
				gameStatesPrunedTotal.get(),
				queue.size(),
				wins.size());
        for (final MoveFilter<M, B> filter : game.getFilters()) {
            final Object statisticsKey = filter.getStatisticsKey();
            out.printf(Locale.US,
                    "Nodes pruned by filter %s: %,d\n",
                    statisticsKey,
                    gameStatesPruned.get(statisticsKey).get());
        }
		out.flush();
	}
	
	public long getNumberOfGameStatesExamined() {
		return gameStatesExamined.get();
	}

	public Future<Collection<List<GameState<M, B>>>> search() {
		for (final Thread thread : threads) {
			thread.start();
		}
		return new Watcher();
	}
	
	private final class Watcher implements Future<Collection<List<GameState<M, B>>>> {

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
		public Collection<List<GameState<M, B>>> get() throws InterruptedException, ExecutionException {
			for (final Thread thread : threads) {
				thread.join();
			}
			return wins;
		}

		@Override
		public Collection<List<GameState<M, B>>> get(long timeout, TimeUnit unit)
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
	        final Collection<? extends MoveFilter<M, B>> filters = game.getFilters();
	        
			do {
				LinkedNode<GameState<M, B>> gameStateHistory = queue.poll();
				while (gameStateHistory != null && !cancelled) {
					gameStatesExamined.incrementAndGet();
					final GameState<M, B> gameState = gameStateHistory.getFirst();
					final B board = gameState.getBoard();
					final Collection<M> moves = game.findAllMoves(gameStateHistory);
					movesLoop: for (final M move : moves) {
					    final B newBoard = move.apply(board);
					    final GameState<M, B> newGameState = new GameState<>(move, newBoard);
                        final LinkedNode<GameState<M, B>> newHistory = new LinkedNode<>(newGameState, gameStateHistory);
					    for (final MoveFilter<M, B> filter : filters) {
                            if (filter.shouldFilter(newHistory)) {
                                gameStatesPrunedTotal.incrementAndGet();
                                gameStatesPruned.get(filter.getStatisticsKey()).incrementAndGet();
                                continue movesLoop;
                            }
                        }
						
						if (game.isWin(newGameState)) {
							wins.add(newHistory);
						} else {
							queue.add(newHistory);
						}
					}
					
					gameStateHistory = queue.poll();
				}
				try {
					Thread.sleep(TimeUnit.SECONDS.toMillis(1));
				} catch (final InterruptedException e) {
				}
			} while (!cancelled);
		}
		
	}

}
