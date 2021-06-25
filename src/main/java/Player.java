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
    public TichuAnnouncement recieveCard(Card c){ // tichu announcement
        handCards.add(c);
        return TichuAnnouncement.None;
    }


    public int numberOfHandCards(){
        return handCards.size();
    }
    public void winCards(CardCollection cards){
        gainedCards.addAll(cards);
    }

    public CardCollection play(CardCollection lastPlayed){
        var lastCombo = lastPlayed.getCombo();
        if(lastCombo.type == ComboType.None){ //We are first to play! todo handle this
            var superset =handCards.superset();
            // here we can choose the mode ...
            lastCombo.type = ComboType.Single;

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

    public boolean IsStartingPlayer(){
        return handCards.stream().anyMatch(Card::isMahjong);
    }
    public int getAccountValue(){
        return gainedCards.getAccountValue();
    }

}
