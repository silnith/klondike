#pragma once

#include <silnith/game/deck/card.h>
#include <silnith/game/deck/color.h>
#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <array>
#include <ostream>
#include <ranges>
#include <tuple>
#include <span>
#include <string>
#include <vector>

namespace silnith
{
    namespace game
    {
        namespace solitaire
        {
            /// <summary>
            /// A column on the solitaire board.
            /// </summary>
            /// <remarks>
            /// <para>
            /// A column consists of a number of face-down cards and a number of face-up
            /// cards.  The face-down cards are initialized when the board is first dealt.
            /// When there are no face-up cards, the top face-down card is flipped to face up.
            /// If there are no cards remaining, the column is emplty and any <see cref="silnith::game::deck::value::king"/>
            /// may be moved to it.
            /// </para>
            /// <para>
            /// Face-up cards in a column must obey the rules of a run.  Cards may be
            /// stacked on top of other face-up cards to make a run provided that a card is
            /// only placed on top of a card of the opposite color and of one value higher.
            /// For example, a <see cref="silnith::game::deck::value::two"/> of <see cref="silnith::game::deck::suit::club"/>
            /// may be placed on top of a <see cref="silnith::game::deck::value::three"/>
            /// of <see cref="silnith::game::deck::suit::heart"/>.
            /// </para>
            /// </remarks>
            class column
            {
            public:
                column(void) = default;
                column(column const&) = default;
                column& operator=(column const&) = default;
                column(column&&) noexcept = default;
                column& operator=(column&&) noexcept = default;
                ~column(void) = default;

                /// <summary>
                /// Constructs a new column with the provided face-down and face-up cards.
                /// </summary>
                /// <param name="face_down_cards">The face-down cards.</param>
                /// <param name="face_up_cards">The face-up cards.</param>
                explicit column(std::span<deck::card const> face_down_cards, std::span<deck::card const> face_up_cards);

                /// <summary>
                /// Returns <see langword="true"/> if the column has any face-down cards.
                /// </summary>
                /// <returns><see langword="true"/> if the column has any face-down cards.</returns>
                [[nodiscard]]
                bool has_face_down_cards(void) const;

                /// <summary>
                /// Returns <see langword="true"/> if the column has any face-up cards.
                /// </summary>
                /// <returns><see langword="true"/> if the column has any face-up cards.</returns>
                [[nodiscard]]
                bool has_face_up_cards(void) const;

                /// <summary>
                /// Returns the number of face-down cards in the column.
                /// </summary>
                /// <returns>The number of face-down cards in the column.</returns>
                [[nodiscard]]
                std::size_t get_number_of_face_down_cards(void) const;

                /// <summary>
                /// Returns the number of face-up cards in the column.
                /// </summary>
                /// <returns>The number of face-up cards in the column.</returns>
                [[nodiscard]]
                std::size_t get_number_of_face_up_cards(void) const;

                /// <summary>
                /// Returns the face-up cards.
                /// </summary>
                /// <remarks>
                /// <para>
                /// If there are more than one, they must obey the rules of a run.
                /// </para>
                /// </remarks>
                /// <returns>The face-up run of cards.</returns>
                [[nodiscard]]
                std::vector<deck::card> const& get_face_up_cards(void) const;

                /// <summary>
                /// Returns the top face-up card in the column.
                /// </summary>
                /// <returns>The top card.</returns>
                /// <exception cref="std::out_of_range">If there are no face-up cards in the column.</exception>
                [[nodiscard]]
                deck::card const& get_top_card(void) const;

                /// <summary>
                /// Returns a run of cards from the column.
                /// </summary>
                /// <param name="number_of_cards">The number of cards to take from the current column run.</param>
                /// <returns>The run of cards.</returns>
                /// <exception cref="std::out_of_range">If the number of cards is less than <c>1</c>,
                /// or exceeds the number of face-up cards.</exception>
                [[nodiscard]]
                std::vector<deck::card> get_top_cards(std::size_t number_of_cards) const;

                /// <summary>
                /// Returns a pair containing the top card from the column,
                /// and a copy of the column missing that card.
                /// </summary>
                /// <returns>A pair of the card and a new column.</returns>
                /// <exception cref="std::out_of_range">If the column does not have any cards.</exception>
                [[nodiscard]]
                std::pair<deck::card, column> extract_card(void) const;

                /// <summary>
                /// Returns a pair containing the top <paramref cref="number_of_cards"/> cards from the column,
                /// and a copy of the column missing those cards.
                /// </summary>
                /// <param name="number_of_cards">The number of cards to take from the current column run.</param>
                /// <returns>A pair of the run and a new column.</returns>
                /// <exception cref="std::out_of_range">If the column does not have <paramref cref="number_of_cards"/>
                /// cards available in the current run.</exception>
                [[nodiscard]]
                std::pair<std::vector<deck::card>, column> extract_run(std::size_t number_of_cards) const;

                /// <summary>
                /// Returns a copy of this column with the given run of cards added.
                /// </summary>
                /// <param name="new_cards">The new run of cards.</param>
                /// <returns>A copy of the column with the new run of cards added.</returns>
                /// <exception cref="std::invalid_argument">If the run is empty.</exception>
                [[nodiscard]]
                column with_cards(std::span<deck::card const> new_cards) const;

                /// <summary>
                /// Returns a copy of this column with the given card added to the run.
                /// </summary>
                /// <param name="new_card">The card to add.</param>
                /// <returns>A copy of the column with the added card.</returns>
                [[nodiscard]]
                column with_card(deck::card new_card) const;

                /// <summary>
                /// Returns whether it is legal to add the given run of cards to this column.
                /// </summary>
                /// <remarks>
                /// <para>
                /// If the column is empty, this checks whether the first card in the run
                /// is a king.
                /// </para>
                /// <para>
                /// If the column is not empty, this checks whether the first card in the run
                /// is one lower in value and of the opposite color to the top card on the column.
                /// </para>
                /// </remarks>
                /// <param name="run">The run of cards to add to this column.</param>
                /// <returns><see langword="true"/> if it is legal to add the given run of cards to this column.</returns>
                /// <exception cref="std::invalid_argument">If the run is empty.</exception>
                [[nodiscard]]
                bool can_add_run(std::span<deck::card const> run) const;

                [[nodiscard]]
                bool operator==(column const& other) const;

                [[nodiscard]]
                bool operator!=(column const& other) const = default;

                friend std::string to_string(column const& column);

                friend std::wstring to_wstring(column const& column);

            private:
                std::vector<deck::card> face_down;
                std::vector<deck::card> face_up;
            };

            std::ostream& operator<<(std::ostream& out, column const& column);

            std::wostream& operator<<(std::wostream& out, column const& column);
        }
    }
}
