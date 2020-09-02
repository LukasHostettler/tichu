package combos;

import cards.Card;

import java.util.*;
import java.util.stream.Collectors;

;

public class CardCollection extends ArrayList<Card> {
    public static final double NONEXISTENT_COMBO_VALUE = -1.0;
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

    private Double allEqualValue() {
        Double value = null;
        for (var item : this) {
            if (value == null) {
                value = item.getValue();
            } else {
                if (item.getValue() != value)
                    return NONEXISTENT_COMBO_VALUE;
            }
        }
        return value;
    }

    public Double isSingle() {
        return this.size() == 1 ? this.get(0).getValue() : NONEXISTENT_COMBO_VALUE;
    }

    public Double isPair() {
        if (this.size() != 2 || containsNonComboCard())
            return NONEXISTENT_COMBO_VALUE;
        if (this.containsPhoenix()) {
            var withoutPhoenix = this.withoutPhoenix();
            return withoutPhoenix.isSingle();
        } else {
            return this.allEqualValue();
        }
    }

    public Double isTriple() {
        if (this.size() != 3 || containsNonComboCard())
            return NONEXISTENT_COMBO_VALUE;
        if (this.containsPhoenix()) {
            var withoutPhoenix = this.withoutPhoenix();
            return withoutPhoenix.isPair();
        } else {
            return this.allEqualValue();
        }
    }

    public Double isQuadruple() {
        if (this.size() != 4 || containsNonComboCard())
            return NONEXISTENT_COMBO_VALUE;
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

    public Double isFullHouse() {
        if (size() != 5 || containsNonComboCard()) {
            return NONEXISTENT_COMBO_VALUE;
        }
        var copy = new CardCollection(this);
        if (copy.containsPhoenix()) {
            var withoutPhoenix = copy.withoutPhoenix();
            copy.sort(CardCollection::byValue);
            // either  pair pair
            var pair1 = new CardCollection(withoutPhoenix.subList(0, 2));
            var pair2 = new CardCollection(withoutPhoenix.subList(2, 4));
            var pair1value =pair1.isPair();
            var pair2value =pair2.isPair();
            if (pair1value != NONEXISTENT_COMBO_VALUE && pair2value != NONEXISTENT_COMBO_VALUE )
                return Double.max(pair1value,pair2value);// For simplicity the special case of the phoenix being the smaller is ignored..
            else {
                // or single +  triple
                var triple1 = new CardCollection(withoutPhoenix.subList(0, 3));
                var triple2 = new CardCollection(withoutPhoenix.subList(1, 4));
                return Double.max(triple1.isTriple() ,triple2.isTriple());
            }
        }

        copy.sort(CardCollection::byValue);
        var pair1value = new CardCollection(copy.subList(0, 2)).isPair();
        var pair2value = new CardCollection(copy.subList(3, 5)).isPair();
        var singleValue = new CardCollection(copy.subList(2,3)).isSingle();
        if(     pair1value != NONEXISTENT_COMBO_VALUE
                && pair2value != NONEXISTENT_COMBO_VALUE
                && ((pair1value.doubleValue() == singleValue) || pair2value.doubleValue() == singleValue))
            return singleValue;

        return NONEXISTENT_COMBO_VALUE;
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
    public List<CardCollection> getBeatingCombos(Combo toBeat){
        var superSet= superset();
        //var ofSameLength = superSet.stream().filter(); optimization
        return superSet.stream().filter(x->{
            var combo = x.getCombo();
            return combo.type == toBeat.type && combo.value > toBeat.value;
        }).collect(Collectors.toList());

    }

    public Double isStraight() {
        if (size() < 5)
            return NONEXISTENT_COMBO_VALUE;
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
                    return NONEXISTENT_COMBO_VALUE;
                }
            }
            value = card.getValue();
        }
        return Double.min(value+numSkips,14); //if additional phoenix it is always at the end (unless for the ace)
    }


    Double isConsequtivePair() {
        if (size() < 4 || size() % 2 == 1)
            return NONEXISTENT_COMBO_VALUE;
        if (containsNonComboCard())
            return NONEXISTENT_COMBO_VALUE;
        var hasPhoenix = this.containsPhoenix();
        var withoutPhoenix = this.withoutPhoenix();
        withoutPhoenix.sort(Card::compareTo);
        Double lastValue = null;
        for (int index = 1; index < withoutPhoenix.size(); index = index + 2) {
            var cards = new Card[]{withoutPhoenix.get(index-1), withoutPhoenix.get(index)};
            if (lastValue != null && cards[0].getValue() != lastValue + 1)
                return NONEXISTENT_COMBO_VALUE;
            lastValue = cards[0].getValue();

            var testPair = new CardCollection(Arrays.asList(cards));
            if (testPair.isPair() != NONEXISTENT_COMBO_VALUE) {
                continue;
            }
            if (!hasPhoenix) {
                return NONEXISTENT_COMBO_VALUE;
            } else {
                hasPhoenix = false;
                index -= 1; // test the other pair as well.
            }
        }
        if(hasPhoenix){
            var valueOfLastCard = withoutPhoenix.peek().orElseThrow().getValue();
            if(valueOfLastCard == lastValue+1)
                return valueOfLastCard;
            else
                return NONEXISTENT_COMBO_VALUE;
        }
        return lastValue;
    }


    Double isQuartetBomb() {
        if (this.size() != 4)
            return NONEXISTENT_COMBO_VALUE;
        if (this.containsPhoenix())
            return NONEXISTENT_COMBO_VALUE;
        return isQuadruple();
    }

    Double isStraightBomb() {
        return allEqualColor()? isStraight() : NONEXISTENT_COMBO_VALUE;
    }

    public Combo getCombo() {
        var tempValue = isQuartetBomb();
        if(tempValue != NONEXISTENT_COMBO_VALUE)
            return new Combo(ComboType.QuartetBomb,tempValue);

        tempValue = isSingle();
        if(tempValue != NONEXISTENT_COMBO_VALUE )
            return new Combo(ComboType.Single,tempValue);

        tempValue = isPair();
        if(tempValue != NONEXISTENT_COMBO_VALUE )
            return new Combo(ComboType.Pair,tempValue);

        tempValue = isTriple();
        if(tempValue!= NONEXISTENT_COMBO_VALUE )
            return new Combo(ComboType.Tripple,tempValue);

        tempValue = isFullHouse();
        if(tempValue!= NONEXISTENT_COMBO_VALUE )
            return new Combo(ComboType.FullHouse,tempValue);

        tempValue = isConsequtivePair();
        if(tempValue != NONEXISTENT_COMBO_VALUE){
            switch (size()){
                case 2:
                    return new Combo(ComboType.Consequtive2Pairs,tempValue);
                case 3:
                    return new Combo(ComboType.Consequtive3Pairs,tempValue);
                case 4:
                    return new Combo(ComboType.Consequtive4Pairs,tempValue);
                case 5:
                    return new Combo(ComboType.Consequtive5Pairs,tempValue);
                case 6:
                    return new Combo(ComboType.Consequtive6Pairs,tempValue);
                case 7:
                    return new Combo(ComboType.Consequtive7Pairs,tempValue);
                default:
                    throw new Error("longer combo than ever thought");

            }
        }
        tempValue = isStraightBomb();
        if(tempValue != NONEXISTENT_COMBO_VALUE){
            switch (size()){
                case 5:
                    return new Combo(ComboType.Straight5Bomb,tempValue);
                case 6:
                    return new Combo(ComboType.Straight6Bomb,tempValue);
                case 7:
                    return new Combo(ComboType.Straight7Bomb,tempValue);
                case 8:
                    return new Combo(ComboType.Straight8Bomb,tempValue);
                case 9:
                    return new Combo(ComboType.Straight9Bomb,tempValue);
                case 10:
                    return new Combo(ComboType.Straight10Bomb,tempValue);
                case 11:
                    return new Combo(ComboType.Straight11Bomb,tempValue);
                case 12:
                    return new Combo(ComboType.Straight12Bomb,tempValue);
                case 13:
                    return new Combo(ComboType.Straight13Bomb,tempValue);
                default:
                    throw new Error("straightbomb of unexpected size");
            }
        }
        tempValue = isStraight();
        if(tempValue != NONEXISTENT_COMBO_VALUE){
            switch (size()){
                case 5:
                    return new Combo(ComboType.Straight5,tempValue);
                case 6:
                    return new Combo(ComboType.Straight6,tempValue);
                case 7:
                    return new Combo(ComboType.Straight7,tempValue);
                case 8:
                    return new Combo(ComboType.Straight8,tempValue);
                case 9:
                    return new Combo(ComboType.Straight9,tempValue);
                case 10:
                    return new Combo(ComboType.Straight10,tempValue);
                case 11:
                    return new Combo(ComboType.Straight11,tempValue);
                case 12:
                    return new Combo(ComboType.Straight12,tempValue);
                case 13:
                    return new Combo(ComboType.Straight13,tempValue);
                case 14:
                    return new Combo(ComboType.Straight14,tempValue);
                default:
                    throw new Error("straight of unexpected size");
            }
        }


        return new Combo(ComboType.None,NONEXISTENT_COMBO_VALUE);
    }

    public int getAccountValue(){
        return this.stream().map(x->x.getAccountValue()).reduce(0,Integer::sum);
    }
}
