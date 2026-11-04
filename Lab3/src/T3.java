// ================= ПОТІК T3 =================

public class T3 extends Thread {

    private Data data;
    private final int START;
    private final int END;

    public T3(Data data) {
        this.data = data;
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
            data.signalIn();
            data.waitIn();

            //Обрахунок ai = B_H * Z_H та a = a + ai
            int a3 = data.dot(START, END);
            data.addA(a3);

            //Сигнал та очікування завершення обрахунку а
            data.signalCalcA();
            data.waitCalcA();

            //Копіювання d та сортування S_H = sort( d * B_H + Z_H * MM_H)
            int d = data.copyD();
            data.sortS(START, END, d);

            //Очікування завершення S_H від Т4
            data.waitCalcS(4);

            //S2_H = sort(S_H, S_H)
            data.merge(START, END, 4 * data.H);

            //Сигнал про завершення злиття та очікування завершення сортування
            data.signalMerge();
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
            System.out.println("Потік Т3 завершив роботу.");
        }
    }
}
