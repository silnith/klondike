#pragma once

#include <silnith/game/deck/card.h>

#include <silnith/game/move.h>

#include <silnith/game/solitaire/board.h>

#include <memory>
#include <vector>

namespace silnith
{
    namespace game
    {
        namespace solitaire
        {
            namespace move
            {
                /// <summary>
                /// The common interface for solitaire moves.
                /// </summary>
                class solitaire_move : public silnith::game::move<silnith::game::solitaire::board>
                {
                public:
                    solitaire_move(void) = default;
                    solitaire_move(solitaire_move const&) = default;
                    solitaire_move& operator=(solitaire_move const&) = default;
                    solitaire_move(solitaire_move&&) noexcept = default;
                    solitaire_move& operator=(solitaire_move&&) noexcept = default;
                    virtual ~solitaire_move(void) = default;

                    /// <summary>
                    /// Returns whether the move involves one or more cards.
                    /// </summary>
                    /// <returns><see langword="true"/> if the move involves cards.</returns>
                    [[nodiscard]]
                    virtual bool has_cards(void) const = 0;

                    /// <summary>
                    /// Returns the cards that have been moved.
                    /// Returns an empty vector if there were no cards moved.
                    /// </summary>
                    /// <returns>The cards moved.</returns>
                    [[nodiscard]]
                    virtual std::vector<silnith::game::deck::card> get_cards(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move changes the stock pile index.
                    /// Moves that advance or recycle the stock pile will return <see langword="true"/>.
                    /// Drawing from the stock pile also adjusts the index, so will also
                    /// return <see langword="true"/> for this method.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move adjusts the stock pile index.</returns>
                    [[nodiscard]]
                    virtual bool is_stock_pile_modification(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move advances the stock pile.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move advances the stock pile.</returns>
                    [[nodiscard]]
                    virtual bool is_stock_pile_advance(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move recycles the stock pile.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move recycles the stock pile.</returns>
                    [[nodiscard]]
                    virtual bool is_stock_pile_recycle(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move takes a card from the stock pile.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move takes a card from the stock pile.</returns>
                    [[nodiscard]]
                    virtual bool is_from_stock_pile(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move takes a card from the foundation.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move takes a card from the foundation.</returns>
                    [[nodiscard]]
                    virtual bool is_from_foundation(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move takes cards from a column.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move takes cards from a column.</returns>
                    [[nodiscard]]
                    virtual bool is_from_column(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move takes cards from the specific column.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move takes cards from the specific column.</returns>
                    [[nodiscard]]
                    virtual bool is_from_column(std::size_t column_index) const = 0;

                    /// <summary>
                    /// Returns the index of the column from which this move takes cards.
                    /// Throws an exception if either <see cref="has_cards()"/> or <see cref="is_from_column()"/>
                    /// returns <see langword="false"/>.
                    /// </summary>
                    /// <returns>The index of the column from which this move takes cards.</returns>
                    [[nodiscard]]
                    virtual std::size_t get_source_column_index(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move puts a card into the foundation.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move puts a card into the foundation.</returns>
                    [[nodiscard]]
                    virtual bool is_to_foundation(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move puts cards onto a column run.
                    /// </summary>
                    /// <returns><see langword="true"/> if this move puts cards onto a column run.</returns>
                    [[nodiscard]]
                    virtual bool is_to_column(void) const = 0;

                    /// <summary>
                    /// Returns <see langword="true"/> if this move puts cards onto the specific column.
                    /// </summary>
                    /// <param name="column_index">The column to check whether receives cards.</param>
                    /// <returns><see langword="true"/> if this move puts cards onto the specific column.</returns>
                    [[nodiscard]]
                    virtual bool is_to_column(std::size_t column_index) const = 0;

                    /// <summary>
                    /// Returns the index of the column to which this move adds cards.
                    /// Throws an exception if either <see cref="has_cards()"/> or <see cref="is_to_column()"/>
                    /// returns <see langword="false"/>.
                    /// </summary>
                    /// <returns>The index of the column to which this move adds cards.</returns>
                    [[nodiscard]]
                    virtual std::size_t get_destination_column_index(void) const = 0;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual std::shared_ptr<silnith::game::solitaire::board> apply(std::shared_ptr<silnith::game::solitaire::board> const& board) const override = 0;

                private:
                };
            }
        }
    }
}
