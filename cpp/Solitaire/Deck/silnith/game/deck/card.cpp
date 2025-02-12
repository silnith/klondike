#include <silnith/game/deck/card.h>

#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <ostream>
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
}
