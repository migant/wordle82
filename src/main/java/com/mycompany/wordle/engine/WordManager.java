/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wordle.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Word Manager for Wordle82
 *
 * @author MAN
 */
public class WordManager {

    private final ArrayList<String> dictionary;

    private String chosenWord;

    private MatchResult lastMatchResult;

    private String hints;
    private String exactChars;
    private String existingChars;
    private String badChars;

    /**
     *
     */
    public static enum MatchResult {
        INVALID_WORD,
        HINTS_AVAILABLE,
        MATCHED_WORD
    }

    /**
     * Constructor
     *
     * @param filename File from where to load words from
     * @param wordSize Load words with specified size only
     */
    public WordManager(String filename, int wordSize) {
        dictionary = new ArrayList<>();
        loadWords(filename, wordSize);
        chosenWord = "";
    }

    /**
     * loadWords - Loads words from specified filename with specified length
     *
     * @param filename File from where to load words from
     * @param ws Load words with specified size only
     *
     */
    private void loadWords(String filename, int ws) {
        BufferedReader reader;

        dictionary.clear();

        try {
            reader = new BufferedReader(new FileReader(filename));

            String line = reader.readLine();

            while (line != null) // Read till the end of the file
            {
                line = line.trim().toUpperCase();

                if (line.length() == ws) // Only accept words of ws length
                {
                    dictionary.add(line);
                }

                line = reader.readLine();   // read next line
            }

            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            loadHardcodedWords();
        }

        System.out.println("[DEBUG] There were " + dictionary.size() + " words loaded.");
    }

    private void loadHardcodedWords() {
        dictionary.clear();
        dictionary.add("amigo");
        dictionary.add("leite");
        dictionary.add("termo");
        dictionary.add("ativo");
        dictionary.add("etico");
        dictionary.add("amiga");
        dictionary.add("elite");
        dictionary.add("terma");
        dictionary.add("ativa");
        dictionary.add("etica");
    }

    /**
     * getRandomWord - Get a random word from the word list
     *
     * @return Random Word string
     *
     */
    public String getRandomWord() {
        Random rand = new Random();
        int randWord = rand.nextInt(dictionary.size());
        chosenWord = dictionary.get(randWord).toUpperCase();

        System.out.println("[DEBUG] Palavra escolhida foi: " + chosenWord);

        exactChars = "";
        existingChars = "";
        badChars = "";

        return chosenWord;
    }

    /**
     * isWordValid - Check if a word exists in the dictionary
     *
     * @param word Word to be validated
     * @return true if word is known
     *
     */
    public boolean isWordValid(String word) {
        return (dictionary.indexOf(word) >= 0);
    }

    /**
     *
     * @param word
     * @return
     */
    public MatchResult match(String word) {
        if (!isWordValid(word)) {
            hints = "";
            lastMatchResult = MatchResult.INVALID_WORD;
            return lastMatchResult;
        }

        char hintChar;

        hints = "";

        for (int i = 0; i < chosenWord.length(); i++) {

            // Check if there was a correct guessed letter
            if (word.charAt(i) == chosenWord.charAt(i)) {
                hintChar = '!'; // Char exists in this position

                if (exactChars.indexOf(word.charAt(i)) == -1) {
                    exactChars += word.charAt(i);
                }
            } // Check if there was a existing letter
            else if (chosenWord.indexOf(word.charAt(i)) >= 0) {
                hintChar = '.'; // Char exists in another position

                if (existingChars.indexOf(word.charAt(i)) == -1) {
                    existingChars += word.charAt(i);
                }
            } else {
                hintChar = '_'; // Char doesn't exist

                if (badChars.indexOf(word.charAt(i)) == -1) {
                    badChars += word.charAt(i);
                }
            }

            hints += hintChar; // Char doesn't exist 
        }

        if (hints.equals("!!!!!")) {
            lastMatchResult = MatchResult.MATCHED_WORD;
            return lastMatchResult;
        }

        lastMatchResult = MatchResult.HINTS_AVAILABLE;
        return lastMatchResult;
    }

    public MatchResult getLastMatchResult() {
        return lastMatchResult;
    }

    public String getHints() {
        return hints;
    }

    public String getExactChars() {
        return exactChars;
    }

    public String getExistingChars() {
        return existingChars;
    }

    public String getBadChars() {
        return badChars;
    }

}
