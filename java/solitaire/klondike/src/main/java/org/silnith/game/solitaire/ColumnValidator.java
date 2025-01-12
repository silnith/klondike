package org.silnith.game.solitaire;

import org.silnith.game.Validator;


/**
 * Validates that a column conforms to the rules of Klondike solitaire.
 * 
 * <p>The primary validation is that the face-up cards form a valid run.
 * It also checks that any face-down cards have at least one face-up card on top of them.</p>
 */
public class ColumnValidator implements Validator<Column> {
	
	private final RunValidator runValidator;

    /**
     * Constructs a new column validator.
     * 
     * @param runValidator a run validator
     */
    public ColumnValidator(final RunValidator runValidator) {
        super();
        if (runValidator == null) {
        	throw new IllegalArgumentException("Run validator cannot be null.");
        }
        this.runValidator = runValidator;
    }
    
    @Override
    public void validate(final Column column) {
        if (column == null) {
            throw new IllegalArgumentException("Column cannot be null.");
        }
        if (column.hasFaceDownCards() && !column.hasFaceUpCards()) {
            throw new IllegalArgumentException("Column has face down cards but no face up cards.");
        }
        if (column.hasFaceUpCards()) {
        	runValidator.validate(column.getFaceUpCards());
        }
    }
    
}
