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
                /// A move that deals a fresh deck of cards into a Klondike solitaire board.
                /// </summary>
                class DealMove : public solitaire_move
                {
                public:
                    DealMove(void) = default;
                    DealMove(DealMove const&) = default;
                    DealMove& operator=(DealMove const&) = default;
                    DealMove(DealMove&&) noexcept = default;
                    DealMove& operator=(DealMove&&) noexcept = default;
                    virtual ~DealMove(void) = default;

                    explicit DealMove(std::span<deck::card const> const& cards);

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
                    bool operator==(DealMove const& other) const;

                    [[nodiscard]]
                    bool operator!=(DealMove const&) const = default;

                private:
                    /// <summary>
                    /// The deck of cards to deal.
                    /// </summary>
                    std::vector<deck::card> const cards;
                };

                /// <summary>
                /// Converts a move into a <c>std::string</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::string to_string(DealMove const& move);

                /// <summary>
                /// Converts a move into a <c>std::wstring</c>.
                /// </summary>
                /// <param name="move">The move.</param>
                /// <returns>A string representation of the move.</returns>
                [[nodiscard]]
                std::wstring to_wstring(DealMove const& move);

                /// <summary>
                /// Formats a move into an output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::ostream& operator<<(std::ostream& out, DealMove const& move);

                /// <summary>
                /// Formats a move into a wide output stream.
                /// </summary>
                /// <param name="out">The output stream.</param>
                /// <param name="move">The move.</param>
                /// <returns>The same output stream.</returns>
                std::wostream& operator<<(std::wostream& out, DealMove const& move);
            }
        }
    }
}