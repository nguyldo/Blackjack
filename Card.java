/*
 * Card.java
 *
 * Card object, containing suits and number values
 *
 * Nguyen Do
 *
 * */

public class Card {

    private String suit;
    private int number;
    private int value;

    public Card(String suit, int number) {
        this.suit = suit;
        this.number = number;
        value = number;
        if(number >= 10)
            value = 10;
    }

    public String getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }

    public int getValue() {
        return value;
    }

    public boolean isAce() {
        if(number == 1)
            return true;
        return false;
    }

    public String toString() {
        String printedNumber = "" + number;
        if(number == 1)
            printedNumber = "ace";
        if(number == 11)
            printedNumber = "jack";
        if(number == 12)
            printedNumber = "queen";
        if(number == 13)
            printedNumber = "king";
        return printedNumber + " of " + suit;
    }

}
