# HTTP_기본

> 출처 : 김영한님의 "모든 개발자를 위한 HTTP 웹 기본 지식"

## 모든 것이 HTTP

> HTTP는 무엇을 할 수 있을까

* html, text
* image, 음성, 영상, 파일
* JSON, XML (API)
* 거의 모든 형태의 데이터를 전송할 수 있다.
* 서버간 데이터를 주고 받을 때도 HTTP를 사용한다.
  * TCP/IP를 직접 주고 받는 경우는 드물다.



### HTTP의 역사

> 우리에게 2.0, 3.0대신 1.1 버전이 가장 중요한 이유는 1.1 스펙에 대부분의 모든 기능이 구현되어 있고, 2.0, 3.0은 성능 개선 위주이다.

| 연도 | 버전       | 특징                                                         |
| ---- | ---------- | ------------------------------------------------------------ |
| 1991 | HTTP / 0.9 | GET 메서드만 지원, 헤더가 없음                               |
| 1996 | HTTP / 1.0 | 메서드, 헤더 추가                                            |
| 1997 | HTTP / 1.1 | 가장 많이 사용, 우리에게 가장 중요한 버전이다.<br />RFC2068(1997) -> RFC2616(1999) -> RFC7230 ~ 7235(2014)로 3번의 개정을 하였다. |
| 2015 | HTTP / 2.0 | 성능 개선                                                    |
|      | HTTP / 3.0 | 진행 중. 성능 개선, UDP 사용                                 |



## 클라이언트 서버 구조

* Request, Response 구조
* 클라이언트는 서버에 요청을 보내고, 응답을 대기한다.
* 서버가 요청에 대한 결과를 만들어서 응답한다.

이전에는 클라이언트, 서버라는 개념이 분리되지 않았었다. 이를 개념적으로 클라이언트와 서버를 분리했고, UI/UX는 클라이언트에 집어넣고, 데이터, 비즈니스 로직은 서버에 집어넣게 되면서 각각 독립적인 진화가 가능해졌다.



## Stateful, Stateless

### 상태유지 - Stateful

서버가 클라이언트의 이전 상태를 보존함

* 단점 : 중간에 서버가 장애가 나면 클라이언트가 처음부터 다시 요청을 보내야 한다.

### 무상태 - Stateless

서버가 클라이언트의 상태를 보존하지 않음

* 장점 : 서버 확장성이 높음(스케일 아웃 - 수평확장 유리)
* 단점 : 클라이언트가 추가 데이터를 전송해야함



### Stateless 실무 한계

* 모든 것을 무상태로 설계할 수 있는 경우가 있고, 없는 경우도 있다.
  * 무상태의 예 - 로그인이 필요 없는 단순 서비스 소개화면
  * 상태 유지의 예 - 로그인
* 로그인한 사용자의 경우 로그인 했다는 상태를 서버에 유지(주로 쿠키, 세션 등을 사용해서 상태를 유지)
* 상태 유지는 최소한만 사용



## 비연결성(Connectionless)

### 연결을 유지하는 모델

![Screen Shot 2022-06-19 at 18.08.41](https://tva1.sinaimg.cn/large/e6c9d24egy1h3do0hcqkrj20ka0bi74k.jpg)

각 클라이언트(1,2,3)가 Request(요청)을 보내면 서버는 이를 Response(응답)하면서 연결을 유지한다. 이렇게 됐을 때의 문제점은 연결된 클라이언트 중 아무런 작업도 하고 있지 않는것도 서버가 N:1의 연결을 하고 있기 때문의 자원을 효율적으로 사용하지 못한다는 것이다. 연결을 유지하지 않는 모델은 다음과 같다.

###  연결을 유지하지 않는 모델

![Screen Shot 2022-06-19 at 18.11.41](https://tva1.sinaimg.cn/large/e6c9d24egy1h3do3l1q3yj20n20dqwep.jpg)

각 클라이언트가 Request를 보내면 서버는 이에 Response만 하고 연결을 끊어버린다. 서버는 연결을 유지하지 않기 때문에 최소한의 자원으로 서버를 유지할 수 있다.



### 비연결성의 특징

* HTTP는 기본이 연결을 유지하지 않는 모델이다.
* 일반적으로 초 단위 위하의 빠른 속도로 응답한다.
* 1시간 동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 수십개 이하로 매우 작다.
  * 예) 웹 브라우저에서 계속 연속해서 검색 버튼을 누르지는 않는다.
* 서버 자원을 매우 효율적으로 사용할 수 있음



### 비연결성의 한계와 극복

* TCP/IP 연결을 매번 새로 맺어야한다(3 way handshake 시간이 추가된다.)
* 웹 브라우저로 사이트를 요청하면 HTML 자원뿐만 아니라 css, js, image 등 수많은 자원이 함께 다운로드 된다.
* 지금은 HTTP 지속연결(Persistant Connections)로 문제를 해결한다.
* HTTP/2, HTTP/3에서 더 많은 최적화





## HTTP 메시지

> HTTP 메시지의 구조는
>
> 1. start-line (시작라인)
> 2. header (헤더)
> 3. empty line (공백라인 - CRLF)
> 4. message body (바디)
>
> 로 구성되어 있다.

공식 스펙은 다음과 같다.

```http
HTTP-message 	= start-line
				 *( header-filed CRLF )
				 CRLF
				 [ message-body ]
```



### 시작 라인

시작라인(start-line)은 request 라인과 status 라인으로 구분될 수 있다.

* request-line (요청 메시지)

  ```http
  GET/search?q=hello&hl=ko HTTP/1.1
  Host:www.google.com
  ```

  *  method(`GET, POST 등`) SP(공백) request-target(`Path`) SP HTTP-Version CRLF(엔터)
  * HTTP-Method - GET, POST, PUT, DELETE 등이 있고 서버가 수행해야 할 동작을 지정한다.
  * Path - 요청 대상에는 보통 절대경로로 시작하고 `절대경로[?쿼리]`의 방식으로 이뤄진다.
  * HTTP 버전

* status-line (응답 메시지)

  ```http
  HTTP/1.1 200 OK
  Content-Type: text/html;charset=UTF-8
  Content-Length:3423
  
  <html>
  	<body>...</body>
  </html>
  ```

  * HTTP-Version SP status SP reason-phrase CRLF
  * HTTP 버전(`HTTP/1.1`)
  * HTTP 상태코드(`200`) - 요청의 성공, 실패를 나타낸다.
    * 200 - 성공
    * 400 - 클라이언트 요청 오류
    * 500 - 서버 내부 오류
  * 이유 문구(`OK`) - 사람이 이해할 수 있는 짧은 상태 코드 설명 글

### HTTP 헤더

```
# request-line
Host:www.google.com

# status-line
Content-Type: text/html;charset=UTF-8
Content-Length: 3423
```

* header-field = field-name ":" OWS field-value OWS (*`OWS란?` - 띄어쓰기를 허용한다는 뜻)
* field-name은 대소문자 구분이 없다. value는 대소문자를 구분한다.
* 헤더의 용도는 HTTP 전송에 필요한 모든 부가정보이다.
  * 예) 바디의 내용, 바디의 크기, 압축, 인증, 요청 클라이언트의 정보, 서버 Application 정보, 캐시 관리 정보 등, 메시지 바디 빼고 모든 메타데이터가 들어있다고 이해하면 된다.
* 표준 헤더 필드는 너무 많다.
* 임의의 헤더를 추가할 수 있다.(단, 클라이언트와 서버가 약속되어야 한다.)



### HTTP 메시지 바디

* 실제 전송할 데이터가 들어있다. (html, text, JSON, XML등 byte로 표현할 수 있는 모든 데이터를 전송할 수 있다) 