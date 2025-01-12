#include <silnith/game/deck/card.h>

using namespace silnith::game::deck;

using namespace std::literals::string_literals;

std::string silnith::game::deck::to_string(card const& card)
{
    return to_string(card.get_value()) + " of "s + to_string(card.get_suit()) + "s"s;
}

std::wstring silnith::game::deck::to_wstring(card const& card)
{
    return to_wstring(card.get_value()) + L" of "s + to_wstring(card.get_suit()) + L"s"s;
}

std::ostream& silnith::game::deck::operator<<(std::ostream& out, card const& card)
{
    out << to_string(card);
    return out;
}

std::wostream& silnith::game::deck::operator<<(std::wostream& out, card const& card)
{
    out << to_wstring(card);
    return out;
}
