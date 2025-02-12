#include <silnith/game/solitaire/klondike.h>

#include <silnith/game/solitaire/move/ColumnToColumnMove.h>
#include <silnith/game/solitaire/move/ColumnToFoundationMove.h>
#include <silnith/game/solitaire/move/FoundationToColumnMove.h>
#include <silnith/game/solitaire/move/StockPileAdvanceMove.h>
#include <silnith/game/solitaire/move/StockPileRecycleMove.h>
#include <silnith/game/solitaire/move/StockPileToColumnMove.h>
#include <silnith/game/solitaire/move/StockPileToFoundationMove.h>

using namespace silnith::game;
using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;

bool klondike::is_win(board const& board) const
{
    constexpr size_t const thirteen{ static_cast<std::size_t>(value::king) };
    map<suit, vector<card>> const& foundation{ board.get_foundation() };
    if (foundation.at(suit::club).size() < thirteen)
    {
        return false;
    }
    if (foundation.at(suit::diamond).size() < thirteen)
    {
        return false;
    }
    if (foundation.at(suit::heart).size() < thirteen)
    {
        return false;
    }
    if (foundation.at(suit::spade).size() < thirteen)
    {
        return false;
    }
    return true;
}

bool klondike::is_win(game_state<solitaire_move, board> const& game_state) const
{
    shared_ptr<board> ptr{ game_state.get_board() };
    return is_win(*ptr);
}

vector<shared_ptr<solitaire_move>> klondike::find_all_moves(
    shared_ptr<linked_node<game_state<solitaire_move, board>>> const& game_state_history) const
{
    game_state<solitaire_move, board> game_state{ game_state_history->get_value() };
    shared_ptr<board> board_ptr{ game_state.get_board() };

    vector<shared_ptr<solitaire_move>> moves{};
    for (shared_ptr<solitaire_move> move : StockPileRecycleMove::find_moves(*board_ptr))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move> move : StockPileAdvanceMove::find_moves(draw_advance, *board_ptr))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move> move : FoundationToColumnMove::find_moves(*board_ptr))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move> move : ColumnToColumnMove::find_moves(*board_ptr))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move> move : StockPileToColumnMove::find_moves(*board_ptr))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move> move : ColumnToFoundationMove::find_moves(*board_ptr))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move> move : StockPileToFoundationMove::find_moves(*board_ptr))
    {
        moves.emplace_back(move);
    }
    return moves;
}

vector<shared_ptr<move_filter<solitaire_move, board>>> klondike::get_filters(void) const
{
    return {};
}
