#include <silnith/game/deck/value.h>

#include <ostream>
#include <string>

using namespace std;
using namespace std::literals::string_literals;

namespace silnith::game::deck
{
	int get_value(value const& value)
	{
		return static_cast<int>(value);
	}

	string to_string(value const& value)
	{
		switch (value)
		{
		case value::ace:
			return "ace"s;
		case value::two:
			return "two"s;
		case value::three:
			return "three"s;
		case value::four:
			return "four"s;
		case value::five:
			return "five"s;
		case value::six:
			return "six"s;
		case value::seven:
			return "seven"s;
		case value::eight:
			return "eight"s;
		case value::nine:
			return "nine"s;
		case value::ten:
			return "ten"s;
		case value::jack:
			return "jack"s;
		case value::queen:
			return "queen"s;
		case value::king:
			return "king"s;
		default:
			return std::to_string(static_cast<int>(value));
		}
	}

	wstring to_wstring(value const& value)
	{
		switch (value)
		{
		case value::ace:
			return L"ace"s;
		case value::two:
			return L"two"s;
		case value::three:
			return L"three"s;
		case value::four:
			return L"four"s;
		case value::five:
			return L"five"s;
		case value::six:
			return L"six"s;
		case value::seven:
			return L"seven"s;
		case value::eight:
			return L"eight"s;
		case value::nine:
			return L"nine"s;
		case value::ten:
			return L"ten"s;
		case value::jack:
			return L"jack"s;
		case value::queen:
			return L"queen"s;
		case value::king:
			return L"king"s;
		default:
			return std::to_wstring(static_cast<int>(value));
		}
	}

	ostream& operator<<(ostream& out, value const& value)
	{
		out << to_string(value);
		return out;
	}

	wostream& operator<<(wostream& out, value const& value)
	{
		out << to_wstring(value);
		return out;
	}
}
