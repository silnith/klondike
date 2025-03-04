#pragma once

#include <silnith/game/game.h>
#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move.h>

#include <silnith/game/search/game_tree_searcher.h>

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
    namespace game::search
    {
        template<class M, class B>
            requires (std::is_base_of_v<move<B>, M>)
        class sequential_depth_first_search : public game_tree_searcher<M, B>
        {
        public:
            sequential_depth_first_search(void) = default;
            sequential_depth_first_search(sequential_depth_first_search const&) = default;
            sequential_depth_first_search& operator=(sequential_depth_first_search const&) = default;
            sequential_depth_first_search(sequential_depth_first_search&&) noexcept = default;
            sequential_depth_first_search& operator=(sequential_depth_first_search&&) noexcept = default;
            virtual ~sequential_depth_first_search(void) = default;

            explicit sequential_depth_first_search(std::shared_ptr<silnith::game::game<M, B> const> game_engine, silnith::game::game_state<M, B> const& initial_game_state)
                : game_tree_searcher<M, B>{ game_engine }
            {
                _stack.emplace(std::make_shared<silnith::game::linked_node<silnith::game::game_state<M, B>>>(initial_game_state));
            }

            [[nodiscard]]
            std::list<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>> const>> search(void)
            {
                while (!_stack.empty())
                {
                    std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>> const> node{ _stack.top() };
                    _stack.pop();
                    this->examine_node(node);
                }

                return _wins;
            }

        protected:
            /// <inheritdoc/>
            [[nodiscard]]
            virtual std::size_t queue_size(void) const override
            {
                return _stack.size();
            }

            /// <inheritdoc/>
            [[nodiscard]]
            virtual std::size_t win_count(void) const override
            {
                return _wins.size();
            }

            /// <inheritdoc/>
            virtual void queue_node(std::shared_ptr<linked_node<game_state<M, B>> const> const& node) override
            {
                _stack.emplace(node);
            }

            /// <inheritdoc/>
            virtual void add_win(std::shared_ptr<linked_node<game_state<M, B>> const> const& node) override
            {
                _wins.emplace_back(node);
            }

        private:
            std::stack<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>> const>> _stack{};
            std::list<std::shared_ptr<silnith::game::linked_node<silnith::game::game_state<M, B>> const>> _wins{};
        };
    }
}
