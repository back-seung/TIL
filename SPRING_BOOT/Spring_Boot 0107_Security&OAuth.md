## 0106 Spring_Boot_CRUD

* 오늘은 스프링 시큐리티와 OAuth 2.0으로 로그인 기능을 구현한다.

  

## 스프링 시큐리티란

* 막강한 인증과 인가 기능을 가진 프레임 워크.
* 스프링 기반의 APP에서 보안을 위한 표준이다.
* 인터셉터 / 필터 기반의 보안 기능을 구현하는 것보다 시큐리티를 통해 구현하는 것이 권장된다.



## OAuth 로그인 구현의 장점

> 1. 로그인 시 보안
> 2. 비밀번호 찾기
> 3. 비밀번호 변경
> 4. 회원정보 변겨
> 5. 회원가입 시 이메일 || 전화번호 인증

등의 기능들을 네이버, 페이스북, 구글, 등에 맡기면 되기 때문에, 서비스 개발에 집중할 수 있다.



### 사용 라이브러리

`spring-security-oauth2-autoconfigure` 는 스프링 1.5에서 사용되던 라이브러리인데 실습 중인 책에서는 `Spring Security OAuth2 Client`를 사용한다.

이유는 총 3가지로,

1. 스프링 1.5에서 사용되는 방식에 신규기능은 더는 없고 버그 수정 등의 기능만 추가되기 때문
2. 스프링 부트용 라이브러리가 출시됨
3. 신규 라이브러리의 경우 확장 포인트를 고려해서 설계됨



### 스프링 1.5와 2.0에서의 설정 차이점

> 1.5에서는 url 주소를 모두 명시해야 하지만 2.0에서는 클라이언트 인증 정보만 입력하면 된다. 
>
> 2.0에서는 enum(`CommonOAuthProvider`)으로 대체 되었기 때문이다.



## 구글 로그인 구현

1. (구글 서비스)[console.cloud.google.com]에 접속해서 순서에 따라 클라이언트 ID를 생성한다.
2. 프로젝트의 resources에 `application-oauth.properties` 생성 후 클라이언트 ID, 클라이언트 보안 비밀, **스코프**를 지정한다.



### 알게된 점

* profile, email의 스코프를 강제로 지정하는 이유

  > openid라는 스코프가 있으면 Open id Provider로 인식하기 때문이다.
  >
  > 이렇게 되면 OpenId Provider인 서비스와 그렇지 않은 서비스를 나눠서 각각 서비스를 만들어야 한다. 하나의 서비스로 사용하기 위해 openid를 제외한 나머지 스코프를 등록한다.

* 스프링 부트에서는 properties의 이름을 application-xxx.properties로 만들면 xxx라는 이름의 profile이 생성 되어, 이를 통해 관리 가능하다.

  > ex) profile = xxx
  >
  > 이런 방식으로 불러오면 해당 properties의 설정을 가져온다.



3. 클라이언트 ID및 보안 비밀은 노출되면 안되는 정보기 때문에 gitIgnore파일에 `application-oauth.properties`(클라이언트 id, 보안 비밀 작성 파일)항목을 작성해야 한다.



4.  User Entity 생성

   > * 사용자의 권한을 관리하는 Enum 타입의 **Role**이 추가된다.
   > * 스프링 시큐리티에서는 권한 코드에 항상 **ROLE_** 앞에 있어야 한다.

   

5. User Repository 생성

   > `Optional<User> findByEmail(String email)` - 사용자가 처음 가입 || 이미 등록된 상태인지 파악하기 위한 메소드

   

6. 스프링 시큐리티 설정 

   > 1. build.gradle 의존성 추가
   >
   >    : `compile('org.springframework.boot:spring-boot-starter-oauth2-client')` -  소셜 로그인 등 클라이언트 입장에서 소셜 기능 구현시 필요
   >
   >    
   >
   > 2. config.path > SecurityConfig 클래스 생성
   >
   >    * h2-console 사용을 위한 설정
   >    * URL 별 권한 관리 설정
   >    * 설정 외 URL에 대한 권한은 인증된 유저만 사용가능하게 설정.
   >    * 로그아웃 성공시 접속될 URL 설정
   >    * oauth2 로그인 기능 설정 
   >    * 사용자 정보를 가져올 떄의 설정
   >    * 로그인 성공시 후속 조치를 진행할 인터페이스 등록
   >
   >    
   >
   > 3. CustomOAuth2UserService 클래스 생성 (로그인 이후 가져온 사용자의 정보를 기반으로 가입 및 정보수정 세션 저장등의 기능 지원)
   >
   >    * `registraionId` : 로그인 진행중인 서비스를 구분하는 코드 (구글 || 네이버 || 카카오 등 어느 소셜의 로그인인지 구분)
   >
   >    * `userNameAttributeName`
   >    * 
   >    *  : PK가 될 필드 값. (구글만 기본 코드인  `sub`를 제공, 네이버, 카카오는 지원하지 않음 )
   >    * `OAuthAttributes` 생성 : OAuthUserService를 통해 가져온 OAuthUser의 attribute를 담을 클래스. (바로 다음 생성 예정)
   >    * `SessionUser` DTO 생성 















