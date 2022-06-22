# HTTP 헤더 -2-

## 캐시 기본 동작

### 캐시가 없을 때

> 클라이언트는 star.jpg를 총 2번 요청한다. 서버에서는 이 요청을 받고 star.jpg를 응답한다. 각 구문은 다음과 같다.

#### 첫 번째 요청

* 요청

```http
GET /stat.jpg
```

* 응답

```http
HTTP/1.1 200 OK
Content-Type: image/jpeg
Content-Length: 34012

jjk34h1lkj56hk12ljb43nj12b5jl1234hkb1j24b12lkj4b123kjl5b23jkh5bjhk321lb4jkh132b34jhk12b431jhk23b4j1hk24bjk1h23b4jhk12b456nm423b76nm234brjlkwqehfuiawhf1j23bv4jh1gk23f5v
```

이 때, `star.jpg`를 응답하는 서버는 HTTP-Header에 0.1M, HTTP-Body에 1.0M, 총 용량이 1.1M로 네트워크를 차지하며 응답값인 `star.jpg`를 웹 브라우저에 렌더링하고 표시한다. 



#### 두 번째 요청

* 요청

```http
GET /stat.jpg
```

* 응답

```http
HTTP/1.1 200 OK
Content-Type: image/jpeg
Content-Length: 34012

jjk34h1lkj56hk12ljb43nj12b5jl1234hkb1j24b12lkj4b123kjl5b23jkh5bjhk321lb4jkh132b34jhk12b431jhk23b4j1hk24bjk1h23b4jhk12b456nm423b76nm234brjlkwqehfuiawhf1j23bv4jh1gk23f5v
```

두 번째 응답에서도 첫 번째 응답처럼 1.1M의 네트워크를 사용하면서 웹 브라우저에 렌더링하고 표시한다. 

#### 캐시가 없을 때 - 정리

* 데이터가 변경되지 않아도 계속 네트워크를 통해서 데이터를 다운로드 받아야 한다.
* 인터넷 네트워크는 매우 느리고 비싸다.
* 브라우저 로딩 속도가 느리다.
* 느린 사용자 경험 



### 캐시 적용

* 첫 번째 요청

```http
GET /star.jpg
```

* 응답

```http
HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60
Content-Length: 34012

jjk34h1lkj56hk12ljb43nj12b5jl1234hkb1j24b12lkj4b123kjl5b23jkh5bjhk321lb4jkh132b34jhk12b431jhk23b4j1hk24bjk1h23b4jhk12b456nm423b76nm234brjlkwqehfuiawhf1j23bv4jh1gk23f5
```

이전과는 다르게 `cache-control`헤더가 추가된 것을 볼 수 있다. 헤더의 값에는 `max-age`가 있는데 이는 **캐시가 유효한 시간(초)**을 의미한다. 최초로 요청할 때, 서버는 `star.jpg`를 내려준다 웹 브라우저는 브라우저 캐시(캐시 저장소)에 이 응답 결과를 저장한다.

* 두 번째 요청

에서는 캐시를 먼저 조회한다. 60초 안에 유효하다면 캐시에서 응답 결과를 바로 가져온다(네트워크를 타지 않는다) 

* 세 번째 요청

60초가 초과 됐을때, 클라이언트는 다시 요청을 해야한다. 이렇게 되면 서버는 캐시를 덮어씌우면서 첫 번째 요청과 같이 1.1M의 네트워크를 사용하고 캐시 저장소에 응답 결과를 저장한다. 60초 동안 유효 시간이 생긴다.

#### 캐시 적용 - 정리

* 캐시 덕분에 캐시가 유효한 시간 동안 네트워크를 사용하지 않아도 된다.
* 비싼 네트워크 사용량을 줄일 수 있다.
* 브라우저 로딩 속도가 매우 빠르다
* 빠른 사용자 경험

### 캐시 시간 초과

* 캐시 유효 시간이 초과하면, 서버를 통해 데이터를 다시 조회하고 캐시를 갱신한다.
* 이때 다시 네트워크 다운로드가 발생한다.



## 검증 헤더와 조건부 요청

> 캐시 유효 시간이 초과해서 서버에 다시 요청하면 다음 두 가지 상황이 나타난다.

1. 서버에서 기존 데이터를 변경함
2. 서버에서 기존 데이터를 변경하지 않음



### 캐시 시간 초과

* 캐시 만료후에도 서버에서 데이터를 변경하지 않았다.
* 생각해보면 데이터를 전송하는 대신에 저장해 두었던 캐시를 재사용 할 수 있다.
* 단 클라이언트의 캐시데이터와 서버의 캐시 데이터가 같다는 사실을 확인할 수 있는 방법이 필요하다.



### 검증 헤더 추가 

#### 첫 번째 요청

* 요청

```http
GET /star.jpg
```

* 응답

```http
HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60
Last-Modified: 2020년 11월 10일 10:00:00
Content-Length: 34012

jjk34h1lkj56hk12ljb43nj12b5jl1234hkb1j24b12lkj4b123kjl5b23jkh5bjhk321lb4jkh132b34jhk12b431jhk23b4j1hk24bjk1h23b4jhk12b456nm423b76nm234brjlkwqehfuiawhf1j23bv4jh1gk23f5
```

`Last-Modified`를 추가하여 이 데이터가 마지막 수정된 시간을 추가하여 응답을 내려줄 수 있다.

#### 두 번째 요청

* 요청

```http
GET /star.jpg

if-modified-since: 2020년 11월 10일 10:00:00
```

* 응답

에서는 데이터 최종 수정일과 클라이언트의 `if-modified-since`를 서로 비교하여 데이터가 아직 수정되지 않았음을 검증할 수 있다. 수정되지 않은 데이터는 서버에서 다음과 같은 응답을 내린다.

```http
HTTP/1.1 304 Not Modified
Content-Type: image/jpeg
cache-control: max-age=60
Last-Modified: 2020년 11월 10일 10:00:00
Content-Length: 34012
```

서버에서는 304 Not Modified 즉, 수정되지 않았다는 상태코드와 함께 `cache-control`, `Last-Modified`헤더를 응답하는데 이 응답에는 **HTTP Body가 없다.** 그러면 바디를 제외한 헤더 부분만 응답하기 때문에 네트워크 부하가 줄어든다. 클라이언트에서는 상태 코드를 확인하고 캐시가 바뀌지 않았음을 인지하여 각 헤더를 갱신하며 캐시를 다시 세팅하고 브라우저 캐시를 조회하여 캐시를 사용하게 된다.



### 검증 헤더와 조건부 요청 - 정리

> * 검증 헤더 - `Last-Modified`
> * 조건부 요청 - `if-modified-since`

* 캐시 유효 시간이 초과해도, 서버의 데이터가 갱신되지 않으면 304 Not Modified + 헤더 메타 정보만 응답한다.(Body는 포함하지 않고)
* 클라이언트는 서버가 보낸 응답 헤더 정보롤 캐시의 메타 정보를 갱신한다
* 클라이언트는 캐시에 저장되어 있는 데이터를 재활용한다.
* 결과적으로 네트워크 다운로드가 발생하지만 용량이 적은 헤더 정보만 다운로드받으면 된다. 이는 굉장히 실용적인 해결책이다.



### ETag, If-None-Match

* Etag(Entity Tag)
* 캐시용 데이터에 임의의 고유한 버전 이름을 달아둠
  * 예) ETag: "v1.0", ETag: "a2jiodwjekki3"
* 데이터가 변경되면 이 이름을 바꾸어서 변경한다(Hash를 다시 생성)
  * 예)ETag: "aaaaa" -> ETag: "bbbbb"
* 진짜 단순하게 ETag만 보내서 같으면 유지, 다르면 다시 받기



### 검증헤더 추가

#### 첫 번째 요청 

* 요청

```http
GET /star.jpg

ETag: "aaaaa"
```

* 응답

```http
HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60
ETag: "aaaaa"
Content-Length: 34012

jjk34h1lkj56hk12ljb43nj12b5jl1234hkb1j24b12lkj4b123kjl5b23jkh5bjhk321lb4jkh132b34jhk12b431jhk23b4j1hk24bjk1h23b4jhk12b456nm423b76nm234brjlkwqehfuiawhf1j23bv4jh1gk23f5
```



#### 두 번째 요청 - 캐시 시간 초과

서버의 ETag가 이전과 바뀌지 않은 상태라고 가정한다.

* 요청

```http
GET /star.jpg

ETag: "aaaaa"
```

* 응답

```http
HTTP/1.1 304 Not Modified
Content-Type: image/jpeg
cache-control: max-age=60
ETag: "aaaaa"
Content-Length: 34012
```

데이터가 수정되지 않아 `304 Not Modified` 응답을 준다(Body 없이).



### ETag, If-None-Match 정리

*  진짜 단순하게 ETag만 서버에 보내서 같으면 유지, 다르면 다시 받는다.
* **캐시 제어 로직을 서버에서 완전히 관리**
* 클라이언트는 단순히 이 값을 서버에 제공(클라이언트는 캐시 메커니즘을 모른다)



## 캐시와 조건부 요청 헤더

### 캐시 제어 헤더

#### Cache-Control - 캐시 지시어(directives)

* `Cache-Control: max-age` - 캐시 유효시간, 초 단위이다.
* `Cache-Control: no-cache` - 데이터는 캐시해도 되지만, 항상 원(origin)서버에 검증하고 사용
* `Cache-Control: no-store` - 데이터에 민감한 정보가 있으므로 저장하면 안됨.(메모리에서 사용하고 최대한 빨리 삭제)

#### Cache-Control - 캐시 지시어(directives) - 기타

* `Cache-Control: public` - 응답이 public 캐시에 저장되어도 됨
* `Cache-Control: private` - 응답이 해당 사용자만을 위한 것이다. private 캐시에 저장해야 한다(기본값)
* `Cache-Control: s-maxage` - 프록시 캐시에만 적용되는 max-age
* `Age: 60(HTTP 헤더)` - 오리진 서버에서 응답 후 프록시 캐시 내에 머문 시간(초)

#### Cache-Control - 캐시 지시어(directives) - 확실한 캐시 무효화

* `Cache-Control: no-cache` - 데이터는 캐시해도 되지만, 항상 **원 서버에 검증**하고 사용(이름에 주의) 
* `Cache-Control: no-store` - 데이터에 민감한 정보가 있으므로 저장하면 안됨.(메모리에서 사용하고 최대한 빨리 삭제)
* `Cache-Control: must-revalidate` - 캐시 만료후 최초 조회시 원 서버에 검증해야한다. 원 서버 접근 실패시 반드시 오류가 발생해야 한다(504 - Gateway Timeout). `must-revalidate`는 캐시 유효시간이라면 캐시를 사용함
* `Pragma: no-cache` - HTTP 1.0 하위 호환

#### Pragma - 캐시 제어 (하위 호환)

* Pragma - no-cache
* HTTP 1.0 하위 호환 - 지금은 잘 사용하지 않는다.

#### Expires - 캐시 유효 기간 (하위 호환)

* expires: Mon, 01 Jan 1990 00:00:00 GWT



* 캐시 만료일을 정확한 날짜로 지정
* HTTP 1.0부터 사용
* 지금은 더 유연한 Cache-Control: max-age 사용을 권장한다.
* Cache-Control: max-age와 함께 사용시 Expires는 무시된다.



### 검증 헤더와 조건부 요청 헤더 

#### 검증 헤더 (Validator)

* `ETag`: "v1.0", ETag:  "asid93jkrh2l"
* `Last-Modified`: Thu, 04 Jun 2020 07:18:24 GMT

#### 조건부 요청 헤더

* `If-Match`, `If-None-Match`: ETag값 사용
* `If-Modified-Since`, `If-Unmodified-Since`: Last-Modified값 사용



## 프록시 캐시

### 원서버(Origin Server) 직접 접근

![Screen Shot 2022-06-22 at 23.55.01](https://tva1.sinaimg.cn/large/e6c9d24egy1h3hevtcez2j20d205wt8l.jpg)

한국에 있는 여러 클라이언트가 미국에 있는 서버에 접근한다고 가정할 때 실제로 데이터를 주는 **원소스가 있는 서버를 원서버라고 한다**. 직접 접근할 때, 요청을 받은 미국 서버에서 한국 클라이언트까지 이미지를 응답해주는 시간은 꽤나 길어질 것이다(강의에서는 500ms로 가정).



### 프록시 캐시 도입

#### 첫 번째 요청

![Screen Shot 2022-06-23 at 00.00.01](https://tva1.sinaimg.cn/large/e6c9d24egy1h3hf0x8nguj20cw06caa1.jpg)

미국의 원서버 입장에서는 응답이 느리니 한국 어딘가에 프록시 캐시 서버를 넣어 놓고 DNS 요청을 할 때 원서버에 직접 접근하는 것이 아니라 프록시 캐시 서버에 접근하게 한다. 웹 브라우저는 프록시 캐시 서버를  요청하여 응답 시간이 빨라지게 한다. 

* 웹브라우저, 로컬에 저장되는 개인 캐시를 `private 캐시`, 프록시 캐시 서버의 공용으로 사용할 수 있는 캐시를 `public 캐시`라고한다.



## 캐시 무효화

### Cache-Control - 확실한 캐시 무효화 응답

확실하게 캐시를 무효화하려면 다음 헤더들을 입력해야 한다. 보통 그래야 대응이 된다.

* Cache-Control: no-cache, no-store, must-revalidate
* Pragma: no-cache(HTTP 1.0 호환)



