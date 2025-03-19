package org.silnith.game.ticTacToe;

/**
 * A tic-tac-toe move.
 */
public class Move implements org.silnith.game.move.Move<Board> {

	private final int row;
	private final int column;
	private final Player player;

	/**
	 * Creates a new move.
	 * 
	 * @param row the row
	 * @param column the column
	 * @param player the player that moved
	 */
	public Move(final int row, final int column, final Player player) {
		this.row = row;
		this.column = column;
		this.player = player;
	}

	private static Player[][] copyBoard(final Player[][] source) {
		final Player[][] copy = new Player[3][3];
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 3; column++) {
				copy[row][column] = source[row][column];
			}
		}
		return copy;
	}

	@Override
	public Board apply(final Board board) {
		final Player[][] copy = copyBoard(board.getBoard());
		copy[row][column] = player;
		return new Board(player.getOther(), copy);
	}

	@Override
	public String toString() {
		return "Move [row=" + row + ", column=" + column + ", player=" + player + "]";
	}

}