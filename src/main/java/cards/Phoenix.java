package cards;

public class Phoenix extends SpecialCard {
    Phoenix(){
        accountValue = -25;
        color = Color.NONE;

    }
    public void setValue(double value){
        value = value;
    }

    @Override
    public boolean isPhoenix() {
        return true;
    }
}
