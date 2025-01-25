using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    public class MoveCapFilter : ISolitaireMoveFilter
    {
        public int MoveCap
        {
            get;
        }

        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        }

        public MoveCapFilter(int moveCap)
        {
            MoveCap = moveCap;
            StatisticsKey = "Move Cap of " + moveCap;
        }

        /// <inheritdoc/>
        public bool ShouldFilter(IReadOnlyList<GameState<ISolitaireMove, Board>> gameStateHistory)
        {
            return gameStateHistory.Count > MoveCap;
        }
    }
}
