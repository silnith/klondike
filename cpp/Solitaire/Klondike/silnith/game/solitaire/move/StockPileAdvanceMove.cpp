#include <silnith/game/solitaire/move/StockPileAdvanceMove.h>

#include <exception>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> StockPileAdvanceMove::find_moves(size_t stock_pile_advance, board const& board)
    {
        if (board.can_advance_stock_pile())
        {
            shared_ptr<solitaire_move> move{ make_shared<StockPileAdvanceMove>(stock_pile_advance, board) };
            return vector<shared_ptr<solitaire_move>>{ move };
        }
        else
        {
            return vector<shared_ptr<solitaire_move>>{};
        }
    }

    StockPileAdvanceMove::StockPileAdvanceMove(size_t beginning_index, size_t increment)
        : beginning_index{ beginning_index },
        increment{ increment }
    {
        if (increment < 1)
        {
            throw out_of_range("Increment must be positive.");
        }
    }

    StockPileAdvanceMove::StockPileAdvanceMove(size_t increment, board const& board)
        : StockPileAdvanceMove{ board.get_stock_pile_index(), increment }
    {}

    StockPileAdvanceMove::StockPileAdvanceMove(size_t increment, shared_ptr<board> const& b)
        : StockPileAdvanceMove{ increment, *b }
    {}

    size_t StockPileAdvanceMove::get_beginning_index(void) const
    {
        return beginning_index;
    }

    size_t StockPileAdvanceMove::get_increment(void) const
    {
        return increment;
    }

    bool StockPileAdvanceMove::has_cards(void) const
    {
        return false;
    }

    vector<card> StockPileAdvanceMove::get_cards(void) const
    {
        return {};
    }

    bool StockPileAdvanceMove::is_stock_pile_modification(void) const
    {
        return true;
    }

    bool StockPileAdvanceMove::is_stock_pile_advance(void) const
    {
        return true;
    }

    bool StockPileAdvanceMove::is_stock_pile_recycle(void) const
    {
        return false;
    }

    bool StockPileAdvanceMove::is_from_stock_pile(void) const
    {
        return false;
    }

    bool StockPileAdvanceMove::is_from_foundation(void) const
    {
        return false;
    }

    bool StockPileAdvanceMove::is_from_column(void) const
    {
        return false;
    }

    bool StockPileAdvanceMove::is_from_column(size_t column_index) const
    {
        return false;
    }

    size_t StockPileAdvanceMove::get_source_column_index(void) const
    {
        throw invalid_argument("Not a move from a column.");
    }

    bool StockPileAdvanceMove::is_to_foundation(void) const
    {
        return false;
    }

    bool StockPileAdvanceMove::is_to_column(void) const
    {
        return false;
    }

    bool StockPileAdvanceMove::is_to_column(size_t column_index) const
    {
        return false;
    }

    size_t StockPileAdvanceMove::get_destination_column_index(void) const
    {
        throw invalid_argument("Not a move to a column.");
    }

    shared_ptr<board> StockPileAdvanceMove::apply(shared_ptr<board> const& b) const
    {
        vector<card> const stock_pile{ b->get_stock_pile() };
        size_t const new_index{ min(b->get_stock_pile_index() + increment, stock_pile.size()) };
        return make_shared<board>(
            b->get_columns(),
            stock_pile,
            new_index,
            b->get_foundation());
    }

    bool StockPileAdvanceMove::operator==(StockPileAdvanceMove const& other) const
    {
        return beginning_index == other.beginning_index
            && increment == other.increment;
    }

    string to_string(StockPileAdvanceMove const& move)
    {
        return "Advance stock pile from "s
            + std::to_string(move.get_beginning_index())
            + " by "s
            + std::to_string(move.get_increment())
            + "."s;
    }

    wstring to_wstring(StockPileAdvanceMove const& move)
    {
        return L"Advance stock pile from "s
            + std::to_wstring(move.get_beginning_index())
            + L" by "s
            + std::to_wstring(move.get_increment())
            + L"."s;
    }

    ostream& operator<<(ostream& out, StockPileAdvanceMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, StockPileAdvanceMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
