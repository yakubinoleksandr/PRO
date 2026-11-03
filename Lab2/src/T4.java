// ================= ПОТІК T4 =================

import java.util.concurrent.BrokenBarrierException;

public class T4 extends Thread{
    private final Data data;
    private final int start = 3 * Data.H;
    private final int end = 4 * Data.H;

    public T4(String name, Data data) {
        super(name);
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("Потік T4 стартував.");
        try {
            // 1. Введення Z, d, MM і сповіщення інших 3-х потоків через семафор
            data.initVector(data.Z, 4);
            data.d = 4;
            data.initMatrix(data.MM, 4);
            data.semInput4.release(3);

            // Очікування надходження даних від T1 та T3
            data.semMB.acquire();
            data.semMC.acquire();

            // 2. Обрахунок локального min(Z) з атомарним оновленням
            data.calcLocalMinZ(start, end);

            // 3. Обрахунок проміжної смуги MX_4
            data.calcSubMatrixMX(start, end);

            // Синхронізація на бар'єрі
            data.barrierMX.await();

            // 4. Отримання копій через критичні секції
            int d4 = data.copyScalarD();
            int minZ4 = data.copyScalarMinZ();

            // 5. Обрахунок смуги MR_4
            data.calcSubMatrixMR(start, end, d4, minZ4);

            // Сповіщення потоку T2
            data.semMR.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік T4 завершився.");
            data.semMain.release();
        }
    }
}
