// ================= ПОТІК T3 =================

public class T3 extends Thread {

    private final Data data;
    private final Monitor monitor;
    private final int START;
    private final int END;

    public T3(Data data) {
        this.data = data;
        this.monitor = Lab3.monitor;
        this.START  = 2 * data.H;
        this.END  = 3 * data.H;
    }

    @Override
    public void run() {
        try {
            System.out.println("Потік Т3 запущено");

            //Введення даних p
            data.setP(3);

            //Сигнал та очікування про ввід даних
            monitor.signalIn();
            monitor.waitIn();

            //Обрахунок ai = B_H * Z_H та a = a + ai
            int a3 = data.dot(START, END);
            monitor.addA(a3);

            //Сигнал та очікування завершення обрахунку а
            monitor.signalCalcA();
            monitor.waitCalcA();

            //Копіювання d та сортування S_H = sort( d * B_H + Z_H * MM_H)
            int d = monitor.copyD();
            data.sortS(START, END, d);

            //Очікування завершення S_H від Т4
            monitor.waitCalcS(4);

            //S2_H = sort(S_H, S_H)
            data.merge(START, END, 4 * data.H);

            //Сигнал про завершення злиття та очікування завершення сортування
            monitor.signalMerge();
            monitor.waitSort();

            //Копіювання p і a та обрахунок MA_H = MX * MT_H; A_H = p * (S * MA_H) + a * Z_H
            int p = monitor.copyP();
            int a = monitor.copyA();
            data.calcA(START, END, p, a);

            //Сигнал про завершення обрахунків
            monitor.signalOut();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік Т3 завершив роботу.");
        }
    }
}
