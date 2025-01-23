using Silnith.Game.Deck;
using System.Collections.Generic;

namespace Silnith.Game.Klondike.Move
{
    /// <summary>
    /// The common interface for moves in the game Klondike solitaire.
    /// </summary>
    public interface ISolitaireMove : IMove<Board>
    {
        /// <summary>
        /// Whether the move involves one or more cards.
        /// </summary>
        /// <remarks>
        /// <para>
        /// Some moves in the game do not result in any cards changing position.
        /// These moves will have this property set to <see langword="false"/>.
        /// </para>
        /// </remarks>
        bool HasCards
        {
            get;
        }

        /// <summary>
        /// The cards affected by this move.  If this move does not affect any cards,
        /// this will be empty.
        /// </summary>
        public IReadOnlyList<Card> Cards
        {
            get;
        }
    }
}
