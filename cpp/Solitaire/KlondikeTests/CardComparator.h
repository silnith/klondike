#pragma once

#include <silnith/game/deck/card.h>

#include <span>

bool operator<(silnith::game::deck::card const& a, silnith::game::deck::card const& b);

bool operator<(std::span<silnith::game::deck::card const> const& lhs, std::span<silnith::game::deck::card const> const& rhs);
