#pragma once

#include <iterator>
#include <memory>

namespace silnith
{
    namespace game
    {
        /// <summary>
        /// An immutable, forwards-linked list.  Instances of this list are created by
        /// concatenating a new element onto the beginning of an existing list.  The
        /// advantage is that multiple lists can share the same tail safely, which can
        /// provide memory savings for certain specific use-cases.
        /// </summary>
        /// <remarks>
        /// <para>
        /// Users should always allocate and refer to instances of this type using a
        /// <c>std::shared_ptr</c>.
        /// </para>
        /// <para>
        /// Lists of this type will always have at least one element.
        /// An empty list cannot be represented by this implementation.
        /// </para>
        /// <para>
        /// Users of the language Lisp will recognize this paradigm as similar to the
        /// <c>car</c>, <c>cdr</c>, <c>cons</c> concept.
        /// </para>
        /// </remarks>
        /// <typeparam name="T">The type of elements in this list.</typeparam>
        // TODO: Look into std::enable_shared_from_this
        template<class T>
        class linked_node
        {
        public:
            class const_iterator
            {
            public:
                using iterator_category = std::input_iterator_tag;
                using value_type = T;
                using pointer = T const*;
                using reference = T const&;
                // TODO: difference_type makes no sense for this type, but it is necessary to satisfy the concept.
                using difference_type = std::ptrdiff_t;

            public:
                const_iterator(void) = default;
                const_iterator(const_iterator const&) = default;
                const_iterator& operator=(const_iterator const&) = default;
                const_iterator(const_iterator&&) = default;
                const_iterator& operator=(const_iterator&&) = default;
                ~const_iterator(void) = default;

                explicit const_iterator(linked_node<T> const* node) : current{ node }
                {}

                [[nodiscard]]
                T const& operator*(void) const
                {
                    return current->get_value();
                }

                const_iterator& operator++(void)
                {
                    std::shared_ptr<linked_node<T> const> next_ptr{ current->get_next() };
                    if (next_ptr.get() == nullptr)
                    {
                        operator=(const_iterator{ nullptr });
                    }
                    else
                    {
                        operator=(next_ptr->cbegin());
                    }
                    return *this;
                }

                const_iterator operator++(int huh)
                {
                    linked_node<T> const* copy{ current };
                    std::shared_ptr<linked_node<T> const> next_ptr{ current->get_next() };
                    current = next_ptr.get();
                    return const_iterator{ copy };
                }

                [[nodiscard]]
                bool operator==(const_iterator const& other) const
                {
                    return current == other.current;
                }

                [[nodiscard]]
                bool operator!=(const_iterator const& other) const
                {
                    return current != other.current;
                }

            private:
                linked_node<T> const* current;
            };

        public:
            linked_node(void) = delete;
            linked_node(linked_node<T> const&) = default;
            linked_node<T>& operator=(linked_node<T> const&) = default;
            linked_node(linked_node<T>&&) noexcept = default;
            linked_node<T>& operator=(linked_node<T>&&) noexcept = default;
            ~linked_node(void) = default;

            /// <summary>
            /// Constructs a new list with one element.
            /// </summary>
            /// <param name="value">The element to put into the list.</param>
            explicit linked_node(T const& value)
                : linked_node{ value, nullptr }
            {}

            /// <summary>
            /// Prepends one element to the start of an existing list as if creating a new list.
            /// </summary>
            /// <param name="value">The element to prepend to the list.</param>
            /// <param name="next">The list to extend.</param>
            explicit linked_node(T const& value, std::shared_ptr<linked_node<T> const> const& next)
                : _value{ value },
                _next{ next },
                _size{ next == nullptr ? std::size_t{ 1 } : next->size() + std::size_t{ 1 } }
            {}

            /// <summary>
            /// Returns the first element of the list.
            /// </summary>
            /// <returns>The <c>car</c> of this node.</returns>
            [[nodiscard]]
            T const& get_value(void) const
            {
                return _value;
            }

            /// <summary>
            /// Returns the first element of the list.
            /// </summary>
            /// <returns>The first element of the list.</returns>
            [[nodiscard]]
            T const& operator*(void) const
            {
                return _value;
            }

            /// <summary>
            /// Returns the remainder of this list without the first element.
            /// May be a shared pointer containing <c>nullptr</c>.
            /// </summary>
            /// <returns>The <c>cdr</c> of this node.</returns>
            [[nodiscard]]
            std::shared_ptr<linked_node<T> const> const& get_next(void) const
            {
                return _next;
            }

            [[nodiscard]]
            bool operator==(linked_node<T> const& other)
            {
                return _value == other._value
                    && _next == other._next;
            }

            [[nodiscard]]
            bool operator!=(linked_node<T> const& other)
            {
                return _value != other._value
                    || _next != other._next;
            }

            /// <summary>
            /// Returns an iterator to the first element.
            /// </summary>
            /// <returns>Iterator to the first element.</returns>
            [[nodiscard]]
            const_iterator cbegin(void) const
            {
                return const_iterator{ this };
            }

            /// <summary>
            /// Returns an iterator to the element following the last element.
            /// </summary>
            /// <returns>Iterator to the element following the last element.</returns>
            [[nodiscard]]
            const_iterator cend(void) const
            {
                return const_iterator{ nullptr };
            }

            /// <summary>
            /// Returns an iterator to the first element.
            /// </summary>
            /// <returns>Iterator to the first element.</returns>
            [[nodiscard]]
            const_iterator begin(void) const
            {
                return const_iterator{ this };
            }

            /// <summary>
            /// Returns an iterator to the element following the last element.
            /// </summary>
            /// <returns>Iterator to the element following the last element.</returns>
            [[nodiscard]]
            const_iterator end(void) const
            {
                return const_iterator{ nullptr };
            }

            /// <summary>
            /// Returns the number of elements in the container.
            /// </summary>
            /// <returns>The number of elements in the container.</returns>
            [[nodiscard]]
            std::size_t size(void) const
            {
                return _size;
            }

            [[nodiscard]]
            /// <summary>
            /// Checks if the container has no elements.
            /// </summary>
            /// <returns><see langword="true"/> if the container is empty, <see langword="false"/> otherwise.</returns>
            bool empty(void) const
            {
                return false;
            }

        private:
            T const _value;
            std::shared_ptr<linked_node<T> const> const _next;
            std::size_t const _size;
        };
    }
}
