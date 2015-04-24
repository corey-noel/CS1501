import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Corey on 1/30/2015.
 */
public class MyBoggle {

    public static void main(String[] args) {
        String fileName = null;
        String dicType = null;
        DictionaryInterface allWords = null;
        DictionaryInterface boardWords = null;
        Board board;

        //arg parsing
        String arg;
        for (int i = 0; i < args.length; i++) {
            arg = args[i];

            if (arg.startsWith("-")) {
                arg = arg.toLowerCase();

                if (arg.length() < 2)
                    throw new IllegalArgumentException("No argument provided");

                for (int j = 1; j < arg.length(); j++) {
                    char c = arg.charAt(j);

                    if (c == 'b') {
                        if (args.length <= i + j) {
                            throw new IllegalArgumentException("No board provided");
                        } else if (fileName != null) {
                            throw new IllegalArgumentException("Too many boards");
                        } else {
                            fileName = args[i + j];
                        }
                    }

                    if (c == 'd') {
                        if (args.length <= i + j) {
                            throw new IllegalArgumentException("No dictionary type provided");
                        } else if (dicType != null) {
                            throw new IllegalArgumentException("Too many dictionary types");
                        } else {
                            dicType = args[i + j];
                        }

                        if (dicType.equalsIgnoreCase("simple")) {
                            allWords = new SimpleDictionary();
                            boardWords = new SimpleDictionary();
                        } else if (dicType.equalsIgnoreCase("dlb")) {
                            allWords = new DLBDictionary();
                            boardWords = new DLBDictionary();
                        } else {
                            throw new IllegalArgumentException(
                                    "Valid dictionary types are \"simple\" and \"dlb\"");
                        }
                    }
                }
            }
        }

        if (fileName == null)
            fileName = "board.txt";

        if (dicType == null) {
            dicType = "default";
            allWords = new DLBDictionary();
            boardWords = new DLBDictionary();
        }

        System.out.println("Filename: " + fileName);
        System.out.println("DictType: " + dicType);

        //board creation
        try {
            Scanner boardScanner = new Scanner(new File(fileName));
            board = new Board(boardScanner.nextLine(), 4, 4);
        } catch(FileNotFoundException e) {
            throw new IllegalArgumentException("File at " + fileName + " not found");
        }

        //import dictionary
        try {
            Scanner dictionaryScanner = new Scanner(new File("dictionary.txt"));
            while (dictionaryScanner.hasNextLine())
                allWords.add(dictionaryScanner.nextLine());
        } catch(FileNotFoundException e) {
            throw new IllegalArgumentException("Dictionary at dictionary.txt not found");
        }

        //search board for words
        board.searchBoard(boardWords, allWords);

        //start game finally
        System.out.println("Welcome to Boggle™!\n");
        System.out.println(board.rep());
        System.out.println("What words do you see?");

        Scanner kb = new Scanner(System.in);
        boolean guessing = true;
        String input;
        ArrayList<String> guesses = new ArrayList<String>();

        while (guessing) {
            System.out.print("Guess [w]ord, display [b]oard, or [f]inish guessing: ");
            input = kb.next().toLowerCase();
            if (input.startsWith("w")) {
                System.out.print("Guess word: ");
                input = kb.next();
                if (input.length() < 3) {
                    System.out.println("Words must be at least 3 letters long");
                } else {
                    if (boardWords.search(new StringBuilder(input)) >= 2) {
                        if (guesses.contains(input)) {
                            System.out.println("You already guessed " + input + "!");
                        } else {
                            guesses.add(input);
                            input = Character.toUpperCase(input.charAt(0)) + input.substring(1);
                            System.out.println("Yep! " + input + " is in there.");
                        }
                    } else {
                        input = Character.toUpperCase(input.charAt(0)) + input.substring(1);
                        System.out.println(input + " isn't in there...");
                    }
                }
            } else if (input.startsWith("b")) {
                System.out.println(board.rep());
            } else if (input.startsWith("f")) {
                guessing = false;
            } else {
                System.out.println("Bad input.");
            }
        }

        System.out.println("\nAlright! Let's see how you did...");
        System.out.println("You guessed " + guesses.size() + " words:");
        for (String s : guesses)
            System.out.println(s);
        System.out.println();

        //TODO
        ArrayList<String> boardWordsList = boardWords.getAll();

        System.out.println("There were " + boardWordsList.size() + " possible words:");
        for (String s : boardWordsList)
            System.out.println(s);
        System.out.println();

        System.out.println("That's " + guesses.size() + "/" + boardWordsList.size() +
                " possible words.");
        System.out.println("Or " + (double) guesses.size()/boardWordsList.size() + "%");
    }
}
