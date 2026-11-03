// ================= ПОТІК T1 =================

import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread{
    private final Data data;
    private final int start = 0;
    private final int end = Data.H;

    public T1(String name, Data data) {
        super(name);
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("Потік T1 стартував.");
        try {
            // 1. Введення MB і сповіщення інших 3-х потоків через семафор
            data.initMatrix(data.MB, 1);
            data.semMB.release(3);

            // Очікування надходження даних від T3 та T4
            data.semMC.acquire();
            data.semInput4.acquire();

            // 2. Обрахунок локального min(Z) з атомарним оновленням
            data.calcLocalMinZ(start, end);

            // 3. Обрахунок проміжної смуги MX_1
            data.calcSubMatrixMX(start, end);

            // Синхронізація на бар'єрі: чекаємо повної готовності MX та minZ
            data.barrierMX.await();

            // 4. Безпечне читання спільних скалярів через критичні ділянки
            int d1 = data.copyScalarD();
            int minZ1 = data.copyScalarMinZ();

            // 5. Обрахунок власної частини результуючої матриці MR_1
            data.calcSubMatrixMR(start, end, d1, minZ1);

            // Сповіщення потоку T2 про готовність своєї частини MR
            data.semMR.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Потік T1 завершився.");
            data.semMain.release();
        }
    }
}
