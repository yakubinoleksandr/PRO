// ================= ПОТІК T2 =================

public class T2 extends Thread {

    private Data data;
    private final int START;
    private final int END;

    public T2(Data data) {
        this.data = data;
        this.START = data.H;
        this.END = 2 * data.H;
    }

    @Override
    public void run() {
        try {
            System.out.println("Потік Т2 запущено");

            //Очікування про ввід даних
            data.waitIn();

            //Обрахунок ai = B_H * Z_H та a = a + ai
            int a2 = data.dot(START, END);
            data.addA(a2);

            //Сигнал та очікування завершення обрахунку а
            data.signalCalcA();
            data.waitCalcA();

            //Копіювання d та сортування S_H = sort( d * B_H + Z_H * MM_H)
            int d = data.copyD();
            data.sortS(START, END, d);

            //Сигнал про завершення потоком Т2 сортування S_H та очікування завершення сортування
            data.signalCalcS(2);
            data.waitSort();

            //Копіювання p і a та обрахунок MA_H = MX * MT_H; A_H = p * (S * MA_H) + a * Z_H
            int p = data.copyP();
            int a = data.copyA();
            data.calcA(START, END, p, a);

            //Сигнал про завершення обрахунків
            data.signalOut();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік Т2 завершив роботу.");
        }
    }
}
