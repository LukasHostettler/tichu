package combos;

import cards.Card;

import java.util.*;
import java.util.stream.Collectors;

;

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
    public Optional<Card> peek(){
        var numItems = this.size();
        if(numItems>1){
            var card = this.get(numItems-1);
            return Optional.of(card);
        }
        else{
            return Optional.empty();
        }
    }
    public Optional<Card> pop(){
        var numItems = this.size();
        if(numItems>=1){
            var card = this.remove(numItems-1);
            return Optional.of(card);
        }
        else{
            return Optional.empty();
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
    public List<CardCollection> superset(){
        int n = this.size();
        var subcombos = new LinkedList<CardCollection>();
        // Run a loop for printing all 2^n
        // subsets one by one
        for (int i = 0; i < (1<<n); i++)
        {
            var subcombo = new CardCollection();
            // Print current subset
            for (int j = 0; j < n; j++)

                // (1<<j) is a number with jth bit 1
                // so when we 'and' them with the
                // subset number we get which numbers
                // are present in the subset and which
                // are not
                if ((i & (1 << j)) > 0)
                    subcombo.add(this.get(j));

            subcombos.add(subcombo);
        }
        return subcombos;
    }
    public List<CardCollection> subCombosOfType(ComboType comboType){
        var superSet= superset();
        //var ofSameLength = superSet.stream().filter(); optimization
        return superSet.stream().filter(x->x.getComboType() == comboType).collect(Collectors.toList());

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

    public ComboType getComboType() {
        if(isSingle())
            return ComboType.Single;
        if(isPair())
            return ComboType.Pair;
        if(isTriple())
            return  ComboType.Tripple;
        if(isFullHouse())
            return ComboType.FullHouse;
        if(isConsequtivePair()){
            switch (size()){
                case 2:
                    return ComboType.Consequtive2Pairs;
                case 3:
                    return ComboType.Consequtive3Pairs;
                case 4:
                    return ComboType.Consequtive4Pairs;
                case 5:
                    return ComboType.Consequtive5Pairs;
                case 6:
                    return ComboType.Consequtive6Pairs;
                case 7:
                    return ComboType.Consequtive7Pairs;
                default:
                    throw new Error("longer combo than ever thought");

            }
        }
        if(isStraightBomb()){
            switch (size()){
                case 5:
                    return ComboType.Straight5Bomb;
                case 6:
                    return ComboType.Straight6Bomb;
                case 7:
                    return ComboType.Straight7Bomb;
                case 8:
                    return ComboType.Straight8Bomb;
                case 9:
                    return ComboType.Straight9Bomb;
                case 10:
                    return ComboType.Straight10Bomb;
                case 11:
                    return ComboType.Straight11Bomb;
                case 12:
                    return ComboType.Straight12Bomb;
                case 13:
                    return ComboType.Straight13Bomb;
                default:
                    throw new Error("straightbomb of unexpected size");
            }
        }
        if(isStraight()){
            switch (size()){
                case 5:
                    return ComboType.Straight5;
                case 6:
                    return ComboType.Straight6;
                case 7:
                    return ComboType.Straight7;
                case 8:
                    return ComboType.Straight8;
                case 9:
                    return ComboType.Straight9;
                case 10:
                    return ComboType.Straight10;
                case 11:
                    return ComboType.Straight11;
                case 12:
                    return ComboType.Straight12;
                case 13:
                    return ComboType.Straight13;
                case 14:
                    return ComboType.Straight14;
                default:
                    throw new Error("straightbomb of unexpected size");
            }
        }


        return ComboType.None;
    }

    public int getAccountValue(){
        return this.stream().map(x->x.getAccountValue()).reduce(0,Integer::sum);
    }
}
