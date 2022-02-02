## 컬렉션 프레임워크

* 컬렉션 : 사전적 의미로 요소를 수집해서 저장하는 것

> 배열의 문제점
>
> * 저장할 수 있는 객체 수가 배열을 생성할 때 결정  ▶ 불특정 다수의 객체를 저장하기에는 문제
> * 객체를 삭제했을 때 해당 인덱스가 비게 된다. ▶ 객체를 저장하려면 어디가 비어있는지 확인해야 된다.



### 컬렉션 프레임워크

* 객체들을 효율적으로 추가, 삭제, 검색할 수 있도록 제공되는 컬렉션 라이브러리 
* java.util에 포함
* 인터페이스를 통해서 정형화된 방법으로 다양한컬렉션 클래스를 이용할 수 있다.



### 컬렉션 프레임워크의 주요 인터페이스

1. List

   : 배열과 유사하게 인덱스로 관리, ArrayList, Vector, LinkedList가 있다.

2. Set

   : 집합과 유사하다, HashSet, TreeSet이 있다.

3. Map

   : 키와 값의 쌍으로 관리한다, HashMap, Hashtable, TreeMap, Properties가 있다.



| 인터페이스 분류 |           | 특징                                                | 구현 클래스                             |
| --------------- | --------- | --------------------------------------------------- | --------------------------------------- |
| Collection      | List 계열 | - 순서를 유지하고 저장<br />- 중복 저장 가능        | ArrayList, Vector, LinkedList           |
| Collection      | Set 계열  | - 순서를 유지하지 않고 저장<br />- 중복 저장 불가능 | HashSet, TreeSet                        |
| Map 계열        |           | - 키와 값의 쌍으로 저장<br />-키는 중복 저장 불가능 | HashMap, Hashtable, TreeMap, Properties |



## List 컬렉션

> List 컬렉션의 특징 및 주요 메서드

###  특징

* 인덱스로 관리
* 중복해서 객체 저장 가능

### 구현 클래스

* ArrayList
* Vector
* LinkedList

### 주요 메서드

| 기능      | 메서드                         | 설명                                             |
| --------- | ------------------------------ | ------------------------------------------------ |
| 객체 추가 | boolean add(E e)               | 주어진 객체를 맨 끝에 추가                       |
|           | void add(int index, E element) | 주어진 인덱스에 객체를 추가                      |
|           | set(int index, E element)      | 주어진 인덱스에 저장된 객체를 주어진 객체로 바꿈 |
| 객체 검색 | boolean contains(Object o)     | 주어진 객체가 저장되어 있는지 여부               |
|           | E get(int index)               | 주어진 인덱스에 저장된 객체를 리턴               |
|           | isEmpty()                      | 컬렉션이 비어있는지 조사                         |
|           | int size()                     | 저장되어 있는 전체 객체의 수를 리턴              |
| 객체 삭제 | void clear()                   | 저장된 모든 객체를 삭제                          |
|           | E remove(int index)            | 주어진 인덱스에 저장된 객체를 삭제               |
|           | boolean remove(Object o )      | 주어진 객체를 삭제                               |



### ArrayList

* Lsit 인터페이스의 구현 클래스로 ArrayList에 객체를 추가하면 객체가 인덱스로 관리된다. **배열은 생성될 때 크기가 정해지지만, ArrayList는 저장 용량을 초과한 객체들이 들어오면 자동적으로 저장 용량이 늘어난다.**

* 구조

```java
List<E> list = new ArrayList<E>(); // E ▶ 타입 파라미터, ()의 기본생성자 값은 10이다.
List<String> list2 = new ArrayList<String>(); // String을 저장하는 ArrayList 
```

* 모든 종류의 객체를 저장할 수는 있지만 저장시 Object로 변환하고 찾아올 때 다시 원래 타입으로 변환해야 하므로 실행 성능에 좋지 못한 영향을 미친다. ▶ 자바 5부터 제네릭을 도입하여 ArrayList개게를 생성할 때 타입 파라미터로 저장할 객체의 타입을 지정함으로써 불필요한 타입 변환을 하지 않도록 했다.

* **빈번한 객체 삭제와 삽입이 일어나는 곳**에서는 ArrayList를 사용하는 것이 바람직하지 않다. ▶ Linked List 사용을 추천한다.

* **인덱스 검색이나 맨 마지막에 객체를 추가**하는 경우에는 ArrayList가 더 좋은 성능을 발휘한다.

* ArrayLIst를 생성하고 런타임 시 필요에 의해 객체들을 추가하는 것이 일반적이다. 그러나 고정된 객체들로 구성된 List를 생성할 때도 있다. 다음과 같이 사용하는 것이 간편하다.

  `List<T> list = Arrays.asList(T...a);`

  > T타입 파라미터에 맞게 asList()의 매개값을 순차적으로 입력하거나, T[] 배열을 매개값으로 주면 된다.



#### ArrayList 활용 예제

```java
public class StringArrayListExam {

    public static void main(String[] args) {

        // String을 저장하는 ArrayList
        List<String> list = new ArrayList<String>();

        // list에 객체 추가
        list.add("java");
        list.add("programming");
        list.add("hello");

        //list에 저장된 객체의 수
        System.out.println(list.size());

        //list가 가진 객체 출력
        for (String s : list) {
            System.out.println(s);
        }

        // asList 사용하여 고정 객체 list 생성 및 출력
        List<String> fixedList = Arrays.asList("java", "programming", "hello");
        for (String s : fixedList) {
            System.out.println(s);
        }
    }
}
```



### Vector

> Vector는 ArrayList와 동일한 내부 구조를 가지고 있다. 생성을 위해 저장할 객체 타입을 타입 파라미터로 표기하고 기본 생성자를 호출한다.

```java
List<E> list = new Vector<E>();
```

* ArrayList와 다른점

  : Vector는 동기화된 메서드로 구성되어 있기 때문에 멀티 스레드가 동시에 이 메서드들을 실행할 수 없고, 하나의 스레드가 실행을 완료해야만 다른 스레드를 실행할 수 있다. 따라서 **멀티 스레드 환경에서 안전하게 객체를 추가, 삭제할 수 있다. (스레드가 안전하다)**



#### Vector 활용 객체 추가, 삭제, 검색 예제

* Vector 타입

```java
public class ExamOBJ {
    private String name;
    private String addr;
    private int age;

    ExamOBJ(String name, String addr, int age) {
        this.name = name;
        this.addr = addr;
        this.age = age;

    }
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

* Vector 활용

```java
public class VectorExam {
    public static void main(String[] args) {
        List<ExamOBJ> list = new Vector<>();

        list.add(new ExamOBJ("seunghan", "suwon", 25));
        list.add(new ExamOBJ("seungnoon", "busan", 20));
        list.add(new ExamOBJ("seungbaek", "seoul", 21));

        // 삭제 전 출력
        for (ExamOBJ obj : list) {
            System.out.println(obj.getName() + ", " + obj.getAddr() + "," + 						obj.getAge());
        }

        list.remove(1); // seungnoon 삭제
        list.remove(1); // seungbaek 삭제

        //삭제 후 출력
        for (ExamOBJ obj : list) {
            System.out.println(obj.getName() + ", " + obj.getAddr() + "," + 						obj.getAge());
        }


    }
}
```



### LinkedList

> List 구현 클래스이므로 ArrayList와 사용 방법은 똑같지만 **내부 구조가 다르다**
>
> ▶ ArrayList는 내부 배열에 객체를 저장해서 인덱스로 관리하지만, LinkedList는 인접 참조를 링크해서 체인처럼 관리한다.

* 구조![image-20220202163902952](C:\Users\seung\AppData\Roaming\Typora\typora-user-images\image-20220202163902952.png)

  > LinkedList에서 특정 인덱스의 객체를 제거하면 앞뒤 링크만 변경되고 나머지 링크는 변경되지 않는다.
  >
  > 특정 인덱스에 객체를 삽입할 때에도 마찬가지다.
  >
  > * ArrayList는 중간 인덱스의 객체를 제거하면 뒤의 객체는 인덱스가 1씩 앞으로 당겨진다. 그렇기 때문에 빈번한 삽입, 삭제가 일어나는 곳에서는 LinkedList가 좋은 성능을 발휘한다.

* 삭제시 LinkedList 구조
  ![image-20220202164244574](C:\Users\seung\AppData\Roaming\Typora\typora-user-images\image-20220202164244574.png)

* ArrayList와 성능차이

> * 끝에서 부터 추가/삭제하는 경우는 ArrayList가 빠르지만, 중간에 추가 또는 삭제하는 경우에는 앞뒤 링크 정보만 변경하면 되는 LinkedList가 더 빠르다.(**ArrayList는 뒤쪽 인덱스를 모두 1씩 증가 || 감소시키는 시간이 필요하므로**)



## Set 컬렉션

> * List 컬렉션은 저장 순서가 유지되지만, Set 컬렉션은 그렇지 않다. 또한 객체를 중복해서 저장할 수 없다. (null 포함)
> * Set 컬렉션은 수학의 집합에 비유될 수 있다. 집합은 순서와 상관없고 중복이 허용되지 않기 때문이다.



### Set 컬렉션 종류

* HashSet
* LinkedHashSet
* TreeSet

### Set 인터페이스의 공통 사용 가능 메서드

| 기능      | 메서드                     | 설명                                                         |
| --------- | -------------------------- | ------------------------------------------------------------ |
| 객체 추가 | boolean add(E e)           | 주어진 객체를 저장, 객체가 성공적으로 저장되면 true를 리턴하고 중복 객체면 false를 리턴 |
| 객체 검색 | boolean contains(Object o) | 주어진 객체가 저장되어 있는지 여부                           |
|           | boolean isEmpty()          | 컬렉션이 비어있는지 조사                                     |
|           | Iterator<E> iterator()     | 저장된 객체를 한 번씩 가져오는 반복자 리턴                   |
|           | int size()                 | 저장되어 있는 전체 객체의 수 리턴                            |
| 객체 삭제 | void clear()               | 저장된 모든 객체를 삭제                                      |
|           | boolean remove(Object o)   | 주어진 객체를 삭제                                           |

> 위 표에서 E라는 타입 파라미터는Set 인터페이스가 제네릭 타입이기 때문이다. 구체적 타입은 구현 객체를 생성할 때 결정된다 .



```java
public class StringSetExam {

    public static void main(String[] args) {

        // set 생성
        Set<String> set = ...;

        // set 삽입
        set.add("hello");
        set.add("java");
        set.add("java"); // 중복 X
        set.add("set");

        // 반복자 
        Iterator<String> iter = set.iterator();

        // 반복자를 통한 객체 검색
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
```

> * Set은 인덱스로 객체를 검색해서 가져오는 메서드가 없다. 대신 전체 객체를 대상으로 한 번씩 반복해서 가져오는 반복자를 제공한다. 반복자는 Iterator 인터페이스를 구현한 객체를 말하는데 iterator() 메서드를 호출하여 얻을 수 있다.



* Iterator 메서드 종류

| 리턴타입 | 메서드    | 설명                                                         |
| -------- | --------- | ------------------------------------------------------------ |
| boolean  | hasNext() | 가져올 객체가 있으면 true를 리턴하고 없으면 false를 리턴한다. |
| E        | next()    | 컬렉션에서 하나의 객체를 가져온다.                           |
| void     | remove()  | Set 컬렉션에서 객체를 제거한다.                              |

> remove() 메서드는 Iterator의 메서드지만 실제로는 Set 컬렉션에서 객체가 제거된다는 것을 알아두어야 한다.



* iterator 사용하지 않고 Set 검색 활용

```java
// iterator 사용하지 않고 객체 출력 (향상된 for문 사용)
for(String s : set) {
    System.out.println(s);
}
```



### HashSet

> * 객체들을 순서 없이 저장하고 동일한 객체는 중복 저장하지 않는다.
> * 동일한 객체란 꼭 같은 인스턴스를 뜻하진 않는다.
> * HashSet은 객체를 저장하기 전에 먼저 객체의 hashCode() 메서드를 호출하여 해시코드를 얻어낸다. ▶ 그리고 이전의 객체들의 해시코드와 비교한다. 동일한 해시코드가 있다면 equals()를 호출하고, true가 나오면 동일 객체로 판단 후 저장하지 않는다.



#### hashSet 활용 예제

```java
public class HashSetExam {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        set.add("java");
        set.add("JDBC");
        set.add("Servlet");
        set.add("Spring");
        set.add("java");
        set.add("iBATIS");

        int size = set.size();

        System.out.println("총 객체 수 = " + size);

        Iterator<String> iter = set.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        
        set.clear(); // 객체 전체 제거
        
        if(set.isEmpty()) {
            System.out.println("비어있는 set");
        }
    }
}
```



#### equals(), hashCode() 오버라이딩

> 인스턴스가 달라도 필드의 값이 동일하다면 동일한 객체로 간주하여 중복 저장하지 않는다.

```java
public class Member {
    public String name;
    public int age; 
    
    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Member) {
            Member member = (Member) obj;
            return member.name.equals(name) && (member.age==age);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return name.hashCode() + age;
        // String의 hashCode() 이용
    }
}
```

* 활용

```java
public class HashSetExam2 {

    public static void main(String[] args) {
        Set<Member> set = new HashSet<>();

        // 인스턴스는 다르지만 내부 데이터가 동일하므로 객체 1개만 저장
        set.add(new Member("승한", 25));
        set.add(new Member("승한", 25));

        System.out.println("총 객체의 수 : " + set.size());

    }

}
```



## Map 컬렉션

> Map 컬렉션은 키(key)와, 값(value)으로 구성된 Entry 객체를 저장하는 구조를 가지고 있다. 여기서 키와 값은 모두 객체이다. 키는 중복 저장될 수 없지만 값은 중복 저장될 수 있다. 만약 기존에 저장된 키와 동일한 키로 값을 저장하면 기존의 값은 없어지고 새로운 값으로 대체된다.



### 구조

![image-20220202172345391](C:\Users\seung\AppData\Roaming\Typora\typora-user-images\image-20220202172345391.png)

### 종류

* HashMap
* Hashtable
* LinkedHashMap
* Properties
* TreeMap



### Map 인터페이스 공통메서드

| 기능      | 메서드                         | 설명                                                         |
| --------- | ------------------------------ | ------------------------------------------------------------ |
| 객체 추가 | V put(K key, V value)          | 주어진 키로 값을 저장. 새로운 키일 경우 null을 리턴하고 동일한 키가 있을 경우 값을 대체하고 이전 값을 리턴 |
| 객체 검색 | boolean contains(Object key)   | 주어진 키가 있는지 여부                                      |
|           | boolean contains(Object key)   | 주어진 값이 있는지 여부                                      |
|           | Set<Map.Entry<K,V>> entrySet() | 키와 값의 쌍으로 구성된 모든 Map.Entry객체를 Set에 담아서 리턴 |
|           | V get(Object keyt)             | 주어진 키가 가지고 있는 값을 리턴                            |
|           | boolean isEmpty()              | 컬렉션이 비어있는지 여부                                     |
|           | Set<K> keySet()                | 모든 키를 Set 객체에 담아서 리턴                             |
|           | int size()                     | 저장된 키의 총 수를 리턴                                     |
|           | Collection<V> values()         | 저장된 모든 값을 Collection에 담아서 리턴                    |
| 객체 삭제 | void clear()                   | 모든 Map.Entry(키와 값)을 삭제                               |
|           | V remove(Object key)           | 주어진 키와 일치하는 Map.Entry를 삭제하고 값을 리턴          |

> * 위 표에서 메서드의 매개 변수 타입과 리턴 타입에 K와 V라는 타입 파라미터가 있는데 이것은 Map 인터페이스가 제네릭 타입이기 때문이다.
> * 객체 추가는 put() 메서드를 사용하고, 키로 객체를 찾아올 때에는 get() 메서드를 사용한다.



### 저장된 전체 객체를 대상으로 하나씩 객체를 얻는 방법

1. ketSet() 사용

```java
Map<K, V> map = ~;

Set<K> keySet = map.keySet();

Iterator<k> keyIter = keySet.iterator();

while(keyIter.hasNext()) {
    K key = keyIter.next();
    V value = map.get(key);
}
```

2. entrySet() 사용

```java
Set<Map.Entry<K, V>> entrySet = map.entrySet();

Iterator<Map.Entry<K, V>> entryIter = entrySet.iterator();

while(entryIter.hasNext()) {
    Map.Entry<K, V> entry = entryIter.next();
}
```





### HashMap

>  Map 인터페이스를 구현한 대표적인 Map 컬렉션, HashMap의 키로 사용할 객체는 hashCode()와 equals() 메서드를 재정의해서 동등 객체가 될 조건을 정해야 한다. 동등 객체, 즉 동일한 키가 될 조건은 hashCode()의 리턴값이 같아야하고, equals() 메서드가 true를 리턴해야 한다.

* 키 타입의 주로 String을 사용한다. (String은 문자열이 같을 경우 동등 객체가 될 수 있도록 hashCode()와 equals() 메서드가 재정의 되어 있다.)

#### 생성 방법

> 키 타입과 값 타입을 파라미터로 주고 기본 생성자를 호출하면 된다.
>
> 키 타입과 값 타입은 Primitive Type을 사용할 수 없다.

```java
// 생성 방법 basic
Map<K, V> map = new HashMap<K, V>();

// 생성 방법 String, Integer 사용
Map<String, Integer> map = new HashMap<String, Integer>();
```



#### 예제

```java
public class HashMapExam {
    public static void main(String[] args) {
        // Map 생성
        Map<String, Integer> map = new HashMap<String, Integer>();

        // 객체 삽입
        map.put("seunghan", 25);
        map.put("kildong", 26);
        map.put("bonggu", 23);
        map.put("kildong", 62); // 키값이 같기 때문에 62로 대치

        System.out.println("총 ENTRY 수 : " + map.size());

        // 키값으로 값 찾기
        System.out.println("홍길동 : " + map.get("kildong"));

        // 객체를 하나씩 처리
        Set<String> keySet = map.keySet();
        Iterator<String> keyIter = keySet.iterator();

        while (keyIter.hasNext()) {
            String key = keyIter.next();
            Integer value = map.get(key);

            System.out.println("KEY : " + key + "VALUE : " + value);
        }
        System.out.println();

    }
}
```



### Hashtable 

> Hashtable은 HashMap과 동일한 내부 구조를 가지고 있다.
>
> 키로 사용할 객체는 hashCode()와 equals() 메서드를 재정의해서 동등 객체가 될 조건을 정해야 한다.
>
> HashMap과의 차이점은 Hashtable은 동기화된 메서드로 구성되어 있기 때문에 멀티 스레드가 동시에 이 메서드들을 실행할 수는 없고, 하나의 스레드가 실행을 완료해야만 다른 스레드를 실행할 수 있다. (스레드가 안전하다) 



#### 생성방법

> HashMap과 크게 다르지 않다.

```java
// 생성 방법 basic
Map<K, V> map = new Hashtable<K, V>();

// 생성 방법 String, Integer 사용
Map<String, Integer> map = new Hashtable<String, Integer>();
```



#### 활용 예제

```
public class HashTableExam {

    public static void main(String[] args) {
        Map<String, Integer> map = new Hashtable<>();

        map.put("id", 1234);
        map.put("id2", 5678);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("ID 입력 : ");
            String id = sc.nextLine();

            System.out.println("PW 입력 : ");
            String pw = sc.nextLine();

            if (map.containsKey(id)) {
                if (map.get(id).equals(pw)) {
                    System.out.println("LOGINED!!!");
                    break;
                } else {
                    System.out.println("PW 불일치 ");
                }
            } else {
                System.out.println("ID 없음");
            }

        }
    }
}
```



### Properties

> Hashtable의 하위 클래스이다. 따라서 Hashtable의 모든 특징을 그대로 가지고 있다. 차이점은 키와 값을 다양한 타입으로 지정하지 못하고 String으로만 제한한 컬렉션이라는 점이다.
>
> * 애플리케이션의 옵션 정보, 데이터베이스 연결 정보 그리고 국제화 정보가 저장된 프로퍼티(~.properties) 파일을 읽을 때 주로 사용한다.
> * 프로퍼티 파일은 키와 값이 = 기호로 연결되어 있는 텍스트 파일로 ISO 8859-1 문자셋으로 저장된다.
> * 직접 표현할 수 없는 한글은 Unicode로 저장된다.



#### DB 연결 정보 예제

* properties 파일

```properties
driver=oracle.jdbc.OracleDriver
url=jdbc:oracle:thin:@localhost:1521:orcl
username=root
password=1234
```

* 파일을 읽어오는 코드

```java
String path = 클래스.class.getResource("database.properties").getPath();
path = URLDecoder.decode(path, "UTF-8");

Properties properties = new Properties();
properties.load(new FileReader(path));

String driver = properties.getProperty("driver");
String url = properties.getProperty("url");
String username = properties.getProperty("username");
String password = properties.getProperty("password");

System.out.println("드라이버 : " + driver);
System.out.println("URL : " + url);
System.out.println("이름 : " + username);
System.out.println("비밀번호 : " + password);
```



#### properties에서 해당 키의 값을 읽는 방법

> Map 컬렉션이므로 get() 메서드를 사용해도 되지만, 값을 Object 타입으로 리턴하므로 강제 타입 변환해서 String을 얻기 때문에 getProperty() 메서드를 사용한다.

```java
String value = properties.getProperty("key");
```



## 검색 기능을 강화시킨 컬렉션

> 컬렉션 프레임워크는 TreeSet과 TreeMap을 제공하고 있다. TreeSet은 Set 컬렉션이고 TreeMap은 Map 컬렉션이다. 이 컬렉션들은 **이진트리**를 이용해서 계층적 구조를 가지며 객체를 저장한다.



### 이진 트리 구조

> **여러개의 노드가 트리 형태로 연결된 구조,** 루트 노드라고 불리는 하나의 노드에서부터 시작해 각 노드에 최대 2개의 노드를 연결할 수 있는 구조이다. 부모 노드와 자식 노드로 구성되어 있다.