package com.cosean.sudoku.uı;

import com.cosean.sudoku.backend.FirstSolverMethod;
import com.cosean.sudoku.backend.MyBoolean;
import com.cosean.sudoku.Utils;
import com.cosean.sudoku.backend.Winner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cosean.sudoku.Utils.Type;

public class SudokuPage extends JFrame {
    private int sudokuMatris[][] = new int[9][9];
    JPanel f;
    JLabel[][] jButtonList = new JLabel[9][9];
    List<int[][]> showSudokuPastStepList;
    int showSudokuStepNumber;
    FirstSolverMethod thread1, thread2, thread3, thread4, thread5;
    static List<Thread> threadList;
    Thread animationThread;


    private JProgressBar progressBar1 = new JProgressBar();
    private JProgressBar progressBar2 = new JProgressBar();
    private JProgressBar progressBar3 = new JProgressBar();
    private JProgressBar progressBar4 = new JProgressBar();
    private JProgressBar progressBar5 = new JProgressBar();


    public SudokuPage() {
        initUI();


        f = new JPanel();
        f.setLayout(new GridLayout(9, 9));
        f.setBorder(BorderFactory.createLineBorder(Color.black));
        f.setBounds(10, 10, 585, 585);
        f.setAutoscrolls(true);
        //f.setBackground(Color.white);
        //getSudoku("C:\\sudoku.txt");

        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                JPanel p = new JPanel();
                JLabel b1 = new JLabel();
                p.setBorder(BorderFactory.createLineBorder(Color.white));
                b1.setForeground(Color.darkGray);
                b1.setFont(new Font(b1.getName(), Font.BOLD, 40));

                if (
                        ((row == 0 || row == 1 || row == 2) && (column == 0 || column == 1 || column == 2)) ||
                                ((row == 0 || row == 1 || row == 2) && (column == 6 || column == 7 || column == 8)) ||
                                ((row == 6 || row == 7 || row == 8) && (column == 6 || column == 7 || column == 8)) ||
                                ((row == 6 || row == 7 || row == 8) && (column == 0 || column == 1 || column == 2)) ||
                                ((row == 3 || row == 4 || row == 5) && (column == 3 || column == 4 || column == 5))
                        )
                    p.setBackground(new Color(0X4542AFDC, true));
                else
                    p.setBackground(new Color(0X25929FAC, true));

                p.add(b1);
                f.add(p);
                jButtonList[column][row] = b1;
            }
        }

        JButton jButton = new JButton("Start");
        jButton.addActionListener(click -> {
            threadList = new ArrayList<Thread>();
            MyBoolean myBoolean = new MyBoolean();
            Winner winner = new Winner();
            thread1 = new FirstSolverMethod(myClone(sudokuMatris), myBoolean, winner, Utils.Type.TopLeft);
            thread2 = new FirstSolverMethod(myClone(sudokuMatris), myBoolean, winner, Utils.Type.TopRight);
            thread3 = new FirstSolverMethod(myClone(sudokuMatris), myBoolean, winner,Utils.Type.Diagonal);
            thread4 = new FirstSolverMethod(myClone(sudokuMatris), myBoolean, winner,Utils.Type.BottomLeft);
            thread5 = new FirstSolverMethod(myClone(sudokuMatris), myBoolean, winner,Utils.Type.BottomRight);

            threadList.add(new Thread(thread1 ));
            threadList.add(new Thread(thread2 ));
            threadList.add(new Thread(thread3 ));
            threadList.add(new Thread(thread4 ));
            threadList.add(new Thread(thread5 ));
            for (Thread t : threadList)
                t.start();

            jButton.setText("Loading...");
            repaint();
            int runner = 0;
            do {
                runner = 0;
                for (Thread t : threadList)
                    if (t.isAlive())
                        runner++;
            } while (runner > 0);
            showSudoku(winner.getWinnerSudoku());
            showSudokuPastStepList = winner.getPastSteps();
            showSudokuStepNumber = winner.getPastSteps().size() - 1;
            jButton.setText("Start");

            progressBar1.setValue((int) Utils.completePercent(sudokuMatris, thread1.sudoku));
            progressBar2.setValue((int) Utils.completePercent(sudokuMatris, thread2.sudoku));
            progressBar3.setValue((int) Utils.completePercent(sudokuMatris, thread3.sudoku));
            progressBar4.setValue((int) Utils.completePercent(sudokuMatris, thread4.sudoku));
            progressBar5.setValue((int) Utils.completePercent(sudokuMatris, thread5.sudoku));

            progressBar1.setBorder(BorderFactory.createTitledBorder(String.format("Top Left      Time: %.2f ms", thread1.getTimer())));
            progressBar2.setBorder(BorderFactory.createTitledBorder(String.format("Top Right     Time: %.2f ms", thread2.getTimer())));
            progressBar3.setBorder(BorderFactory.createTitledBorder(String.format("Diagonal      Time: %.2f ms", thread3.getTimer())));
            progressBar4.setBorder(BorderFactory.createTitledBorder(String.format("Bottom Left   Time: %.2f ms", thread4.getTimer())));
            progressBar5.setBorder(BorderFactory.createTitledBorder(String.format("Bottom Right  Time: %.2f ms", thread5.getTimer())));

        });

        JButton jButtonNext = new JButton("Next");
        jButtonNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showSudokuPastStepList != null) {
                    if (showSudokuStepNumber != showSudokuPastStepList.size() - 1)
                        showSudokuStepNumber++;
                    showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                }
                super.mouseClicked(e);
            }
        });

        JButton jButtonPre = new JButton("Pre");
        jButtonPre.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showSudokuPastStepList != null) {
                    if (showSudokuStepNumber != 0)
                        showSudokuStepNumber--;
                    showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                }
                super.mouseClicked(e);
            }
        });


        jButton.setBounds(700, 10, 100, 40);
        jButtonNext.setBounds(495, 620, 100, 40);
        jButtonPre.setBounds(10, 620, 100, 40);
        add(jButton);
        add(jButtonNext);
        add(jButtonPre);


        progressBar1.setValue(0);
        progressBar2.setValue(0);
        progressBar3.setValue(0);
        progressBar4.setValue(0);
        progressBar5.setValue(0);
        progressBar2.setStringPainted(true);
        progressBar3.setStringPainted(true);
        progressBar1.setStringPainted(true);
        progressBar4.setStringPainted(true);
        progressBar5.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Thread-1");
        Border border2 = BorderFactory.createTitledBorder("Thread-2");
        Border border3 = BorderFactory.createTitledBorder("Thread-3");
        Border border4 = BorderFactory.createTitledBorder("Thread-4");
        Border border5 = BorderFactory.createTitledBorder("Thread-5");
        progressBar1.setBorder(border);
        progressBar2.setBorder(border2);
        progressBar3.setBorder(border3);
        progressBar4.setBorder(border4);
        progressBar5.setBorder(border5);

        progressBar1.setBounds(615, 60, 250, 60);
        progressBar2.setBounds(615, 130, 250, 60);
        progressBar3.setBounds(615, 200, 250, 60);
        progressBar4.setBounds(615, 270, 250, 60);
        progressBar5.setBounds(615, 340, 250, 60);

        progressBar1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread1.pastSteps.size() - 1;
                showSudokuPastStepList = thread1.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                showAnimations();
                super.mouseClicked(e);
            }
        });

        progressBar2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread2.pastSteps.size() - 1;
                showSudokuPastStepList = thread2.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                showAnimations();
                super.mouseClicked(e);
            }
        });

        progressBar3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread3.pastSteps.size() - 1;
                showSudokuPastStepList = thread3.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                showAnimations();
                super.mouseClicked(e);
            }
        });

        progressBar4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread4.pastSteps.size() - 1;
                showSudokuPastStepList = thread4.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                showAnimations();
                super.mouseClicked(e);
            }
        });

        progressBar5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread5.pastSteps.size() - 1;
                showSudokuPastStepList = thread5.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                showAnimations();
                super.mouseClicked(e);
            }
        });

        add(progressBar1);
        add(progressBar2);
        add(progressBar3);
        add(progressBar4);
        add(progressBar5);
        add(f);
        setVisible(true);
    }

    private static int[][] myClone(int[][] sudokuMatris) {
        int[][] my = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                my[i][j] = sudokuMatris[i][j];
            }
        }
        return my;
    }

    private void showAnimations() {

        animationThread = new Thread(new Runnable() {
            public void run() {
                for (int[][] step : showSudokuPastStepList) {
                    showSudoku(step);
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }

                }
            }
        });
        animationThread.start();

    }


    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Open");
        eMenuItem.setMnemonic(KeyEvent.VK_O);
        eMenuItem.setToolTipText("Open Sudoku File");
        eMenuItem.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text File", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getPath());
                getSudoku(chooser.getSelectedFile().getPath());
                showSudoku(sudokuMatris);
            }
        });

        JMenuItem eMenuItem2 = new JMenuItem("Exit");
        eMenuItem2.setMnemonic(KeyEvent.VK_E);
        eMenuItem2.setToolTipText("Exit application");
        eMenuItem2.addActionListener(event -> {
            System.exit(0);
        });


        file.add(eMenuItem);
        file.add(eMenuItem2);

        menubar.add(file);

        setJMenuBar(menubar);
    }

    private void showSudoku(int[][] sudoku) {
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                if (sudokuMatris[column][row] == 0)
                    jButtonList[column][row].setForeground(Color.red);
                else
                    jButtonList[column][row].setForeground(Color.black);

                jButtonList[column][row].setText(sudoku[column][row] == 0 ? " " : sudoku[column][row] + "");
                repaint();
            }
        }

    }

    private void getSudoku(String name) {
        try {
            sudokuMatris = new int[9][9];
            FileReader okuyucu = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(okuyucu);
            String satir = bufferedReader.readLine();
            int k = 0;
            while (satir != null) {
                for (int i = 0; i < satir.length(); i++) {
                    if (satir.charAt(i) != '*') {
                        sudokuMatris[k][i] = Character.getNumericValue(satir.charAt(i));
                    }
                }
                k++;
                System.out.println(satir);
                satir = bufferedReader.readLine();

            }
            okuyucu.close();
            bufferedReader.close();
        } catch (FileNotFoundException excep) {
            System.err.println("Dosya bulunamadı...");

        } catch (IOException ex) {
            System.err.println("hata olustu...");
        }
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        createMenuBar();
        setTitle("Simple example");
        setSize(900, 750);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
