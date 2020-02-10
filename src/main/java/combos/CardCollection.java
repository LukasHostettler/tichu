package combos;

import cards.Card;

import java.util.ArrayList;
import java.util.Arrays;


public class CardCollection extends ArrayList<Card> {
    public CardCollection() {
        super();
    }

    public CardCollection(Iterable<Card> c) {
        super();
        for (var card : c) {
            if (card == null) {
                throw new NullPointerException();
            }
            this.add(card);
        }
    }


    private static int byValue(Card c1, Card c2) {
        return (int) (c1.getValue() - c2.getValue());
    }

    private boolean containsPhoenix() {
        for (var item : this) {
            if (item.isPhoenix())
                return true;
        }

        return false;
    }

    private CardCollection withoutPhoenix() {
        var collection = new CardCollection();
        for (var item : this) {
            if (!item.isPhoenix()) {
                collection.add(item);
            }
        }
        return collection;
    }

    private boolean allEqualColor() {
        Card.Color color = null;
        for (var item : this) {
            if (color == null) {
                color = item.getColor();
            } else {
                if (item.getColor() != color)
                    return false;
            }

        }
        return true;
    }

    private boolean allEqualValue() {
        Double value = null;
        for (var item : this) {
            if (value == null) {
                value = item.getValue();
            } else {
                if (item.getValue() != value)
                    return false;
            }
        }
        return true;
    }

    public boolean isSingle() {
        return this.size() == 1;
    }

    public boolean isPair() {
        if (this.size() != 2 || containsNonComboCard())
            return false;
        if (this.containsPhoenix()) {
            var withoutPhoenix = this.withoutPhoenix();
            return withoutPhoenix.isSingle();
        } else {
            return this.allEqualValue();
        }
    }

    public boolean isTriple() {
        if (this.size() != 3 || containsNonComboCard())
            return false;
        if (this.containsPhoenix()) {
            var withoutPhoenix = this.withoutPhoenix();
            return withoutPhoenix.isPair();
        } else {
            return this.allEqualValue();
        }
    }

    public boolean isQuadruple() {
        if (this.size() != 4 || containsNonComboCard())
            return false;
        if (this.containsPhoenix()) {
            var withoutPhoenix = this.withoutPhoenix();
            return withoutPhoenix.isTriple();
        } else {
            return this.allEqualValue();
        }
    }

    private boolean containsNonComboCard() {
        for (var card : this) {
            if (card.isSpecialCard() && !card.isPhoenix())
                return true;
        }
        return false;
    }

    public boolean isFullHouse() {
        if (size() != 5 || containsNonComboCard()) {
            return false;
        }
        var copy = new CardCollection(this);
        if (copy.containsPhoenix()) {
            var withoutPhoenix = copy.withoutPhoenix();
            copy.sort(CardCollection::byValue);
            // either  pair pair
            var pair1 = new CardCollection(withoutPhoenix.subList(0, 2));
            var pair2 = new CardCollection(withoutPhoenix.subList(2, 4));
            if (pair1.isPair() && pair2.isPair())
                return true;
            else {
                // or single triple
                var triple1 = new CardCollection(withoutPhoenix.subList(0, 3));
                var triple2 = new CardCollection(withoutPhoenix.subList(1, 4));
                return triple1.isTriple() || triple2.isTriple();
            }
        }
        copy.sort(CardCollection::byValue);

        var triple1 = new CardCollection(copy.subList(0, 3));
        if (triple1.isTriple()) {
            var pair1 = new CardCollection(copy.subList(3, 5));
            return pair1.isPair();
        }
        var triple2 = new CardCollection(copy.subList(2, 5));
        if (triple2.isTriple()) {
            var pair2 = new CardCollection(copy.subList(0, 2));
            return pair2.isPair();
        }
        return false;
    }

    public boolean isStraight() {
        if (size() < 5)
            return false;
        var copy = new CardCollection(this);
        copy.sort(Card::compareTo);
        var numSkips = 0;
        if (copy.containsPhoenix()) {
            numSkips = 1;
            copy = copy.withoutPhoenix();
        }
        Double value = null;
        for (var card : copy) {
            if (value != null) {
                if (card.getValue() != value + 1.0) {
                    numSkips -= 1;
                }
                if (numSkips < 0) {
                    return false;
                }
            }
            value = card.getValue();
        }
        return true;
    }


    boolean isConsequtivePair() {
        if (size() < 4 || size() % 2 == 1)
            return false;
        if (containsNonComboCard())
            return false;
        var hasPhoenix = this.containsPhoenix();
        var withoutPhoenix = this.withoutPhoenix();
        withoutPhoenix.sort(Card::compareTo);
        Double lastValue = null;
        for (int index = 0; index < withoutPhoenix.size() - 1; index = index + 2) {
            var cards = new Card[]{withoutPhoenix.get(index), withoutPhoenix.get(index + 1)};
            if (lastValue != null && cards[0].getValue() != lastValue + 1)
                return false;
            lastValue = cards[0].getValue();

            var testPair = new CardCollection(Arrays.asList(cards));
            if (testPair.isPair()) {
                continue;
            }
            if (!hasPhoenix) {
                return false;
            } else {
                hasPhoenix = false;
                index -= 1; // test the other pair as well.
            }
        }
        return true;
    }


    boolean isQuartetBomb() {
        if (this.size() != 4)
            return false;
        if (this.containsPhoenix())
            return false;
        return isQuadruple();
    }

    boolean isStraightBomb() {
        return isStraight() && allEqualColor();
    }
}
