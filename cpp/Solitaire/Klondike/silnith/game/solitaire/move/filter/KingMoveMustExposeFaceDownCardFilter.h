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
                    /// If a run starting with a king is moved to a new column, it must be
                    /// for the purpose of exposing a face-down card.
                    /// </summary>
                    class KingMoveMustExposeFaceDownCardFilter : public move_filter<solitaire_move, board>
                    {
                    public:
                        KingMoveMustExposeFaceDownCardFilter(void) = default;
                        KingMoveMustExposeFaceDownCardFilter(KingMoveMustExposeFaceDownCardFilter const&) = default;
                        KingMoveMustExposeFaceDownCardFilter& operator=(KingMoveMustExposeFaceDownCardFilter const&) = default;
                        KingMoveMustExposeFaceDownCardFilter(KingMoveMustExposeFaceDownCardFilter&&) noexcept = default;
                        KingMoveMustExposeFaceDownCardFilter& operator=(KingMoveMustExposeFaceDownCardFilter&&) noexcept = default;
                        virtual ~KingMoveMustExposeFaceDownCardFilter(void) = default;

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
