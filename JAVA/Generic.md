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

