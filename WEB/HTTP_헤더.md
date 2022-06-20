# HTTP 헤더 1 & 2

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

