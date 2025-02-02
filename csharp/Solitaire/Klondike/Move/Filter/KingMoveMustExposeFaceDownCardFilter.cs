using Silnith.Game.Deck;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// If a run starting with a king is moved to a new column, it must be
    /// for the purpose of exposing a face-down card.
    /// </summary>
    public class KingMoveMustExposeFaceDownCardFilter : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "King Move Must Expose Card";

        /// <inheritdoc/>
        public bool ShouldFilter(IReadOnlyList<GameState<ISolitaireMove, Board>> gameStateHistory)
        {
            GameState<ISolitaireMove, Board> currentGameState = gameStateHistory[0];
            ISolitaireMove currentMove = currentGameState.Move;
            Board currentBoard = currentGameState.Board;
            /*
             * The current board is the state after the move has been applied.
             */

            if (currentMove.IsFromColumn && currentMove.IsToColumn)
            {
                IReadOnlyList<Card> run = currentMove.Cards;
                Card firstCard = run[0];
                if (firstCard.Value == Value.King)
                {
                    /*
                     * This is the column after the move.
                     */
                    Column sourceColumn = currentBoard.Columns[currentMove.FromColumnIndex];
                    if (sourceColumn.HasFaceUpCards())
                    {
                        /*
                         * Something was left behind, therefore the move has value.
                         */
                        return false;
                    }
                    else
                    {
                        /*
                         * The king was moved and now the column is empty.
                         * Since we know the destination must have been an empty column,
                         * we moved from an empty column to an empty column.
                         * Therefore, the move has no purpose.
                         */
                        return true;
                    }
                }
                else
                {
                    /*
                     * This filter only cares about runs that start with a king.
                     */
                    return false;
                }
            }
            else
            {
                /*
                 * Only interested in moves from a column to another column
                 * where the run begins with a king.
                 */
                return false;
            } 
        }
    }
}
