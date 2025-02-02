using Silnith.Game.Deck;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// If a run is moved from one column to another, one of two conditions must hold true.
    /// Either the entire run is moved, exposing a new face-down card (or emptying a column),
    /// or the subsequent move must involve the existing face-up card exposed by moving the run.
    /// </summary>
    public class RunMoveMustBeFollowedBySomethingUsefulFilter : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "Run Move Must Be Useful";

        /// <inheritdoc/>
        public bool ShouldFilter(IReadOnlyList<GameState<ISolitaireMove, Board>> gameStateHistory)
        {
            IEnumerator<GameState<ISolitaireMove, Board>> enumerator = gameStateHistory.GetEnumerator();
            _ = enumerator.MoveNext();
            GameState<ISolitaireMove, Board> currentGameState = enumerator.Current;
            ISolitaireMove currentMove = currentGameState.Move;

            if (!enumerator.MoveNext())
            {
                /*
                 * This can only happen at the very beginning of the game.
                 * In that case, this filter is not helpful, so just let everything pass.
                 */
                return false;
            }

            GameState<ISolitaireMove, Board> previousGameState = enumerator.Current;
            ISolitaireMove previousMove = previousGameState.Move;
            Board previousBoard = previousGameState.Board;
            /*
             * This is the board AFTER the move has been applied!
             */

            if (previousMove.IsFromColumn)
            {
                // Check whether the previous run move used u pall the cards.
                // If it did, everything is fine.

                if (!enumerator.MoveNext())
                {
                    /*
                     * This can only happen at the very beginning of the game.
                     * In that case, this filter is not helpful, so just let everything pass.
                     */
                    return false;
                }
                GameState<ISolitaireMove, Board> gameStateTwoStepsBack = enumerator.Current;
                Board boardTwoStepsBack = gameStateTwoStepsBack.Board;

                /*
                 * Check whether the previous move took all the available cards from the source column.
                 */
                int sourceColumnIndex = previousMove.FromColumnIndex;
                int numberOfMovedCards = previousMove.Cards.Count;
                if (numberOfMovedCards == boardTwoStepsBack.Columns[sourceColumnIndex].FaceUp.Count)
                {
                    /*
                     * The previous move took all the face-up cards from the source column.
                     * Therefore, it is not suspicious.
                     * (Shenanigans with the king are handled by other filters.)
                     */
                    return false;
                }
                else
                {
                    /*
                     * The previous move took only a portion of the available run.
                     * Therefore, we want to make sure the top-most card NOT taken
                     * is involved with the current move.  Otherwise, the previous
                     * move has no value.
                     */
                    Card cardExposedByRunMove = previousBoard.Columns[sourceColumnIndex].GetTopCard();
                    List<Card> singletonList = new List<Card>()
                    {
                        cardExposedByRunMove,
                    };
                    if (singletonList.SequenceEqual(currentMove.Cards))
                    {
                        // This move uses the exposed card, meaning the previous run move is allowed.
                        // This makes sure the move uses ONLY the exposed card, not as one of a run.
                        // TODO: Should the only allowed follow-up move be column to foundation?
                        return false;
                    }
                    else
                    {
                        /*
                         * This move does not use the exposed card, so filter the sequence of moves.
                         */
                        return true;
                    }
                }
            }
            else
            {
                /*
                 * Not interested in any scenario except where the second-most-recent move is moving a run from one column to another.
                 */
                return false;
            }
        }
    }
}
