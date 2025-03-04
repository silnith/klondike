#include <silnith/game/solitaire/move/StockPileToFoundationMove.h>

#include <silnith/game/deck/card.h>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> StockPileToFoundationMove::find_moves(board const& board)
    {
        if (board.get_stock_pile_index() > 0)
        {
            if (board.can_add_to_foundation(board.get_stock_pile_card()))
            {
                return vector<shared_ptr<solitaire_move>>{ make_shared<StockPileToFoundationMove>(board) };
            }
        }
        return vector<shared_ptr<solitaire_move>>{};
    }

    StockPileToFoundationMove::StockPileToFoundationMove(size_t source_index, card const& card)
        : source_index{ source_index },
        _card{ card }
    {}

    StockPileToFoundationMove::StockPileToFoundationMove(board const& board)
        : StockPileToFoundationMove(board.get_stock_pile_index(), board.get_stock_pile_card())
    {}

    StockPileToFoundationMove::StockPileToFoundationMove(shared_ptr<board> const& board)
        : StockPileToFoundationMove(*board)
    {}

    size_t StockPileToFoundationMove::get_source_index(void) const
    {
        return source_index;
    }

    bool StockPileToFoundationMove::has_cards(void) const
    {
        return true;
    }

    vector<card> StockPileToFoundationMove::get_cards(void) const
    {
        return vector<card>{ _card };
    }

    bool StockPileToFoundationMove::is_stock_pile_modification(void) const
    {
        return true;
    }

    bool StockPileToFoundationMove::is_stock_pile_advance(void) const
    {
        return false;
    }

    bool StockPileToFoundationMove::is_stock_pile_recycle(void) const
    {
        return false;
    }

    bool StockPileToFoundationMove::is_from_stock_pile(void) const
    {
        return true;
    }

    bool StockPileToFoundationMove::is_from_foundation(void) const
    {
        return false;
    }

    bool StockPileToFoundationMove::is_from_column(void) const
    {
        return false;
    }

    bool StockPileToFoundationMove::is_from_column(std::size_t column_index) const
    {
        return false;
    }

    size_t StockPileToFoundationMove::get_source_column_index(void) const
    {
        throw invalid_argument("Not a move from a column.");
    }

    bool StockPileToFoundationMove::is_to_foundation(void) const
    {
        return true;
    }

    bool StockPileToFoundationMove::is_to_column(void) const
    {
        return false;
    }

    bool StockPileToFoundationMove::is_to_column(size_t column_index) const
    {
        return false;
    }

    size_t StockPileToFoundationMove::get_destination_column_index(void) const
    {
        throw invalid_argument("Not a move to a column.");
    }

    shared_ptr<board const> StockPileToFoundationMove::apply(shared_ptr<board const> const& b) const
    {
        pair<card, vector<card>> pair{ b->extract_stock_pile_card() };
        card drawn_card{ pair.first };
        vector<card> new_stock_pile{ pair.second };

        size_t stock_pile_index{ b->get_stock_pile_index() };
        size_t new_stock_pile_index{ stock_pile_index - 1 };

        map<suit, vector<card>> new_foundation{ b->get_foundation_plus_card(drawn_card) };

        return make_shared<board>(
            b->get_columns(),
            new_stock_pile,
            new_stock_pile_index,
            new_foundation);
    }

    bool StockPileToFoundationMove::operator==(StockPileToFoundationMove const& other) const
    {
        return source_index == other.source_index
            && _card == other._card;
    }

    string to_string(StockPileToFoundationMove const& move)
    {
        return "Move "s
            + to_string(move.get_cards().front())
            + " from stock pile index "s
            + std::to_string(move.get_source_index())
            + " to foundation."s;
    }

    wstring to_wstring(StockPileToFoundationMove const& move)
    {
        return L"Move "s
            + to_wstring(move.get_cards().front())
            + L" from stock pile index "s
            + std::to_wstring(move.get_source_index())
            + L" to foundation."s;
    }

    ostream& operator<<(ostream& out, StockPileToFoundationMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, StockPileToFoundationMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
