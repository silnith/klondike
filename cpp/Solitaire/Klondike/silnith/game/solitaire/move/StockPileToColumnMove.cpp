#include <silnith/game/solitaire/move/StockPileToColumnMove.h>

#include <silnith/game/deck/card.h>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> StockPileToColumnMove::find_moves(board const& board)
    {
        if (board.get_stock_pile_index() > 0)
        {
            vector<shared_ptr<solitaire_move>> moves{};
            card stock_pile_card{ board.get_stock_pile_card() };
            vector<card> run{};
            run.emplace_back(stock_pile_card);
            for (size_t index{ 0 }; index < board::num_columns; index++)
            {
                if (board.get_column(index).can_add_run(run))
                {
                    moves.emplace_back(
                        static_cast<shared_ptr<solitaire_move>>(
                            make_shared<StockPileToColumnMove>(index, board)));
                }
            }
            return moves;
        }
        else
        {
            return vector<shared_ptr<solitaire_move>>{};
        }
    }

    StockPileToColumnMove::StockPileToColumnMove(size_t source_index, size_t destination_column_index, card const& card)
        : source_index{ source_index },
        destination_column_index{ destination_column_index },
        _card{ card }
    {}

    StockPileToColumnMove::StockPileToColumnMove(size_t destination_column_index, board const& board)
        : StockPileToColumnMove(board.get_stock_pile_index(), destination_column_index, board.get_stock_pile_card())
    {}

    StockPileToColumnMove::StockPileToColumnMove(size_t destination_column_index, shared_ptr<board> const& board)
        : StockPileToColumnMove(destination_column_index, *board)
    {}

    size_t StockPileToColumnMove::get_source_index(void) const
    {
        return source_index;
    }

    bool StockPileToColumnMove::has_cards(void) const
    {
        return true;
    }

    vector<card> StockPileToColumnMove::get_cards(void) const
    {
        vector<card> cards{};
        cards.emplace_back(_card);
        return cards;
    }

    bool StockPileToColumnMove::is_stock_pile_modification(void) const
    {
        return false;
    }

    bool StockPileToColumnMove::is_stock_pile_advance(void) const
    {
        return false;
    }

    bool StockPileToColumnMove::is_stock_pile_recycle(void) const
    {
        return false;
    }

    bool StockPileToColumnMove::is_from_stock_pile(void) const
    {
        return true;
    }

    bool StockPileToColumnMove::is_from_foundation(void) const
    {
        return false;
    }

    bool StockPileToColumnMove::is_from_column(void) const
    {
        return false;
    }

    bool StockPileToColumnMove::is_from_column(std::size_t column_index) const
    {
        return false;
    }

    size_t StockPileToColumnMove::get_source_column_index(void) const
    {
        throw invalid_argument("Not a move from a column.");
    }

    bool StockPileToColumnMove::is_to_foundation(void) const
    {
        return false;
    }

    bool StockPileToColumnMove::is_to_column(void) const
    {
        return true;
    }

    bool StockPileToColumnMove::is_to_column(size_t column_index) const
    {
        return column_index == destination_column_index;
    }

    size_t StockPileToColumnMove::get_destination_column_index(void) const
    {
        return destination_column_index;
    }

    shared_ptr<board> StockPileToColumnMove::apply(shared_ptr<board> const& b) const
    {
        pair<card, vector<card>> pair{ b->extract_stock_pile_card() };
        card drawn_card{ pair.first };
        vector<card> new_stock_pile{ pair.second };

        size_t stock_pile_index{ b->get_stock_pile_index() };
        size_t new_stock_pile_index{ stock_pile_index - 1 };

        vector<column> columns{ b->get_columns() };
        column old_column{ columns.at(destination_column_index) };
        column new_column{ old_column.with_card(drawn_card) };

        vector<column> new_columns{};
        for (size_t index{ 0 }; index < board::num_columns; index++)
        {
            if (index == destination_column_index)
            {
                new_columns.emplace_back(new_column);
            }
            else
            {
                new_columns.emplace_back(columns.at(index));
            }
        }

        return make_shared<board>(
            new_columns,
            new_stock_pile,
            new_stock_pile_index,
            b->get_foundation());
    }

    bool StockPileToColumnMove::operator==(StockPileToColumnMove const& other) const
    {
        return source_index == other.source_index
            && destination_column_index == other.destination_column_index
            && _card == other._card;
    }

    string to_string(StockPileToColumnMove const& move)
    {
        return "Move "s
            + to_string(move.get_cards().front())
            + " from stock pile index "s
            + std::to_string(move.get_source_index())
            + " to column "s
            + std::to_string(move.get_destination_column_index())
            + "."s;
    }

    wstring to_wstring(StockPileToColumnMove const& move)
    {
        return L"Move "s
            + to_wstring(move.get_cards().front())
            + L" from stock pile index "s
            + std::to_wstring(move.get_source_index())
            + L" to column "s
            + std::to_wstring(move.get_destination_column_index())
            + L"."s;
    }

    ostream& operator<<(ostream& out, StockPileToColumnMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, StockPileToColumnMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
