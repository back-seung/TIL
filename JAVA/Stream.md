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
| 리턴 타입 | 메소드 (매개변수) | 소스 |
| --------- | ----------------- | --- |
|Stream\<T>           | java.util.Collection.stream()<br />java.util.Collection.parallelStream() | 컬렉션 |
| Stream<T><br />IntStream<br />LongStream<br />DoubleStream | Arrays.Stream(T[])          Stream.of(T[])<br />Arrays.stream(int[])          IntStream.of(int[])<br />Arrays.stream(long[])          LongStream.of(long[])<br />Arrays.stream(double[])          DoubleStream.of(double[]) | 배열 |
| IntStream | IntStream.range(int, int)<br />IntStream.rangeClosed(int, int) | int 범위 |
| LongStream | LongStream.range(long, long)<br />LongStream.rangeClosed(long, long) | long 범위 |
| Stream<Path> | Files.find(Path, int, BiPredicate, FileVisitOption)<br />Files.list(Path) | 디렉토리 |
| Stream<String> | Files.lines(Path, Charset)<br />BufferedReader.lines() | 파일 |
| DoubleStream | Random.doubles(...)<br />Random.ints()<br />Random.longs() | 랜덤 수 |



### 컬렉션으로부터 스트림 얻기

> 다음 예제는 List<Stream> 컬렉션에서 Stream<Student>를 얻어내고 요소를 콘솔에 출력한다.

```java
package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FromCollectionExample {
	public static void main(String[] args) {
		List<Student> studentList = Arrays.asList(new Student("baek", 25), new Student("weon", 25),
				new Student("cho", 25));
		
		Stream<Student> stream = studentList.stream();
		
		stream.forEach(s -> {
			System.out.println(s.getName());
		});
	}
}
```



### 배열로부터 스트림 얻기

```java
package stream;

import java.util.Arrays;
import java.util.stream.Stream;

public class FromArrayExample {
	public static void main(String[] args) {
		String[] strArray = {"baek", "weon", "cho"};
		Stream<String> strStream = Arrays.stream(strArray);
		
		strStream.forEach(a -> System.out.print(a + ", "));
	}
}
```



### 숫자 범위로부터 스트림 얻기

> 1부터 100까지의 합을 구하기 위해 IntStream의 rangeClosed() 메소드를 사용한다.  
>
> `rangeClosed()`는 첫 번쨰 매개값에서부터 두 번째 매개값까지 순차적으로 제공하는 IntStream을 리턴한다. `range()`는 똑같이 IntStream을 리턴하는데, 두 번쨰 매개값은 포함하지 않는다.

```java
package stream;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FromIntRangeExample {
	public static int sum;

	public static void main(String[] args) {

		IntStream stream = IntStream.rangeClosed(1, 100);
		stream.forEach(a -> sum += a);
		System.out.println("총합 : " + sum);
	}
}
```

 

### 파일로부터 스트림 얻기 

> `Files`의 정적 메소드인 lines()와BufferedReader의 lines() 메소드를 이용하여 문자 파일의 내용을 스트림을 통해 행단위로 읽고 콘솔에 출력한다.

```java
package stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FromFileContentExameple {
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("/Exam_Source/stream/linedata.txt");
		Stream<String> stream;

		// Files.lines() 메소드 ㅇ이용
		stream = Files.lines(path, Charset.defaultCharset());
		stream.forEach(System.out::print);
		System.out.println();

		// BufferedReader lines() 이용
		File file = path.toFile();
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		stream = br.lines();
		stream.forEach(System.out::println);
	}
}
```



### 디렉토리로부터 스트림 얻기





## 스트림 파이프라인

> 리덕션 : 대량의 데이터를 가공해서 축소하는 것. 합계, 평균값, 카운팅, 최대값 등이 대표적인 리덕션의 결과물이라고 볼 수있다. 그러나 컬렉션의 요소를 리덕션의 결과물로 바로 집계할 수 없을 경우에는 집계하기 좋도록 필터링, 매핑, 정렬, 그룹핑 등의 중간 처리가 필요하다.



### 중간 처리와 최종 처리

> 스트림은 데이터의 중간처리(필터링, 매핑, 그룹핑)와 최종 처리(카운팅, 평균, 합계)를 파이프라인으로 해결한다. 파이프라인은 **여러 개의 스트림이 연결되어 있는 구조를 말한다**. 파이프라인에서 최종 처리를 제외하고는 모두 중간 처리 스트림이다.  
>
> 중간 스트림이 생성될 때 바로 중간 처리 되는 것이 아니라 최종 처리가 시작되기 전까지 중간처리는 지연된다. 이후 최종 처리가 시작되면 비로소 컬렉션의 요소가 하나씩 처리 되고, 최종 처리 된다.

* 파이프라인 예시

```java
// 로컬 변수 사용 시
Stream<Member> maleFemaleStream = list.stream();
Stream<Member> maleStream = maleFemaleStream.filter( m -> m.getSex == Member.Male);
IntStream ageStream = maleStream.mapToInt(Member::getAge);
OptionalDouble optionalDouble = ageStream.average();
double ageAvg = optionalDouble.getAsDouble();

// 로컬 변수 사용 X
double ageAvg = list.stream()
  .filter( a -> a.getSex == Member.MALE)
  .mapToInt(Member::getAge)
  .average()
  .getAsDouble();
```



* 예제

```java
package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamPipelinesExample {
    public static void main(String[] args) {
        List<Member> list = Arrays.asList(
                new Member("kildong", Member.MALE, 25),
                new Member("baek", Member.MALE, 45),
                new Member("weon", Member.FEMALE, 27)
        );

        double ageAvg = list.stream()
                .filter(a -> a.getSex() == Member.MALE)
                .mapToInt(Member::getAge)
                .average()
                .getAsDouble();

        System.out.println(ageAvg);
    }
}
```



### 중간 처리 메소드와 최종 처리 메소드

> 스트림 파이프라인에서 중간 처리를 하는 메소드와 최종 처리를 하는 메소드의 종류를 살펴본다.

| 종류     | 종류   | 리턴타입                                                | 메소드(매개변수)     | 소속된 언터페이스                   |
| -------- | ------ | ------------------------------------------------------- | -------------------- | ----------------------------------- |
| 중간처리 | 필터링 | Stream<br />IntStream<br />LongStream<br />DoubleStream | distinct()           | 공통                                |
|          |        |                                                         | filter(...)          | 공통                                |
|          | 매핑   |                                                         | flatMap(...)         | 공통                                |
|          |        |                                                         | flatMapToDouble(...) | Stream                              |
|          |        |                                                         | flatMapToInt(...)    | Stream                              |
|          |        |                                                         | flatMapToLong(...)   | Stream                              |
|          |        |                                                         | map(...)             | 공통                                |
|          |        |                                                         | mapToInt(...)        | Stream, LongStream, DoubleStream    |
|          |        |                                                         | mapToDouble(...)     | Stream, LongStream, IntStream       |
|          |        |                                                         | mapToLong(...)       | Stream, IntStream, DoubleStream     |
|          |        |                                                         | mapToObj(...)        | IntStream, LongStream, DoubleStream |
|          |        |                                                         | asDoubleStream()     | IntStream, LongStream               |
|          |        |                                                         | asLongStream()       | IntStream                           |
|          |        |                                                         | boxed()              | IntStream, LongStream, DoubleStream |
|          | 정렬   |                                                         | sorted(...)          | 공통                                |
|          | 루핑   |                                                         | peek(...)            | 공통                                |
| 최종처리 | 매칭   | boolean                                                 | allMatch(...)        | 공통                                |
|          |        | boolean                                                 | anyMatch(...)        | 공통                                |
|          |        | boolean                                                 | noneMatch(...)       | 공통                                |
|          | 집계   | long                                                    | count()              | 공통                                |
|          |        | OptionalXXX                                             | findFirst()          | 공통                                |
|          |        | OptionalXXX                                             | max(...)             | 공통                                |
|          |        | OptionalXXX                                             | min(...)             | 공통                                |
|          |        | OptionalDouble                                          | average()            | IntStream, LongStream, DoubleStream |
|          |        | OptionalXXX                                             | reduce(...)          | 공통                                |
|          |        | int, long, double                                       | sum()                | IntStream, LongStream, DoubleStream |
|          | 루핑   | void                                                    | forEach(...)         | 공통                                |
|          | 수집   | R                                                       | collect(...)         | 공통                                |

> 리턴 타입이 스트림이라면 중간 처리 타입이고, 기본 타입이거나 OptionalXXX라면 최종 처리 메소드이다.
>
> 공통의 의미는 Stream, IntStream, LongStream, DoubleStream에서 모두 제공된다는 것이다.  



## 필터링(distinct(), filter())

> 필터링은 중간 처리 기능으로 요소를 걸러내는 역할을 한다.ㄴㄴ

| 리턴 타입                                               | 메소드(매개 변수)       | 설명        |
| ------------------------------------------------------- | ----------------------- | ----------- |
| Stream<br />IntStream<br />LongStream<br />DoubleStream | distinct()              | 중복제거    |
|                                                         | filter(Predicate)       | 조건 필터링 |
|                                                         | filter(IntPredicate)    |             |
|                                                         | filter(LongPredicate)   |             |
|                                                         | filter(DoublePredicate) |             |

* distinct() 메소드는 중복을 제거하는데, Stream의 경우 Object.eqauls(Object)가 true이면 동일 객체로 판단하고 중복을 제거한다. 그 외 int, long, double Stream의 경우 동일 값이면 중복을 제거한다.

* 예제

```java
package stream;

import java.util.Arrays;
import java.util.List;

public class FilteringExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList(
                "baek",
                "weon",
                "cho",
                "weon",
                "bung"
        );

        // 중복 제거
        names.stream()
                .distinct()
                .forEach(n -> System.out.println(n));
        System.out.println();


        // 필터링
        names.stream()
                .filter(n -> n.startsWith("b"))
                .forEach(n -> System.out.println(n));
        System.out.println();

        // 중복 제거 후 필터링
        names.stream()
                .distinct()
                .filter(n -> n.startsWith("b"))
                .forEach(n -> System.out.println(n));
    }
}
```



## 매핑( flatMapXXX(), mapXXX(), asXXXStream(), boxed())

> 매핑은 중간처리 기능으로 스트림의 요소를 다른 요소로 대체하는 작업이다.



### flatMapXXX()

> 요소를 대체하는 복수 개의 요소들로 구성된 새로운 스트림을 리턴한다.

<img width="676" alt="스크린샷 2022-02-22 09 10 50" src="https://user-images.githubusercontent.com/84169773/155125882-3d2115b8-bfb4-4b91-b160-eae4cc290752.png">

* flatMapXXX() 메소드의 종류는 다음과 같다

| 리턴 타입    | 메소드(매개 변수)                          | 요소 ->대체 요소       |
| ------------ | ------------------------------------------ | ---------------------- |
| Stream<R>    | flatMap(Function<T.Stream<R>>)             | T -> Stream<R>         |
| DoubleStream | flatMap(DoubleFunction<DoubleStream>)      | double -> DoubleStream |
| IntStream    | flatMap(IntFucntion<IntStream>)            | int -> IntStream       |
| LongStream   | flatMap(LongFunction<LongStream>)          | long -> LongStream     |
| DoubleStream | flatMapToDouble(Function<T, DoubleStream>) | T -> DoubleStream      |
| IntStream    | flatMapToInt(Function<T, IntStream>)       | T -> IntStream         |
| LongStream   | flatMapToLong(Function<T, LongStream>)     | T -> LongStream        |



* List<String>에 저장된 요소별로 단어를 뽑아 단어 스트림으로 재생성하는 예제

```java
package stream;

import java.util.Arrays;
import java.util.List;

public class FlatMapExample {
    public static void main(String[] args) {
        List<String> inputList1 = Arrays.asList(
                "java8 Lambda", "stream mapping"
        );

        inputList1.stream()
                .flatMap(data -> Arrays.stream(data.split(" ")))
                .forEach(word -> System.out.println(word));

        List<String> inputList2 = Arrays.asList("10, 20, 30, 40, 50, 60");

        inputList2.stream()
                .flatMapToInt(data -> {
                    String[] strArr = data.split(",");
                    int[] intArr = new int[strArr.length];
                    for (int i = 0; i < strArr.length; i++) {
                        intArr[i] = Integer.parseInt(strArr[i].trim());
                    }
                    return Arrays.stream(intArr);
                })
                .forEach(number -> System.out.println(number));
    }
}
```



### mapXXX() 메소드

> 요소를 대체하는 요소로 구성된 새로운 스트림을 리턴한다. A와 B스트림이 있을때 각각 A->C, B->D 요소로 대체된다고 할 경우 C, D 요소를 가지는 새로운 스트림이 생성된다.



* mapXXX() 메소드 종류

| 리턴 타입    | 메소드 (매개 변수)                | 요소 -> 대체요소 |
| ------------ | --------------------------------- | ---------------- |
| Stream<R>    | map(Function<T, R>)               | T -> R           |
| DoubleStream | mapToDouble(ToDoubleFunction<T>)  | T -> Double      |
| IntStream    | mapToInt(ToIntFunction<T>)        | T -> Int         |
| LongStream   | mapToLong(ToLongFunction<T>)      | T -> Long        |
| DoubleStream | map(DoubleUnaryOperator)          | double -> double |
| IntStream    | mapToInt(DoubleToIntFunction)     | double -> Int    |
| LongStream   | mapToLong(DoubleToLongFunction)   | double -> long   |
| Stream<U>    | mapToObj(DoubleFunction<U>)       | double -> U      |
| IntStream    | map(IntUnaryOperator)             | int -> int       |
| DoubleStream | maptoDouble(IntToDoubleFunction)  | int -> double    |
| LongStream   | mapToLong(IntToLongFunction)      | int -> long      |
| Stream<U>    | maoToObj(IntFunction<U>)          | int -> U         |
| LongStream   | map(LongUnaryOperator)            | long -> long     |
| DoubleStream | mapToDouble(LongToDoubleFunction) | long -> double   |
| IntStream    | mapToInt(LongToIntFunction)       | long -> int      |
| Stream<U>    | mapToObj(LongFunction<U>)         | long -> U        |

* 예제

```java
package stream;

import java.util.Arrays;
import java.util.List;

public class MapExample {
    public static void main(String[] args) {
        List<Student> studentList = Arrays.asList(
                new Student("홍길동", 10),
                new Student("백승한", 20),
                new Student("김말이", 30)
        );
        studentList.stream()
                .mapToInt(Student::getScore)
                .forEach(score -> System.out.println(score));
    }
}
```



### asDoubleStream(), asLongStream(), boxed() 메소드

> asDoubleStream 메소드는 IntStream의 int 요소 또는 LongStream의 long 요소를 double 요소로 변환해서 DoubleStream을 생성한다.  
>
> asLongStream 메소드 또한 마찬가지로 IntStream의 int요소 또는 DoubleStream의 double 요소를 long으로 변환해서 LongStream을 생성한다.  
>
> boxed() 메소드는 int, long, double 요소를 Wrapper 클래스로 박싱해서 Stream을 생성한다.



* 예제

```java
package stream;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AsDoubleStreamAndBoxedExample {
    public static void main(String[] args) {
        int[] intArr = {1, 2, 3, 4, 5};

        IntStream intStream = Arrays.stream(intArr);
        intStream
                .asDoubleStream()
                .forEach(d -> System.out.println(d));

        System.out.println();

        intStream = Arrays.stream(intArr);
        intStream
                .boxed()
                .forEach(obj -> System.out.println(obj.intValue()));

    }
}
```



## 정렬 (sorted())

> 스트림은 요소가 최종 처리되기 전에 중간 단계에서 요소를 정렬해서 최종 처리 순서를 변경할 수 있다. 요소를 정렬하는 메소드는 다음과 같다.

| 리턴 타입    | 메소드( 매개 변수)    | 설명                                    |
| ------------ | --------------------- | --------------------------------------- |
| Stream<T>    | sorted()              | 객체를 Comparable 구현 방법에 따라 정렬 |
| Stream<T>    | sorted(Comparator<T>) | 객체를 주어진 Comparator에 따라 정렬    |
| DoubleStream | sorted()              | double 요소를 오름차순 정렬             |
| IntStream    | sorted()              | int 요소를 오름차순 정렬                |
| LongStream   | sorted()              | long 요소를 오름차순 정렬               |



객체 요소일 경우에는 클래스가 Comparable을 구현하지 않으면 sorted() 메소드를 호출했을 때 `ClassCastException`이 발생한다. 따라서 Comparable을 구현한 요소에서만 sorted() 메소드를 호출해야 한다.

Comparable을 구현한 객체이면 다음 중 하나의 방법으로 sorted()를 호출하면 된다.

```java
sorted();
sorted( (a,b) -> a.compareTo(b) );
sorted( Comaparator.naturalOrder() );
```



만약 객체 요소가 Comparable을 구현하고 있지만 기본비교 방법과 정반대 방법으로 정렬하고 싶다면 다음과 같이 sorted()를 호출하면 된다.

```java
sorted( (a,b) -> b.compare(a) );
sorted( Comparator.reverseOrder() );
```



만약 객체 요소가 Comaprable을 구현하지 않았다면 Comparable을 매개값으로 갖는 sorted() 메소드를 사용하면 된다. Comparator는 함수적 인터페이스이므로 다음과 같이 작성할 수 있다.

```java
sorted( (a,b) -> { . . . })
```

> 중괄호 안에는 a와 b 중 a가 작으면 음수, 같으면 0, 크면 1을 리턴하는 코드를 작성하면 된다.



* 예제 - 숫자 요소일 경우 오름차순 정렬, Student 요소일 경우 기본 비교(Comparable) 기준으로 오름차순 정렬 후 출력

```java
package stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class SortingExample {
    public static void main(String[] args) {
        // 숫자 요소일 경우
        IntStream intStream = Arrays.stream(new int[]{5, 2, 4, 3, 1});
        intStream
                .sorted()
                .forEach(n -> System.out.print(n + ","));
        System.out.println();

        //객체 요소일 경우
        List<Student> studentList = Arrays.asList(
                new Student("홍길동", 30),
                new Student("백승한", 40),
                new Student("신용권", 20)
        );

        studentList.stream()
                .sorted()   // 오름차순 정렬
                .forEach(s -> System.out.print(s.getScore() + ","));
        System.out.println();


        studentList.stream()
                .sorted(Comparator.reverseOrder()) // 내림차순 정렬
                .forEach(s -> System.out.print(s.getScore() + ","));

    }
}
```



## 루핑(peek(), forEach())

> 루핑은 요소 전체를 반복하는 것을 말한다. 루핑하는 메소드에는 peek(), forEach()가 있다. 각자 루핑의 기능은 같지만 동작 방식이 다르다. peek()은 중간 처리 메소드, forEach()는 최종 처리 메소드이다.  

* peek()은 중간 처리 단계에서 전체 요소를 루핑하며 추가적 작업을 하기 위해 사용한다. 반드시 최종 처리 메소드가 호출되야 작동한다.  

  ```java
  intStream
    .filter( a -> a % 2 == 0 )
    .peek( a -> System.out.println(a)) // peek() 지연
    .sum() // 정상 작동
  ```

  > 요소 처리의 최종 단계가 전체 합이라면 sum() 메소드를 호출해야만 정상적으로 동작한다.



* forEach()는 최종 처리 메소드이기 때문에 파이프라인 마지막에 루핑하면서 요소를 하나씩 처리한다. forEach()는 요소를 소비하는 최종 처리 메소드이므로 이후에 sum()처럼 다른 최종 처리 메소드를 호출하면 안된다.

```java
package stream;

import java.util.Arrays;

public class LoopingExample {
    public static void main(String[] args) {
        int[] intArr = {1, 2, 3, 4, 5};

        System.out.println("[peek()를 마지막에 호출한 경우]");
        Arrays.stream(intArr)
                .filter(a -> a % 2 == 0)
                .peek(n -> System.out.println(n)); // 동작 X

        System.out.println("[최종 처리 메소드를 마지막에 호출한 경우]");
        int total = Arrays.stream(intArr)
                .filter(a -> a % 2 == 0)
                .peek(n -> System.out.println(n)) // 동작 O
                .sum(); // 최종 처리 메소드
        System.out.println("총합 : " + total);

        System.out.println("[forEach()를 마지막에 호출한 경우]");
        Arrays.stream(intArr)
                .filter(a -> a % 2 == 0)
                .forEach(n -> System.out.println(n)); // 최종 메소드로 동작
    }
}
```



## 매칭(allMatch(), anyMatch(), noneMatch())

> 스트림 클래스는 최종 처리 단계에서 요소들이 특정 조건에 만족하는지 조사할 수 있도록 세가지 매칭 메소드를 제공하고 있다.  
>
> * allMatch() : 모든 요소들이 매개값으로 주어진 Predicate의 조건을 만족하는지 조사한다.
>
> * anyMatch() :  최소한 한 개의 요소가 매개값으로 주어진 Predicate의 조건을 만족하는지 조사한다.
> * noneMatch() : 모든 요소들이 매개값으로 주어진 Predicate의 조건을 만족하지 않는지 조사한다.

| 리턴 타입 | 메소드(매개 변수)                                            | 제공 인터페이스 |
| --------- | ------------------------------------------------------------ | --------------- |
| boolean   | allMatch(Predicate<T> predicate)<br />anyMatch(Predicate<T> predicate)<br />noneMatch(Predicate<T> predicate) | Stream          |
| boolean   | allMatch(IntPredicate<T> predicate)<br />anyMatch(IntPredicate<T> predicate)<br />noneMatch(IntPredicate<T> predicate) | IntStream       |
| boolean   | allMatch(LongPredicate<T> predicate)<br />anyMatch(LongPredicate<T> predicate)<br />noneMatch(LongPredicate<T> predicate) | LongStream      |
| boolean   | allMatch(DoublePredicate<T> predicate)<br />anyMatch(DoublePredicate<T> predicate)<br />noneMatch(DoublePredicate<T> predicate) | DoubleStream    |



* 예제 - int 배열 스트림 생성(모든 요소가 2의 배수인지, 하나라도 3의 배수가 존재하는지, 모든 요소가 3의 배수가 아닌지를 조사)

```java
package stream;

import java.util.Arrays;

public class MatchExample {
    public static void main(String[] args) {
        int[] intArr = {2, 4, 6};

        boolean result = Arrays.stream(intArr)
                .allMatch(a -> a % 2 == 0);
        System.out.println("모두 2의 배수인가? : " + result);

        result = Arrays.stream(intArr)
                .anyMatch(a -> a % 3 == 0);
        System.out.println("하나라도 3의 배수인가? : " + result);

        result = Arrays.stream(intArr)
                .noneMatch(a -> a % 3 == 0);
        System.out.println("3의 배수가 없는가? : " + result);
    }
}
```



## 기본 집계(sum(), count(), average(), min(), max())

> 집계는 최종 처리 기능으로 요소들을 처리해서 카운팅, 합계, 평균값, 최대값, 최소값 등과 같이 하나의 값으로 산출하는 것을 말한다.  
>
> 집계 : 대량의 데이터를 가공해서 축소하는 리덕션



### 스트림이 제공하는 기본 집계

| 리턴 타입                    | 메소드(매개 변수)             | 설명         |
| ---------------------------- | ----------------------------- | ------------ |
| long                         | count()                       | 요소 개수    |
| OptionalXXX                  | findFirst()                   | 첫 번째 요소 |
| Optional<T><br />OptionalXXX | max(Comparator<T>)<br />max() | 최대 요소    |
| Optional<T><br />OptionalXXX | min(Comparator<T>)<br />min() | 최소 요소    |
| OptionalDouble               | average()                     | 요소 평균    |
| int, long, double            | sum()                         | 요소 합계    |

> `OptionalXXX` 는 자바 8에서 추가한 `java.util`패키지의 Optional, OptionalInt, OptionalLong, OptionalDouble을 말한다. 이들은 값을 저장하는 값 기반 클래스이다. 



### Optional 클래스

> 이 클래스들은 저장하는 값의 타입만 다를 뿐 제공하는 기능은 거의 동일하다. Optional 클래스는 단순히 집계 값만 저장하는 것이 아니라, 집계 값이 존재하지 않을 경우 디폴트 값을 설정할 수 도 있고, 집계 값을 처리하는 Consumer도 등록이 가능하다.

 

* 메소드 종류

| 리턴 타입                        | 메소드(매개 변수)                                            | 설명                                         |
| -------------------------------- | ------------------------------------------------------------ | -------------------------------------------- |
| boolean                          | isPresent()                                                  | 값이 저장되어 있는지 여부                    |
| T<br />double<br />int<br />long | orElse(T)<br />orElse(double)<br />orElse(int)<br />orElse<long | 값이 저장되어 있지 않을 경우 Default 값 지정 |
| void                             | ifPresent(Consumer)<br />ifPresent(DoubleConsumer)<br />ifPresent(IntConsumer)<br />ifPresent(LongConsumer) | 값이 저장되어 있을 경우 Consumer에서 처리    |

* 컬렉션은 주로 동적으로 요소가 추가된다. 만약 값이 없을 때 평균(average())를 구한다면NoSuchElementException이 발생하게 된다.
* 요소가 없을 경우 예외를 피하는 3가지 방법에 대해서 알아보자.

```java
// 1. Optional 객체 얻어 isPresent() 메소드로 평균 여부 확인
OptionalDouble optional = list.stream()
  .mapToInt(Integer :: intValue)
  .average();
if(optional.isPresent()) {
  System.out.println("평균 : " + optional.getAsDouble());
} else {
  System.out.println("평균 : 0.0");
}

// 2. orElse() 사용
double avg = list.stream()
  .mapToInt(Integer :: intValue)
  .average()
  .orElse(0.0);
System.out.println("평균 : " + avg);

// 3. ifPresent() 메소드 사용 - 평균값이 있을 경우에만 값 사용
list.stream()
  .mapToInt(Integer :: intValue)
  .average()
  .ifPresent(a -> System.out.println("평균 : " + a));
```



* 집계 예제

```java
package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class OptionalExample {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        // 예외 발생 NoSuchElementException
//        double avg = list.stream()
//                .mapToInt(Integer::intValue)
//                .average()
//                .getAsDouble();

        // 방법 1
        OptionalDouble optional = list.stream()
                .mapToInt(Integer::intValue)
                .average();
        if (optional.isPresent()) {
            System.out.println("평균 : " + optional.getAsDouble());
        } else {
            System.out.println("방법1 평균 : 0.0");
        }

        // 방법 2
        double avg = list.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        System.out.println("방법2 평균 : " + avg);

        // 방법 3
        list.stream()
                .mapToInt(Integer::intValue)
                .average()
                .ifPresent(a -> System.out.println("방법3 평균 : " + a));
    }
}
```



## 커스텀 집계(reduce())

> 스트림은 기본 집계 메소드를 제공하지만, 프로그램화 해서 다양한 집계 결과물을 만들 수 있도록 reduce() 메소드도 제공한다.

| 인터페이스   | 리턴 타입      | 메소드(매개 변수)                                 |
| ------------ | -------------- | ------------------------------------------------- |
| Stream       | Optional<T>    | reduce(BinaryOperator<T> accumulator)             |
|              | T              | reduce(T identity, BinaryOperator<T> accumulator) |
| IntStream    | OptionalInt    | reduce(IntBinaryOperator op)                      |
|              | int            | reduce(int identity, IntBinaryOperator op)        |
| LongStream   | OptionalLong   | reduce(LongBinaryOperator op)                     |
|              | long           | reduce(long identity, LongBinaryOperator op)      |
| DoubleStream | OptionalDouble | reduce(DoubleBinaryOperator op)                   |
|              | double         | reduce(double identity, DoubleBinaryOperator op)  |

> 각 인터페이스에는 매개 타입으로 XXXOperator, 리턴 타입으로 OptionalXXX, int, long, double을 가지는 reduce() 메소드가 오버로딩 되어있다. 스트림에 요소가 전혀 없을 경우 identity(디폴트값)가 리턴된다.
