package cards;

import combos.CardCollection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeckFactory {
    private static final List<Card.Color> validColors = Arrays.asList(Card.Color.RED, Card.Color.GREEN, Card.Color.BLUE, Card.Color.BLACK);

    public static CardCollection createDeck() {
        var cards = new CardCollection();
        var cardValues = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        for (var color : Card.Color.values()) {
            if (color == Card.Color.NONE)
                continue;
            for (var value : cardValues) {
                var card = createPokerCard(value, color);
                cards.add(card);
            }
        }
        cards.add(createDog());
        cards.add(createMahjong());
        cards.add(createDragon());
        cards.add(createPhoenix());
        return cards;
    }

    public static CardCollection createShuffledDeck() {
        var deck = createDeck();
        Collections.shuffle(deck);
        return deck;
    }

    public static Card createDog() {
        return new Dog();
    }

    public static Card createMahjong() {
        return new Mahjong();
    }

    public static Card createDragon() {
        return new Dragon();
    }

    public static Card createPhoenix() {
        return new Phoenix();
    }

    public static Card createPokerCard(int value, Card.Color color) {
        if (value < 2 || value > 14) throw new IllegalArgumentException("Value must be is between 2 and 14");
        if (!validColors.contains(color)) throw new IllegalArgumentException("invalid color");
        var card = new Card();
        card.color = color;
        card.value = value;
        if (value == 5)
            card.accountValue = 5;
        else if (value == 10 || value == 13)
            card.accountValue = 10;
        else
            card.accountValue = 0;
        return card;
    }

}
