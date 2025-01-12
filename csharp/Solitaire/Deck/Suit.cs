using System;

namespace Silnith.Game.Deck
{
    /// <summary>
    /// A suit for a playing card.
    /// </summary>
    public enum Suit
    {
        /// <summary>
        /// &#x2663;
        /// </summary>
        Club,
        /// <summary>
        /// &#x2666;
        /// </summary>
        Diamond,
        /// <summary>
        /// &#x2665;
        /// </summary>
        Heart,
        /// <summary>
        /// &#x2660;
        /// </summary>
        Spade
    }

    /// <summary>
    /// Extension methods for <see cref="Suit"/>.
    /// </summary>
    public static class SuitExtensions
    {
        /// <summary>
        /// Returns the color of a particular suit.
        /// </summary>
        /// <param name="suit">The suit.</param>
        /// <returns>The color of the suit.</returns>
        public static Color GetColor(this Suit suit)
        {
            return suit switch
            {
                Suit.Club => Color.Black,
                Suit.Diamond => Color.Red,
                Suit.Heart => Color.Red,
                Suit.Spade => Color.Black,
                _ => throw new ArgumentException("Unknown Suit", nameof(suit)),
            };
        }

        /// <summary>
        /// Returns the Unicode symbol for the card suit.
        /// </summary>
        /// <remarks>
        /// <list type="table">
        ///   <listheader>
        ///     <term>Suit</term>
        ///     <term>Symbol</term>
        ///   </listheader>
        ///   <item>
        ///     <term><see cref="Suit.Club"/></term>
        ///     <term>&#x2663;</term>
        ///   </item>
        ///   <item>
        ///     <term><see cref="Suit.Diamond"/></term>
        ///     <term>&#x2666;</term>
        ///   </item>
        ///   <item>
        ///     <term><see cref="Suit.Heart"/></term>
        ///     <term>&#x2665;</term>
        ///   </item>
        ///   <item>
        ///     <term><see cref="Suit.Spade"/></term>
        ///     <term>&#x2660;</term>
        ///   </item>
        /// </list>
        /// </remarks>
        /// <param name="suit">The suit.</param>
        /// <returns>A single Unicode glyph.</returns>
        public static string ToSymbol(this Suit suit)
        {
            return suit switch
            {
                Suit.Club => "\u2663",
                Suit.Diamond => "\u2666",
                Suit.Heart => "\u2665",
                Suit.Spade => "\u2660",
                _ => throw new ArgumentException("Unknown Suit", nameof(suit)),
            };
        }
    }
}
