// ================= Data =================

import java.util.Arrays;

// Спільні дані, обчислювальні функції та методи синхронізації
public class Data {
    public final int P = 4;
    public final int N;
    public final int H;

    private int p;
    private int d;
    public int[] A;
    public int[] B;
    public int[] S;
    public int[] Z;
    public int[][] MM;
    public int[][] MX;
    public int[][] MT;

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
        for (int i = from; i < to; i++) {
            int temp = 0;
            for (int j = 0; j < N; j++) {
                temp += Z[j] * MM[j][i];
            }
            S[i] = d * B[i] + temp;
        }
        Arrays.sort(S, from, to);
    }

    // S[from, to) = sort(S[from, middle), S[middle, to))
    public void merge(int from, int mid, int to) {
        int[] temp = Arrays.copyOfRange(S, from, to);
        int i = 0;
        int j = mid - from;
        int k = from;
        int leftEnd = mid - from;
        int rightEnd = to - from;
        while (i < leftEnd && j < rightEnd) {
            if (temp[i] <= temp[j]) {
                S[k++] = temp[i++];
            } else {
                S[k++] = temp[j++];
            }
        }
        while (i < leftEnd) {
            S[k++] = temp[i++];
        }
        while (j < rightEnd) {
            S[k++] = temp[j++];
        }
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

    public int getP() {
        return p;
    }

    public int getD() {
        return d;
    }
}
