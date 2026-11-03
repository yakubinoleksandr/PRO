// ================= ПОТІК T2 =================

import java.util.concurrent.BrokenBarrierException;

public class T2 extends Thread{
    private final Data data;
    private final int start = Data.H;
    private final int end = 2 * Data.H;

    public T2(String name, Data data) {
        super(name);
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("Потік T2 стартував.");
        try {
            // 1. Очікування завершення введення даних потоками T1, T3, T4
            data.semMB.acquire();
            data.semMC.acquire();
            data.semInput4.acquire();

            // 2. Обрахунок локального min(Z) з атомарним оновленням
            data.calcLocalMinZ(start, end);

            // 3. Обрахунок проміжної смуги MX_2
            data.calcSubMatrixMX(start, end);

            // Синхронізація на бар'єрі: чекаємо готовності повної матриці MX
            data.barrierMX.await();

            // 4. Отримання спільних скалярів через критичні секції
            int d2 = data.copyScalarD();
            int minZ2 = data.copyScalarMinZ();

            // 5. Обрахунок смуги MR_2
            data.calcSubMatrixMR(start, end, d2, minZ2);

            // Чекаємо сигналів завершення від потоків T1, T3, T4
            data.semMR.acquire(3);

            // 6. Виведення результату
            data.printResult();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік T2 завершився.");
            data.semMain.release();
        }
    }
}
