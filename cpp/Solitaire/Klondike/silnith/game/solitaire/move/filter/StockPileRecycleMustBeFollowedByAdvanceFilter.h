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
                    /// Filters moves that introduce a cycle into the board history.
                    /// </summary>
                    class StockPileRecycleMustBeFollowedByAdvanceFilter : public move_filter<solitaire_move, board>
                    {
                    public:
                        StockPileRecycleMustBeFollowedByAdvanceFilter(void) = default;
                        StockPileRecycleMustBeFollowedByAdvanceFilter(StockPileRecycleMustBeFollowedByAdvanceFilter const&) = default;
                        StockPileRecycleMustBeFollowedByAdvanceFilter& operator=(StockPileRecycleMustBeFollowedByAdvanceFilter const&) = default;
                        StockPileRecycleMustBeFollowedByAdvanceFilter(StockPileRecycleMustBeFollowedByAdvanceFilter&&) noexcept = default;
                        StockPileRecycleMustBeFollowedByAdvanceFilter& operator=(StockPileRecycleMustBeFollowedByAdvanceFilter&&) noexcept = default;
                        virtual ~StockPileRecycleMustBeFollowedByAdvanceFilter(void) = default;

                        /// <inheritdoc/>
                        [[nodiscard]]
                        virtual std::string get_statistics_key() const override;

                        /// <inheritdoc/>
                        [[nodiscard]]
                        virtual bool should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const override;

                    private:
                    };
                }
            }
        }
    }
}
