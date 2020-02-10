import cards.DeckFactory;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        var cards = DeckFactory.createDeck();
        System.out.println(cards);
    }
}
