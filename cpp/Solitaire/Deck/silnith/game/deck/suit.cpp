#include <silnith/game/deck/suit.h>

#include <silnith/game/deck/color.h>

#include <ostream>
#include <stdexcept>
#include <string>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::deck
{
    color get_color(suit const& suit)
    {
        switch (suit)
        {
        case suit::club:
            return color::black;
        case suit::diamond:
            return color::red;
        case suit::heart:
            return color::red;
        case suit::spade:
            return color::black;
        default:
            throw invalid_argument{ "Invalid suit: "s + std::to_string(static_cast<int>(suit)) };
        }
    }

    string to_string(suit const& suit)
    {
        /*
         * If these produces warnings about "cannot be represented in the current code page",
         * ensure that the /utf-8 compiler switch is specified for all projects.
         */
        switch (suit)
        {
        case suit::club:
            //return "\xE2\x99\xA3"s;
            return "\u2663"s;
        case suit::diamond:
            //return "\xE2\x99\xA6"s;
            return "\u2666"s;
        case suit::heart:
            //return "\xE2\x99\xA5"s;
            return "\u2665"s;
        case suit::spade:
            //return "\xE2\x99\xA5"s;
            return "\u2660"s;
        default:
            return std::to_string(static_cast<int>(suit));
        }
    }

    wstring to_wstring(suit const& suit)
    {
        switch (suit)
        {
        case suit::club:
            return L"\u2663"s;
        case suit::diamond:
            return L"\u2666"s;
        case suit::heart:
            return L"\u2665"s;
        case suit::spade:
            return L"\u2660"s;
        default:
            return std::to_wstring(static_cast<int>(suit));
        }
    }

    ostream& operator<<(ostream& out, suit const& suit)
    {
        out << to_string(suit);
        return out;
    }

    wostream& operator<<(wostream& out, suit const& suit)
    {
        out << to_wstring(suit);
        return out;
    }
}
