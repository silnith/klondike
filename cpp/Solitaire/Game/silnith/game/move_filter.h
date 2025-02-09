#pragma once

#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>

#include <concepts>
#include <memory>
#include <string>

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// A common interface for predicates used to filter the game search tree.
        /// </summary>
        /// <typeparam name="M">The move type for the game.</typeparam>
        /// <typeparam name="B">The board type for the game.</typeparam>
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        class move_filter
        {
        public:
            move_filter(void) = default;
            move_filter(move_filter const&) = default;
            move_filter& operator=(move_filter const&) = default;
            move_filter(move_filter&&) noexcept = default;
            move_filter& operator=(move_filter&&) noexcept = default;
            virtual ~move_filter(void) = default;

            /// <summary>
            /// Returns a key that can be used in a map for gathering statistics
            /// about how this filter performs.
            /// </summary>
            /// <returns>a map key for storing statistical information about the use of this filter.</returns>
            [[nodiscard]]
            virtual std::string get_statistics_key() const = 0;

            /// <summary>
            /// Returns <c>true</c> if the game state should be pruned from the
            /// search tree of possible moves for the game.
            /// </summary>
            /// <remarks>
            /// <para>
            /// Element <c>gameStateHistory->get_value()</c> will be the current move and resulting board.
            /// The last <c>linked_node.get_next()</c> will be the
            /// very first move of the game.
            /// </para>
            /// </remarks>
            /// <param name="game_state_history">a sequence of game states,
            /// beginning with the most recent move and resulting board.</param>
            /// <returns><c>true</c> if the node should be pruned from the search tree.</returns>
            [[nodiscard]]
            virtual bool should_filter(std::shared_ptr<linked_node<game_state<M, B>>> game_state_history) const = 0;
        };
    }
}
