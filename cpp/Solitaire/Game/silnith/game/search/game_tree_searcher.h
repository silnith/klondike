#pragma once

#include <silnith/game/game.h>
#include <silnith/game/move.h>

#include <atomic>
#include <concepts>
#include <map>
#include <memory>
#include <ostream>
#include <span>
#include <string>

namespace silnith
{
    namespace game
    {
        namespace search
        {
            /// <summary>
            /// Abstract base class for game tree search algorithms.
            /// This provides a common implementation of the logic to expand a single node
            /// in the game tree.
            /// </summary>
            /// <typeparam name="M">The move type for the game.</typeparam>
            /// <typeparam name="B">The board type for the game.</typeparam>
            template<class M, class B>
                requires (std::is_base_of_v<move<B>, M>)
            class game_tree_searcher
            {
            protected:
                game_tree_searcher(void) = default;
                game_tree_searcher(game_tree_searcher const&) = default;
                game_tree_searcher& operator=(game_tree_searcher const&) = default;
                game_tree_searcher(game_tree_searcher&&) noexcept = default;
                game_tree_searcher& operator=(game_tree_searcher&&) noexcept = default;
                virtual ~game_tree_searcher(void) = default;

                /// <summary>
                /// Constructs a game tree search run for the provided game.
                /// </summary>
                /// <param name="game">The game.</param>
                explicit game_tree_searcher(std::shared_ptr<game<M, B> const> const& game)
                    : _game{ game },
                    _game_filters{ game->get_filters() }
                {
                    for (std::shared_ptr<move_filter<M, B> const> const& filter : _game_filters)
                    {
                        std::string const statistics_key{ filter->get_statistics_key() };
                        //moves_pruned.emplace(statistics_key, std::atomic_ullong{ 0 });
                        moves_pruned[statistics_key] = 0;
                    }
                }

                /// <summary>
                /// Returns the current size of the queue of nodes to be searched.
                /// </summary>
                /// <returns>The current size of the queue.</returns>
                [[nodiscard]]
                virtual std::size_t queue_size(void) const = 0;

                /// <summary>
                /// Returns the current number of winning game states found.
                /// </summary>
                /// <returns>The current number of wins found.</returns>
                [[nodiscard]]
                virtual std::size_t win_count(void) const = 0;

            public:
                /// <summary>
                /// Prints statistics on the game tree search to the provided
                /// output stream.
                /// </summary>
                /// <param name="out">The output stream to print statistics to.</param>
                /// <returns>The same output stream.</returns>
                virtual std::ostream& print_statistics(std::ostream& out) const
                {
                    using namespace std::literals::string_literals;
                    out << "Nodes examined: "s << game_states_examined << std::endl;
                    out << "Boards generated: "s << boards_generated << std::endl;
                    out << "Moves pruned: "s << moves_pruned_total << std::endl;
                    out << "Queue size: "s << queue_size() << std::endl;
                    out << "Wins: "s << win_count() << std::endl;
                    for (std::shared_ptr<move_filter<M, B> const> const& filter : _game_filters)
                    {
                        std::string const statistics_key{ filter->get_statistics_key() };
                        out << "Moves pruned by filter "s
                            << statistics_key
                            << ": "s
                            << moves_pruned.at(statistics_key)
                            << std::endl;
                    }
                    return out;
                }

                /// <summary>
                /// Returns the total number of game states examined.
                /// </summary>
                /// <returns>The total number of game states examined.</returns>
                [[nodiscard]]
                virtual unsigned long long get_number_of_game_states_examined(void) const
                {
                    return game_states_examined;
                }

                /// <summary>
                /// Returns the total number of boards generated.
                /// </summary>
                /// <returns>The total number of boards generated.</returns>
                [[nodiscard]]
                virtual unsigned long long get_boards_generated(void) const
                {
                    return boards_generated;
                }

            protected:
                /// <summary>
                /// Queues a game tree node for searching.
                /// </summary>
                /// <param name="node">The game tree node to search.</param>
                virtual void queue_node(std::shared_ptr<linked_node<game_state<M, B>> const> const& node) = 0;

                /// <summary>
                /// Captures a sequence of game states that culminates in a win.
                /// </summary>
                /// <param name="node">The game state history that results in a win.</param>
                virtual void add_win(std::shared_ptr<linked_node<game_state<M, B>> const> const& node) = 0;

                /// <summary>
                /// Examines a single node in the game tree.  This enumerates all possible
                /// moves, applies the move filters, and examines unfiltered moves to see if
                /// they are a winning game state.  Winning game states are passed to
                /// <c>add_win</c>.  All other unfiltered game states are passed to
                /// <c>queue_node</c>.
                /// </summary>
                /// <remarks>
                /// <para>
                /// This also captures statistics on the number of nodes examined, boards
                /// generated, and moves filtered.  These statistics can be printed using
                /// <c>print_statistics</c>.
                /// </para>
                /// </remarks>
                /// <param name="node">The node containing the game state to examine.</param>
                virtual void examine_node(std::shared_ptr<linked_node<game_state<M, B>> const> const& node)
                {
                    game_states_examined++;
                    game_state<M, B> const& game_state1{ node->get_value() };
                    std::shared_ptr<B const> const& board{ game_state1.get_board() };
                    std::vector<std::shared_ptr<M const>> const moves{ _game->find_all_moves(node) };
                    for (std::shared_ptr<M const> const& move : moves)
                    {
                        std::shared_ptr<B const> new_board{ move->apply(board) };
                        boards_generated++;
                        game_state<M, B> new_game_state{ move, new_board };
                        std::shared_ptr<linked_node<game_state<M, B>> const> new_node{ std::make_shared<linked_node<game_state<M, B>>>(new_game_state, node) };
                        bool broken{ false };
                        for (std::shared_ptr<move_filter<M, B> const> const& filter : _game_filters)
                        {
                            if (filter->should_filter(new_node))
                            {
                                std::string const statistics_key{ filter->get_statistics_key() };
                                moves_pruned_total++;
                                moves_pruned.at(statistics_key)++;
                                broken = true;
                                break;
                            }
                        }
                        if (broken)
                        {
                            continue;
                        }

                        if (_game->is_win(new_node))
                        {
                            add_win(new_node);
                        }
                        else
                        {
                            queue_node(new_node);
                        }
                    }
                }

            private:
                std::shared_ptr<silnith::game::game<M, B> const> const _game;
                std::span<std::shared_ptr<move_filter<M, B> const> const> const _game_filters;
                std::atomic_ullong game_states_examined{ 0 };
                std::atomic_ullong boards_generated{ 0 };
                std::atomic_ullong moves_pruned_total{ 0 };
                std::map<std::string const, std::atomic_ullong> moves_pruned{};
            };
        }
    }
}
