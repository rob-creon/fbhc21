import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class ProblemB {
    public static void main(String[] args) {
        String[] data = getInputs("xs_and_os_input.txt");
        assert data != null;

        int T = Integer.parseInt(data[0]);
        int ctr = 1;
        for (int i = 0; i < T; i++) {
            int N = Integer.parseInt(data[ctr++]);
            char[][] board = new char[N][N];
            for (int j = 0; j < N; j++) {
                board[j] = data[ctr++].toCharArray();
            }
            System.out.println("Case #" + (i + 1) + ": " + new ProblemB().solve(N, board));
        }
    }

    private static String[] getInputs(String name) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));
            return reader.lines().toArray(String[]::new);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private int project1d(int i, int j, int n) {
        return i * n + j;
    }

    public String solve(int n, char[][] board) {
        //if a line contains an O then winning on that line is impossible
        //otherwise, the # of turns needed to win on that line is equal to the # of empty tiles
        int[] map = new int[n * n];
        boolean possible = false;

        Set<Integer> potentialCollisions = new HashSet<>();

        outer:
        for (int i = 0; i < n; i++) {
            int emptySpaces = 0;
            int lastIndex = 0;
            inner:
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') continue outer;
                if (board[i][j] == '.') {
                    lastIndex = project1d(i, j, n);
                    emptySpaces++;
                }
            }
            //emptySpaces = # of turns required to win on this line
            map[emptySpaces]++;
            possible = true;

            //if there was only one tile needed to place, then there is a potential collision
            if (emptySpaces == 1) potentialCollisions.add(lastIndex);
        }

        outer:
        for (int i = 0; i < n; i++) {
            int emptySpaces = 0;
            int lastIndex = 0;
            inner:
            for (int j = 0; j < n; j++) {
                if (board[j][i] == 'O') continue outer;
                if (board[j][i] == '.') {
                    emptySpaces++;
                    lastIndex = project1d(j, i, n);
                }
            }
            //emptySpaces = # of turns required to win on this line
            if (emptySpaces != 1 || !potentialCollisions.contains(lastIndex))
                map[emptySpaces]++;
            possible = true;
        }

        if (!possible) return "Impossible";
        else for (int i = 0; i < map.length; i++) if (map[i] > 0) return i + " " + map[i];
        return "Impossible"; //error
    }
}