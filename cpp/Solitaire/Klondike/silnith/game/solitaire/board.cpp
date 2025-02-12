#include <silnith/game/solitaire/board.h>

#include <cstddef>
#include <exception>
#include <map>
#include <span>
#include <sstream>
#include <string>
#include <vector>

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;

using namespace std;
using namespace std::literals::string_literals;

board::board(
    span<column const> columns,
    span<card const> stock_pile,
    size_t stock_pile_index,
    map<suit, vector<card>> foundation)
    : columns{ columns.begin(), columns.end() },
    stock_pile{ stock_pile.begin(), stock_pile.end() },
    stock_pile_index{ stock_pile_index },
    foundation{ foundation }
{
    if (stock_pile_index < 0)
    {
        throw out_of_range("Stock pile index must be non-negative"s);
    }
    if (stock_pile_index > stock_pile.size())
    {
        throw out_of_range("Stock pile index outside of stock pile."s);
    }
}

vector<column> const& board::get_columns(void) const
{
    return columns;
}

column const& board::get_column(size_t index) const
{
    return columns.at(index);
}

vector<card> const& board::get_stock_pile(void) const
{
    return stock_pile;
}

size_t board::get_stock_pile_index(void) const
{
    return stock_pile_index;
}

map<suit, vector<card>> const& board::get_foundation(void) const
{
    return foundation;
}

bool board::can_advance_stock_pile(void) const
{
    return stock_pile_index < stock_pile.size();
}

bool board::can_recycle_stock_pile(void) const
{
    return !stock_pile.empty() && stock_pile_index >= stock_pile.size();
}

card const& board::get_stock_pile_card(void) const
{
    if (stock_pile_index > 0)
    {
        return stock_pile.at(stock_pile_index - 1);
    }
    else
    {
        throw out_of_range("No card available to draw from the stock pile."s);
    }
}

card const& board::get_top_of_foundation(suit suit) const
{
    vector<card> const& foundation_for_suit{ foundation.at(suit) };
    if (foundation_for_suit.empty())
    {
        throw out_of_range("Foundation has no cards for suit "s + to_string(suit));
    }
    return foundation_for_suit.back();
}

map<suit, vector<card>> board::get_foundation_plus_card(card new_card) const
{
    map<suit, vector<card>> new_foundation{ foundation };
    new_foundation.at(new_card.get_suit()).emplace_back(new_card);
    return new_foundation;
}

pair<card, map<suit, vector<card>>> board::extract_card_from_foundation(suit _suit) const
{
    map<suit, vector<card>> new_foundation{ foundation };
    vector<card>& foundation_for_suit{ new_foundation.at(_suit) };
    if (foundation_for_suit.empty())
    {
        throw out_of_range("No cards in the foundation with the suit "s + to_string(_suit));
    }
    card foundation_card{ foundation_for_suit.back() };
    foundation_for_suit.pop_back();
    return pair<card, map<suit, vector<card>>>{ foundation_card, new_foundation };
}

pair<card, vector<card>> board::extract_stock_pile_card(void) const
{
    card stock_pile_card{ get_stock_pile_card() };
    /*
     * StockPile: [a, b, c]
     * StockPileIndex: 2
     *
     * Result: [a, c]
     */
    vector<card> new_stock_pile{};
    span<card const> stock_pile_span{ stock_pile };
    for (card c : stock_pile_span.first(stock_pile_index - 1))
    {
        new_stock_pile.emplace_back(c);
    }
    for (card c : stock_pile_span.last(stock_pile_span.size() - stock_pile_index))
    {
        new_stock_pile.emplace_back(c);
    }
    return pair<card, vector<card>>{ stock_pile_card, new_stock_pile };
}

bool board::can_add_to_foundation(card _card) const
{
    return get_value(_card.get_value()) == 1 + foundation.at(_card.get_suit()).size();
}

string board::to_symbol(suit s) const
{
    switch (s)
    {
    case suit::club:
        return "C"s;
    case suit::diamond:
        return "D"s;
    case suit::heart:
        return "H"s;
    case suit::spade:
        return "S"s;
    default:
        return std::to_string(static_cast<int>(s));
    }
}

string board::to_symbol(value v) const
{
    switch (v)
    {
    case value::ace:
        return " A"s;
    case value::ten:
        return "10"s;
    case value::jack:
        return " J"s;
    case value::queen:
        return " Q"s;
    case value::king:
        return " K"s;
    default:
        return " "s + std::to_string(static_cast<int>(v));
    }
}

string board::to_symbol(card c) const
{
    return to_symbol(c.get_value()) + to_symbol(c.get_suit());
}

ostream& board::print_to(ostream& out) const
{
    for (pair<suit, vector<card>> const& pair : foundation)
    {
        out << "  "s;
        vector<card> const& cards{ pair.second };
        if (cards.empty())
        {
            out << " --"s;
        }
        else
        {
            out << to_symbol(cards.back());
        }
    }
    out << "   "s;
    out << "("s << stock_pile_index << "/"s << stock_pile.size() << ")"s;
    out << "  "s;
    if (stock_pile_index > 0)
    {
        out << to_symbol(stock_pile.at(stock_pile_index - 1));
    }
    else
    {
        out << "   "s;
    }
    out << endl;
    vector<vector<card>::const_iterator> iterators{};
    for (size_t i{ 0 }; i < num_columns; i++)
    {
        out << "  "s;
        out << "("s << columns.at(i).get_number_of_face_down_cards() << ")"s;
        vector<card>::const_iterator iter{ columns.at(i).get_face_up_cards().cbegin() };
        iterators.emplace_back(iter);
    }
    out << endl;
    bool printed_something;
    do {
        printed_something = false;
        for (size_t i{ 0 }; i < num_columns; i++)
        {
            out << "  "s;
            vector<card>::const_iterator& iter{ iterators.at(i) };
            if (iter != columns.at(i).get_face_up_cards().cend())
            {
                string symbol{ to_symbol(*iter) };
                out << symbol;
                iter++;
                printed_something = true;
            }
            else
            {
                out << "   "s;
            }
        }
        out << endl;
    } while (printed_something);
    return out;
}

bool board::operator==(board const& other) const
{
    if (stock_pile_index != other.stock_pile_index)
    {
        return false;
    }
    if (stock_pile.size() != other.stock_pile.size())
    {
        return false;
    }
    return columns == other.columns
        && foundation == other.foundation
        && stock_pile == other.stock_pile;
}

template<class t>
ostream& print_vec(ostream& out, vector<t> const& vec)
{
    out << "["s;
    typename vector<t>::const_iterator citer{ vec.cbegin() };
    typename vector<t>::const_iterator cend{ vec.cend() };
    if (citer != cend)
    {
        out << *citer;
        citer++;
        while (citer != cend)
        {
            out << ", "s;
            out << *citer;
            citer++;
        }
    }
    out << "]"s;
    return out;
}

ostream& print_pair(ostream& out, pair<suit, vector<card>> p)
{
    out << "{"s;
    out << p.first;
    out << ", "s;
    print_vec(out, p.second);
    out << "}"s;
    return out;
}

string silnith::game::solitaire::to_string(board const& b)
{
    ostringstream out{};
    out << "Board {columns: "s;
    print_vec(out, b.get_columns());
    out << ", stock_pile: "s;
    print_vec(out, b.get_stock_pile());
    out << ", stock_pile_index: "s;
    out << b.get_stock_pile_index();
    out << ", foundation: ["s;
    print_pair(out, { suit::club, b.get_foundation().at(suit::club) });
    print_pair(out, { suit::diamond, b.get_foundation().at(suit::diamond) });
    print_pair(out, { suit::heart, b.get_foundation().at(suit::heart) });
    print_pair(out, { suit::spade, b.get_foundation().at(suit::spade) });
    out << "]}"s;
    return out.str();
}

template<class t>
wostream& wprint_vec(wostream& out, vector<t> const& vec)
{
    out << L"["s;
    typename vector<t>::const_iterator citer{ vec.cbegin() };
    typename vector<t>::const_iterator cend{ vec.cend() };
    if (citer != cend)
    {
        out << *citer;
        citer++;
        while (citer != cend)
        {
            out << L", "s;
            out << *citer;
            citer++;
        }
    }
    out << L"]"s;
    return out;
}

wostream& wprint_pair(wostream& out, pair<suit, vector<card>> p)
{
    out << L"{"s;
    out << p.first;
    out << L", "s;
    wprint_vec(out, p.second);
    out << L"}"s;
    return out;
}

wstring silnith::game::solitaire::to_wstring(board const& b)
{
    wostringstream out{};
    out << L"Board {columns: "s;
    wprint_vec(out, b.get_columns());
    out << L", stock_pile: "s;
    wprint_vec(out, b.get_stock_pile());
    out << L", stock_pile_index: "s;
    out << b.get_stock_pile_index();
    out << L", foundation: ["s;
    wprint_pair(out, { suit::club, b.get_foundation().at(suit::club) });
    wprint_pair(out, { suit::diamond, b.get_foundation().at(suit::diamond) });
    wprint_pair(out, { suit::heart, b.get_foundation().at(suit::heart) });
    wprint_pair(out, { suit::spade, b.get_foundation().at(suit::spade) });
    out << L"]}"s;
    return out.str();
}
