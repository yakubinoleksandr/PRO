/**
 * <program header>
 * Дисципліна: Паралельні та розподілені обчислення
 * Лабораторна робота 2.2. Мова Java. Семафори, критичні секції, атомік-змінні, бар’єрі
 * Варіант 10: MR = MB * (MC * MM) * d + min(Z) * MC
 * Введення: Т1 - МВ, Т3 - МС, Т4 - Z, d, MM
 * Виведення T2 - MR
 * ПІБ студента: Якубін Олександр
 * Група: ІО-44
 * 05.10.2026
 */

//==================================
//Головний клас запуску потоків
//==================================

import java.util.Scanner;

public class Lab2 {

    public static void main(String[] args) {
        Data data = new Data();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Натисніть ENTER щоб почати обчислення");

        if (scanner.hasNextLine()) {
            //Створення потоків
            T1 T1 = new T1("T1", data);
            T2 T2 = new T2("T2", data);
            T3 T3 = new T3("T3", data);
            T4 T4 = new T4("T4", data);

            long startTime = System.currentTimeMillis();

            System.out.println("================ Головний процес запущено ================");

            //Запуск потоків
            T1.start();
            T2.start();
            T3.start();
            T4.start();

            try {
                // Очікування сигналу від усіх 4-х потоків через семафор
                data.semMain.acquire(Data.P);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long finishTime = System.currentTimeMillis() - startTime;

            System.out.println("================ Головний процес закінчено. Час виконання: "
                    + finishTime
                    + "mc ================");
        }
    }
}
