import cards.Card;
import combos.CardCollection;
import combos.ComboType;

import java.util.List;

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

    public CardCollection play(CardCollection lastPlayed){
        var lastCombo = lastPlayed.getComboType();
        if(lastCombo.comboType == ComboType.None){ //We are first to play! todo handle this
            var superset =handCards.superset();
            // here we can choose the mode ...
            lastCombo.comboType = ComboType.Single;

        }
        var possibleCombos =handCards.getBeatingCombos(lastCombo);
        var playCards = possibleCombos.stream().findFirst().orElse(new CardCollection());
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
