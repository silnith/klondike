#pragma once

#include <silnith/game/deck/card.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/column.h>
#include <silnith/game/solitaire/move/solitaire_move.h>

#include <concepts>
#include <memory>
#include <ostream>
#include <string>
#include <vector>

#include <cstddef>

namespace silnith
{
    namespace game
    {
        namespace solitaire
        {
            namespace move
            {
                /// <summary>
                /// A move that advances the stock pile by an increment.
                /// </summary>
                /// <remarks>
                /// <para>
                /// In a typical game, the advance is three unless there are fewer than
                /// three cards left in the stock pile.
                /// </para>
                /// </remarks>
                class StockPileAdvanceMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all stock pile advance moves for a given board.
                    /// </summary>
                    /// <param name="stock_pile_advance">The number of cards to advance the stock pile.</param>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(std::size_t stock_pile_advance, board const& board);

                public:
                    StockPileAdvanceMove(void) = default;
                    StockPileAdvanceMove(StockPileAdvanceMove const&) = default;
                    StockPileAdvanceMove& operator=(StockPileAdvanceMove const&) = default;
                    StockPileAdvanceMove(StockPileAdvanceMove&&) noexcept = default;
                    StockPileAdvanceMove& operator=(StockPileAdvanceMove&&) noexcept = default;
                    virtual ~StockPileAdvanceMove(void) = default;

                    /// <summary>
                    /// Constructs a new move that advances the stock pile index.
                    /// </summary>
                    /// <param name="beginning_index">The stock pile index before the move is applied.</param>
                    /// <param name="increment">The number of cards that the stock pile index advances.</param>
                    /// <exception cref="out_of_range">If <paramref name="increment"/> is not positive.</exception>
                    explicit StockPileAdvanceMove(std::size_t beginning_index, std::size_t increment);

                    /// <summary>
                    /// Constructs a new move that advances the stock pile index.
                    /// </summary>
                    /// <param name="increment">The number of cards that the stock pile index advances.</param>
                    /// <param name="board">The board from which to take the beginning index.</param>
                    /// <exception cref="out_of_range">If <paramref name="increment"/> is not positive.</exception>
                    explicit StockPileAdvanceMove(std::size_t increment, board const& board);

                    /// <summary>
                    /// Constructs a new move that advances the stock pile index.
                    /// </summary>
                    /// <param name="increment">The number of cards that the stock pile index advances.</param>
                    /// <param name="b">The board from which to take the beginning index.</param>
                    /// <exception cref="out_of_range">If <paramref name="increment"/> is not positive.</exception>
                    explicit StockPileAdvanceMove(std::size_t increment, std::shared_ptr<board> const& b);

                    /// <summary>
                    /// Returns the stock pile index before the move is applied.
                    /// </summary>
                    /// <returns>The stock pile index before the move happens.</returns>
                    [[nodiscard]]
                    virtual std::size_t get_beginning_index(void) const;

                    /// <summary>
                    /// Returns the number of cards that the stock pile index is advanced by the move.
                    /// </summary>
                    /// <returns>The number of cards advanced in the stock pile.</returns>
                    [[nodiscard]]
                    virtual std::size_t get_increment(void) const;

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
                    bool operator==(StockPileAdvanceMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(StockPileAdvanceMove const&) const = default;

                private:
                    /// <summary>
                    /// The index into the stock pile before the advance move happens.
                    /// </summary>
                    std::size_t const beginning_index;
                    /// <summary>
                    /// The number of cards that the move advances the stock pile index.
                    /// </summary>
                    /// <remarks>
                    /// <para>
                    /// If the advanced index needs to be clamped, that happens when
                    /// the move is applied.
                    /// </para>
                    /// </remarks>
                    std::size_t const increment;
                };

                /// <summary>
                /// Converts a move into a <c>std::string</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::string to_string(StockPileAdvanceMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(StockPileAdvanceMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, StockPileAdvanceMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, StockPileAdvanceMove const& move);
            }
        }
    }
}
