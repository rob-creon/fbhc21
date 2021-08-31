import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ProblemC1 {

    private record Tunnel(int caveA, int caveB) {
    }

    public static void main(String[] args) {
        String[] data = getInputs();
        assert data != null;

        int T = Integer.parseInt(data[0]);
        int ctr = 1;
        for (int i = 0; i < T; i++) {
            int n = Integer.parseInt(data[ctr++]);
            int[] gold = Arrays.stream(data[ctr++].split(" ")).map(Integer::parseInt).mapToInt(v -> v).toArray();
            Tunnel[] tunnels = new Tunnel[n - 1];
            for (int j = 0; j < n - 1; j++) {
                int[] nums = Arrays.stream(data[ctr++].split(" ")).map(Integer::parseInt).mapToInt(v -> v).toArray();
                tunnels[j] = new Tunnel(nums[0] - 1, nums[1] - 1); // -1 to change from [1] to [0]
            }
            System.out.println("Case #" + (i + 1) + ": " + new ProblemC1().solve(n, gold, tunnels));
        }
    }

    private static String[] getInputs() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("gold_mine_chapter_1_input.txt"));
            return reader.lines().toArray(String[]::new);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int solve(int n, int[] gold, Tunnel[] tunnels) {
        Node[] nodes = new Node[n];

        Node root = generateTree(n, gold, tunnels, 0, null, nodes);
        Node highest = root;
        for (Node node : nodes) {
            if (node.score > highest.score) highest = node;
        }
        if (highest == root) return highest.score;
        Node highestBranch = getBranch(highest);
        Node nextHighest = root;
        for (Node node : nodes) {
            if (node == highest) continue;
            if (node.children.size() != 0) continue;
            if (node.score < nextHighest.score) continue;
            if (getBranch(node) == highestBranch) continue;
            nextHighest = node;
        }
        return nextHighest.score + highest.score - gold[0];
    }

    private static class Node {
        public int index;
        public int value;
        public int score;
        public Node parent;
        public List<Node> children;

        public Node(int index, int value) {
            this(index, value, null);
        }

        public Node(int index, int value, Node parent) {
            this.index = index;
            this.value = value;
            this.parent = parent;
            this.children = new ArrayList<>();
        }
    }

    public Node generateTree(int n, int[] gold, Tunnel[] tunnels, int nodeIndex, Node parent, Node[] nodes) {
        Node node = new Node(nodeIndex, gold[nodeIndex], parent);
        nodes[nodeIndex] = node;
        node.score = gold[nodeIndex];
        if (parent != null) node.score += parent.score;
        List<Integer> childrenIndexes = new ArrayList<>();
        for (Tunnel t : tunnels) {
            if (t.caveA == nodeIndex && (parent == null || t.caveB != parent.index)) childrenIndexes.add(t.caveB);
            else if (t.caveB == nodeIndex && (parent == null || t.caveA != parent.index)) childrenIndexes.add(t.caveA);
        }
        for (Integer childIndex : childrenIndexes) {
            node.children.add(generateTree(n, gold, tunnels, childIndex, node, nodes));
        }
        return node;
    }

    private Node getBranch(Node n) {
        if (n.parent == null) return n;
        Node checker = n;
        while (checker.parent != null && checker.parent.parent != null) checker = checker.parent;
        return checker;
    }
}