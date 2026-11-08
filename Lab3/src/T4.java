// ================= ПОТІК T4 =================

import java.util.Arrays;

public class T4 extends Thread {

    private final Data data;
    private final Monitor monitor;
    private final int START;
    private final int END;

    public T4(Data data) {
        this.data = data;
        this.monitor = Lab3.monitor;
        this.START  = 3 * data.H;
        this.END  = 4 * data.H;
    }

    @Override
    public void run() {
        try {
            System.out.println("Потік Т4 запущено");

            //Введення даних Z, MT, d
            data.fill(data.Z, 4);
            data.fill(data.MT, 4);
            data.setD(4);

            //Сигнал та очікування про ввід даних
            monitor.signalIn();
            monitor.waitIn();

            //Обрахунок ai = B_H * Z_H та a = a + ai
            int a4 = data.dot(START, END);
            monitor.addA(a4);

            //Сигнал та очікування завершення обрахунку а
            monitor.signalCalcA();
            monitor.waitCalcA();

            //Копіювання d та сортування S_H = sort( d * B_H + Z_H * MM_H)
            int d = monitor.copyD();
            data.sortS(START, END, d);

            //Сигнал про завершення потоком Т4 сортування S_H та очікування завершення сортування
            monitor.signalCalcS(4);
            monitor.waitSort();

            //Копіювання p і a та обрахунок MA_H = MX * MT_H; A_H = p * (S * MA_H) + a * Z_H
            int p = monitor.copyP();
            int a = monitor.copyA();
            data.calcA(START, END, p, a);

            //Очікування завершення обчислення
            monitor.waitOut();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік Т4 завершив роботу.");
            System.out.println("A = " + Arrays.toString(Arrays.copyOf(data.A, 10)));
        }
    }
}
