#include "CppUnitTest.h"

#include <silnith/game/linked_node.h>

#include <algorithm>
#include <iterator>
#include <memory>
#include <vector>

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

using namespace silnith::game;

namespace GameTests
{
	TEST_CLASS(LinkedNodeTests)
	{
	public:
		TEST_METHOD(TestLinkedNodeConstruction)
		{
			linked_node<int> node{ 5 };

			Assert::AreEqual(5, node.get_value());
		}

		TEST_METHOD(TestLinkedNodeValue)
		{
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(5) };

			Assert::AreEqual(5, node_ptr->get_value());
		}

		TEST_METHOD(TestLinkedNodeNext)
		{
			std::shared_ptr<linked_node<int>> node2{ std::make_shared<linked_node<int>>(5) };
			std::shared_ptr<linked_node<int>> node1{ std::make_shared<linked_node<int>>(2, node2) };

			Assert::AreEqual(2, node1->get_value());
			Assert::AreEqual(5, node1->get_next()->get_value());
		}

		TEST_METHOD(TestHasCBegin)
		{
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(5) };

			linked_node<int>::const_iterator iter{ node_ptr->cbegin() };
		}

		TEST_METHOD(TestHasCEnd)
		{
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(5) };

			linked_node<int>::const_iterator iter{ node_ptr->cend() };
		}

		TEST_METHOD(TestConstIteratorCanPostIncrement)
		{
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(5) };

			linked_node<int>::const_iterator iter{ node_ptr->cbegin() };

			iter++;
		}

		TEST_METHOD(TestConstIteratorCanPreIncrement)
		{
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(5) };

			linked_node<int>::const_iterator iter{ node_ptr->cbegin() };

			++iter;
		}

		TEST_METHOD(TestConstIteratorWorks)
		{
			std::shared_ptr<linked_node<int>> node3{ std::make_shared<linked_node<int>>(3) };
			std::shared_ptr<linked_node<int>> node2{ std::make_shared<linked_node<int>>(2, node3) };
			std::shared_ptr<linked_node<int>> node1{ std::make_shared<linked_node<int>>(1, node2) };
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(0, node1) };

			linked_node<int>::const_iterator iter{ node_ptr->cbegin() };
			linked_node<int>::const_iterator end{ node_ptr->cend() };

			int count{ 0 };
			while (iter != end)
			{
				int value{ *iter };
				Assert::AreEqual(count, value);
				++iter;
				++count;
			}
			Assert::AreEqual(4, count);
		}

		TEST_METHOD(TestSequenceEquals)
		{
			std::shared_ptr<linked_node<int>> node3{ std::make_shared<linked_node<int>>(3) };
			std::shared_ptr<linked_node<int>> node2{ std::make_shared<linked_node<int>>(2, node3) };
			std::shared_ptr<linked_node<int>> node1{ std::make_shared<linked_node<int>>(1, node2) };
			std::shared_ptr<linked_node<int>> node_ptr{ std::make_shared<linked_node<int>>(0, node1) };

			std::vector<int> vec{ 0, 1, 2, 3 };
			Assert::IsTrue(std::equal(node_ptr->cbegin(), node_ptr->cend(), vec.cbegin(), vec.cend()));
		}

		//TEST_METHOD(TestLinkedNodeRangeBasedForLoopConstValue)
		//{
		//	linked_node<int> node{ 5 };
		//
		//	for (int const& value : node)
		//	{
		//		Assert::AreEqual(5, value);
		//	}
		//}
	};
}
