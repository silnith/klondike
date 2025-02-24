package org.silnith.game.solitaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.deck.Value;
import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.MoveFilter;
import org.silnith.game.search.GameTreeSearcher;
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
import org.silnith.game.solitaire.move.filter.StockPileAdvanceMustBeFollowedBySomethingUsefulFilter;
import org.silnith.game.solitaire.move.filter.StockPileRecycleMustBeFollowedByAdvanceFilter;
import org.silnith.util.LinkedNode;

/**
 * An implementation of Klondike solitaire.
 */
public class Klondike implements Game<SolitaireMove, Board> {

    // RunMove cannot follow stock pile advance or recycle.
    private static final Collection<SolitaireMoveFilter> filters = Arrays.asList(
            new MoveCapFilter(150),
            new KingMoveMustExposeFaceDownCardFilter(),
            new StockPileRecycleMustBeFollowedByAdvanceFilter(),
            new StockPileAdvanceMustBeFollowedBySomethingUsefulFilter(),
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

	public boolean isWin(final GameState<SolitaireMove, Board> state) {
		return isWin(state.getBoard());
	}

	@Override
    public boolean isWin(final List<GameState<SolitaireMove, Board>> gameStates) {
        return isWin(gameStates.get(0));
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

	public static void main(final String[] args) throws Exception {
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
		System.out.format(Locale.US, "Runtime processors: %d\n", availableProcessors);
		
		//runSearch0(klondike, initialState);
		sequentialDFS(klondike, initialState);
		//parallelDFS(klondike, initialState, Math.max(availableProcessors - 2, 1));
		//parallelDFS(klondike, initialState, 1);
		
		System.out.println("Finished.");
	}

	@SuppressWarnings("unused")
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
			final Board currentBoard = gameStateHistory.getValue().getBoard();
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
                
				if (game.isWin(newHistory)) {
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

    @SuppressWarnings("unused")
	private static void sequentialDFS(final Game<SolitaireMove, Board> game,
			final GameState<SolitaireMove, Board> initialState) throws Exception {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
		try (final GameTreeSearcher<SolitaireMove, Board> searcher = new SequentialDepthFirstSearch<>(game, initialState)) {
		    final Future<Collection<List<GameState<SolitaireMove, Board>>>> future = executor.submit(searcher);
		    
            long statesExamined = 0;
            long boardsGenerated = 0;
            while (!future.isDone()) {
            	searcher.printStatistics(System.out);
            	final long nextStatesExamined = searcher.getNumberOfGameStatesExamined();
            	final long nextBoardsGenerated = searcher.getBoardsGenerated();
            	final long nodesPerSecond = nextStatesExamined - statesExamined;
            	final long boardsPerSecond = nextBoardsGenerated - boardsGenerated;
            	System.out.format(Locale.US, "Nodes per second: %,d\n", nodesPerSecond);
            	System.out.format(Locale.US, "Boards per second: %,d\n", boardsPerSecond);
            	System.out.println();
            	statesExamined = nextStatesExamined;
            	boardsGenerated = nextBoardsGenerated;
            	Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }
            
            final Collection<List<GameState<SolitaireMove, Board>>> wins = future.get();
            
            for (final List<GameState<SolitaireMove, Board>> gameState : wins) {
                System.out.println(gameState);
                gameState.get(0).getBoard().printTo(System.out);
            }
            
            searcher.printStatistics(System.out);
        }
		executor.shutdown();
	}

	@SuppressWarnings("unused")
    private static void parallelDFS(final Game<SolitaireMove, Board> game,
			final GameState<SolitaireMove, Board> initialState,
			final int numThreads) throws Exception {
	    final ExecutorService executor = Executors.newSingleThreadExecutor();
		try (final GameTreeSearcher<SolitaireMove, Board> searcher = new WorkerThreadDepthFirstSearch<>(game, initialState, numThreads);) {
    		final Future<Collection<List<GameState<SolitaireMove, Board>>>> future = executor.submit(searcher);

            long statesExamined = 0;
            long boardsGenerated = 0;
    		while (!future.isDone()) {
                searcher.printStatistics(System.out);
                final long nextStatesExamined = searcher.getNumberOfGameStatesExamined();
                final long nextBoardsGenerated = searcher.getBoardsGenerated();
                final long nodesPerSecond = nextStatesExamined - statesExamined;
                final long boardsPerSecond = nextBoardsGenerated - boardsGenerated;
                System.out.format(Locale.US, "Nodes per second: %,d\n", nodesPerSecond);
                System.out.format(Locale.US, "Boards per second: %,d\n", boardsPerSecond);
                System.out.println();
                statesExamined = nextStatesExamined;
                boardsGenerated = nextBoardsGenerated;
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    		}
    		
    		final Collection<List<GameState<SolitaireMove, Board>>> wins = future.get();
            
            for (final List<GameState<SolitaireMove, Board>> gameState : wins) {
                System.out.println(gameState);
                gameState.get(0).getBoard().printTo(System.out);
            }
            
            searcher.printStatistics(System.out);
		}
        executor.shutdown();
	}

    /**
     * A fake move that simply produces a pre-arranged board.
     */
    private static class PrearrangedMove implements SolitaireMove {

        private final Board board;

        public PrearrangedMove(final Board board) {
            super();
            this.board = board;
        }

        @Override
        public Board apply(final Board board) {
            return this.board;
        }

        @Override
        public boolean hasCards() {
            return false;
        }

        @Override
        public List<Card> getCards() {
            return Collections.emptyList();
        }

        @Override
        public boolean isStockPileModification() {
            return false;
        }

        @Override
        public boolean isStockPileAdvance() {
            return false;
        }

        @Override
        public boolean isStockPileRecycle() {
            return false;
        }

        @Override
        public boolean isFromStockPile() {
            return false;
        }

        @Override
        public boolean isFromFoundation() {
            return false;
        }

        @Override
        public boolean isFromColumn() {
            return false;
        }

        @Override
        public boolean isFromColumn(int columnIndex) {
            return false;
        }

        @Override
        public int getSourceColumnIndex() {
            return 0;
        }

        @Override
        public boolean isToFoundation() {
            return false;
        }

        @Override
        public boolean isToColumn() {
            return false;
        }

        @Override
        public boolean isToColumn(int columnIndex) {
            return false;
        }

        @Override
        public int getDestinationColumnIndex() {
            return 0;
        }

    }
    
    private static Board makeBoardThatNeedsCardsWalkedToFoundation() {
        final List<Column> columns = Arrays.asList(
                new Column(null, Arrays.asList()),
                new Column(null, Arrays.asList()),
                new Column(null, Arrays.asList()),
                new Column(null, Arrays.asList()),
                new Column(null, Arrays.asList()),
                new Column(null, Arrays.asList()),
                new Column(null, Arrays.asList()));
        final List<Card> stockPile = Arrays.asList();
        final int stockPileIndex = 0;
        final Map<Suit, List<Card>> foundation = new EnumMap<Suit, List<Card>>(Suit.class);
        foundation.put(Suit.CLUB, Arrays.asList());
        foundation.put(Suit.DIAMOND, Arrays.asList());
        foundation.put(Suit.HEART, Arrays.asList());
        foundation.put(Suit.SPADE, Arrays.asList());
        return new Board(columns, stockPile, stockPileIndex, foundation);
    }

}
