package thread;public class TargetThread extends Thread {    int sum;    public int getSum() {        return sum;    }    public void setSum(int sum) {        this.sum = sum;    }    public void run() {        for (long i = 0; i < 1_000_000_000; i++) {        }        try {            Thread.sleep(1500);        } catch (InterruptedException e) {            e.printStackTrace();        }        for (long i = 0; i < 1_000_000_000; i++) {        }    }    public void sum() {        for (int i = 0; i < 99; i++) {            sum += i + 1;        }    }}