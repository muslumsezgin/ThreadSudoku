public class Utils {

    enum Type {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
        Diogonal,
        Spiral,
    };

    public static double completePercent(int [][] firstSudokuMatris,int[][] lastSudokuMatris){
        int haveZero=0;
        int firstInput=0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(firstSudokuMatris[i][j]==0)
                    firstInput++;
                if(lastSudokuMatris[i][j]==0)
                    haveZero++;
            }
        }
        System.out.println(firstInput+"/"+haveZero);

        return 100-((haveZero*100)/(firstInput));
    }

    public static int[][] myClone(int[][] sudokuMatris) {
        int[][] my = new int[9][9];
        for (int i = 0; i < sudokuMatris.length; i++) {
            for (int j = 0; j < sudokuMatris.length; j++) {
                my[i][j] = sudokuMatris[i][j];
            }
        }
        return my;
    }

}
