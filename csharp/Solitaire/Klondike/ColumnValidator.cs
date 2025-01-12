using Silnith.Game.Deck;
using System;
using System.Collections.Generic;

namespace Silnith.Game.Klondike
{
    /// <summary>
    /// Validates that a column conforms to the rules of Klondike solitaire.
    /// </summary>
    /// <remarks>
    /// <para>
    /// The primary validation is that the face-up cards form a valid run.
    /// It also checks that any face-down cards have at least one face-up card on top of them.
    /// </para>
    /// </remarks>
    public class ColumnValidator : IValidator<Column>
    {
        private readonly IValidator<IEnumerable<Card>> runValidator;

        /// <summary>
        /// Constructs a new column validator.
        /// </summary>
        /// <param name="runValidator">A run validator.</param>
        public ColumnValidator(IValidator<IEnumerable<Card>> runValidator)
        {
            this.runValidator = runValidator ?? throw new ArgumentNullException(nameof(runValidator));
        }

        /// <inheritdoc/>
        public void Validate(Column column)
        {
            if (column.HasFaceDownCards() && !column.HasFaceUpCards())
            {
                throw new ArgumentException("Column has face-down cards but no face-up cards.");
            }
            if (column.HasFaceUpCards())
            {
                runValidator.Validate(column.FaceUp);
            }
        }
    }
}
