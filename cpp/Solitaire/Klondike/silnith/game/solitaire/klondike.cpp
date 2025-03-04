#include <silnith/game/solitaire/klondike.h>

#include <silnith/game/solitaire/move/ColumnToColumnMove.h>
#include <silnith/game/solitaire/move/ColumnToFoundationMove.h>
#include <silnith/game/solitaire/move/FoundationToColumnMove.h>
#include <silnith/game/solitaire/move/StockPileAdvanceMove.h>
#include <silnith/game/solitaire/move/StockPileRecycleMove.h>
#include <silnith/game/solitaire/move/StockPileToColumnMove.h>
#include <silnith/game/solitaire/move/StockPileToFoundationMove.h>

#include <silnith/game/solitaire/move/filter/BoardCycleFilter.h>
#include <silnith/game/solitaire/move/filter/DrawFromFoundationMustBeUsefulFilter.h>
#include <silnith/game/solitaire/move/filter/DrawFromStockPileFilter.h>
#include <silnith/game/solitaire/move/filter/KingMoveMustExposeFaceDownCardFilter.h>
#include <silnith/game/solitaire/move/filter/MoveCapFilter.h>
#include <silnith/game/solitaire/move/filter/RunMoveMustBeFollowedBySomethingUsefulFilter.h>
#include <silnith/game/solitaire/move/filter/StockPileAdvanceMustBeFollowedBySomethingUsefulFilter.h>
#include <silnith/game/solitaire/move/filter/StockPileRecycleMustBeFollowedByAdvanceFilter.h>

using namespace silnith::game;
using namespace silnith::game::deck;
using namespace silnith::game::solitaire;
using namespace silnith::game::solitaire::move;

using namespace std;

klondike::klondike(void)
    : filters{
        make_shared<move::filter::MoveCapFilter>(150),
        make_shared<move::filter::KingMoveMustExposeFaceDownCardFilter>(),
        make_shared<move::filter::StockPileRecycleMustBeFollowedByAdvanceFilter>(),
        make_shared<move::filter::StockPileAdvanceMustBeFollowedBySomethingUsefulFilter>(),
        make_shared<move::filter::DrawFromFoundationMustBeUsefulFilter>(),
        make_shared<move::filter::DrawFromStockPileFilter>(),
        make_shared<move::filter::RunMoveMustBeFollowedBySomethingUsefulFilter>(),
        make_shared<move::filter::BoardCycleFilter>(),
    }
{}

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

bool klondike::is_win(shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& node_ptr) const
{
    linked_node<game_state<solitaire_move, board>> const& node{ *node_ptr };
    game_state<solitaire_move, board> const& game_state{ node.get_value() };
    shared_ptr<board const> const& b{ game_state.get_board() };
    return is_win(*b);
}

vector<shared_ptr<solitaire_move const>> klondike::find_all_moves(
    shared_ptr<linked_node<game_state<solitaire_move, board>> const> const& game_state_history) const
{
    game_state<solitaire_move, board> const& game_state{ game_state_history->get_value() };
    shared_ptr<board const> const& board_ptr{ game_state.get_board() };
    board const& b{ *board_ptr };

    vector<shared_ptr<solitaire_move const>> moves{};
    for (shared_ptr<solitaire_move const> move : StockPileRecycleMove::find_moves(b))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move const> move : StockPileAdvanceMove::find_moves(draw_advance, b))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move const> move : FoundationToColumnMove::find_moves(b))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move const> move : ColumnToColumnMove::find_moves(b))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move const> move : StockPileToColumnMove::find_moves(b))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move const> move : ColumnToFoundationMove::find_moves(b))
    {
        moves.emplace_back(move);
    }
    for (shared_ptr<solitaire_move const> move : StockPileToFoundationMove::find_moves(b))
    {
        moves.emplace_back(move);
    }
    return moves;
}

span<shared_ptr<move_filter<solitaire_move, board> const> const> klondike::get_filters(void) const
{
    return filters;
}
