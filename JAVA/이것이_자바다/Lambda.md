## 람다식이란

> `y = f(x)` 형태의 함수적 프로그래밍 기법이며 람다 계산법에서 사용된 식을 프로그래밍 언어에 접목시킨 것으로 자바 8부터 람다식을 언어차원에서 제공하게 되었다. 람다식은 익명 함수를 생성하기 위한 식이다.  
>
> `(타입 매개변수, ...) -> { 실행문 }`
>
> 데이터를 매개값으로 전달하고 결과를 받는 코드들로 구성되어 있다.  



### 객체지향 보다 효율적인 경우

* 대용량 데이터의 처리시에 유리하다.

  > 데이터 포장 객체를 생성하고 처리하는 것보다, 바로 처리하는 것이 속도에 유리하다.  
  >
  > 멀티 코어 CPU에서 데이터를 병렬 처리하고 취합할 때 객체보다 함수가 유리하다.

* 이벤트 지향 프로그래밍에 적합

  > 반복적인 이벤트 처리는 핸들러 객체보다는 핸들러 함수가 적합하다.



### 람다식을 수용한 이유

* 코드가 매우 간결해진다
* 컬렉션 요소 ( 대용량 데이터 )를 필터링 또는 매핑하여 쉽게 집계할 수 있다.



### 자바는 람다식을 함수적 인터페이스의 익명 구현 객체로 취급

> 어떤 인터페이스를 구현할지는 대입되는 인터페이스에 달려있다.

```java
// 람다식 -> 매개변수를 가진 코드 블록 -> 익명 구현 객체

Runnable runnable = () -> { ... }; // 아래 코드를 람다식으로 표현

Runnable runnable = new Runnable() { // 
  public void run() { ... }
};
```



## 람다식 기본 문법

> 함수적 스타일의 람다식을 작성하는 방법은 다음과 같다

```java
(타입 매개변수, ...) { 실행문; ... }
```

* 타입 매개변수는 오른쪽 중괄호 블록을 실행하기 위해 필요한 값을 제공하는 역할을 한다.  



```java
(int a) -> { System.out.println(a); }

(a) -> { System.out.println(a); }

a -> System.out.println(a)
```

* 매개 변수 타입은 런타임 시에 대입되는 값에 따라 자동으로 인식될 수 있기 때문에 **람다식에서는 매개 변수의 타입을 일반적으로 언급하지 않는다.**

* 하나의 매개 변수만 있다면 괄호 ()를 생략할 수 있고, 하나의 실행문만 있다면 {}도 생략할 수 있다.

* 만약 매개 변수가 없다면 람다식에서 매개 변수 자리가 없어지므로 빈 괄호()를 반드시 사용해야 한다.  



```java
(x, y) -> { return x + y; };
(x, y) -> x + y;
```

* 중괄호에 리턴문만 있을 경우(첫 줄 코드) 람다식에서는 위와 같이 적는게 일반적이다(밑 줄 코드)



## 타겟 타입과 함수적 인터페이스

> 자바는 메소드를 단독으로 실행할 수 없고, 항상 클래스의 구성 멤버로 선언하기 때문에 람다식은  단순히 메소드를 선언하는 것이 아니라, 메소드를 가지는 객체를 생성해 낸다. 

```java
인터페이스 변수 = 람다식;
```

* 람다식은 인터페이스의 익명 구현 객체를 생성한다. 인터페이스는 직접 객체화 할 수 없기 때문에 구현 클래스가 중요한데, 람다식은 익명 구현 클래스를 생성하고 객체화한다.
* 대입될 인터페이스의 종류에 따라 작성 방법이 달라지기 때문에 대입도리 인터페이스를 람다식의  **타겟 타입**이라고 한다.



### 함수적 인터페이스(@FuncionalInterface)

> 모든 인터페이스를 람다식의 타겟 타입으로 사용할 수는 없다. 두 개 이상의 추상 메소드가 선언된 인터페이스는 람다식을 이용해서 구현 객체를 생성할 수 없다.  
>
> 하나의 추상 메소드가 선언된 인터페이스만이 람다식의 타겟 타입이 될 수 있는데, 이러한 인터페이스를 함수적 인터페이스라고 한다. 함수적 인터페이스를 작성할 때 두 개 이상의 추상 메소드가 선언되지 않도록 컴파일러가 체킹해주는 기능이 있는데 `@FunctionalInterface` 어노테이션을 붙이면 된다.  



* `@FuntionalInterface`는 선택사항이다.
* 타겟 타입인 함수적 인터페이스가 가지고 있는 추상 메소드의 선언 형태에 따라 작성 방법이 달라진다.



### 매개 변수와 리턴값이 없는 람다식

```java
@FucntionalInterface
public interface MyFunctionalInterface {
  public void method();
}
```

```java
MyFunctionalInterface f1 = () -> { ... };

fi.method();
```



### 매개 변수가 있는 람다식

```java
@FucntionalInterface
public interface MyFunctionalInterface {
  public void method(int x);
}
```

```java
MyFunctionalInterface f1 = (x) -> { ... }; // 또는 x -> { ... };

f1.method(2);
```



### 리턴값이 있는 람다식

```java
@FucntionalInterface
public interface MyFunctionalInterface {
  public int method(int x, int y);
}

f1.method(2, 5);
```

```java
MyFunctionalInterface f1 = (x, y) -> { ...; return 값; }
```

> 만약 중괄호 `{}`에 return문만 있고, return문 뒤에 연산식이나 메소드 호출이 오는 경우라면 다음과 같이 작성할 수 있다.

```java
MyFunctionalInterface f1 = (x, y) -> x + y;
f1.method(2, 5);

MyFunctionalInterface f1 = (x, y) -> sum(x, y);
```



## 클래스 멤버와 로컬 변수 사용

> 람다식의 실행 블록에는 클래스의 멤버(필드, 메소드) 및 로컬 변수를 사용할 수 있따. 클래스의 멤버는 제약 사항 없이 사용이 가능하지만, 로컬 변수는 제약이 따른다.



### 클래스의 멤버 사용 

> 람다식 실행 블록에 `this` 키워드를 사용할 때에는 주의가 필요하다. 일반적으로 익명 객체 내부에서  this는 익명 객체의 참조이지만, 람다식에서 this는 내부적으로 생성되는 익명 객체의 참조가 아니라 람다식을 실행한 객체의 참조이다.



* 위를 코드로 설명하는 예제

```java
public interface MyFunctionalInterface {
  public void method();
}
```

> 함수형 인터페이스 선언



```java
public class UsingThis {
  public int outterField = 10;
  
  class Inner {
    int innerField = 20;
    
    void method() {
      // 람다식
      MyfunctionalInterface f1 = () -> {
        System.out.println("OutterField : " + outterField);
        System.out.println("OutterField : " + UsingThis.this.outterField + "\n");
        
        System.out.println("InnerField : " + innerField);
        System.out.println("OutterField : " + this.innerFIeld + "\n");
      };
      fi.method();
    }
  }
}
```

> this 사용



```java
public class UsingThisExample {
  public static void main(String[] args) {
    UsingThis usingThis = new UsingThis();
    UsingThis.Inner inner = new usingThis.new Inner();
    inner.method();
  }
}
```

> 실행 클래스



### 로컬 변수 사용

> 람다식은 메소드 내부에서 주로 작성되기 때문에 로컬 익명 구현 객체를 생성시킨다고 봐야 한다.  
>
> 메소드의 매개 변수 또는 로컬 변수를 사용하면 이 두 변수는 final 특성을 가져야 한다. 매개 변수 또는 로컬 변수를 람다식에서 읽는 것은 허용되지만, 람다식 내부 또는 외부에서 변경할 수 없다.



```java
public interface MyFunctionalInterface {
  public void method();
}
```

> 함수적 인터페이스



```java
public class UsingLocalVariable {
  void method(int arg) { // arg는 final 특성
    int localVar = 10; // localVar는 final 특성 
    
    // arg = 20; 수정 불가
    // localVar = 20; 수정 불가
    
    MyfunctionalInterface f1 = () -> {
      System.out.println("arg : " + arg);
      System.out.println("localvar : " + localVar);
    };
    f1.method();
  }
}
```

> final 특성을 가지는 로컬 변수



```java
public class UsingLoclaVariableExample { 
  public static void main(String[] args) {
    UsingLocalVariable usingLocalVariable = new UsingLocalVariable();
    usingLocalVariable.method(20);
  }
}
```

> 실행



## 표준  API의 함수적 인터페이스

> 자바에서 제공되는  표준 API에서 한 개의 추상 메소드를 가지는 인터페이스들은 모두 람다식으로 표현 가능하다.
>
> 자바 8부터는 빈번하게 사용되는 함수적 인터페이스는 `java.util.function` 표준 API 패키지로 제공된다. 패키지에서 제공하는 목적은 메소드 또는 생성자의 매개 타입으로 사용되어 람다식을 대입할 수 있도록 하기 위해서이다.  
>
> 자바 8부터 추가되거나 변경된 API에서 이 함수적 인터페이스들을 매개타입으로 많이 사용하며 사용자 개발 메소드에도 함수적 인터페이스를 사용할 수 있다.



### 함수적 인터페이스 구분

> 구분 기준은 인터페이스에 선언된 추상 메소드의 매개값과 리턴값의 유무이다.

| 종류      | 추상 메소드 특징                                             |                          |
| --------- | ------------------------------------------------------------ | ------------------------ |
| Consumer  | 매개값은 있고, 리턴값은 없음                                 | 매개값▶️Consumer          |
| Supplier  | 매개값은 없고, 리턴값은 있음                                 | Sullplier▶️리턴값         |
| Function  | 매개값도 있고, 리턴값도 있음<br />주로 매개값을 리턴값으로 매핑 | 매개값▶️Function▶️리턴값   |
| Operator  | 매개값고 있고, 리턴값도 있음<br />주로 매개값을 연산하고 결과를 리턴 | 매개값▶️Operator▶️리턴값   |
| Predicate | 매개값은 있고, 리턴 타입은 boolean<br />매개값을 조사해서 true/false를 리턴 | 매개값▶️Predicate▶️boolean |

​	

### Consumer 함수적 인터페이스

> 리턴값이 없는 accept() 메소드를 가지고 있다. 이 메소드는 단지 매개값을 소비하는 역할만 한다. 사용만하고 리턴은 없다.

*  매개 변수의 타입과 수에 따라서 아래와 같은 Consumer들이 있다.

| 인터페이스명         | 추상 메소드                    | 설명                         |
| -------------------- | ------------------------------ | ---------------------------- |
| Consumer<T>          | void accept(T t)               | 객체 T를 받아 소비           |
| BiConsumer<T, U>     | void accept(T t, U u)          | 객체 T, U를 받아 소비        |
| DoubleConsumer       | void accept(double value)      | double값을 받아 소비         |
| IntConsumer          | void accept(int value)         | int값을 받아 소비            |
| LongConsumer         | void accept(long value)        | long값을 받아 소비           |
| ObjDoubleConsumer<T> | void accept(T t, double value) | T객체와 double값을 받아 소비 |
| ObjIntConsumer<T>    | void accept(T t, int value)    | T객체와 int값을 받아 소비    |
| ObjLongConsumer<T>   | void accept(T t, long value)   | T객체와 long값을 받아 소비   |

  

* Consumer<T> 인터페이스를 타겟 타입으로 하는 람다식은 다음과 같이 작성할 수 있다. accept() 메소드는 매개값으로 T 객체 하나를 가지므로 람다식도 한 개의 매개 변수를 사용한다.
* BiConsumer<T, U>를 타겟 타입으로 하는 람다식은 두 개의 객체를 가지므로 람다식도 두 개의 매개 변수를 사용한다.
* ObjXXXConsumer를 타겟 타입으로 하는 람다식은 T 객체와 XXX의 타입 두 개를 사용한다.

```java
Consumer<String> consumer = t -> { t를 소비하는 실행문 };  // Consumer<T>
BiConsumer<String, String> consumer = (t, u) -> { t와 u를 소비하는 실행문 }; // BiConsumer<T, U>
ObjIntConsumer<String> consumer = (t, i) -> { t와 i를 소비하는 실행문 };
```

  

### Supplier 함수적 인터페이스

> 매개 변수가 없고 리턴값은 있는 getXXX() 메소드를 가지고 있다. 실행 후 호출한 곳으로 데이터를 리턴한다.

| 인터페이스명   | 추상 메소드            | 설명              |
| -------------- | ---------------------- | ----------------- |
| Supplier<T>    | T get()                | T 객체를 리턴     |
| BooleanSuplier | boolean getAsBoolean() | boolean 값을 리턴 |
| DoubleSupplier | double getAsDouble()   | double 값을 리턴  |
| IntSupplier    | int getAsInt()         | int 값을 리턴     |
| LongSupplier   | long getAsLong         | long 값을 리턴    |

  

* Supplier와 XXXSupplier는 다음과 같이 람다식으로 작성할 수 있다.

```java
Supplier<String> supplier = () -> { return "문자열"; }
IntSupplier supplier = () -> {...; return int값; }
```



### Function 함수적 인터페이스

> 매개값과 리턴값이 있는 applyXXX() 메소드를 가지고 있다. 매개값을 리턴값으로 매핑하는 역할을 한다.

| 인터페이스명            | 추상 메소드                      | 설명                       |
| ----------------------- | -------------------------------- | -------------------------- |
| Function<T,R>           | R apply(T t)                     | 객체 T를 객체  R로 매핑    |
| BiFunction<T,U,R>       | R apply(T t, U u)                | 객체 T와 U를 객체 R로 매핑 |
| DoubleFunction<R>       | R apply(double value)            | double을 객체 R로 매핑     |
| IntFunction<R>          | R apply(int value)               | int를 객체 R로 매핑        |
| IntToDoubleFunction     | double applyAsDouble(int value)  | int를 double로 매핑        |
| intToLongFunction       | long applyAsLong(int value)      | int를 long으로 매핑        |
| LongToDoubleFunction    | double applyAsDouble(long value) | long을 double로 매핑       |
| LongToIntFunction       | int applyAsInt(long value)       | long을 int로 매핑          |
| ToDoubleBiFiontion<T,U> | double applyAsDouble(T t, U u)   | T와 U를 double로 매핑      |
| ToDoubleFunction<T>     | double applyAsDouble(T t)        | T를 double로 매핑          |
| ToIntBiFunction<T, U>   | int applyAsInt(T t, U u)         | T와 U를  int로 매핑        |
| ToIntFunction<T>        | int applyAsInt(T t)              | T를 int로 매핑             |
| ToLongBiFunction<T, U>  | long applyAsLong(T t, U u)       | T와 U를 long으로 매핑      |
| ToLongFunction<T>       | long applyAsLong(T t)            | T를 long으로 매핑          |

  

* Funciton<T,R>을 타겟 타입으로 하는 람다식은 매개값으로 T를 가지고 리턴값으로는 R을 가진다.
* ToIntFunction<T>을 타겟 타입으로 하는 람다식은 t 객체를 int로 리턴한다.

```java
Function<Student, String> function = t -> { return t.getName(); } // Fucntion<T,R>
ToIntFunction<Student> toIntFunction = t -> { return t.getScore(); }
```



### Operator 함수적 인터페이스

> Function과 동일하게 매개변수와 리턴값이 있는 apply() 메소드를 가지고 있다. 하지만 매개값을 리턴값으로 매팡하는 역할보다는 매개값을 이용해 연산을 수행한다음 동일한 타입으로 리턴값을 제공하는 역할을 한다. 

| 인터페이스명         | 추상메소드                           | 설명                     |
| -------------------- | ------------------------------------ | ------------------------ |
| BinaryOperator<T>    | T apply(T t, Tt)                     | T와 T를 연산한 후 T 리턴 |
| UnaryOperator<T>     | T apply(T t)                         | T를 연산한 후 T 리턴     |
| DoubleBinaryOperator | double applyAsDouble(double, double) | 두 개의 double 연산      |
| DoubleUnaryOperator  | double applyAsDouble(double)         | 한 개의 double연산       |
| IntBinaryOperator    | int applyAsInt(int, int)             | 두 개의 int 연산         |
| IntUnaryOperator     | int applyAsint(int)                  | 한 개의 int 연산         |
| LongBinaryOperator   | long applyAsLong(long, long)         | 두 개의 long 연산        |
| LongUnaryOperator    | long applyAsLong(long)               | 한 개의 long 연산        |



* IntBinaryOperator를 타겟 타입으로 하는 람다식은 매개값으로 두 개의 int를 받고 int를 리턴한다.

```java
IntBinaryOperator operator = (x, y) -> { ...; return int값; }
```



### Predicate 함수적 인터페이스

> 매개 변수와 boolean 리턴 값이 있는 testXXX() 메소드를 가지고 있다. 매개갑을 조사해서 true/false를 리턴하는 역할을 한다.



| 인터페이스명      | 추상메소드                 | 설명             |
| ----------------- | -------------------------- | ---------------- |
| Predicate<T>      | boolean test(T t)          | 객체 T를 조사    |
| BiPredicate<T, U> | boolean test(T t, U u)     | 객체  T,U를 조사 |
| DoublePredicate   | boolean test(double value) | double값을 조사  |
| IntPredicate      | boolean test(int value)    | int 값을 조사    |
| LongPredicate     | boolean test(long value)   | long값을 조사    |



* Predicate<T>를 타겟 타입으로하는 람다식은 T를 조사하여 boolean을 리턴한다.

```java
Predicate<Student> predicate = t -> { return t.getSex().equals("남자"); }
```



### andThen()과 compose() 디폴트 메소드

> 디폴트 및 정적 메소드는 추상 메소드가 아니기 때문에 함수적 인터페이스에 선언되어도 여전히 함수적 인터페이스의 성질을 잃지 않는다.  
>
> **함수적 인터페이스 성질** : 하나의 추상 메소드를 가지고 있고 람다식으로 익명 구현 객체를 생성할 수 있는 것.  
>
> `java.util.funciton` 패키지의 함수적 인터페이스는 하나 이상의 디폴트 및 정적 메소드를 가지고 있다.  
>
> Consumer, Funciton, Operator 종류의 함수적 인터페이스는 andThen()과 compose() 디폴트 메소드를 가지며 이 메소드들은 두 개의 함수적 인터페이스를 **순차적으로 연결하여 첫 번째 처리 결과를 두 번째 매개값으로 제공해서 최종 결과값을 얻을 때 사용한다.**



#### andThen()

> AB.method()를 호출하면 인터페이스A를 먼저 처리하고 결과를 인터페이스B의 매개값으로 제공한다. 인터페이스B는 제공받은 매개값을 가지고 최종 결과를 도출한다.

```java
인터페이스AB = 인터페이스A.andThen(인터페이스B);
최종결과 = 인터페이스AB.method();
```



#### compose()

> AB.method()를 호출하면  B를 먼저 처리하고 결과를 A의 매개값으로 제공한다. A는 제공받은 매개값을가지고 최종 결과를 리턴한다.

```java
인터페이스AB = 인터페이스A.compose(인터페이스B);
최종결과 = 인터페이스AB.method();
```



#### andThen()과 compose() 디폴트 메소드를 제공하는 함수적 인터페이스

| 종류     | 함수적 인터페이스   | andThen() | compose() |
| -------- | ------------------- | --------- | --------- |
| Consumer | Consumer<T>         | O         |           |
|          | BiConsumer<T,U>     | O         |           |
|          | DoubleConsumer      | O         |           |
|          | IntConsumer         | O         |           |
|          | LongConsumer        | O         |           |
| Function | Function<T,R>       | O         | O         |
|          | BiFunction<T,U,R>   | O         |           |
| Operator | BinaryOperator<T>   | O         |           |
|          | DoubleUnaryOperator | O         | O         |
|          | IntUnaryOperator    | O         | O         |
|          | LongUnaryOperator   | O         | O         |



### Consumer의 순차적 연결

> 처리 결과를 리턴하지 않기 때문에 andThen() 디폴트 메소드는 함수적 인터페이스의 호출 순서만을 정한다. 



* 예제

```java
import java.util.function.Consumer;

public class ComsumerAndThenExmaple {
  public static void main(String[] args) {
    Consumer<Member> consumerA = (m) -> {
      System.out.println("ConsumerA : " + consumerA.getName());
    };
    
    Consumer<Member> consumerB = (m) -> {
      System.out.println("ConsumerB : " + consumerB.getName());
    };
    
    Consumer<Member> consumerAB = consumerA.andThen(consumerB);
    consumerAB.accept(new Member("홍길동", "kildong", null));
  }
}
```



### Function의 순차적 연결

> Function과 Operator 종류의 함수적 인터페이스는 먼저 실행한 인터페이스의 결과를 매개값으로 넘겨주고 최종 처리 결과를 리턴한다. 

```java
Function<A,B> + Function<B,C> = Function<A,C> 라고 이해하면 된다.
```

* 예제

```java
package andThenCompose;

import java.util.function.Function;

public class FunctionAndThenComposeExample {
    public static void main(String[] args) {
        Function<Member, Address> functionA;
        Function<Address, String> functionB;
        Function<Member, String> functionAB;
        String city;

        functionA = (m) -> m.getAddress();
        functionB = (a) -> a.getCity();

        functionAB = functionA.andThen(functionB);
        city = functionAB.apply(
                new Member("홍길동", new Address("수원"))
        );
        System.out.println("거주 도시 : " + city);

        functionAB = functionB.compose(functionA);
        city = functionAB.apply(
                new Member("길동쓰", new Address("서울"))
        );
        System.out.println("거주 도시 : " + city);
    }
}
```



### and(), or(), negate() 디폴트 메소드와 isEqual() 정적 메소드

> Predicate 함수적 인터페이스는 and(), or(), negate() 디폴트 메소드를 가지고 있다. &&, ||, !과 대응된다고 볼 수있다.

| 종류      | 함수적 인터페이스 | and() | or() | negate() |
| --------- | ----------------- | ----- | ---- | -------- |
| Predicate | Predicate<T>      | O     | O    | O        |
|           | BiPredicate<T,U>  | O     | O    | O        |
|           | DoublePredicate   | O     | O    | O        |
|           | IntPredicate      | O     | O    | O        |
|           | LongPredicate     | O     | O    | O        |



* 2와 3의 배수를 조사하는 두 Predicate를 논리 연산한 새로운 Predicate 생성 예제

```java
import java.util.function.IntPredicate;

public class PredicateAndOrNegateExample {
    public static void main(String[] args) {
        // 2의 배수 검사
        IntPredicate predicateA = a -> a % 2 == 0;

        // 3의 배수 검사
        IntPredicate predicateB = a -> a % 3 == 0;

        IntPredicate predicateAB;
        boolean result;

        //and()
        predicateAB = predicateA.and(predicateB);
        result = predicateAB.test(9);
        System.out.println("9는 2와 3의 배수인가? " + result);

        // or()
        predicateAB = predicateA.or(predicateB);
        result = predicateAB.test(9);
        System.out.println("9는 2또는 3의 배수인가? " + result);

        // negate()
        predicateAB = predicateA.negate();
        result = predicateAB.test(9);
        System.out.println("9는 홀수인가? " + result);
    }
}
```



> 또한 Predicate<T>는 디폴트 메소드 이외에 isEqual() 정적 메소드를 추가로 제공한다.  
>
> isEqual() 메소드는 test() 매개값인 sourceObject와 isEqual()의 매개값인  targetObject를 `java.util.Object` 클래스의  equals() 매개값으로 제공하고 Objects.equals(sourceObject, targetObject)의 리턴값을 얻어 새로운 Predicate<T> 를 생성한다.

```java
Predicate<Object> predicate = Predicate.isEqual(targetObject);
boolean result = predicate.test(sourceObject);
```



`Objects.equals(sourceObject, targetObject)`는 다음과 같은 리턴값을 제공한다.

| sourceObject | targetObject | 리턴값                                     |
| ------------ | ------------ | ------------------------------------------ |
| null         | null         | true                                       |
| not null     | null         | false                                      |
| null         | not null     | false                                      |
| not null     | not null     | sourceObject.equals(targetObject)의 리턴값 |



* isEqual 정적 메소드 예제

```java
package isEqual;

import java.util.function.Predicate;

public class PredicateIsEqualExample {
    public static void main(String[] args) {
        Predicate<String> predicate;

        predicate = Predicate.isEqual(null);
        System.out.println("null, null: " + predicate.test(null));

        predicate = Predicate.isEqual("Java8");
        System.out.println("null, Java8: " + predicate.test(null));

        predicate = Predicate.isEqual("null");
        System.out.println("Java8, null: " + predicate.test("Java8"));

        predicate = Predicate.isEqual("Java8");
        System.out.println("Java8, Java8: " + predicate.test("Java8"));
        
    }
}
```



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

