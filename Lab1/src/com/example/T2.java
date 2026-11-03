package com.example;

import java.util.Arrays;

public class T2 extends Thread{

    public T2(String name, int priority, long stackSize) {
        super(null, null, name, stackSize);
        this.setPriority(priority);
    }

    @Override
    public void run() {
        System.out.println(getName() + " run | Priority: " + getPriority());

        //заповнення даних
        int[][] MH = Data.fillMatrix(2);
        int[][] MK = Data.fillMatrix(2);
        int[][] ML = Data.fillMatrix(2);

        //обчислення функції в потоці
        long startTime = System.currentTimeMillis();
        int[][] MF = Data.func2(MH, MK, ML);
        long time = System.currentTimeMillis() - startTime;


        if (Data.getN() == 3) {
            System.out.println("Result MF:");
            System.out.println(Arrays.deepToString(MF));
        }

        //виведення
        System.out.println(getName() + " finished | Execution time: " + time + " ms");
    }
}

