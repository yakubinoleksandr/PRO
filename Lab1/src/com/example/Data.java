
//==================================
//Головний клас статичних методів та функцій
//==================================

package com.example;

public class Data {

    private static int N; //Розмір векторів та матриць

    Data(int N) {
        Data.N = N;
    }

    //Заповнення векторів
    public static int[] fillVector(int val) {
        int[] res = new int[N];
        for (int i = 0; i < N; i++) {
            res[i] = val;
        }
        return res;
    }

    //Заповнення матриць
    public static int[][] fillMatrix(int val) {
        int[][] res = new int[N][N];
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++) {
                res[i][j] = val;
            }
        }
        return res;
    }

    //Множення векторів
    public static int multiplyVectors(int[] V1, int[] V2){
        int sum = 0;
        for (int i = 0; i < N; i++) {
            sum += V1[i] * V2[i];
        }
        return sum;
    }

    //множення матриць
    public static int[] [] multiplyMatrix(int[][] M1, int[][] M2) {
        int[][] res = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    res[i][j] += M1[i][k] * M2[k][j];
                }
            }
        }
        return res;
    }

    //множення матриці на вектор
    public static int[] multiplyMatrixVector(int[][] M, int[] V) {
        int[] res = new int[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                res[i] += M[i][j] * V[j];
            }
        }
        return res;
    }

    //множення скаляра на матрицю
    public static int[][] multiplyScalarMatrix(int val, int[][] M) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                M[i][j] *= val;
            }
        }
        return M;
    }

    //знаходження мінімального елемента в матриці
    public static int minMatrix(int[][] M) {
        int min = M[0][0];
        for (int[] row : M) {
            for (int val : row) {
                if (val < min) min = val;
            }
        }
        return min;
    }

    //транспонування матриці
    public static int[][] transpose(int[][] M) {
        int[][] res = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                res[j][i] = M[i][j];
            }
        }
        return res;
    }

    //F1: MD = (B * C) * (MA * ME)
    public static int[][] func1(int[] B, int[] C, int[][] MA, int[][] ME) {
        int s = multiplyVectors(B, C);
        int[][] prodM= multiplyMatrix(MA, ME);
        return multiplyScalarMatrix(s, prodM);
    }

    //F2: MF = MIN(MH) * MK * ML
    public static int[][] func2(int[][] MH, int[][] MK, int[][] ML) {
        int min = minMatrix(MH);
        int[][] prodM = multiplyScalarMatrix(min, MK);
        return multiplyMatrix(prodM, ML);
    }

    //F3: O = TRANS(MP * MR) * V
    public static int[] func3(int[][] MP, int[][] MR, int[] V) {
        int[][] prodM = multiplyMatrix(MP, MR);
        int[][] trans = transpose(prodM);
        return multiplyMatrixVector(trans, V);
    }

    public static int getN() {
        return N;
    }
}
