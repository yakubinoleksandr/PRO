import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    public static int N = 1000; //розмірність матриць та векторів
    public static final int P = 4; //кількість процесорів
    public static final int H = N/P; //розмір матриць та векторів у кожному потоці

    //Спільні ресурси
    public int[][] MB = new int[N][N];
    public int[][] MC = new int[N][N];
    public int[][] MM = new int[N][N];
    public int[][] MR = new int[N][N];
    public int[] Z = new int[N];
    public int d; //--критична ділянка 1

    //Проміжна матриця MX = MC * MM
    public final int[][] MX = new int[N][N];

    //Атомарна змінна для глобального мінімуму вектора Z
    public final AtomicInteger minZ = new AtomicInteger(Integer.MAX_VALUE); //--критична ділянка 2

    // ================= ПРИМІТИВИ СИНХРОНІЗАЦІЇ =================

    //Сенафори готовності введення даних
    public final Semaphore semMB = new Semaphore(0); // Сигнал від T1 про введення MB
    public final Semaphore semMC = new Semaphore(0); // Сигнал від T3 про введення MC
    public final Semaphore semInput4 = new Semaphore(0); // Сигнал від T4 про введення Z, d, MM

    // Бар'єр готовності матриці MX та знаходження кінцевого min(Z)
    public final CyclicBarrier barrierMX = new CyclicBarrier(P);

    // Семафор готовності результуючої матриці MR перед друком у T2
    public final Semaphore semMR = new Semaphore(0);

    // Семафор завершення роботи для головного методу main
    public final Semaphore semMain = new Semaphore(0);

    public Data() {}

    public Data(int val) {
        N = val;
    }

    // ================= КРИТИЧНІ СЕКЦІЇ =================

    // Захищене читання скаляра d
    public synchronized int copyScalarD() {
        return this.d;
    }

    // Захищене читання остаточного значення minZ
    public synchronized int copyScalarMinZ() {
        return this.minZ.get();
    }

    // ================= МЕТОДИ ІНІЦІАЛІЗАЦІЇ ТА ОБЧИСЛЕНЬ =================

    public void initMatrix(int[][] matrix, int value) {
        for (int i = 0; i < N; i++) {
            Arrays.fill(matrix[i], value);
        }
    }

    public void initVector(int[] vector, int value) {
        Arrays.fill(vector, value);
    }

    // Пошук мінімуму у своїй смузі Z_H з атомарним оновленням через CAS
    public void calcLocalMinZ(int start, int end) {
        int localMin = Z[0];
        for (int i = start; i < end; i++) {
            if (Z[i] < localMin) {
                localMin = Z[i];
            }
        }
        minZ.accumulateAndGet(localMin, Math::min);
    }

    // Обчислення підматриці MX_H = (MC * MM)_H
    public void calcSubMatrixMX(int start, int end) {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < N; j++) {
                int sum = 0;
                for (int k = 0; k < N; k++) {
                    sum += MC[i][k] * MM[k][j];
                }
                MX[i][j] = sum;
            }
        }
    }

    // Обчислення підматриці MR_H = (MB * MX)_H * d_i + min(Z)_i * MC_H,   i = 1...P
    public void calcSubMatrixMR(int start, int end, int d_i, int minZ_i) {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < N; j++) {
                int mb_mx_val = 0;
                for (int k = 0; k < N; k++) {
                    mb_mx_val += MB[i][k] * MX[k][j];
                }
                MR[i][j] = mb_mx_val * d_i + minZ_i * MC[i][j];
            }
        }
    }

    // Виведення результату
    public void printResult() {
        System.out.println("\n--- Результат обчислень MR (фрагмент 4x4) ---");
        for (int i = 0; i < Math.min(4, N); i++) {
            for (int j = 0; j < Math.min(4, N); j++) {
                System.out.printf("%8d ", MR[i][j]);
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------");
    }
}
