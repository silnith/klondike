#pragma once

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move_filter.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/move/solitaire_move.h>

#include <memory>
#include <span>
#include <vector>

namespace silnith
{
    namespace game
    {
        namespace solitaire
        {
            /// <summary>
            /// An implementation of Klondike solitaire.
            /// </summary>
            class klondike : public game<move::solitaire_move, board>
            {
            public:
                /// <summary>
                /// The number of columns on the board.
                /// </summary>
                //static std::size_t const number_of_columns{ board::num_columns };

                /// <summary>
                /// The number of cards to advance the stock pile.
                /// </summary>
                //static std::size_t const draw_advance{ 3 };

            public:
                klondike(void);
                klondike(klondike const&) = default;
                klondike& operator=(klondike const&) = default;
                klondike(klondike&&) noexcept = default;
                klondike& operator=(klondike&&) noexcept = default;
                virtual ~klondike(void) = default;

                /// <summary>
                /// Returns whether the given board is a winning game state for this game.
                /// </summary>
                /// <param name="board">The board to check.</param>
                /// <returns><c>true</c> if the board represents a win.</returns>
                [[nodiscard]]
                virtual bool is_win(board const& board) const;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual bool is_win(std::shared_ptr<linked_node<game_state<move::solitaire_move, board>> const> const& node_ptr) const;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual std::vector<std::shared_ptr<move::solitaire_move const>> find_all_moves(
                    std::shared_ptr<linked_node<game_state<move::solitaire_move, board>> const> const& game_state_history) const override;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual std::span<std::shared_ptr<move_filter<move::solitaire_move, board> const> const> get_filters(void) const override;

            private:
                std::size_t const number_of_columns{ board::num_columns };
                std::size_t const draw_advance{ 3 };
                std::vector<std::shared_ptr<move_filter<move::solitaire_move, board> const>> const filters;
            };
        }
    }
}
