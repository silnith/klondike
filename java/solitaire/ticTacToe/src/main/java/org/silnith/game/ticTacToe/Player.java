package org.silnith.game.ticTacToe;

public enum Player {
	X,
	O;

	public Player getOther() {
		switch (this) {
		case X:
			return O;
		case O:
			return X;
		default:
			throw new IllegalArgumentException();
		}
	}
}
