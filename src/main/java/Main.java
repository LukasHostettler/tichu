import cards.DeckFactory;
import cards.Dog;
import cards.Dragon;
import combos.CardCollection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        TeamScores[] scores;
        var players = new Player[4];

        for(int i=0;i<4;i++){
            players[i]=new Player();
        }
        var round = new Round(players);
        scores  = round.Play();
    }
}
