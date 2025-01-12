import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private String[]suits = new String[]{"Hearts", "Diamonds", "Clubs", "Spades"};

    public Deck(){
        reset();
        shuffler();
    }
    public void reset(){
        this.deck.clear();
        //Reseting and adding new cards
        for(String suit : suits) {
            for (int i = 2; i < 15; i++) {
                deck.add(new Card(i, suit));
            }
        }
    }
    public void shuffler(){
        ArrayList<Card> tempDeck = new ArrayList<Card>();
        while(this.deck.size() > 0 ){
            int randIndex = ((int)(Math.random() * 100))% this.deck.size();
            Card card = this.deck.remove(randIndex);
            tempDeck.add(card);
        }
        this.deck = tempDeck;
    }

    public Card dealCard(){
        return this.deck.remove(this.deck.size()-1);
    }

    public int getRemainingCardCount() {
        return this.deck.size();
    }
    public static void main(String[] args){
        Deck sd = new Deck();
        System.out.println(sd.deck.toString());
        System.out.println(sd.deck.size());
        System.out.println(sd.dealCard());
        System.out.println(sd.dealCard());
        System.out.println(sd.dealCard());
        System.out.println(sd.dealCard());
        System.out.println(sd.getRemainingCardCount());
    }
}
