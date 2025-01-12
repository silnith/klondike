#include <silnith/game/deck/color.h>

using namespace silnith::game::deck;

std::string silnith::game::deck::to_string(color const color)
{
	using namespace std::literals::string_literals;
	switch (color)
	{
	case color::black:
		return "black"s;
	case color::red:
		return "red"s;
	default:
		return std::to_string(static_cast<int>(color));
	}
}

std::wstring silnith::game::deck::to_wstring(color const color)
{
	using namespace std::literals::string_literals;
	switch (color)
	{
	case color::black:
		return L"black"s;
	case color::red:
		return L"red"s;
	default:
		return std::to_wstring(static_cast<int>(color));
	}
}

std::ostream& silnith::game::deck::operator<<(std::ostream& out, color const color)
{
	out << to_string(color);
	return out;
}

std::wostream& silnith::game::deck::operator<<(std::wostream& out, color const color)
{
	out << to_wstring(color);
	return out;
}
