#pragma once

#include <silnith/game/deck/card.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/column.h>
#include <silnith/game/solitaire/move/solitaire_move.h>

#include <cstddef>
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
                /// A move that takes a card from the stock pile and puts it into the foundation
                /// </summary>
                class StockPileToFoundationMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all the moves where a card is drawn from the stock pile to the foundation.
                    /// </summary>
                    /// <remarks>
                    /// <para>
                    /// This will either contain one move or zeor.
                    /// </para>
                    /// </remarks>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(board const& board);

                public:
                    StockPileToFoundationMove(void) = default;
                    StockPileToFoundationMove(StockPileToFoundationMove const&) = default;
                    StockPileToFoundationMove& operator=(StockPileToFoundationMove const&) = default;
                    StockPileToFoundationMove(StockPileToFoundationMove&&) noexcept = default;
                    StockPileToFoundationMove& operator=(StockPileToFoundationMove&&) noexcept = default;
                    virtual ~StockPileToFoundationMove(void) = default;

                    /// <summary>
                    /// Constructs a new move that takes a card from the stock pile and puts it into
                    /// the foundation.
                    /// </summary>
                    /// <param name="source_index">The stock pile index of the card being moved.</param>
                    /// <param name="card">The card being moved.</param>
                    explicit StockPileToFoundationMove(std::size_t source_index, deck::card const& card);

                    /// <summary>
                    /// Constructs a new move that takes a card from the stock pile and puts it into
                    /// the foundation.
                    /// </summary>
                    /// <param name="board">The board from which to get the source index and card.</param>
                    /// <exception cref="std::out_of_range">If no card is available to be drawn from the stock pile.</exception>
                    explicit StockPileToFoundationMove(board const& board);

                    /// <summary>
                    /// Constructs a new move that takes a card from the stock pile and puts it into
                    /// the foundation.
                    /// </summary>
                    /// <param name="board">The board from which to get the source index and card.</param>
                    /// <exception cref="std::out_of_range">If no card is available to be drawn from the stock pile.</exception>
                    explicit StockPileToFoundationMove(std::shared_ptr<board> const& board);

                    /// <summary>
                    /// Returns the index into the stock pile of the card being moved.
                    /// </summary>
                    /// <returns>The stock pile index.</returns>
                    [[nodiscard]]
                    std::size_t get_source_index(void) const;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool has_cards(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual std::vector<deck::card> get_cards(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_stock_pile_modification(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_from_stock_pile(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_from_foundation(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_from_column(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_from_column(std::size_t column_index) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual std::size_t get_source_column_index(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_to_foundation(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_to_column(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_to_column(std::size_t column_index) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual std::size_t get_destination_column_index(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual std::shared_ptr<board> apply(std::shared_ptr<board> const& b) const override;

                    [[nodiscard]]
                    bool operator==(StockPileToFoundationMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(StockPileToFoundationMove const& other) const = default;

                private:
                    /// <summary>
                    /// The index into the stock pile from which the card is taken.
                    /// </summary>
                    std::size_t const source_index;
                    /// <summary>
                    /// The card being moved.
                    /// </summary>
                    deck::card const _card;
                };

                /// <summary>
                /// Converts a move into a <c>std::string</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::string to_string(StockPileToFoundationMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(StockPileToFoundationMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, StockPileToFoundationMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, StockPileToFoundationMove const& move);
            }
        }
    }
}
