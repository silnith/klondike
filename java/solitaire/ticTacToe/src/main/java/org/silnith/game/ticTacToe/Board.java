package org.silnith.game.ticTacToe;

import java.io.PrintStream;
import java.util.Locale;

/**
 * A tic-tac-toe board.
 */
public class Board {
	
	private final Player whoseMove;
	
	private final Player[][] board;
	
	/**
	 * Creates a new board where the specified player is next to move.
	 * 
	 * @param whoseMove the next player to move
	 */
	public Board(final Player whoseMove) {
		super();
		this.whoseMove = whoseMove;
		this.board = new Player[3][3];
	}
	
	/**
	 * Creates a new board where the specified player is next to move using
	 * the provided board.
	 * 
	 * @param whoseMove the next player to move
	 * @param board the board to move on
	 */
	public Board(final Player whoseMove, final Player[][] board) {
		super();
		this.whoseMove = whoseMove;
		this.board = board;
	}

	/**
	 * @return the whoseMove
	 */
	public Player getWhoseMove() {
		return whoseMove;
	}

	/**
	 * @return the board
	 */
	public Player[][] getBoard() {
		return board;
	}

	private String to(Player player) {
		if (player == null) {
			return " ";
		} else {
			return player.toString();
		}
	}

    /**
     * Prints an ASCII art representation of the board to the given output stream.
     * 
     * @param out the output stream
     */
	public void printTo(final PrintStream out) {
		out.format(Locale.US, " %s \u2551 %s \u2551 %s ", to(board[0][0]), to(board[0][1]), to(board[0][2]));
		out.println();
		out.println("\u2550\u2550\u2550\u256c\u2550\u2550\u2550\u256c\u2550\u2550\u2550");
		out.format(Locale.US, " %s \u2551 %s \u2551 %s ", to(board[1][0]), to(board[1][1]), to(board[1][2]));
		out.println();
		out.println("\u2550\u2550\u2550\u256c\u2550\u2550\u2550\u256c\u2550\u2550\u2550");
		out.format(Locale.US, " %s \u2551 %s \u2551 %s ", to(board[2][0]), to(board[2][1]), to(board[2][2]));
		out.println();
	}

}
