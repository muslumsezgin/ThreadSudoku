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

        threadList.add(new Thread(new FirstSolverMethod(sudokuMatris)));
        threadList.add(new Thread(new FirstSolverMethod(sudokuMatris)));
        threadList.add(new Thread(new FirstSolverMethod(sudokuMatris)));
        for(Thread t:threadList)
            t.start();
        try
        {
            while(threadList.get(0).isAlive())
            {
                System.out.println("Main thread will be alive till the child thread is live");
                Thread.sleep(1500);
            }
    }
        catch(InterruptedException e)
        {
            System.out.println("Main thread interrupted");
        }
        System.out.println("Main thread's run is over" );

            System.out.println("-------------------------------------");
            for (int column = 0; column < 9; column++) {
                System.out.println();
                for (int row = 0; row < 9; row++) {
                    System.out.print(sudokuMatris[column][row]);
                }
            }
            System.out.println();
            System.out.println("-------------------------------------");


    }

    public synchronized void stop() throws IOException {
        for (Thread threads : threadList)
            threads.interrupt();
    }
}
