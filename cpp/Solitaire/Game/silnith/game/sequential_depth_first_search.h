#pragma once

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>

#include <atomic>
#include <concepts>
#include <future>
#include <list>
#include <map>
#include <memory>
#include <ostream>
#include <stack>
#include <vector>

namespace silnith
{
    namespace game
    {
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        class sequential_depth_first_search
        {
        public:
            sequential_depth_first_search(void) = default;
            sequential_depth_first_search(sequential_depth_first_search const&) = default;
            sequential_depth_first_search& operator=(sequential_depth_first_search const&) = default;
            sequential_depth_first_search(sequential_depth_first_search&&) noexcept = default;
            sequential_depth_first_search& operator=(sequential_depth_first_search&&) noexcept = default;
            ~sequential_depth_first_search(void) = default;

            explicit sequential_depth_first_search(std::shared_ptr<silnith::game::game<M, B>> game_engine, silnith::game::game_state<M, B> const& initial_game_state)
                : _game{ game_engine }
            {
                for (std::shared_ptr<silnith::game::move_filter<M, B>> const& filter : _game->get_filters())
                {
                    moves_pruned.emplace(filter->get_statistics_key(), 0);
                }

                _stack.emplace(std::make_shared<silnith::game::linked_node<silnith::game::game_state<M, B>>>(initial_game_state));
            }

            void print_statistics(std::ostream& out)
            {
                using namespace std::literals::string_literals;
                out << "Nodes examined: "s << game_states_examined << std::endl;
                out << "Boards generated: "s << boards_generated << std::endl;
                out << "Moves pruned: "s << moves_pruned_total << std::endl;
                out << "Queue size: "s << _stack.size() << std::endl;
                out << "Wins: "s << _wins.size() << std::endl;
                for (std::shared_ptr<silnith::game::move_filter<M, B>> const& filter : _game->get_filters())
                {
                    std::string statistics_key{ filter->get_statistics_key() };
                    out << "Moves pruned by filter "s << statistics_key << ": "s << moves_pruned.at(statistics_key) << std::endl;
                }
            }

            long get_number_of_game_states_examined(void) const
            {
                return game_states_examined;
            }

            long get_boards_generated(void) const
            {
                return boards_generated;
            }

            [[nodiscard]]
            std::list<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>>> search()
            {
                std::span<std::shared_ptr<silnith::game::move_filter<M, B>> const> filters{ _game->get_filters() };

                while (!_stack.empty())
                {
                    std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>> game_state_history{ _stack.top() };
                    _stack.pop();
                    game_states_examined++;
                    silnith::game::game_state<M, B> const& game_state{ game_state_history->get_value() };
                    std::shared_ptr<B> const& board{ game_state.get_board() };
                    std::vector<std::shared_ptr<M>> moves{ _game->find_all_moves(game_state_history) };
                    for (std::shared_ptr<M> const& move : moves)
                    {
                        std::shared_ptr<B> new_board{ move->apply(board) };
                        boards_generated++;
                        silnith::game::game_state<M, B> new_game_state{ move, new_board };
                        std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>> new_history{ std::make_shared<silnith::game::linked_node<silnith::game::game_state<M, B>>>(new_game_state, game_state_history) };
                        bool broken{ false };
                        for (std::shared_ptr<silnith::game::move_filter<M, B>> const& filter : filters)
                        {
                            if (filter->should_filter(new_history))
                            {
                                moves_pruned_total++;
                                moves_pruned[filter->get_statistics_key()]++;
                                broken = true;
                                break;
                            }
                        }
                        if (broken)
                        {
                            continue;
                        }

                        if (_game->is_win(new_game_state))
                        {
                            _wins.push_back(new_history);
                        }
                        else
                        {
                            _stack.push(new_history);
                        }
                    }
                }

                return _wins;
            }

        private:
            std::shared_ptr<game<M, B>> const _game;
            std::stack<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>>> _stack{};
            std::list<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>>> _wins{};
            std::atomic_long game_states_examined{ 0 };
            std::atomic_long boards_generated{ 0 };
            std::atomic_long moves_pruned_total{ 0 };
            std::map<std::string, std::atomic_long> moves_pruned{};
        };
    }
}
