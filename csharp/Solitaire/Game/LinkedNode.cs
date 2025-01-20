using System;
using System.Collections;
using System.Collections.Generic;

namespace Silnith.Game
{
    /// <summary>
    /// An immutable, forwards-linked list.
    /// </summary>
    /// <remarks>
    /// <para>
    /// Instances of this list are created by concatenating a new element onto the beginning of an existing list.
    /// The advantage is that multiple lists can share the same tail safely,
    /// which can provide memory savings for certain specific use-cases.
    /// </para>
    /// <para>
    /// Lists of this type will always have at least one element if they are
    /// non-<see langword="null"/>.  An empty list cannot be represented by this implementation.
    /// </para>
    /// <para>
    /// Users of the language Lisp will recognize this paradigm as similar to the
    /// <c>car</c>, <c>cdr</c>, <c>cons</c> concept.
    /// </para>
    /// </remarks>
    /// <typeparam name="E">The type of elements in this list.</typeparam>
    public class LinkedNode<E> : IReadOnlyList<E>, IEquatable<LinkedNode<E>>
    {
        /// <summary>
        /// The first element of the list.
        /// </summary>
        public E Value
        {
            get;
        }

        /// <summary>
        /// The remainer of this list without the first element.
        /// May be <see langword="null"/>.
        /// </summary>
        public LinkedNode<E>? Next
        {
            get;
        }

        /// <inheritdoc cref="IReadOnlyCollection{T}.Count"/>
        public int Count
        {
            get;
        }

        /// <inheritdoc cref="P:System.Collections.Generic.IReadOnlyList`1.Item(System.Int32)"/>
        public E this[int index]
        {
            get
            {
                if (index < 0)
                {
                    throw new ArgumentOutOfRangeException(nameof(index), "Cannot be negative.");
                }
                LinkedNode<E>? next = this;
                while (index > 0)
                {
                    next = next.Next;
                    if (next is null)
                    {
                        throw new ArgumentOutOfRangeException(nameof(index), "Too large.");
                    }
                    index--;
                }
                return next.Value;
            }
        }

        /// <summary>
        /// Creates a new list with one element.
        /// </summary>
        /// <param name="e">The element to put into the list.</param>
        public LinkedNode(E e) : this(e, null)
        {
        }

        /// <summary>
        /// Appends one element to the start of an existing list as a new list.
        /// </summary>
        /// <param name="e">The element to prepend to the list.</param>
        /// <param name="list">The list to extend.</param>
        public LinkedNode(E e, LinkedNode<E>? list)
        {
            Value = e;
            Next = list;
            if (list is null)
            {
                Count = 1;
            }
            else
            {
                if (list.Count == int.MaxValue)
                {
                    Count = int.MaxValue;
                }
                else
                {
                    Count = list.Count + 1;
                }
            }
        }

        /// <inheritdoc/>
        public IEnumerator<E> GetEnumerator()
        {
            return new LinkedNodeEnumerator(this);
        }

        /// <inheritdoc/>
        IEnumerator IEnumerable.GetEnumerator()
        {
            return new LinkedNodeEnumerator(this);
        }

        /// <inheritdoc/>
        public override bool Equals(object? obj)
        {
            return Equals(obj as LinkedNode<E>);
        }

        /// <inheritdoc/>
        public bool Equals(LinkedNode<E>? other)
        {
            return other != null &&
                   EqualityComparer<E>.Default.Equals(Value, other.Value) &&
                   EqualityComparer<LinkedNode<E>?>.Default.Equals(Next, other.Next);
        }

        /// <inheritdoc/>
        public override int GetHashCode()
        {
            return HashCode.Combine(Value, Next);
        }

        private class LinkedNodeEnumerator : IEnumerator<E>
        {
            private LinkedNode<E>? position;

            public E Current
            {
                get;
                internal set;
            }

#pragma warning disable CS8603 // Possible null reference return.
            object IEnumerator.Current => Current;
#pragma warning restore CS8603 // Possible null reference return.

            public LinkedNodeEnumerator(LinkedNode<E> list)
            {
                position = list;
                /*
                 * This initial assignment is unnecessary according to the contract for the interface.
                 * But the nullability analysis won't shut up without it.
                 */
                Current = list.Value;
            }

            public void Dispose()
            {
            }

            public bool MoveNext()
            {
                if (position is null)
                {
                    return false;
                }
                Current = position.Value;
                position = position.Next;
                return true;
            }

            public void Reset()
            {
                throw new NotImplementedException();
            }
        }
    }
}
