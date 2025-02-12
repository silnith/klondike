#pragma once

#include "CppUnitTest.h"

#include <silnith/game/solitaire/board.h>

#include <string>
#include <vector>

namespace Microsoft
{
	namespace VisualStudio
	{
		namespace CppUnitTestFramework
		{
			template<>
			std::wstring ToString<>(silnith::game::solitaire::board const& b)
			{
				return silnith::game::solitaire::to_wstring(b);
			}
		}
	}
}
