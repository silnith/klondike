using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// Filters draw from stock pile moves if they do not follow a stock pile advance or recycle.
    /// </summary>
    public class DrawFromStockPileFilter : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "Draw From Stock Pile Must Follow Advance";

        /// <inheritdoc/>
        public bool ShouldFilter(IReadOnlyList<GameState<ISolitaireMove, Board>> gameStateHistory)
        {
            IEnumerator<GameState<ISolitaireMove, Board>> enumerator = gameStateHistory.GetEnumerator();
            _ = enumerator.MoveNext();
            GameState<ISolitaireMove, Board> currentGameState = enumerator.Current;
            ISolitaireMove currentMove = currentGameState.Move;

            if (currentMove is StockPileToColumnMove || currentMove is StockPileToFoundationMove)
            {
                // TODO: Split out the logic between "to column" and "to foundation"!
                // This is because one requires the special case for pulling from the foundation, the other does not.
                // continue
            }
            else
            {
                // This filter does not apply.
                return false;
            }

            if (!enumerator.MoveNext())
            {
                return false;
            }

            GameState<ISolitaireMove, Board> previousGameState = enumerator.Current;
            ISolitaireMove previousMove = previousGameState.Move;
            // There may be a sequence of draws from the stock pile.
            // Moving from the foundation to a column is a special case that we allow between stock pile advances and draws.
            while (previousMove is StockPileToColumnMove
                || previousMove is StockPileToFoundationMove
                || previousMove is FoundationToColumnMove)
            {
                _ = enumerator.MoveNext();
                previousGameState = enumerator.Current;
                previousMove = previousGameState.Move;
            }
            // Theoretically, it should only be possible for the previous move to be a stock pile advance.
            // The recycle should make it impossible to draw from the stock pile.
            if (previousMove is StockPileAdvanceMove)
            {
                // This is acceptable, no need to filter.
                return false;
            }
            else
            {
                // The previous move did not modify the stock pile, so drawing from the stock pile is silly.
                return true;
            }
        }
    }
}
