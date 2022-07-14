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



