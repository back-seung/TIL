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



