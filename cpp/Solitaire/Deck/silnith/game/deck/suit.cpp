#include <silnith/game/deck/suit.h>

#include <ostream>
#include <stdexcept>
#include <string>

using namespace silnith::game::deck;

color silnith::game::deck::get_color(suit const suit)
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
        using namespace std::literals::string_literals;
        throw std::invalid_argument{ "Invalid suit: "s + std::to_string(static_cast<int>(suit)) };
    }
}

std::string silnith::game::deck::to_string(suit const suit)
{
    using namespace std::literals::string_literals;
    switch (suit)
    {
    case suit::club:
        return "club"s;
    case suit::diamond:
        return "diamond"s;
    case suit::heart:
        return "heart"s;
    case suit::spade:
        return "spade"s;
    default:
        return std::to_string(static_cast<int>(suit));
    }
}

std::wstring silnith::game::deck::to_wstring(suit const suit)
{
    using namespace std::literals::string_literals;
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

std::ostream& silnith::game::deck::operator<<(std::ostream& out, suit const suit)
{
    out << to_string(suit);
    return out;
}

std::wostream& silnith::game::deck::operator<<(std::wostream& out, suit const suit)
{
    out << to_wstring(suit);
    return out;
}
