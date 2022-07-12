# 웹 애플리케이션과 싱글톤

> 출처 : 스프링 핵심 원리 - 기본편

- 스프링은 태생이 기업용 온라인 서비스 기술을 지원하기 위해 탄생했다.
- 대부분의 스프링 애플리케이션은 웹 애플리케이션이다. 웹이 아닌 애플리케이션 개발도 얼마든지 할 수 있다.
- 웹 애플리케이션은 보통 여러 고객이 동시에 요청을 한다.

![스크린샷 2022-07-12 22.44.18](https://tva1.sinaimg.cn/large/e6c9d24egy1h44h89lcfwj20xw0is40c.jpg)

> 고객(클라이언트)이 3번 요청을 하면 3번 객체를 생성해야 하는 문제가 생긴다.



### 스프링이 없는 순수한 컨테이너

```java
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

* 실행 결과

```
memberService1 : hello.core.member.MemberServiceImpl@2002fc1d
memberService2 : hello.core.member.MemberServiceImpl@69453e37
```

> 생성된 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성한다. 그래서 memberService1, 2가 서로 값이 다르다.

* 고객 트래픽이 초당 100이 나오면 초당 100개의 객체가 생성되고 소멸된다. -> 메모리 낭비가 심하다.
* 해결방안은 해당 객체가 딱 1개만 생성되고, 공유하도록 설계하면 된다. -> Singleton 패턴 



## 싱글톤 패턴(Singleton Pattern)

> 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴.

### 싱글톤 패턴을 적용하려면

* 객체 인스턴스를 2개 이상 생성하지 못하도록 막아야 한다.
  * private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 한다.

```java
public class SingletonService {
    // static 영역에 객체 instance를 미리 하나 생성해서 올려둔다.
    private static final SingletonService instance = new SingletonService();

    // 이 객체 인스턴스가 필요하면 오직 `getInstance()`를 통해서만 객체를 조회할 수 있다. 이 메서드를 호출하면 항상 같은 인스턴스를 변환한다.
    public static SingletonService getInstance() {
        return instance;
    }
	// 딱 1개의 객체 인스턴스만 존재해야 하므로 생성자를 private으로 막아서 혹시라도 외부에서 new 키워드로 접근하는 것을 막는다.	
    private SingletonService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
```

1. static 영역에 객체 instance를 미리 하나 생성해서 올려둔다.
2. 이 객체 인스턴스가 필요하면 오직 `getInstance()`를 통해서만 객체를 조회할 수 있다. 이 메서드를 호출하면 항상 같은 인스턴스를 변환한다.
3. 딱 1개의 객체 인스턴스만 존재해야 하므로 생성자를 private으로 막아서 혹시라도 외부에서 new 키워드로 접근하는 것을 막는다.



### 싱글톤 패턴을 적용한 객체 사용

```java
@Test
@DisplayName("싱글톤 패턴을 적용한 객체 사용")
void singletonServiceTest() {
    // private으로 생성자를 막아두었다. 컴파일 오류가 발생한다.
    // new SingletonService();

    // 1. 조회 : 호출할 때 마다 같은 객체를 반환
    SingletonService singletonService1 = SingletonService.getInstance();
    // 2. 조회 : 호출할 때 마다 같은 객체를 반환
    SingletonService singletonService2 = SingletonService.getInstance();

    // 참조값이 같은 것을 확인
    System.out.println("singletonService 1 : " + singletonService1);
    System.out.println("singletonService 2 : " + singletonService2);

    // singletonService1 == singletonService2
    Assertions.assertThat(singletonService1).isSameAs(singletonService2);

    singletonService1.logic();
}
```

*싱글톤 패턴을 구현하는 방법은 여러가지가 있다. 여기서는 객체를 미리 생성해두는 가장 단순하고 안전한 방법을 선택했다.*

* 실행결과

```
singletonService 1 : hello.core.singleton.SingletonService@70325e14
singletonService 2 : hello.core.singleton.SingletonService@70325e14
```

> 같은 객체 인스턴스가 반환이 되었다.  
>
>   
>
> 싱글톤 패턴을 적용하면 고객의 요청이 올 때 마다 객체를 생성하는 것이 아니라 이미 만들어진 객체를 공유해서 효율적으로 사용할 수 있다.



### 싱글톤 패턴의 문제점

* 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
* 의존관계상 클라이언트가 구체 클래스에 의존한다. DIP 위반
* 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다.
* 테스트가 어렵다
* 내부 속성을 변경하거나 초기화가 어렵다.
* private 생성자를 쓰기 때문에 자식 클래스를 만들기 어렵다.
* 결론적으로 유연성이 떨어진다. DI 인젝션이 힘들다.
* 안티패턴으로 불리기도 한다.



## 싱글톤 컨테이너

> 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.
>
>   
>
> 스프링에서의 Bean이 싱글톤으로 관리되는 대표적인 예이다.



* 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도 객체 인스턴스를 싱글톤으로 관리한다.
* 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 **싱글톤 레지스트리**라고 하낟.
* 스프링 컨테이너의 기능 덕분에 싱글턴 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
  * 또한 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
  * DIP, OCP, Test, private 생성자로 자식 클래스 만들기 등 자유롭게 싱글톤을 사용할 수 있다.



### 스프링 컨테이너를 사용하는 테스트코드

  

```java
@Test
@DisplayName("스프링 컨테이너와 싱글톤")
void springContainer() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    MemberService memberService1 = ac.getBean("memberService", MemberService.class);
    MemberService memberService2 = ac.getBean("memberService", MemberService.class);

    System.out.println("memberService1 : " + memberService1);
    System.out.println("memberService2 : " + memberService2);

    // memberService1 == memberService2
    Assertions.assertThat(memberService1).isSameAs(memberService2);
}
```



### 싱글톤 컨테이너 적용 후

![스크린샷 2022-07-12 23.54.18](https://tva1.sinaimg.cn/large/e6c9d24egy1h44j93zvs2j20z60jaabd.jpg)

> 스프링 컨테이너 덕분에 고객의 요청이 올 때마다 인스턴스를 공유해서 효율적으로 재사용 할 수 있다.



## 싱글톤 방식의 주의점 

> 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 