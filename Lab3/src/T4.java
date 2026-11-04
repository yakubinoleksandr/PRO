// ================= ПОТІК T4 =================

import java.util.Arrays;

public class T4 extends Thread {

    private Data data;
    private final int START;
    private final int END;

    public T4(Data data) {
        this.data = data;
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
            data.signalIn();
            data.waitIn();

            //Обрахунок ai = B_H * Z_H та a = a + ai
            int a4 = data.dot(START, END);
            data.addA(a4);

            //Сигнал та очікування завершення обрахунку а
            data.signalCalcA();
            data.waitCalcA();

            //Копіювання d та сортування S_H = sort( d * B_H + Z_H * MM_H)
            int d = data.copyD();
            data.sortS(START, END, d);

            //Сигнал про завершення потоком Т4 сортування S_H та очікування завершення сортування
            data.signalCalcS(4);
            data.waitSort();

            //Копіювання p і a та обрахунок MA_H = MX * MT_H; A_H = p * (S * MA_H) + a * Z_H
            int p = data.copyP();
            int a = data.copyA();
            data.calcA(START, END, p, a);

            //Очікування завершення обчислення
            data.waitOut();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік Т4 завершив роботу.");
            System.out.println("A = " + Arrays.toString(Arrays.copyOf(data.A, 10)));
        }
    }
}
