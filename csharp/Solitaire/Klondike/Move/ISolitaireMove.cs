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
        /// If the <see cref="HasCards"/> property is <see langword="true"/>,
        /// this will be the cards that were affected by the move.
        /// </summary>
        /// <remarks>
        /// <para>
        /// This property is undefined if <see cref="HasCards"/> is <see langword="false"/>.
        /// </para>
        /// </remarks>
        public IReadOnlyList<Card> Cards
        {
            get;
        }
    }
}
