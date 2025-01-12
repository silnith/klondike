#pragma once

#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>

#include <concepts>
#include <memory>
#include <tuple>
#include <type_traits>
#include <utility>

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// A single node in a game tree.  This keeps track of the history of a game, both
        /// the sequence of moves made from the beginning and the state of the board
        /// after every move.
        /// </summary>
        /// <remarks>
        /// <para>
        /// It is assumed that <c>game_state.get_moves().size() == game_state.get_boards().size()</c>.
        /// </para>
        /// </remarks>
        /// <typeparam name="M">The move type for the game.  This can simply be <c>move&lt;B&gt;</c>,
        /// but the interface allows specifying a subtype in the case that a game-specific interface is needed.</typeparam>
        /// <typeparam name="B">The board type for the game.</typeparam>
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        class game_state : private std::pair<std::shared_ptr<linked_node<M>>, std::shared_ptr<linked_node<B>>>
        {
        public:
            game_state(void) = delete;
            game_state(game_state<M, B> const&) = default;
            game_state<M, B>& operator=(game_state<M, B> const&) = default;
            game_state(game_state<M, B>&&) noexcept = default;
            game_state<M, B>& operator=(game_state<M, B>&&) noexcept = default;
            ~game_state(void) = default;

            /// <summary>
            /// Constructs a new game state with the given list of moves and associated board states.
            /// </summary>
            /// <param name="moves">The list of moves.</param>
            /// <param name="boards">The list of boards.</param>
            explicit game_state(std::shared_ptr<linked_node<M>> const& moves, std::shared_ptr<linked_node<B>> const& boards)
                : std::pair<std::shared_ptr<linked_node<M>>, std::shared_ptr<linked_node<B>>>{ moves, boards }
            {}

            /// <summary>
            /// Constructs an initial game state with the given initial move and board.
            /// </summary>
            /// <param name="initial_move">The initial move.  This is often a form of "deal deck".</param>
            /// <param name="initial_board">The initial board.</param>
            explicit game_state(M const& initial_move, B const& initial_board)
                : game_state{ std::make_shared<linked_node<M>>(initial_move), std::make_shared<linked_node<B>>(initial_board) }
            {}

            /// <summary>
            /// Constructs a new game state by appending the given move and board to the
            /// previous game state.
            /// </summary>
            /// <param name="parent">The previous game state.</param>
            /// <param name="move">The new move to append.</param>
            /// <param name="board">The new board to append.</param>
            explicit game_state(game_state<M, B> const& parent, M const& move, B const& board)
                : game_state{ std::make_shared<linked_node<M>>(move, parent.get_moves()), std::make_shared<linked_node<B>>(board, parent.get_boards()) }
            {}

            /// <summary>
            /// Constructs a new game state by applying the given move to the most recent
            /// board in the given parent game state.
            /// </summary>
            /// <param name="parent">The previous game state.</param>
            /// <param name="move">The new move to apply to the game state.</param>
            explicit game_state(game_state<M, B> const& parent, M const& move)
                : game_state{ parent, move, move.apply(parent.get_boards()[0]) }
            {}

            /// <summary>
            /// Returns the list of moves.
            /// </summary>
            /// <returns>The list of moves.</returns>
            std::shared_ptr<linked_node<M>> get_moves(void) const
            {
                return this->first;
            }

            /// <summary>
            /// Returns the list of board states.
            /// </summary>
            /// <returns>The list of board states.</returns>
            std::shared_ptr<linked_node<B>> get_boards(void) const
            {
                return this->second;
            }

        private:
        };
    }
}
