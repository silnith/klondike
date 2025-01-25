package org.silnith.game.search;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;
import org.silnith.game.move.MoveFilter;
import org.silnith.util.LinkedNode;

public class SequentialBreadthFirstSearch<M extends Move<B>, B> {

	private final Game<M, B> game;
	private final Queue<LinkedNode<GameState<M, B>>> queue;
	private final Collection<List<GameState<M, B>>> wins;
	private final AtomicLong gameStatesExamined;
	private final AtomicLong gameStatesPrunedTotal;
	private final Map<Object, AtomicLong> gameStatesPruned;

	public SequentialBreadthFirstSearch(final Game<M, B> game, final GameState<M, B> initialState) {
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
		this.gameStatesPrunedTotal = new AtomicLong();
		final Map<Object, AtomicLong> tempMap = new HashMap<>();
		for (final MoveFilter<M, B> filter : this.game.getFilters()) {
		    tempMap.put(filter.getStatisticsKey(), new AtomicLong());
		}
		this.gameStatesPruned = Collections.unmodifiableMap(tempMap);
		
		this.queue.add(new LinkedNode<>(initialState));
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
        final Collection<? extends MoveFilter<M, B>> filters = game.getFilters();
        
	    LinkedNode<GameState<M, B>> gameStateHistory = queue.poll();
		while (gameStateHistory != null) {
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
		return CompletableFuture.completedFuture(wins);
	}

}
