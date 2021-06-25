package cards;

public class Mahjong extends SpecialCard {
    private boolean wishSet;
    private Integer wish;

    Mahjong() {
        value = 1;
        accountValue = 0;
        wishSet = false;
    }

    public Integer getWish() {
        if (wishSet) {
            return wish;
        }
        return null;
    }

    public void setWish(Integer wish) {
        if (!wishSet) {
            this.wish = wish;
            this.wishSet = true;
        }
    }

    @Override
    public boolean isMahjong() {
        return true;
    }
}
