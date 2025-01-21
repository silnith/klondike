package org.silnith.game.search;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;

public class SequentialDepthFirstSearch<M extends Move<B>, B> {

	private final Game<M, B> game;
	private final Deque<GameState<M, B>> queue;
	private final Collection<GameState<M, B>> wins;
	private final AtomicLong gameStatesExamined;
	private final AtomicLong gameStatesPruned;

	public SequentialDepthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState) {
		super();
		if (game == null) {
			throw new IllegalArgumentException("Game cannot be null.");
		}
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state cannot be null.");
		}
		this.game = game;
		this.queue = new ArrayDeque<>();
		this.wins = new ArrayList<>();
		this.gameStatesExamined = new AtomicLong();
		this.gameStatesPruned = new AtomicLong();
		
		this.queue.addLast(initialState);
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
	
	public long getNumberOfGameStatesExamined() {
		return gameStatesExamined.get();
	}

	public Future<Collection<GameState<M, B>>> search() {
		GameState<M, B> gameState = queue.pollLast();
		while (gameState != null) {
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
		return CompletableFuture.completedFuture(wins);
	}

}
