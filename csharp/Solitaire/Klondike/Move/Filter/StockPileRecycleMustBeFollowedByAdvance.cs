using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    public class StockPileRecycleMustBeFollowedByAdvance : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "Stock Pile Recycle Must Be Followed By Advance";

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

            if (previousMove.IsStockPileRecycle)
            {
                if (currentMove.IsStockPileModification)
                {
                    /*
                     * This is accepetable, no need to filter.
                     */
                    return false;
                }
                else
                {
                    /*
                     * Why do something not involving the stock pile after recycling it?
                     */
                    return true;
                }
            }
            else
            {
                /*
                 * This filter doesn't apply.
                 */
                return false;
            }
        }
    }
}
