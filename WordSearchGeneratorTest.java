
public class WordSearchGeneratorTest {
    public static void main(String[] args) {
        WordSearchGenerator wSearch = new WordSearchGenerator();

        for (int i = 0; i < wSearch.words.length; i++) {
            System.out.println(wSearch.words[i]);
        }
        for (int i = 0; i < wSearch.words.length; i++) {
            System.out.println(wSearch.dirtyWords[i]);
        }
    }
}
