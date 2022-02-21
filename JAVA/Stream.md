# 스트림

> 스트림은 자바 8부터 추가된 컬렉션의 저장 요소를 하나씩 참조해서 람다식으로 처리할 수 있도록 해주는 반복자이다.

## 반복자 스트림

> 자바 7까지는 List<String> 컬렉션에서 요소를 순차적으로 처리하기 위해 `Iterator` 반복자를 사용했다.

* Iterator 사용

```java
List<String> list = Arrays.asList("홍길동", "백승한", "김자바");
Iterator<String> iterator = list.iterator();
while(iterator.hasNext()) {
    String name = iterator.next();
    System.out.println(name);
}
```

* Stream 사용

```java
List<String> list = Arrays.asList("홍길동", "백승한", "김자바");
Stream<String> stream = list.stream();
stream.forEach(name -> System.out.println(name));
```

컬렉션의 `stream()`메소드로 스트림 객체를 얻고 나서 `stream.forEach( name -> System.out.println(name) );` 메소드를 통해 컬렉션의 요소를 하나씩 콘솔에 출력한다. `forEach()`  메소드는 다음과 같이 Consumer 함수적 인터페이스 타입의 매개값을 가지므로 컬렉션의 요소를 소비할 코드를 람다식으로 기술할 수 있다.

* forEach 문법

```java
void forEach( Consumer<T> action );
```

## 스트림의 특징

> Stream은 Iterator와 비슷한 역할을 하는 반복자이지만, 람다식으로 요소 처리 코드를 제공하는 점과 내부 반복자를 사용하므로 병렬 처리가 쉽다는 점, 중간 처리와 최종 처리 작업을 수행하는 점에서 많은 차이를 가지고 있다. 자세하게 살펴보자

### 람다식으로 요소 처리 코드를 제공한다.

Stream이 제공하는 대부분의 요소 처리 메소드는 함수적 인터페이스 매개 타입을 가지기 때문에 람다식 또는 메소드 참조를 이용해서 요소 처리 내용을 매개값으로 전달할 수 있다. 

```java
package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LambdaExpressionExample {
    public static void main(String[] args) {
        List<Student> list = Arrays.asList(
                new Student("cho", 25),
                new Student("baek", 25),
                new Student("weon", 25)
        );

        Stream<Student> stream = list.stream();
        stream.forEach( s -> {
            String name = s.getName();
            int score = s.getScore();
            System.out.println("이름 : " + name + ", 점수 : " + score);
        });
    }
}
```

### 내부 반복자를 사용하므로 병렬처리가 쉽다.

**외부 반복자**란 개발자가 코드로 직접 컬렉션의 요소를 반복해서 가져오는 코드 패턴을 말한다.  

내부 반복자는 컬렉션 내부에서 요소들을 반복시키고, 개발자는 요소당 처리해야 할 코드만 제공하는 코드 패턴을 말한다.  

![스크린샷 2022-02-20 10.38.57.png](/var/folders/lr/kr79btws2rn24c1jtbx10slw0000gn/T/TemporaryItems/NSIRD_screencaptureui_m37lJo/스크린샷%202022-02-20%2010.38.57.png)

> 그림을 통해 이해하는 외부 반복자, 내부 반복자

내부 반복자를 사용해서 얻는 이점은 컬렉션 내부에서 어떻게 요소를 반복시킬 것인가는 컬렉션에게 맡겨두고, 개발자는 요소 처리 코드에만 집중할 수 있다고 집중할 수 있다. 내부 반복자는 요소들의 반복 순서를 변경하거나, 멀티 코어 CPU를 최대한 활용하기 위해 요소들을 분배시켜 병렬 작업을 할 수 있게 도와주기 때문에 하나씩 처리하는 순차적 외부 반복자 보다 효율적으로 요소를 반복시킬 수 있다.  

Iterator는 컬렉션 요소를 가져오는 것부터 처리까지 모두 작성해야 하지만, 스트림은 람다식으로 요소 처리 내용만 전달할 뿐, 반복은 컬렉션 내부에서 일어난다.  

* 병렬처리 : 한가지 작업을 서브 작업으로 나누고, 서브 작업들을 분리된 스레드에서 병렬적으로 처리하는 것.

병렬 처리 스트림을 이용하면 런타임 시 하나의 작업을 서브 작업으로 자동으로 나누고, 이 결과를 자동으로 결합하여 결과를 만든다. 즉, 여러개의 스레드가 요소들을 부분적으로 합하고 이 부분합을 최종 결합하여 전체 합을 생성한다.

```java
package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParallelExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("baek", "cho", "weon");

        // 순차 처리
        Stream<String> stream = list.stream();
        stream.forEach(ParallelExample::print);
        System.out.println();


        // 병렬 처리
        Stream<String> parallelStream = list.parallelStream();
        parallelStream.forEach(ParallelExample::print);
    }


    public static void print(String str) {
        System.out.println(str + " : " + Thread.currentThread().getName());

    }
}
```

### 스트림은 중간 처리와 최종 처리를 할 수 있다.

스트림은 컬렉션의 요소에 대해 중간 처리와 최종 처리를 수행할 수 있는데, 중간 처리에서는 **매핑, 필터링, 정렬을 수행**하고 최종 처리에서는 **반복, 카운팅, 평균, 총합 등**의 집계 처리를 수행한다.  

* List에 저장되어 있는 Student 객체를 중간 처리에서 score 필드값으로 매핑하여 최종 처리에서 평균값 산출하는 예제

```java
package stream;

import java.util.Arrays;
import java.util.List;

public class MapAndReduceExample {
    public static void main(String[] args) {
        List<Student> studentList = Arrays.asList(
                new Student("baek", 70),
                new Student("cho", 58),
                new Student("weon", 80)
        );

        double avg = studentList.stream()
                .mapToInt(Student::getScore)
                .average()
                .getAsDouble();

        System.out.println("평균 점수 : " + avg);
    }
}
```

## 스트림의 종류

> 자바 8부터 새로 추가된 `java.util.stream` 패키지에는 부모 인터페이스인 BaseStream과 그 자식들로 이루어져 있다.  
> 
> BaseStream에는 공통 메소드가 있을 뿐, 직접적으로 사용은 하지 않는다.

### 자식 인터페이스

1. Stream

2. IntStream

3. LongStream

4. DoublStream

> `Stream`은 객체 요소를 처리하는 스트림이고, 나머지는 기본 타입인 int, long, double을 처리하는 스트림이다.

### 스트림 구현 객체를 얻는 소스
| 리턴 타입 | 메소드 (매개변수) |     | 소스 |
| --------- | ----------------- | --- | ---- |
|Stream\<T>           |                   |     |      |
|           |                   |     |      |
|           |                   |     |      |





