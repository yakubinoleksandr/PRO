// ================= ПОТІК T1 =================

public class T1 extends Thread {

    private Data data;
    private final int START;
    private final int END;

    public T1(Data data) {
        this.data = data;
        this.START = 0;
        this.END = data.H;
    }

    @Override
    public void run() {
        try {
            System.out.println("Потік Т1 запущено");

            //Введення даних B, MM, MX
            data.fill(data.B, 1);
            data.fill(data.MM, 1);
            data.fill(data.MX, 1);

            //Сигнал та очікування про ввід даних
            data.signalIn();
            data.waitIn();

            //Обрахунок ai = B_H * Z_H та a = a + ai
            int a1 = data.dot(START, END);
            data.addA(a1);

            //Сигнал та очікування завершення обрахунку а
            data.signalCalcA();
            data.waitCalcA();

            //Копіювання d та сортування S_H = sort( d * B_H + Z_H * MM_H)
            int d = data.copyD();
            data.sortS(START, END, d);

            //Очікування завершення S_H від Т2
            data.waitCalcS(2);

            //S2_H = sort(S_H, S_H) та очікування сортування від Т3
            data.merge(0, data.H, 2 * data.H);
            data.waitMerge();

            //S = sort(S2_Y, S2_R) та сигнал про завершення сортування
            data.merge(0, 2 * data.H, 4 * data.H);
            data.signalSort();

            //Копіювання p і a та обрахунок MA_H = MX * MT_H; A_H = p * (S * MA_H) + a * Z_H
            int p = data.copyP();
            int a = data.copyA();
            data.calcA(START, END, p, a);

            //Сигнал про завершення обрахунків
            data.signalOut();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік Т1 завершив роботу.");
        }
    }
}
