import java.util.Scanner;
import java.util.Random;

class WordSearchGenerator {
    private Scanner input; // scanner for user input
    private Random rand; // random variable

    // generating the 2d arrays
    private char[][] wordSearch; 
    private char[][] solution;

    // Test array with words in it
    String[] words = {"Apple", "Banana", "Grape", "Orange", "Pear"};
    String[] dirtyWords;


    public WordSearchGenerator() {
        // initializing Scanner and Random
        this.input = new Scanner(System.in);
        this.rand = new Random();
        
        // iniializing the arrays
        solution = new char[20][20];
        wordSearch = new char[20][20];

        char response = printIntro();
        dirtyWords = backwardsArray(words);

        switch (response) {
            case 'g':
                
                break;
            case 'p':

                break;
            case 's':

                break;
            case 'q':

                break;
            default:
                break;
        }

        // input.close(); // closes the scanner
    }

    // This method prints out the intro to the word search
    // returns the value 
    public char printIntro() {
        
        // Menu
        System.out.printf("%s%n%s%n%s%n%s%n%s%n%s%n%s%n",
                        "Welcome to my word search generator!",
                        "This program will allow you to generate your own word search puzzle",
                        "Please selet an option:",
                        "Generate a new word search (g)",
                        "Print out your word search (p)",
                        "Show the solution to your word search (s)",
                        "Quite the program");

        // the first letter what the user inputed
        char response = input.nextLine().toLowerCase().charAt(0);
        
        return response;
    }

    // generates a 2d array asking for input 
    public void generate() {

        // loop for every String in array
        for (int i = 0; i < words.length; i++){

        }
    }

    // reverses the word
    public String backwards(String word) {
        String reverseWord = ""; 
        for (int i = word.length()-1; i >= 0; i--){
            reverseWord += word.charAt(i);
        }

        return reverseWord;
    }

    // randomized whether the words in the array are backwards
    public String[] backwardsArray(String[] clean) {
        String[] dirty = new String[clean.length]; // new array w/ same length
        int reverse; // placeholder for random number

        // 50% percent chance whether the word will be flipped
        for (int i = 0; i < clean.length; i++) {
            reverse = rand.nextInt(2); // rand int between 0 and 1
            if (reverse == 1) {
                dirty[i] = backwards(clean[i]);
            } else {
                dirty[i] = clean[i];
            }
        }

        return dirty;
    }
    
    // prints the word search
    public void print() {
        System.out.println("Here is your word search!\nHave fun!");

    }

    // prints solution with X's in place of the random letters
    public void showSolution() {

    }
}