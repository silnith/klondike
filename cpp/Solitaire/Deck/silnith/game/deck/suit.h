#pragma once

#include <silnith/game/deck/color.h>

#include <ostream>
#include <string>

namespace silnith
{
    namespace game
    {
        namespace deck
        {
            /// <summary>
            /// A suit for a playing card.
            /// </summary>
            enum class suit
            {
                /// <summary>
                /// &#x2663;
                /// </summary>
                club,
                /// <summary>
                /// &#x2666;
                /// </summary>
                diamond,
                /// <summary>
                /// &#x2665;
                /// </summary>
                heart,
                /// <summary>
                /// &#x2660;
                /// </summary>
                spade
            };

            /// <summary>
            /// Returns the color of a particular suit.
            /// </summary>
            /// <param name="suit">The suit.</param>
            /// <returns>The color of the suit.</returns>
            [[nodiscard]]
            color get_color(suit suit);

            /// <summary>
            /// Converts a suit to a string.
            /// </summary>
            /// <param name="suit">The suit.</param>
            /// <returns>A string representation of the suit.</returns>
            [[nodiscard]]
            std::string to_string(suit suit);

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
            ///     <term><see cref="suit::club"/></term>
            ///     <term>&#x2663;</term>
            ///   </item>
            ///   <item>
            ///     <term><see cref="suit::diamond"/></term>
            ///     <term>&#x2666;</term>
            ///   </item>
            ///   <item>
            ///     <term><see cref="suit::heart"/></term>
            ///     <term>&#x2665;</term>
            ///   </item>
            ///   <item>
            ///     <term><see cref="suit::spade"/></term>
            ///     <term>&#x2660;</term>
            ///   </item>
            /// </list>
            /// </remarks>
            /// <param name="suit">The suit.</param>
            /// <returns>A single Unicode glyph.</returns>
            [[nodiscard]]
            std::wstring to_wstring(suit suit);

            /// <summary>
            /// Formats a suit into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="suit">The suit.</param>
            /// <returns>The same output stream.</returns>
            std::ostream& operator<<(std::ostream& out, suit suit);

            /// <summary>
            /// Formats a suit into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="suit">The suit.</param>
            /// <returns>The same output stream.</returns>
            std::wostream& operator<<(std::wostream& out, suit suit);
        }
    }
}
