package org.silnith.game.ticTacToe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.search.*;

public class TicTacToe implements Game<Move, Board> {

	private boolean isWinForPlayer(final Player player, final Board board) {
		final Player[][] b = board.getBoard();
		if (b[0][0] == player
				&& b[1][1] == player
				&& b[2][2] == player) {
			return true;
		}
		if (b[0][2] == player
				&& b[1][1] == player
				&& b[2][0] == player) {
			return true;
		}
		for (int i = 0; i < 3; i++) {
			if (b[0][i] == player
					&& b[1][i] == player
					&& b[2][i] == player) {
				return true;
			}
			if (b[i][0] == player
					&& b[i][1] == player
					&& b[i][2] == player) {
				return true;
			}
		}
		return false;
	}

	public boolean isWin(final Board board) {
		/*
		 * A win is defined as player X winning.
		 */
		return isWinForPlayer(Player.X, board);
	}
	
	@Override
	public boolean isWin(final GameState<Move, Board> state) {
		return isWin(state.getBoards().getFirst());
	}

	@Override
	public Collection<Move> findAllMoves(final GameState<Move, Board> state) {
		final Collection<Move> moves = new ArrayList<>(9);
		final Board board = state.getBoards().getFirst();
		final Player[][] b = board.getBoard();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (b[i][j] == null) {
					moves.add(new Move(i, j, board.getWhoseMove()));
				}
			}
		}
		return moves;
	}

	@Override
	public GameState<Move, Board> pruneGameState(final GameState<Move, Board> state) {
		if (isWinForPlayer(Player.O, state.getBoards().getFirst())) {
			// game is a loss, don't bother searching more
			return null;
		}
		return state;
	}

	public static void main(final String[] args) throws InterruptedException, ExecutionException {
		final TicTacToe game = new TicTacToe();
		final Move initialMove = new Move(0, 0, null);
		final Board initialBoard = new Board(Player.X);
		final GameState<Move, Board> initialState = new GameState<>(initialMove, initialBoard);
		
		initialBoard.printTo(System.out);
		
		final ConcurrentLinkedDeque<GameState<Move, Board>> deque = new ConcurrentLinkedDeque<>();
		final ConcurrentLinkedDeque<GameState<Move, Board>> wins = new ConcurrentLinkedDeque<>();
		deque.add(initialState);
		
		final int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.out.format(Locale.US, "Runtime processors: %d", availableProcessors);
		System.out.println();
		
		final SequentialBreadthFirstSearch<Move, Board> searcher = new SequentialBreadthFirstSearch<>(game, initialState);
		final Future<Collection<GameState<Move, Board>>> future = searcher.search();
		
		while (!future.isDone()) {
			searcher.printStatistics(System.out);
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		}
		
		final Collection<GameState<Move, Board>> wins2 = future.get();
		
		System.out.printf(Locale.US, "Found wins: %,d", wins2.size());
		System.out.println();
		for (final GameState<Move, Board> gameState : wins2) {
			//System.out.println(gameState);
			//gameState.getBoards().getFirst().printTo(System.out);
		}
		
		/*
		long nodesExamined = 0;
		long gameStatesPruned = 0;
		GameState<Move, Board> currentGameState = deque.poll();
		outer: while (currentGameState != null) {
			nodesExamined++;
			for (final Move move : game.findAllMoves(currentGameState)) {
				final GameState<Move, Board> gameState = game.pruneGameState(new GameState<>(currentGameState, move));
				if (gameState == null) {
					gameStatesPruned++;
					continue;
				}
				//System.out.println(gameState.getMoves().getFirst());
				//gameState.getBoards().getFirst().printTo(System.out);
				//System.out.println();
				if (game.isWin(gameState.getBoards().getFirst())) {
					wins.add(gameState);
					break outer;
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
		
		System.out.println("Found wins: " + wins.size());
		for (final GameState<Move, Board> gameState : wins) {
			System.out.println(gameState);
			gameState.getBoards().getFirst().printTo(System.out);
		}
		*/
	}

}
