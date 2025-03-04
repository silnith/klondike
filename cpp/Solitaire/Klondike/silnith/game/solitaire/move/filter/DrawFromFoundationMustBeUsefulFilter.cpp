#include <silnith/game/solitaire/move/filter/DrawFromFoundationMustBeUsefulFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string DrawFromFoundationMustBeUsefulFilter::get_statistics_key() const
    {
        return "Draw From Foundation Must Be Used"s;
    }

    bool DrawFromFoundationMustBeUsefulFilter::should_filter(shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const
    {
        linked_node<game_state<solitaire_move, board>>::const_iterator iterator{ game_state_history->cbegin() };
        linked_node<game_state<solitaire_move, board>>::const_iterator end{ game_state_history->cend() };
        game_state<solitaire_move, board> const& current_game_state{ *iterator };
        iterator++;
        shared_ptr<solitaire_move const> const& current_move{ current_game_state.get_move() };
        shared_ptr<board const> const& current_board{ current_game_state.get_board() };

        if (iterator == end)
        {
            /*
             * This can only happen at the very beginning of the game.
             * In that case, this filter is not helpful, so just let everything pass.
             */
            return false;
        }

        game_state<solitaire_move, board> const& previous_game_state{ *iterator };
        iterator++;
        shared_ptr<solitaire_move const> const& previous_move{ previous_game_state.get_move() };

        if (previous_move->is_from_foundation() && previous_move->is_to_column())
        {
            if (current_move->is_from_foundation())
            {
                /*
                 * Chained moves from the foundation could be for a purpose,
                 * so allow the chain to unfold.
                 */
                return false;
            }
            if (current_move->is_to_column(previous_move->get_destination_column_index()))
            {
                /*
                 * The current move puts a card on top of the card taken
                 * from the foundation, so the foundation move has value.
                 */
                return false;
            }
            else
            {
                /*
                 * The current move does not make use of the card taken
                 * from the foundation, so the foundation move was worthless.
                 */
                return true;
            }
        }
        else
        {
            /*
             * This filter only cares about moves that take cards from the foundation.
             */
            return false;
        }
    }
}
