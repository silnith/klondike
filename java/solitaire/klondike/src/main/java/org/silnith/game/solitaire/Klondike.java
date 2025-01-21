package org.silnith.game.solitaire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.silnith.game.search.SequentialDepthFirstSearch;
import org.silnith.game.search.WorkerThreadBreadthFirstSearch;
import org.silnith.game.search.WorkerThreadDepthFirstSearch;
import org.silnith.game.solitaire.move.AdvanceStockPileMove;
import org.silnith.game.solitaire.move.ColumnToFoundationMove;
import org.silnith.game.solitaire.move.DealMove;
import org.silnith.game.solitaire.move.FoundationToColumnMove;
import org.silnith.game.solitaire.move.RecycleStockPileMove;
import org.silnith.game.solitaire.move.RunMove;
import org.silnith.game.solitaire.move.SolitaireMove;
import org.silnith.game.solitaire.move.StockPileToColumnMove;
import org.silnith.game.solitaire.move.StockPileToFoundationMove;
import org.silnith.util.LinkedNode;

/**
 * An implementation of Klondike solitaire.
 */
public class Klondike implements Game<SolitaireMove, Board> {
	
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
		return isWin(state.getBoards().getFirst());
	}

	/**
	 * Returns all the legal moves for the given board.
	 * 
	 * @param board the board to examine for legal moves
	 * @return all the legal moves
	 */
	public Collection<SolitaireMove> findAllMoves(final Board board) {
		final Collection<SolitaireMove> moves = new ArrayList<>();
		moves.addAll(StockPileToFoundationMove.findMoves(board));
		moves.addAll(StockPileToColumnMove.findMoves(board));
		moves.addAll(ColumnToFoundationMove.findMoves(board));
		moves.addAll(FoundationToColumnMove.findMoves(board));
		moves.addAll(RunMove.findMoves(board));
		moves.addAll(AdvanceStockPileMove.findMoves(drawAdvance, board));
		moves.addAll(RecycleStockPileMove.findMoves(board));
		return moves;
	}

	@Override
	public Collection<SolitaireMove> findAllMoves(final GameState<SolitaireMove, Board> state) {
		return findAllMoves(state.getBoards().get(0));
	}

	@Override
	public GameState<SolitaireMove, Board> pruneGameState(final GameState<SolitaireMove, Board> state) {
		final SolitaireMove currentMove = state.getMoves().getFirst();
		final LinkedNode<SolitaireMove> moveHistory = state.getMoves().getNext();
		final Board currentBoard = state.getBoards().getFirst();
		final LinkedNode<Board> boardHistory = state.getBoards().getNext();
		
		if (moveHistory == null || boardHistory == null) {
			// No past history to check, allow the search.
			return state;
		}

		if (boardHistory.size() > 150) {
			return null;
		}
		
		if (boardHistory.contains(currentBoard)) {
			// The move introduces a cycle into the search tree.
			return null;
		}
		
		final SolitaireMove previousMove = moveHistory.getFirst();
		@SuppressWarnings("unused")
		final Board previousBoard = boardHistory.getFirst();
		
		if (currentMove.hasCards() && previousMove.hasCards()
				&& currentMove.getCards().equals(previousMove.getCards())) {
			/*
			 * If two consecutive moves are moving the same stack of cards,
			 * they are redundant and the search tree should be pruned.
			 */
			return null;
		}
		
		return state;
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
		
		//sequentialDFS(klondike, initialState);
		parallelDFS(klondike, initialState, Math.max(availableProcessors - 2, 1));
		//parallelDFS(klondike, initialState, 1);
	}

	private static void runSearch0(final Game<SolitaireMove, Board> game, final GameState<SolitaireMove, Board> initialState) {
		final ConcurrentLinkedDeque<GameState<SolitaireMove, Board>> deque = new ConcurrentLinkedDeque<>();
		final ConcurrentLinkedDeque<GameState<SolitaireMove, Board>> wins = new ConcurrentLinkedDeque<>();
		deque.add(initialState);
		
		long nodesExamined = 0;
		long gameStatesPruned = 0;
		GameState<SolitaireMove, Board> currentGameState = deque.poll();
		while (currentGameState != null) {
			nodesExamined++;
			for (final SolitaireMove move : game.findAllMoves(currentGameState)) {
				final GameState<SolitaireMove, Board> gameState = game.pruneGameState(new GameState<>(currentGameState, move));
				if (gameState == null) {
					gameStatesPruned++;
					continue;
				}
				//System.out.println(gameState.getMoves().getFirst());
				//gameState.getBoards().getFirst().printTo(System.out);
				//System.out.println();
				if (game.isWin(gameState)) {
					wins.add(gameState);
				} else {
					deque.addFirst(gameState);
				}
			}
			
			currentGameState = deque.poll();
			
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
		
		for (final GameState<SolitaireMove, Board> gameState : wins) {
			System.out.println(gameState);
			gameState.getBoards().getFirst().printTo(System.out);
		}
	}

	private static void sequentialDFS(final Game<SolitaireMove, Board> game,
			final GameState<SolitaireMove, Board> initialState) throws InterruptedException {
		final SequentialDepthFirstSearch<SolitaireMove, Board> searcher = new SequentialDepthFirstSearch<>(game, initialState);
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				final Future<Collection<GameState<SolitaireMove, Board>>> future = searcher.search();
				
				final Collection<GameState<SolitaireMove, Board>> wins;
				try {
					wins = future.get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				}
				
				for (final GameState<SolitaireMove, Board> gameState : wins) {
					System.out.println(gameState);
					gameState.getBoards().getFirst().printTo(System.out);
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
	}

	private static void parallelDFS(final Game<SolitaireMove, Board> game,
			final GameState<SolitaireMove, Board> initialState,
			final int numThreads) throws InterruptedException {
		final WorkerThreadDepthFirstSearch<SolitaireMove, Board> searcher = new WorkerThreadDepthFirstSearch<>(game, initialState, numThreads);
		final Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				final Future<Collection<GameState<SolitaireMove, Board>>> future = searcher.search();
				
				final Collection<GameState<SolitaireMove, Board>> wins;
				try {
					wins = future.get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				}
				
				for (final GameState<SolitaireMove, Board> gameState : wins) {
					System.out.println(gameState);
					gameState.getBoards().getFirst().printTo(System.out);
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
	}

}
