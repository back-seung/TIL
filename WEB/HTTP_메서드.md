## HTTP 메서드

> HTTP API를 만들어 보며 HTTP-Method에 대해서 알아본다.



### HTTP 메서드 - 주요 메서드

| 메서드 | 설명                                     |
| ------ | ---------------------------------------- |
| GET    | 리소스를 조회                            |
| POST   | 요청 데이터 처리, 주로 등록에 사용한다.  |
| PUT    | 리소스를 대체, 해당 리소스가 없으면 생성 |
| PATCH  | 리소스 부분 변경                         |
| DELETE | 리소스 삭제                              |



#### GET

GET은 리소스를 조회하는 데 사용된다. 서버에 전달하고 싶은 데이터는 쿼리 파라미터, 쿼리 스트링을 통해서 전달한다.

최근 스펙에서는 GET에서 메시지 바디를 사용해서 데이터를 전달을 허용하고 있지만, 지원하지 않는 곳이 많아서 권장하지 않는다.

1. Request

```http
GET/members/100 HTTP/1.1
Host: localhost:8080
```

2. Response

```json
{
    "username": "young",
    "age": 20
}
```



#### POST

POST는 주로 새 리소스를 생성하는데 사용하거나 요청 데이터를 처리하는데 사용한다.

POST는 클라이언트에서 메시지 바디를 통해 서버로 데이터를 요청하면 서버가 이 데이터를 처리해달라고 하는 것이다.

메시지 바디(요청 메시지)를 통해 들어온 데이터를 처리하는 모든 기능을 전부 수행한다.

1. Request

```http
POST/members HTTP/1.1
Content-Type: application/json

{
	"username": "young",
	"age": 20	
}
```

2. Response 

```http
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 34
Location: /members/100

{
	"username": "young",
	"age": 20	
}
```



#### PUT

PUT은 리소스가 있으면 대체하고 없으면 생성하는데 이 때 대체는 완전한 대체이다(기존의 것을 날려버리고 새로 생성).

**중요한 점은 클라이언트가 리소스의 전체 위치를 알고 있다는 것이다.** POST는 리소스 위치를 모르지만 PUT은 위치를 알기 떄문에 URI를 지정한다는 것이 POST와의 큰 차이이다.

1. Request - 리소스의 위치 `/members/100`을 알고 있다.

```http
PUT /members/100 HTTP/1.1
Content-Type: application/json

{
	"username": "old",
	"age": 20	
}
```

2. Response - 존재시 대체, 없으면 생성

```json
{
	"username": "old",
	"age": 20	
}
```

* 리소스를 완전히 대체한다는 것은 무엇일까? 기존에 `members/100`에는 아래와 같은 정보가 있다고 가정한다.

```json
{
	"username": "old",
	"age": 20	
}
```

* `members/100`에 아래와 같은 정보를 PUT 메서드로 보낸다.

```json
{
	"age": 50	
}
```

* 그럼 username필드는 놔두고 age만 바뀌게 되는 것이 아니라 `members/100`위치의 기존의 정보는 모두 사라지고 위 정보로 모두 덮어쓰이게 된다. 



#### PATCH

PUT은 부분변경이 리소스를 완전히 대체하는 반면에 PATCH는 부분 변경을 위한 것이다. 가끔 PATCH를 지원하지 않는 경우가 있는데 이럴 때는 무적인 POST를 사용해버리자

```json
{
	"username": "young",
	"age": 20	
}
```

* 기존 위와 같은 리소스가 담겨있는 `members/100`에 age를 변경하는 데이터를 PATCH 메서드로 보낸다.

```json
{
	"age": 50	
}
```

* 그렇게 되면 age만 50으로 변경되고 PUT과 달리 username필드가 삭제되지 않고 age 필드의 값만 **부분 변경**된다.

```json
{
	"username": "young",
	"age": 50	
}
```



#### DELETE

DELETE는 말 그대로 리소스를 제거하는 역할을 한다.



### HTTP 메서드 - 기타 메서드

| 메서드  | 설명                                                         |
| ------- | ------------------------------------------------------------ |
| HEAD    | GET과 동일 But, 메시지 부분을 제외하고 상태 줄과 헤더만 반환 |
| OPTIONS | 대상 리소스에 대한 통신 가능 옵션(메서드)을 설명(주로 CORS에서 사용) |
| CONNECT | 대상 자원으로 식별되는 서버에 대한 터널을 설정               |
| TRACE   | 대상 리소스에 대한 경로를 따라 메시지 루프백 테스트를 수행   |



### 만들어보자

> **요구사항** - 회원 정보 관리 API를 만들어라
>
> * 회원 목록 조회
> * 회원 조회
> * 회원 등록
> * 회원 수정
> * 회원 삭제



#### API URI 설계

* 회원 목록 조회 `/read-member-list`
* 회원 조회 `/read-memeber-by-id`
* 회원 등록 `/create-member`
* 회원 수정 `/update-member`
* 회원 삭제 `/delete-member`

먼저 각 기능에 맞게 URI를 설계해야하기 때문에 위 처럼 URI를 설계했다(뿌-듯)

하지만 URI를 설계할 때 가장 중요한 것은 **리소스를 식별하는 것이다.**

리소스는 무엇일까? 회원을 등록, 수정, 삭제하는 것일까? 아니다 회원 그 자체가 바로 리소스인 것이다.

이제 근본적인 리소스의 의미를 알았으니 우리는 **모든 것을 배제하고 회원이라는 리소스만 식별하면 된다. -> 회원 리소스를  URI 매핑**

회원 리소스를 URI로 아래와 같이 매핑했다.

* 회원 목록 조회 `/members`
* 회원 조회 `members/{id}`
* 회원 등록 `members/{id}`
* 회원 수정 `members/{id}`
* 회원 삭제 `members/{id}`

#### 이제 해결할 문제는 같은 URI를 구분하는 것이다. 

* URI는 리소스만 식별해야 한다.
* 리소스와 해당 리소스를 대상으로 하는 행위를 분리해야 한다.
  * 리소스 - 회원
  * 행위 : 조회, 등록, 수정, 삭제
* `리소스 - 명사`, `행위 - 동사`

* 위 URI를 HTTP 메서드에 따라서 구분하면 된다

### HTTP 메서드의 속성 

| HTTP 메서드 | RFC      | 요청에 Body가 있는가 | 응답에 Body가 있는가 | 안전 | 멱등(Idempotent) | 캐시 가능 |
| ----------- | -------- | -------------------- | -------------------- | ---- | ---------------- | --------- |
| GET         | RFC 7231 | X                    | O                    | O    | O                | O         |
| HEAD        | RFC 7231 | X                    | X                    | O    | O                | O         |
| POST        | RFC 7231 | O                    | O                    | X    | X                | O         |
| PUT         | RFC 7231 | O                    | O                    | X    | O                | X         |
| DELETE      | RFC 7231 | X                    | O                    | X    | O                | X         |
| CONNECT     | RFC 7231 | O                    | O                    | X    | X                | X         |
| OPTIONS     | RFC 7231 | 선택사항             | O                    | O    | O                | X         |
| TRACE       | RFC 7231 | X                    | O                    | O    | O                | X         |
| PATCH       | RFC 5789 | O                    | O                    | X    | X                | O         |



#### 안전 - Safe

* 호출해도 리소스를 변경하지 않는다.

#### 멱등 - Iempotent

* f(f(x)) = f(x)
* 한 번 호출하든 두 번 호출하든, 100번 호출하든 결과는 똑같다.
* 멱등 메서드
  * GET -몇 번을 조회하든 같은 결과가 조회된다.
  * PUT - 결과를 대체한다. 따라서 같은 요청을 여러번해도 최종 결과는 같다.
  * DELETE - 결과를 삭제한다. 같은 요청을 여러번해도 삭제된 결과는 같다.
  * POST - **멱등이 아니다.** 두 번 호출하면 같은 결제가 중복해서 발생할 수 있다.
* 멱등은 외부 요인으로 중간에 리소스가 변경되는 것 까지는 고려하지 않는다.

#### 캐시 가능

* 응답 결과 리소스를 캐시해서 사용해도 되는가?
* GET, HEAD, POST, PATCH 캐시 가능
* 실제로는 GET, HEAD 정도만 캐시로 사용
  * POST, PATCH는 본문 내용까지 캐시 키로 고려해야 하는데 구현이 쉽지 않다.



## HTTP 메서드 활용

### 클라이언트에서 서버로 데이터를 전달하는 방법은 크게 2가지

* 쿼리 파라미터를 통한 데이터 전송
  * GET
  * 주로 정렬 필터(검색어)
* 메시지 바디를 통한 데이터 전송
  * POST, PUT, PATCH
  * 회원 가입, 상품 주문, 리소스 등록, 리소스 변경

#### 정적 데이터 조회 

* 쿼리 파라미터 미사용

```http
GET /static/star.jpg HTTP/1.1
Host: localhost:8080

...

HTTP/1.1 200 OK
Content-Type: image/jpeg
Content-Length: 34012

lksdafjfgaslkjglkj23l4kj23l5n1jhk45b3kjh2345ghj325v234jk5v23
```

* 정리
  * 이미지나 정적 텍스트 문서
  * 조회는 GET사용
  * 정적 데이터는 일반적으로 쿼리 파라미터 없이 리소스 경로로 단순하게 조회 가능

#### 동적 데이터 조회

* 쿼리 파라미터 사용 - 쿼리 파라미터를 기반으로 정렬 필터해서 결과를 동적으로 생성

```
https://www.google.com/search?q=hello&hl=ko
```

```http
GET /search?q=hello&hl=ko HTTP/1.1
Host: www.google.com
```

* 정리
  * 주로 검색, 게시판 목록에서 정렬 필터(검색어)
  * 조회 조건을 줄여주는 필터, 조회 결과를 정렬하는 정렬 조건에 주로 사용
  * 조회는 GET 사용
  * GET은 쿼리 파라미터 사용해서 데이터를 전달

#### HTML Form 데이터 전송 

* POST 전송 - 저장

```html
<form action="/save" method="post">
    <input type="text" name="username" value="kim" />
    <input type="text" name="age" value="20" />
    <button type="submit">전송</button>
</form>
```

* 웹 브라우저가 생성한 요청  HTTP 메시지

```http
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=kim&age=20
```

* 주로 회원가입, 상품 주문, 데이터 변경에 사용

#### HTTP API 데이터 전송

```http
POST /members HTTP/1.1
Content-Type: application/json

{
	"username": "young",
	"age": 20
}
```

* 정리
  * 서버 to 서버
    * 백엔드 시스템 통신
  * 앱 클라이언트
    * 아이폰, 안드로이드
  * 웹 클라이언트
    * HTML에서 Form 전송 대신 자바 스크립트를 통한 통신에 사용(Ajax)
    * 예) React, Vue.js 같은 웹 클라이언트와 API 통신
  * POST, PUT, PATCH - 메시지 바디를 통해 데이터 전송
  * GET - 조회, 쿼리 파라미터로 데이터 전달
  * Content-Type - application/json을 주로 사용(사실상 표준)
    * TEXT, XML, JSON 등등