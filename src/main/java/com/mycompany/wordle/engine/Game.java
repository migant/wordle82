/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wordle.engine;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author MAN
 */
public class Game {

    private final WordManager wordManager;

    private final StatsManager stats;

    private int guessTry;
    private final String[] guessHints;

    /**
     *
     * @param wordSize
     * @param maxGuesses
     */
    public Game(int wordSize, int maxGuesses) {
        banner();

        guessTry = 0;
        guessHints = new String[maxGuesses];

        stats = new StatsManager(wordSize, maxGuesses);
        stats.load();

        wordManager = new WordManager("palavras", wordSize);
    }

    /**
     *
     */
    public final void banner() {
        System.out.println("");
        System.out.println("Wordle82 v0.1");
        System.out.println("");
        System.out.println("");
    }

    /**
     *
     */
    public void clearStatistics() {
        stats.reset();
        stats.save();
        newGame();
    }

    /**
     *
     */
    public void newGame() {
        wordManager.getRandomWord();

        guessTry = 0;

        stats.display();
    }

    /**
     *
     * @return
     */
    public Integer getCurrentGuessTry() {
        return guessTry + 1; // return guesses from 1 to max guesses
    }

    /**
     *
     * @return
     */
    public boolean hasMoreGuesses() {
        return (guessTry < 6);
    }

    /**
     *
     * @param userGuess
     * @return
     */
    public boolean checkGuess(String userGuess) {
        guessHints[guessTry] = "";

        switch (wordManager.match(userGuess)) {
            case INVALID_WORD:

                System.out.println("[ERROR] Unknown word. Try again.");

                return false;

            case HINTS_AVAILABLE:

                guessHints[guessTry] = wordManager.getHints();

                showHints();

                stats.updateTotalTry();

                guessTry++;

                return false;

            case MATCHED_WORD:

                System.out.println("[DEBUG] Word Matched! at Try " + guessTry + 1);

                stats.updateGame(true, guessTry);
                stats.updateTotalTry();

                break;
        }

        return true;
    }

    public void showHints() {
        System.out.print("Guess Hints:  ");
        System.out.println(Arrays.toString(guessHints));

        System.out.println("Exact Letters:    " + wordManager.getExactChars());
        System.out.println("Existing Letters: " + wordManager.getExistingChars());
        System.out.println("Bad Letters:      " + wordManager.getBadChars());
        System.out.println("");
    }

    public void run() {

        while (true) {
            
            this.newGame();

            String userGuess;

            while (this.hasMoreGuesses()) {
                
                userGuess = readGuess("Guess(" + this.getCurrentGuessTry() + ") Word  ", 5);

                if (userGuess.equals("------")) {
                    this.clearStatistics();
                    break;
                }

                if (this.checkGuess(userGuess.toUpperCase())) {
                    stats.display();
                    break;
                }

            }

        }

    }

    private static Scanner input = new Scanner(System.in);

    public static String readGuess(String prompt, int size) {
        String line = "";

        do {

            System.out.print(prompt + " (Type " + size + " letters or c to clear statistics or . to exit): ");
            line = input.nextLine();

            if (line.equals("c")) {
                return "------";
            }

            if (line.equals(".")) {
                System.exit(0);
            }

        } while (line.length() != size);

        return line;
    }

}
