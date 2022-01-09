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

   ```properties
   spring.security.oauth2.client.registration.google.client-id= 클라이언트 ID
   spring.security.oauth2.client.registration.google.client-secret= 클라이언트 SECRET
   spring.security.oauth2.client.registration.google.scope = profile, email
   ```

   

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

   ```properties
   # Project exclude paths
   /.gradle/
   .idea
   ++ application-oauth.properties
   
   ```

   

4. User Entity 생성

   

   > > User.class 
   >
   > ```java
   > package com.seung.booboo.springboot.domain.user;
   > 
   > import com.seung.booboo.springboot.domain.BaseTimeEntity;
   > import lombok.Builder;
   > import lombok.Getter;
   > import lombok.NoArgsConstructor;
   > 
   > import javax.persistence.*;
   > 
   > @Getter
   > @NoArgsConstructor
   > @Entity
   > public class User extends BaseTimeEntity {
   > 
   >     @Id
   >     @GeneratedValue(strategy = GenerationType.IDENTITY)
   >     private Long id;
   > 
   >     @Column(nullable = false)
   >     private String name;
   > 
   >     @Column(nullable = false)
   >     private String email;
   > 
   >     @Column
   >     private String picture;
   > 
   >     @Enumerated(EnumType.STRING)
   >     @Column
   >     private Role role;
   > 
   >     @Builder
   >     public User (Long id, String name, String email, String picture, Role role) {
   >         this.id = id;
   >         this.name = name;
   >         this.email = email;
   >         this.picture = picture;
   >         this.role = role;
   >     }
   > 
   >     public User update(String name, String picture) {
   >         this.name = name;
   >         this.picture = picture;
   > 
   >         return this;
   >     }
   > 
   >     public String getRoleKey() {
   >         return this.role.getKey();
   >     }
   > }
   > 
   > ```
   >
   > * 사용자의 권한을 관리하는 Enum 타입의 **Role**이 추가된다.
   >
   >   > Role.class
   >
   >   ```java
   >   package com.seung.booboo.springboot.domain.user;
   >   
   >   import lombok.Getter;
   >   import lombok.RequiredArgsConstructor;
   >   
   >   @Getter
   >   @RequiredArgsConstructor
   >   public enum Role {
   >   
   >       GUEST("ROLE_GUEST", "손님"),
   >       USER("ROLE_USER", "일반 사용자");
   >       // spring security에서는 권한 코드에 ROLE_이 앞에 있어야만 한다.
   >       private final String key;
   >       private final String title;
   >   }
   >   
   >   ```
   >
   > * 스프링 시큐리티에서는 권한 코드에 항상 **ROLE_** 앞에 있어야 한다.

   

5. User Repository 생성

   ```java
   package com.seung.booboo.springboot.domain.user;
   
   import org.springframework.data.jpa.repository.JpaRepository;
   
   import java.util.Optional;
   
   public interface UserRepository extends JpaRepository<User, Long> {
       Optional<User> findByEmail(String email);
   }
   ```

   > `Optional<User> findByEmail(String email)` - 사용자가 처음 가입 || 이미 등록된 상태인지 파악하기 위한 메소드

   

6. 스프링 시큐리티 설정 

   > 1. build.gradle 의존성 추가
   >
   >    : `compile('org.springframework.boot:spring-boot-starter-oauth2-client')` -  소셜 로그인 등 클라이언트 입장에서 소셜 기능 구현시 필요
   >
   > 2. config.path > SecurityConfig 클래스 생성
   >
   >    ```java
   >    package com.seung.booboo.springboot.config.auth;
   >    
   >    
   >    import com.seung.booboo.springboot.domain.user.Role;
   >    import lombok.RequiredArgsConstructor;
   >    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   >    import org.springframework.security.config.annotation.web.builders.WebSecurity;
   >    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
   >    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
   >    
   >    @RequiredArgsConstructor
   >    @EnableWebSecurity // 스프링 시큐리티 설정 활성화
   >    public class SecurityConfig extends WebSecurityConfigurerAdapter {
   >    
   >        private final CustomOAuth2UserService customOAuth2UserService;
   >    
   >        @Override
   >        public void configure(HttpSecurity http) throws Exception {
   >            http.csrf().disable().headers().frameOptions().disable() //h2-console 사용하기 위해 해당 옵션 disable
   >                    .and()
   >                    .authorizeRequests().antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
   >                    // authorizeRequests() > URL 별 권한 관리 설정 옵션 시작점, 선언되야만 andMatchers 사용가능
   >                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
   >                    // andMathcer > 권한관리 대상 지정 URL, HTTP 별로 관리 가능
   >                    .anyRequest().authenticated()
   >                    // 그 외 URL은 인증된(로그인된) 유저만 사용 가능
   >                    .and()
   >                    .logout().logoutSuccessUrl("/")
   >                    // 로그아웃 성공시 url
   >                    .and()
   >                    .oauth2Login()
   >                    // oauth2 로그인 기능 설정의 진입점
   >                    .userInfoEndpoint()
   >                    // 사용자 정보를 가져올 때의 설정 담당
   >                    .userService(customOAuth2UserService);
   >                    // 로그인 성공시 후속 조치를 진행할 인터페이스의 구현체 등록함.
   >        }
   >    }
   >    
   >    ```
   >    
   >    
   >
   > 3. CustomOAuth2UserService 클래스 생성 (로그인 이후 가져온 사용자의 정보를 기반으로 가입 및 정보수정 세션 저장등의 기능 지원)
   >
   >    ```java
   >    @RequiredArgsConstructor
   >    @Service
   >    public class CustomOAuth2UserService implements OAuth2UserService {
   >        private final UserRepository userRepository;
   >        private final HttpSession httpSession;
   >    
   >        @Override
   >        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
   >    
   >            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
   >            OAuth2User oAuth2User = delegate.loadUser(userRequest);
   >    
   >            String registrationId = userRequest.getClientRegistration().getRegistrationId();
   >            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
   >    
   >            OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
   >    
   >            User user = SaveOrUpdate(attributes);
   >            httpSession.setAttribute("user", new SessionUser(user));
   >    
   >            return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());
   >        }
   >    
   >        private User SaveOrUpdate(OAuthAttributes attributes) {
   >            User user = userRepository.findByEmail(attributes.getEmail())
   >                    .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
   >                    .orElse(attributes.toEntity());
   >    
   >            return userRepository.save(user);
   >        }
   >    }
   >    
   >    ```
   >    
   >    
   >    
   >    * `registraionId` : 로그인 진행중인 서비스를 구분하는 코드 (구글 || 네이버 || 카카오 등 어느 소셜의 로그인인지 구분)
   >    
   >    * `userNameAttributeName` : PK가 될 필드 값. (구글만 기본 코드인  `sub`를 제공, 네이버, 카카오는 지원하지 않음 )
   >    
   >    * `OAuthAttributes` 생성 : OAuthUserService를 통해 가져온 OAuthUser의 attribute를 담을 클래스. 
   >    
   >      ```java
   >      @Getter
   >      public class OAuthAttributes {
   >          private Map<String, Object> attributes;
   >          private String nameAttributeKey;
   >          private String name;
   >          private String email;
   >          private String picture;
   >      
   >          @Builder
   >          public OAuthAttributes (Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
   >              this.attributes = attributes;
   >              this.nameAttributeKey = nameAttributeKey;
   >              this.name = name;
   >              this.email = email;
   >              this.picture = picture;
   >          }
   >      
   >          public static OAuthAttributes of(String registraionId, String userNameAttributeName, Map<String, Object> attributes) {
   >              //OAuth2User 에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환
   >          return ofGoogle(userNameAttributeName, attributes);
   >      
   >          }
   >      
   >          private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
   >              return OAuthAttributes.builder()
   >                      .name((String) attributes.get("name"))
   >                      .email((String) attributes.get("email"))
   >                      .picture((String) attributes.get("picture"))
   >                      .attributes(attributes)
   >                      .nameAttributeKey(userNameAttributeName)
   >                      .build();
   >          }
   >      
   >          public User toEntity() {
   >              return User.builder()
   >                      .name(name)
   >                      .email(email)
   >                      .picture(picture)
   >                      .role(Role.GUEST)
   >                      .build();
   >          }
   >      }
   >      ```
   >    
   >    * `SessionUser` DTO 생성 
   >    
   >      ```java
   >      @Getter
   >      public class SessionUser implements Serializable {
   >          private String name;
   >          private String email;
   >          private String picture;
   >      
   >          public SessionUser(User user) {
   >              this.name = user.getName();
   >              this.email = user.getEmail();
   >              this.picture = user.getPicture();
   >          }
   >      }
   >      ```
   >    
   >      : Entity Class에는 언제 다른 Entity와 관계가 형성될지 모른다. 성능 이슈 및 부수 효과가 발생할 확률이 높아진다.
   >    
   >      : 때문에 SessionUser DTO를 새로 생성한다. **User클래스는 Entity이기 때문에 직렬화를 구현하지 않았기 때문이다.** 

   

7.  index.mustache 수정

    ```mustache
    .
    .
    .
    <h1>스프링 부트로 시작하는 웹 서비스</h1>
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6">
                    <a href="/posts/save" role="button" class="btn btn-primary">글 등록</a>
                    {{#userName}}
                        Logged in as : <span id="user">{{userName}}</span>
                        <a href="/logout">Logout</a>
                    {{/userName}}
                    {{^userName}}
                        <a href="/oauth2/authorization/google" class="btn btn-success active" role="button">Google Login</a>
                    {{/userName}}
                </div>
            </div>
        <br>
    ```

    `{{#userName}}` : true/false를 판단하는 머스테치에는 최종값을 넘겨줘야 한다. ▶ userName이 있다면 userName을 노출시킨다.

    `a href="/logout"` : spring security에서 제공하는 URL이다. 로그아웃 기능을 컨트롤러 구현 없이 지원한다.

    `{{^userName}}` : ▶ userName이 없다면 로그인 `Google Login`버튼을 노출시킨다.

    `a href="/oauth2/authorization/google"` : spring security에서 제공하는 로그인 URL. 마찬가지로 컨트롤러를 생성할 필요가 없다.

    

8.  indexController.class / index 메소드 수정

    * indexController.class

    ```java
        @GetMapping("/")
        public String index(Model model) {
            model.addAttribute("posts", postsService.findAllDesc());
    		SessionUser user = (SessionUser) httpSession.getAttribute("user");
            if(user != null) {
                model.addAttribute("userName", user.getName());
            }
            return "index";
        }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> config/oauth 패키지에 @LoginUser 어노테이션 생성 및 SessionUser 중복 제거
    
        @GetMapping("/")
        public String index(Model model, @LoginUser SessionUser user) {
            model.addAttribute("posts", postsService.findAllDesc());
            if(user != null) {
                model.addAttribute("userName", user.getName());
            }
            return "index";
    	}
    ```

    * LoginUser : index메소드 외에 다른 컨트롤러와 메소드에서 매번 Session값을 필요할 때 마다 직접 값을 가져와야 하는 불편을 개선하기 위해 생성

      ```java
      import java.lang.annotation.ElementType;
      import java.lang.annotation.Retention;
      import java.lang.annotation.RetentionPolicy;
      import java.lang.annotation.Target;
      
      @Target(ElementType.PARAMETER) // 어노테이션 생성될 수 있는 위치
      @Retention(RetentionPolicy.RUNTIME)
      public @interface LoginUser { // 어노테이션 클래스 지정.
      
      }
      ```

    

9.  LoginUserArgumentResolver 생성 : `HandlerMethodArgumentResolver` 인터페이스를 구현한 클래스.

    * 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의 구현체가 지정한 값으로 해당 메소드의 파라미터로 넘길 수 있다.

    * ```java
      @RequiredArgsConstructor
      @Component
      public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
      
          private final HttpSession httpSession;
      
      
          @Override
          public boolean supportsParameter(MethodParameter parameter) { // 파라미터에 @LoginUser 어노테이션이 붙어있고, 파라미터 클래스 타입이 SessionUser.class인 경우 true
              boolean isLoginUserAnnotaion = parameter.getParameterAnnotation(LoginUser.class) != null;
              boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
              return isLoginUserAnnotaion && isUserClass;
          }
      
          @Override
          public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
              // 파라미터에 전달할 객체 생성. 여기서는 세션에서 객체를 가져옴.
              return httpSession.getAttribute("user");
          }
      }
      
      ```

      > * supportsParameter() : 컨트롤러 메소드의 특정 파라미터를 지원하는지 판단(`@LoginUser` 어노테이션이 있고 파라미터 클래스 타입이 SessionUser.class인 경우 true)
      > * resolveArgument() : 파라미터에 전달할 객체를 생성(세션에서 객체를 가져온다)

    * 8 ~ 9 를 통해 `@LoginUser`를 사용하기 위한 환경 구성이 끝났다.

    

10.  WebConfig.class 생성 : 스프링에서 인식될 수 있도록 한다.

     ```java
     @RequiredArgsConstructor
     @Configuration
     public class WebConfig implements WebMvcConfigurer {
     
         private final LoginUserArgumentResolver loginUserArgumentResolver;
     
         @Override
         public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
             // HandlerMethodArgumentResolver는 항상 WebMvcConfigure의 addArgumentResolvers()를 통해 추가해야 함.
             // 다른 Handler-MethodArgumentResolver가 필요할 때 같은 방식으로 추가해주면 됨.
             argumentResolvers.add(loginUserArgumentResolver);
         }
     }
     
     ```



## 이제 세션이 필요할 때 어느 컨트롤러든 @LoginUser를 사용하여 정보를 가져올 수 있게 되었다.

> 스프링 부트도 처음인데 스프링 시큐리티도 처음이라 뭐가 뭔진 모르겠지만 일단 끝내자. 끝까지 마무리 해보고 책을 다시 봐도 안 늦는다.





