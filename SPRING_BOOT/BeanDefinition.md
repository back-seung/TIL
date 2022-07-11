## 스프링 빈 설정 메타 정보 - BeanDefinition

> 스프링의 다양한 설정 형식을 지원하는데의 중심에는 BeanDefinition이라는 추상화가 있다.

### BeanDefinition이란

![스크린샷 2022-07-11 22.56.56](https://tva1.sinaimg.cn/large/e6c9d24egy1h43bz8by0yj20w20a2mxq.jpg)빈 설정 메타 정보.

BeanDefinition이란 쉽게 말하면 역할과 구현을 개념적으로 나눈 것인데 XML(`<bean>`)이든 자바 코드(`@Bean`)든 BeanDefinition을 만들면 스프링 컨테이너는 이를 몰라도 되며 BeanDefinition만 알면된다.

스프링 컨테이너는 이 메타 정보를 기반으로 스프링 빈을 생성한다.![스크린샷 2022-07-11 23.07.03](https://tva1.sinaimg.cn/large/e6c9d24egy1h43c9okqkuj216g0oago4.jpg)

* AnnotationConfigApplicationContetx는 AnnotatedBeanDefinitionReader를 통해 Appconfig.class를 읽고 BeanDefinition을 생성한다.

* Xml, XXX 또한 이와 같이 BeanDefinition을 생성한다.



## BeanDefinition 정보

* BeanClassName - 생성할 빈의 클래스명(자바 설정처럼 팩토리 역할의 빈을 사용하면 없음)
* factoryBeanName - 팩토리 역할의 빈을 사용할 경우 이름 ex)AppConfig
* factoryMethodName - 빈을 생성할 팩토리 메서드 지정, 예)memberService

* Scope - 싱글톤(기본값)
* lazyInit - 스프링 컨테이너를 생성할 때 빈을 생성하는 것이 아니라, 실제 빈을 사용할 때까지 최대한 생성을 지연처리 하는지의 여부
* InitMethodName - 빈을 생성하고 의존관계를 적용한 뒤에 호출되는 초기화 메서드 명
* DestroyMethodName - 빈의 생명주기가 끝나서 제거하기 직전에 호출되는 메서드 명
* Constructor arguments, Properties - 의존관계 주입에서 사용한다.(자바 설정처럼 팩토리 역할의 빈을 사용하면 없음)

