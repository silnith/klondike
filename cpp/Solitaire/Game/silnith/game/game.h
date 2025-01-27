#pragma once

#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>
#include <silnith/game/move_filter.h>

#include <concepts>
#include <memory>
#include <vector>

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// A generic interface for any type of game.
        /// </summary>
        /// <typeparam name="M">The move type for the game.  This can simply be <c>move&lt;B&gt;</c>,
        /// but the interface allows specifying a subtype in the case that a
        /// game-specific interface is needed.</typeparam>
        /// <typeparam name="B">The board type for the game.</typeparam>
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        class game
        {
        public:
            game(void) = default;
            game(game const&) = default;
            game& operator=(game const&) = default;
            game(game&&) noexcept = default;
            game& operator=(game&&) noexcept = default;
            virtual ~game(void) = default;

            /// <summary>
            /// Returns whether the given board is a winning game state for this game.
            /// </summary>
            /// <param name="board">The board to check.</param>
            /// <returns><c>true</c> if the board represents a win.</returns>
            virtual bool is_win(B const& board) const = 0;

            /// <summary>
            /// Returns whether the given game state is a winning game state for this game.
            /// </summary>
            /// <param name="game_state">The game state to check.</param>
            /// <returns><c>true</c> if the game state is a win.</returns>
            virtual bool is_win(game_state<M, B> const& game_state) const = 0;

            /// <summary>
            /// Returns all the legal moves for the provided game state.  The current game
            /// board can be retrieved using <c>*(game_state_history->get_value().get_board())</c>.
            /// </summary>
            /// <param name="game_state_history">The game state to search for legal moves.</param>
            /// <returns>A collection of legal moves for the given game state.</returns>
            virtual std::vector<std::shared_ptr<M>> find_all_moves(std::shared_ptr<linked_node<game_state<M, B>>> const& game_state_history) const = 0;

            /// <summary>
            /// Returns filters.
            /// </summary>
            /// <returns>Move filters.</returns>
            virtual std::vector<std::shared_ptr<move_filter<M, B>>> get_filters(void) const = 0;
        };
    }
}
