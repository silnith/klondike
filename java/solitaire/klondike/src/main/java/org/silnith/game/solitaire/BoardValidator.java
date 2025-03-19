package org.silnith.game.solitaire;

import java.util.List;
import java.util.Map;

import org.silnith.deck.Card;
import org.silnith.deck.Suit;
import org.silnith.game.Validator;


/**
 * A validator for the entire board in a game of Klondike solitaire.
 * 
 * <p>This validates that the foundation is properly ordered, that runs
 * in each column conform to the rules, and that the total number of
 * cards matches the expected size of a deck.</p>
 */
public class BoardValidator implements Validator<Board> {
    
    private final int numberOfColumns;
    
    private final int deckSize;
    
    private final ColumnValidator columnValidator;
    
    private final FoundationValidator foundationValidator;
    
    /**
     * Creates a new board validator.
     * 
     * @param numberOfColumns the number of columns on the board.  In a normal game, this is {@code 7}.
     * @param deckSize the size of the deck.  In a normal game, this is {@code 52}.
     * @param columnValidator a column validator
     * @param foundationValidator a foundation validator
     */
    public BoardValidator(final int numberOfColumns, final int deckSize, final ColumnValidator columnValidator,
            final FoundationValidator foundationValidator) {
        super();
        this.numberOfColumns = numberOfColumns;
        this.deckSize = deckSize;
        this.columnValidator = columnValidator;
        this.foundationValidator = foundationValidator;
    }
    
    @Override
    public void validate(final Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }
        final Map<Suit, List<Card>> foundation = board.getFoundation();
        foundationValidator.validate(foundation);
        final List<Column> columns = board.getColumns();
        if (columns == null) {
            throw new IllegalArgumentException("Board has no list of columns.");
        }
        if (columns.size() != numberOfColumns) {
            throw new IllegalArgumentException(
                    "Board must have " + numberOfColumns + " columns, instead has " + columns.size());
        }
        for (final Column column : columns) {
            columnValidator.validate(column);
        }
        final List<Card> stockPile = board.getStockPile();
        if (stockPile == null) {
            throw new IllegalArgumentException("Board stock pile cannot be null.");
        }
        final int stockPileIndex = board.getStockPileIndex();
        if (stockPileIndex < 0) {
            throw new IllegalArgumentException("Stock pile index must be non-negative, is " + stockPileIndex + ".");
        }
        if (stockPileIndex > stockPile.size()) {
            throw new IllegalArgumentException("Stock pile index must not be larger than the stock pile size.  Stock pile has "
                    + stockPile.size() + " cards, stock pile index is " + stockPileIndex + ".");
        }
        // count the cards
        int numCards = stockPile.size();
        for (final Suit suit : Suit.values()) {
            numCards += foundation.get(suit).size();
        }
        for (final Column column : columns) {
            numCards += column.getNumberOfFaceDownCards();
            numCards += column.getNumberOfFaceUpCards();
        }
        if (numCards != deckSize) {
            throw new IllegalArgumentException(
                    "Must have " + deckSize + " cards on the board, instead has " + numCards + ".");
        }
    }
    
}
