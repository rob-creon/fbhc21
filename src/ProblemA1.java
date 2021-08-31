import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class ProblemA1 {

    public static void main(String[] args) {
        String[] data = getValidationSet();
        for (int i = 1; i < data.length; i++) {
            System.out.println("Case #" + i + ": " + new ProblemA1().solve(data[i]));
        }
    }

    private static String[] getValidationSet() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("consistency_chapter_1_input.txt"));
            return reader.lines().toArray(String[]::new);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static final String VOWELS = "AEIOU";

    private static boolean isVowel(char c) {
        return VOWELS.indexOf(c) != -1;
    }

    public int solve(String text) {

        // count vowels and consonants and find most common vowel and consonant
        HashMap<Character, Integer> vMap = new HashMap<>();
        HashMap<Character, Integer> cMap = new HashMap<>();

        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (isVowel(ch)) {
                if (vMap.containsKey(ch)) vMap.put(ch, vMap.get(ch) + 1);
                else vMap.put(ch, 1);
            } else {
                if (cMap.containsKey(ch)) cMap.put(ch, cMap.get(ch) + 1);
                else cMap.put(ch, 1);
            }
        }

        char cdot = '0';
        int cdotCount = 0;
        int consonants = 0;
        for (Character ch : cMap.keySet()) {
            if (cMap.get(ch) > cdotCount) {
                cdot = ch;
                cdotCount = cMap.get(ch);
            }
            consonants += cMap.get(ch);
        }
        int cNotCDot = consonants - cdotCount;

        char vdot = '0';
        int vdotCount = 0;
        int vowels = 0;
        for (Character ch : vMap.keySet()) {
            if (vMap.get(ch) > vdotCount) {
                vdot = ch;
                vdotCount = vMap.get(ch);
            }
            vowels += vMap.get(ch);
        }
        int vNotVDot = vowels - vdotCount;

        int sOfCdot = 2 * cNotCDot + vowels;
        int sOfVdot = 2 * vNotVDot + consonants;

        return Math.min(sOfCdot, sOfVdot);
    }
}
