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
                /// A move that takes a card from the stock pile and puts it onto a column.
                /// </summary>
                /// <remarks>
                /// <para>
                /// If the column is empty, the card must be a <see cref="silnith::game::deck::value::king"/>.
                /// If the column is not empty, the card most follow the rules of a run.
                /// </para>
                /// </remarks>
                class StockPileToColumnMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all the moves where a card is drawn from the stock pile to a column run.
                    /// </summary>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(board const& board);

                public:
                    StockPileToColumnMove(void) = default;
                    StockPileToColumnMove(StockPileToColumnMove const&) = default;
                    StockPileToColumnMove& operator=(StockPileToColumnMove const&) = default;
                    StockPileToColumnMove(StockPileToColumnMove&&) noexcept = default;
                    StockPileToColumnMove& operator=(StockPileToColumnMove&&) noexcept = default;
                    virtual ~StockPileToColumnMove(void) = default;

                    /// <summary>
                    /// Constructs a new move of a card from the stock pile to a column on the board.
                    /// </summary>
                    /// <param name="source_index">The stock pile index of the card being moved.</param>
                    /// <param name="destination_column_index">The index into the board of the destination column</param>
                    /// <param name="card">The card being moved.</param>
                    explicit StockPileToColumnMove(std::size_t source_index, std::size_t destination_column_index, deck::card const& card);

                    /// <summary>
                    /// Constructs a new move of a card from the stock pile to a column on the board.
                    /// </summary>
                    /// <param name="destination_column_index">The index into the board of the destination column.</param>
                    /// <param name="board">The board from which to get the card.</param>
                    /// <exception cref="std::out_of_range">If no card is available to be drawn from the stock pile.</exception>
                    explicit StockPileToColumnMove(std::size_t destination_column_index, board const& board);

                    /// <summary>
                    /// Constructs a new move of a card from the stock pile to a column on the board.
                    /// </summary>
                    /// <param name="destination_column_index">The index into the board of the destination column.</param>
                    /// <param name="board">The board from which to get the card.</param>
                    /// <exception cref="std::out_of_range">If no card is available to be drawn from the stock pile.</exception>
                    explicit StockPileToColumnMove(std::size_t destination_column_index, std::shared_ptr<board> const& board);

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
                    virtual bool is_stock_pile_advance(void) const override;

                    /// <inheritdoc/>
                    [[nodiscard]]
                    virtual bool is_stock_pile_recycle(void) const override;

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
                    bool operator==(StockPileToColumnMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(StockPileToColumnMove const& other) const = default;

                private:
                    /// <summary>
                    /// The index into the stock pile from which the card is taken.
                    /// </summary>
                    std::size_t const source_index;
                    /// <summary>
                    /// The index into the board of the column to which the card is being moved.
                    /// </summary>
                    std::size_t const destination_column_index;
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
                std::string to_string(StockPileToColumnMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(StockPileToColumnMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, StockPileToColumnMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, StockPileToColumnMove const& move);
            }
        }
    }
}
