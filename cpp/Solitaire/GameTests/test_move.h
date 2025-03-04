#pragma once

#include "CppUnitTest.h"

#include <silnith/game/move.h>

#include <memory>
#include <string>
#include <ostream>

class test_move : public silnith::game::move<int>
{
public:
	test_move(void) = delete;
	test_move(test_move const&) = default;
	test_move& operator=(test_move const&) = default;
	test_move(test_move&&) noexcept = default;
	test_move& operator=(test_move&&) noexcept = default;
	virtual ~test_move(void) = default;

	test_move(int id) : id{ id }
	{}

	virtual std::shared_ptr<int const> apply(std::shared_ptr<int const> const& board) const override
	{
		return std::make_shared<int>(id);
	}

	bool operator==(test_move const& other) const
	{
		return id == other.id;
	}

	friend std::wstring to_wstring(test_move const& test_move)
	{
		using namespace std::literals::string_literals;
		return L"test_move{ "s + std::to_wstring(test_move.id) + L" }"s;
	}

	friend std::wostream& operator<<(std::wostream& out, test_move const& move)
	{
		out << to_wstring(move);
		return out;
	}

private:
	int id;
};

template<>
std::wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(test_move const& test_move)
{
	return to_wstring(test_move);
}
