#include <silnith/game/solitaire/move/ColumnToFoundationMove.h>

#include <stdexcept>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> ColumnToFoundationMove::find_moves(board const& b)
    {
        vector<shared_ptr<solitaire_move>> moves{};
        for (size_t index{ 0 }; index < board::num_columns; index++)
        {
            column const& source_column{ b.get_column(index) };
            if (source_column.has_face_up_cards())
            {
                card const& top_card{ source_column.get_top_card() };
                if (b.can_add_to_foundation(top_card))
                {
                    moves.emplace_back(make_shared<ColumnToFoundationMove>(index, top_card));
                }
            }
        }
        return moves;
    }

    ColumnToFoundationMove::ColumnToFoundationMove(size_t source_column_index, card const& c)
        : source_column_index{ source_column_index },
        _card{ c }
    {
        if (source_column_index > board::num_columns)
        {
            throw out_of_range("Source column "s + std::to_string(source_column_index) + " is out of range."s);
        }
    }

    ColumnToFoundationMove::ColumnToFoundationMove(size_t source_column_index, board const& b)
        : ColumnToFoundationMove(source_column_index, b.get_column(source_column_index).get_top_card())
    {}

    ColumnToFoundationMove::ColumnToFoundationMove(size_t source_column_index, shared_ptr<board> const& b)
        : ColumnToFoundationMove(source_column_index, *b)
    {}

    bool ColumnToFoundationMove::has_cards(void) const
    {
        return true;
    }

    vector<card> ColumnToFoundationMove::get_cards(void) const
    {
        return vector<card>{ _card };
    }

    bool ColumnToFoundationMove::is_stock_pile_modification(void) const
    {
        return false;
    }

    bool ColumnToFoundationMove::is_stock_pile_advance(void) const
    {
        return false;
    }

    bool ColumnToFoundationMove::is_stock_pile_recycle(void) const
    {
        return false;
    }

    bool ColumnToFoundationMove::is_from_stock_pile(void) const
    {
        return false;
    }

    bool ColumnToFoundationMove::is_from_foundation(void) const
    {
        return false;
    }

    bool ColumnToFoundationMove::is_from_column(void) const
    {
        return true;
    }

    bool ColumnToFoundationMove::is_from_column(size_t column_index) const
    {
        return column_index == source_column_index;
    }

    size_t ColumnToFoundationMove::get_source_column_index(void) const
    {
        return source_column_index;
    }

    bool ColumnToFoundationMove::is_to_foundation(void) const
    {
        return true;
    }

    bool ColumnToFoundationMove::is_to_column(void) const
    {
        return false;
    }

    bool ColumnToFoundationMove::is_to_column(size_t column_index) const
    {
        return false;
    }

    size_t ColumnToFoundationMove::get_destination_column_index(void) const
    {
        throw invalid_argument("Not a move to a column.");
    }

    shared_ptr<board const> ColumnToFoundationMove::apply(shared_ptr<board const> const& b) const
    {
        column const& source_column{ b->get_column(source_column_index) };
        pair<card, column> pair{ source_column.extract_card() };
        card moved_card{ pair.first };
        column new_column{ pair.second };

        vector<column> new_columns{};
        for (size_t index{ 0 }; index < board::num_columns; index++)
        {
            if (index == source_column_index)
            {
                new_columns.emplace_back(new_column);
            }
            else
            {
                new_columns.emplace_back(b->get_column(index));
            }
        }

        vector<card> const& stock_pile{ b->get_stock_pile() };
        size_t stock_pile_index{ b->get_stock_pile_index() };
        map<suit, vector<card>> const& new_foundation{ b->get_foundation_plus_card(moved_card) };

        return make_shared<board>(new_columns, stock_pile, stock_pile_index, new_foundation);
    }

    bool ColumnToFoundationMove::operator==(ColumnToFoundationMove const& other) const
    {
        return source_column_index == other.source_column_index
            && _card == other._card;
    }

    string to_string(ColumnToFoundationMove const& move)
    {
        return "Move "s
            + to_string(move.get_cards().front())
            + " from column "s
            + std::to_string(move.get_source_column_index())
            + " to foundation."s;
    }

    wstring to_wstring(ColumnToFoundationMove const& move)
    {
        return L"Move "s
            + to_wstring(move.get_cards().front())
            + L" from column "s
            + std::to_wstring(move.get_source_column_index())
            + L" to foundation."s;
    }

    ostream& operator<<(ostream& out, ColumnToFoundationMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, ColumnToFoundationMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
