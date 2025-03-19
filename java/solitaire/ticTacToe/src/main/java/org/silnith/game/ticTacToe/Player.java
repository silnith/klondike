package org.silnith.game.ticTacToe;

/**
 * The players for tic-tac-toe.
 */
public enum Player {
	/**
	 * The first player.
	 */
	X,
	/**
	 * The second player.
	 */
	O;

	/**
	 * Returns the player that moves after the current player.
	 * 
	 * @return the other player
	 */
	public Player getOther() {
		switch (this) {
		case X:
			return O;
		case O:
			return X;
		default:
		    assert false : "Unknown player: " + this;
			throw new IllegalArgumentException();
		}
	}
}
