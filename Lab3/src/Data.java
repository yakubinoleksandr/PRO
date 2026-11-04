// ================= Data =================

import java.util.Arrays;

// Спільні дані, обчислювальні функції та методи синхронізації
public class Data {
    private final int P = 4;
    private final int N;
    public final int H;

    private int a;
    private int p;
    private int d;
    public int[] A;
    public int[] B;
    public int[] S;
    public int[] Z;
    public int[][] MM;
    public int[][] MX;
    public int[][] MT;

    private int inCount; // скільки потоків завершили введення
    private int calACount; // скільки потоків обчислили частину a
    private int outCount; // скільки потоків обчислили частину A_H
    private boolean[] sReady = new boolean[P + 1]; // Ti відсортував S_H
    private boolean merged = false;
    private boolean sorted = false;

    public Data(int n) {
        N = n;
        H = N / P;
        A = new int[N];
        B = new int[N];
        S = new int[N];
        Z = new int[N];
        MM = new int[N][N];
        MX = new int[N][N];
        MT = new int[N][N];
    }

    // Заповнення даних
    public void fill(int[] vec, int val) {
        Arrays.fill(vec, val);
    }
    public void fill(int[][] matrix, int val) {
        for (int i = 0; i < N; i++) {
            Arrays.fill(matrix[i], val);
        }
    }
    public void setP(int p) {
        this.p = p;
    }
    public void setD(int d) {
        this.d = d;
    }

    // ai = B_H * Z_H
    public int dot(int from, int to) {
        int s = 0;
        for (int i = from; i < to; i++) s += B[i] * Z[i];
        return s;
    }

    // S_H = sort(d * B_H + Z * MM_H)
    public void sortS(int from, int to, int d) {
        int temp = 0;
        for (int i = from; i < to; i++) {
            for (int j = 0; j < N; j++) temp += Z[j] * MM[j][i];
            S[i] = d * B[i] + temp;
        }
        Arrays.sort(S, from, to);
    }

    // S[from, to) = sort(S[from, middle), S[middle, to))
    public void merge(int from, int mid, int to) {
        int[] t = Arrays.copyOfRange(S, from, mid);
        int i = 0, j = mid, k = from;
        while (i < t.length && j < to) S[k++] = t[i] <= S[j] ? t[i++] : S[j++];
        while (i < t.length) S[k++] = t[i++];
    }

    // A_H = p * (S * (MX * MT_H)) + a * Z_H
    public void calcA(int from, int to, int p, int a) {
        for (int i = from; i < to; i++) {
            int sma = 0;
            for (int j = 0; j < N; j++) {
                int ma = 0;
                for (int k = 0; k < N; k++) ma += MX[j][k] * MT[k][i];
                sma += S[j] * ma;
            }
            A[i] = p * sma + a * Z[i];
        }
    }

    // Введення завершено (сигнал від Т1, Т3, Т4)
    public synchronized void signalIn() {
        inCount++;
        notifyAll();
    }

    public synchronized void waitIn() throws InterruptedException {
        while (inCount < P - 1) wait();
    }

    // Частину a обчислено (сигнал від усіх 4 потоків)
    public synchronized void signalCalcA() {
        calACount++;
        notifyAll();
    }

    public synchronized void waitCalcA() throws InterruptedException {
        while (calACount < P) wait();
    }

    // Частину S_H відсортовано: сигнал від Т2 та Т4, очікують Т1 (для Т2) і Т3 (для Т4)
    public synchronized void signalCalcS(int T) {
        sReady[T] = true;
        notifyAll();
    }

    public synchronized void waitCalcS(int T) throws InterruptedException {
        while (!sReady[T]) wait();
    }

    // S_2H (злиття S_H Т3 і Т4) готове: сигнал від Т3, очікує Т1
    public synchronized void signalMerge() {
        merged = true;
        notifyAll();
    }

    public synchronized void waitMerge() throws InterruptedException {
        while (!merged) wait();
    }

    // S відсортовано (сигнал від Т1, очікують Т2, Т3, Т4)
    public synchronized void signalSort() {
        sorted = true;
        notifyAll();
    }

    public synchronized void waitSort() throws InterruptedException {
        while (!sorted) wait();
    }

    // Частину A_H обчислено (сигнал від Т1, Т2, Т3, очікує Т4)
    public synchronized void signalOut() {
        outCount++;
        notifyAll();
    }

    public synchronized void waitOut() throws InterruptedException {
        while (outCount < P - 1) wait();
    }

    // КД1: a = a + a_i
    public synchronized void addA(int a) {
        this.a += a;
    }

    // КД2, КД3, КД4: копіювання d, p, a
    public synchronized int copyA() {
        return a;
    }
    public synchronized int copyD() {
        return d;
    }
    public synchronized int copyP() {
        return p;
    }
}
