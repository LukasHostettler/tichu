import cards.Card;
import combos.CardCollection;

import java.util.Arrays;
import java.util.List;

public class Player {
    private CardCollection handCards;
    private CardCollection gainedCards;
    public Player(){

    }
    public void recieveCard(Card c){ // tichu announcement

    }

    public void winCards(CardCollection cards){
        gainedCards.addAll(cards);
    }

    public CardCollection play(CardCollection lastCombo){
        return handCards;
    }

    public Player donateCardsToOponent(List<Player> oponents){
        // dumb strategy
        return oponents.stream().findFirst().orElseThrow();
    }
    public int getAccountValue(){
        return gainedCards.getAccountValue();
    }

}
