#include <silnith/game/solitaire/move/filter/DrawFromStockPileFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string DrawFromStockPileFilter::get_statistics_key() const
    {
        return "Draw From Stock Pile Must Follow Advance"s;
    }

    bool DrawFromStockPileFilter::should_filter(shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const
    {
        linked_node<game_state<solitaire_move, board>>::const_iterator iterator{ game_state_history->cbegin() };
        linked_node<game_state<solitaire_move, board>>::const_iterator end{ game_state_history->cend() };
        game_state<solitaire_move, board> const& current_game_state{ *iterator };
        iterator++;
        shared_ptr<solitaire_move const> const& current_move{ current_game_state.get_move() };
        shared_ptr<board const> const& current_board{ current_game_state.get_board() };

        if (current_move->is_from_stock_pile())
        {
            // continue
        }
        else
        {
            // This filter does not apply.
            return false;
        }

        if (iterator == end)
        {
            /*
             * This can only happen at the very beginning of the game.
             * In that case, this filter is not helpful, so just let everything pass.
             */
            return false;
        }

        shared_ptr<solitaire_move const> previous_move{ (*iterator).get_move() };
        iterator++;
        // There may be a sequence of draws from the stock pile.
        while (previous_move->is_from_stock_pile() || previous_move->is_from_foundation())
        {
            /*
             * Walk backwards.
             * Ignoring moves from the foundation is a special-case.
             * In general moves from the foundation are filtered, but
             * they are allowed to provide a destination for column-to-column
             * moves or stock pile draws in the event that no other destination
             * is available.
             */
            previous_move = (*iterator).get_move();
            iterator++;
        }
        // Theoretically, it should only be possible for the previous move to be a stock pile advance.
        // The recycle should mkae it impossible to draw from the stock pile.
        if (previous_move->is_stock_pile_modification())
        {
            /*
             * This is accepetable, no need to filter.
             */
            return false;
        }
        else
        {
            /*
             * The previous move did not modify the stock pile,
             * so drawing from the stock pile is silly.
             */
            return true;
        }
    }
}
