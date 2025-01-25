using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// If a card is drawn from the foundation, the following move must make use
    /// of the card drawn.  Specifically, something must be put on top of it.
    /// </summary>
    public class DrawFromFoundationMustBeUsefulFilter : ISolitaireMoveFilter
    {
        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        } = "Draw From Foundation Must Be Used";

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

            if (previousMove is FoundationToColumnMove foundationToColumnMove)
            {
                int destinationColumn = foundationToColumnMove.DestinationColumn;
                // if current move adds cards to the same column, this is valid.
                // otherwise, filter
                if (currentMove.AddsCardsToColumn(destinationColumn))
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
