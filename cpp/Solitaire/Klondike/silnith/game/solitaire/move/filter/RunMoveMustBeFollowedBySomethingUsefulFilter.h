#pragma once

#include <silnith/game/game_state.h>
#include <silnith/game/linked_node.h>
#include <silnith/game/move_filter.h>

#include <silnith/game/solitaire/board.h>

#include <silnith/game/solitaire/move/solitaire_move.h>

#include <memory>
#include <string>

namespace silnith
{
    namespace game
    {
        namespace solitaire
        {
            namespace move
            {
                namespace filter
                {
                    /// <summary>
                    /// If a run is moved from one column to another, one of two conditions must hold true.
                    /// Either the entire run is moved, exposing a new face-down card (or emptying a column),
                    /// or the subsequent move must involve the existing face-up card exposed by moving the run.
                    /// </summary>
                    class RunMoveMustBeFollowedBySomethingUsefulFilter : public move_filter<solitaire_move, board>
                    {
                    public:
                        RunMoveMustBeFollowedBySomethingUsefulFilter(void) = default;
                        RunMoveMustBeFollowedBySomethingUsefulFilter(RunMoveMustBeFollowedBySomethingUsefulFilter const&) = default;
                        RunMoveMustBeFollowedBySomethingUsefulFilter& operator=(RunMoveMustBeFollowedBySomethingUsefulFilter const&) = default;
                        RunMoveMustBeFollowedBySomethingUsefulFilter(RunMoveMustBeFollowedBySomethingUsefulFilter&&) noexcept = default;
                        RunMoveMustBeFollowedBySomethingUsefulFilter& operator=(RunMoveMustBeFollowedBySomethingUsefulFilter&&) noexcept = default;
                        virtual ~RunMoveMustBeFollowedBySomethingUsefulFilter(void) = default;

                        /// <inheritdoc/>
                        [[nodiscard]]
                        virtual std::string get_statistics_key() const override;

                        /// <inheritdoc/>
                        [[nodiscard]]
                        virtual bool should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>>> const& game_state_history) const override;

                    private:
                    };
                }
            }
        }
    }
}
