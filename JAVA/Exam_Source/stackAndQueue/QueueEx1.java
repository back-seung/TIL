package stackAndQueue;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;

public class QueueEx1 {
    static Queue q = new LinkedList();  // LinkedList는 Queue를 구현한 클래스이다. 따라서 Queue를 LinkedList로 초기화했다, Queue는 FIFO.
    static final int MAX_SIZE = 5;  // Queue에 최대 5개까지만 저장되도록 한다.

    public static void main(String[] args) {
        System.out.println("help를 입력하면 도움말을 볼 수 있습니다.");
        while (true) {
            try {
                // 화면으로부터 라인 단위로 입력받는다.
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine().trim();

                // equalsIgnoreCase는 말 그대로 대/소문자를 구별하지 않는다
                if (input.equalsIgnoreCase("q")) {
                    System.exit(0);
                } else if (input.equalsIgnoreCase("help")) {
                    System.out.println(" help - 도움말을 보여줍니다.");
                    System.out.println(" q 또는 Q - 프로그램을 종료합니다.");
                    System.out.println(" history - 최근에 입력한 명령어를 " + MAX_SIZE + "개 보여줍니다.");
                } else if (input.equalsIgnoreCase("history")) {
                    int i = 0;
                    // 입력받은 명령어를 저장하고,
                    save(input);

                    // LinkedList의 내용을 보여준다.
                    LinkedList temp = (LinkedList) q;
                    ListIterator iter = temp.listIterator();

                    while (iter.hasNext()) {
                        System.out.println(++i + "." + iter.next());
                    }
                } else {
                    save(input);
                    System.out.println(input);
                }   // if(input.equalsIgnoreCase("q")) {
            } catch (Exception e) {
                System.out.println("입력 오류입니다.");
            }
        }   // while
    }   // main

    // input을 queue에 저장한다. 사이즈가 MAX_SIZE를 넘으면
    public static void save(String input) {
        if (!"".equals(input)) {    // 공백이 아닐때만 큐에 삽입한다.
            q.offer(input);
        }
        // queue의 최대크기를 넘으면 제일 처음 입력한 것을 삭제한다.
        if (q.size() > MAX_SIZE) {
            q.remove();
        }
    }
}
