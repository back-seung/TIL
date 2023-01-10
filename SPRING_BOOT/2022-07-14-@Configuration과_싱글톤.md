# @Configuration과 싱글톤

> 출처 : 스프링 핵심 원리 - 기본편



## AppConfig - 문제 발생

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
```

* memberService 빈을 만드는 코드를 보면 `memberRepository()`를 호출한다.
  * 이 메서드를 호출하면 `new MemoryMemberRepositry()`를 호출한다.
* orderService 빈을 만드는 코드도 동일하게 `memberRepository()`를 호출한다.
  * 이 메서드를 호출하면 `new MemoryMemberRepository()`를 호출한다.

결과적으로 각각 다른 2개의 `MemoryMemberRepository`가 생성되면서 싱글톤이 깨지는 것처럼 보인다. 스프링 컨테이너는 이 문제를 어떻게 해결할까?



## 검증 용도의 코드 추가

 

```java
@Test
void configurationTest() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
    OrderServiceImpl orderService1 = ac.getBean("orderService", OrderServiceImpl.class);
    MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

    MemberRepository memberRepository1 = memberService.getMemberRepository();
    MemberRepository memberRepository2 = orderService1.getMemberRepository();

    System.out.println("memberRepository1 = " + memberRepository1);
    System.out.println("memberRepository2 = " + memberRepository2);
    System.out.println("memberRepository = " + memberRepository);

    Assertions.assertThat(memberRepository1).isSameAs(memberRepository);
    Assertions.assertThat(memberRepository2).isSameAs(memberRepository);
    Assertions.assertThat(memberRepository1).isSameAs(memberRepository2);
}
```

* 출력결과

```
memberRepository1 = hello.core.member.MemoryMemberRepository@5cdd09b1
memberRepository2 = hello.core.member.MemoryMemberRepository@5cdd09b1
memberRepository = hello.core.member.MemoryMemberRepository@5cdd09b1
```

확인해보면 memberRepository는 각각 2번 호출되어야 하는데 모두 같은 인스턴스가 공유되어 사용된다. 어떻게 된 것일까? 



### AppConfig에 호출 로그 남기기

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call ppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
```

> 스프링 컨테이너가 각각 `@Bean`을 호출해서 스프링 빈을 생성한다. 그래서 `memberRepository()`는 다음과 같이 총 3번이 호출되어야 하는 것 아닐까?

1. 스프링 컨테이너가 스프링 빈에 등록하기 위해 @Bean이 붙어있는 memberRepository()호출
2. memberService()에서 memberRepository() 호출
3. orderService()에서 memberRepository() 호출

* 그렇기에 예상대로라면 다음과 같이 호출되어야 한다.

```java
call AppConfig.memberService
call AppConfig.memberRepository    
call AppConfig.memberRepository
call AppConfig.orderService    
call AppConfig.memberRepository    
```

* 하지만 예상과는 다른 호출 결과가 나온다.

```java
call AppConfig.memberService
call AppConfig.memberRepository
call AppConfig.orderService
```



## @Configuration과 바이트 코드 조작의 마법

스프링 컨테이너는 싱글톤 레지스트리다. 따라서 스프링 빈이 싱글톤이 되도록 보장해주어야 한다.**그런데 스프링이 자바 코드까지 어떻게 하기는 어렵다**. 저 자바 코드를 보면 분명 3번 호출되어야 하는 것이 맞다.

그래서 스프링은 클래스의 바이트코드를 조작하는 라이브러리를 사용한다.

모든 비밀은 @Configuration 어노테이션을 적용한 AppConfig에 있다.

* 다음 코드를 보자

```java
@Test
void configurationDeep() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    // AppConfig도 스프링 빈으로 등록된다.
    AppConfig bean = ac.getBean(AppConfig.class);

    System.out.println("bean = " + bean.getClass());
    // 출력 : bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$ad8aea39
}
```

- 사실 `AnnotationConfigApplicationContext`에 파라미터로 넘긴 값은 스프링 빈으로 등록된다. 그래서 AppConfig도 스프링 빈이 된다.
- `AppConfig` 스프링 빈을 조회해서 클래스 정보(`.getClass()`)를 출력했다.
  - 순수한 코드라면 `class hello.core.AppConfig`가 출력되어야 한다.
  - 하지만 `class hello.core.AppConfig$$EnhancerBySpringCGLIB$$ad8aea39`가 출력되었다. 클래스명 뒤에 CGLIB이 붙으면서 상당히 복잡해졌다.
- 스프링이 CGLIB이라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고 그 다른 클래스를 스프링 빈으로 등록한 것이다.
  - 이 임의의 다른 클래스가 바로 싱글톤이 보장되도록 해준다.
- **AppConfig@CGLIB 예상 코드**

```java
@Bean
public MemberRepository memberRepository() {
    if(memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
        return 스프링 컨테이너에서 찾아서 변환;
    } else {
        기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록;
        return 반환;
    }
}
```

- @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다. 덕분에 싱글톤이 보장이 된다.

> AppConfig@CGLIB은 AppConfig의 자식 타입이므로 AppConfig 타입으로 조회할 수 있다.



### @Configuration를 빼고 @Bean만 적용한다면?

@Configuration을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤이 보장된다.

하지만 @Bean으로만 적용하면 어떻게 될까?

```java
//@Configuration 삭제
public class AppConfig {
    
}
```

* 처음 예상했던 것처럼 `memberRepository()`가 3번 호출된다. 그리고 빈(AppConfig)을 조회해보면 순수 클래스인 `class hello.core.AppConfig`가 출력된다.

```
call AppConfig.memberService
call AppConfig.memberRepository
call AppConfig.memberRepository
call AppConfig.orderService
call AppConfig.memberRepository

bean = class hello.core.AppConfig
```

이 출력 결과를 통해서 AppConfig가 CGLIB 기술 없이 순수한 AppConfig로 스프링 빈에 등록된 것을 확인할 수 있다.



### 정리

* @Bean만 사용해도 스프링 빈으로 등록되지만 싱글톤을 보장하지 않는다.
  * `memberRepository()`처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
* 스프링 설정 정보는 항상 `@Configuration`을 사용하자.

