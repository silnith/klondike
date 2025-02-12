#include <silnith/game/deck/color.h>

#include <ostream>
#include <string>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::deck
{
	string to_string(color const& color)
	{
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

	wstring to_wstring(color const& color)
	{
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

	ostream& operator<<(ostream& out, color const& color)
	{
		out << to_string(color);
		return out;
	}

	wostream& operator<<(wostream& out, color const& color)
	{
		out << to_wstring(color);
		return out;
	}
}
