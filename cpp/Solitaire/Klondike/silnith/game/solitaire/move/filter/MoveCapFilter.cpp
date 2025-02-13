#include <silnith/game/solitaire/move/filter/MoveCapFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    MoveCapFilter::MoveCapFilter(size_t cap)
        : move_cap{ cap },
        key{ "Move Cap of "s + std::to_string(cap) }
    {}

    string MoveCapFilter::get_statistics_key() const
    {
        return key;
    }

    bool MoveCapFilter::should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>>> const& game_state_history) const
    {
        linked_node<game_state<solitaire_move, board>>::const_iterator iterator{ game_state_history->cbegin() };
        linked_node<game_state<solitaire_move, board>>::const_iterator end{ game_state_history->cend() };
        size_t size{ 0 };
        while (iterator != end)
        {
            iterator++;
            size++;
        }
        return size > move_cap;
    }
}
