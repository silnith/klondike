#pragma once

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move_filter.h>

#include <silnith/game/solitaire/board.h>
#include <silnith/game/solitaire/move/solitaire_move.h>

#include <memory>
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
                klondike(void) = default;
                klondike(klondike const&) = default;
                klondike& operator=(klondike const&) = default;
                klondike(klondike&&) noexcept = default;
                klondike& operator=(klondike&&) noexcept = default;
                virtual ~klondike(void) = default;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual bool is_win(board const& board) const override;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual bool is_win(game_state<move::solitaire_move, board> const& game_state) const override;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual std::vector<std::shared_ptr<move::solitaire_move>> find_all_moves(
                    std::shared_ptr<linked_node<game_state<move::solitaire_move, board>>> const& game_state_history) const override;

                /// <inheritdoc/>
                [[nodiscard]]
                virtual std::vector<std::shared_ptr<move_filter<move::solitaire_move, board>>> get_filters(void) const override;

            private:
                std::size_t const number_of_columns{ board::num_columns };
                std::size_t const draw_advance{ 3 };
            };
        }
    }
}
