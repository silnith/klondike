#pragma once

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/move/solitaire_move.h>

#include <memory>
#include <ostream>
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
                /// A move that takes a face-up card from a column and puts it into the foundation.
                /// </summary>
                class ColumnToFoundationMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all the column to foundation moves for a given board.
                    /// </summary>
                    /// <remarks>
                    /// <para>
                    /// This could potentially find a move for each suit.
                    /// </para>
                    /// </remarks>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(board const& board);

                public:
                    ColumnToFoundationMove(void) = default;
                    ColumnToFoundationMove(ColumnToFoundationMove const&) = default;
                    ColumnToFoundationMove& operator=(ColumnToFoundationMove const&) = default;
                    ColumnToFoundationMove(ColumnToFoundationMove&&) noexcept = default;
                    ColumnToFoundationMove& operator=(ColumnToFoundationMove&&) noexcept = default;
                    virtual ~ColumnToFoundationMove(void) = default;

                    /// <summary>
                    /// Constructs a new move that takes a face-up card from a column and puts it
                    /// into the foundation.
                    /// </summary>
                    /// <param name="source_column_index">The index into the board of the column
                    /// from which the card is taken.</param>
                    /// <param name="card">The card being moved.</param>
                    ColumnToFoundationMove(std::size_t source_column_index, deck::card const& card);

                    /// <summary>
                    /// Constructs a new move that takes a face-up card from a column and puts it
                    /// into the foundation.
                    /// </summary>
                    /// <param name="source_column_index">The index into the board of the column
                    /// from which the card is taken.</param>
                    /// <param name="board">The board containing the card to move.</param>
                    /// <exception cref="std::out_of_range"></exception>
                    /// <exception cref="std::illegal_argument"></exception>
                    ColumnToFoundationMove(std::size_t source_column_index, board const& board);

                    /// <summary>
                    /// Constructs a new move that takes a face-up card from a column and puts it
                    /// into the foundation.
                    /// </summary>
                    /// <param name="source_column_index">The index into the board of the column
                    /// from which the card is taken.</param>
                    /// <param name="board">The board containing the card to move.</param>
                    /// <exception cref="std::out_of_range">If the source column index is out of range,
                    /// or the source column has no cards to move.</exception>
                    ColumnToFoundationMove(std::size_t source_column_index, std::shared_ptr<board> const& board);

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
                    bool operator==(ColumnToFoundationMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(ColumnToFoundationMove const&) const = default;

                private:
                    /// <summary>
                    /// The index in the board of the column from which the card is taken.
                    /// </summary>
                    std::size_t const source_column_index;
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
                std::string to_string(ColumnToFoundationMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(ColumnToFoundationMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, ColumnToFoundationMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, ColumnToFoundationMove const& move);
            }
        }
    }
}
