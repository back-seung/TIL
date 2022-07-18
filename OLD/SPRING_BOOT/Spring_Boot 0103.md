## 0103 Spring_Boot 

> * 12시에 일어나서 면접 과제를 풀었다( C#을 응용한 간단한 DB연동 후 CRUD)
* 주민등록증 재발급 수령하기
* 운동하기
* 스프링부트 공부 (✅)

## 이제는 ?



> * Application 생성(`@SpringBootApplication`)
* HelloController, HelloControllerTest 클래스 생성, 어노테이션 파악 및 실습
* 진도 계속 나아가기

## SpringApplication



`@SpringBootApplication` : 
* 스프링부트의 자동설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정.
* 해당 어노테이션이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치시킨다.

SpringApplication.run ? : 
* 내장 WAS를 실행시키는 역할.
  
## HelloController    



`@RestController` : 
* 컨트롤러를 JSON으로 반환. 이전에는`@ResponseBody`를 각 메서드마다 선언한 것을 한번에 사용하는 의미.

`@GetMapping` :
* HTTP의 GET방식 요청을 받는 API를 생성함. 
* 이전에는 `RequestMapping(value="/hello" method = RequestMethod.GET)`으로 사용됐었음.

## HelloControllerTest



`@RunWith(SpringRunner.class)` :
* SpringRunner라는 스프링 실행자를 사용.
* 스프링부트 테스트와 JUnit 사이의 연결자 역할을 함(JUnit에 내장된 실행자 외에 다른 실행자 실행)

`@WebMvcTest(controllers = HelloController.class)` : 
* 여러 테스트 어노테이션중 Web에 집중할 수 있는 어노테이션.
* 선언하면 `@Controller`, `@ControllerAdvice` 등 사용 가능. 단, `@Service`, `@Repository`, `@Component`는 X.

`@Autowired` :
* 스프링 Bean을 주입 받음.

`MockMvc` :
* Web API 테스트 시 사용, MVC 테스트 시작점, HTTP GET, POST에 대한 API 테스트 가능.

`mvc.perform(get("/hello"))` : 
* MockMVC를 통해 /hello 주소로 GET 요청을 받음

`.andExpect(status().isOk())` :
* 결과 검증 Http Header의 Status 검증 (200, 404, 500 등) ▶ 200인지 아닌지를 검사함

`.andExpect(content().string(hello))` :
* 응답 본문의 내용 검증, hello를 리턴하는데 이 값이 맞는지 검증함.



## Controller 실행 결과



```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HelloController {

    @GetMapping("/hello") 
    public String hello() {
        return "hello";
    }
}

```
![](https://images.velog.io/images/seunghan-baek/post/486436bc-4d2e-425b-a9b9-89a45d6341b9/image.png)
> 깔끔하게 hello 출력완료!



## Lombok
* 롬복은 `Getter` `Setter` `생성자` `toString`을 어노테이션으로 자동 생성해준다.
* Spring Boot에서 롬복을 사용하는 순서는 아래와 같다.
> 1. build.gradle `compile('org.projectlombok:lombok')` 작성
> 2.  plugins -> marketplace(롬복 설치)
> 3. Build/Compiler/Annotation Processors -> Enable annotation processing 체크



### DTO를 생성한다.
```java
// MAIN
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public class HelloResponseDto {

    private final String name;
    private final int amount;
}
```
`@Getter` :
 * GET 메소드 생성 역할

`@RequiredArgsConstructor` :
 * final 필드가 포함된 생성자를 생성해줌.

```java
 //TEST
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트(){
        // given
        String name = "test";
        int amount = 1000;

        // when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        // then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
```
 `@assertThat` :
 * 테스트 검증 라이브러리의 검증 메소드.
 * 메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어 사용 가능

`isEqualTo` :
 * 동등 비교 메소드
 * assertThat에 있는 값과 비교해서 같을때만 성공임.



> 오늘도 목포량 만큼 열심히 공부했다. 내일부터는 `JPA- 자바 표준ORM`을 공부할 것이다.