# HTTP 헤더 -1-

## HTTP 헤더 - 일반 헤더

> header-field = header-name":" OWS field-name OWS (`OWS - 띄어쓰기 허용`)
>
>   
>
> * field-name - 대소문자 구분 없음
> * 헤더는 start-line 다음에 오게 된다.



### 헤더의 용도

* HTTP 전송에 필요한 모든 부가정보
  * 예) 메시지 바디의 내용, 바디의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 등등
* 표준 헤더가 너무 많음
* 필요시 임의의 헤더 추가 가능



### HTTP 헤더 분류 - RFC2616(과거)

> 과거에는 크게 4가지로 헤더를 분류했다.

1. **General** 헤더 - 요청, 응답 구분없이 메시지 전체에 적용되는 정보, 예) Connection: close
2. **Request** 헤더 - 요청 정보, 예) User-Agent: Mozilla/5.0(Macintosh; ..)
3. **Response** 헤더 - 응답 정보, 예) Server: Apache
4. **Entity** 헤더 - 엔티티 바디 정보, 예) Content-Type: text/html, Content-Length: 3423 



### HTTP 바디 - RFC2616(과거)

* 과거에는 메시지 본문(message body)은 엔티티 본문(entity body)을 전달하는데 사용한다고 정의했다.
* 엔티티 본문 = 요청이나 응답에서 전달할 실제 데이터(메시지 본문안에 엔티티 본문을 담아서 전송한다)
* **엔티티 헤더**는 **엔티티 본문**의 데이터를 해석할 수 있는 정보 제공
  * 데이터 유형(html, json), 데이터 길이, 압축 정보 등등



2014년에 RFC7230 ~ 7325가 등장하면서 RFC2616은 폐기 되었다.

### RFC 723x 등장

* 엔티티(Entity) -> 표현(Representation)
* Representation = representation Metadata + Representation Data
* 표현 = 표현 메타데이터 + 표현 데이터 



### HTTP 바디 - RFC7230

* 메시지 본문(message body)을 통해 표현 데이터를 전달
* 메시지 본문을 페이로드라고 한다.
* 표현은 요청이나 응답에서 전달할 실제 데이터
* 표현 헤더는 표현 데이터를 해석할 수 있는 정보 제공
  * 데이터 유형(html, json), 데이터 길이, 압축 정보 등등
* 참고로 표현 헤더는 표현 메타데이터와 페이로드 메시지를 명확하게 구분해야 하는데 이렇게 되면 복잡해져서 생략함.



## 표현?

* Content-Type: 표현 데이터 양식
* Content-Encoding: 표현 데이터의 압축 방식
* Content_Language: 표현 데이터의 자연 언어(한국어, 영어 등)
* Content_Length: 표현 데이터의 길이

* 표현 데이터는 요청, 응답 둘 다 사용



### Content-Type - 표현 데이터의 형식 설명

* 미디어 타입, 문자 인코딩과 관련된 정보가 들어간다.
* 예)
  * text/html; charset=UTF-8
  * application/json
  * image/png



### Content-Encoding - 표현 데이터 인코딩

* 표현 데이터를 압축하기 위해 사용
* 데이터를 전달하는 곳에서 압축 후 인코딩 헤더 추가
* 데이터를 읽는 쪽에서 인콛이 헤더의 정보로 압축 해제
* 예
  * gzip
  * deflate
  * ientity (압축 X)



### Content-Language - 표현 데이터의 자연 언어

* 표현 데이터의 자연 언어를 표현
* 예)
  * ko
  * en
  * en-US



### Content-Length - 표현 데이터의 길이

* 바이트 단위
* Transfer-Encoding(전송 코딩)을 사용하면 Content-Length를 사용하면 안됨





## 협상 헤더 - 컨텐츠 네고시에이션

> 클라이언트가 선호하는 표현 요청이다. 협상 헤더는 **요청**시에만 사용한다.

* Accept - 클라이언트가 선호하는 미디어 타입 전달

* Accept-Charset - 클라이언트가 선호하는 문자 인코딩

* Accept-Encoding - 클라이언트가 선호하는 압축 인코딩

* Accept-Language - 클라이언트가 선호하는 자연 언어 

  * Accept-Language 적용 전

    클라이언트가 한국어 브라우저를 사용하는데, 외국에 있는 이벤트 사이트에 들어간다. 이벤트 사이트의 서버는 다중 언어를 지원한다. 기본 언어는 영어이고 한국어도 지원한다. 하지만 서버는 클라이언트가 한국어인지 모르기 때문에 영어로 사이트를 제공한다.

  * Accept-Language 적용 후

    서버의 기본 언어는 영어지만 컨텐츠 네고시에이션이 적용됨에 따라 클라이언트에게 message body에 한국어를 제공한다.



### 협상과 우선순위 1 - Quality Values(q)

> * Accept-Language 복잡한 예시
>
>   기본 언어는 독일어, 영어도 지원하는 서버가 있다. 클라이언트는 한국어를 요청한다. 다중언어 지원 서버는 첫 번째는 독일어이고 다중 언어는 영어인데 둘 중 무엇을 지원하는지 **우선순위**를 지정해야 한다. 

* 우선순위는 0 ~ 1, 클수록 높은 우선순위(생략하면 1)

```http
GET /event
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
```

위 요청으로 보내게 되면 `ko > en-US > en`을 순서로 우선순위가 지정되어, ko와 en-US는 없으니 지원하는 언어인 en으로 페이지를 제공한다!



### 협상과 우선순위 2 - Quality Values(q)

```http
GET /event
Accept: text/*, text/plain, text/plain;format=flowed, */*
```

* 구체적인 것이 우선한다.

* `Accept: text/*, text/plain, text/plain;format=flowed, */*`

  1. text/plain;format=flowed

  2. text/plain

  3. text/*

  4. \*/\*

      

### 협상과 우선순위 3 - Quality Values(q)

* 구체적인 것을 기준으로 미디어 타입을 맞춘다.
* Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1,text/html;level=2;q=0.4, \*/\*;q=0.5

| Media Type        | Quality |
| ----------------- | ------- |
| text/html;level=1 | 1       |
| text/html         | 0.7     |
| text/plain        | 0.3     |
| image/jpeg        | 0.5     |
| text/html;level=2 | 0.4     |
| text/html;level=3 | 0.7     |



## 전송 방식

### 단순 전송 - Content-Length

한 번에 요청하고 한 번에 쭉 받는다.

```http
HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Length:3423

<html>
	<body>...</body>
</html>
```



### 압축 전송 - Content-Encoding

서버에서 메시지 바디를 압축을 하여 전송을 한다. 클라이언트에서는 Content-Encoding에 맞춰 압축을 해제할 수 있다.

```http
HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Encoding: gzip
Content-Length: 521

123kb12klj541321k25bkjbkj4l3b5k4j6blkj78jblk
```



### 분할 전송 - Transfer-Encoding

`chunked`는 덩어리로 쪼개서 보낸다는 뜻이다. 서버에서 5바이트, 5바이트씩 쪼개서 클라이언트로 보내진다. 마지막엔 0byte를 보내는데 이는 끝이라는 것을 표현한다. 

* **주의!** 분할 전송에서는 Content-Length를 포함하면 안된다. 왜냐면 Content-Length가 예상이 되지 않고 분할된 chunked마다 바이트의 길이가 있기 때문이다.

```http
HTTP/1.1 200 OK
Content-Type: text/plain
Transfer-Encoding: chunked

5
Hello
5
World
0
\r\n
```



### 범위 전송 - Range, Content-Range

범위를 지정하여 요청하면 서버에서 Content-Range에 맞춰 응답을 준다.  

* 요청

```http
GET /event
Range: bytes=1001-2000
```

* 응답

```http
HTTP/1.1 200 OK
Content-Type: text/plain
Content-Range: bytes 1001-2000 / 2000

qweqeqwe1441t123t134nm1eqw1
```



## 일반 정보

### From - 유저 에이전트의 이메일 정보

* 일반적으로 잘 사용되지는 않는다.
* 검색엔진 같은 곳에서 주로 사용한다.
* **요청**에서 사용

### Referer - 이전 웹 페이지 정보

* 현재 요청된 페이지의 이전 웹페이지 주소
* A -> B로 이동하는 경우 B를 요청할 때 `Referer: A`를 포함해서 요청
* Referer를 사용해서 유입 경로를 분석할 수 있다.
* **요청**에서 사용
* Referer는 Referrer의 오타이다 (ㅋㅋ?)

### User-Agent - 유저 에이전트 애플리케이션 정보

* user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36
* 클라이언트의 애플리케이션 정보(웹 브라우저 정보 등등)
* 통계 정보
* 어떤 종류의 브라우저에서 장애가 발생하는지 파악 가능하다.
* **요청**에서 사용

### Server - 요청을 처리하는 오리진 서버의 소프트웨어 정보

* ORIGIN ? : HTTP는 중간에 여러 PROXY서버를 거치게 된다. **실제 요청이 도착해서 HTTP 응답(표현 데이터를 만들어주는)을 해주는 마지막 서버를 ORIGIN 서버라고 한다**.

* Server: Apache/2.2.22(Debian)
* Server: nginx
* **응답**에서 사용

### Date - 메시지가 생성된 날짜

* Date: Tue, 15 Nov 1994 08:12:31 GMT
* 과거에는 요청에도 사용했지만 현재는 **응답**에서만 사용한다.



## 특별한 정보

### Host: 요청한 호스트 정보(도메인)

> Host가 없으면 무슨 상황이 발생할까? 다음과 같은 상황이 있다.
>
> * 클라이언트가 있다. IP는 100.100.100.1
>
> * 서버가 있다. IP는 200.200.200.2이다. 서버는 가상 호스트를 통해 여러 도메인을 한번에 처리할 수 있어 실제 애플리케이션이 각각의 도메인으로 구동될 수 있다. 그래서 아래와 같이 
>
>   1. aaa.com
>   2. bbb.com
>   3. ccc.com
>
>   의 도메인을 가지고 있다. 그런데 클라이언트가
>
>   ```http
>   GET /hello HTTP/1.1
>   ```
>
>   로 TCP/IP 요청을 보냈을 때 서버는 구분을 할 방법이 없다(**어느 도메인의 `/hello`로 들어갈지 모르게 된다**).
>
>   따라서 Host 헤더 필드를 필수값으로 지정하기로 개정했다.
>
>   ```http
>   GET /hello HTTP/1.1
>   Host: aaa.com
>   ```

* 요청에서 사용한다.
* 필수값이다.
* 하나의 서버가 여러 도메인을 처리해야 할 때 구분을 해준다.
* 하나의 IP 주소에 여러 도메인이 적용되어 있을 때

### Location - 페이지 리다이렉션

* 웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면 해당 Location으로 자동 이동한다(Redirect)
* 201 Created - Location 값은 요청에 의해 생성된 리소스의 URI를 뜻한다.
* 3xx (Redirection) : Location 값은 요청을 자동으로 리다이렉션하기 위한 대상 리소스를 뜻한다.



### Allow - 허용 가능한 HTTP 메서드 

> 서버에서 많이 구현되어있지 않지만 알고 있자. 상황을 보자
>
> 
>
> 클라이언트가 서버에 POST로 요청을 보냈다. 하지만 서버에서는 해당 리소스에 POST를 지원하지 않기 때문에 응답에서 HTTP 상태코드를 405로 반환했다. 이때 허용하는 HTTP 메서드를 명시해주어야 하는데 그 필드가 Allow이다.

* 405 (Method Not Allowed) 에서 **응답**에 포함해야한다.
* Allow: GET, HEAD, PUT (GET, HEAD, PUT만 허용합니다)



### Retry-After - 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간

* 503(Service Unavailable) : 서비스가 언제까지 불능인지 알려줄 수 있음
* Retry-After: Fri, 31 Dec 1999 23:59:59 GMT (날짜 표기)
* Retry-After: 120 (초단위 표기)



## 인증

### Authorization - 클라이언트의 인증 정보를 서버에 전달

* Authorization: Basic xxxxxxxxxxxxxxxx

### WWW-Authenticate - 리소스 접근시 인증 방법을 정의

> 클라이언트가 서버에 요청을 보냈는데 401 코드를 반환하면 이는 WWW-Authenticate 헤더를 추가해서 다시 요청을 하라는 의미이다.

* 리소스 접근시 필요한 인증 방법을 정의한다.
* 401 Unathorized 응답과 함께 사용한다.
* WWW-Authenticate: Newauth realm="apps", type=1, title="Login to \\"apps\\"", Basic realm="simple"



## 쿠키

### 쿠키 미사용시 예제

#### 상황

* 클라이언트는 처음 welcome 페이지에 접속한다.

```http
GET /welcome HTTP/1.1
```

* 서버는 환영한다.

```http
HTTP/1.1 200 OK

안녕하세요 손님
```

* 클라이언트는 로그인을 한다.

```http
POST /login HTTP/1.1
user=홍길동
```

* 서버는 응답한다(200).

```http
HTTP/1.1 200 OK

홍길동님이 로그인했습니다.
```

* 클라이언트가 로그인 후 welcome 페이지에 접속한다.

```http
GET /welcome HTTP/1.1
```

* 아쉽게도 서버는 클라이언트(홍길동)를 기억하지 못한다.

```http
HTTP/1.1 200 OK

안녕하세요 #손님#
```

> 서버는 클라이언트가 보낸 요청인지 아닌지 구분할 수 있는 방법이 없다. 요청에 응답하면 상태를 유지하지 않고, 이전 요청을 기억하지 못하기 때문이다.(무상태 프로토콜)

#### 대안 - 문제

* 모든 요청에 사용자 정보를 포함한다?

```http
GET /welcome?user=홍길동 HTTP/1.1
```

* 서버는 포함된 정보를 통해 클라이언트(홍길동)을 기억한다.

```http
HTTP/1.1 200 OK

안녕하세요 #홍길동님#
```

위 대안은 보안, 개발(사용자 정보를 계속 전달하게끔 개발해야함) 등 여러 심각한 문제가 있다.



#### 대안 - 쿠키 사용

* 클라이언트가 정보와 함께 로그인 요청을 한다.

```http
POST /login HTTP/1.1
user=홍길동
```

* 서버는 홍길동이라는 이름을 세션에 저장하고 쿠키 헤더(`Set-Cookie`)를 생성해 응답한다.

```http
HTTP/1.1 200 OK
Set-Cookie: user=홍길동

#홍길동#님이 로그인했습니다.
```

웹 브라우저 내부에는 쿠키 저장소가 있는데 그 저장소에 user=홍길동이라는 정보를 저장소에 저장한다.

* 클라이언트는 로그인 이후 welcome 페이지 접근한다.

```http
GET /welcome HTTP/1.1
Cookie: user=홍길동
```

웹 브라우저는 서버에 요청을 보낼 때마다 쿠키 저장소를 확인하여 쿠키 헤더의 생성과 함께 요청한다.

* 서버는 웹 브라우저가 생성한 클라이언트의 쿠키 헤더(`Cookie`)를 확인하고 클라이언트의 정보를 기억한다.

```http
HTTP/1.1 200 OK

안녕하세요 #홍길동#님
```



### 쿠키 사용

> 쿠키는 모든 요청에 쿠키 정보를 자동으로 포함한다. 하지만 모든 요청에 쿠키를 보내는 것은 보안에 문제가 있을수 있다.

* 예) set-cookie: sessionId=abcde1234; expires=Sat, 26-Dec-2020 00:00:00 GMT; path=/; domain=google.com; Secure
* 사용처
  * 사용자 로그인 세션 관리
  * 광고 정보 트래킹
* 쿠키 정보는 항상 서버에 전송된다.
  * 네트워크 트래픽을 추가로 유발한다.
  * 따라서 최소한의 정보만 사용한다(sessionId, 인증 토큰)
  * 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹 스토리치(localStorage, sessionSotrage)참고
* 주의할 점
  * 보안에 민감한 데이터는 저장하면 안된다(비밀번호, 주민번호, 신용카드 번호 등)



### 쿠키 - 생명주기 Expires, max-age

#### Set-Cookie: **expires**=Sat, 26-Dec-2020 04:39:21 GMT

* 만료일이 되면 쿠키 삭제

#### Set-Cookie: **max-age**=3600(3600초)

* 0이나 음수를 지정하면 쿠키 삭제

* 세션 쿠키 - 만료 날짜를 생략하면 브라우저 종료시까지만 유지
* 영속 쿠기 - 만료 날짜를 입력하면 해당 날짜까지 유지



### 쿠키 - 도메인

#### 예) domain=example.org

도메인을 명시하게 되면, 명시한 문서 기준 도메인 + 서브 도메인 포함하여 전송한다.

* domain=exmaple.org는 물론이고 dev.example.org에도 쿠키 접근

#### 생략을 하게되면 현재 문서 기준 도메인만 적용

example.org에서 쿠키를 생성하고 domain 지정을 생략한다면

* example.org에서만 쿠키 접근을 하고, dev.example.org는 쿠키 미접근



### 쿠키 - 경로

#### 예) path=/home

* 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
* **일반적으로 path=/ 루트로 지정**

#### 예)

* path=/home 지정
* /home -> 가능
* /home/level1 -> 가능
* /home/level1/level2 -> 가능
* /hello -> 불가능 



### 쿠키 - 보안

#### Secure

* 쿠키는 http, https를 구분하지 않고 전송한다.
* Secure를 넣으면 https인 경우에만 쿠키를 전송한다.

#### HttpOnly

* XSS 공격 방지
* 자바스크립트에서 접근 불가능 (document.cookie)
* HTTP 전송에만 사용한다.

#### SameSite

* XSRF 공격 방지
* 요청 도메인과 쿠키에 설정된 도메인이 같은 경우에만 쿠키 전송

