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

