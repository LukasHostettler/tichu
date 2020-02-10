package cards;

public class Mahjong extends SpecialCard {
    Mahjong(){
        value = 1;
        accountValue = 0;
        wishSet = false;
    }
    private boolean wishSet;
    private Integer wish;

    public void setWish(Integer wish){
        if(!wishSet){
            this.wish = wish;
            this.wishSet = true;
        }
    }
    public Integer getWish( ){
        if(wishSet) {
            return wish;
        }
        return null;
    }
}
