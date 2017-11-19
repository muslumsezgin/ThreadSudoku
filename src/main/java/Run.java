import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Run {

    static int sudokuMatris[][]= new int[9][9];
    static List<Thread> threadList=new ArrayList<Thread>();

    public static void main(String[] args) {
        try{
            FileReader okuyucu=new FileReader("C:\\Users\\anil\\Desktop\\sudoku.txt");
            BufferedReader bufferedReader=new BufferedReader(okuyucu);
            String satir=bufferedReader.readLine();
            int k=0;
            while (satir!=null) {
                for (int i = 0; i < satir.length(); i++) {
                    if(satir.charAt(i)!='*') {
                        sudokuMatris[k][i] = Character.getNumericValue(satir.charAt(i));
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
        MyBoolean myBoolean =new MyBoolean();
        Winner winner =new Winner();
        FirstSolverMethod firstSolverMethod =new FirstSolverMethod(myClone(sudokuMatris),myBoolean,winner);
        FirstSolverMethod firstSolverMethod2 =new FirstSolverMethod(myClone(sudokuMatris),myBoolean,winner);
        FirstSolverMethod firstSolverMethod3 =new FirstSolverMethod(myClone(sudokuMatris),myBoolean,winner);

        threadList.add(new Thread(firstSolverMethod));
        threadList.add(new Thread(firstSolverMethod2));
        threadList.add(new Thread(firstSolverMethod3));
        for(Thread t:threadList)
            t.start();


        int runner=0;
        do{
            runner=0;
            for(Thread t :threadList)
                if(t.isAlive())
                    runner++;
        }while (runner>0);

        writeMatrix(winner.getWinnerSudoku());
        System.out.println(winner.getText());


    }

    private static int[][] myClone(int[][] sudokuMatris) {
        int [][] my =new int[9][9];
        for (int i = 0; i < sudokuMatris.length; i++) {
            for (int j = 0; j < sudokuMatris.length; j++) {
                my[i][j]=sudokuMatris[i][j];
            }
        }
        return my;
    }

    public static synchronized void stop() throws IOException {
        for (Thread threads : threadList)
            threads.interrupt();
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
}
