package org.silnith.game.solitaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.MoveFilter;
import org.silnith.game.search.SequentialDepthFirstSearch;
import org.silnith.game.search.WorkerThreadDepthFirstSearch;
import org.silnith.game.solitaire.move.ColumnToColumnMove;
import org.silnith.game.solitaire.move.ColumnToFoundationMove;
import org.silnith.game.solitaire.move.DealMove;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileAdvanceMove;
import org.silnith.game.solitaire.move.StockPileRecycleMove;
import org.silnith.game.solitaire.move.StockPileToColumnMove;
import org.silnith.game.solitaire.move.StockPileToFoundationMove;
import org.silnith.game.solitaire.move.filter.BoardCycleFilter;
import org.silnith.game.solitaire.move.filter.DrawFromFoundationMustBeUsefulFilter;
import org.silnith.game.solitaire.move.filter.DrawFromStockPileFilter;
import org.silnith.game.solitaire.move.filter.KingMoveMustExposeFaceDownCardFilter;
import org.silnith.game.solitaire.move.filter.MoveCapFilter;
import org.silnith.game.solitaire.move.filter.RunMoveMustBeFollowedBySomethingUsefulFilter;
import org.silnith.game.solitaire.move.filter.SolitaireMoveFilter;
import org.silnith.game.solitaire.move.filter.StockPileAdvanceMustBeFollowedBySomethingUseful;
import org.silnith.game.solitaire.move.filter.StockPileRecycleMustBeFollowedByAdvance;
import org.silnith.util.LinkedNode;

/**
 * An implementation of Klondike solitaire.
 */
public class Klondike implements Game<SolitaireMove, Board> {

    // RunMove cannot follow stock pile advance or recycle.
    private static final Collection<SolitaireMoveFilter> filters = Arrays.asList(
            new MoveCapFilter(150),
            new KingMoveMustExposeFaceDownCardFilter(),
            new StockPileRecycleMustBeFollowedByAdvance(),
            new StockPileAdvanceMustBeFollowedBySomethingUseful(),
            new DrawFromFoundationMustBeUsefulFilter(),
            new DrawFromStockPileFilter(),
            new RunMoveMustBeFollowedBySomethingUsefulFilter(),
            new BoardCycleFilter());

	/**
	 * The number of columns on the board.
	 * 
	 * <p>This is always {@code 7}.</p>
	 */
	private final int numberOfColumns;
	
	/**
	 * The number of cards to advance the stock pile.
	 * 
	 * <p>This is typically {@code 3}, but could be {@code 1}.</p>
	 * 
	 * ZQTLHB
	 * MYSWTI
	 */
	private final int drawAdvance;
	
	/**
	 * Creates a new game instance for exploring the search space of
	 * Klondike solitaire.
	 */
	public Klondike() {
		super();
		this.numberOfColumns = 7;
		this.drawAdvance = 3;
	}
	
	/**
	 * Returns the number of columns on the board.
	 * 
	 * <p>This is always {@code 7}.</p>
	 * 
	 * @return the number of columns on the board
	 */
	public int getNumberOfColumns() {
		return numberOfColumns;
	}
	
	/**
	 * The number of cards to advance the stock pile.
	 * 
	 * <p>This is typically {@code 3}, but could be {@code 1}.</p>
	 * 
	 * @return the number of cards to advance the stock pile
	 */
	public int getDrawAdvance() {
		return drawAdvance;
	}
	
    /**
     * Returns whether the given board is a winning game state for this game.
     * 
     * @param board the board to check
     * @return {@code true} if the board represents a win
     */
	public boolean isWin(final Board board) {
		for (final Suit suit : Suit.values()) {
			if (board.getFoundation().get(suit).size() < Value.values().length) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isWin(final GameState<SolitaireMove, Board> state) {
		return isWin(state.getBoard());
	}

	/**
	 * Returns all the legal moves for the given board.
	 * 
	 * @param board the board to examine for legal moves
	 * @return all the legal moves
	 */
	public Collection<SolitaireMove> findAllMoves(final Board board) {
		final Collection<SolitaireMove> moves = new ArrayList<>();
		moves.addAll(StockPileRecycleMove.findMoves(board));
		moves.addAll(StockPileAdvanceMove.findMoves(drawAdvance, board));
		moves.addAll(FoundationToColumnMove.findMoves(board));
		moves.addAll(ColumnToColumnMove.findMoves(board));
		moves.addAll(StockPileToColumnMove.findMoves(board));
		moves.addAll(ColumnToFoundationMove.findMoves(board));
		moves.addAll(StockPileToFoundationMove.findMoves(board));
		return moves;
	}

	@Override
	public Collection<SolitaireMove> findAllMoves(final List<GameState<SolitaireMove, Board>> state) {
		return findAllMoves(state.get(0).getBoard());
	}

	@Override
	public Collection<? extends MoveFilter<SolitaireMove, Board>> getFilters() {
		return filters;
	}

	public static void main(final String[] args) throws InterruptedException {
		final List<Card> deck = new ArrayList<>(52);
		for (final Suit suit : Suit.values()) {
			for (final Value value : Value.values()) {
				deck.add(new Card(value, suit));
			}
		}
		Collections.shuffle(deck);
		
		System.out.println(deck);
		
		final Board emptyBoard = new Board(Collections.emptyList(), Collections.emptyList(), 0, Collections.emptyMap());
		final Klondike klondike = new Klondike();
		final DealMove deal = new DealMove(deck, klondike.getNumberOfColumns());
		final Board board = deal.apply(emptyBoard);
		final GameState<SolitaireMove, Board> initialState = new GameState<>(deal, board);
		
		board.printTo(System.out);

		final int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.out.format(Locale.US, "Runtime processors: %d", availableProcessors);
		
		//runSearch0(klondike, initialState);
		sequentialDFS(klondike, initialState);
		//parallelDFS(klondike, initialState, Math.max(availableProcessors - 2, 1));
		//parallelDFS(klondike, initialState, 1);
		
		System.out.println("Finished.");
	}

	private static void runSearch0(final Game<SolitaireMove, Board> game, final GameState<SolitaireMove, Board> initialState) {
		final Deque<LinkedNode<GameState<SolitaireMove, Board>>> deque = new ConcurrentLinkedDeque<>();
		final Deque<List<GameState<SolitaireMove, Board>>> wins = new ConcurrentLinkedDeque<>();
		deque.add(new LinkedNode<>(initialState));
		
		long nodesExamined = 0;
		long gameStatesPruned = 0;
		LinkedNode<GameState<SolitaireMove, Board>> gameStateHistory = deque.poll();
		while (gameStateHistory != null) {
			nodesExamined++;
			final Collection<SolitaireMove> moves = game.findAllMoves(gameStateHistory);
			
			System.out.println();
			System.out.println("Scenario:");
			final List<GameState<SolitaireMove, Board>> history = new ArrayList<>(gameStateHistory);
			Collections.reverse(history);
			for (final GameState<SolitaireMove, Board> gameState : history) {
				System.out.println(gameState.getMove());
			}
			System.out.format(Locale.US, "Moves made: %,d\n", history.size());
			System.out.format(Locale.US, "Game states to examine: %,d\n", deque.size());
			final Board currentBoard = gameStateHistory.getFirst().getBoard();
			currentBoard.printTo(System.out);
			System.out.println("Choices:");
			for (final SolitaireMove move : moves) {
			    final Board newBoard = move.apply(currentBoard);
			    final GameState<SolitaireMove, Board> newGameState = new GameState<>(move, newBoard);
                final LinkedNode<GameState<SolitaireMove, Board>> newHistory = new LinkedNode<>(newGameState, gameStateHistory);
				System.out.print(move);
				for (final SolitaireMoveFilter filter : filters) {
					if (filter.shouldFilter(newHistory)) {
						System.out.print(" (filtered by ");
						System.out.print(filter.getStatisticsKey());
						System.out.print(")");
					}
				}
				System.out.println();
			}
			System.out.println();
			/*
			 * If moving out of foundation, it must be followed by a move onto
			 * the card pulled from the foundation, or by another move out of
			 * the foundation.
			 */
			
			movesLoop: for (final SolitaireMove move : moves) {
                final Board newBoard = move.apply(currentBoard);
                final GameState<SolitaireMove, Board> newGameState = new GameState<>(move, newBoard);
                final LinkedNode<GameState<SolitaireMove, Board>> newHistory = new LinkedNode<>(newGameState, gameStateHistory);
                for (final MoveFilter<SolitaireMove, Board> filter : filters) {
                    if (filter.shouldFilter(newHistory)) {
                        gameStatesPruned++;
                        continue movesLoop;
                    }
                }
                
				if (game.isWin(newGameState)) {
					wins.add(newHistory);
				} else {
					deque.addFirst(newHistory);
				}
			}
			
			gameStateHistory = deque.pollFirst();
			
			if (nodesExamined % 10_000 == 0) {
				System.out.print("Nodes examined: ");
				System.out.println(String.format(Locale.US, "%,d", nodesExamined));
				System.out.print("Nodes pruned: ");
				System.out.println(String.format(Locale.US, "%,d", gameStatesPruned));
				System.out.print("Queue size: ");
				System.out.println(String.format(Locale.US, "%,d", deque.size()));
				System.out.print("Wins: ");
				System.out.println(String.format(Locale.US, "%,d", wins.size()));
				System.out.println();
			}
		}
		
		for (final List<GameState<SolitaireMove, Board>> gameState : wins) {
			System.out.println(gameState);
			gameState.get(0).getBoard().printTo(System.out);
		}
	}

	private static void sequentialDFS(final Game<SolitaireMove, Board> game,
			final GameState<SolitaireMove, Board> initialState) throws InterruptedException {
		final SequentialDepthFirstSearch<SolitaireMove, Board> searcher = new SequentialDepthFirstSearch<>(game, initialState);
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				final Future<Collection<List<GameState<SolitaireMove, Board>>>> future = searcher.search();
				
				final Collection<List<GameState<SolitaireMove, Board>>> wins;
				try {
					wins = future.get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				}
				
				for (final List<GameState<SolitaireMove, Board>> gameState : wins) {
					System.out.println(gameState);
					gameState.get(0).getBoard().printTo(System.out);
				}
			}
		};
		final Thread thread = new Thread(runnable);
		thread.start();
		
		long statesExamined = 0;
		while (thread.isAlive()) {
			searcher.printStatistics(System.out);
			final long nextStatesExamined = searcher.getNumberOfGameStatesExamined();
			final long nodesPerSecond = nextStatesExamined - statesExamined;
			System.out.format(Locale.US, "Nodes per second: %,d\n", nodesPerSecond);
			System.out.println();
			statesExamined = nextStatesExamined;
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		}
		
		thread.join();
		searcher.printStatistics(System.out);
	}

	private static void parallelDFS(final Game<SolitaireMove, Board> game,
			final GameState<SolitaireMove, Board> initialState,
			final int numThreads) throws InterruptedException {
		final WorkerThreadDepthFirstSearch<SolitaireMove, Board> searcher = new WorkerThreadDepthFirstSearch<>(game, initialState, numThreads);
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				final Future<Collection<List<GameState<SolitaireMove, Board>>>> future = searcher.search();
				
				final Collection<List<GameState<SolitaireMove, Board>>> wins;
				try {
					wins = future.get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				}
				
				for (final List<GameState<SolitaireMove, Board>> gameState : wins) {
					System.out.println(gameState);
					gameState.get(0).getBoard().printTo(System.out);
				}
			}
		};
		final Thread thread = new Thread(runnable);
		thread.start();
		
		long statesExamined = 0;
		while (thread.isAlive()) {
			searcher.printStatistics(System.out);
			final long nextStatesExamined = searcher.getNumberOfGameStatesExamined();
			final long nodesPerSecond = nextStatesExamined - statesExamined;
			System.out.format(Locale.US, "Nodes per second: %,d\n", nodesPerSecond);
			System.out.println();
			statesExamined = nextStatesExamined;
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		}
        
        thread.join();
        searcher.printStatistics(System.out);
	}

}
