package collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsEx1 {
    public static void main(String[] args) {
        List list = Collections.checkedList(new ArrayList<>(), String.class);

        list.add(1);

        System.out.println(list);
    }
}
