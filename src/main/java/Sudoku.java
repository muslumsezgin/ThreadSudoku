import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {

    /**
     * Print the specified Sudoku problem and its solution.  The
     * problem is encoded as specified in the class documentation
     * above.
     *
     * @param args The command-line arguments encoding the problem.
     */
   /* public static void main(String[] args) {
        int[][] matrix = parseProblem(args);
        writeMatrix(matrix);
        if (solve(0,0,matrix))    // solves in place
            writeMatrix(matrix);
        else
            System.out.println("NONE");
    }

    static boolean solve(int i, int j, int[][] cells) {
        if (i == 9) {
            i = 0;
            if (++j == 9)
                return true;
        }
        if (cells[i][j] != 0)  // skip filled cells
            return solve(i+1,j,cells);

        for (int val = 1; val <= 9; ++val) {
            if (legal(i,j,val,cells)) {
                cells[i][j] = val;
                if (solve(i+1,j,cells))
                    return true;
            }
        }
        cells[i][j] = 0; // reset on backtrack
        return false;
    }

    static boolean legal(int i, int j, int val, int[][] cells) {
        for (int k = 0; k < 9; ++k)  // row
            if (val == cells[k][j])
                return false;

        for (int k = 0; k < 9; ++k) // col
            if (val == cells[i][k])
                return false;

        int boxRowOffset = (i / 3)*3;
        int boxColOffset = (j / 3)*3;
        for (int k = 0; k < 3; ++k) // box
            for (int m = 0; m < 3; ++m)
                if (val == cells[boxRowOffset+k][boxColOffset+m])
                    return false;

        return true; // no violations, so it's legal
    }

    static int[][] parseProblem(String[] args) {
        int[][] problem = new int[9][9]; // default 0 vals
        try{
            FileReader okuyucu=new FileReader("C:\\Users\\anil\\Desktop\\sudoku.txt");
            BufferedReader bufferedReader=new BufferedReader(okuyucu);
            String satir=bufferedReader.readLine();
            int k=0;
            while (satir!=null) {
                for (int i = 0; i < satir.length(); i++) {
                    if(satir.charAt(i)!='*') {
                        problem[k][i] = Character.getNumericValue(satir.charAt(i));
                    }
                }
                k++;
                System.out.println(satir);
                satir = bufferedReader.readLine();

            }
            okuyucu.close();
            bufferedReader.close();
        }catch(FileNotFoundException excep){
            System.err.println("Dosya bulunamadı...");

        }catch(IOException ex){
            System.err.println("hata olustu...");
        }
        return problem;
    }

    static void writeMatrix(int[][] solution) {
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                System.out.println(" -----------------------");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(solution[i][j] == 0
                        ? " "
                        : Integer.toString(solution[i][j]));

                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }
*/
}