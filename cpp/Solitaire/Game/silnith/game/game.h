#pragma once

#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>
#include <silnith/game/move_filter.h>

#include <concepts>
#include <memory>
#include <span>
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
            [[nodiscard]]
            virtual bool is_win(B const& board) const = 0;

            /// <summary>
            /// Returns whether the given game state is a winning game state for this game.
            /// </summary>
            /// <param name="game_state">The game state to check.</param>
            /// <returns><c>true</c> if the game state is a win.</returns>
            [[nodiscard]]
            virtual bool is_win(game_state<M, B> const& game_state) const = 0;

            /// <summary>
            /// Returns all the legal moves for the provided game state.  The current game
            /// board can be retrieved using <c>*(game_state_history->get_value().get_board())</c>.
            /// </summary>
            /// <param name="game_state_history">The game state to search for legal moves.</param>
            /// <returns>A collection of legal moves for the given game state.</returns>
            [[nodiscard]]
            virtual std::vector<std::shared_ptr<M>> find_all_moves(std::shared_ptr<linked_node<game_state<M, B>>> const& game_state_history) const = 0;

            /// <summary>
            /// Returns filters for pruning the game search space.
            /// </summary>
            /// <remarks>
            /// <para>
            /// The search space for any non-trivial game is massive.
            /// Realistically no search will ever complete unless the search space
            /// is pruned in some way.This method provides a way for an implementation
            /// of a game&#x2019;s logic to provide filters for pruning the search
            /// space, in a way that is meaningful for the specifics of the game.
            /// </para>
            /// <para>
            /// The search engine will run all the provided filters on every game state
            /// in the search tree.If any filter returns <see langword="true"/>, that game state
            /// will be pruned and no further search of it will happen.
            /// </para>
            /// <para>
            /// The collection of filters will only be queried when the search begins,
            /// so there is no value in altering the collection of returned filters
            /// beyond their initial creation.
            /// </para>
            /// <para>
            /// If the game implementation does not wish to provide any filters,
            /// it should return an empty enumerable.
            /// </para>
            /// </remarks>
            /// <returns>A collection of game state filters for pruning the search space.</returns>
            [[nodiscard]]
            virtual std::span<std::shared_ptr<move_filter<M, B>> const> get_filters(void) const = 0;
        };
    }
}
