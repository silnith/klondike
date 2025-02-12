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
            /// A numeric value for a playing card.
            /// </summary>
            enum class value
            {
                /// <summary>
                /// The ace.  Usually the lowest card, but in some games is also the highest card.
                /// Has a numeric value of <c>1</c>.
                /// </summary>
                ace = 1,
                /// <summary>
                /// The value <c>2</c>.
                /// </summary>
                two,
                /// <summary>
                /// The value <c>3</c>.
                /// </summary>
                three,
                /// <summary>
                /// The value <c>4</c>.
                /// </summary>
                four,
                /// <summary>
                /// The value <c>5</c>.
                /// </summary>
                five,
                /// <summary>
                /// The value <c>6</c>.
                /// </summary>
                six,
                /// <summary>
                /// The value <c>7</c>.
                /// </summary>
                seven,
                /// <summary>
                /// The value <c>8</c>.
                /// </summary>
                eight,
                /// <summary>
                /// The value <c>9</c>.
                /// </summary>
                nine,
                /// <summary>
                /// The value <c>10</c>.
                /// </summary>
                ten,
                /// <summary>
                /// The jack.  Has the value <c>11</c>, though some games treat it as <c>10</c>.
                /// </summary>
                jack,
                /// <summary>
                /// The queen.  Has the value <c>12</c>, though some games threat it as <c>10</c>.
                /// </summary>
                queen,
                /// <summary>
                /// The king.  Has the value <c>13</c>, though some games treat it as <c>10</c>.
                /// The highest-value card, unless a game overrides this by treating the <see cref="ace"/>
                /// as higher.
                /// </summary>
                king
            };

            /// <summary>
            /// Returns the numeric equivalent for the card value.
            /// </summary>
            /// <param name="value">The card value.</param>
            /// <returns>The numeric equivalent for the card value.</returns>
            [[nodiscard]]
            int get_value(value const& value);

            /// <summary>
            /// Converts a value to a <c>std::string</c>.
            /// </summary>
            /// <param name="value">The value.</param>
            /// <returns>A string representation of the value.</returns>
            [[nodiscard]]
            std::string to_string(value const& value);

            /// <summary>
            /// Converts a value to a <c>std::wstring</c>.
            /// </summary>
            /// <param name="value">The value.</param>
            /// <returns>A string representation of the value.</returns>
            [[nodiscard]]
            std::wstring to_wstring(value const& value);

            /// <summary>
            /// Formats a value into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="value">The value.</param>
            /// <returns>The same output stream.</returns>
            std::ostream& operator<<(std::ostream& out, value const& value);

            /// <summary>
            /// Formats a value into the wide output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="value">The value.</param>
            /// <returns>The same output stream.</returns>
            std::wostream& operator<<(std::wostream& out, value const& value);
        }
    }
}
