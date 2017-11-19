package com.cosean.sudoku.backend;

import java.util.Arrays;

public class MyBoolean {

    private volatile boolean isFinished;



    public boolean isFinished() {
        return isFinished;
    }

    public synchronized void setFinished(boolean finished) {
        isFinished = finished;
    }

}
