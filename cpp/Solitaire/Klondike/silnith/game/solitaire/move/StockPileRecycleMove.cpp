#include <silnith/game/solitaire/move/StockPileRecycleMove.h>

#include <exception>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> StockPileRecycleMove::find_moves(board const& board)
    {
        if (board.can_recycle_stock_pile())
        {
            shared_ptr<solitaire_move> move{ make_shared<StockPileRecycleMove>(board) };
            return vector<shared_ptr<solitaire_move>>{ move };
        }
        else
        {
            return vector<shared_ptr<solitaire_move>>{};
        }
    }

    StockPileRecycleMove::StockPileRecycleMove(size_t source_index) : source_index{ source_index }
    {}

    StockPileRecycleMove::StockPileRecycleMove(board const& board) : StockPileRecycleMove{ board.get_stock_pile_index() }
    {}

    StockPileRecycleMove::StockPileRecycleMove(shared_ptr<board> board) : StockPileRecycleMove{ *board }
    {}

    size_t StockPileRecycleMove::get_source_index(void) const
    {
        return source_index;
    }

    bool StockPileRecycleMove::has_cards(void) const
    {
        return false;
    }

    vector<card> StockPileRecycleMove::get_cards(void) const
    {
        return {};
    }

    bool StockPileRecycleMove::is_stock_pile_modification(void) const
    {
        return true;
    }

    bool StockPileRecycleMove::is_stock_pile_advance(void) const
    {
        return false;
    }

    bool StockPileRecycleMove::is_stock_pile_recycle(void) const
    {
        return true;
    }

    bool StockPileRecycleMove::is_from_stock_pile(void) const
    {
        return false;
    }

    bool StockPileRecycleMove::is_from_foundation(void) const
    {
        return false;
    }

    bool StockPileRecycleMove::is_from_column(void) const
    {
        return false;
    }

    bool StockPileRecycleMove::is_from_column(std::size_t column_index) const
    {
        return false;
    }

    size_t StockPileRecycleMove::get_source_column_index(void) const
    {
        throw invalid_argument("Not a move from a column.");
    }

    bool StockPileRecycleMove::is_to_foundation(void) const
    {
        return false;
    }

    bool StockPileRecycleMove::is_to_column(void) const
    {
        return false;
    }

    bool StockPileRecycleMove::is_to_column(std::size_t column_index) const
    {
        return false;
    }

    size_t StockPileRecycleMove::get_destination_column_index(void) const
    {
        throw invalid_argument("Not a move to a column.");
    }

    shared_ptr<board> StockPileRecycleMove::apply(shared_ptr<board> const& b) const
    {
        return make_shared<board>(
            b->get_columns(),
            b->get_stock_pile(),
            0,
            b->get_foundation());
    }

    bool StockPileRecycleMove::operator==(StockPileRecycleMove const& other) const
    {
        return source_index == other.source_index;
    }

    string to_string(StockPileRecycleMove const& move)
    {
        return "Recycle stock pile from index "s
            + std::to_string(move.get_source_index())
            + " to the beginning."s;
    }

    wstring to_wstring(StockPileRecycleMove const& move)
    {
        return L"Recycle stock pile from index "s
            + std::to_wstring(move.get_source_index())
            + L" to the beginning."s;
    }

    ostream& operator<<(ostream& out, StockPileRecycleMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, StockPileRecycleMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
