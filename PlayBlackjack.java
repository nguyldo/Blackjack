import java.util.Scanner;

/*
* PlayBlackjack.java
*
* Run this class to play blackjack (accesses methods from Blackjack.java)
*
* Nguyen Do
*
* */

public class PlayBlackjack {

    public static void main (String[] args) {

        Blackjack game = new Blackjack();
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to Blackjack! You are starting with $500.");

        String input = "";
        while(input != "STOP") {
            System.out.println("Please enter an amount to bet (or type 'STOP' to end the game):");
            input = scan.nextLine();
            if(input.equals("STOP"))
                break;
            try {
                int bet = Integer.parseInt(input);
                if(bet <= game.getAmount()) {
                    game.playGame(bet);
                    if(game.getAmount() <= 0)
                        input = "STOP";
                } else {
                    System.out.println("You cannot bet more than your current balance.");
                }
            } catch (Exception e) {
                System.out.println("The input was invalid.");
            }
        }

        System.out.println("Thanks for playing! You ended the game with $" + game.getAmount() + ".");

    }

}
