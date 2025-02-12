#include "CardComparator.h"

#include <silnith/game/deck/card.h>

#include <span>

using namespace silnith::game::deck;

using namespace std;

bool operator<(card const& lhs, card const& rhs)
{
	if (lhs.get_value() < rhs.get_value())
	{
		return true;
	}
	else if (lhs.get_value() == rhs.get_value())
	{
		return lhs.get_suit() < rhs.get_suit();
	}
	else
	{
		return false;
	}
}

bool operator<(span<card const> const& lhs, span<card const> const& rhs)
{
	span<card const>::iterator left_iter{ lhs.begin() };
	span<card const>::iterator left_end{ lhs.end() };
	span<card const>::iterator right_iter{ rhs.begin() };
	span<card const>::iterator right_end{ rhs.end() };
	while (left_iter != left_end
		&& right_iter != right_end)
	{
		card const left_card{ *left_iter };
		card const right_card{ *right_iter };
		if (left_card < right_card)
		{
			return true;
		}
		else if (left_card == right_card)
		{
			// continue
		}
		else
		{
			return false;
		}
		left_iter++;
		right_iter++;
	}
	if (left_iter == left_end)
	{
		// left span is ended, check if right span has more elements.
		// if so, it is greater
		// if not, both spans are ended, so they are equal
		if (right_iter == right_end)
		{
			// equal is not less, so false
			return false;
		}
		else
		{
			// right is longer, so it is greater
			// that means left is less
			return true;
		}
	}
	else
	{
		// left span is not ended
		// since we cannot exit the loop above unless one span is ended,
		// by process of elimination right span is done
		// therefore, the right span is less, so the left is greater, so this returns false
		return false;
	}
}
