using System;

namespace Silnith.Game.Deck
{
    /// <summary>
    /// A numeric value for a playing card.
    /// </summary>
    public enum Value
    {
        /// <summary>
        /// The ace.  Usually the lowest card, but in some games is also the highest card.
        /// Has a numeric value of <c>1</c>.
        /// </summary>
        Ace = 1,
        /// <summary>
        /// The value <c>2</c>.
        /// </summary>
        Two,
        /// <summary>
        /// The value <c>3</c>.
        /// </summary>
        Three,
        /// <summary>
        /// The value <c>4</c>.
        /// </summary>
        Four,
        /// <summary>
        /// The value <c>5</c>.
        /// </summary>
        Five,
        /// <summary>
        /// The value <c>6</c>.
        /// </summary>
        Six,
        /// <summary>
        /// The value <c>7</c>.
        /// </summary>
        Seven,
        /// <summary>
        /// The value <c>8</c>.
        /// </summary>
        Eight,
        /// <summary>
        /// The value <c>9</c>.
        /// </summary>
        Nine,
        /// <summary>
        /// The value <c>10</c>.
        /// </summary>
        Ten,
        /// <summary>
        /// The jack.  Has the value <c>11</c>, though some games treat it as <c>10</c>.
        /// </summary>
        Jack,
        /// <summary>
        /// The queen.  Has the value <c>12</c>, though some games threat it as <c>10</c>.
        /// </summary>
        Queen,
        /// <summary>
        /// The king.  Has the value <c>13</c>, though some games treat it as <c>10</c>.
        /// The highest-value card, unless a game overrides this by treating the <see cref="Ace"/>
        /// as higher.
        /// </summary>
        King
    }

    /// <summary>
    /// Extension methods for <see cref="Value"/>.
    /// </summary>
    public static class ValueExtensions
    {
        /// <summary>
        /// Returns the numeric equivalent for the card value.
        /// </summary>
        /// <param name="value">The card value.</param>
        /// <returns>The numeric equivalent for the card value.</returns>
        public static int GetValue(this Value value)
        {
            return (int)value;
        }

        /// <summary>
        /// Returns a short symbol representing the card value.
        /// This is usually a single character, except for <see cref="Value.Ten"/>.
        /// </summary>
        /// <param name="value">The card value.</param>
        /// <returns>A short symbol representing the card value.</returns>
        public static string ToSymbol(this Value value)
        {
            return value switch
            {
                Value.Ace => "A",
                Value.Jack => "J",
                Value.Queen => "Q",
                Value.King => "K",
                _ => $"{value:D}",
            };
        }
    }
}
