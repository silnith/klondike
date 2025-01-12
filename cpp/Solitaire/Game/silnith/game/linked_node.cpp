#include <silnith/game/linked_node.h>

using namespace silnith::game;

static_assert(std::input_iterator<linked_node<int>::const_iterator>);
