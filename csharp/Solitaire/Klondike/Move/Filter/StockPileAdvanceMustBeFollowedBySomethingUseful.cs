using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    public class StockPileAdvanceMustBeFollowedBySomethingUseful : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "Stock Pile Draw Must Follow Advance";

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

            if (previousMove.IsStockPileAdvance)
            {
                if (currentMove.IsStockPileModification
                    || currentMove.IsFromStockPile
                    || currentMove.IsFromFoundation)
                {
                    // This is fine.
                    return false;
                }
                else
                {
                    // No need to advance the stock pile simply to do something unrelated to it.
                    return true;
                }
            }
            else
            {
                // Don't care.
                return false;
            }
        }
    }
}
