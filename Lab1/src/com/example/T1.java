
//==================================
//Головний клас потоків
//==================================

package com.example;

import java.util.Arrays;

public class T1 extends Thread{

    public T1(String name, int priority, long stackSize) {
        super(null, null, name, stackSize);
        this.setPriority(priority);
    }

    @Override
    public void run() {
        System.out.println(getName() + " run | Priority: " + getPriority());

        //ВВЕДЕННЯ даних
        int[] B = Data.fillVector(1);
        int[] C = Data.fillVector(1);
        int[][] MA = Data.fillMatrix(1);
        int[][] ME = Data.fillMatrix(1);

        //обчислення функції в потоці
        long startTime = System.currentTimeMillis();
        int[][] MD = Data.func1(B, C, MA, ME);
        long time = System.currentTimeMillis() - startTime;


        if (Data.getN() == 3) {
            System.out.println("Result MD:");
            System.out.println(Arrays.deepToString(MD));
        }

        //виведення
        System.out.println(getName() + " finished | Execution time: " + time + " ms");
    }
}

