import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class PokerGame {
    private ArrayList<Player> playerList;
    private Deck gameDeck;
    private final Card[] communityCards;
    private int winningPot;
    private static Scanner scan = new Scanner(System.in);
    private int[] playersTotalBets;
    private boolean keepPlaying;


    public static void main(String[] args) {
        new PokerGame();
        System.out.println("Would you like to play again?");
        String playAgain = scan.next();
        if(playAgain.toLowerCase().contains("yes")){
            new PokerGame();
        }
    }
    public PokerGame(){
        displayIntroduction();
        this.playerList = new ArrayList<Player>();
        this.gameDeck = new Deck();
        this.communityCards = new Card[5];;
        this.winningPot = 0;
        playerSetup();
        bettingRound();
        dealCommunityCards();
        bettingRound();
        dealCommunityCards();
        bettingRound();
        dealCommunityCards();
        bettingRound();
        findWinner();

    }
    public void displayIntroduction(){
        System.out.println("Welcome to Texas Hold'em Poker Application");
        System.out.println("Would you like to see the rules(Y/N)");
        if(scan.next().toLowerCase().contains("y")){
            displayRules();
        }

    }
    public static void displayRules(){
        System.out.println("This poker game is modeled off of Texas Hold'em Poker. and is a simplified version of the game involving only two players." +
                "\nAt the start of the game both players are dealt two hole cards and five community cards are laid down on the table." +
                "\nThere are 4 betting rounds, the pre-flop, flop, turn, and river" +
                "\nUsing your cards you need to predict and calculate your chances of winning and bet accordingly, by either" +
                "\n-Checking: A passive action that maintains the board state allowing you to check what the next card is" +
                "\n-Betting/Raising: A active action that gambles money" +
                "\n-Calling: Matching someone else's bet" +
                "\n-Folding: Giving up and losing the match" +
                "\nAs the betting rounds go on, community cards get revealed, and every round there is a betting round." +
                "\nThe betting rounds are as follows: " +
                "\n-Pre-Flop: No community cards are revealed" +
                "\n-Flop: First 3 community cards" +
                "\n-Turn: 4th community card" +
                "\n-River: 5th and final community card" +
                "\nAfter the final betting round, both players hole cards will be revealed and make the best 5 card hand using the community cards, the person with the greater hand strength wins." +
                "\nThe different hand strengths from best to worst are as follows: \n1.Royal Flush: 10JQKA all of the same suit." +
                "\n2.Straight Flush: 5 cards of the same suit and in numerical order. \n3.Four of a Kind: 4 cards of the same number with one extra" +
                "\n4.Full House: Three cards of the same number and a pair of cards of the same number. \n5.Flush: 5 cards of the same suit" +
                "\n6.Straight: 5 card of ascending order.\n7.Three of a Kind: 3 cards of the same rank \n8.Two Pair: Two pairs of two differents cards of the same rank" +
                "\n9.Pair: Two cards of the same number.\n10.High Card: the highest card numerically.");
    }
    public void playerSetup() {
        boolean addAnotherPlayer = true;
        while (addAnotherPlayer) {
            addAnotherPlayer = false;
            if(playerList.size() < 2){
                addPlayer();
                if (playerList.size() < 2){
                    System.out.println("Do you want to add another player? (Y/N)");
                    if (scan.next().toLowerCase().contains("y")) {
                        addAnotherPlayer = true;
                    }
                }
            }
        }

        this.playersTotalBets = new int[playerList.size()];
        //payOutBlinds();
    }
    public void addPlayer() {
        System.out.println("Enter player name:");
        String tempName = scan.next();
        System.out.println("Enter " + tempName + "'s starting balance:");
        int tempBalance = scan.nextInt();
        Card[] tempHoleCards = {gameDeck.dealCard(), gameDeck.dealCard()};
        playerList.add(new Player(tempName, tempBalance, tempHoleCards));
        System.out.println(playerList.get(playerList.size()-1).toString());}

    public void dealCommunityCards(){
        if(communityCards[0] == null){
            communityCards[0] = gameDeck.dealCard();
            communityCards[1] = gameDeck.dealCard();
            communityCards[2] = gameDeck.dealCard();
            System.out.println("The flop contains " + Arrays.toString(communityCards));
        }
        else if(communityCards[3] == null) {
            communityCards[3] = gameDeck.dealCard();
            System.out.println("The turn contains " + Arrays.toString(communityCards));
        }
        else if(communityCards[4] == null){
            communityCards[4] = gameDeck.dealCard();
            System.out.println("The river contains " + Arrays.toString(communityCards));
        }
    }
    public void bettingRound() {
        int currentPlayerIndex = 0;
        for (int i = 0; i < this.playerList.size(); i++) {
            if (this.playerList.get(currentPlayerIndex).getIsInGame()) {
                currentPlayerIndex = individualBet(currentPlayerIndex);
            }
        }
        while (!areAllBetsEqual()) {
            currentPlayerIndex = individualBet(currentPlayerIndex);
        }
    }
    public int individualBet(int currentPlayerIndex){
        String checkOrCall = areAllBetsEqual() ? "check" : "call";
        System.out.println("The pot is: " + this.winningPot);
        System.out.println(this.playerList.get(currentPlayerIndex).toString() +
                "\nDo you want to fold, " + checkOrCall + " or raise?");
        String answer = scan.next();
        if (answer.toLowerCase().contains("fold")) {
            fold(this.playerList.get(currentPlayerIndex));
        }
        else if (answer.toLowerCase().contains("call")) {
            call(this.playerList.get(currentPlayerIndex));
        }
        else if (answer.toLowerCase().contains("raise")) {
            raise(this.playerList.get(currentPlayerIndex));
        }
        currentPlayerIndex++;
        if (currentPlayerIndex == this.playerList.size()) {
            currentPlayerIndex = 0;
        }
        return currentPlayerIndex;
    }
    public boolean areAllBetsEqual() {
        int highestBet = getHighestBet();
        for (int i = 0; i < this.playersTotalBets.length; i++) {
            if (this.playersTotalBets[i] < highestBet && this.playerList.get(i).getIsInGame()) {
                return false;
            }
        }
        return true;
    }


    public int getHighestBet() {
        int highestBet = 0;
        for (int i = 0; i < this.playersTotalBets.length; i++) {
            if (this.playersTotalBets[i] > highestBet && this.playerList.get(i).getIsInGame()) {
                highestBet = this.playersTotalBets[i];
            }
        }
        return highestBet;
    }
    public void updateWinningPot() {
        this.winningPot = 0;
        for (int i = 0; i < this.playersTotalBets.length; i++) {
            this.winningPot += this.playersTotalBets[i];
        }
    }
    public void fold(Player player) {
        player.setIsInGame(false);
    }
    public void call(Player player) {
        int highestBet = getHighestBet();
        int playersBetDifference = highestBet - (this.playersTotalBets[this.playerList.indexOf(player)]);
        player.reduceFromBalance(playersBetDifference);
        this.playersTotalBets[this.playerList.indexOf(player)] += playersBetDifference;
        updateWinningPot();
    }
    public void raise(Player player) {
        System.out.println(player.getName() + ": How much do you want to raise?");
        int raiseAmount = scan.nextInt();
        call(player);
        player.reduceFromBalance(raiseAmount);
        this.playersTotalBets[this.playerList.indexOf(player)] += raiseAmount;
        updateWinningPot();
    }
    public void findWinner() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        //royalFlush
        winningPlayers = royalFlush();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //straightFlush
        winningPlayers = straightFlush();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //fourOfAKind
        winningPlayers = fourOfAKind();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //fullHouse
        winningPlayers = fullHouse();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //flush
        winningPlayers = flush();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //straight
        winningPlayers = straight();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //threeOfAKind
        winningPlayers = threeOfAKind();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //twoPair
        winningPlayers = twoPair();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //onePair
        winningPlayers = onePair();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            System.out.println("Winner(s) are: " + winningPlayers);
            return;
        }
        //highCard
        winningPlayers = highCard();
        if (!winningPlayers.isEmpty()) {
            handleWinners(winningPlayers);
            if(winningPlayers.size()>1){
                System.out.println("Both players win");

            }
            System.out.println("Winner(s) are: " + winningPlayers);
        }

    }

    public void handleWinners(ArrayList<Player> winningPlayers) {
        int tempWinnerAward = this.winningPot / winningPlayers.size();
        for (int i = 0; i < winningPlayers.size(); i++) {
            winningPlayers.get(i).addToBalance(tempWinnerAward);
        }
        this.winningPot = 0;
    }

    public ArrayList<Player> royalFlush() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            int hearts = 0;
            int diamonds = 0;
            int spades = 0;
            int clubs = 0;
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                ArrayList<Integer> tempPlayerCards = new ArrayList<Integer>();
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int l = 0; l < tempPlayersCardsAll.size(); l++) {
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Hearts")) {
                        hearts++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Diamonds")) {
                        diamonds++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Spades")) {
                        spades++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Clubs")) {
                        clubs++;
                    }
                }
                if (hearts >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Hearts");
                }
                else if (diamonds >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Diamonds");
                }
                else if (spades >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Spades");
                }
                else if (clubs >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Clubs");
                }

                if (tempPlayerCards.contains(10) &&
                        tempPlayerCards.contains(11) &&
                        tempPlayerCards.contains(12) &&
                        tempPlayerCards.contains(13) &&
                        tempPlayerCards.contains(14)) {
                    winningPlayers.add(tempPlayer);
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> straightFlush() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            int hearts = 0;
            int diamonds = 0;
            int spades = 0;
            int clubs = 0;
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                ArrayList<Integer> tempPlayerCards = new ArrayList<Integer>();
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int l = 0; l < tempPlayersCardsAll.size(); l++) {
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Hearts")) {
                        hearts++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Diamonds")) {
                        diamonds++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Spades")) {
                        spades++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Clubs")) {
                        clubs++;
                    }
                }
                if (hearts >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Hearts");
                }
                else if (diamonds >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Diamonds");
                }
                else if (spades >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Spades");
                }
                else if (clubs >= 5) {
                    tempPlayerCards = getValuesOfSuit(tempPlayersCardsAll, "Clubs");
                }
                Collections.sort(tempPlayerCards);
                int lowestValue = 15;
                int secondHighestValue = 15;
                int thirdHighestValue = 15;
                if (tempPlayerCards.size() >= 5) {
                    lowestValue = tempPlayerCards.get(0);
                    secondHighestValue = tempPlayerCards.get(1);
                    thirdHighestValue = tempPlayerCards.get(2);
                    if(tempPlayerCards.contains(lowestValue + 1) &&
                            tempPlayerCards.contains(lowestValue + 2) &&
                            tempPlayerCards.contains(lowestValue + 3) &&
                            tempPlayerCards.contains(lowestValue + 4)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
                if (tempPlayerCards.size() >= 6) {
                    if(tempPlayerCards.contains(secondHighestValue + 1) &&
                            tempPlayerCards.contains(secondHighestValue + 2) &&
                            tempPlayerCards.contains(secondHighestValue + 3) &&
                            tempPlayerCards.contains(secondHighestValue + 4)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
                if (tempPlayerCards.size() >= 7) {
                    if(tempPlayerCards.contains(thirdHighestValue + 1) &&
                            tempPlayerCards.contains(thirdHighestValue + 2) &&
                            tempPlayerCards.contains(thirdHighestValue + 3) &&
                            tempPlayerCards.contains(thirdHighestValue + 4)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> fourOfAKind() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                int [] allValues = new int[13];
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int j = 0; j < tempPlayersCardsAll.size(); j++) {
                    allValues[tempPlayersCardsAll.get(j).getValue()-2]++;
                }
                for (int k = 0; k < allValues.length; k++) {
                    if (allValues[k] >= 4) {
                        winningPlayers.add(tempPlayer);
                    }
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> fullHouse() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                int [] allValues = new int[13];
                boolean threeCard = false;
                boolean twoCard = false;

                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int j = 0; j < tempPlayersCardsAll.size(); j++) {
                    allValues[tempPlayersCardsAll.get(j).getValue()-2]++;
                }
                for (int k = 0; k < allValues.length; k++) {
                    if (allValues[k] >= 3) {
                        threeCard = true;
                    }
                    else if (allValues[k] >= 2) {
                        twoCard = true;
                    }
                }
                if (threeCard && twoCard) {
                    winningPlayers.add(tempPlayer);
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> flush() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            int hearts = 0;
            int diamonds = 0;
            int spades = 0;
            int clubs = 0;
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int l = 0; l < tempPlayersCardsAll.size(); l++) {
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Hearts")) {
                        hearts++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Diamonds")) {
                        diamonds++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Spades")) {
                        spades++;
                    }
                    if (tempPlayersCardsAll.get(l).getSuit().contains("Clubs")) {
                        clubs++;
                    }
                }
                if (hearts >= 5 || diamonds >= 5 || spades >= 5 || clubs >= 5) {
                    winningPlayers.add(tempPlayer);
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> straight() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                ArrayList<Integer> tempPlayerCards = getValuesOfAllCards(tempPlayersCardsAll);

                Collections.sort(tempPlayerCards);
                int lowestValue = 15;
                int secondHighestValue = 15;
                int thirdHighestValue = 15;
                if (tempPlayerCards.size() >= 5) {
                    lowestValue = tempPlayerCards.get(0);
                    secondHighestValue = tempPlayerCards.get(1);
                    thirdHighestValue = tempPlayerCards.get(2);
                    if(tempPlayerCards.contains(lowestValue + 1) &&
                            tempPlayerCards.contains(lowestValue + 2) &&
                            tempPlayerCards.contains(lowestValue + 3) &&
                            tempPlayerCards.contains(lowestValue + 4)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
                if (tempPlayerCards.size() >= 6) {
                    if(tempPlayerCards.contains(secondHighestValue + 1) &&
                            tempPlayerCards.contains(secondHighestValue + 2) &&
                            tempPlayerCards.contains(secondHighestValue + 3) &&
                            tempPlayerCards.contains(secondHighestValue + 4)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
                if (tempPlayerCards.size() >= 7) {
                    if(tempPlayerCards.contains(thirdHighestValue + 1) &&
                            tempPlayerCards.contains(thirdHighestValue + 2) &&
                            tempPlayerCards.contains(thirdHighestValue + 3) &&
                            tempPlayerCards.contains(thirdHighestValue + 4)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> threeOfAKind() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                int [] allValues = new int[13];
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int j = 0; j < tempPlayersCardsAll.size(); j++) {
                    allValues[tempPlayersCardsAll.get(j).getValue()-2]++;
                }
                for (int k = 0; k < allValues.length; k++) {
                    if (allValues[k] >= 3) {
                        winningPlayers.add(tempPlayer);
                    }
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> twoPair() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                int [] allValues = new int[13];
                int pairCount = 0;
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int j = 0; j < tempPlayersCardsAll.size(); j++) {
                    allValues[tempPlayersCardsAll.get(j).getValue()-2]++;
                }
                for (int k = 0; k < allValues.length; k++) {
                    if (allValues[k] >= 2) {
                        pairCount++;
                        if (pairCount == 2) {
                            winningPlayers.add(tempPlayer);
                        }
                    }
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> onePair() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                int [] allValues = new int[13];
                ArrayList<Card> tempPlayersCardsAll = tempPlayersCardsAll(tempPlayer);
                for (int j = 0; j < tempPlayersCardsAll.size(); j++) {
                    allValues[tempPlayersCardsAll.get(j).getValue()-2]++;
                }
                for (int k = 0; k < allValues.length; k++) {
                    if (allValues[k] >= 2 && !winningPlayers.contains(tempPlayer)) {
                        winningPlayers.add(tempPlayer);
                    }
                }
            }
        }
        return winningPlayers;
    }

    public ArrayList<Player> highCard() {
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        int[][] sortedPlayersHoleCards = new int[this.playerList.size()][2];
        // The first highest card
        int highestCardColTwo = 0;
        // The second highest card
        int highestCardColOne = 0;
        boolean tie = false;

        for (int i = 0; i < this.playerList.size(); i++) {
            Player tempPlayer = this.playerList.get(i);
            if (tempPlayer.getIsInGame()) {
                ArrayList<Integer> tempHoleCards = new ArrayList<Integer>();
                tempHoleCards.add(tempPlayer.getHoleCards()[0].getValue());
                tempHoleCards.add(tempPlayer.getHoleCards()[1].getValue());
                Collections.sort(tempHoleCards);
                if (tempHoleCards.get(1) > highestCardColTwo) {
                    highestCardColTwo = tempHoleCards.get(1);
                    winningPlayers.clear();
                    winningPlayers.add(tempPlayer);
                    sortedPlayersHoleCards = new int[this.playerList.size()][2];
                    sortedPlayersHoleCards[i][0] = i;
                    sortedPlayersHoleCards[i][1] = tempHoleCards.get(0);
                }
                else if (tempHoleCards.get(1) == highestCardColTwo) {
                    winningPlayers.add(tempPlayer);
                    tie = true;
                    // Saves the index of the player and the value of the players second highest card
                    sortedPlayersHoleCards[i][0] = i;
                    sortedPlayersHoleCards[i][1] = tempHoleCards.get(0);
                }
            }
        }
        if (tie) {
            winningPlayers.clear();
            for (int k = 0; k < sortedPlayersHoleCards.length; k++) {
                if (sortedPlayersHoleCards[k][1] > highestCardColOne) {
                    highestCardColOne = sortedPlayersHoleCards[k][1];
                }
            }
            for (int l = 0; l < sortedPlayersHoleCards.length; l++) {
                if (sortedPlayersHoleCards[l][1] == highestCardColOne) {
                    winningPlayers.add(this.playerList.get(l));
                }
            }
        }
        return winningPlayers;
    }
    public ArrayList<Integer> getValuesOfSuit(ArrayList<Card> playersCardsAll, String suit) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < playersCardsAll.size(); i++) {
            if (playersCardsAll.get(i).getSuit().contains(suit)) {
                result.add(playersCardsAll.get(i).getValue());
            }
        }
        return result;
    }

    public ArrayList<Integer> getValuesOfAllCards(ArrayList<Card> playersCardsAll) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < playersCardsAll.size(); i++) {
            result.add(playersCardsAll.get(i).getValue());
        }
        return result;
    }
    public ArrayList<Card> tempPlayersCardsAll(Player player) {
        ArrayList<Card> result = new ArrayList<Card>();
        for (int i = 0; i < this.communityCards.length; i++) {
            result.add(this.communityCards[i]);
        }
        result.add(player.getHoleCards()[0]);
        result.add(player.getHoleCards()[1]);
        return result;

    }
}
