using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move.Filter
{
    /// <summary>
    /// A move filter that caps the total game length.
    /// </summary>
    /// <remarks>
    /// <para>
    /// Realistically there is a finite limit to the number of moves it takes
    /// to win a game of Klondike solitaire.  So searching a tree to a depth
    /// greater than that number is unproductive.
    /// </para>
    /// </remarks>
    public class MoveCapFilter : ISolitaireMoveFilter
    {
        /// <summary>
        /// The maximum number of moves to allow in a game.
        /// </summary>
        public int MoveCap
        {
            get;
        }

        /// <inheritdoc/>
        public object StatisticsKey
        {
            get;
        }

        /// <summary>
        /// Constructs a new move cap filter with the given cap.
        /// </summary>
        /// <param name="moveCap">The maximum number of moves to allow in a game.</param>
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
