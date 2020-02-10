package cards;

public class Card implements Comparable<Card> {
    @Override
    public int compareTo(Card o) {

        if(this.value != o.getValue())
            return (int) (this.getValue()-o.getValue());
        if(this.color != o.getColor())
            return this.color.ordinal()-o.getColor().ordinal();
        return 0;
    }

    public enum  Color { NONE, RED, GREEN, BLACK, BLUE }

    double value;
    Color color;
    int accountValue = 0;

    public double getValue() {
        return value;
    }

    public Color getColor(){
        return color;
    }

    public int getAccountValue(){
        return accountValue;
    }

    public boolean isPhoenix(){
        return false;
    }
    public boolean isSpecialCard(){
        return false;
    }
    @Override
    public String toString() {
        return "Card{" +
                "value=" + value +
                ", color=" + color +
                ", accountValue=" + accountValue +
                '}';
    }
}
