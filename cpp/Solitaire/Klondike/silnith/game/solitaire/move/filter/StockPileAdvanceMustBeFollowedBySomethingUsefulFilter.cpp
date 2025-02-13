#include <silnith/game/solitaire/move/filter/StockPileAdvanceMustBeFollowedBySomethingUsefulFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string StockPileAdvanceMustBeFollowedBySomethingUsefulFilter::get_statistics_key() const
    {
        return "Stock Pile Draw Must Follow Advance"s;
    }

    bool StockPileAdvanceMustBeFollowedBySomethingUsefulFilter::should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>>> const& game_state_history) const
    {
        linked_node<game_state<solitaire_move, board>>::const_iterator iterator{ game_state_history->cbegin() };
        linked_node<game_state<solitaire_move, board>>::const_iterator end{ game_state_history->cend() };
        game_state<solitaire_move, board> current_game_state{ *iterator };
        iterator++;
        shared_ptr<solitaire_move> current_move{ current_game_state.get_move() };
        shared_ptr<board> current_board{ current_game_state.get_board() };

        if (iterator == end)
        {
            /*
             * This can only happen at the very beginning of the game.
             * In that case, this filter is not helpful, so just let everything pass.
             */
            return false;
        }

        game_state<solitaire_move, board> previous_game_state{ *iterator };
        iterator++;
        shared_ptr<solitaire_move> previous_move{ previous_game_state.get_move() };

        if (previous_move->is_stock_pile_advance())
        {
            if (current_move->is_stock_pile_modification()
                || current_move->is_from_stock_pile()
                || current_move->is_from_foundation())
            {
                // This is fine.
                return false;
            }
            else
            {
                // No need to advance the stock pile simply to do something unrelated to it.
                return true;
            }
        }
        else
        {
            // Don't care.
            return false;
        }
    }
}
