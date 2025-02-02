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
                /*
                 * This can only happen at the very beginning of the game.
                 * In that case, this filter is not helpful, so just let everything pass.
                 */
                return false;
            }

            GameState<ISolitaireMove, Board> previousGameState = enumerator.Current;
            ISolitaireMove previousMove = previousGameState.Move;

            if (previousMove.IsFromFoundation && previousMove.IsToColumn)
            {
                if (currentMove.IsFromFoundation)
                {
                    /*
                     * Chained moves from the foundation could be for a purpose,
                     * so allow the chain to unfold.
                     */
                    return false;
                }
                if (currentMove.AddsToColumn(previousMove.DestinationColumnIndex))
                {
                    /*
                     * The current move puts a card on top of the card taken
                     * from the foundation, so the foundation move has value.
                     */
                    return false;
                }
                else
                {
                    /*
                     * The current move does not make use of the card taken
                     * from the foundation, so the foundation move was worthless.
                     */
                    return true;
                }
            }
            else
            {
                /*
                 * This filter only cares about moves that take cards from the foundation.
                 */
                return false;
            }
        }
    }
}
