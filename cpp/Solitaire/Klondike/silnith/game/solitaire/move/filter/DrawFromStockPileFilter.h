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
                    /// Filters draw from stock pile moves if they do not follow a stock pile advance
                    /// or recycle.
                    /// </summary>
                    class DrawFromStockPileFilter : public move_filter<solitaire_move, board>
                    {
                    public:
                        DrawFromStockPileFilter(void) = default;
                        DrawFromStockPileFilter(DrawFromStockPileFilter const&) = default;
                        DrawFromStockPileFilter& operator=(DrawFromStockPileFilter const&) = default;
                        DrawFromStockPileFilter(DrawFromStockPileFilter&&) noexcept = default;
                        DrawFromStockPileFilter& operator=(DrawFromStockPileFilter&&) noexcept = default;
                        virtual ~DrawFromStockPileFilter(void) = default;

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
