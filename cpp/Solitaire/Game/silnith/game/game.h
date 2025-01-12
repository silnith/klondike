#pragma once

#include <silnith/game/game_state.h>
#include <silnith/game/move.h>

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
            /// <summary>
            /// Returns whether the given board is a winning game state for this game.
            /// </summary>
            /// <param name="board">The board to check.</param>
            /// <returns><c>true</c> if the board represents a win.</returns>
            virtual bool is_win(B const& board) const = 0;

            /// <summary>
            /// Returns all the legal moves for the provided game state.  The current game
            /// board can be retrieved using <c>game_state->get_boards()[0]</c>.
            /// </summary>
            /// <param name="game_state">The game state to search for legal moves.</param>
            /// <returns>A collection of legal moves for the given game state.</returns>
            std::vector<M> find_all_moves(std::shared_ptr<game_state<M, B>> const& game_state) = 0;

            /// <summary>
            /// Possibly prunes or modifies the given game state based on the state
            /// history.  The returned value may be a different object than the input,
            /// so callers should always use the return value if it is not <c>null</c>.
            /// </summary>
            /// <param name="game_state"></param>
            /// <returns></returns>
            std::shared_ptr<game_state<M, B>> prune_game_state(std::shared_ptr<game_state<M, B>> const& game_state) = 0;
        };
    }
}
