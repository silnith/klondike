#pragma once

#include <silnith/game/deck/card.h>

#include <silnith/game/solitaire/column.h>

#include <cstddef>
#include <exception>
#include <map>
#include <ostream>
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
            /// The board for Klondike solitaire.
            /// </summary>
            /// <remarks>
            /// <para>
            /// The board consists of seven columns, the foundation, and the stock pile.
            /// </para>
            /// </remarks>
            class board
            {
            public:
                /// <summary>
                /// The number of columns on the board.
                /// </summary>
                static std::size_t const num_columns{ 7 };

            public:
                board(void) = default;
                board(board const&) = default;
                board& operator=(board const&) = default;
                board(board&&) noexcept = default;
                board& operator=(board&&) noexcept = default;
                ~board(void) = default;

                /// <summary>
                /// Constructs a new board.
                /// </summary>
                /// <param name="columns">The columns for the new board.</param>
                /// <param name="stock_pile">The stock pile.</param>
                /// <param name="stock_pile_index">The index into the stock pile of the current draw card.
                /// <c>0</c> means no card is available to be drawn, <c>stock_pile.size()</c>
                /// means all cards have been advanced and the last card is available to be drawn.</param>
                /// <param name="foundation">The foundation.</param>
                /// <exception cref="std::out_of_range">If the stock pile index is not inside of the stock pile.</exception>
                explicit board(
                    std::span<column const> columns,
                    std::span<deck::card const> stock_pile,
                    std::size_t stock_pile_index,
                    std::map<deck::suit, std::vector<deck::card>> foundation);

                /// <summary>
                /// Returns the columns.
                /// </summary>
                /// <returns>The columns.</returns>
                [[nodiscard]]
                std::vector<column> const& get_columns(void) const;

                /// <summary>
                /// Returns the given column.
                /// </summary>
                /// <param name="index">The index of the column.</param>
                /// <returns>The column.</returns>
                /// <exception cref="std::out_of_range">If the index is out of bounds.</exception>
                [[nodiscard]]
                column const& get_column(std::size_t index) const;

                /// <summary>
                /// Returns the stock pile.
                /// </summary>
                /// <returns>The stock pile.</returns>
                [[nodiscard]]
                std::vector<deck::card> const& get_stock_pile(void) const;

                /// <summary>
                /// Returns the current index into the stock pile.
                /// This is <c>0</c> if the stock pile has not been advanced,
                /// meaning no cards are available to be drawn.
                /// When this reaches the size of the stock pile, it is
                /// eligible to be recycled.
                /// </summary>
                /// <returns>The current index into the stock pile.
                /// This is the number of cards that have been advanced.</returns>
                [[nodiscard]]
                std::size_t get_stock_pile_index(void) const;

                /// <summary>
                /// Returns the foundation.  The game is won when all
                /// cards have been moved to the foundation.
                /// </summary>
                /// <returns>The foundation.</returns>
                [[nodiscard]]
                std::map<deck::suit, std::vector<deck::card>> const& get_foundation(void) const;

                /// <summary>
                /// Returns <see langword="true"/> if the stock pile can be advanced.
                /// If the stock pile is empty, or if the stock pile index
                /// is beyond the end of the stock pile, this is <see langword="false"/>.
                /// </summary>
                /// <remarks>
                /// <para>
                /// If the stock pile is empty, it cannot be advanced or recycled.
                /// Otherwise, either this or <see cref="can_recycle_stock_pile()"/>
                /// will be <see langword="true"/>, and the other <see langword="false"/>.
                /// </para>
                /// </remarks>
                /// <returns><see langword="true"/> if the stock pile can be advanced.</returns>
                [[nodiscard]]
                bool can_advance_stock_pile(void) const;

                /// <summary>
                /// Returns <see langword="true"/> if the stock pile can be recycled.
                /// If the stock pile is empty, or if the stock pile index
                /// is not currently beyond the end of the stock pile, this is <see langword="false"/>.
                /// </summary>
                /// <remarks>
                /// <para>
                /// If the stock pile is empty, it cannot be advanced or recycled.
                /// Otherwise, either this or <see cref="can_advance_stock_pile()"/>
                /// will be <see langword="true"/>, and the other <see langword="false"/>.
                /// </para>
                /// </remarks>
                /// <returns><see langword="true"/> if the stock pile can be recycled.</returns>
                [[nodiscard]]
                bool can_recycle_stock_pile(void) const;

                /// <summary>
                /// Returns the current card that can be drawn from the stock pile.
                /// </summary>
                /// <returns>The current card available from the stock pile.</returns>
                /// <exception cref="std::out_of_range">If no card is available
                /// to be drawn from the stock pile.</exception>
                [[nodiscard]]
                deck::card const& get_stock_pile_card(void) const;

                /// <summary>
                /// Returns the top card on the foundation for the given suit.
                /// </summary>
                /// <param name="suit">The suit.</param>
                /// <returns>The current top card for the suit in the foundation.</returns>
                /// <exception cref="std::out_of_range">If the foundation has no cards for the given suit.</exception>
                [[nodiscard]]
                deck::card const& get_top_of_foundation(deck::suit suit) const;

                /// <summary>
                /// Returns a copy of the foundation with the given card added to it.
                /// </summary>
                /// <remarks>
                /// <para>
                /// This does not validation that the move is legal.
                /// </para>
                /// </remarks>
                /// <param name="new_card">The card to add to the foundation.</param>
                /// <returns>A copy of the foundation with one card added.</returns>
                [[nodiscard]]
                std::map<deck::suit, std::vector<deck::card>> get_foundation_plus_card(deck::card new_card) const;

                /// <summary>
                /// Extracts the top card from the foundation for the given suit, and returns
                /// both the card and the remaining foundation missing the card.
                /// </summary>
                /// <param name="_suit">The suit of the foundation from which to remove the top card.</param>
                /// <returns>A pair of the card and the remaining foundation.</returns>
                /// <exception cref="std::out_of_range">If there are no cards in the foundation with the given suit.</exception>
                [[nodiscard]]
                std::pair<deck::card, std::map<deck::suit, std::vector<deck::card>>> extract_card_from_foundation(deck::suit _suit) const;

                /// <summary>
                /// Extracts the currently visible card from the stock pile, and returns
                /// both the card and the remaining stock pile missing the card.
                /// </summary>
                /// <returns>A pair of the card, and the remaining stock pile.</returns>
                /// <exception cref="std::out_of_range">If no card is available
                /// to be drawn from the stock pile.</exception>
                [[nodiscard]]
                std::pair<deck::card, std::vector<deck::card>> extract_stock_pile_card(void) const;

                /// <summary>
                /// Returns whether it would be legal to add the given card to the foundation.
                /// </summary>
                /// <param name="_card">The card to check.</param>
                /// <returns><see langword="true"/> if it is legal to add the card to the foundation.</returns>
                [[nodiscard]]
                bool can_add_to_foundation(deck::card _card) const;

                std::ostream& print_to(std::ostream& out) const;

                [[nodiscard]]
                bool operator==(board const& other) const;

                [[nodiscard]]
                bool operator!=(board const& other) const = default;

            private:
                [[nodiscard]]
                std::string to_symbol(deck::value v) const;

                [[nodiscard]]
                std::string to_symbol(deck::card c) const;

            private:
                std::vector<column> columns;
                std::vector<deck::card> stock_pile;
                std::size_t stock_pile_index;
                std::map<deck::suit, std::vector<deck::card>> foundation;
            };

            std::string to_string(board const& b);

            std::wstring to_wstring(board const& b);
        }
    }
}
