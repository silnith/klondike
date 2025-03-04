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
                    /// A move filter that caps the total game length.
                    /// </summary>
                    /// <remarks>
                    /// <para>
                    /// Realistically there is a finite limit to the number of moves it takes
                    /// to win a game of Klondike solitaire.  SO searching a tree to a depth
                    /// greater than that number is unproductive.
                    /// </para>
                    /// </remarks>
                    class MoveCapFilter : public move_filter<solitaire_move, board>
                    {
                    public:
                        MoveCapFilter(void) = default;
                        MoveCapFilter(MoveCapFilter const&) = default;
                        MoveCapFilter& operator=(MoveCapFilter const&) = default;
                        MoveCapFilter(MoveCapFilter&&) noexcept = default;
                        MoveCapFilter& operator=(MoveCapFilter&&) noexcept = default;
                        virtual ~MoveCapFilter(void) = default;

                        /// <summary>
                        /// Constructs a new filter with the given move cap.
                        /// </summary>
                        /// <param name="move_cap">The maximum number of moves to allow.</param>
                        explicit MoveCapFilter(std::size_t move_cap);

                        /// <inheritdoc/>
                        [[nodiscard]]
                        virtual std::string get_statistics_key() const override;

                        /// <inheritdoc/>
                        [[nodiscard]]
                        virtual bool should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const override;

                    private:
                        std::size_t const move_cap;
                        std::string const key;
                    };
                }
            }
        }
    }
}
