# IOC와 DI, 그리고 Container

## 제어의 역전 - IoC(Inversion Of Control)

> 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 제어의 역전(IoC)라고 한다.

* 기존 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성하고, 연결하고 실행했다 -> 구현 객체는 프로그램의 제어 흐름을 스스로 조종했다. 개발자 입장에서는 자연스러운 흐름이다.
* 반면에 AppConfig가 등장한 이후에 구현 객체는 자신의 로직을 실행하는 역할만 담당한다. 프로그램의 제어 흐름은 이제 AppConfig가 가져간다. 에를 들어서 `OrderServiceImpl`은 필요한 인터페이스들을 호출하지만 어떤 구현 객체들이 실행될지 모른다.
* 프로그램에 대한 제어 흐름에 대한 권한은 모두 AppConfig가 가지고 있다. 심지어 `OrderServiceImpl`도 AppConfig가 생성한다. 그리고 AppConfig는 `OrderServiceImpl`이 아닌 OrderService 인터페이스의 다른 구현 객체를 생성하고 실행할 수도 있다. 그런 사실도 모른체 OrderServiceImpl은 자신의 로직을 실행할 뿐이다.



### 프레임워크 vs 라이브러리

* 프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임 워크가 맞다.(JUnit)
* 라이브러리는 내가 작성한 코드를 직접 제어하는 것이다.



## 의존관계 주입 - DI(Dependency Injection)

> 의존 관계는 **정적인 클래스 의존 관계와, 실행 시점에 결정되는 동적인 객체(인스턴스) 의존 관계** 둘을 분리해서 생각해야 한다.

* `OrderServiceImpl`은 `DiscountPolicy`인터페이스에 의존한다. 실제 어떤 구현 객체가 사용될지는 모른다.

### 정적인 클래스 의존 관계

클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단할 수 있다. 정적인 의존관계는 애플리케이션을 실행하지 않아도 분석할 수 있다.

`OrderServiceImpl`은 `MemberRepository`, `DiscountPolicy`에 의존한다는 것을 알 수 있다. 그런데 이러한 클래스 의존관계만으로는 실제 어떤 객체가 `OrderServiceImpl`에 주입될지 알 수 없다.



