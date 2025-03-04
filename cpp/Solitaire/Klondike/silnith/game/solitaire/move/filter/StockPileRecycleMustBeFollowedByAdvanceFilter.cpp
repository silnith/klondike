#include <silnith/game/solitaire/move/filter/StockPileRecycleMustBeFollowedByAdvanceFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string StockPileRecycleMustBeFollowedByAdvanceFilter::get_statistics_key() const
    {
        return "Stock Pile Recycle Must Be Followed By Advance"s;
    }

    bool StockPileRecycleMustBeFollowedByAdvanceFilter::should_filter(shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const
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

        // TODO: Replace instanceof check with polymorphism!
        if (previous_move->is_stock_pile_recycle())
        {
            if (current_move->is_stock_pile_modification())
            {
                /*
                 * This is acceptable, no need to filter.
                 */
                return false;
            }
            else
            {
                /*
                 * Why do something not involving the stock pile after recycling it?
                 */
                return true;
            }
        }
        else
        {
            /*
             * This filter doesn't apply.
             */
            return false;
        }
    }
}
