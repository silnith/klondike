#pragma once

#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <ostream>
#include <string>

namespace silnith
{
    namespace game
    {
        namespace deck
        {
            class card
            {
            public:
                card(void) = delete;
                card(card const&) = default;
                card& operator=(card const&) = default;
                card(card&&) noexcept = default;
                card& operator=(card&&) noexcept = default;
                ~card(void) = default;

                /// <summary>
                /// Constructs a new playing card with the given value and suit.
                /// </summary>
                /// <param name="value">The value for the card.</param>
                /// <param name="suit">The suit for the card.</param>
                explicit card(value const value, suit const suit) : value{ value }, suit{ suit }
                {
                }

                /// <summary>
                /// Returns the card value.
                /// </summary>
                /// <returns>The card value.</returns>
                [[nodiscard]]
                value get_value(void) const noexcept
                {
                    return value;
                }

                /// <summary>
                /// Returns the card suit.
                /// </summary>
                /// <returns>The card suit.</returns>
                [[nodiscard]]
                suit get_suit(void) const noexcept
                {
                    return suit;
                }

                [[nodiscard]]
                bool operator==(card const& other) const
                {
                    return value == other.value && suit == other.suit;
                }

                [[nodiscard]]
                bool operator!=(card const& other) const = default;

            private:
                value const value;
                suit const suit;
            };

            /// <summary>
            /// Converts a card to a <c>std::string</c>.
            /// </summary>
            /// <param name="card">The card.</param>
            /// <returns>A string representation of the card.</returns>
            [[nodiscard]]
            std::string to_string(card const& card);

            /// <summary>
            /// Converts a card to a <c>std::wstring</c>.
            /// </summary>
            /// <param name="card">The card.</param>
            /// <returns>A string representation of the card.</returns>
            [[nodiscard]]
            std::wstring to_wstring(card const& card);

            /// <summary>
            /// Formats a card into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="card">The card.</param>
            /// <returns>The same output stream.</returns>
            std::ostream& operator<<(std::ostream& out, card const& card);

            /// <summary>
            /// Formats a card into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="card">The card.</param>
            /// <returns>The same output stream.</returns>
            std::wostream& operator<<(std::wostream& out, card const& card);
        }
    }
}
