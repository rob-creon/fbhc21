import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ProblemA2 {

    public static void main(String[] args) {
        String[] data = getInputs();
        assert data != null;
        int T = Integer.parseInt(data[0]);
        int ctr = 1;
        for (int i = 0; i < T; i++) {
            String S = data[ctr++];
            int K = Integer.parseInt(data[ctr++]);
            Transition[] transitions = new Transition[K];
            for (int j = 0; j < K; j++) {
                String line = data[ctr++];
                transitions[j] = new Transition(line.charAt(0), line.charAt(1));
            }
            System.out.println("Case #" + (i + 1) + ": " + new ProblemA2().solve(S, transitions));
        }
    }

    private static String[] getInputs() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("consistency_chapter_2_input.txt"));
            return reader.lines().toArray(String[]::new);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private record Transition(char from, char to) {
        public String toString() {
            return "{" + from + "->" + to + "}";
        }
    }

    public int solve(String text, Transition[] transitions) {

        final int ALPHABET_SIZE = 26;

        // Create a distance matrix representation of the graph of transitions
        int[][] d = new int[ALPHABET_SIZE][ALPHABET_SIZE];
        fillArr(d, Integer.MAX_VALUE); //by default we assume there is no connection between them
        for (Transition t : transitions) {
            int from = t.from - 'A';
            int to = t.to - 'A';
            d[from][to] = 1;
        }

        // Use Floyd Warshall Algorithm to find the distance between each pair of nodes
        for (int k = 0; k < ALPHABET_SIZE; k++) {
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                for (int j = 0; j < ALPHABET_SIZE; j++) {
                    if (d[i][k] == Integer.MAX_VALUE || d[k][j] == Integer.MAX_VALUE) continue; //infinity handling
                    if (d[i][k] + d[k][j] < d[i][j]) {
                        if (d[i][k] == Integer.MAX_VALUE || d[k][j] == Integer.MAX_VALUE)
                            d[i][j] = Integer.MAX_VALUE; //infinity handling
                        else d[i][j] = d[i][k] + d[k][j];
                    }
                }
            }
        }

        // for every letter in the alphabet, check if it is a possible state for each char in the string.
        // track the highest distance (seconds) between the char node and the target alphabet char node
        int[] alphabet = new int[ALPHABET_SIZE];
        fillArr(alphabet, 0);
        aloop:
        for (char c = 'A'; c < 'A' + ALPHABET_SIZE; c++) {
            for (int charIndex = 0; charIndex < text.length(); charIndex++) {
                if (c == text.charAt(charIndex)) continue;
                int distance = d[text.charAt(charIndex) - 'A'][c - 'A'];
                if (distance == Integer.MAX_VALUE) {
                    alphabet[c - 'A'] = -1;
                    continue aloop; //this alphabet char is not possible
                }
                alphabet[c - 'A'] += distance;
            }
        }

        int least = Integer.MAX_VALUE;
        for (int j : alphabet) if (j != -1 && j < least) least = j;
        return least == Integer.MAX_VALUE ? -1 : least;
    }

    private void fillArr(int[][] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = val;
            }
        }
    }

    private void fillArr(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = val;
        }
    }

}
