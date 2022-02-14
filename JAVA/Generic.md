# 제네릭

> 출처 : 신용권의 이것이 자바다.

  

## 왜 제네릭을 사용해야 하는가 ?

> 제네릭이란 자바 5부터 새로 추가된 기능으로 타입을 파라미터화 해서 컴파일 시에 구체적인 타입이 결정되도록 하는 것.
>
> * 컬렉션, 람다식(함수적 인터페이스), 스트림, NIO에서 널리 사용된다.
> * API 도큐먼트에 많이 표현되기에 이해하지 못하면 API를 읽기 힘들다.



### 제네릭을 사용하므로써 얻는 이점

* 컴파일시 강한 타입 체크 : 잘못된 타입이 사용될 수 있는 문제를 컴파일 과정에서 제거할 수 있다.
* 타입 변환 제거 : 비제네릭 코드는 불필요한 타입 변환을 하기 때문에 성능에 악영향을 미친다. 아래는 이해를 돕기 위한 코드이다.

```java
// 비제네릭
List list = new ArrayList();
list.add("hello");
String str = (String) list.get(0); // 타입 변환 O

// 제네릭
List<String> list = new ArrayList<String>();
list.add("hello");
String str = list.get(0); // 타입 변환 X
```

  

## 제네릭 타입이란

> 타입을 파라미터로 가지는 클래스와 인터페이스를 말한다.

* 선언시 클래스 또는 이름 뒤에 `<>` 부호가 붙고 `<>` 사이에는 타입 파라미터가 위치한다.

```java
public class 클래스명<T> {...}
public interface 인터페이스명<T> {...}
```



### 타입 파라미터

* 일반적으로 대문자 알파벳 한 문자로 표현한다.
* 개별 코드에서는 타입 파라미터 자리에 구체적인 타입을 지정한다.



### 제네릭 타입을 사용할 경우의 효과

* 비제네릭

```java
public class Box {
  private Object object; // Object는 모든 자바 클래스의 최상위 부모 클래스임
  public void set(Object obeject) { this.objecct = object; } 
  public Object get() { return object; }
}

Box box = new Box();
box.set("hello"); // String을 Object에 자동 타입 변환
String str = (String) box.get(); // Object를 String으로 강제 타입 변환
```

> Object 타입을 사용하므로서 빈번한 타입 변환이 발생한다(성능 저하를 일으킴)



## 멀티 타입 파라미터

> 두 개 이상의 타입 파라미터를 사용해서 선언할 수 있다. 

```java
public class Product<T, M> {
  private T kind;
  private M model;
  
  public T getKind() {
    return kind;
  }
  public M getModel() {
    return model;
  }
  public void setKind(T kind) {
    this.kind = kind;
  }
  public void setModel(M model) {
    this.model = model;
  }
}
```



### 다이아몬드 연산자

> 변수를 선언할 때 구체적으로 타입을 지정하면 생성자를 호출할 때는 구체적인 타입을 명시하지 않아도 된다. 컴파일러가 알아서 해석해준다.



* List로 보는 예제

```java
List<String> list = new ArrayList<>(); // 선언부에 String을 명시하여 초기화부에는 생략할 수 있음.
```

 

## 제네릭 메소드

> 매개변수 타입과 리턴 타입으로 타입 파라미터를 갖는 메소드를 말한다.



### 선언 방법

* 리턴 타입 앞에 `<>` 기호를 추가하고 타입 파라미터를 기술한다.
* 타입 파라미터를 리턴타입과 매개변수에 사용한다.

```java
public <타입 파라미터> 리턴타입 메소드명(매개변수, ...) { ... }
```



### 호출 방법

> 제네릭 메소드는 두 가지 방법으로 호출할 수 있다. **명시적으로 구체적 타입을 지정해주는 방법**과 **매개값을 보고 구체적 타입을추정**하는 방법이 있는데 일반적으로 후자의 방법을 더 많이 사용한다.

```java
Box<Integer> box = <Integer>boxing(100); // 타입 파라미터를 명시적으로 Integer로 지정
Box<Integer> box = boxing(100); // 매개변수의 타입을 보고 Integer로 추정
```



### 제네릭 메소드 예제

* 메소드를 생성하는 Util 클래스

```java
public class Util {
    public static <T> Box<T> boxing(T t) {
        Box<T> box = new Box<>();
        box.setT(t);
        return box; 
    }
}
```

* Box

```java
public class Box<T> {
    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
```

* 메소드를 호출하는 Main

```java
public class BoxMethodExample {
    public static void main(String[] args) {
        Box<Integer> box1 = Util.boxing(100);
        int intValue = box1.getT();

        Box<String> box2 = Util.boxing("스트링링");
        String strValue = box2.getT();
    }
}
```



### compare를 활용한 제네릭 메소드 예제

> boolean타입의 정적 제네릭 메소드 compare()를 정의하여 이를 활용한다.

* 제네릭 메소드를 만드는  Util 클래스

```java
public class Util {
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        boolean keyCompare = p1.getKey().equals(p2.getKey());
        boolean valueCompare = p1.getValue().equals(p2.getValue());

        return keyCompare && valueCompare;
    }
}
```

* 제네릭 타입의 Pair 클래스

```java
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
```

* 제네릭 메소드를 호출하는 Example

```java
public class CompareMethodExample {
    public static void main(String[] args) {
        Pair<Integer, String> pair1 = new Pair<>(1, "사과"); // 제네릭 타입 구체적 명시
        Pair<Integer, String> pair2 = new Pair<>(1, "사과");

        boolean result = Util.compare(pair1, pair2); // true

        if (result) {
            System.out.println("논리적 동등 객체");
        } else {
            System.out.println("논리적 동등 객체 아님");
        }

        Pair<String, String> pair3 = new Pair<>("user1", "홍길동"); // 제네릭 타입 구체적 명시
        Pair<String, String> pair4 = new Pair<>("user2", "홍길동");
        boolean result2 = Util.compare(pair3, pair4);
        if (result2) {
            System.out.println("논리적 동등 객체");
        } else {
            System.out.println("논리적 동등 객체 아님");
        }
    }
}
```



## 제한된 타입 파라미터(<T extends 최상위타입>)

> 타입 파라미터에 저장되는 구체적 타입을 제한할 필요가 종종 있다. 숫자를 연산하는 제네릭 메소드에서는 Number타입과 그 하위 타입의 인스턴스만 가져야 하듯 말이다. 이것이 **제한된 타입 파라미터가 필요한 이유**이다.  
>
> 제한된 타입 파라미터를 선언하려면 타입 파라미터 뒤에 **extends 키워드를 붙이고 상위 타입을 명시하면 된다.** 여기서 extends는 상속이 아니라 종류의 의미로 사용된다.

```java
public <T extends 상위 타입> 리턴타입 메소드(매개변수, ...) { ... }
```

* 타입 파라미터에 저장되는 구체적인 타입은 상위 타입이거나 상위 타입의 하위 또는 구현 클래스만 가능하다.
* 주의 : 메소드의 중괄호`{}` 안에서 타입 파라미터의 변수로 사용 가능한 것은 상위 타입의 멤버(필드, 메소드)로 제한된다.



### 숫자 타입만 갖는 제네릭 메소드 예제

> doubleValue() 메소드는 Number 클래스에 정의되어 있는 메소드로 숫자를 double 타입으로 변환한다. 크면 1, 같으면 0, 작으면 -1을 리턴한다.

  

* 제한된 타입 파라미터를 가지는 Util 클래스

```java
public class Util {
    public static <T extends Number> int compare(T t1, T t2) {
        double v1 = t1.doubleValue();
        double v2 = t2.doubleValue();
        return Double.compare(v1, v2);
    }
}
```

  

* Main 클래스 

```java
public class BoundedTypeParameterExample {
    public static void main(String[] args) {
        // String str = Util.compare("1", "2"); // String은 Number 타입이 아님

        int result1 = Util.compare(10, 20); // int -> Integer autoBoxing
        System.out.println(result1);

        int result2 = Util.compare(4.5, 3.0); // double -> Double autoBoxing
        System.out.println(result2);

    }
}
```



## 와일드카드 타입( <?>, <? extends ...>, <? super ...>)

> 일반적으로 코드에서 `?` 를 와일드카드라고 부른다. 제네릭 타입을 매개변수나 리턴타입으로 사용할 때 타입 파라미터를 제한할 목적으로 사용된다.



### 와일드카드의 세가지 형태 

* `제네릭타입<?>` : Unbounded Wildcards (제한 없음)

  > 타입 파라미터를 대치하는 구체적인 타입으로 모든 클래스나 인터페이스 타입이 올 수 있다.

* `제네릭타입<? extends 상위타입>` : Upper Bounded Wildcards

  > 타입 파라미터를 대치하는 구체적인 타입으로 상위 타입이나 하위 타입만 올 수 있다.

* `제네릭타입<? super 하위타입>` : Lower Bounded Wildcards(하위 클래스)

  > 타입 파라미터를 대치하는 구체적인 타입으로 하위 타입이나 상위 타입이 올 수 있다.

  

### 세가지 형태의 대한 설명을 코드를 통해 이해하는 예제

> 제네릭 타입 Course는 과정 클래스로 과정 이름과 수강생을 저장할 수 있는 배열을 가지고 있다. 타입 파라미터 T가 적용된 곳은 수강생 타입 부분이다.

```java
public class Course<T> {
    private String name;
    private T[] students;

    public Course(String name, int capacity) {
        this.name = name;
        students = (T[]) (new Object[capacity]);
    }

    public String getName() {
        return name;
    }

    public T[] getStudents() {
        return students;
    }

    public void add(T t) {
        for (int i = 0; i < students.length; i++) {
            if (students[i] == null) {
                students[i] = t;
                break;
            }
        }
    }
}
```

* 클래스들이 다음과 같은 관계를 가지고 있다고 생각해보자.

![스크린샷 2022-02-14 17.29.44](/Users/mac/Library/Application Support/typora-user-images/스크린샷 2022-02-14 17.29.44.png)

* `Course<?>`

  > 수강생은 모든 타입(Person, Worker, Student, HighStudent)이 될 수 있다.

* `Course<? extends Student>`

  >수강생은 Student와 HighStudent만 될 수 있다.

* `Course<? super Worker>`

  > 수강생은 Worker와 Person만 될 수 있다.



## 제네릭 타입의 상속과 구현

> 제네릭 타입도 다른 타입과 마찬가지로 부모 클래스가 될 수 있다.

```java
public class ChildProduct<T, M> extends Product<T,M> { ... }
public class ChildProduct<T, M, C> extends Product<T,M> { ... }
```

* ChildProduct<T, M>은 Product<T, M>을 상속하여 정의된다. 또한 자식 제네릭 타입은 추가적으로 타입 파라미터를 가질 수 있다.



### ChildProduct 예제

```java
package generic;

public class ChildProduct<T, M, C> extends Product {
    private C company;

    public C getCompany() {
        return company;
    }

    public void setCompany(C company) {
        this.company = company;
    }
}
```

> Product를 상속 하는 ChildProduct에는 윗 부분에서 선언만 Product 클래스에 타입 파라미터 C가 추가 되었다.



* 제네릭 인터페이스를 구현한 클래스도 제네릭 타입이 된다. 인터페이스가 제네릭 타입<T>를 가지고 있다고 가정할 때 이를 구현한 클래스 또한 <T> 타입 파라미터를 가지고 있어야 한다는 뜻이다.
