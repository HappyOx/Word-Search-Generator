import java.util.Scanner;
import java.util.Random;

// TODO: User chooses dimentions of word search
// TODO: User chooses number of words to add
// TODO: Throw exception when words dont fit (i.e. too long, too little space based on previous placements)
// TODO: Reset/clear the wordSearch matrix if the user wants to generate another one
// TODO: Combine print() and showSolution()

class WordSearchGenerator {
    private Scanner input; // scanner for user input
    private Random rand; // random variable

    // Dimentions rows and columns
    private int rows = 10;
    private int cols = 10;

    // generating the 2d arrays
    private char[][] wordSearch; 
    private char[][] solution;

    // TEST ARRAY with words in it. It can do an 8 x 8 2d array
    private String[] words = {"APPLE", "BANANA", "GRAPE", "ORANGE", "PEAR", "KIWI", "CHERRY", "DATES", "PLUM"};

    private String[] userWords; // user input words
    private String[] dirtyWords; // modified array; Values have 50% of being in reverse


    // Constructor. Initializes the scanner, rand object, and arrays
    public WordSearchGenerator() {
        // initializing Scanner and Random
        this.input = new Scanner(System.in);
        this.rand = new Random();
        
        // iniializing the arrays
        userWords = new String[7]; // Ammount of words in search
        solution = new char[rows][cols]; // Solution matrix
        wordSearch = new char[rows][cols]; // Wordsearch matrix
    }

    // Starts the program
    public void start() {
        char response; // user response variable

        // intro text
        System.out.printf("s%n%s%n%n",
        "Welcome to my word search generator!",
        "This program will allow you to generate your own word search puzzle");

        // Loop continues while the user has not chosen to quit the program
        do {
            response = menu(); // gives user a menu, returns a char and saves it

            // switch case based on the menu options
            switch (response) {
                case 'g':
                    userPrompt(); // prompts for user input of words
                    generate(); // generates the wordsearch based on the words
                    break;
                case 'p':
                    print(); // Prints the word search
                    break;
                case 's':
                    showSolution(); // Shows the solution
                    break;
                case 'q':
                    System.out.println("Have a great day!"); // Exit prompt
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

    // This method prints out the menu to the word search
    // returns a char value to determine which menu option will be selected
    private char menu() {
        char response; // placeholder for user response
        boolean repeat = false; // do we require the user to repeat their entry

        // Menu printout
        System.out.printf("%s%n%s%n%s%n%s%n%s%n%n",
                        "Please selet an option:",
                        "Generate a new word search (g)",
                        "Print out your word search (p)",
                        "Show the solution to your word search (s)",
                        "Quit the program (q)");


        // Repeats until the user enters a proper answer
        do {
            if (repeat) { // error response
                System.out.println("Invalid Reponse. Please try again.");
            }
            repeat = true; // triggers the above statement if it's after the first runthrough

            // the first letter what the user inputed
            response = input.nextLine().toLowerCase().charAt(0); // shortens the text to one character
        } while (response != 'g' && response != 'p' && response != 's' && response != 'q');

        return response; // returns the char response
    }

    // generates a word search based on the dimentions as well as the words given by the user
    // This creates both the word search and it's solution
    private void generate() {
        // row and col will be the starting position of the word upon insertion
        int row;
        int col;

        // converts the user's array of words into an array filled
        // with words that have a 50% chance of being flipped
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
            // if the string does not fit, it randomizes it's position and orientaiton
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

        // copies the 2d array from wordsearch to solution
        // .clone() only works 1 array at a time.
        for (int i = 0; i < rows; i++) {
            solution[i] = wordSearch[i].clone();
        }

        // fills the wordsearch and it's solution with random char's or '*' respectively
        fill(); 
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
    // it has 4 different arguements based on the orientation
    private Boolean isSpace(String word, int wordLen, int row, int col, int orientation) {

        // it has 4 different arguements based on the orientation
        // PLEASE LOOK AT ORIENTATION 0 FOR COMMENTS
        if (orientation == 0) { // HORIZONTAL
            if (((cols - 1) - col) >= wordLen) { // checks for horizontal space
                for (int i = 0; i < wordLen; i++){ // iterates through word
                    // checks if the spot is not empty and if the char at that position
                    // does not match the spot on the word search
                    if (wordSearch[row][i+col] != 0 && wordSearch[row][col+i] != word.charAt(i)) {
                        return false; // returns false if the spot is taken and the letters do not match
                    }
                }
                // returns true if it fits based on space AND
                // if does not encounter a letter that would clash with the placement/orientation of the word
                return true;
            } else {
                return false; // returns false if it does not fit based on space
            }
        } else if (orientation == 1) { // VERTICAL
            if (((rows - 1) - row) >= wordLen) {
                for (int i = 0; i < wordLen; i++){
                    if (wordSearch[row+i][col] != 0 && wordSearch[row+i][col] != word.charAt(i)) {
                        return false;
                    }
                }
                return true;
            } else {
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
                for (int i = 0; i < wordLen; i++){
                    if (wordSearch[row+i][col+i] != 0 && wordSearch[row+i][col+i] != word.charAt(i)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        
        return false; // returns false if it doesn't enter in the loop
    }   

    // inserts word into wordsearch
    // it has 4 different arguements based on the orientation
    private void insert(String word, int wordLen, int row, int col, int orientation) {

        // it has 4 different arguements based on the orientation
        // PLEASE LOOK AT ORIENTATION 0 FOR COMMENTS
        if (orientation == 0) {
            // Horizontal words
            for (int i = 0; i < wordLen; i++) { // iterates through the string based on it's lenght
                wordSearch[row][col + i] = word.charAt(i); // adds char to word seach based on orientation
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
        // iterates from the last char to the first of the string
        // to reverse the word given
        for (int i = word.length()-1; i >= 0; i--){
            reverseWord += word.charAt(i); 
        }
        return reverseWord; // returns the reversed word
    }

    // randomized whether the words in the array are backwards
    // this method used backwards() for every string in the array
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
        return dirty; // returns the "dirty" array. Dirty
    }
    
    // I AM NOW REALIZING THAT I COULD HAVE MADE THIS ONE METHOD
    // prints the word search
    public void print() {
        System.out.println("Here is your word search!\nHave fun!");

        // iterates through the matrix and prints the char with spaces " " in between
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

        // iterates through the solution matrix and prints the char with spaces " " in between
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(" " + solution[i][j] + " ");
            } 
            System.out.println(); // new line
        }
        System.out.println(); // new line
    }
}