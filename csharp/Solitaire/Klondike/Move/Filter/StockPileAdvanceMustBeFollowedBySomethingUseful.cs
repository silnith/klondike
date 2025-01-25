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
                return false;
            }

            GameState<ISolitaireMove, Board> previousGameState = enumerator.Current;
            ISolitaireMove previousMove = previousGameState.Move;

            if (previousMove is StockPileAdvanceMove)
            {
                if (currentMove is StockPileAdvanceMove
                    || currentMove is StockPileRecycleMove
                    || currentMove is StockPileToColumnMove
                    || currentMove is StockPileToFoundationMove
                    || currentMove is FoundationToColumnMove)
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
