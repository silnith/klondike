package org.silnith.game.ticTacToe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.silnith.game.Game;
import org.silnith.game.GameState;
import org.silnith.game.move.MoveFilter;

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
		return isWin(state.getBoard());
	}

	@Override
    public Collection<Move> findAllMoves(final List<GameState<Move, Board>> gameState) {
        final Collection<Move> moves = new ArrayList<>(9);
        final Board board = gameState.get(0).getBoard();
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

	/**
	 * If player {@code O} wins the game, then by definition player {@code X} loses.
	 */
	private class GameLostFilter implements MoveFilter<Move, Board> {

		@Override
        public Object getStatisticsKey() {
            return "Game Lost";
        }

        @Override
		public boolean shouldFilter(final List<GameState<Move, Board>> state) {
			return isWinForPlayer(Player.O, state.get(0).getBoard());
		}

	}

	@Override
    public Collection<? extends MoveFilter<Move, Board>> getFilters() {
        return Collections.singleton(new GameLostFilter());
    }

}
