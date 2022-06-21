# HTTP 헤더 -2-

## 캐시 기본 동작

### 캐시가 없을 때

> 클라이언트는 star.jpg를 총 2번 요청한다. 서버에서는 이 요청을 받고 star.jpg를 응답한다. 각 구문은 다음과 같다.

* 첫 번째 요청

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



* 두 번째 요청

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