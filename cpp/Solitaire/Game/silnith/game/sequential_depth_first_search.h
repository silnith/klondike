#pragma once

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>

#include <concepts>
#include <list>
#include <memory>
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

            explicit sequential_depth_first_search(std::shared_ptr<game<M, B>> game, silnith::game::game_state<M, B> const& initial_game_state)
                : game_{ game }, stack_{}, wins_{}
            {
                stack_.emplace(std::make_shared<silnith::game::linked_node<silnith::game::game_state<M, B>>>(initial_game_state));
            }

            std::list<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>>> search()
            {
                std::vector<std::shared_ptr<silnith::game::move_filter<M, B>>> filters{ game_->get_filters() };

                while (!stack_.empty())
                {
                    std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>> game_state_history{ stack_.top() };
                    stack_.pop();
                    silnith::game::game_state<M, B> game_state{ game_state_history->get_value() };
                    std::shared_ptr<B> board{ game_state.get_board() };
                    std::vector<std::shared_ptr<M>> moves{ game_->find_all_moves(game_state_history) };
                    for (std::shared_ptr<M> move : moves)
                    {
                        std::shared_ptr<B> new_board{ move->apply(board) };
                        silnith::game::game_state<M, B> new_game_state{ move, new_board };
                        std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>> new_history{ std::make_shared<silnith::game::linked_node<silnith::game::game_state<M, B>>>(new_game_state, game_state_history) };
                        bool broken{ false };
                        for (std::shared_ptr<silnith::game::move_filter<M, B>> filter : filters)
                        {
                            if (filter->should_filter(new_history))
                            {
                                broken = true;
                                break;
                            }
                        }
                        if (broken)
                        {
                            continue;
                        }

                        if (game_->is_win(new_game_state))
                        {
                            wins_.push_back(new_history);
                        }
                        else
                        {
                            stack_.push(new_history);
                        }
                    }
                }
                return wins_;
            }

        private:
            std::shared_ptr<game<M, B>> const game_;
            std::stack<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>>> stack_;
            std::list<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>>>> wins_;
        };
    }
}
