/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wordle.engine;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

/**
 *
 * @author MAN
 */
public class StatsManager {

    private final String GAMES_PLAYED = "GamesPlayed";
    private final String GAMES_WON = "GamesWon";
    private final String TOTAL_TRYS = "TotalTrys";
    private final String BEST_TRY = "BestTry";
    private final String ACTUAL = "ActualBestSeries";
    private final String BEST = "AllTimeBestSeries";

    private int gamesPlayed;
    private int gamesWon;

    private int totalTrys;       // Total number of tries
    private int bestTry;         // Best Try
    private final int tryStats[];

    private int actualBestSeries;
    private int allTimeBestSeries;

    private Properties appProps = new Properties();

    /**
     *
     * @param wordSize
     * @param maxGuesses
     */
    public StatsManager(int wordSize, int maxGuesses) {
        tryStats = new int[maxGuesses];
        reset();
    }

    /**
     *
     */
    public final void reset() {
        gamesPlayed = 0;
        gamesWon = 0;

        totalTrys = 0;
        bestTry = 0;
        tryStats[0] = 0;
        tryStats[1] = 0;
        tryStats[2] = 0;
        tryStats[3] = 0;
        tryStats[4] = 0;
        tryStats[5] = 0;

        actualBestSeries = 0;
        allTimeBestSeries = 0;
    }

    /**
     *
     */
    public final void load() {
        System.out.println("(-) Loading Stats...");

        try {
            appProps.load(new FileInputStream("wordle.properties"));

            gamesPlayed = Integer.parseInt(appProps.getProperty(GAMES_PLAYED));
            gamesWon = Integer.parseInt(appProps.getProperty(GAMES_WON));

            totalTrys = Integer.parseInt(appProps.getProperty(TOTAL_TRYS));
            tryStats[0] = Integer.parseInt(appProps.getProperty("Try1"));
            tryStats[1] = Integer.parseInt(appProps.getProperty("Try2"));
            tryStats[2] = Integer.parseInt(appProps.getProperty("Try3"));
            tryStats[3] = Integer.parseInt(appProps.getProperty("Try4"));
            tryStats[4] = Integer.parseInt(appProps.getProperty("Try5"));
            tryStats[5] = Integer.parseInt(appProps.getProperty("Try6"));
            bestTry = Integer.parseInt(appProps.getProperty(BEST_TRY));

            actualBestSeries = 0;
            allTimeBestSeries = Integer.parseInt(appProps.getProperty(BEST));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("(!) Error Loading Stats.");
            System.out.println("(-) Reseting stats...");
            reset();
        }
    }

    /**
     *
     */
    void updateTotalTry() {
        totalTrys++;

        save();
    }

    /**
     *
     * @param won
     * @param guessTry
     */
    public void updateGame(boolean won, int guessTry) {
        gamesPlayed++;

        if (won) {
            System.out.println("[DEBUG] Add Win for guess " + guessTry);
            tryStats[guessTry]++;

            gamesWon++;

            actualBestSeries++;

            if (actualBestSeries > allTimeBestSeries) {
                allTimeBestSeries = actualBestSeries;
            }

        } else {
            actualBestSeries = 0;
        }

        save();
    }

    /**
     *
     */
    public void save() {
        try {
            appProps.setProperty(GAMES_PLAYED, Integer.toString(gamesPlayed));
            appProps.setProperty(GAMES_WON, Integer.toString(gamesWon));

            appProps.setProperty(TOTAL_TRYS, Integer.toString(totalTrys));
            appProps.setProperty("Try1", Integer.toString(tryStats[0]));
            appProps.setProperty("Try2", Integer.toString(tryStats[1]));
            appProps.setProperty("Try3", Integer.toString(tryStats[2]));
            appProps.setProperty("Try4", Integer.toString(tryStats[3]));
            appProps.setProperty("Try5", Integer.toString(tryStats[4]));
            appProps.setProperty("Try6", Integer.toString(tryStats[5]));
            appProps.setProperty(BEST_TRY, Integer.toString(bestTry));

            appProps.setProperty(BEST, Integer.toString(allTimeBestSeries));

            appProps.store(new FileWriter("wordle.properties"), "Worldl82 properties file");
        } catch (Exception e) {
            System.out.println("[ERROR] Error Saving Stats.\n");
        }

    }

    private int getBestTry() {
        if (totalTrys == 0) {
            return 0;
        }

        int result = 0;
        int maxTryStat = tryStats[0];

        for (int i = 1; i < tryStats.length; i++) {
            if (tryStats[i] > maxTryStat) {
                result = i;
            }
        }

        return result + 1;
    }

    /**
     *
     */
    public void display() {
        System.out.println("----------------------------------------------------------------");
        System.out.println("Statistics\n");

        System.out.print("Games: " + gamesPlayed);
        System.out.print("   Won: " + gamesWon);
        System.out.println("   %Won: " + (double) gamesWon / (double) gamesPlayed * 100.00 + "%");

        System.out.println("Best Try: #" + getBestTry() + "   Actual: " + actualBestSeries + "   Best: " + allTimeBestSeries);

        System.out.println("Guess 1: " + (double) tryStats[0] / (double) totalTrys * 100 + "%");
        System.out.println("Guess 2: " + (double) tryStats[1] / (double) totalTrys * 100 + "%");
        System.out.println("Guess 3: " + (double) tryStats[2] / (double) totalTrys * 100 + "%");
        System.out.println("Guess 4: " + (double) tryStats[3] / (double) totalTrys * 100 + "%");
        System.out.println("Guess 5: " + (double) tryStats[4] / (double) totalTrys * 100 + "%");
        System.out.println("Guess 6: " + (double) tryStats[5] / (double) totalTrys * 100 + "%");

        System.out.println("----------------------------------------------------------------\n");
    }

}
