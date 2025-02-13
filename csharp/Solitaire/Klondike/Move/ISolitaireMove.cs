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
        /// The cards affected by this move.  If this move does not affect any cards,
        /// this will be empty.
        /// </summary>
        IReadOnlyList<Card> Cards
        {
            get;
        }

        /// <summary>
        /// The index of the column from which this move takes cards.
        /// </summary>
        /// <exception cref="System.ArgumentException">If <see cref="HasCards"/> or <see cref="IsFromColumn"/> are <see langword="false"/>.</exception>
        int SourceColumnIndex
        {
            get;
        }

        /// <summary>
        /// The index of the column to which this move adds cards.
        /// </summary>
        /// <exception cref="System.ArgumentException">If either <see cref="HasCards"/> or <see cref="IsToColumn"/> is <see langword="false"/>.</exception>
        int DestinationColumnIndex
        {
            get;
        }

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
        /// <see langword="true"/> if this move changes the stock pile index.
        /// Moves that advance or recycle the stock pile will be <see langword="true"/>.
        /// Drawing from the stock pile also adjusts the index, so will also
        /// be <see langword="true"/>.
        /// </summary>
        bool IsStockPileModification
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move advances the stock pile.
        /// </summary>
        bool IsStockPileAdvance
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move recycles the stock pile.
        /// </summary>
        bool IsStockPileRecycle
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move draws a card from the stock pile.
        /// </summary>
        bool IsFromStockPile
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move takes a card from the foundation.
        /// </summary>
        bool IsFromFoundation
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move takes cards from a column.
        /// </summary>
        bool IsFromColumn
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move puts a card into the foundation.
        /// </summary>
        bool IsToFoundation
        {
            get;
        }

        /// <summary>
        /// <see langword="true"/> if this move puts cards onto a column run.
        /// </summary>
        bool IsToColumn
        {
            get;
        }

        /// <summary>
        /// Returns <see langword="true"/> if this move takes cards from the specific column.
        /// </summary>
        /// <param name="columnIndex">The column index to check whether this move takes cards from.</param>
        /// <returns><see langword="true"/> if this move takes cards from the specific column.</returns>
        bool TakesFromColumn(int columnIndex);

        /// <summary>
        /// Returns <see langword="true"/> if this move puts cards onto the specific column.
        /// </summary>
        /// <param name="columnIndex">The column to check whether receives cards.</param>
        /// <returns><see langword="true"/> if thos move puts cards onto the specific column.</returns>
        bool AddsToColumn(int columnIndex);
    }
}
