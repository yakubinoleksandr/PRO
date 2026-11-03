/**
 * <program header>
 * Дисципліна: Паралельні та розподілені обчислення
 * Лабораторна робота 1.3. Потоки в мові Java
 * F1: MD = (B * C) * (MA * ME)
 * F2: MF = MIN(MH) * MK * ML
 * F3: O = TRANS(MP * MR) * V
 * ПІБ студента: Якубін Олександр
 * Група: ІО-44
 * 07.09.2026
 */

//==================================
//Головний клас запуску потоків
//==================================

package com.example;

import java.util.Scanner;

public class Lab1 {

    public static void main(String[] args) {

        //розмір стеку
        long stackSize = 1024*1024*2;

        //ввід розміру матриць та векторів
        System.out.print("Введіть розмірність N: ");
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        new Data(N);

        //створення потоків
        T1 t1 = new T1("T1", Thread.NORM_PRIORITY, stackSize);
        T2 t2 = new T2("T2", Thread.MIN_PRIORITY, stackSize);
        T3 t3 = new T3("T3", Thread.MAX_PRIORITY, stackSize);

        //запуск потоків
        System.out.println("================ Main thread started ================");
        long startTime = System.currentTimeMillis();

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
        long endTime = System.currentTimeMillis();

        //вивід результату
        System.out.println("================ Main thread finished. Execution time: "
                + (endTime - startTime)
                + "ms ================");
    }

}
