package org.silnith.game.solitaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.silnith.deck.Suit.CLUB;
import static org.silnith.deck.Suit.DIAMOND;
import static org.silnith.deck.Suit.HEART;
import static org.silnith.deck.Suit.SPADE;
import static org.silnith.deck.Value.ACE;
import static org.silnith.deck.Value.EIGHT;
import static org.silnith.deck.Value.FOUR;
import static org.silnith.deck.Value.JACK;
import static org.silnith.deck.Value.KING;
import static org.silnith.deck.Value.NINE;
import static org.silnith.deck.Value.QUEEN;
import static org.silnith.deck.Value.SEVEN;
import static org.silnith.deck.Value.SIX;
import static org.silnith.deck.Value.TEN;
import static org.silnith.deck.Value.TWO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.silnith.deck.Card;


public class ColumnTest {
    
    @Test
    public void testColumnEmpty() {
    	final List<Card> faceDown = Collections.emptyList();
    	final List<Card> faceUp = Collections.emptyList();
    	
        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnDownNull() {
    	final List<Card> faceDown = null;
    	final List<Card> faceUp = Collections.emptyList();

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnUpNull() {
    	final List<Card> faceDown = Collections.emptyList();
    	final List<Card> faceUp = null;

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnNullNull() {
    	final List<Card> faceDown = null;
    	final List<Card> faceUp = null;

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnWithDown() {
		final List<Card> faceDown = Arrays.asList(
				new Card(FOUR, SPADE),
				new Card(JACK, HEART),
				new Card(ACE, HEART));
        final List<Card> faceUp = Collections.emptyList();

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnWithUp() {
        final List<Card> faceDown = Collections.emptyList();
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
                new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testColumnWithBoth() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));

        new Column(faceDown, faceUp);
    }
    
    @Test
    public void testHasFaceDownCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertTrue(column.hasFaceDownCards());
    }
    
    @Test
    public void testHasFaceDownCardsNull() {
        final List<Card> faceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(null, faceUp);
                
        assertFalse(column.hasFaceDownCards());
    }
    
    @Test
    public void testHasFaceDownCardsEmpty() {
        final List<Card> faceDown = Collections.emptyList();
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
                new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                
        assertFalse(column.hasFaceDownCards());
    }
    
    @Test
    public void testHasFaceUpCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertTrue(column.hasFaceUpCards());
    }
    
    @Test
    public void testHasFaceUpCardsNull() {
        final List<Card> faceDown = Collections.emptyList();
		final Column column = new Column(faceDown, null);
        
        assertFalse(column.hasFaceUpCards());
    }
    
    @Test
    public void testHasFaceUpCardsEmpty() {
        final List<Card> faceDown = Collections.emptyList();
        final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
        
        assertFalse(column.hasFaceUpCards());
    }
    
    @Test
    public void testHasFaceUpCardsAfterFlipFromDown() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
                
        assertTrue(column.hasFaceUpCards());
    }
    
    @Test
    public void testGetNumberOfFaceDownCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertEquals(3, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetNumberOfFaceDownCardsNull() {
        final List<Card> faceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(null, faceUp);
                
        assertEquals(0, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetNumberOfFaceDownCardsEmpty() {
        final List<Card> faceDown = Collections.emptyList();
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
                new Card(QUEEN, DIAMOND),
                new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                
        assertEquals(0, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetNumberOfFaceUpCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertEquals(4, column.getNumberOfFaceUpCards());
    }
    
    @Test
    public void testGetNumberOfFaceUpCardsNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
                
        assertEquals(1, column.getNumberOfFaceUpCards());
    }
    
    @Test
    public void testGetNumberOfFaceUpCardsEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
        final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
                
        assertEquals(1, column.getNumberOfFaceUpCards());
    }
    
    @Test
    public void testFlippedCardRemovedFromDownNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
                
        assertEquals(2, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testFlippedCardRemovedFromDownEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
                
        assertEquals(2, column.getNumberOfFaceDownCards());
    }
    
    @Test
    public void testGetTopCard() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertEquals(new Card(TEN, DIAMOND), column.getTopCard());
    }
    
    @Test
    public void testGetTopCardNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
        
        assertEquals(new Card(ACE, HEART), column.getTopCard());
    }
    
    @Test
    public void testGetTopCardEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
        
        assertEquals(new Card(ACE, HEART), column.getTopCard());
    }
    
    @Test
    public void testGetFaceUpCards() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
        final List<Card> actual = column.getFaceUpCards();
        
		final List<Card> expected = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFaceUpCardsNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final Column column = new Column(faceDown, null);
                
        final List<Card> actual = column.getFaceUpCards();
        
		final List<Card> expected = Collections.singletonList(
				new Card(ACE, HEART));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFaceUpCardsEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Collections.emptyList();
		final Column column = new Column(faceDown, faceUp);
        
        final List<Card> actual = column.getFaceUpCards();
        
		final List<Card> expected = Collections.singletonList(
				new Card(ACE, HEART));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCardsOverflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.getTopCards(5);
        });
    }
    
    @Test
    public void testGetTopCards4() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final List<Card> actual = column.getTopCards(4);
        
		final List<Card> expected = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
                new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCards3() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final List<Card> actual = column.getTopCards(3);
        
		final List<Card> expected = Arrays.asList(
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
				new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCards2() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
		final List<Card> actual = column.getTopCards(2);
		
        final List<Card> expected = Arrays.asList(
        		new Card(JACK, CLUB),
        		new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCards1() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
		
		final List<Card> actual = column.getTopCards(1);
		
        final List<Card> expected = Collections.singletonList(
        		new Card(TEN, DIAMOND));
		assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTopCardsUnderflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.getTopCards(0);
        });
    }
    
    @Test
    public void testColumnMissingTopCardsOverflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
              
        assertThrows(IllegalArgumentException.class, () -> {
        	column.getColumnMissingTopCards(5);
        });
    }
    
    @Test
    public void testColumnMissingTopCards4() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
        final Column actual = column.getColumnMissingTopCards(4);
        
        final List<Card> expectedFaceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART));
		final List<Card> expectedFaceUp = Collections.singletonList(
				new Card(ACE, HEART));
		final Column expected = new Column(expectedFaceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testColumnMissingTopCards3() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
		
        final Column actual = column.getColumnMissingTopCards(3);
        
        final List<Card> expectedFaceUp = Collections.singletonList(
        		new Card(KING, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testColumnMissingTopCards2() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
        final Column actual = column.getColumnMissingTopCards(2);
        
        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testColumnMissingTopCards1() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
        
        final Column actual = column.getColumnMissingTopCards(1);
        
        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testColumnMissingTopCardsUnderflow() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        assertThrows(IllegalArgumentException.class, () -> {
        	column.getColumnMissingTopCards(0);
        });
    }
    
    @Test
    public void testAddNewCard() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final Column actual = column.addNewCard(new Card(NINE, CLUB));
        
        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND),
		        new Card(NINE, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testAddNewCardNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);

        assertThrows(RuntimeException.class, () -> {
        	column.addNewCard(null);
        });
    }
    
    @Test
    public void testAddNewCards1() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column column = new Column(faceDown, faceUp);
                        
        final List<Card> run = Collections.singletonList(
        		new Card(NINE, CLUB));
		final Column actual = column.addNewCards(run);
        
        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND),
		        new Card(NINE, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testAddNewCards3() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		final Column originalColumn = new Column(faceDown, faceUp);

        final List<Card> run = Arrays.asList(
        		new Card(NINE, CLUB),
        		new Card(EIGHT, DIAMOND),
        		new Card(SEVEN, CLUB));
		final Column actual = originalColumn.addNewCards(run);

        final List<Card> expectedFaceUp = Arrays.asList(
        		new Card(KING, CLUB),
        		new Card(QUEEN, DIAMOND),
        		new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND),
		        new Card(NINE, CLUB),
		        new Card(EIGHT, DIAMOND),
		        new Card(SEVEN, CLUB));
		final Column expected = new Column(faceDown, expectedFaceUp);
		assertEquals(expected, actual);
    }
    
    @Test
    public void testAddNewCardsEmpty() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		
		final Column column = new Column(faceDown, faceUp);
		
        assertThrows(IllegalArgumentException.class, () -> {
        	column.addNewCards(Collections.emptyList());
        });
    }
    
    @Test
    public void testAddNewCardsNull() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(FOUR, SPADE),
        		new Card(JACK, HEART),
        		new Card(ACE, HEART));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(KING, CLUB),
				new Card(QUEEN, DIAMOND),
				new Card(JACK, CLUB),
		        new Card(TEN, DIAMOND));
		
		final Column column = new Column(faceDown, faceUp);
                       
        assertThrows(IllegalArgumentException.class, () -> {
        	column.addNewCards(null);
        });
    }
    
    @Test
    public void testEquals() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown, faceUp);
        final Column column2 = new Column(faceDown, faceUp);
                
        assertTrue(column1.equals(column2));
    }
    
    @Test
    public void testHashCode() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown, faceUp);
        final Column column2 = new Column(faceDown, faceUp);
                
        assertEquals(column1.hashCode(), column2.hashCode());
    }
    
    @Test
    public void testEqualsFaceDownDiffer() {
        final List<Card> faceDown1 = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        final List<Card> faceDown2 = Arrays.asList(
        		new Card(TWO, SPADE),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown1, faceUp);
		final Column column2 = new Column(faceDown2, faceUp);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceDownShorter() {
        final List<Card> faceDown1 = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        final List<Card> faceDown2 = Collections.emptyList();
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown1, faceUp);
		final Column column2 = new Column(faceDown2, faceUp);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceDownLonger() {
        final List<Card> faceDown1 = Collections.emptyList();
        final List<Card> faceDown2 = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
		
		final Column column1 = new Column(faceDown1, faceUp);
		final Column column2 = new Column(faceDown2, faceUp);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceUpDiffer() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp1 = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
        final List<Card> faceUp2 = Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, HEART),
        		new Card(SIX, CLUB));
        
		final Column column1 = new Column(faceDown, faceUp1);
		final Column column2 = new Column(faceDown, faceUp2);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceUpShorter() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp1 = Arrays.asList(
				new Card(EIGHT, SPADE),
				new Card(SEVEN, DIAMOND),
				new Card(SIX, CLUB));
        final List<Card> faceUp2 = Collections.emptyList();
        
		final Column column1 = new Column(faceDown, faceUp1);
		final Column column2 = new Column(faceDown, faceUp2);
                
        assertFalse(column1.equals(column2));
    }
    
    @Test
    public void testEqualsFaceUpLonger() {
        final List<Card> faceDown = Arrays.asList(
        		new Card(TWO, HEART),
        		new Card(KING, DIAMOND));
        
		final List<Card> faceUp1 = Collections.emptyList();
        final List<Card> faceUp2 = Arrays.asList(
        		new Card(EIGHT, SPADE),
        		new Card(SEVEN, DIAMOND),
        		new Card(SIX, CLUB));
        
		final Column column1 = new Column(faceDown, faceUp1);
		final Column column2 = new Column(faceDown, faceUp2);
                
        assertFalse(column1.equals(column2));
    }
    
}
