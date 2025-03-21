#pragma once

#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>

#include <concepts>
#include <memory>
#include <ostream>
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
        class game_state : private std::pair<std::shared_ptr<M const> const, std::shared_ptr<B const> const>
        {
        public:
            game_state(void) = delete;
            game_state(game_state<M, B> const&) = default;
            game_state<M, B>& operator=(game_state<M, B> const&) = default;
            game_state(game_state<M, B>&&) noexcept = default;
            game_state<M, B>& operator=(game_state<M, B>&&) noexcept = default;
            ~game_state(void) = default;

            /// <summary>
            /// Constructs a new game state with the given move and resulting board.
            /// </summary>
            /// <param name="move">The move.</param>
            /// <param name="board">The board.</param>
            explicit game_state(std::shared_ptr<M const> const& move, std::shared_ptr<B const> const& board)
                : std::pair<std::shared_ptr<M const> const, std::shared_ptr<B const> const>{ move, board }
            {}

            /// <summary>
            /// Returns the move.
            /// </summary>
            /// <returns>The move.</returns>
            [[nodiscard]]
            std::shared_ptr<M const> const& get_move(void) const
            {
                //return std::pair<M, B>::first;
                return this->first;
            }

            /// <summary>
            /// Returns the board.
            /// </summary>
            /// <returns>The board.</returns>
            [[nodiscard]]
            std::shared_ptr<B const> const& get_board(void) const
            {
                //return std::pair<M, B>::second;
                return this->second;
            }

        private:
        };

        /// <summary>
        /// Formats a game state into the output stream.
        /// </summary>
        /// <typeparam name="M">The move type for the game.</typeparam>
        /// <typeparam name="B">The board type for the game.</typeparam>
        /// <param name="out">The output stream.</param>
        /// <param name="state">The game state.</param>
        /// <returns>The same output stream.</returns>
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        std::ostream& operator<<(std::ostream& out, game_state<M, B> const& state)
        {
            std::shared_ptr<M const> const& move_ptr{ state.get_move() };
            std::shared_ptr<B const> const& board_ptr{ state.get_board() };

            using namespace std::literals::string_literals;
            out << "game_state{"s;
            out << *move_ptr;
            out << ", "s;
            out << *board_ptr;
            out << "}"s;
            return out;
        }

        /// <summary>
        /// Formats a game state into the wide output stream.
        /// </summary>
        /// <typeparam name="M">The move type for the game.</typeparam>
        /// <typeparam name="B">The board type for the game.</typeparam>
        /// <param name="out">The output stream.</param>
        /// <param name="state">The game state.</param>
        /// <returns>The same output stream.</returns>
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        std::wostream& operator<<(std::wostream& out, game_state<M, B> const& state)
        {
            std::shared_ptr<M const> const& move_ptr{ state.get_move() };
            std::shared_ptr<B const> const& board_ptr{ state.get_board() };

            using namespace std::literals::string_literals;
            out << L"game_state{"s;
            out << *move_ptr;
            out << L", "s;
            out << *board_ptr;
            out << L"}"s;
            return out;
        }
    }
}
