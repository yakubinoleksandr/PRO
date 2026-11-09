// ================= Monitor =================

public class Monitor {

    private final Data data;
    private int inCount; // скільки потоків завершили введення
    private int calACount; // скільки потоків обчислили частину a
    private int outCount; // скільки потоків обчислили частину A_H
    private boolean[] sReady; // Ti відсортував S_H
    private boolean merged = false;
    private boolean sorted = false;

    //СР
    private int a;
    private int p;
    private int d;

    public Monitor(Data data) {
        this.data = data;
        sReady = new boolean[data.P + 1];
    }

    // Введення завершено (сигнал від Т1, Т3, Т4)
    public synchronized void signalIn() {
        p = data.getP();
        d = data.getD();

        inCount++;
        notifyAll();
    }

    public synchronized void waitIn() throws InterruptedException {
        while (inCount < data.P - 1) wait();
    }

    // Частину a обчислено (сигнал від усіх 4 потоків)
    public synchronized void signalCalcA() {
        calACount++;
        notifyAll();
    }

    public synchronized void waitCalcA() throws InterruptedException {
        while (calACount < data.P) wait();
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
        while (outCount < data.P - 1) wait();
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
