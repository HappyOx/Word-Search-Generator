import java.util.Scanner;
import java.util.Random;

class WordSearchGenerator {
    private Scanner input; // scanner for user input
    private Random rand; // random variable

    // Dimentions rows and columns
    private int rows = 10;
    private int cols = 10;

    // generating the 2d arrays
    private char[][] wordSearch; 
    private char[][] solution;

    // Test array with words in it. It can do an 8 x 8 2d array
    private String[] words = {"APPLE", "BANANA", "GRAPE", "ORANGE", "PEAR", "KIWI", "CHERRY", "DATES", "PLUM"};

    private String[] dirtyWords; // modified array; Values have 50% of being in reverse
    private String[] userWords; // user input words


    public WordSearchGenerator() {
        // initializing Scanner and Random
        this.input = new Scanner(System.in);
        this.rand = new Random();
        
        // iniializing the arrays
        userWords = new String[7]; // Ammount of words in search
        solution = new char[rows][cols];
        wordSearch = new char[rows][cols];
    }

    // Starts the program
    public void start() {
        char response; // user response variable
        
        // intro
        System.out.printf("s%n%s%n%n",
        "Welcome to my word search generator!",
        "This program will allow you to generate your own word search puzzle");

        // char returned from intro prompt
        do {
            response = menu();

            switch (response) {
                case 'g':
                    userPrompt(); // prompts for user input of words
                    generate();
                    break;
                case 'p':
                    print();
                    break;
                case 's':
                    showSolution();
                    break;
                case 'q':
                    System.out.println("Have a great day!");
                    break;
            }
        } while (response != 'q');

        input.close(); // closes the scanner
    }

    // Prompts the user when generating the word search
    private void userPrompt() {
        System.out.printf("%s%n%s%n",
                        "You get to type 7 words to be placed in your word search!",
                        "The dimentions of the word search are 10 x 10");

        for (int i = 0; i < 7; i++) {
            System.out.print("Word " + (i+1) + ": ");
            userWords[i] = input.nextLine().toUpperCase();
        }

        System.out.println();
    }

    // This method prints out the intro to the word search
    // returns the value 
    public char menu() {
        char response;
        boolean repeat = false; // do we require the user to repeat their entry

        // Menu
        System.out.printf("%s%n%s%n%s%n%s%n%s%n%n",
                        "Please selet an option:",
                        "Generate a new word search (g)",
                        "Print out your word search (p)",
                        "Show the solution to your word search (s)",
                        "Quit the program (q)");

        do {
            if (repeat) {
                System.out.println("Invalid Reponse. Please try again.");
            }
            repeat = true; // triggers the above statement if it's after the first runthrough

            // the first letter what the user inputed
            response = input.nextLine().toLowerCase().charAt(0);
        } while (response != 'g' && response != 'p' && response != 's' && response != 'q');

        return response; // returns the char response
    }

    // generates a 2d array asking for input 
    public void generate() {
        // two random ints based on the dimentions of the word search
        int row;
        int col;

        // 50% chance the words are backwards in this array
        dirtyWords = backwardsArray(userWords);
        
        // four different orientations that the word can be in
        // 0: horizontal
        // 1: vertical
        // 2: diagonal positive slope
        // 3: diagonal negative slope
        int orientation;

        boolean fits; // does the word fit in the dimentions
        String word; // word
        int wordLen; // word length

        // loop for every String in array
        for (int i = 0; i < dirtyWords.length; i++){
            word = dirtyWords[i]; // word at index i
            wordLen = word.length(); // length of word
            fits = false; 

            // loops until the word is able to fit
            while (!fits){ 
                //sets the position of the word
                row = rand.nextInt(rows); 
                col = rand.nextInt(cols);
                // sets the orientation of the word
                orientation = rand.nextInt(4); 
                // returns t/f whether the word fits with the above variables
                fits = isSpace(word, wordLen, row, col, orientation);
                
                // if it fits, it will be inserted into the word search
                if (fits) {
                    insert(word, wordLen, row, col, orientation);
                }
            }
            fits = false; // resets the fits booleon
        }

        // copies the 2d array
        for (int i = 0; i < rows; i++) {
            solution[i] = wordSearch[i].clone();
        }

        fill(); // fills the wordsearch and it's solution
    }

    // fills in the word search and it's solution
    private void fill() {
        // Word Search fill
        // Fill every null value with a random letter
        for (int i = 0; i < rows; i++) { // rows
            for (int j = 0; j < cols; j++) { // columns
                char letter = wordSearch[i][j];
                if (letter == 0) {
                    wordSearch[i][j] = (char)(rand.nextInt(26) + 65); // random uppercase char
                }
            }
        }

        // Solution fill
        // filling every null value with a *
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char letter = solution[i][j];
                if (letter == 0) {
                    solution[i][j] = '*';
                }
            }
        }
    }

    // checks if there is space for the word to fit
    private Boolean isSpace(String word, int wordLen, int row, int col, int orientation) {

        if (orientation == 0) { // HORIZONTAL
            if (((cols - 1) - col) >= wordLen) {
                // secondly determines if word conficts with whatever is already down
                for (int i = 0; i < wordLen; i++){
                    // returns false if the value is not null or not the same char in the word as the word search
                    if (wordSearch[row][i+col] != 0 && wordSearch[row][col+i] != word.charAt(i)) {
                        return false;
                    }
                }
                // returns true if it passes the test
                return true;
            } else {
                // if it doesn't fit based on size of string and position
                return false;
            }
        } else if (orientation == 1) { // VERTICAL
            if (((rows - 1) - row) >= wordLen) {
                // secondly determines if word conficts with whatever is already down
                for (int i = 0; i < wordLen; i++){
                    // returns false if the value is not null or not the same char in the word as the word search
                    if (wordSearch[row+i][col] != 0 && wordSearch[row+i][col] != word.charAt(i)) {
                        return false;
                    }
                }
                // returns true if it passes the test
                return true;
            } else {
                // if it doesn't fit based on size of string and position
                return false;
            }
        } else if (orientation == 2) { // DIAGONAL POSITIVE
            if ((row + 1) >= wordLen && ((cols - 1) - col) >= wordLen) {
                for (int i = 0; i < wordLen; i++){
                    if (wordSearch[row-i][col+i] != 0 && wordSearch[row-i][col+i] != word.charAt(i)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (orientation == 3) { // DIAGONAL NEGATIVE
            if (((rows - 1) - row) >= wordLen && ((cols - 1) - col) >= wordLen) {
                // secondly determines if word conficts with whatever is already down
                for (int i = 0; i < wordLen; i++){
                    // returns false if the value is not null or not the same char in the word as the word search
                    if (wordSearch[row+i][col+i] != 0 && wordSearch[row+i][col+i] != word.charAt(i)) {
                        return false;
                    }
                }
                // returns true if it passes the test
                return true;
            } else {
                // if it doesn't fit based on size of string and position
                return false;
            }
        }
        
        return false;
    }   

    // inserts word into wordsearch
    private void insert(String word, int wordLen, int row, int col, int orientation) {

        if (orientation == 0) {
            // Horizontal words
            for (int i = 0; i < wordLen; i++) {
            wordSearch[row][col + i] = word.charAt(i);
            }
        } else if (orientation == 1) {
            // Vertical words
            for (int i = 0; i < wordLen; i++) {
            wordSearch[row + i][col] = word.charAt(i);
            }
        } else if (orientation == 2) {
            // Diagonal pos
            for (int i = 0; i < wordLen; i++) {
            wordSearch[row - i][col + i] = word.charAt(i);
            }
        } else if (orientation == 3) {
            // Diagonal neg
            for (int i = 0; i < wordLen; i++) {
            wordSearch[row + i][col + i] = word.charAt(i);
            }
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

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(" " + wordSearch[i][j] + " ");
            } 
            System.out.println(); // new line
        }
        System.out.println(); // new line
    }

    // prints solution with *'s in place of the random letters
    public void showSolution() {
        System.out.println("Here is your solution!");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(" " + solution[i][j] + " ");
            } 
            System.out.println(); // new line
        }
        System.out.println(); // new line
    }
}