package treeSet;

import java.util.TreeSet;

public class TreeSetEx2 {
    public static void main(String[] args) {
        TreeSet set = new TreeSet();
        int[] arr = {80, 95, 50, 35, 45, 65, 10, 100};

        for (int i = 0; i < arr.length; i++) {
            set.add(new Integer(arr[i]));
        }

        System.out.println("50보다 작은 값 : " + set.headSet(50));
        System.out.println("50보다 큰 값 : " + set.tailSet(50));

    }
}
