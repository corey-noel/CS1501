import java.util.Stack;

/**
 * Created by Corey on 1/30/2015.
 */
public class Board {

    public final char[][] board;
    public final int height;
    public final int width;

    public Board(int h, int w, boolean wildcard) {
        width = w;
        height = h;

        board = new char[width][];

        for (int i = 0; i < width; i++) {
            board[i] = new char[height];
            for (int j = 0; j < height; j++)
                board[i][j] = (char)(Math.random() * 26 + 65); //this seems playable
        }

        if (wildcard) {
            int randW = (int) Math.random() * w;
            int randH = (int) Math.random() * h;
            board[randW][randH] = '*';
        }
    }

    public Board(String b, int h, int w) {
        width = w;
        height = h;

        if (b.length() != height * width)
            throw new IllegalArgumentException();

        board = new char[width][];

        for (int i = 0; i < width; i++) {
            board[i] = new char[height];
            for (int j = 0; j < height; j++)
                board[i][j] = Character.toUpperCase(b.charAt(i * width + j));
        }
    }

    public String toString() {
        String result = "";
        for (char[] line : board)
            for (char c : line)
                result += c;

        return result;
    }

    public String rep() {
        String result = " ___ ___ ___ ___ \n";
        for (char[] line : board) {
            result += "|   |   |   |   |\n|";
            for (char c : line) {
                result += " " + c + " |";
            }
            result += "\n|___|___|___|___|\n";
        }

        return result;
    }

    public DictionaryInterface searchBoard(DictionaryInterface result, DictionaryInterface allWords) {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                Stack<Point> path = new Stack<Point>();
                path.push(new Point(w, h));
                StringBuilder sb = new StringBuilder();
                sb.append(board[w][h]);
                search(result, allWords, path, sb);
            }
        }

        return result;
    }

    private void search(DictionaryInterface result, DictionaryInterface allWords, Stack<Point> path, StringBuilder sb) {
        for (int i = 0; i < 8; i++) {
            Point last = path.peek();
            Point next = last.getNeighbor(i);
            if (next.x >= 0 && next.x < width && next.y >= 0 && next.y < height && !path.contains(next)) {
                path.push(next);
                sb.append(board[next.x][next.y]);
                int res = allWords.search(sb);

                if (res > 2)
                    result.add(sb.toString());

                if (res % 2 == 1)
                    search(result, allWords, path, sb);

                path.pop();
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }
}
