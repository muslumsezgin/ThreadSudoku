public class Winner {
    private String text ;
    private int [][] winnerSudoku;

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
