package hashSet;

import java.util.HashSet;
import java.util.Set;

public class HashSetEx4 {
    public static void main(String[] args) {
        Set set = new HashSet();

        set.add(new String("ABC"));
        set.add(new String("ABC"));
        set.add(new Person("David", 10));
        set.add(new Person("David", 10));

        System.out.println(set);
    }
}