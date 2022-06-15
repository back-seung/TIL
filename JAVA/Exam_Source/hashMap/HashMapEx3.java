package hashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HashMapEx3 {
    // TODO 전화번호부로 사용할 HashMap
    static HashMap phoneBook = new HashMap();

    public static void main(String[] args) {
        addPhoneNo("친구", "이자바", "010-1111-1111");
        addPhoneNo("친구", "김자바", "010-2222-2222");
        addPhoneNo("친구", "김자바", "010-3333-3333");
        addPhoneNo("친구", "박자바", "010-4444-4444");
        addPhoneNo("회사", "이대리", "010-5555-5555");
        addPhoneNo("회사", "박과장", "010-6666-6666");
        addPhoneNo("회사", "김차장", "010-7777-7777");
        addPhoneNo("세탁", "010-8888-8888");

        printList();
    }
    // 그룹에 전화번호를 추가하는 메서드
    static void addPhoneNo(String groupName, String name, String tel) {
        addGroup(groupName);
        // 그룹명의 값(value)는 HashMap이다. 해당 그룹명에 전화번호를 추가한다.
        HashMap group = (HashMap) phoneBook.get(groupName);
        group.put(tel, name);
    }

    // 매개변수를 두개 주었을 때, 그룹명은 기타임. (메서드 오버라이딩)
    static void addPhoneNo(String name, String tel) {
        addPhoneNo("기타", name, tel);
    }
    // 그룹을 추가하는 메서드
    static void addGroup(String groupName) {
        if (!phoneBook.containsKey(groupName)) {
            phoneBook.put(groupName, new HashMap());
        }
    }

    static void printList() {
        Set set = phoneBook.entrySet();
        Iterator it = set.iterator();

        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();

            Set subSet = ((HashMap) e.getValue()).entrySet();
            Iterator subIt = subSet.iterator();

            System.out.println(" * " + e.getKey() + "[" + subSet.size() + "]");

            while (subIt.hasNext()) {
                Map.Entry subE = (Map.Entry) subIt.next();
                String telNo = (String) subE.getKey();
                String name = String.valueOf(subE.getValue());
                System.out.println(name + " " + telNo);
            }
            System.out.println();
        }
    }
}
