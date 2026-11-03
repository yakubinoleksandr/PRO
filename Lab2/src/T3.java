// ================= ПОТІК T3 =================

import java.util.concurrent.BrokenBarrierException;

public class T3 extends Thread{
    private final Data data;
    private final int start = 2 * Data.H;
    private final int end = 3 * Data.H;

    public T3(String name, Data data) {
        super(name);
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("Потік T3 стартував.");
        try {
            // 1. Введення MC і сповіщення інших 3-х потоків через семафор
            data.initMatrix(data.MC, 3);
            data.semMC.release(3);

            // Очікування надходження даних від T1 та T4
            data.semMB.acquire();
            data.semInput4.acquire();

            // 2. Обрахунок локального min(Z) з атомарним оновленням
            data.calcLocalMinZ(start, end);

            // 3. Обрахунок проміжної смуги MX_3
            data.calcSubMatrixMX(start, end);

            // Синхронізація на бар'єрі
            data.barrierMX.await();

            // 4. Отримання копій через критичні секції
            int d3 = data.copyScalarD();
            int minZ3 = data.copyScalarMinZ();

            // 5. Обрахунок смуги MR_3
            data.calcSubMatrixMR(start, end, d3, minZ3);

            // Сповіщення потоку T2
            data.semMR.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік T3 завершився.");
            data.semMain.release();
        }
    }
}
