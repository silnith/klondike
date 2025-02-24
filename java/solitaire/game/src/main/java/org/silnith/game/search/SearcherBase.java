package org.silnith.game.search;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.Move;
import org.silnith.game.move.MoveFilter;
import org.silnith.util.LinkedNode;

/**
 * Abstract base class for game tree search algorithms.
 * This provides a common implementation of the logic to expand a single node
 * in the game tree
 * 
 * @param <M> the move type for the game
 * @param <B> the board type for the game
 */
public abstract class SearcherBase<M extends Move<B>, B> implements AutoCloseable, Callable<Collection<List<GameState<M, B>>>> {

    protected final Game<M, B> game;
    protected final Collection<? extends MoveFilter<M, B>> gameFilters;
    protected final AtomicLong gameStatesExamined;
    protected final AtomicLong boardsGenerated;
    protected final AtomicLong movesPrunedTotal;
    protected final Map<Object, AtomicLong> movesPruned;

    protected SearcherBase(final Game<M, B> game) {
        super();
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        this.game = game;
        this.gameFilters = this.game.getFilters();
        this.gameStatesExamined = new AtomicLong();
        this.boardsGenerated = new AtomicLong();
        this.movesPrunedTotal = new AtomicLong();
        final Map<Object, AtomicLong> tempMap = new HashMap<>();
        for (final MoveFilter<M, B> filter : gameFilters) {
            final Object statisticsKey = filter.getStatisticsKey();
            tempMap.put(statisticsKey, new AtomicLong());
        }
        this.movesPruned = Collections.unmodifiableMap(tempMap);
    }

    protected abstract int getQueueSize();

    protected abstract int getWinCount();

    public void printStatistics(final PrintStream out) {
        out.printf(Locale.US,
                "Nodes examined: %,d\n"
                + "Boards generated: %,d\n"
                + "Moves pruned: %,d\n"
                + "Queue size: %,d\n"
                + "Wins: %,d\n",
                gameStatesExamined.get(),
                boardsGenerated.get(),
                movesPrunedTotal.get(),
                getQueueSize(),
                getWinCount());
        for (final MoveFilter<M, B> filter : gameFilters) {
            final Object statisticsKey = filter.getStatisticsKey();
            out.printf(Locale.US,
                    "Moves pruned by filter %s: %,d\n",
                    statisticsKey,
                    movesPruned.get(statisticsKey).get());
        }
        out.flush();
    }
    
    public long getNumberOfGameStatesExamined() {
        return gameStatesExamined.get();
    }
    
    public long getBoardsGenerated() {
        return boardsGenerated.get();
    }

    /**
     * Queues a game tree node for searching.
     * 
     * @param node the game tree node to search
     */
    protected abstract void queueNode(LinkedNode<GameState<M, B>> node);

    /**
     * Captures a sequence of game states that culminates in a win.
     * 
     * @param node the game state history that results in a win
     */
    protected abstract void addWin(List<GameState<M, B>> node);

    /**
     * Examines a single node in the game tree.  This enumerates all possible
     * moves, applies the move filters, and examines unfiltered moves to see if
     * they are a winning game state.  Winning game states are passed to
     * {@link #addWin(List)}.  All other unfiltered game states are passed to
     * {@link #queueNode(LinkedNode)}.
     * 
     * <p>This also captures statistics on the number of nodes examined, boards
     * generated, and moves filtered.  These statistics can be printed using
     * {@link #printStatistics(PrintStream)}.
     * 
     * @param node the node containing the game state to examine
     */
    protected void examineNode(final LinkedNode<GameState<M, B>> node) {
        gameStatesExamined.getAndIncrement();
        final GameState<M, B> gameState = node.getValue();
        final B board = gameState.getBoard();
        final Collection<M> moves = game.findAllMoves(node);
        movesLoop: for (final M move : moves) {
            final B newBoard = move.apply(board);
            boardsGenerated.getAndIncrement();
            final GameState<M, B> newGameState = new GameState<>(move, newBoard);
            final LinkedNode<GameState<M, B>> newNode = new LinkedNode<>(newGameState, node);
            for (final MoveFilter<M, B> filter : gameFilters) {
                if (filter.shouldFilter(newNode)) {
                    final Object statisticsKey = filter.getStatisticsKey();
                    movesPrunedTotal.getAndIncrement();
                    movesPruned.get(statisticsKey).getAndIncrement();
                    continue movesLoop;
                }
            }
            
            if (game.isWin(newNode)) {
                addWin(newNode);
            } else {
                queueNode(newNode);
            }
        }
    }

}
