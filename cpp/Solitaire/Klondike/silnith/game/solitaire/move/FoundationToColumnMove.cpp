#include <silnith/game/solitaire/move/FoundationToColumnMove.h>

#include <silnith/game/deck/card.h>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    vector<shared_ptr<solitaire_move>> FoundationToColumnMove::find_moves(board const& b)
    {
        static vector<suit> const suits{
            suit::club,
            suit::diamond,
            suit::heart,
            suit::spade,
        };

        vector<shared_ptr<solitaire_move>> moves{};
        map<suit, vector<card>> const& foundation{ b.get_foundation() };
        for (suit s : suits)
        {
            vector<card> const& foundation_for_suit{ foundation.at(s) };
            if (!foundation_for_suit.empty())
            {
                card const& c{ foundation_for_suit.back() };
                vector<card> run{ c };
                for (size_t index{ 0 }; index < board::num_columns; index++)
                {
                    if (b.get_column(index).can_add_run(run))
                    {
                        moves.emplace_back(make_shared<FoundationToColumnMove>(index, c));
                    }
                }
            }
        }
        return moves;
    }

    FoundationToColumnMove::FoundationToColumnMove(size_t destination_column_index, card const& card)
        : destination_column_index{ destination_column_index },
        _card{ card }
    {}

    FoundationToColumnMove::FoundationToColumnMove(size_t destination_column_index, suit const& suit, board const& board)
        : FoundationToColumnMove(destination_column_index, board.get_top_of_foundation(suit))
    {}

    FoundationToColumnMove::FoundationToColumnMove(size_t destination_column_index, suit const& suit, shared_ptr<board> const& board)
        : FoundationToColumnMove(destination_column_index, suit, *board)
    {}

    bool FoundationToColumnMove::has_cards(void) const
    {
        return true;
    }

    vector<card> FoundationToColumnMove::get_cards(void) const
    {
        return vector<card>{ _card };
    }

    bool FoundationToColumnMove::is_stock_pile_modification(void) const
    {
        return false;
    }

    bool FoundationToColumnMove::is_stock_pile_advance(void) const
    {
        return false;
    }

    bool FoundationToColumnMove::is_stock_pile_recycle(void) const
    {
        return false;
    }

    bool FoundationToColumnMove::is_from_stock_pile(void) const
    {
        return true;
    }

    bool FoundationToColumnMove::is_from_foundation(void) const
    {
        return true;
    }

    bool FoundationToColumnMove::is_from_column(void) const
    {
        return false;
    }

    bool FoundationToColumnMove::is_from_column(std::size_t column_index) const
    {
        return false;
    }

    size_t FoundationToColumnMove::get_source_column_index(void) const
    {
        throw invalid_argument("Not a move from a column.");
    }

    bool FoundationToColumnMove::is_to_foundation(void) const
    {
        return false;
    }

    bool FoundationToColumnMove::is_to_column(void) const
    {
        return true;
    }

    bool FoundationToColumnMove::is_to_column(size_t column_index) const
    {
        return column_index == destination_column_index;
    }

    size_t FoundationToColumnMove::get_destination_column_index(void) const
    {
        return destination_column_index;
    }

    shared_ptr<board const> FoundationToColumnMove::apply(shared_ptr<board const> const& b) const
    {
        pair<card, map<suit, vector<card>>> pair{ b->extract_card_from_foundation(_card.get_suit()) };
        card drawn_card{ pair.first };
        map<suit, vector<card>> new_foundation{ pair.second };

        column new_column{ b->get_column(destination_column_index).with_card(drawn_card) };

        vector<column> new_columns{};
        for (size_t index{ 0 }; index < board::num_columns; index++)
        {
            if (index == destination_column_index)
            {
                new_columns.emplace_back(new_column);
            }
            else
            {
                new_columns.emplace_back(b->get_column(index));
            }
        }

        return make_shared<board>(
            new_columns,
            b->get_stock_pile(),
            b->get_stock_pile_index(),
            new_foundation);
    }

    bool FoundationToColumnMove::operator==(FoundationToColumnMove const& other) const
    {
        return destination_column_index == other.destination_column_index
            && _card == other._card;
    }

    string to_string(FoundationToColumnMove const& move)
    {
        return "Move "s
            + to_string(move.get_cards().front())
            + " from foundation to column "s
            + std::to_string(move.get_destination_column_index())
            + "."s;
    }

    wstring to_wstring(FoundationToColumnMove const& move)
    {
        return L"Move "s
            + to_wstring(move.get_cards().front())
            + L" from foundation to column "s
            + std::to_wstring(move.get_destination_column_index())
            + L"."s;
    }

    ostream& operator<<(ostream& out, FoundationToColumnMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, FoundationToColumnMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
