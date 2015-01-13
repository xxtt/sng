package com.sng;

import com.sng.game.cards.Card;
import com.sng.game.cards.hands.HandImpl;
import com.sng.screen.*;
import com.sng.screen.figures.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws IOException {
//      testImage();
        testStraight();

        GameService service = new GameService();
        List<Game> games = service.getAllGames();

        //       analyze(games);
//        parseAllHands(games);

    }

    private static void testStraight() {
        String[] cards = {"Ac", "Kc", "Kd", "Qc", "Qh", "Qd", "Qc"};
        HandImpl hand = new HandImpl();
        for (String card : cards) {
            hand.setCards(Card.parse(card));
        }
        for (String card : cards) System.out.print(" " + card);
        System.out.println("\n" + hand.getType());
        System.out.println(hand.getBestHand());
    }

    private static void parseAllHands(List<Game> games) throws IOException {
        int count = 0;
        for (Game game : games) {
            testHand(game);
            count++;
        }
        System.out.println(count);
    }

    private static void testHand(Game game) {
        List<Card> handCards = game.getPreFlop().getCards();
        ArrayList<Card> cards = new ArrayList<>();
        for (Street street : game.getStreetList()) {
            cards.addAll(street.getCards());
        }
        HandImpl hand = new HandImpl(handCards);
        if (cards.size() == 0) return;
        hand.setCards(cards);
        Collections.sort(cards);
        System.out.println(handCards + ", " + cards);
        System.out.println(hand.getType());
        System.out.println(hand.getBestHand());
        System.out.println(hand.PlayerCardsTakePart());
        System.out.println("------------------------------");
    }

    private static void analyze(List<Game> games) {
        int count = 0;
        for (Game game : games) {
            Street preFlop = game.getPreFlop();
            List<Card> cards = preFlop.getCards();
            int playerIndex = preFlop.getPlayerIndex(game.getPlayerName());
            Player player = preFlop.getPlayer(playerIndex);
            Move move = player.getMoves().getLast();
            State state = move.getState();

            // if (cards.get(1).getRank().toString().equals("A")) {
            if (cards.get(0).getRank().equals(cards.get(1).getRank())) {

                //if (state != State.FOLD
                //  && state != State.BIG_BLIND
                //   && state != State.CHECK
                //       && (move.getBet() > game.getBigBlind())

                count++;
                System.out.println("bb " + game.getBigBlind() +
                        " stack " + move.getStack() +
                        " " + cards +
                        " " + state +
                        " " + move.getBet() / Float.valueOf(game.getBigBlind()) +
                        " " + move.getBet());
            }
        }

        System.out.println("number: " + count);
    }

    static void testImage() {
        try {
            BufferedImage image = ImageIO.read(new File("images/test/bank.png"));

            List<Player> playerList = new ArrayList<>();

            PlayerState state = new PlayerState();
            PlayerButton button = new PlayerButton();
            PlayerStack stack = new PlayerStack();
            PlayerBet bet = new PlayerBet();
            TableCards cards = new TableCards();

            System.out.println("pot " + bet.getPot(image));
            System.out.println("bank " + bet.getBank(image));
            System.out.println("my cards " + cards.getHand(image));
            System.out.println("table cards " + cards.getFlopTurnRiver(image));
            System.out.println("---------------");


            for (int i = 0; i < 9; i++) {
                System.out.println("player" + i);
                Player player = new Player(i, "", 0);
                if (i != 0) System.out.println("state " + state.getState(image, i));
                System.out.println("button " + button.checkButton(image, i));
                if (i != 0) System.out.println("stack " + stack.getStack(image, i));
                System.out.println("bet " + bet.getPlayerBet(image, i));
                System.out.println("---------------");
                playerList.add(player);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
