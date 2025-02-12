#include "CppUnitTest.h"

#include "CardToString.h"

#include <silnith/game/solitaire/column.h>

#include <memory>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game::deck;
using namespace silnith::game::solitaire;

using namespace std;
using namespace std::literals::string_literals;

template<>
wstring Microsoft::VisualStudio::CppUnitTestFramework::ToString<>(column const& column)
{
	return to_wstring(column);
}

namespace SolitaireTests
{
	TEST_CLASS(ColumnTests)
	{
	private:

	public:
		TEST_METHOD(TestConstructorVector)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };
		}

		TEST_METHOD(TestConstructorVectorWithDown)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{};

			column new_column{ face_down, face_up };
		}

		TEST_METHOD(TestConstructorVectorWithUp)
		{
			vector<card> face_down{};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };
		}

		TEST_METHOD(TestConstructorVectorWithBoth)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };
		}

		TEST_METHOD(TestConstructorSpanWithBoth)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ span<card const>{ face_down }, span<card const>{ face_up } };
		}

		TEST_METHOD(TestHasFaceDownCards)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::IsTrue(new_column.has_face_down_cards());
		}

		TEST_METHOD(TestHasFaceDownCardsEmpty)
		{
			vector<card> face_down{};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::IsFalse(new_column.has_face_down_cards());
		}

		TEST_METHOD(TestHasFaceUpCards)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::IsTrue(new_column.has_face_up_cards());
		}

		TEST_METHOD(TestHasFaceUpCardsEmpty)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			Assert::IsFalse(new_column.has_face_up_cards());
		}

		TEST_METHOD(TestHasFaceUpCardsAfterFlipFromDown)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			Assert::IsTrue(new_column.has_face_up_cards());
		}

		TEST_METHOD(TestGetNumberOfFaceDownCards)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			size_t expected{ 3 };
			Assert::AreEqual(expected, new_column.get_number_of_face_down_cards());
		}

		TEST_METHOD(TestGetNumberOfFaceDownCardsEmpty)
		{
			vector<card> face_down{};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			size_t expected{ 0 };
			Assert::AreEqual(expected, new_column.get_number_of_face_down_cards());
		}

		TEST_METHOD(TestGetNumberOfFaceUpCards)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			size_t expected{ 4 };
			Assert::AreEqual(expected, new_column.get_number_of_face_up_cards());
		}

		TEST_METHOD(TestGetNumberOfFaceUpCardsEmpty)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			size_t expected{ 1 };
			Assert::AreEqual(expected, new_column.get_number_of_face_up_cards());
		}

		TEST_METHOD(TestFlippedCardRemovedFromDown)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			size_t expected{ 2 };
			Assert::AreEqual(expected, new_column.get_number_of_face_down_cards());
		}

		TEST_METHOD(TestGetTopCard)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			card actual{ new_column.get_top_card() };

			card expected{ value::ten, suit::diamond };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetTopCardEmpty)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			card actual{ new_column.get_top_card() };

			card expected{ value::ace, suit::heart };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetFaceUpCards)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.get_face_up_cards() };

			vector<card> expected{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetFaceUpCardsEmpty)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.get_face_up_cards() };

			vector<card> expected{
				card{ value::ace, suit::heart },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetTopCardsOverflow)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::ExpectException<out_of_range>([new_column]()
				{
					vector<card> run{ new_column.get_top_cards(5) };
				});
		}

		TEST_METHOD(TestGetTopCards4)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.get_top_cards(4) };

			vector<card> expected{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetTopCards3)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.get_top_cards(3) };

			vector<card> expected{
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetTopCards2)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.get_top_cards(2) };

			vector<card> expected{
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetTopCards1)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.get_top_cards(1) };

			vector<card> expected{
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestGetTopCardsUnderflow)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::ExpectException<out_of_range>([new_column]()
				{
					vector<card> run{ new_column.get_top_cards(0) };
				});
		}

		TEST_METHOD(TestExtractCardEmpty)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			Assert::ExpectException<out_of_range>([new_column]()
				{
					pair<card, column> unused{ new_column.extract_card() };
				});
		}

		TEST_METHOD(TestExtractCardCard)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			card actual{ new_column.extract_card().first };

			card expected{ value::ten, suit::diamond };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractCardColumn)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			column actual{ new_column.extract_card().second };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRunEmpty)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			Assert::ExpectException<out_of_range>([new_column]()
				{
					pair<vector<card>, column> unused{ new_column.extract_run(1) };
				});
		}

		TEST_METHOD(TestExtractRunOverflow)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::ExpectException<out_of_range>([new_column]()
				{
					pair<vector<card>, column> unused{ new_column.extract_run(5) };
				});
		}

		TEST_METHOD(TestExtractRun4Run)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.extract_run(4).first };

			vector<card> expected{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun4Column)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			column actual{ new_column.extract_run(4).second };

			vector<card> expected_face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
			};
			vector<card> expected_face_up{
				card{ value::ace, suit::heart },
			};
			column expected{ expected_face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun3Run)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.extract_run(3).first };

			vector<card> expected{
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun3Column)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			column actual{ new_column.extract_run(3).second };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun2Run)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.extract_run(2).first };

			vector<card> expected{
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun2Column)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			column actual{ new_column.extract_run(2).second };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun1Run)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> actual{ new_column.extract_run(1).first };

			vector<card> expected{
				card{ value::ten, suit::diamond },
			};
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRun1Column)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			column actual{ new_column.extract_run(1).second };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestExtractRunUnderflow)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::ExpectException<out_of_range>([new_column]()
				{
					pair<vector<card>, column> unused{ new_column.extract_run(0) };
				});
		}

		TEST_METHOD(TestWithCard)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			column actual{ new_column.with_card(card{ value::nine, suit::club }) };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
				card{ value::nine, suit::club },
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestWithCards1)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::nine, suit::club },
			};
			column actual{ new_column.with_cards(run) };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
				card{ value::nine, suit::club },
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestWithCards3)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::nine, suit::club },
				card{ value::eight, suit::diamond },
				card{ value::seven, suit::club },
			};
			column actual{ new_column.with_cards(run) };

			vector<card> expected_face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
				card{ value::nine, suit::club },
				card{ value::eight, suit::diamond },
				card{ value::seven, suit::club },
			};
			column expected{ face_down, expected_face_up };
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(TestWithCardsEmpty)
		{
			vector<card> face_down{
				card{ value::four, suit::spade },
				card{ value::jack, suit::heart },
				card{ value::ace, suit::heart },
			};
			vector<card> face_up{
				card{ value::king, suit::club },
				card{ value::queen, suit::diamond},
				card{ value::jack, suit::club },
				card{ value::ten, suit::diamond },
			};

			column new_column{ face_down, face_up };

			Assert::ExpectException<invalid_argument>([new_column]()
				{
					vector<card> empty{};
					column unused{ new_column.with_cards(empty) };
				});
		}

		TEST_METHOD(TestCanAddRunEmptyColumnEmptyRun)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			Assert::ExpectException<invalid_argument>([new_column]()
				{
					vector<card> empty{};
					bool unused{ new_column.can_add_run(empty) };
				});
		}

		TEST_METHOD(TestCanAddRunEmptyColumnKing)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::king, suit::club },
			};
			Assert::IsTrue(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunEmptyColumnKingRun)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::king, suit::club },
				card{ value::queen, suit::heart },
				card{ value::jack, suit::club },
			};
			Assert::IsTrue(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunEmptyColumnQueen)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::queen, suit::heart },
			};
			Assert::IsFalse(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunEmptyColumnQueenRun)
		{
			vector<card> face_down{};
			vector<card> face_up{};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::queen, suit::heart },
				card{ value::jack, suit::club },
				card{ value::ten, suit::heart },
			};
			Assert::IsFalse(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunEmptyRun)
		{
			vector<card> face_down{
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
			};
			vector<card> face_up{
				card{ value::ten, suit::spade },
				card{ value::nine, suit::heart },
				card{ value::eight, suit::spade },
			};

			column new_column{ face_down, face_up };

			Assert::ExpectException<invalid_argument>([new_column]()
				{
					vector<card> empty{};
					bool unused{ new_column.can_add_run(empty) };
				});
		}

		TEST_METHOD(TestCanAddRun)
		{
			vector<card> face_down{
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
			};
			vector<card> face_up{
				card{ value::ten, suit::spade },
				card{ value::nine, suit::heart },
				card{ value::eight, suit::spade },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::seven, suit::heart },
				card{ value::six, suit::spade },
				card{ value::five, suit::heart },
			};
			Assert::IsTrue(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunWrongColor)
		{
			vector<card> face_down{
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
			};
			vector<card> face_up{
				card{ value::ten, suit::spade },
				card{ value::nine, suit::heart },
				card{ value::eight, suit::spade },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::seven, suit::spade },
				card{ value::six, suit::heart },
				card{ value::five, suit::spade },
			};
			Assert::IsFalse(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunTooHigh)
		{
			vector<card> face_down{
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
			};
			vector<card> face_up{
				card{ value::ten, suit::spade },
				card{ value::nine, suit::heart },
				card{ value::eight, suit::spade },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::eight, suit::heart },
				card{ value::seven, suit::spade },
				card{ value::six, suit::heart },
			};
			Assert::IsFalse(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunTooLow)
		{
			vector<card> face_down{
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
			};
			vector<card> face_up{
				card{ value::ten, suit::spade },
				card{ value::nine, suit::heart },
				card{ value::eight, suit::spade },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::six, suit::heart },
				card{ value::five, suit::spade },
				card{ value::four, suit::heart },
			};
			Assert::IsFalse(new_column.can_add_run(run));
		}

		TEST_METHOD(TestCanAddRunKing)
		{
			vector<card> face_down{
				card{ value::three, suit::club },
				card{ value::three, suit::diamond },
				card{ value::three, suit::heart },
				card{ value::three, suit::spade },
			};
			vector<card> face_up{
				card{ value::ten, suit::spade },
				card{ value::nine, suit::heart },
				card{ value::eight, suit::spade },
			};

			column new_column{ face_down, face_up };

			vector<card> run{
				card{ value::king, suit::heart },
				card{ value::queen, suit::spade },
				card{ value::jack, suit::heart },
			};
			Assert::IsFalse(new_column.can_add_run(run));
		}

		TEST_METHOD(TestEquals)
		{
			vector<card> face_down{
				card{ value::two, suit::heart },
				card{ value::king, suit::diamond },
			};
			vector<card> face_up{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::diamond },
				card{ value::six, suit::club },
			};

			column column1{ face_down, face_up };
			column column2{ face_down, face_up };

			Assert::AreEqual(column1, column2);
		}

		TEST_METHOD(TestEqualsFaceDownDiffer)
		{
			vector<card> face_down1{
				card{ value::two, suit::heart },
				card{ value::king, suit::diamond },
			};
			vector<card> face_down2{
				card{ value::two, suit::spade },
				card{ value::king, suit::diamond },
			};
			vector<card> face_up{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::diamond },
				card{ value::six, suit::club },
			};

			column column1{ face_down1, face_up };
			column column2{ face_down2, face_up };

			Assert::AreNotEqual(column1, column2);
		}

		TEST_METHOD(TestEqualsFaceDownShorter)
		{
			vector<card> face_down1{
				card{ value::two, suit::heart },
				card{ value::king, suit::diamond },
			};
			vector<card> face_down2{};
			vector<card> face_up{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::diamond },
				card{ value::six, suit::club },
			};

			column column1{ face_down1, face_up };
			column column2{ face_down2, face_up };

			Assert::AreNotEqual(column1, column2);
		}

		TEST_METHOD(TestEqualsFaceDownLonger)
		{
			vector<card> face_down1{};
			vector<card> face_down2{
				card{ value::two, suit::spade },
				card{ value::king, suit::diamond },
			};
			vector<card> face_up{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::diamond },
				card{ value::six, suit::club },
			};

			column column1{ face_down1, face_up };
			column column2{ face_down2, face_up };

			Assert::AreNotEqual(column1, column2);
		}

		TEST_METHOD(TestEqualsFaceUpDiffer)
		{
			vector<card> face_down{
				card{ value::two, suit::heart },
				card{ value::king, suit::diamond },
			};
			vector<card> face_up1{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::diamond },
				card{ value::six, suit::club },
			};
			vector<card> face_up2{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::heart },
				card{ value::six, suit::club },
			};

			column column1{ face_down, face_up1 };
			column column2{ face_down, face_up2 };

			Assert::AreNotEqual(column1, column2);
		}

		TEST_METHOD(TestEqualsFaceUpShorter)
		{
			vector<card> face_down{
				card{ value::two, suit::heart },
				card{ value::king, suit::diamond },
			};
			vector<card> face_up1{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::diamond },
				card{ value::six, suit::club },
			};
			vector<card> face_up2{};

			column column1{ face_down, face_up1 };
			column column2{ face_down, face_up2 };

			Assert::AreNotEqual(column1, column2);
		}

		TEST_METHOD(TestEqualsFaceUpLonger)
		{
			vector<card> face_down{
				card{ value::two, suit::heart },
				card{ value::king, suit::diamond },
			};
			vector<card> face_up1{};
			vector<card> face_up2{
				card{ value::eight, suit::spade },
				card{ value::seven, suit::heart },
				card{ value::six, suit::club },
			};

			column column1{ face_down, face_up1 };
			column column2{ face_down, face_up2 };

			Assert::AreNotEqual(column1, column2);
		}
	};
}
