using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// Filters moves that introduce a cycle into the board history.
    /// </summary>
    public class BoardCycleFilter : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "Board Cycle";

        /// <inheritdoc/>
        public bool ShouldFilter(IReadOnlyList<GameState<ISolitaireMove, Board>> gameStateHistory)
        {
            IEnumerator<GameState<ISolitaireMove, Board>> enumerator = gameStateHistory.GetEnumerator();
            _ = enumerator.MoveNext();
            GameState<ISolitaireMove, Board> currentGameState = enumerator.Current;
            Board currentBoard = currentGameState.Board;

            while (enumerator.MoveNext())
            {
                GameState<ISolitaireMove, Board> gameState = enumerator.Current;
                if (currentBoard.Equals(gameState.Board))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
