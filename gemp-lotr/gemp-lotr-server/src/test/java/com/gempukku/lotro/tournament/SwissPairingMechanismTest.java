package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.BestOfOneStandingsProducer;
import com.gempukku.lotro.competitive.PlayerStanding;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class SwissPairingMechanismTest {
//    @Test
//    public void testPairingLargeTournament() {
//        int repeatCount = 1;
//        int playerCount = 4096*2;
//
//        for (int repeat = 0; repeat < repeatCount; repeat++) {
//            testSwissPairingForPlayerCount(playerCount);
//        }
//    }

    public void calculateSallyJackChances() {
        // 10 Sallys and 250 Jacks play in a tournament with 9 rounds of Swiss and Top 8.
        // Sally has 66% chance of winning a game against Jack and 50% against anotherSally, Jack has 50% of winning
        // a game against another Jack

        int repeatCount = 100000;
        int roundCount = 9;

        int betterPlayerCount = 10;
        int worsePlayerCount = 250;
        float chanceToWin = 0.66666f;

        int playerCount = betterPlayerCount+worsePlayerCount;

        int betterPlayerWins =0;

        Set<String> betterPlayers = new HashSet<String>();
        Set<String> worsePlayers = new HashSet<String>();

        Random rnd = new Random();

        for (int repeat = 0; repeat < repeatCount; repeat++) {
            Set<String> players = new HashSet<String>();
            for (int i = 0; i < playerCount; i++) {
                String playerName = String.valueOf(i);
                players.add(playerName);
                if (i<betterPlayerCount)
                    betterPlayers.add(playerName);
                else
                    worsePlayers.add(playerName);
            }

            Set<String> droppedPlayers = new HashSet<String>();
            Map<String, Integer> byes = new HashMap<String, Integer>();

            Set<TournamentMatch> matches = new HashSet<TournamentMatch>();
            Map<String, Set<String>> previouslyPaired = new HashMap<String, Set<String>>();
            for (String player : players)
                previouslyPaired.put(player, new HashSet<String>());

            SwissPairingMechanism pairing = new SwissPairingMechanism("swiss");
            for (int i = 1; i <= roundCount; i++) {
                if (!pairing.isFinished(i - 1, players, droppedPlayers)) {
                    List<PlayerStanding> standings = BestOfOneStandingsProducer.produceStandings(players, matches, 3, 0, byes);

                    Map<String, String> newPairings = new LinkedHashMap<String, String>();
                    Set<String> newByes = new HashSet<String>();

                    pairing.pairPlayers(i, players, droppedPlayers, byes, standings, previouslyPaired, newPairings, newByes);
                    if (newByes.size() > 0) {
                        for (String newBye : newByes) {
                            byes.put(newBye, 1);
                        }
                    }

                    for (Map.Entry<String, String> newPairing : newPairings.entrySet()) {
                        String playerOne = newPairing.getKey();
                        String playerTwo = newPairing.getValue();

                        String winner = getWinner(rnd, betterPlayers, worsePlayers, playerOne, playerTwo, chanceToWin);

                        previouslyPaired.get(playerOne).add(playerTwo);
                        previouslyPaired.get(playerTwo).add(playerOne);

                        matches.add(new TournamentMatch(playerOne, playerTwo, winner, i));
                    }
                }
            }
            List<PlayerStanding> standings = BestOfOneStandingsProducer.produceStandings(players, matches, 1, 0, byes);

            String firstSemi = getWinner(rnd, betterPlayers, worsePlayers, standings.get(0).getPlayerName(), standings.get(7).getPlayerName(), chanceToWin);
            String secondSemi = getWinner(rnd, betterPlayers, worsePlayers, standings.get(1).getPlayerName(), standings.get(6).getPlayerName(), chanceToWin);
            String thirdSemi = getWinner(rnd, betterPlayers, worsePlayers, standings.get(2).getPlayerName(), standings.get(5).getPlayerName(), chanceToWin);
            String fourthSemi = getWinner(rnd, betterPlayers, worsePlayers, standings.get(3).getPlayerName(), standings.get(4).getPlayerName(), chanceToWin);

            String firstFinalist = getWinner(rnd, betterPlayers, worsePlayers, firstSemi, fourthSemi, chanceToWin);
            String secondFinalist = getWinner(rnd, betterPlayers, worsePlayers, secondSemi, thirdSemi, chanceToWin);

            String winner = getWinner(rnd, betterPlayers, worsePlayers, firstFinalist, secondFinalist, chanceToWin);

            if (betterPlayers.contains(winner))
                betterPlayerWins++;
        }

        System.out.println(betterPlayerWins);
    }

    private String getWinner(Random rnd, Set<String> betterPlayers, Set<String> worsePlayers, String playerOne, String playerTwo, float winChance) {
        if (betterPlayers.contains(playerOne) && worsePlayers.contains(playerTwo)) {
            if (rnd.nextFloat() < winChance)
                return playerOne;
            else
                return playerTwo;
        } else if (betterPlayers.contains(playerTwo) && worsePlayers.contains(playerOne)) {
            if (rnd.nextFloat() < winChance)
                return playerTwo;
            else
                return playerOne;
        } else {
            if (rnd.nextBoolean())
                return playerOne;
            else
                return playerTwo;
        }
    }

    @Test
    public void testPairingSmallTournament() {
        int repeatCount = 10;
        int playerCount = 12;

        for (int repeat = 0; repeat < repeatCount; repeat++) {
            testSwissPairingForPlayerCount(playerCount);
        }
    }

    @Test
    public void testPairingVerySmallTournament() {
        int repeatCount = 10;
        int playerCount = 8;

        for (int repeat = 0; repeat < repeatCount; repeat++) {
            testSwissPairingForPlayerCount(playerCount);
        }
    }

    @Test
    public void testPairingSmallTournamentWithOddNumberOfPlayers() {
        int repeatCount = 10;
        int playerCount = 9;

        for (int repeat = 0; repeat < repeatCount; repeat++) {
            testSwissPairingForPlayerCount(playerCount);
        }
    }

    private void testSwissPairingForPlayerCount(int playerCount) {
        Set<String> players = new HashSet<String>();
        for (int i = 0; i < playerCount; i++)
            players.add("p" + i);

        Set<String> droppedPlayers = new HashSet<String>();
        Map<String, Integer> byes = new HashMap<String, Integer>();

        Set<TournamentMatch> matches = new HashSet<TournamentMatch>();
        Map<String, Set<String>> previouslyPaired = new HashMap<String, Set<String>>();
        for (String player : players)
            previouslyPaired.put(player, new HashSet<String>());

        SwissPairingMechanism pairing = new SwissPairingMechanism("swiss");
        for (int i = 1; i < 20; i++) {
            if (!pairing.isFinished(i - 1, players, droppedPlayers)) {
                System.out.println("Pairing round " + i);
                List<PlayerStanding> standings = BestOfOneStandingsProducer.produceStandings(players, matches, 1, 0, byes);
                for (PlayerStanding standing : standings) {
                    String player = standing.getPlayerName();
                    log(player + " points - " + standing.getPoints() + " played against: " + StringUtils.join(previouslyPaired.get(player), ","));
                }

                Map<String, String> newPairings = new LinkedHashMap<String, String>();
                Set<String> newByes = new HashSet<String>();

                assertFalse("Unable to pair for round " + i, pairing.pairPlayers(i, players, droppedPlayers, byes, standings, previouslyPaired, newPairings, newByes));
                assertEquals("Invalid number of pairings", playerCount / 2, newPairings.size());
                if (playerCount % 2 == 0)
                    assertEquals("Invalid number of byes", 0, newByes.size());
                else {
                    assertEquals("Invalid number of byes", 1, newByes.size());
                    String newBye = newByes.iterator().next();
                    log("Bye - " + newBye);
                    assertNull("Player already received bye", byes.get(newBye));
                    byes.put(newBye, 1);
                }

                for (Map.Entry<String, String> newPairing : newPairings.entrySet()) {
                    String playerOne = newPairing.getKey();
                    String playerTwo = newPairing.getValue();

                    assertFalse(previouslyPaired.get(playerOne).contains(playerTwo));
                    assertFalse(previouslyPaired.get(playerTwo).contains(playerOne));

                    System.out.println("Paired " + playerOne + " against " + playerTwo + " points - " + getPlayerPoints(standings, playerOne) + " vs " + getPlayerPoints(standings, playerTwo));
                    String winner = new Random().nextBoolean() ? playerOne : playerTwo;
                    log("Winner - " + winner);

                    previouslyPaired.get(playerOne).add(playerTwo);
                    previouslyPaired.get(playerTwo).add(playerOne);

                    matches.add(new TournamentMatch(playerOne, playerTwo, winner, i));
                }
            }
        }
        System.out.println("Final standings:");
        List<PlayerStanding> standings = BestOfOneStandingsProducer.produceStandings(players, matches, 1, 0, byes);
        for (PlayerStanding standing : standings) {
            String player = standing.getPlayerName();
            System.out.println(standing.getStanding() + ". " + player + " points - " + standing.getPoints() + " played against: " + StringUtils.join(previouslyPaired.get(player), ","));
        }
    }

    private void log(String s) {
        // System.out.println(s);
    }

    private int getPlayerPoints(List<PlayerStanding> standings, String player) {
        for (PlayerStanding standing : standings) {
            if (standing.getPlayerName().equals(player))
                return standing.getPoints();
        }
        return -1;
    }
}
