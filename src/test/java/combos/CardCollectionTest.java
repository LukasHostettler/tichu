package combos;

import cards.Card;
import cards.DeckFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static combos.CardCollection.NONEXISTENT_COMBO_VALUE;
import static org.junit.jupiter.api.Assertions.*;

class CardCollectionTest {
    private Card blackKing;
    private Card redKing;
    private Card blueKing;
    private Card phoenix;
    private Card redQueen;
    private Card blueQueen;
    private Card dragon;

    CardCollectionTest() {
        blackKing = DeckFactory.createPokerCard(13, Card.Color.BLACK);
        redKing = DeckFactory.createPokerCard(13, Card.Color.RED);
        blueKing = DeckFactory.createPokerCard(13, Card.Color.BLUE);
        redQueen = DeckFactory.createPokerCard(12, Card.Color.RED);
        blueQueen = DeckFactory.createPokerCard(12, Card.Color.BLUE);

        phoenix = DeckFactory.createPhoenix();
        dragon = DeckFactory.createDragon();
    }

    private void addBlueRedKing(CardCollection c) {
        c.add(blueKing);
        c.add(redKing);
    }

    private void addBlueRedQueen(CardCollection c) {
        c.add(redQueen);
        c.add(blueQueen);
    }

    @Test
    void singleCardIsValidSingle() {
        var deck = DeckFactory.createDeck();
        for (var card : deck) {
            var singleCardCollection = new CardCollection();
            singleCardCollection.add(card);
            assertEquals(singleCardCollection.isSingle()!= NONEXISTENT_COMBO_VALUE, true);

        }
    }

    @Test
    void twoKingsIsAValidPair() {
        var possiblePair = new CardCollection();
        addBlueRedKing(possiblePair);
        assertEquals(possiblePair.isPair(), 13);
    }

    @Test
    void dragonIsNotAllowedInPair() {
        var possiblePair = new CardCollection();
        possiblePair.add(blackKing);
        possiblePair.add(dragon);
        assertEquals(possiblePair.isPair(), NONEXISTENT_COMBO_VALUE);
    }

    @Test
    void aKingAndAPhoenixIsAValidPair() {
        var possiblePair = new CardCollection();
        possiblePair.add(blackKing);
        possiblePair.add(phoenix);
        assertEquals(possiblePair.isPair(),13);
    }


    @Test
    void threeKingsIsAValidTriple() {
        var possibleTriple = new CardCollection();
        possibleTriple.add(blackKing);
        addBlueRedKing(possibleTriple);
        assertEquals(blackKing.getValue(),possibleTriple.isTriple());
    }

    @Test
    void twoKingsAndPhoenixIsValidTriple() {
        var possibleTriple = new CardCollection();
        possibleTriple.add(phoenix);
        addBlueRedKing(possibleTriple);
        assertEquals(blackKing.getValue(), possibleTriple.isTriple());
    }

    @Test
    void twoPairsAndPhoenixIsValidFullHouse() {
        var possibleFullhouse = new CardCollection();
        possibleFullhouse.add(phoenix);
        addBlueRedKing(possibleFullhouse);
        addBlueRedQueen(possibleFullhouse);
        assertEquals(blackKing.getValue(),possibleFullhouse.isFullHouse());
    }

    @Test
    void threeKingsAndAPairOfQueensIsValidFullHouse() {
        var possibleFullhouse = new CardCollection();
        possibleFullhouse.add(blackKing);
        addBlueRedKing(possibleFullhouse);
        addBlueRedQueen(possibleFullhouse);
        assertEquals(blackKing.getValue(),possibleFullhouse.isFullHouse());
    }

    @Test
    void twoPairsAndARandomCardIsInvalidFullHouse() {
        var possibleFullhouse = new CardCollection();
        addBlueRedKing(possibleFullhouse);
        possibleFullhouse.add(DeckFactory.createPokerCard(2, Card.Color.RED));
        addBlueRedQueen(possibleFullhouse);
        assertEquals(NONEXISTENT_COMBO_VALUE, possibleFullhouse.isFullHouse());
    }

    @Test
    void fiveConsequtiveCardsIsValidStraight() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(2, Card.Color.RED),
                DeckFactory.createPokerCard(6, Card.Color.RED),
                DeckFactory.createPokerCard(4, Card.Color.GREEN),
                DeckFactory.createPokerCard(5, Card.Color.RED),
                DeckFactory.createPokerCard(3, Card.Color.RED),
        };
        var possibleStraight = new CardCollection(Arrays.asList(cards));
        assertEquals(6,possibleStraight.isStraight());
    }

    @Test
    void fourConsequtiveCardsAndPhoenixIsValidStraight() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(7, Card.Color.RED),
                DeckFactory.createPokerCard(6, Card.Color.RED),
                DeckFactory.createPhoenix(),
                DeckFactory.createPokerCard(5, Card.Color.RED),
                DeckFactory.createPokerCard(3, Card.Color.RED),
        };
        var possibleStraight = new CardCollection(Arrays.asList(cards));
        assertEquals(7, possibleStraight.isStraight());
    }

    @Test
    void APhoenixCanNotMakeTheStraightGoBeyondTheAce() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(11, Card.Color.RED),
                DeckFactory.createPokerCard(12, Card.Color.RED),
                DeckFactory.createPhoenix(),
                DeckFactory.createPokerCard(13, Card.Color.RED),
                DeckFactory.createPokerCard(14, Card.Color.RED),
        };
        var possibleStraight = new CardCollection(Arrays.asList(cards));
        assertEquals(14, possibleStraight.isStraight());
    }

    @Test
    void MahjongIsPartOfValidStraight() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(2, Card.Color.RED),
                DeckFactory.createPokerCard(6, Card.Color.RED),
                DeckFactory.createPokerCard(4, Card.Color.GREEN),
                DeckFactory.createPokerCard(5, Card.Color.RED),
                DeckFactory.createPokerCard(3, Card.Color.RED),
                DeckFactory.createMahjong()
        };
        var possibleStraight = new CardCollection(Arrays.asList(cards));
        assertEquals(6,possibleStraight.isStraight());
    }

    @Test
    void DragonDoesNotBelongInStraight() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(11, Card.Color.RED),
                DeckFactory.createPokerCard(13, Card.Color.RED),
                DeckFactory.createPokerCard(12, Card.Color.GREEN),
                DeckFactory.createPokerCard(10, Card.Color.RED),
                DeckFactory.createDragon()
        };
        var possibleStraight = new CardCollection(Arrays.asList(cards));
        assertEquals(NONEXISTENT_COMBO_VALUE, possibleStraight.isStraight());
    }

    @Test
    void PairsOfCardsIsValidPairsOfCard() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(6, Card.Color.BLUE),
                DeckFactory.createPokerCard(5, Card.Color.RED),
                DeckFactory.createPokerCard(7, Card.Color.BLACK),
                DeckFactory.createPokerCard(7, Card.Color.RED),
                DeckFactory.createPokerCard(5, Card.Color.GREEN),
                DeckFactory.createPokerCard(6, Card.Color.RED),
        };
        assertEquals(7,new CardCollection(Arrays.asList(cards)).isConsequtivePair());
    }

    @Test
    void PairsOfCardsWithPhoenixIsValidPairsOfCard() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(6, Card.Color.BLUE),
                DeckFactory.createPokerCard(5, Card.Color.RED),
                DeckFactory.createPhoenix(),
                DeckFactory.createPokerCard(7, Card.Color.RED),
                DeckFactory.createPokerCard(5, Card.Color.GREEN),
                DeckFactory.createPokerCard(6, Card.Color.RED),
        };
        assertEquals(7,new CardCollection(Arrays.asList(cards)).isConsequtivePair());
    }

    @Test
    void PairsOfCardsWithPhoenixLastCardIsValidatedToo() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(6, Card.Color.BLUE),
                DeckFactory.createPokerCard(5, Card.Color.RED),
                DeckFactory.createPhoenix(),
                DeckFactory.createPokerCard(8, Card.Color.RED),
                DeckFactory.createPokerCard(5, Card.Color.GREEN),
                DeckFactory.createPokerCard(6, Card.Color.RED),
        };
        assertEquals(NONEXISTENT_COMBO_VALUE,new CardCollection(Arrays.asList(cards)).isConsequtivePair());
    }

    @Test
    void PairsOfCardsWithTripletIsInvalidPairsOfCard() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(6, Card.Color.BLUE),
                DeckFactory.createPokerCard(6, Card.Color.RED),
                DeckFactory.createPhoenix(),
                DeckFactory.createPokerCard(7, Card.Color.RED),
                DeckFactory.createPokerCard(6, Card.Color.GREEN),
                DeckFactory.createPokerCard(5, Card.Color.BLUE)
        };
        assertEquals(NONEXISTENT_COMBO_VALUE, new CardCollection(Arrays.asList(cards)).isConsequtivePair());
    }

    @Test
    void QuartetIsValidBomb() {
        var cards = new Card[]{
                DeckFactory.createPokerCard(6, Card.Color.BLUE),
                DeckFactory.createPokerCard(6, Card.Color.BLACK),
                DeckFactory.createPokerCard(6, Card.Color.RED),
                DeckFactory.createPokerCard(6, Card.Color.GREEN),
        };
        assertEquals(6, new CardCollection(Arrays.asList(cards)).isQuartetBomb());
    }

}