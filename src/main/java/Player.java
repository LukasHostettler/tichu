import cards.Card;
import combos.CardCollection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player {
    private CardCollection handCards;
    private CardCollection gainedCards;
    public Player(){
        handCards = new CardCollection();
        gainedCards = new CardCollection();
    }
    public void recieveCard(Card c){ // tichu announcement
        handCards.add(c);
    }
    public int numberOfHandCards(){
        return handCards.size();
    }
    public void winCards(CardCollection cards){
        gainedCards.addAll(cards);
    }

    public CardCollection play(CardCollection lastCombo){
        if(lastCombo.isPair()){

        }
        var playCards = lastCombo;
        handCards.removeAll(playCards);
        return playCards;

    }

    public Player donateCardsToOponent(List<Player> oponents){
        // dumb strategy
        return oponents.stream().findFirst().orElseThrow();
    }
    public int getAccountValue(){
        return gainedCards.getAccountValue();
    }

}
