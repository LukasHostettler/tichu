package cards;

public class SpecialCard extends Card {
    @Override
    public boolean isSpecialCard() {
        return true;
    }

    @Override
    public String toString() {
        String s = this.getClass().toString() + ":" + super.toString();
        return s;
    }
}
