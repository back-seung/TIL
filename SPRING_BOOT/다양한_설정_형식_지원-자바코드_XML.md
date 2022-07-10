## 다양한 설정 형식지원 - 자바 코드, XML

> 스프링 컨테이너는 다양한 형식의 설정 정보를 받아들일 수 있게 유연하게 설계되어 있다.



### 애노테이션 기반 자바 코드 설정 사용

* new AnnotationConfigApplicationContext(Appconfig.class);
* AnnotationConfigApplicationContext 클래스를 사용하면서 자바 코드로 된 설정 정보를 넘기면 된다.

### XML 설정 사용 

* 최근에는 XML 설정은 잘 사용하지 않는다
* GenericXmlApplicationContext를 사용하면서 xml 설정 파일을 넘기면 된다.

