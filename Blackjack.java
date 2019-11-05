import java.util.Scanner;

/*
* Blackjack.java
*
* RULES OF BLACKJACK
* - start with $500
* - user is dealt two cards, both visible
* - dealer is dealt two cards, only one is visible
* - user can choose to hit or stand, must be at or under 21
* - card values
*   - ace is worth 1 or 11
*   - face cards (jack, queen, king) are all 10
* - dealer always stands on 17
* - splitting and doubling down are NOT included in this game
*
* Nguyen Do
*
* */

import java.util.ArrayList;

public class Blackjack {

    private Card[] cards; // card deck
    private int amount; // current amount
    private String divider = "============"; // divider string for readability

    public Blackjack() {
        cards = new Card[52];
        int index = 0;

        // initializes the cards in the deck
        for(int i = 1; i <= 13; i++) {
            for(int j = 1; j <= 4; j++) {
                switch(j) {
                    case 1:
                        cards[index] = new Card("clubs", i);
                        index++;
                        break;
                    case 2:
                        cards[index] = new Card("diamonds", i);
                        index++;
                        break;
                    case 3:
                        cards[index] = new Card("hearts", i);
                        index++;
                        break;
                    case 4:
                        cards[index] = new Card("spades", i);
                        index++;
                        break;
                    default:
                        break;
                }
            }
        }
        amount = 500;
    }

    // getAmount - gets the current amount
    public int getAmount() {
        return amount;
    }

    // playGame - runs game, quits if a negative price is entered

    public void playGame(int bet) {

        System.out.println("CURRENT BALANCE: " + amount);
        System.out.println("YOUR BET: " + bet);

        ArrayList<Card> userCards = new ArrayList<Card>();
        ArrayList<Card> dealerCards = new ArrayList<Card>();

        userCards.add(pickNewCard(userCards, dealerCards));
        userCards.add(pickNewCard(userCards, dealerCards));
        dealerCards.add(pickNewCard(userCards, dealerCards));

        ArrayList<Integer> sums = addedValues(userCards);
        ArrayList<Integer> sumsDealer = addedValues(dealerCards);

        printCards(dealerCards, sumsDealer, false);
        printCards(userCards, sums, true);

        boolean hasEnded = false;

        Scanner scan = new Scanner(System.in);

        while(!hasEnded) {
            if(sums.contains(21)) {
                amount += bet;
                System.out.println(win(bet));
                hasEnded = true;
            } else if(didLose(sums)) {
                amount -= bet;
                System.out.println(lost(bet));
                hasEnded = true;
            } else {
                System.out.println("Type 'hit' or 'stand': ");
                String choice = scan.nextLine();
                while(!choice.equals("hit") && !choice.equals("stand")) {
                    System.out.println("Please enter either 'hit' or 'stand': ");
                    choice = scan.nextLine();
                }

                if(choice.equals("hit")) {
                    userCards.add(pickNewCard(userCards, dealerCards));
                    sums = addedValues(userCards);
                    printCards(userCards, sums, true);
                } else if(choice.equals("stand")) {

                    while(lowest(sumsDealer) < 17) {
                        dealerCards.add(pickNewCard(userCards, dealerCards));
                        sumsDealer = addedValues(dealerCards);
                    }
                    printCards(dealerCards, sumsDealer, false);

                    int chosenUserValue = sums.get(0);
                    int chosenDealerValue = sumsDealer.get(0);

                    if(sums.size() > 1) {
                        for(int i = 0; i < sums.size(); i++) {
                            int possibleValue = highest(sums);
                            if(possibleValue <= 21 && possibleValue > chosenUserValue)
                                chosenUserValue = possibleValue;
                        }
                    }

                    if(sumsDealer.size() > 1) {
                        for(int i = 0; i < sumsDealer.size(); i++) {
                            int possibleValue = highest(sumsDealer);
                            if(possibleValue <= 21 && possibleValue > chosenDealerValue)
                                chosenDealerValue = possibleValue;
                        }
                    }

                    if(didLose(sumsDealer) || chosenUserValue > chosenDealerValue) {
                        amount += bet;
                        System.out.println(win(bet));
                        hasEnded = true;
                    } else if(chosenUserValue == chosenDealerValue) {
                        System.out.println("You and the dealer have tied. Your current balance stays at $" + amount);
                        hasEnded = true;
                    } else if(chosenUserValue < chosenDealerValue) {
                        amount -= bet;
                        System.out.println(lost(bet));
                        hasEnded = true;
                    }
                }
            }
        }

    }

    // printCards - displays the current cards of either the user or the dealer
    private void printCards(ArrayList<Card> userCards, ArrayList<Integer> sums, boolean user) {
        if(user)
            System.out.println("\nYOUR CARDS\n" + divider);
        else
            System.out.println("\nDEALER'S CARDS\n" + divider);
        for(int i = 0; i < userCards.size(); i++) {
            System.out.println(userCards.get(i));
        }

        System.out.println(divider);

        if(sums.size() == 1) {
            System.out.println("Value: " + sums.get(0));
        } else {
            System.out.print("Values: " + sums.get(0));
            for(int i = 1; i < sums.size(); i++) {
                System.out.print(", " + sums.get(i));
            }
            System.out.print("\n");
        }
        System.out.println(divider);
    }

    // pickCard - returns random card
    private Card pickNewCard(ArrayList<Card> currentUserCards, ArrayList<Card> currentDealerCards) {
        int randomInt = (int) (Math.random() * 52);

        while(currentUserCards.contains(cards[randomInt]) || currentDealerCards.contains(cards[randomInt])) {
            randomInt = (int) (Math.random() * 52);
        }

        return cards[randomInt];
    }

    // returns all the possible sums (usually only one sum, but when there is an ace there are multiple sums)
    private ArrayList<Integer> addedValues(ArrayList<Card> currentCards) {
        ArrayList<Integer> sums = new ArrayList<Integer>();
        sums.add(0);
        for(int i = 0; i < currentCards.size(); i++) {
            int size = sums.size();
            for(int j = 0; j < size; j++) {
                Card currentCard = currentCards.get(i);
                sums.set(j, sums.get(j) + currentCard.getValue());

                if (currentCard.isAce()) {
                    sums.add(sums.get(j) + 10);
                }
            }
        }
        return sums;
    }

    // didLose - checks if lowest value is above 21
    private boolean didLose(ArrayList<Integer> sums) {
        int lowest = lowest(sums);
        return (lowest > 21);
    }

    // lowest - returns lowest value of the sums
    private int lowest(ArrayList<Integer> sums) {
        int lowest = sums.get(0);
        for(int i = 0; i < sums.size(); i++) {
            if(sums.get(i) < lowest)
                lowest = sums.get(i);
        }
        return lowest;
    }

    // returns highest value of the sums
    private int highest(ArrayList<Integer> sums) {
        int highest = sums.get(0);
        for(int i = 0; i < sums.size(); i++) {
            if(sums.get(i) > highest)
                highest = sums.get(i);
        }
        return highest;
    }

    // win - winning message
    private String win(int bet) {
        return "Congrats! You have won $" + bet + ".\nYour new balance is $" + amount + ".\n";
    }

    // lost - losing message
    private String lost(int bet) {
        return "Sorry, you have lost $" + bet + ".\nYour new balance is $" + amount + ".\n";
    }

}
