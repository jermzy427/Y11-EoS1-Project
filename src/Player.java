import java.util.Arrays;

public class Player {
    private String name;
    private int balance;
    private Card[] holecards = new Card[2];
    private boolean isInGame;

    public Player(String name, int balance, Card[] holecards) {
        this.name = name;
        this.balance = balance;
        this.holecards = holecards;
        this.isInGame = true;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }

    public void addToBalance(int additionAmount) {
        this.balance += additionAmount;
    }

    public void reduceFromBalance(int reduceAmount) {
        this.balance -= reduceAmount;
    }

    public Card[] getHoleCards() {
        return this.holecards;
    }

    public void setHoleCards(Card[] holeCards) {
        this.holecards = holeCards;
    }

    public boolean getIsInGame() {
        return this.isInGame;
    }

    public void setIsInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public String toString() {
        return ("Player: " + this.name + " has a balance of " + this.balance + ". \n Hole cards: " + Arrays.toString(this.holecards)  +". \nIn the game: " + this.isInGame);

    }

    public static void main(String args[]){
        Card holeCards[] = {new Card(1,"hearts"), new Card(2,"hearts")};
        Player p1 = new Player("Jeremy", 10000,holeCards );
        System.out.println(p1.getName());
        System.out.println(p1.getBalance());
        p1.addToBalance(100);
        System.out.println(p1.getBalance());
        p1.reduceFromBalance(100);
        System.out.println(Arrays.toString(p1.getHoleCards()));
        System.out.println(p1.toString());
        Card[] holeCards2 = {new Card(9, "clubs"), new Card(13, "spades")};
        p1.setHoleCards(holeCards2);
        System.out.println(Arrays.toString(p1.getHoleCards()));


    }

}
