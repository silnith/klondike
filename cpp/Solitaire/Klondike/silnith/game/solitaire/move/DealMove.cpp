#include <silnith/game/solitaire/move/DealMove.h>

#include <sstream>

using namespace silnith::game::deck;

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::solitaire::move
{
    DealMove::DealMove(span<card const> const& cards)
        : cards{ cards.begin(), cards.end() }
    {
        size_t cards_required{ 0 };
        for (size_t i{ 1 }; i <= board::num_columns; i++)
        {
            cards_required += i;
        }
        if (cards.size() < cards_required)
        {
            throw out_of_range("A deck of size "s + std::to_string(cards_required) + " is required to deal "s + std::to_string(board::num_columns) + " columns."s);
        }
    }

    bool DealMove::has_cards(void) const
    {
        return true;
    }

    vector<card> DealMove::get_cards(void) const
    {
        return cards;
    }

    bool DealMove::is_stock_pile_modification(void) const
    {
        return false;
    }

    bool DealMove::is_stock_pile_advance(void) const
    {
        return false;
    }

    bool DealMove::is_stock_pile_recycle(void) const
    {
        return false;
    }

    bool DealMove::is_from_stock_pile(void) const
    {
        return false;
    }

    bool DealMove::is_from_foundation(void) const
    {
        return false;
    }

    bool DealMove::is_from_column(void) const
    {
        return false;
    }

    bool DealMove::is_from_column(size_t column_index) const
    {
        return false;
    }

    size_t DealMove::get_source_column_index(void) const
    {
        throw invalid_argument("Not a move from a column.");
    }

    bool DealMove::is_to_foundation(void) const
    {
        return false;
    }

    bool DealMove::is_to_column(void) const
    {
        return false;
    }

    bool DealMove::is_to_column(size_t column_index) const
    {
        return false;
    }

    size_t DealMove::get_destination_column_index(void) const
    {
        throw invalid_argument("Not a move to a column.");
    }

    shared_ptr<board> DealMove::apply(shared_ptr<board> const& b) const
    {
        vector<vector<card>> stacks{};
        for (size_t index{ 0 }; index < board::num_columns; index++)
        {
            stacks.emplace_back(vector<card>{});
        }
        vector<card>::const_iterator citer{ cards.cbegin() };
        vector<card>::const_iterator cend{ cards.cend() };
        for (size_t outer_index{ 0 }; outer_index < board::num_columns; outer_index++)
        {
            for (size_t inner_index{ outer_index }; inner_index < board::num_columns; inner_index++)
            {
                if (citer == cend)
                {
                    // This should never happen, because the size of the deck is validated in the constructor.
                    throw out_of_range("Not enough cards to deal the board."s);
                }
                stacks.at(inner_index).emplace_back(*citer);
                citer++;
            }
        }

        vector<card> empty{};

        vector<column> columns{};
        for (vector<card> const& stack : stacks)
        {
            columns.emplace_back(column{ stack, empty });
        }
        vector<card> stock_pile{ citer, cend };
        map<suit, vector<card>> foundation{
            { suit::club, empty },
            { suit::diamond, empty },
            { suit::heart, empty },
            { suit::spade, empty },
        };
        return make_shared<board>(
            columns,
            stock_pile,
            0,
            foundation);
    }

    bool DealMove::operator==(DealMove const& other) const
    {
        return cards == other.cards;
    }

    string to_string(DealMove const& move)
    {
        ostringstream out{};
        out << move.get_cards();
        return "Deal "s
            + std::to_string(board::num_columns)
            + " columns using deck "s
            + out.str()
            + "."s;
    }

    wstring to_wstring(DealMove const& move)
    {
        wostringstream out{};
        out << move.get_cards();
        return L"Deal "s
            + std::to_wstring(board::num_columns)
            + L" columns using deck "s
            + out.str()
            + L"."s;
    }

    ostream& operator<<(ostream& out, DealMove const& move)
    {
        out << to_string(move);
        return out;
    }

    wostream& operator<<(wostream& out, DealMove const& move)
    {
        out << to_wstring(move);
        return out;
    }
}
