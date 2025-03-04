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
                    /// If a card is drawn from the foundation, the following move must make use
                    /// of the card drawn.  Specifically, something must be put on top of it.
                    /// </summary>
                    class DrawFromFoundationMustBeUsefulFilter : public move_filter<solitaire_move, board>
                    {
                    public:
                        DrawFromFoundationMustBeUsefulFilter(void) = default;
                        DrawFromFoundationMustBeUsefulFilter(DrawFromFoundationMustBeUsefulFilter const&) = default;
                        DrawFromFoundationMustBeUsefulFilter& operator=(DrawFromFoundationMustBeUsefulFilter const&) = default;
                        DrawFromFoundationMustBeUsefulFilter(DrawFromFoundationMustBeUsefulFilter&&) noexcept = default;
                        DrawFromFoundationMustBeUsefulFilter& operator=(DrawFromFoundationMustBeUsefulFilter&&) noexcept = default;
                        virtual ~DrawFromFoundationMustBeUsefulFilter(void) = default;

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
