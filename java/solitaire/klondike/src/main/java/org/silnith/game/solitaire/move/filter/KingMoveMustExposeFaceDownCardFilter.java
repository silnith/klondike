package org.silnith.game.solitaire.move.filter;

import java.util.List;

import org.silnith.deck.Card;
import org.silnith.deck.Value;
import org.silnith.game.GameState;
import org.silnith.game.solitaire.Board;
import org.silnith.game.solitaire.Column;
import org.silnith.game.solitaire.move.RunMove;
import org.silnith.game.solitaire.move.SolitaireMove;

/**
 * If a run starting with a king is moved to a new column, it must be
 * for the purpose of exposing a face-down card.
 */
public class KingMoveMustExposeFaceDownCardFilter implements SolitaireMoveFilter {

	@Override
	public boolean test(final GameState<SolitaireMove, Board> state) {
		final SolitaireMove currentMove = state.getMoves().getFirst();
		final Board currentBoard = state.getBoards().getFirst();
		
		assert currentMove != null;
		assert currentBoard != null;
		
		if (currentMove instanceof RunMove) {
			final RunMove runMove = (RunMove) currentMove;
			final List<Card> run = runMove.getCards();
			final Card firstCard = run.get(0);
			if (firstCard.getValue() == Value.KING) {
				// This is the column after the move.
				final Column sourceColumn = currentBoard.getColumns().get(runMove.getSourceColumn());
				if (sourceColumn.hasFaceUpCards()) {
					return false;
				} else {
					/*
					 * The king was moved and now the column is empty.
					 * Since we know the destination must have been an empty column,
					 * we moved from an empty column to an empty column.
					 * Therefore, the move has no purpose.
					 */
					return true;
				}
			} else {
				// This filter only cares about runs that start with a king.
				return false;
			}
		} else {
			// Not interested in other types of moves.
			return false;
		}
	}

}
