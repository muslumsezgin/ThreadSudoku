import java.util.ArrayList;
import java.util.List;

public class Winner {
    private String text ;
    private int [][] winnerSudoku;
    private List<int[][]> pastSteps;

    public List<int[][]> getPastSteps() {
        return pastSteps;
    }

    public void setPastSteps(List<int[][]> pastSteps) {
        this.pastSteps = pastSteps;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int[][] getWinnerSudoku() {
        return winnerSudoku;
    }

    public void setWinnerSudoku(int[][] winnerSudoku) {
        this.winnerSudoku = winnerSudoku;
    }
}
