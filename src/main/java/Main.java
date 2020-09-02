import cards.DeckFactory;
import cards.Dog;
import cards.Dragon;
import combos.CardCollection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

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
        var currentPlayerIndex = 0;
        var finishedPlayers = new LinkedList<Player>();
        while(finishedPlayers.size()<3){
            var cardsOnTheTable = new CardCollection();
            var lastCombo = new CardCollection();
            while(true){
                var player =  players[currentPlayerIndex];
                if (player == lastPlayer){
                    System.out.print("player "+player+" wins\n");
                    if(cardsOnTheTable.peek().orElseThrow().getClass() == Dragon.class ){
                        var opponents = new Player[]{players[(currentPlayerIndex+1)%4], players[(currentPlayerIndex+3)%4]};
                        player.donateCardsToOponent(Arrays.stream(opponents).collect(Collectors.toList())).winCards(cardsOnTheTable);// todo correctly chose players
                    }
                    else{
                        player.winCards(cardsOnTheTable);
                    }
                    lastPlayer = null;
                    break;
                }
                if(player.numberOfHandCards() != 0){
                    var playedCards = player.play(lastCombo);
                    System.out.print("player "+player+" plays"+ playedCards+"\n");
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
                }
                else {
                    if(!finishedPlayers.contains(player)){
                        finishedPlayers.add(player);
                    }
                }
                currentPlayerIndex = (currentPlayerIndex+1)%4;
            }
            var pointsOfLastPlayer = Arrays.stream(players).filter(player -> player.numberOfHandCards()>0).findFirst().orElseThrow().getAccountValue();

        }

        System.out.println(cards);
    }
}
