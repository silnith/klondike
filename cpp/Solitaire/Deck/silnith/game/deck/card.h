#pragma once

#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <ostream>
#include <span>
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
                card& operator=(card const&) = delete;
                card(card&&) noexcept = default;
                card& operator=(card&&) noexcept = default;
                ~card(void) = default;

                /// <summary>
                /// Constructs a new playing card with the given value and suit.
                /// </summary>
                /// <param name="value">The value for the card.</param>
                /// <param name="suit">The suit for the card.</param>
                explicit card(value const& value, suit const& suit);

                /// <summary>
                /// Returns the card value.
                /// </summary>
                /// <returns>The card value.</returns>
                [[nodiscard]]
                value const& get_value(void) const noexcept;

                /// <summary>
                /// Returns the card suit.
                /// </summary>
                /// <returns>The card suit.</returns>
                [[nodiscard]]
                suit const& get_suit(void) const noexcept;

                [[nodiscard]]
                bool operator==(card const&) const;

                [[nodiscard]]
                bool operator!=(card const&) const = default;

            private:
                value const _value;
                suit const _suit;
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
            /// Formats a card into the wide output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="card">The card.</param>
            /// <returns>The same output stream.</returns>
            std::wostream& operator<<(std::wostream& out, card const& card);

            /// <summary>
            /// Formats a sequence of cards into the output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="card">The sequence of cards.</param>
            /// <returns>The same output stream.</returns>
            std::ostream& operator<<(std::ostream& out, std::span<card const> const& cards);

            /// <summary>
            /// Formats a sequence of cards into the wide output stream.
            /// </summary>
            /// <param name="out">The output stream.</param>
            /// <param name="card">The sequence of cards.</param>
            /// <returns>The same output stream.</returns>
            std::wostream& operator<<(std::wostream& out, std::span<card const> const& cards);
        }
    }
}
