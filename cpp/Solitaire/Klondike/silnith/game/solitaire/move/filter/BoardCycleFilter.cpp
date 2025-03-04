#include <silnith/game/solitaire/move/filter/BoardCycleFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string BoardCycleFilter::get_statistics_key() const
    {
        return "Board Cycle"s;
    }

    bool BoardCycleFilter::should_filter(shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const
    {
        linked_node<game_state<solitaire_move, board>>::const_iterator iterator{ game_state_history->cbegin() };
        linked_node<game_state<solitaire_move, board>>::const_iterator end{ game_state_history->cend() };
        game_state<solitaire_move, board> const& current_game_state{ *iterator };
        iterator++;
        shared_ptr<board const> const& current_board{ current_game_state.get_board() };

        while (iterator != end)
        {
            game_state<solitaire_move, board> const& game_state{ *iterator };
            iterator++;
            shared_ptr<board const> const& past_board{ game_state.get_board() };
            if (*current_board == *past_board)
            {
                return true;
            }
        }
        return false;
    }
}
