#pragma once

#include <silnith/game/deck/card.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/move/solitaire_move.h>

#include <cstddef>
#include <memory>
#include <string>
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
                /// A move that recycles the stock pile.  This sets the current index into the
                /// stock pile back to zero.
                /// </summary>
                class StockPileRecycleMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all recycle stock pile moves for a given board.
                    /// </summary>
                    /// <remarks>
                    /// <para>
                    /// This will either contain one move or zero.
                    /// </para>
                    /// </remarks>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(board const& board);

                public:
                    StockPileRecycleMove(void) = default;
                    StockPileRecycleMove(StockPileRecycleMove const&) = default;
                    StockPileRecycleMove& operator=(StockPileRecycleMove const&) = default;
                    StockPileRecycleMove(StockPileRecycleMove&&) noexcept = default;
                    StockPileRecycleMove& operator=(StockPileRecycleMove&&) noexcept = default;
                    virtual ~StockPileRecycleMove(void) = default;

                    /// <summary>
                    /// Constructs a new move that recycles the stock pile.
                    /// </summary>
                    /// <param name="source_index">The index into the stock pile before the move.</param>
                    explicit StockPileRecycleMove(std::size_t source_index);

                    /// <summary>
                    /// Constructs a new move that recycles the stock pile.
                    /// </summary>
                    /// <param name="board">The board from which to take the stock pile index.</param>
                    explicit StockPileRecycleMove(board const& board);

                    /// <summary>
                    /// Constructs a new move that recycles the stock pile.
                    /// </summary>
                    /// <param name="board">The board from which to take the stock pile index.</param>
                    explicit StockPileRecycleMove(std::shared_ptr<board> board);

                    /// <summary>
                    /// Returns the index into the stock pile before the move is applied.
                    /// </summary>
                    /// <returns>The stock pile index beforehand.</returns>
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
                    virtual std::shared_ptr<board const> apply(std::shared_ptr<board const> const& b) const override;

                    [[nodiscard]]
                    bool operator==(StockPileRecycleMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(StockPileRecycleMove const&) const = default;

                private:
                    /// <summary>
                    /// The index into the stock pile before the move is applied.
                    /// </summary>
                    /// <remarks>
                    /// <para>
                    /// For a legal move, this will be equal to the size of the stock pile.
                    /// </para>
                    /// </remarks>
                    std::size_t const source_index;
                };

                /// <summary>
                /// Converts a move into a <c>std::string</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::string to_string(StockPileRecycleMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(StockPileRecycleMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, StockPileRecycleMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, StockPileRecycleMove const& move);
            }
        }
    }
}
