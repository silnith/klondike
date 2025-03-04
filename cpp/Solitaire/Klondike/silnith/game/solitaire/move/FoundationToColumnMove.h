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
                /// A move that takes a card from the foundation and puts it onto a column run.
                /// </summary>
                /// <remarks>
                /// <para>
                /// If the column is empty, only a <see cref="silnith::game::deck::value::king"/> may be
                /// moved.  If the column is not empty, then the card must adhere to the rules of
                /// a run.
                /// </para>
                /// </remarks>
                class FoundationToColumnMove : public solitaire_move
                {
                public:
                    /// <summary>
                    /// Finds all moves from the foundation to a column run.
                    /// </summary>
                    /// <param name="board">The board to examine.</param>
                    /// <returns>A collection of moves.</returns>
                    static std::vector<std::shared_ptr<solitaire_move>> find_moves(board const& board);

                public:
                    FoundationToColumnMove(void) = default;
                    FoundationToColumnMove(FoundationToColumnMove const&) = default;
                    FoundationToColumnMove& operator=(FoundationToColumnMove const&) = default;
                    FoundationToColumnMove(FoundationToColumnMove&&) noexcept = default;
                    FoundationToColumnMove& operator=(FoundationToColumnMove&&) noexcept = default;
                    virtual ~FoundationToColumnMove(void) = default;

                    /// <summary>
                    /// Constructs a move that takes a card from the foundation and puts it on top
                    /// of a column.
                    /// </summary>
                    /// <param name="destination_column_index">The index of the column into the board.</param>
                    /// <param name="card">The card being moved.</param>
                    explicit FoundationToColumnMove(std::size_t destination_column_index, deck::card const& card);

                    /// <summary>
                    /// Constructs a move that takes a card from the foundation and puts it on top
                    /// of a column.
                    /// </summary>
                    /// <param name="destination_column_index">The index of the column into the board.</param>
                    /// <param name="suit">The suit of card to pull from the foundation.</param>
                    /// <param name="board">The board to get the card from.</param>
                    /// <exception cref="std::out_of_range">If the foundation has no cards for the suit.</exception>
                    explicit FoundationToColumnMove(std::size_t destination_column_index, deck::suit const& suit, board const& board);

                    /// <summary>
                    /// Constructs a move that takes a card from the foundation and puts it on top
                    /// of a column.
                    /// </summary>
                    /// <param name="destination_column_index">The index of the column into the board.</param>
                    /// <param name="suit">The suit of card to pull from the foundation.</param>
                    /// <param name="board">The board to get the card from.</param>
                    /// <exception cref="std::out_of_range">If the foundation has no cards for the suit.</exception>
                    explicit FoundationToColumnMove(std::size_t destination_column_index, deck::suit const& suit, std::shared_ptr<board> const& board);

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
                    bool operator==(FoundationToColumnMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(FoundationToColumnMove const&) const = default;

                private:
                    /// <summary>
                    /// The index in the board of the destination column for the card.
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
                std::string to_string(FoundationToColumnMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(FoundationToColumnMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, FoundationToColumnMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, FoundationToColumnMove const& move);
            }
        }
    }
}
