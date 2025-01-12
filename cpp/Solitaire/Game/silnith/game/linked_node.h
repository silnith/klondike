#pragma once

#include <memory>

namespace silnith
{
    namespace game
    {
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

                T const operator*(void) const
                {
                    return current->get_value();
                }

                const_iterator& operator++(void)
                {
                    std::shared_ptr<linked_node<T>> next_ptr{ current->get_next() };
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
                    std::shared_ptr<linked_node<T>> next_ptr{ current->get_next() };
                    current = next_ptr.get();
                    return const_iterator{ copy };
                }

                bool operator==(const_iterator const& other) const
                {
                    return current == other.current;
                }

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
            explicit linked_node(T const& value) : value_{ value }, next_{ nullptr }
            {}

            /// <summary>
            /// Prepends one element to the start of an existing list as if creating a new list.
            /// </summary>
            /// <param name="value">The element to prepend to the list.</param>
            /// <param name="next">The list to extend.</param>
            explicit linked_node(T const& value, std::shared_ptr<linked_node<T>> const& next) : value_{ value }, next_{ next }
            {}

            T get_value(void) const
            {
                return value_;
            }

            std::shared_ptr<linked_node<T>> get_next(void) const
            {
                return next_;
            }

            bool operator==(linked_node<T> const& other)
            {
                return value_ == other.value_
                    && next_ == other.next_;
            }

            bool operator!=(linked_node<T> const& other)
            {
                return value_ != other.value_
                    || next_ != other.next_;
            }

            T& operator*(void) const
            {
                return value_;
            }

            const_iterator cbegin(void) const
            {
                return const_iterator{ this };
            }

            const_iterator cend(void) const
            {
                return const_iterator{ nullptr };
            }

        private:
            T const value_;
            std::shared_ptr<linked_node<T>> const next_;
        };
    }
}
