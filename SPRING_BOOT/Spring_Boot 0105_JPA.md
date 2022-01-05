## 0105 Spring_Boot_JPA

* 오늘은 취업 상담을 받았다 (자기소개서 작성 요령)

* 오늘은 JPA를 공부할 것이다.

  

## ORM과 JPA

* ORM(Object-Realational-Mapping)

  * 객체와 관계형 데이터베이스의 데이터를 자동으로 매핑해주는 것이다. 
  * SQL 쿼리가 아닌 메소드로 데이터를 조작한다. 때문에 개발자가 객체지향 패러다임에 맞춰 생산성있는 개발을 할 수 있다.

* JPA

  * 자바 ORM 기술에 대한 API 표준 명세
  * 다양한 객체 모델링을 DB로 구현할 수 없어 웹 애플리케이션 개발은 점점 데이터 베이스 모델링에만 집중되었다. 이를 해결하기 위해 JPA가 등장했다.
  * **객체지향 프로그래밍 언어와 관계형 DB의 패러다임을 중간에서 일치를 시켜주기 위한 기술**이다.
  * Spring에서 사용하기 위해선 `Spring-Data-Jpa` 를 사용한다.

  

## 도메인

> 게시글, 댓글, 회원 , 정산, 결제 등 소프트웨어에 대한 요구사항 || 문제 영역.

`@Getter` : lombok의 어노테이션

* 클래스내의 모든 필드 Getter 메소드 생성

``@NoArgsConstructor` : lombok의 어노테이션

* 기본 생성자 자동 추가

`@Builer` : lombok의 어노테이션

* 필더 패턴 클래스를 생성함
* Setter를 사용하면 해당 필드가 어디서 어떻게 바뀌었는지 확인이 쉽지 않다. 그렇기 때문에 생성자를 통해 값을 충족시킨뒤 DB에 삽입시킨다. 이를 Builder 클래스로 활용한다.

`@Entity` : JPA의 어노테이션

* 테이블과 직접 연결될 클래스임을 명시
* Entity는 서비스, 비즈니스 로직들을 기준으로 동작하기 때문에 여러 클래스에 영향을 끼친다. 하지만 Request 와 Response용 DTO는 View를 위한 클래스라 자주 변경이 필요하다.
* 그렇기 때문에 Entity는**데이터베이스와 맞닿은 핵심 클래스여서 Request / Response 클래스로 사용해서는 안된다, Contoller에서 쓸 Dto는 따로 분리해두자.**

`@id` : 

* Primary Key 필드를 나타낸다.

`@GeneratedValue` : 

* PK의 생성 규칙을 나타낸다. `Generation.Type.IDENTITY` 옵션을 추가해야 auto_increment가 된다. 

`@Column` : 데이터의 칼럼을 나타낸다. 굳이 선언은 하지 않아도 되지만, 추가적으로 필요한 옵션이 있는 경우 선언한다.Ex) 기본값 사이즈 및 타입 변경, 



## Repository

> 일반적으로 MyBatis 등에서 불리는 Dao와 유사하다. 하지만 JPA에서는 Repository라 불리며 `Interface`로 생성한다.

```java
public interface Repository_Name extends JpaRepository<Entity Class, PK_Type>
```

* 위와 같은 방식으로 `JpaRepository`를 상속하면 기본적인 CRUD가 자동으로 생성된다.
* 주의할 점은 Entity와 같은 위치에 함께 있어야 한다는 점이다.



## 오늘의 테스트 코드에서 익힐점

`@After` 

* 테스트가 끝날때마다 수행될 메소드를 지정한다. 테스트간 데이터끼리의 침범을 막기 위함이다.

`@repository.save`

* id == null ? insert : update 라고 생각하면 된다.

`@repository.findAll()`

* 테이블의 모든 데이터를 조회해온다.



### 테스트 코드 중 알게된 정보 && 발생한 문제에 관하여

* 테스트 코드의 root는 메인 패키지와 항상 같아야 한다. 처음 테스트 코드를 책으로 배우면서 직접 할 때마다 한 두번씩 오류가 났는데 모두 내가 파일이 있을 절대경로를 잘못 지정해놔서 생긴 오류였다. 때문에 각 클래스의 package 선언부를 모두 바꿔줘야 하는 불상사가 일어났다. 급한 마음을 버리고 꼼꼼하게 경로를 단디 잡아놔야겠다.

* 실행된 쿼리를 로그로보는 방법 

  > 1. src/main/resources ++ application.properties
  >
  > 2. `spring.jpa.show_sql = true` 작성 
  >
  >    2-1. 출력될 로그를 MySQL 버전으로 변경
  >
  >    `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect` 작성

* `@WebMvcTest` 와 `@SpringBootTest` + `@TestRestTemplate`의 차이

  > * JPA기능이 작동하지 않을 때 ▶ @WebMvcTest
  > * JPA까지 한번에 테스트 해야할 때 ▶ @SpringBootTest + @TestRestTemplate



## CRUD 적용하기

* 적용하기 전, Spring의 웹 계층을 파악해 볼 필요가 있다![SpringWebLayer](C:\Users\seung\OneDrive\바탕 화면\TIL\SPRING_BOOT\SpringWebLayer.png)

* 도메인에서 비즈니스 로직을 처리해야 하는 이유 ?

  > 도메인을 사용하면 서비스 메소드는 트랜잭션과 도메인 간의 순서만 보장한다.

  

## 스프링에서 Bean을 주입받는 방식들

* `@AutoWired`
* `@Setter`
* `생성자`  ⭐ 권장 >> lombok의 어노테이션`@RequiredArgsConstructor`로 생성











