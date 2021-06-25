import cards.DeckFactory;
import cards.Dog;
import cards.Dragon;
import combos.CardCollection;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// A round is a complete playthough of a Deck
public class Round {
    public Round(Player [] players){
            deck = DeckFactory.createDeck();
            mPlayers = players;
            ongoing = true;
            playerFinishOrder = new LinkedList<Player>();
        }
    public TeamScores[] Play(){
        DistributeCards();
        var playrightHolder = Arrays.stream(mPlayers).filter(player -> player.IsStartingPlayer()).findFirst().get();
        while (ongoing){
            playrightHolder = PlayStich(playrightHolder);
        }
        return mPreviousScores;

    }


    private void FinishRound(){
        ongoing = false;
        return;
    };
    private void UpdatePlayerFinished(){
        Player [] team1 ={ mPlayers[0], mPlayers[2]};
        Player [] team2 ={ mPlayers[1], mPlayers[3]};

        var finishedPlayers = Arrays.stream(mPlayers).filter(player -> player.numberOfHandCards()== 0);
        finishedPlayers.forEach(player ->  {
            if( ! playerFinishOrder.contains(player)){
                playerFinishOrder.add(player);
            }
        });
        if (playerFinishOrder.containsAll(Arrays.asList(team1))) {
            FinishRound();
        }
        if (playerFinishOrder.containsAll(Arrays.asList(team2))) {
            FinishRound();
        }

    }



    private void DistributeCards(){
        for (int i=0;i<14;i++){
            for (var player: mPlayers) {
                var action = player.recieveCard(deck.pop().orElseThrow());
                allActions.add(Pair.with(player,action));
            }
        }
    }

    // returns winner of Stich
    private Player PlayStich(Player startingPlayer){
        Player lastPlayer = null;
        Player player;
        var currentPlayerIndex = Arrays.asList(mPlayers).indexOf(startingPlayer);
        var cardsOnTheTable = new CardCollection();
        var lastCombo = new CardCollection();
        do {
            player =  mPlayers[currentPlayerIndex];

            if(player.numberOfHandCards() != 0){
                var playedCards = player.play(lastCombo);
                System.out.print("player "+player+" plays"+ playedCards+"\n");
                //check validity
                if(playedCards.size() > 0){
                    lastCombo= playedCards;
                    cardsOnTheTable.addAll(playedCards);
                    lastPlayer = player;
                    UpdatePlayerFinished();
                }
                //dog can only be the starting card there are no points to win
                if(cardsOnTheTable.peek().isPresent() && cardsOnTheTable.peek().get().getClass() == Dog.class){
                    return mPlayers[currentPlayerIndex+2];
                }
            }
            currentPlayerIndex = (currentPlayerIndex+1)%4;
        }
        while (player != lastPlayer && ongoing);

        System.out.print("player "+player+" wins\n");
        if(cardsOnTheTable.peek().orElseThrow().getClass() == Dragon.class ){
            var opponents = new Player[]{mPlayers[(currentPlayerIndex+1)%4], mPlayers[(currentPlayerIndex+3)%4]};
            player.donateCardsToOponent(Arrays.stream(opponents).collect(Collectors.toList())).winCards(cardsOnTheTable);// todo correctly chose players
        }
        else{
            player.winCards(cardsOnTheTable);
        }
        return player;
    }

    private TeamScores [] mPreviousScores;
    private List<Player[]> teams;
    private List<Pair<Player,IAction>> allActions; //todo remodel evererything to actions, such that players can train..
    private Player [] mPlayers;
    private List<Player> playerFinishOrder;
    private boolean ongoing;
    private CardCollection deck;
}
