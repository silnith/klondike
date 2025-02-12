#pragma once

#include "CppUnitTest.h"

#include <silnith/game/deck/card.h>
#include <silnith/game/deck/suit.h>
#include <silnith/game/deck/value.h>

#include <string>
#include <vector>

namespace Microsoft
{
	namespace VisualStudio
	{
		namespace CppUnitTestFramework
		{
			template<>
			std::wstring ToString<>(silnith::game::deck::value const& value)
			{
				return silnith::game::deck::to_wstring(value);
			}

			template<>
			std::wstring ToString<>(silnith::game::deck::suit const& suit)
			{
				return silnith::game::deck::to_wstring(suit);
			}

			template<>
			std::wstring ToString<>(silnith::game::deck::card const& card)
			{
				return silnith::game::deck::to_wstring(card);
			}

			template<>
			std::wstring ToString<>(std::vector<silnith::game::deck::card> const& cards)
			{
				using namespace std::literals::string_literals;
				std::wstring str{};
				str.append(L"["s);
				std::vector<silnith::game::deck::card>::const_iterator citer{ cards.cbegin() };
				std::vector<silnith::game::deck::card>::const_iterator cend{ cards.cend() };
				if (citer != cend)
				{
					str.append(silnith::game::deck::to_wstring(*citer));
					citer++;
					while (citer != cend)
					{
						str.append(L", "s);
						str.append(silnith::game::deck::to_wstring(*citer));
						citer++;
					}
				}
				str.append(L"]"s);
				return str;
			}
		}
	}
}
