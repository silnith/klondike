#include <silnith/game/solitaire/move/filter/KingMoveMustExposeFaceDownCardFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string KingMoveMustExposeFaceDownCardFilter::get_statistics_key() const
    {
        return "King Move Must Expose Card"s;
    }

    bool KingMoveMustExposeFaceDownCardFilter::should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>>> const& game_state_history) const
    {
        linked_node<game_state<solitaire_move, board>>::const_iterator iterator{ game_state_history->cbegin() };
        linked_node<game_state<solitaire_move, board>>::const_iterator end{ game_state_history->cend() };
        game_state<solitaire_move, board> current_game_state{ *iterator };
        iterator++;
        shared_ptr<solitaire_move> current_move{ current_game_state.get_move() };
        shared_ptr<board> current_board{ current_game_state.get_board() };
        /*
         * The current board is the state after the move has been applied.
         */

        if (current_move->is_from_column() && current_move->is_to_column())
        {
            vector<deck::card> run{ current_move->get_cards() };
            deck::card first_card{ run.front() };
            if (first_card.get_value() == deck::value::king)
            {
                /*
                 * This is the column after the move.
                 */
                column const& source_column{ current_board->get_column(current_move->get_source_column_index()) };
                if (source_column.has_face_up_cards())
                {
                    /*
                     * Something was left behind, therefore the move has value.
                     */
                    return false;
                }
                else
                {
                    /*
                     * The king was moved and now the column is empty.
                     * Since we know the destination must have been an empty column,
                     * we moved from an empty column to an empty column.
                     * Therefore, the move has no purpose.
                     */
                    return true;
                }
            }
            else
            {
                /*
                 * This filter only cares about runs that start wtih a king.
                 */
                return false;
            }
        }
        else
        {
            /*
             * Only interested in moves from a column to another column
             * where the run begins with a king.
             */
            return false;
        }
    }
}
