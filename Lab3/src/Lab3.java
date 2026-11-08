/**
 * <program header>
 * Дисципліна: Паралельні та розподілені обчислення
 * Лабораторна робота 3.2. Мова Java. Монітори
 * Варіант 16: A = p *(sort(d * B + Z * MM) * (MX * MT))+ (B * Z) * Z
 * Введення: Т1 - MM, B, MX, Т3 - p, Т4 - Z, d, MT
 * Виведення T4 - A
 * ПІБ студента: Якубін Олександр
 * Група: ІО-44
 * 04.11.2026
 */

//==================================
//Головний клас запуску потоків
//==================================

import java.io.IOException;
import java.util.Scanner;

public class Lab3 {

    public static Data data;
    public static Monitor monitor;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        //Вводимо розмірність N
        System.out.print("Введіть розмірність N: ");
        int N = scanner.nextInt();

        //Створення потоків та монітору
        data = new Data(N);
        monitor = new Monitor(data);

        T1 T1 = new T1(data);
        T2 T2 = new T2(data);
        T3 T3 = new T3(data);
        T4 T4 = new T4(data);

        //Вводимо кількість ядер
        System.out.print("\n[ПАУЗА] Вистав кількість ядер: ");
        int process = scanner.nextInt();

        //Команда для прив'язки ядер
        String command = String.format("powershell -Command \"(Get-Process -Id %d).ProcessorAffinity = %d\"",
                ProcessHandle.current().pid(), ((1L << process)) - 1);
        Process exec = Runtime.getRuntime().exec(command);
        exec.waitFor();

        //Запуск потоків
        System.out.print("\nНатисни ENTER для запуску розрахунків...");
        scanner.nextLine();
        scanner.nextLine();

        long startTime = System.currentTimeMillis();
        System.out.println("\n================ Головний процес запущено ================");
        T1.start();
        T2.start();
        T3.start();
        T4.start();

        T1.join();
        T2.join();
        T3.join();
        T4.join();

        long finishTime = System.currentTimeMillis() - startTime;
        System.out.println("================ Головний процес закінчено. Час виконання: "
                + finishTime
                + "mc ================");
    }
}
