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
|              |              |
|              |              |

 