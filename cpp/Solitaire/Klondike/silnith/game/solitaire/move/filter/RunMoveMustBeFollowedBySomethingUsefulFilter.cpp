#include <silnith/game/solitaire/move/filter/RunMoveMustBeFollowedBySomethingUsefulFilter.h>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move::filter
{
    string RunMoveMustBeFollowedBySomethingUsefulFilter::get_statistics_key() const
    {
        return "Run Move Must Be Useful"s;
    }

    bool RunMoveMustBeFollowedBySomethingUsefulFilter::should_filter(std::shared_ptr<linked_node<game_state<solitaire_move, board>>> const& game_state_history) const
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
        shared_ptr<board> previous_board{ previous_game_state.get_board() };
        /*
         * This is the board AFTER the move has been applied!
         */

        if (previous_move->is_from_column())
        {
            // check whether the previous run move used up all the cards
            // if it did, everything is fine

            if (iterator == end)
            {
                /*
                 * This can only happen at the very beginning of the game.
                 * In that case, this filter is not helpful, so just let everything pass.
                 */
                return false;
            }
            game_state<solitaire_move, board> game_state_two_steps_back{ *iterator };
            iterator++;
            shared_ptr<board> board_two_steps_back{ game_state_two_steps_back.get_board() };

            /*
             * Check whether the previous move took all the available cards from the source column.
             */
            size_t source_column_index{ previous_move->get_source_column_index() };
            size_t number_of_moved_cards{ previous_move->get_cards().size() };
            if (number_of_moved_cards == board_two_steps_back->get_column(source_column_index).get_number_of_face_up_cards())
            {
                /*
                 * The previous move took all face-up cards from the source column.
                 * Therefore, it is not suspicious.
                 * (Shenanigans with the king are handled by other filters.)
                 */
                return false;
            }
            else
            {
                /*
                 * The previous move took only a portion of the available run.
                 * Therefore, we want to make sure the top-most card NOT taken
                 * is involved with the current move.  Otherwise, the previous
                 * move has no value.
                 */
                deck::card card_exposed_by_run_move{ previous_board->get_column(source_column_index).get_top_card() };
                if (vector<deck::card>{ card_exposed_by_run_move } == current_move->get_cards())
                {
                    // This move uses the exposed card, meaning the previous run move is allowed.
                    // The makes sure the move uses ONLY the exposed card, not as one of a run.
                    // TODO: Should the only allowed follow-up move be column to foundation?
                    return false;
                }
                else
                {
                    /*
                     * This move does not use the exposed card, so filter the sequence of moves.
                     */
                    return true;
                }
            }
        }
        else
        {
            /*
             * Not interested in any scenario except where the second-most-recent move is moving a run from one column to another.
             */
            return false;
        }
    }
}
