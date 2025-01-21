package org.silnith.game.search;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;

public class ExecutorServiceBreadthFirstSearch<M extends Move<B>, B> {

	private final Game<M, B> game;
	private final GameState<M, B> initialGameState;
	private final ExecutorService executorService;
	private final Collection<GameState<M, B>> wins;
	private final AtomicLong gameStatesExamined;
	private final AtomicLong gameStatesPruned;

	public ExecutorServiceBreadthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState,
			final ExecutorService executorService) {
		super();
		if (game == null) {
			throw new IllegalArgumentException("Game cannot be null.");
		}
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state cannot be null.");
		}
		this.game = game;
		this.initialGameState = initialState;
		this.executorService = executorService;
		this.wins = new ArrayList<>();
		this.gameStatesExamined = new AtomicLong();
		this.gameStatesPruned = new AtomicLong();
	}
	
	public void printStatistics(final PrintStream out) {
		out.printf(Locale.US,
				"Nodes examined: %,d\n"
                + "Nodes pruned: %,d\n"
                + "Wins: %,d\n",
				gameStatesExamined.get(),
				gameStatesPruned.get(),
				wins.size());
		out.flush();
	}
	
	public Future<Collection<GameState<M, B>>> search() {
		executorService.submit(new NodeExaminer(initialGameState));
		return CompletableFuture.completedFuture(wins);
	}

	private class NodeExaminer implements Runnable {
		
		private final GameState<M, B> gameState;
		
		public NodeExaminer(final GameState<M, B> gameState) {
			this.gameState = gameState;
		}

		@Override
		public void run() {
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
					executorService.submit(new NodeExaminer(filteredGameState));
				}
			}
		}
		
	}

}
