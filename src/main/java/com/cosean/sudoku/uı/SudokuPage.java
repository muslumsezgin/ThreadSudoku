package com.cosean.sudoku.uı;

import com.cosean.sudoku.backend.SolverMethod;
import com.cosean.sudoku.backend.ThreadFlag;
import com.cosean.sudoku.backend.Winner;
import com.cosean.sudoku.utils.Utils;

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

import static com.cosean.sudoku.utils.Utils.sudokuClone;

public class SudokuPage extends JFrame {

    enum Chosen {OPEN, SAVE}

    private int sudokuMatris[][] = new int[9][9];
    private JLabel[][] jButtonList = new JLabel[9][9];
    private List<int[][]> showSudokuPastStepList;
    private int showSudokuStepNumber;
    private SolverMethod thread1, thread2, thread3, thread4, thread5, thread6;
    private List<Thread> threadList;
    private Thread animationThread;
    private boolean isStop = false;

    private JProgressBar progressBar1 = new JProgressBar();
    private JProgressBar progressBar2 = new JProgressBar();
    private JProgressBar progressBar3 = new JProgressBar();
    private JProgressBar progressBar4 = new JProgressBar();
    private JProgressBar progressBar5 = new JProgressBar();
    private JProgressBar progressBar6 = new JProgressBar();
    private JLabel selected = new JLabel();


    public SudokuPage() {
        initUI();

        addSudokuPanel();

        createStartSudokuButton();

        createAllButtons();

        progressBarCreate();

        setVisible(true);
    }

    private void createStartSudokuButton() {
        JButton jButton = new JButton("Start");
        jButton.addActionListener(click -> {
            threadList = new ArrayList<>();
            ThreadFlag myBoolean = new ThreadFlag();
            Winner winner = new Winner();
            thread1 = new SolverMethod(sudokuClone(sudokuMatris), myBoolean, winner, Utils.Type.TopLeft);
            thread2 = new SolverMethod(sudokuClone(sudokuMatris), myBoolean, winner, Utils.Type.TopRight);
            thread3 = new SolverMethod(sudokuClone(sudokuMatris), myBoolean, winner, Utils.Type.Diagonal);
            thread4 = new SolverMethod(sudokuClone(sudokuMatris), myBoolean, winner, Utils.Type.BottomLeft);
            thread5 = new SolverMethod(sudokuClone(sudokuMatris), myBoolean, winner, Utils.Type.BottomRight);
            thread6 = new SolverMethod(sudokuClone(sudokuMatris), myBoolean, winner, Utils.Type.Spiral);

            threadList.add(new Thread(thread1));
            threadList.add(new Thread(thread2));
            threadList.add(new Thread(thread3));
            threadList.add(new Thread(thread4));
            threadList.add(new Thread(thread5));
            threadList.add(new Thread(thread6));

            for (Thread t : threadList)
                t.start();

            jButton.setText("Loading...");
            repaint();
            int runner;
            do {
                runner = 0;
                for (Thread t : threadList)
                    if (t.isAlive())
                        runner++;
            } while (runner > 0);

            showSudoku(winner.getWinnerSudoku());
            showSudokuPastStepList = winner.getPastSteps();
            showSudokuStepNumber = winner.getPastSteps().size() - 1;
            if (winner.getWinnerSudoku() == thread1.sudoku)
                selected.setText("Selected: Thread Top Left");
            if (winner.getWinnerSudoku() == thread2.sudoku)
                selected.setText("Selected: Thread Top Right");
            if (winner.getWinnerSudoku() == thread3.sudoku)
                selected.setText("Selected: Thread Diagonal");
            if (winner.getWinnerSudoku() == thread4.sudoku)
                selected.setText("Selected: Thread Bottom Left");
            if (winner.getWinnerSudoku() == thread5.sudoku)
                selected.setText("Selected: Thread Bottom Right");
            if (winner.getWinnerSudoku() == thread6.sudoku)
                selected.setText("Selected: Thread Spiral");

            jButton.setText("Start");

            progressBar1.setValue((int) Utils.completePercent(sudokuMatris, thread1.sudoku));
            progressBar2.setValue((int) Utils.completePercent(sudokuMatris, thread2.sudoku));
            progressBar3.setValue((int) Utils.completePercent(sudokuMatris, thread3.sudoku));
            progressBar4.setValue((int) Utils.completePercent(sudokuMatris, thread4.sudoku));
            progressBar5.setValue((int) Utils.completePercent(sudokuMatris, thread5.sudoku));
            progressBar6.setValue((int) Utils.completePercent(sudokuMatris, thread6.sudoku));

            progressBar1.setBorder(BorderFactory.createTitledBorder(String.format("Top Left      Time: %.2f ms", thread1.getTimer())));
            progressBar2.setBorder(BorderFactory.createTitledBorder(String.format("Top Right     Time: %.2f ms", thread2.getTimer())));
            progressBar3.setBorder(BorderFactory.createTitledBorder(String.format("Diagonal      Time: %.2f ms", thread3.getTimer())));
            progressBar4.setBorder(BorderFactory.createTitledBorder(String.format("Bottom Left   Time: %.2f ms", thread4.getTimer())));
            progressBar5.setBorder(BorderFactory.createTitledBorder(String.format("Bottom Right  Time: %.2f ms", thread5.getTimer())));
            progressBar6.setBorder(BorderFactory.createTitledBorder(String.format("Spiral        Time: %.2f ms", thread6.getTimer())));

        });

        jButton.setBounds(695, 10, 100, 40);
        add(jButton);

    }

    private void createAllButtons() {
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

        JButton jButtonPre = new JButton("Preview");
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

        JButton jButtonPlay = new JButton("Play");
        jButtonPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (animationThread != null && animationThread.isAlive()) {
                    animationThread.interrupt();
                    jButtonPlay.setText("Start");
                    isStop = true;
                } else {
                    jButtonPlay.setText("Stop");
                    isStop = false;
                    showAnimations(jButtonPlay);
                }
                repaint();
                super.mouseClicked(e);
            }
        });

        JButton jButtonClear = new JButton("Clear");
        jButtonClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sudokuMatris = new int[9][9];
                showSudokuPastStepList = null;
                showSudokuStepNumber = 0;
                progressBarClearValue();
                showSudoku(sudokuMatris);
                super.mouseClicked(e);
            }
        });

        JButton jButtonOpen = new JButton("Open");
        jButtonOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                choseFile(Chosen.OPEN);
                super.mouseClicked(e);
            }
        });

        JButton save = new JButton("Save");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Utils.printDataFile(showSudokuPastStepList, choseFile(Chosen.SAVE));
                super.mouseClicked(e);
            }
        });

        JButton saveAll = new JButton("SaveAll");
        saveAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String path = choseFile(Chosen.SAVE);
                Utils.printDataFile(thread1.pastSteps, path + "TopLeft");
                Utils.printDataFile(thread2.pastSteps, path + "TopRight");
                Utils.printDataFile(thread3.pastSteps, path + "Diagonal");
                Utils.printDataFile(thread4.pastSteps, path + "BottomLeft");
                Utils.printDataFile(thread5.pastSteps, path + "BottomRight");
                Utils.printDataFile(thread6.pastSteps, path + "Spiral");
                super.mouseClicked(e);
            }
        });


        save.setBounds(635, 550, 100, 40);
        saveAll.setBounds(745, 550, 100, 40);

        jButtonClear.setBounds(810, 10, 80, 40);
        jButtonOpen.setBounds(600, 10, 80, 40);
        jButtonPlay.setBounds(250, 620, 100, 40);
        jButtonNext.setBounds(495, 620, 100, 40);
        jButtonPre.setBounds(10, 620, 100, 40);
        add(save);
        add(saveAll);
        add(jButtonOpen);
        add(jButtonPlay);
        add(jButtonClear);
        add(jButtonNext);
        add(jButtonPre);
    }

    private void progressBarClearValue() {
        progressBar1.setValue(0);
        progressBar2.setValue(0);
        progressBar3.setValue(0);
        progressBar4.setValue(0);
        progressBar5.setValue(0);
        progressBar6.setValue(0);
        progressBar2.setStringPainted(true);
        progressBar3.setStringPainted(true);
        progressBar1.setStringPainted(true);
        progressBar4.setStringPainted(true);
        progressBar5.setStringPainted(true);
        progressBar6.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Thread");
        progressBar1.setBorder(border);
        progressBar2.setBorder(border);
        progressBar3.setBorder(border);
        progressBar4.setBorder(border);
        progressBar5.setBorder(border);
        progressBar6.setBorder(border);
        selected.setText("");
    }

    private void progressBarCreate() {
        progressBarClearValue();

        progressBar1.setBounds(615, 70, 250, 60);
        progressBar2.setBounds(615, 140, 250, 60);
        progressBar3.setBounds(615, 210, 250, 60);
        progressBar4.setBounds(615, 280, 250, 60);
        progressBar5.setBounds(615, 350, 250, 60);
        progressBar6.setBounds(615, 420, 250, 60);
        selected.setBounds(635, 495, 250, 60);

        progressBar1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread1.pastSteps.size() - 1;
                showSudokuPastStepList = thread1.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                selected.setText("Selected: Thread Top Left");
                super.mouseClicked(e);
            }
        });

        progressBar2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread2.pastSteps.size() - 1;
                showSudokuPastStepList = thread2.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                selected.setText("Selected: Thread Top Right");
                super.mouseClicked(e);
            }
        });

        progressBar3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread3.pastSteps.size() - 1;
                showSudokuPastStepList = thread3.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                selected.setText("Selected: Thread Diagonal");
                super.mouseClicked(e);
            }
        });

        progressBar4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread4.pastSteps.size() - 1;
                showSudokuPastStepList = thread4.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                selected.setText("Selected: Thread Bottom Left");
                super.mouseClicked(e);
            }
        });

        progressBar5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread5.pastSteps.size() - 1;
                showSudokuPastStepList = thread5.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                selected.setText("Selected: Thread Bottom Right");
                super.mouseClicked(e);
            }
        });

        progressBar6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSudokuStepNumber = thread6.pastSteps.size() - 1;
                showSudokuPastStepList = thread6.pastSteps;
                showSudoku(showSudokuPastStepList.get(showSudokuStepNumber));
                selected.setText("Selected: Thread Spiral");
                super.mouseClicked(e);
            }
        });

        add(progressBar1);
        add(progressBar2);
        add(progressBar3);
        add(progressBar4);
        add(progressBar5);
        add(progressBar6);
        add(selected);
    }

    private void showAnimations(JButton jButtonPlay) {
        animationThread = new Thread(() -> {
            for (int[][] step : showSudokuPastStepList) {
                if (isStop)
                    return;
                showSudoku(step);
                try {
                    Thread.sleep(10);
                } catch (Exception ignored) {
                }

            }
            jButtonPlay.setText("Start");
        });
        animationThread.start();

    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenu help = new JMenu("Help");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Open");
        eMenuItem.setMnemonic(KeyEvent.VK_O);
        eMenuItem.setToolTipText("Open Sudoku File");
        eMenuItem.addActionListener(event -> choseFile(Chosen.OPEN));

        JMenuItem eMenuItem2 = new JMenuItem("Exit");
        eMenuItem2.setMnemonic(KeyEvent.VK_E);
        eMenuItem2.setToolTipText("Exit application");
        eMenuItem2.addActionListener(event -> System.exit(0));

        JMenuItem eMenuItemFile = new JMenuItem("Check for updates");
        eMenuItemFile.setMnemonic(KeyEvent.VK_T);
        eMenuItemFile.setToolTipText("Check for updates");
        eMenuItemFile.addActionListener(ev->
               JOptionPane.showMessageDialog(SudokuPage.this,
                        "You already have lastest version",
                        "Check for updates.",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        JMenuItem eMenuItemFile2 = new JMenuItem("About");
        eMenuItemFile2.setMnemonic(KeyEvent.VK_P);
        eMenuItemFile2.setToolTipText("About");
        eMenuItemFile2.addActionListener(event ->  JOptionPane.showMessageDialog(SudokuPage.this,
                "Developers:\nMüslüm Sezgin - Anıl Coşar",
                "About",
                JOptionPane.INFORMATION_MESSAGE)
        );
        file.add(eMenuItem);
        file.add(eMenuItem2);

        help.add(eMenuItemFile);
        help.add(eMenuItemFile2);

        menuBar.add(file);
        menuBar.add(help);

        setJMenuBar(menuBar);
    }

    private String choseFile(Chosen chosen) {
        String userDir = System.getProperty("user.home");

        JFileChooser chooser = new JFileChooser(userDir + "/Desktop");
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text File", "txt");
        chooser.setFileFilter(filter);

        if (chosen == Chosen.OPEN) {
            int returnVal = chooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getPath());
                getSudoku(chooser.getSelectedFile().getPath());
                showSudoku(sudokuMatris);
            }
        } else if (chosen == Chosen.SAVE) {
            int returnVal = chooser.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getPath());
                return chooser.getSelectedFile().getPath();
            }
        }

        return "";

    }

    private void addSudokuPanel() {
        JPanel sudokuPanel = new JPanel();
        sudokuPanel.setLayout(new GridLayout(9, 9));
        sudokuPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sudokuPanel.setBounds(10, 10, 585, 585);
        sudokuPanel.setAutoscrolls(true);

        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                JPanel p = new JPanel();
                JLabel b1 = new JLabel();
                p.setBorder(BorderFactory.createLineBorder(Color.white));
                b1.setForeground(Color.black);
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
                sudokuPanel.add(p);
                jButtonList[column][row] = b1;
            }
        }
        add(sudokuPanel);
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
            while (satir != null && satir.length() == 9) {
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
        createMenuBar();
        setTitle("Cosean Sudoku");
        setSize(900, 750);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
