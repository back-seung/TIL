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