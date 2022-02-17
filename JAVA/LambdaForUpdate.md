###  minBy(), maxBy() 정적 메소드

> BinaryOperator<T> 함수적 인터페이스는 minBy() 와 maxBy() 정적 메소드를 제공한다.  
>
> 이 두 메소드는 Comparator를 이용해서 최대 T와 최소 T를 얻는 BinaryOperator<T>를 리턴한다

| 리턴타입          | 정적 메소드                             |
| ----------------- | --------------------------------------- |
| BinaryOperator<T> | minBy(Comparator<? super T> comparator) |
| BinaryOperator<T> | maxBy(Comparator<? super T> comparator) |

> Comparator<T>는 o1과 o2를 비교해서 o1이 작으면 음수, 둘이 같다면 0, o1이 크다면 양수를 리턴하는 compare() 메소드가 선언되어 있다.

```java
@FunctionalInterface
public interface Comparator<T> {
    public int compare(T o1, T o2);
}

(o1, o2) -> { ...; return int값; } // 람다식으로 하면 다음과 같이 적을 수 있다.
(o1, o2) -> Integer.compare(o1, o2);
```

* 예제

```java
import java.util.function.BinaryOperator;

public class OperatorMinByMaxByExample { 
	public static void main(String[] args) {
        BinaryOperator<Fruit> binaryOperator;
        Fruit fruit;
        
        binaryOperator = BinaryOperator.minBy( (f1,f2) -> Integer.compare(f1.price, f2.price) );
        fruit = binaryOperator.apply(new Fruit("딸기", 6000), new Fruit("수박", 10000));
        System.out.println(fruit.name);
        
        binaryOperator = BinaryOperator.maxBy( (f1,f2) -> Integer.compare(f1.price, f2.price) );
        fruit = binaryOperator.apply(new Fruit("딸기", 6000), new Fruit("수박", 10000));
        System.out.println(fruit.name);
    }
}
```



## 메소드 참조

> **메소드를 참조해서 매개 변수의 정보 및 리턴 타입을 알아내어, 람다식에서 불필요한 매개 변수를 제거하는 것이 목적**이다. 람다식은 종종 기존 메소드를 단순히 호출만 하는 경우가 많다. 예를 들어 두 개의 값을 받아 큰 수를 리턴하는 Math 클래스의 max() 메소드를 호출하는 람다식은 다음과 같다.

```java
(left, right) -> Math.max(left, right); // 매개값으로 전달하는 역할만 한다.

Math :: max; // 메소드 참조 사용시 위 코드 작성법
```

> 메소드 참조는 정적 또는 인ㅅ턴스 메소드를 참조할 수 있고, 생성자 참조도 가능하다.

  

### 정적 메소드와 인스턴스 메소드 참조

```java
// 정적 메소드를 참조할 경우에는 먼저 객체를 생성한 다음 참조 변수 뒤에 `::`기호를 붙히고 인스턴스 메소드 이름을 기술하면 된다.
클래스 :: 메소드
    
// 인스턴스 메소드 경우 먼저 객체를 생성한 다음 참조 변수 뒤에 `::` 기호를 붙혀서 사용한다.
참조 변수 :: 메소드
```

* Calculator를 사용한 예제

```java
public class Calculator {
    public static int staticMethod(int x, int y) {
        return x + y;
    }
    
    public int instanceMethod(int x, int y) {
        return x + y;
    }
}
```

```java
public class MethodReferenceExample {
    public static void main(String[] args) {
        IntBinaryOperator operator;
        
        // 정적 메소드 참조
        operator = (x, y) -> Calculator.staticMethod(x,y);
        System.out.println("결과 1 : " + operator.applyAsInt(1, 2));
        
        operator = Calculator :: staticMethod;
        System.out.println("결과 2 : " + operator.applyAsInt(3, 4));
        
        // 인스턴스 메소드 참조
        Calculator obj = new Calculator();
        operator = (x, y) -> obj.instanceMethod(x, y);
        System.out.println("결과 3 : " + obj.applyAsInt(5, 6));
        
        Calculator obj = new Calculator();
        operator = obj :: instanceMethod;
        System.out.println("결과 3 : " + obj.applyAsInt(7, 8));
    }
}
```



### 매개 변수의 메소드 참조

> 메소드는 람다식 외부의 클래스 멤버일 수도 있고, 람다식에서 제공되는 매개 변수의 멤버일 수도 있다. 람다식에서 제공되는 a 매개 변수의 메소드를 호출해서 b 매개 변수를 매개값으로 사용하는 경우도 있다.

```java
(a, b) -> { a.instanceMethod(b); }
```

> 이것을 메소드 참조로 표현하면 `클래스 :: instanceMethod`로 작성할 수 있다.

* 동일한 문자열인지 확인하는 예제

```java
import java.util.function.ToIntBiFunction;

public class ArgumentMethodReferencesExample {
    public static void main(String[] args) {
        ToIntBiFunction<String, String> function;
        
        function = (a, b) -> a.compareToIgnoreCase(b); 
        print(function.applyAsInt("Java8", "JAVA8"));
 		
        function = String :: compareToIgnoreCase;
        print(function.applyAsInt("Java8", "JAVA8"));
    }
    
    public void print(int order) {
        if(order < 0) {
            System.out.println("사전순으로 먼저 옵니다");
        } else if (order == 0 ) {
            System.out.println("동일한 문자열입니다.");
        } else {
            System.out.println("사전순으로 나중에 옵니다");
        }
    }
}
```



### 생성자  참조

> 메소드 참조는 생성자 참조도 포함한다.  
>
> 그리고 생성자 참조는 **객체 생성을 의미한다**. 단순히 객체를 생성하고 리턴하도록 구성된 람다식은 생서자 참조로 대치할 수 있다.

```java
(a, b) -> { return new 클래스(a,b); } // 생성자 참조 표현 전,
클래스 :: new // 생성자 참조 표현 후
```

> 생성자가 오버로딩 되어 있을 경우 컴파일러는 함수적 인터페이스의 추상 메소드와 동일한 매개 변수 타입과 개수를 가지고 있는 생성자를 찾아 실행한다. 맞는 생성자가 없으면 **컴파일 오류가 발생한다.**

* 생성자 참조를 활용하여 두 가지 방법으로 Member 객체를 생성하는 예제
  1. Function<String, Member>의 Member apply(String) 메소드 이용하여 생성
  2. BiFunction<String, String, Member>의 Member apply(String, String) 메소드를 이용하여 생성

```java
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConstructorReferencceExample {
    public static void main(String[] args) {
        Function<String, Member> function1 = Member :: new;
        Member member1 = function1.apply("angel");
        
        BiFunction<String, String, Member> function2 = Member :: new;
        Member member2 = function2.apply("신천사", "angel");
    }
}
```

* 생성자 오버로딩

```java
public class Member {
    private String name;
    private String id;
    
    public Member() {
        System.out.println("Member() 실행");
    }
    
    public Member(String id) {
        System.out.println("Member(String name) 실행");
        this.id = id;
    }
    
    public Member(String name, String id) {
        System.out.println("Member(String name, String id) 실행");
        this.name = name;
        this.id = id;
    }
    
    public String getId() { return id; }
}
```

