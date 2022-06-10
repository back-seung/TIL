package stackAndQueue;

import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityQueueEx {
    public static void main(String[] args) {
        // heap이라는 자료구조로 이루어져 있다. null을 저장할 수 없다.(NullPointerException)
        // 우선순위는 숫자가 작을수록 높다.
        Queue q = new PriorityQueue<>();
        q.offer(3);
        q.offer(2);
        q.offer(5);
        q.offer(4);
        q.offer(1);

        System.out.println(q);
        Object obj = null;
        while ((obj = q.poll()) != null) {
            System.out.println(obj);
        }
    }
}
