#include <silnith/game/deck/card.h>

#include <silnith/game/deck/color.h>
#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <ostream>
#include <span>
#include <string>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::deck
{
    card::card(value const& v, suit const& s)
        : _value{ v },
        _suit{ s }
    {}

    value const& card::get_value(void) const noexcept
    {
        return _value;
    }

    suit const& card::get_suit(void) const noexcept
    {
        return _suit;
    }

    color card::get_color(void) const noexcept
    {
        return silnith::game::deck::get_color(_suit);
    }

    bool card::operator==(card const& other) const
    {
        return _value == other._value && _suit == other._suit;
    }

    string to_string(card const& card)
    {
        return to_string(card.get_value()) + " of "s + to_string(card.get_suit()) + "s"s;
    }

    wstring to_wstring(card const& card)
    {
        return to_wstring(card.get_value()) + L" of "s + to_wstring(card.get_suit()) + L"s"s;
    }

    ostream& operator<<(ostream& out, card const& card)
    {
        out << to_string(card);
        return out;
    }

    wostream& operator<<(wostream& out, card const& card)
    {
        out << to_wstring(card);
        return out;
    }

    ostream& operator<<(ostream& out, span<card const> const& cards)
    {
        out << "["s;
        if (!cards.empty())
        {
            span<card const>::iterator iter{ cards.begin() };
            span<card const>::iterator end{ cards.end() };
            out << *iter;
            iter++;
            while (iter != end)
            {
                out << ", "s;
                out << *iter;
                iter++;
            }
        }
        out << "]"s;
        return out;
    }

    wostream& operator<<(wostream& out, span<card const> const& cards)
    {
        out << L"["s;
        if (!cards.empty())
        {
            span<card const>::iterator iter{ cards.begin() };
            span<card const>::iterator end{ cards.end() };
            out << *iter;
            iter++;
            while (iter != end)
            {
                out << L", "s;
                out << *iter;
                iter++;
            }
        }
        out << L"]"s;
        return out;
    }
}
