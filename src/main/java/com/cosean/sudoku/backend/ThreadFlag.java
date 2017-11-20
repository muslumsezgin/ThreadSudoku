package com.cosean.sudoku.backend;

public class ThreadFlag {

    private volatile boolean isFinished;

    public boolean isFinished() {
        return isFinished;
    }

    public synchronized void setFinished(boolean finished) {
        isFinished = finished;
    }

}
