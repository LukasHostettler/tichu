package combos;

public interface Combo extends Comparable<Combo> {
    boolean isValid(CardCollection c);

}
/*
public class Pair extends CardCollection {
    public Pair()
        {
            //...
        }

    public Pair(CardCollection p)
        {
            // copy stuff to 'this'
        }

        public static implicit operator Pair(CardCollection p)
        {
            Question q = new Question(p);
            return q;
        }
    }public Pair asPair(CardCollection c);
}
*/