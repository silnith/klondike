#pragma once

#include <ostream>
#include <string>

namespace silnith
{
    namespace game
    {
        namespace deck
        {
            /// <summary>
            /// A color for a playing card&#8217;s suit.
            /// </summary>
            enum class color
            {
                /// <summary>
                /// &#x2663; &amp; &#x2660;
                /// </summary>
                black,
                /// <summary>
                /// &#x2666; &amp; &#x2665;
                /// </summary>
                red
            };

            /// <summary>
            /// Converts a color to a <c>std::string</c>.
            /// </summary>
            /// <param name="color">The color.</param>
            /// <returns>A string representation of the color.</returns>
            std::string to_string(color color);

            /// <summary>
            /// Converts a color to a <c>std::wstring</c>.
            /// </summary>
            /// <param name="color">The color.</param>
            /// <returns>A string representation of the color.</returns>
            std::wstring to_wstring(color color);

            /// <summary>
            /// Formats a color into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="color">The color.</param>
            /// <returns>The same output stream.</returns>
            std::ostream& operator<<(std::ostream& out, color color);

            /// <summary>
            /// Formats a color into the wide output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="color">The color.</param>
            /// <returns>The same output stream.</returns>
            std::wostream& operator<<(std::wostream& out, color color);
        }
    }
}
