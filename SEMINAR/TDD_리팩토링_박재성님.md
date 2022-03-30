## [참조 - 유튜브 - 190425 TDD 리팩토링 by 자바지기 박재성님](https://www.youtube.com/watch?v=bIeqAlmNRrA&t=84s)

> TDD와 리팩토링을 잘하는 방법은 오직 연습이다. 하지만 무조건 연습을 많이 한다고 잘할 수 있을까?
>
> 무엇인가를 연습할 때는 의식적인 연습이 필요하다.

### 의식적인 연습의 7가지 원칙 - 1만 시간의 재발견

1. 효과적인 훈련 기법이 수립되어 있는 기술 연마
2. 개인의 컴포트 존을 벗어난 지점에서 진행, 자신의 현재 능력을 살짝 넘어가는 작업을 지속적으로 시도
3. 명확하고 구체적인 목표를 가지고 진행
4. 신중하고 계획적이다. 즉 개인이 온전히 집중하고 의식적으로 행동할 것을 요구
5. 피드백과 피드백에 따른 행동 변경을 수반
6. 효과적인 심적 표상을 만들어내는 한편으로 심적 표상에 의존
7. 기존에 습득한 기술의 특정 부분을 집중적으로 개선함으로써 발전시키고, 수정하는 과정을 수반



### 1단계 - 단위 테스트 연습

내가 사용하는 API 사용법을 익히기 위한 학습 테스트에서 시작

* 자바 String 클래스의 다양한 메소드 사용법

```java
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StringTest {
    
    @Test
    public void split() {
        String[] values = "1.".split(",");
        assertThat(values).contains("1");
        values = "1,2".split(",");
        assertThat(values).containsExactly("1","2");
    }
    
    @Test
    public void subString() {
        String input = "(1,2)";
        String result = input.subString(1, input.length() -1);
        assertThat(result).isEqualTo("1,2");
    }
}
```

* 자바 ArrayList에 데이터를 추가, 수정, 삭제하는 방법

```java
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionTest {
    
    @Test
    public void arrayList() {
        ArrayList<String> values = new ArrayList<>();
        values.add("first");f
        values.add("second");
        assertThat(values.add("third")).isTrue();
        assertThat(values.size()).isEqualTo(3);
        assertThat(values.get(0)).isEqualTo("first");
        assertThat(values.contains("first")).isTrue();
        assertThat(values.remove(0)).isEqualTo("first");
        assertThat(values.size()).isEqualTo(2);
    }
}

```



### 1단계 연습의 효과

* 단위 테스트 방법을 학습할 수 있다.
* 단위테스트 도구(xUnit)의 사용법을 익힐 수 있다.
* 사용하는 API에 대한 학습 효과가 있다.

### 1단계 연습 종류

* 내가 구현하는 메소드 중 Input과 Output이 명확한 클래스 메소드(보통 Util 성격의 메소드)에 대한 단위 테스트를 연습한다.

* 알고리즘을 학습한다면 알고리즘 구현에 대한 검증을 단위테스트로 한다.



### 2단계 TDD 연습

어려운 문제를 해결하는 것이 목적이 아니라 TDD 연습이 목적. 난이도가 낮거나 자신에게 익숙한 문제로 시작하는 것을 추천함

웹, 모바일 UI나 DB에 의존관계를 가지지 않는 요구사항으로 연습하는게 좋다(토이 프로젝트)



### 예시 - 문자열 덧셈 계산기 요구사항

| 입력(input)  | 출력(output) |
| ------------ | ------------ |
| null \|\| "" | 0            |
| "1"          | 1            |
| "1,2"        | 3            |
| "1, 2:3"     | 6            |



### TDD Circle of life

>    Test Passes :arrow_right: Test Fails :arrow_right: Refactor 

많은 개발자가 놓치는 것이 TDD연습을 실패하는 테스트를 만들고, 패스하고를 반복한다는 것이다. 이 중에서 가장 중요한 것은 리팩토링에 있다고 한다.



* 테스트 코드

```java
public class StringCalculatorTest {
  @Test
  public void null_또는_빈값() {
    assertThat(StringCalculator.splitAndSum(null)).isEqualTo(0);
    assertThat(StringCalculator.splitAndSum("")).isEqualTo(0);    
  }
  
  @Test
  public void 값_하나() {
        assertThat(StringCalculator.splitAndSum("1")).isEqualTo(3);
  }
  
  @Test
  public void 쉼표_구분자() {
    assertThat(StringCalculator.splitAndSum("1,2")).isEqualTo(3);    
  }
  
  @Test
  public void 쉼표_콜론_구분자() {
    assertThat(StringCalculator.splitAndSum("1,2:3")).isEqualTo(6);    
  }
}
```

* StringCalculator 코드

```java
/*
* 리팩토링 전 코드
*/

public class StringCalculator {
  public static int splitAndSum(String text) {
    int result = 0;
    if (text == null || text.isEmpty()) {
      result = 0;
    } else {
      String[] values = text.split(",|:");
      for(String value : values) {
        result += Integer.parseInt(value);
      }
    }
    return result;
  }
}
```



## 리팩토링 연습 - 메소드 분리

>   **테스트 코드는 변경하지 말고, 테스트 대상 코드(프로덕션 코드)를 개선하는 연습을 의식적으로 집중하여 한다.**

```java
public class StringCalculator {
  public static int splitAndSum(String text) {
    int result = 0;
    if (text == null || text.isEmpty()) {
      result = 0;
    } else {
      String[] values = text.split(",|:");
      for(String value : values) {
        result += Integer.parseInt(value);
      }
    }
    return result;
  }
}
```



1. 한 메서드에는 오직 한 단계의 indent(들여쓰기)를 유지하라.

```java
      for(String value : values) {
        result += Integer.parseInt(value);
      }
```

>   현재 위 구문은 들여쓰기가 2인 곳이 있다. 이를 고쳐보자

```java
public class StringCalculator {
  public static int splitAndSum(String text) {
    int result = 0;
    if (text == null || text.isEmpty()) {
      result = 0;
    } else {
      String[] values = text.split(",|:");
      result = sum(values); // 들여쓰기가 1로 유지된다!
    }
    return result;
  }

	private static int sum(String[] values) {
  	int result = 0;
	  for(String value : values) {
  	  result += Integer.parseInt(value);
	  }
  	return result;
	}
}
```



2.   메소드가 단 한가지의 일만 하게하라.

```java
	private static int sum(String[] values) {
  	int result = 0;
	  for(String value : values) {
  	  result += Integer.parseInt(value);
	  }
  	return result;
	}
```

>   위 메소드를 보면 현재 **문자열을 숫자로 바꾼뒤 이를 result에 담는 두 가지의 일을 하고 있다.**

```java
public class StringCalculator {
  public static int splitAndSum(String text) {
    {...}
	}
  
  private static int[] toInts(String[] values) {
    int[] numbers = new int[values.length];
    for (int i =0; i < values.length; i++) {
      numbers[i] = Integer.parseInt(values[i]);
    }
    return numbers;
  }
  
	private static int sum(int[] numbers) {
  	int result = 0;
	  for(String number : numbers) {
  	  result += number;
	  }
  	return result;
	}
}  	
```

>   이렇게 메소드를 분리한다면 재사용이 쉽다. for문을 두 번 돌기는 하지만 대부분 구현하는 반복문은 데이터 크기가 크지 않기 때문에 성능 차이에 큰 영향을 끼치지 않는다.

3.   else 예약어를 쓰지 않는다.

```java
public class StringCalculator {
  public static int splitAndSum(String text) {
    if (text == null || text.isEmpty()) {
	    return 0; // 바로 return을 하여 로컬 변수를 만들 필요가 없다!
	  }
    
//  String[] values = text.split(",|:"); // + 로컬 변수가 필요한가?
//  return sum(values);
    return sum(toInt(text.split(",|:")));
	}
  
  private static int[] toInts(String[] values) {
    int[] numbers = new int[values.length];
    for (int i =0; i < values.length; i++) {
      numbers[i] = Integer.parseInt(values[i]);
    }
    return numbers;
  }
  
	private static int sum(int[] numbers) {
  	int result = 0;
	  for(String number : numbers) {
  	  result += number;
	  }
  	return result;
	}
}  	
```



### compose method 패턴 적용

```java
public class StringCalculator {
  public static int splitAndSum(String text) {
    if(isBlank(text)) {
      return 0;
    }
    
    return sum(toInts(split(text)));
  }
  
  private static boolean isBlank(String text) {
    return text == null || text.isEmpty();
  }
  
  private static String[] split(String text) {
    return text.split(",|:");
  }
  
  private static int[] toInts(String[] values) {
    int[] numbers = new int[values.length];
    for (int i =0; i < values.length; i++) {
      numbers[i] = Integer.parseInt(values[i]);
    }
    return numbers;
  }
  
	private static int sum(int[] numbers) {
  	int result = 0;
	  for(String number : numbers) {
  	  result += number;
	  }
  	return result;
	}
}
```

>   1줄 단위의 메소드를 만들어 처음 코드를 파악하는 이에게도 알기 쉽게끔 구현이 된다.



### 연습할 때는

*   한번에 모든 원칙을 지키면서 리팩토링하려고 연습하지 마라.
*   한번에 한 가지 명확하고 구체적인 목표를 가지고 연습하라.
*   연습은 극단적인 방법으로 하는 것도 좋은 방법이다. 예를 들어 메소드의 라인을 15줄 -> 10줄로 줄여가면서 연습하는 것도 좋은 방법이다.



## 리팩토링 연습 - 클래스 분리

### 다시 문자열 덧셈 계산기 요구사항

>   쉼표(,) 콜론(:)을 구분자로 가지는 문자열을 전달하는 경우 구분자를 기준으로 분리한 각 숫자의 합을 반환.
>
>   **숫자 이외의 값 또는 음수를 전달하는 경우 RuntimeException 예외를 Thorw 한다.**

| 입력(input)  | 출력(output)     |
| ------------ | ---------------- |
| null \|\| "" | 0                |
| "1"          | 1                |
| "1,2"        | 3                |
| "1, 2:3"     | 6                |
| "-1, 2:3"    | RuntimeException |



```java
public class StringCalculatorTest {
  @Test
  public void null_또는_빈값() {
    assertThat(StringCalculator.splitAndSum(null)).isEqualTo(0);
    assertThat(StringCalculator.splitAndSum("")).isEqualTo(0);    
  }
  
  @Test
  public void 값_하나() {
        assertThat(StringCalculator.splitAndSum("1")).isEqualTo(3);
  }
  
  @Test
  public void 쉼표_구분자() {
    assertThat(StringCalculator.splitAndSum("1,2")).isEqualTo(3);    
  }
  
  @Test
  public void 쉼표_콜론_구분자() {
    assertThat(StringCalculator.splitAndSum("1,2:3")).isEqualTo(6);    
  }
  
  // +++ 음수값 테스트 추가 +++
  @Test(expected = RuntimeException.class) {
    public void 음수값() {
      StringCalculator.splitAndSum("-1,2:3");
    }
  }
}
```

*   요구사항(음수값 처리)에 따른 클래스 수정

```java
public class StringCalculator {
  public static int splitAndSum(String text) {
    {...}
	}
  
  private static int[] toInts(String[] values) {
    int[] numbers = new int[values.length];
    for (int i =0; i < values.length; i++) {
      numbers[i] = toInt(values[i]);
    }
    return numbers;
  }
  
  private static int[] toInt(String value) {
    int number = Integer.parseInt(value);
    if (number < 0) {
      throw new RuntimeException();
    }
    return number;
  }
}
```



*   모든 원시값과 문자열을 포장하라.

```java
  private static int[] toInt(String value) {
    int number = Integer.parseInt(value);
    if (number < 0) {
      throw new RuntimeException();
    }
    return number;
  }
```

>   위 메소드를 클래스로 분리한다.

```java
public class Positive {
  private int number;
  
  public Positive(String value) {
    this(Integer.parseInt(value));
  }
  
  public Positive(int number) {
    if (number > 0) {
      throw new RuntimeException();
    }
    
    this.number = number;
  }
}
```

>   Positive(양수)라는 클래스로 분리하므로써 string과 int로 생성자를 구현하였다. 이로 인해 Positive 클래스는 양수를 보장받을 수 있게된다. 따라서 다시 StringCalculator를 다음과 같이 수정할 수 있다.



```java
public class StringCalculator {
  {...}
  
  private static Positive[] toPositives(String[] values) {
    Postive[] numbers = new Positive[values.length];
    for(int i = 0; i < values.length; i++) {
      numbers[i] = new Positive(values[i]);
    }
    return numbers;
  }
  
  private static int sum(Positive[] numbers) {
    Positive result = new Positive(0);
    for (Positive number : numbers) {
      result = result.add(number); // Positive.add(int number) 매소드 추가
    }
    return result.getNumber();
  }
}

/*
* Positive add Method
*/

public class Positive {
  {...}
  
  public Positive add(Positive other) {
    return new Positive(this.number + other.number);
  }
  
  public int getNumber() {
    return number;
  }
}
```



*   일급 컬렉션을 사용한다.

>   클래스를 포장하는 클래스를 만든다.

```java
import java.util.Set;

public class Lotto {
  private static final int LOTTO_SIZE = 6;
  
  private final Set<LottoNumber> lotto;
  
  private Lotto(Set<LottoNumber> lotto) {
    if(lotto.size() != LOTTO_SIZE) {
      throw new IllegalException();
    }
    this.lotto = lotto;
  }
}
```



*   3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.

```java
public class WinningLotto {
  private final Lotto lotto;
  private final LottoNumber no;
  
  public WinningLotto(Lotto lotto, LottoNumber no) {
    if(lotto.contains(no)) {
      throw new IllegalArgumentException();
    }
    this.lotto = lotto;
    this.no = no;
  }
  
  public Rank match(Lotto userLotto) {
    int matchCount = lotto.match(userLotto);
    boolean matchBonus = userLotto.contains(no);
    return Rank.valueOf(matchCount, matchBonus);
  }
}
```

