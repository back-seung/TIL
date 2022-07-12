# 웹 애플리케이션과 싱글톤

> 출처 : 스프링 핵심 원리 - 기본편

- 스프링은 태생이 기업용 온라인 서비스 기술을 지원하기 위해 탄생했다.
- 대부분의 스프링 애플리케이션은 웹 애플리케이션이다. 웹이 아닌 애플리케이션 개발도 얼마든지 할 수 있다.
- 웹 애플리케이션은 보통 여러 고객이 동시에 요청을 한다.

![스크린샷 2022-07-12 22.44.18](https://tva1.sinaimg.cn/large/e6c9d24egy1h44h89lcfwj20xw0is40c.jpg)

> 고객(클라이언트)이 3번 요청을 하면 3번 객체를 생성해야 하는 문제가 생긴다.



* 스프링이 없는 순수한 컨테이너

```java
package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SingletonTest {
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();
        // 1. 조회 : 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회 : 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        System.out.println("memberService1 : " + memberService1);
        System.out.println("memberService2 : " + memberService2);

        // memberService1 != memberService2
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
    }
}
```

> 생성된 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성한다.

* 고객 트래픽이 초당 100이 나오면 초당 100개의 객체가 생성되고 소멸된다. -> 메모리 낭비가 심하다.
* 해결방안은 해당 객체가 딱 1개만 생성되고, 공유하도록 설계하면 된다. -> Singleton 패턴 



## 싱글톤 패턴(Singleton Pattern)

> 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴.

### 싱글톤 패턴을 적용하려면

* 객체 인스턴스를 2개 이상 생성하지 못하도록 막아야 한다.
  * private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 한다.