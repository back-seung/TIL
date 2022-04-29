## 0109 Spring_Boot_MySQL Storage

* 오늘은 세션 저장소로 데이터베이스 사용하기(MySQL 사용)를 실습한다.




## 현재 실습 중 문제

* 애플리케이션을 재실행하면 로그인이 풀린다.

  > 내장 톰캣 메로리에 세션이 저장되었기 때문. (배포할 때 마다 톰캣이 재시작)

* 2대 이상의 서버에서 서비스 중이라면 톰캣마다 세션 동기화 설정을 해야함.

  > * 현업에서 세션 저장소를 선택하는 3가지
  >
  >   1. 톰캣 세션 사용
  >
  >      : 일반적으로 별다른 설정을 하지 않았을 때 선택됨
  >
  >   2. ✔ MySQL과 같은 DB를 세션 저장소로 사용
  >
  >      : 여러 WAS 간 공용세션을 사용할 수 있음.
  >
  >      : 성능상 이슈 발생 가능성 있음.
  >
  >      : 로그인 요청이 많이 없는 곳(사내 시스템 등)에서 사용
  >
  >   3. Redis, Memcached와 같은 메모리 DB를 사용
  >
  >      : B2C 서비스에서 가장 많이 사용
  >
  >      : 별도로 사용료를 지불해야 함.



## 1. build.gradle 의존성 추가 및 application.properties 수정

* `build.gradle`

```gradle
compile('org.springframework.session:spring-session-jdbc')
```

* `application.properties`

```properties
spring.session.store-type=jdbc
```



> 위 두가지를 설정하여 세션 저장소를 jdbc로 선택하게 되었다.
>
> 서버를 재시작하여 h2-console에 접속하면 SPRING_SESSION, SPRING_SESSION_ATTRIBUTES가 생성된 것을 확인할 수 있다.![image](https://user-images.githubusercontent.com/84169773/148674067-80351ec3-5267-4161-af16-f98dceb76cac.png)
>
> 현재는 스프링이 재실행 될 때, H2또한 재실행 되기 때문에 세션이 풀린다. 이후 **AWS로 배포하면 AWS의 RDS를 사용하여 세션이 풀리지 않는다.**



## 2. 네이버 로그인 구현

1. 네이버 오픈 API 어플리케이션 등록
   ![naverLogin](https://user-images.githubusercontent.com/84169773/148674098-ca24dd34-26a9-4795-aef8-dfa3be849249.png)

2. `application-oauth.properties` 수정

   ```properties
   # registration
   spring.security.oauth2.client.registration.naver.client-id=클라이언트_ID
   spring.security.oauth2.client.registration.naver.client-secret=클라이언트_SECRET
   spring.security.oauth2.client.registration.naver.redirect-uri={baseUrl}/{action}/oauth2/code/{registrationId}
   spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code
   spring.security.oauth2.client.registration.naver.scope=name,email,profile_image
   spring.security.oauth2.client.registration.naver.client-name=Naver
   
   # provider
   spring.security.oauth2.client.provider.naver.authorization-uri=https://nid.naver.com/oauth2.0/authorize
   spring.security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
   spring.security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
   spring.security.oauth2.client.provider.naver.user-name-attribute=response
   ```

   > 네이버에서는 스프링 시큐리티를 공식 지원하지 않기 때문에 common-OAuth2Provider에서 해주던 값들을 수동으로 입력해야함.

3. `OAuthAttributes` 네이버 관련 코드 추가

   ```java
       public static OAuthAttributes of(String registraionId, String userNameAttributeName, Map<String, Object> attributes) {
           //네이버인지 판단하는 코드.
           if("naver".equals(registraionId)) {
               return ofNaver("id", attributes);
           }
           return ofGoogle(userNameAttributeName, attributes);
   
       }
   	// 네이버 생성자
       private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
           Map<String, Object> response = (Map<String, Object>) attributes.get("response");
   
           return OAuthAttributes.builder()
                   .name((String) attributes.get("name"))
                   .email((String) attributes.get("email"))
                   .picture((String) attributes.get("profile_image"))
                   .attributes(response)
                   .nameAttributeKey(userNameAttributeName)
                   .build();
       }
   ```

4. `index.mustache` 네이버 로그인 버튼 추가

   ```mustache
   {{^userName}}
   	<a href="/oauth2/authorization/google" class="btn btn-success active" role="button">Google Login</a>
       <a href="/oauth2/authorization/naver" class="btn btn-secondary active" role="button">Naver Login</a>
   {{/userName}}
   ```

5. 성공 !
   ![image](https://user-images.githubusercontent.com/84169773/148674190-a7e0dfa7-1f6c-488e-96dc-4bc4316e50f0.png)



## 다음 실습

* 기존 테스트에 시큐리티 적용 
* AWS 적용
