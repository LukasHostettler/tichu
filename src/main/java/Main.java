import cards.Card;
import cards.DeckFactory;
import cards.Dog;
import cards.Dragon;
import combos.CardCollection;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        var cards = DeckFactory.createShuffledDeck();
        var players = new Player[4];
        for(int i=0;i<4;i++){
            players[i]=new Player();
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
        var currentPlayerIndex = 0;//Arrays.stream(players).mapToInt();

        while(Arrays.stream(players).filter(x->x.numberOfHandCards()>0).toArray().length>2){
            var cardsOnTheTable = new CardCollection();
            var lastCombo = new CardCollection();
            while(true){
                var player =  players[currentPlayerIndex];

                if (player == lastPlayer){
                    if(cardsOnTheTable.peek().orElseThrow().getClass() == Dragon.class ){
                        player.donateCardsToOponent(new ArrayList<>(){}).winCards(cardsOnTheTable);// todo correctly chose players
                        lastPlayer = null;
                        break;
                    }
                    else{
                        player.winCards(cardsOnTheTable);
                        lastPlayer = null;
                        break;
                    }
                }
                var playedCards = player.play(lastCombo);
                //check validity
                if(playedCards.size() > 0){
                    lastCombo= playedCards;
                    cardsOnTheTable.addAll(playedCards);
                    lastPlayer = player;
                }
                if(cardsOnTheTable.peek().isPresent() && cardsOnTheTable.peek().get().getClass() == Dog.class){
                    lastPlayer = null;
                    currentPlayerIndex = (currentPlayerIndex+1)%4;
                }
                currentPlayerIndex = (currentPlayerIndex+1)%4;
            }
        }

        System.out.println(cards);
    }
}
