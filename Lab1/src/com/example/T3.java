package com.example;

import java.util.Arrays;

public class T3 extends Thread{

    public T3(String name, int priority, long stackSize) {
        super(null, null, name, stackSize);
        this.setPriority(priority);
    }

    @Override
    public void run() {
        System.out.println(getName() + " run | Priority: " + getPriority());

        //заповнення даних
        int[][] MP = Data.fillMatrix(3);
        int[][] MR = Data.fillMatrix(3);
        int[] V = Data.fillVector(3);

        //обчислення функції в потоці
        long startTime = System.currentTimeMillis();
        int[] O = Data.func3(MP, MR, V);
        long time = System.currentTimeMillis() - startTime;


        if (Data.getN() == 3) {
            System.out.println("Result O:");
            System.out.println(Arrays.toString(O));
        }

        //виведення
        System.out.println(getName() + " finished | Execution time: " + time + " ms");
    }
}
