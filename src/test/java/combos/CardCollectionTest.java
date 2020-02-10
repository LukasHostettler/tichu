package combos;

import cards.Card;
import cards.DeckFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardCollectionTest {
    private Card blackKing;
    private Card redKing;
    private Card blueKing;
    private Card phoenix;
    private Card redQueen;
    private Card blueQueen;
    private Card dragon;
    CardCollectionTest(){
        blackKing = DeckFactory.createPokerCard(13, Card.Color.BLACK);
        redKing = DeckFactory.createPokerCard(13,Card.Color.RED);
        blueKing = DeckFactory.createPokerCard(13,Card.Color.BLUE);
        redQueen = DeckFactory.createPokerCard(12,Card.Color.RED);
        blueQueen = DeckFactory.createPokerCard(12,Card.Color.BLUE);

        phoenix = DeckFactory.createPhoenix();
        dragon = DeckFactory.createDragon();
    }
    private void addBlueRedKing(CardCollection c){
        c.add(blueKing);
        c.add(redKing);
    }

    private void addBlueRedQueen(CardCollection c){
        c.add(redQueen);
        c.add(blueQueen);
    }

    @Test
    void singleCardIsValidSingle(){
        var deck = DeckFactory.createDeck();
        for (var card:deck) {
            var singleCardCollection = new CardCollection();
            singleCardCollection.add(card);
            assertEquals(singleCardCollection.isSingle(), true);

        }
    }

    @Test
    void twoKingsIsAValidPair(){
        var possiblePair = new CardCollection();
        addBlueRedKing(possiblePair);
        assertEquals(possiblePair.isPair(),true);
    }

    @Test
    void dragonIsNotAllowedInPair(){
        var possiblePair = new CardCollection();
        possiblePair.add(blackKing);
        possiblePair.add(dragon);
        assertEquals(possiblePair.isPair(),false);
    }

    @Test
    void aKingAndAPhoenixIsAValidPair(){
        var possiblePair = new CardCollection();
        possiblePair.add(blackKing);
        possiblePair.add(phoenix);
        assertTrue(possiblePair.isPair());
    }



    @Test
    void threeKingsIsAValidTripple(){
        var possibleTripple = new CardCollection();
        possibleTripple.add(blackKing);
        addBlueRedKing(possibleTripple);
        assertEquals(possibleTripple.isTriple(),true);
    }

    @Test
    void twoKingsAndPhoenixIsValidTripple(){
        var possibleTripple = new CardCollection();
        possibleTripple.add(phoenix);
        addBlueRedKing(possibleTripple);
        assertEquals(possibleTripple.isTriple(),true);
    }

    @Test
    void twoPairsAndPhoenixIsValidFullHouse(){
        var possibleFullhouse = new CardCollection();
        possibleFullhouse.add(phoenix);
        addBlueRedKing(possibleFullhouse);
        addBlueRedQueen(possibleFullhouse);
        assertEquals(possibleFullhouse.isFullHouse(),true);
    }
    @Test
    void threeKingsAndAPairOfQueensIsValidFullHouse(){
        var possibleFullhouse = new CardCollection();
        possibleFullhouse.add(blackKing);
        addBlueRedKing(possibleFullhouse);
        addBlueRedQueen(possibleFullhouse);
        assertEquals(possibleFullhouse.isFullHouse(),true);
    }
    @Test
    void twoPairsAndARandomCardIsInvalidFullHouse(){
        var possibleFullhouse = new CardCollection();
        addBlueRedKing(possibleFullhouse);
        possibleFullhouse.add(DeckFactory.createPokerCard(2, Card.Color.RED));
        addBlueRedQueen(possibleFullhouse);
        assertEquals(possibleFullhouse.isFullHouse(),false);
    }

}