# 스프링 프레임워크 핵심 기술

## IoC 컨테이너

### IoC란

* Inversion Of Control의 약자로 제어의 역전이라는 뜻이다.
* 객체의 의존성을 역전시켜 객체 간의 결합도를 줄이고 유연한 코드를 작성할 수 있게 하여 가독성 및 코드 중복, 유지 보수를 편하게 할 수 있게 한다.
* 의존 관계 주입(Dependency Injection)이라고도 하며, **어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는게 아닌, 주입 받아 사용하는 방법을 말한다.**

### 자바 / 스프링 객체 생성 ~ 실행 순서

* 자바

  1. 객체 생성
  2. 의존성 객체 생성(클래스 내부에서 생성)
  3. 의존성 객체 메소드 호출

  ```java
  public class UserService {
  	
  	// 의존성 객체 생성
  	private UserRepository userRepository = new Repository();
  
  	public void save(User user) {
  		// 의존성 객체 메소드 호출
  		userRepository.save(user);
  	}
  }
  ```

* 스프링

  1. 객체 생성
  2. 의존성 객체 주입(객체의 생명주기 제어권을 스프링에게 위임하여 스프링이 만들어놓은 객체를 주입)
  3. 의존성 객체 메소드 호출

  ```java
  public class UserService {
  	
  	// 의존성 객체 주입
  	private UserRepository userRepository;
  
  	public UserService(UserRepository userRepository) {
  		this.userRepository = userRepository; 
  	}
  
  	public void save(User user) {
  		// 의존성 객체 메소드 호출
  		userRepository.save(user);
  	}
  }
  ```

### IoC 컨테이너란

* 객체를 생성하고, 관리하고 책임지고 의존성을 관리해준다. 즉, 인스턴스 생성부터 소멸까지 인스턴스 생명주기를 개발자가 아닌 컨테이너가 대신해서 관리한다.
* 스프링 애플리케이션 실행 시 모든 의존성 객체를 만들어주고, 필요한 곳에 주입시켜줌으로써 Bean들은 싱글톤 패턴의 특징을 가진다.
* Bean이란
  * Spring이 제공하는 컨테이너를 통해서 관리되는 인스턴스를 Bean이라고 부른다.

### 스프링 IoC 컨테이너의 종류

* BeanFactory
  * 스프링 IoC 컨테이너의 최상위에 있는 기본이자 핵심 인터페이스이다.
  * 외부의 Bean 설정 소스로부터 Bean을 구성하고, 수정, 조회, 반환 관리를 한다.
  * Bean을 조회하는 getBean() 메소드가 정의되어 있다.
  * BeanFactory 계열의 인터페이스만 구현한 클래스는 단순히 컨테이너에서 객체를 생성하고, DI를 처리하는 기능만 제공한다.
* ApplicationContext
  * BeanFactory에 여러가지 AOP 등 대규모 웹 프로젝트에 필요한 컨테이너 기능을 추가한 것이다.
  * 보통 BeanFactory를 바로 사용하지 않고, 더 확장된 ApplicationContext를 사용한다.
  * 스프링의 각종 부가 기능을 추가 제공한다.
    * 메시지 소스를 활용한 국제화 기능
      * 예를 들어 웹사이트에 접속한 국적이 한국이면 한국어로, 영어권에서 들어오면 영어로 출력
    * 환경변수
      * 로컬, 개발, 운영 등을 구분해서 처리
    * 애플리케이션 이벤트
      * 이벤트를 발행하고 구독하는 모델을 편리하게 지원
    * 편리한 리소스 조회
      * 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회
  * 다양한 빈 설정 방법
    * ClassPathXmlApplicationContext (XML)
    * AnnotationConfigApplicationContext (Java)

## Bean Definition

스프링의 다양한 설정 형식을 지원하는데의 중심에는 BeanDefinition이라는 추상화가 있다.

### BeanDefinition이란

!https://tva1.sinaimg.cn/large/e6c9d24egy1h43bz8by0yj20w20a2mxq.jpg

빈 설정 메타 정보.

BeanDefinition이란 쉽게 말하면 역할과 구현을 개념적으로 나눈 것인데 XML(`<bean>`)이든 자바 코드(`@Bean`)든 BeanDefinition을 만들면 스프링 컨테이너는 이를 몰라도 되며 BeanDefinition만 알면된다.

스프링 컨테이너는 이 메타 정보를 기반으로 스프링 빈을 생성한다.

!https://tva1.sinaimg.cn/large/e6c9d24egy1h43c9okqkuj216g0oago4.jpg

* AnnotationConfigApplicationContetx는 AnnotatedBeanDefinitionReader를 통해 Appconfig.class를 읽고 BeanDefinition을 생성한다.
* Xml, XXX 또한 이와 같이 BeanDefinition을 생성한다.

### BeanDefinition 정보

* BeanClassName - 생성할 빈의 클래스명(자바 설정처럼 팩토리 역할의 빈을 사용하면 없음)
* factoryBeanName - 팩토리 역할의 빈을 사용할 경우 이름 ex)AppConfig
* factoryMethodName - 빈을 생성할 팩토리 메서드 지정, 예)memberService
* Scope - 싱글톤(기본값)
* lazyInit - 스프링 컨테이너를 생성할 때 빈을 생성하는 것이 아니라, 실제 빈을 사용할 때까지 최대한 생성을 지연처리 하는지의 여부
* InitMethodName - 빈을 생성하고 의존관계를 적용한 뒤에 호출되는 초기화 메서드 명
* DestroyMethodName - 빈의 생명주기가 끝나서 제거하기 직전에 호출되는 메서드 명
* Constructor arguments, Properties - 의존관계 주입에서 사용한다.(자바 설정처럼 팩토리 역할의 빈을 사용하면 없음)

### @Autowired

* 필요한 의존 객체의 “타입”에 해당하는 빈을 찾아 주입한다.

* Autowired의 옵션 중 required라는 것이 있는데 기본 값은 true이다.

  * 만약 주입할 스프링 Bean이 주입되지 않았을  때도 애플리케이션을 정상적으로 구동시키고 싶다면 @Autowired(required = false)를 지정하면 된다. 이 옵션을 적용하면 당연히 객체가 Bean으로 등록된 뒤,  주입 대상을 찾지 못했을 때 의존성 주입이 이루어지지 않는다.

* 의존성 주입은 크게 4가지 방법이 있다.

  1. 생성자 주입

     * 생성자를 통해 의존 관계를 주입하는 방식이다.
     * 생성자 호출 시점에 딱 한 번만 호출되는 것이 보장되어 불변적이다.
     * 필수 의존 관계에 사용된다.

     ```java
     @Service
     public class UserService {
     	private final UserRepository userRepository;
     		
     	@Autowired
     	public UserService(UserRepositoty userRepository) {
     		this.userRepository = userRepository;
     	}
     }
     ```

     * 생성자가 1개만 있는 경우에는 @Autowired를 생략해도 된다.

  2. Setter 주입

     * 수정자 주입이라고도 하며, 선택 또는 변경 가능성이 있는 의존 관계에 사용한다.
     * Java Bean 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.

     ```java
     @Service
     public class UserService {
     	private final UserRepository userRepository;
     		
     	@Autowired
     	public void setUserRepository(UserRepositoty userRepository) {
     		this.userRepository = userRepository;
     	}
     }
     ```

  3. 필드 주입

     * 이름 그대로 필드에 바로 주입하는 방법이다.  코드가 간결하다.
     * 외부에서 변경이 불가능하여 테스트가 어려운 단점이 있다. DI 프레임워크가 없으면 아무것도 할 수 없다.

     ```java
     @Service
     public class UserService {
     
     	@Autowired
     	private final UserRepository userRepository;
     
     }
     ```

  4. 일반 메서드 주입

     * 일반 메서드를 만들어 주입시키는 방법이다. 잘 사용하지 않는다.

* 스프링을 포함한 대부분의 DI 프레임워크에서는 위 방법 중에서 생성자 주입을 권고한다.

* 그 이유는 다음과 같다.

  * 객체의 불변성 확보

    * 대부분의 의존관계 주입이 한 번 일어나면 애플리케이션 종료 시점까지 의존관계를 변경할 필요가 없다.
    * 오히려 변하면 안된다.
    * 수정자 주입을 사용하는 것은 public 메서드인 Setter를 통해 값을 변경할 수 있다. 변경하면 안되는 메서드를 열어두는 것은 좋은 설계가 아니다.

  * 테스트 코드 작성 용이

    * 테스트가 특정 프레임워크에 의존하는 것은 침투적이기 때문에 좋지 않다. 따라서 순수 자바로 테스트를 작성하는 것이 좋기 때문에 생성자 주입이 권장된다.

  * final 키워드 사용 가능

    * 의존성 주입을 시킬 필드에 final 키워드를 사용할 수 있어, 컴파일 시점에 누락된 의존성을 확인할 수 있다.
    * 이는 생성자 주입 사용시, Bean으로 등록할 때 생성자를 통해 의존성 주입이 함께 설정되기 때문에 가능한 것이다.
    * 다른 방법들은 Bean으로 등록한 이후에 호출되어 의존 관계를 설정하기 때문에 final 키워드를 사용할 수 없다.

  * 순환 참조 방지

    * 개발하다보면 여러 서비스들 사이에 의존관계가 생기는 경우가 있다. 예를 들면 A에서 B를 호출하고, B에서는 A를 호출하는 경우이다.

    ```java
    public class A {
    	
    	@Autowired
    	B b;
    
    	public void register() {
    		b.save();
    	}
    }
    
    public class B {
    	@Autowired
    	A a;
    	
    	public void save(A a) {
    		a.register();
    	}
    }
    ```

    * 이와 같은 경우에는 계속 서로를 호출하다가 StackOverFlowError를 발생시키고 애플리케이션이 죽게 된다.
    * 필드 주입, 수정자 주입과는 다르게 컨테이너가 Bean을 생성하는 시점에서 순환참조가 일어나기 때문에 처음부터 애플리케이션 구동이 실패하기 때문에 컴파일 시점에서 이를 미리 알고 해결할 수 있다.