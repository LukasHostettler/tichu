import cards.Card;
import cards.DeckFactory;
import cards.Dragon;
import combos.CardCollection;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        var cards = DeckFactory.createShuffledDeck();
        var players = new ArrayList<Player>();
        for(int i=0;i<4;i++){
            players.add(new Player());
        }
        for (int i=0;i<8;i++){
            for (var player:players) {
                player.recieveCard(cards.pop().orElseThrow());
            }
        }
        for (int i=0;i<6;i++){
            for (var player:players) {
                player.recieveCard(cards.pop().orElseThrow());
            }
        }
        Player lastPlayer = null;
        while(true){
            var cardsOnTheTable = new CardCollection();
            var lastCombo = new CardCollection();
            for (var player: players){
                if (player == lastPlayer){
                    if(cardsOnTheTable.peek().orElseThrow().getClass() == Dragon.class ){
                        player.donateCardsToOponent(players);// todo correctly chose players
                    }
                    else{
                        player.winCards(cardsOnTheTable);
                    }
                    cardsOnTheTable = new CardCollection();
                }
                var playedCards = player.play(lastCombo);
                //check validity
                if(lastCombo.size() > 0){
                    lastCombo.addAll(playedCards);
                    cardsOnTheTable.addAll(playedCards);
                    lastPlayer = player;
                }

            }
        }

        System.out.println(cards);
    }
}
