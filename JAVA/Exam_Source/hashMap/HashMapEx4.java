package hashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapEx4 {
    public static void main(String[] args) {
        String[] data = {"A", "K", "A", "K", "D", "K", "A", "K", "K", "K", "Z", "D"};
        HashMap map = new HashMap();

        for (int i = 0; i < data.length; i++) {
            if (map.containsKey(data[i])) {
                Integer value = (Integer) map.get(data[i]);
                map.put(data[i], value.intValue() + 1);
            } else {
                map.put(data[i], new Integer(1));
            }
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int value = ((Integer) entry.getValue()).intValue();
            System.out.println(entry.getKey() + " : " + printBar('#', value) + " " + value);

        }
    }

    public static String printBar(char c, int value) {
        char[] bar = new char[value];

        for (int i = 0; i < bar.length; i++) {
            bar[i] = c;
        }

        return new String(bar);
    }
}
