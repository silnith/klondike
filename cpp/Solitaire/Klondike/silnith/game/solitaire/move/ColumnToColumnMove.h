#pragma once

#include <silnith/game/deck/card.h>
#include <silnith/game/deck/value.h>

#include <silnith/game/solitaire/board.h>

#include <silnith/game/solitaire/move/solitaire_move.h>

#include <cstddef>
#include <memory>
#include <span>
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
                /// A move that takes a run of cards from one column and puts it on top of
                /// another run on a different column.
                /// </summary>
                /// <remarks>
                /// <para>
                /// A run can consist of a single card, or it can consist of many cards.
                /// </para>
                /// </remarks>
                class ColumnToColumnMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all moves where a run is moved from one column to another.
                    /// </summary>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(board const& board);

                public:
                    ColumnToColumnMove(void) = default;
                    ColumnToColumnMove(ColumnToColumnMove const&) = default;
                    ColumnToColumnMove& operator=(ColumnToColumnMove const&) = default;
                    ColumnToColumnMove(ColumnToColumnMove&&) noexcept = default;
                    ColumnToColumnMove& operator=(ColumnToColumnMove&&) noexcept = default;
                    virtual ~ColumnToColumnMove(void) = default;

                    /// <summary>
                    /// Constructs a new move of a run of cards from one column to another.
                    /// </summary>
                    /// <param name="source_column_index">The index into the board of the source column.</param>
                    /// <param name="destination_column_index">The index into the board of the destination column.</param>
                    /// <param name="cards">The cards being moved.</param>
                    /// <exception cref="std::out_of_range">If the source or destination columns are out of range.</exception>
                    /// <exception cref="std::invalid_argument">If the source and destination columns are the same.</exception>
                    explicit ColumnToColumnMove(std::size_t source_column_index,
                        std::size_t destination_column_index,
                        std::span<deck::card const> const& cards);

                    /// <summary>
                    /// Constructs a new move of a run of cards from one column to another.
                    /// </summary>
                    /// <param name="source_column_index">The index into the board of the source column.</param>
                    /// <param name="destination_column_index">The index into the board of the destination column.</param>
                    /// <param name="number_of_cards">The number of cards being moved.</param>
                    /// <param name="board">The board from which to get the cards being moved.</param>
                    /// <exception cref="std::out_of_range">If the source or destination columns are out of range,
                    /// or the number of cards is less than <c>1</c>, or exceeds the available cards.</exception>
                    explicit ColumnToColumnMove(std::size_t source_column_index,
                        std::size_t destination_column_index,
                        std::size_t number_of_cards,
                        board const& board);

                    /// <summary>
                    /// Constructs a new move of a run of cards from one column to another.
                    /// </summary>
                    /// <param name="source_column_index">The index into the board of the source column.</param>
                    /// <param name="destination_column_index">The index into the board of the destination column.</param>
                    /// <param name="number_of_cards">The number of cards being moved.</param>
                    /// <param name="board">The board from which to get the cards being moved.</param>
                    /// <exception cref="std::out_of_range">If the source or destination columns are out of range,
                    /// or the number of cards is less than <c>1</c>, or exceeds the available cards.</exception>
                    explicit ColumnToColumnMove(std::size_t source_column_index,
                        std::size_t destination_column_index,
                        std::size_t number_of_cards,
                        std::shared_ptr<board> const& board);

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
                    bool operator==(ColumnToColumnMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(ColumnToColumnMove const&) const = default;

                private:
                    /// <summary>
                    /// The index into the board of the source column.
                    /// </summary>
                    std::size_t const source_column_index;
                    /// <summary>
                    /// The index into the board of the destination column.
                    /// </summary>
                    std::size_t const destination_column_index;
                    /// <summary>
                    /// The cards being moved.
                    /// </summary>
                    std::vector<deck::card> const cards;
                };

                /// <summary>
                /// Converts a move into a <c>std::string</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::string to_string(ColumnToColumnMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(ColumnToColumnMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, ColumnToColumnMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, ColumnToColumnMove const& move);
            }
        }
    }
}