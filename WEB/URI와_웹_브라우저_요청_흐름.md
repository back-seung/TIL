## URI와 웹 브라우저 요청 흐름

* URI
* 웹 브라우저 요청 흐름

에 대해서 알아본다.



### URI(Uniform Resource Identifier)

* `U` niform : 리소스를 식별하는 통합된 방법.

* `R`esource : 자원, URI로 식별할 수 있는 모든 것(제한이 없다)
* `I`dentifier : 다른 항목과 구분하는데 필요한 정보



### URI와 비슷한 URL, URN

> 위 세개는 착각할 만한 이름을 가지고 있다. 쉽게 생각하면 URI에 URL, URN이 포함 되어 있다고 생각하면 쉽다.

* **URI**

  : URI는 로케이터, 이름 또는 둘 다 추가로 분류될 수 있다.

* **URL** (Uniform Resource Loacator)

  : 리소스가 있는 위치를 지정한다. (가변)

* **URN** (Uniform Resource Name)

  : 리소스에 이름을 부여한다. (불변)

* URL과 URN

| URL  | scheme   | authority          | path          | query           | fragment |
| ---- | -------- | ------------------ | ------------- | --------------- | -------- |
|      | `foo`:// | `example.com:8042` | `/over/there` | `?name=ferret`# | `nose`   |

| URN  | scheme | authority + path             | query | fragment |
| ---- | ------ | ---------------------------- | ----- | -------- |
|      | `urn`: | `example:animal:ferret:nose` |       |          |

> URN처럼 이름만 부여하여 리소스를 찾는 방법은 아직 보편화 되지 않아 찾기 힘들다. 따라서 거의 URL을 사용한다.



### URL 전체 문법과 URL 분석

##### `sheme://[userinfo@]host[:port][/path][?query][#fragment]` - 문법

##### `https://www.google.com/search?q=hello&hl=ko` - 분석

* 프로토콜(https) 
* 호스트명(www.google.com)
* 포트번호 (443)
* 패스(/search)
* 쿼리 파라미터(q=hello&hl=ko)



## 웹 브라우저 요청 흐름

> 위에서 분석했던 URL`https://www.google.com/search?q=hello&hl=ko` 을 직접 연결한다면? 순서는 다음과 같다.

1. 웹 브라우저는 DNS를 조회한다(IP 확인).

2. PORT를 확인한다. https이니 생략하면 -> `443` 

3. 찾아낸 IP와 PORT정보를 통해 `HTTP 요청 메세지`를 생성한다.

4. HTTP 요청 메세지는 다음과 같이 생겼다.
   ```http
   GET/search?q=hello&hl=ko HTTP/1.1
   Host: www.google.com
   ```

5. SOCKET 라이브러리를 통해 전달한다.

   1. HTTP 메시지가 TCP/IP 연결을 하고 전달한다.
   2. HTTP 메시지가 포함된 TCP/IP 패킷이 인터넷으로 전송된다.

6. 전송된 데이터를 서버가 받고 `HTTP 응답 메시지`를 패킷을 통해 보낸다.

```http
HTTP/1.1 200 OK
COntent-Type: text/html;charset=UTF-8
Content-Length: 3423

<html>
	<body>...</body>
</html>
```



7. 서버를 통해 응답 받은 패킷안의 HTTP 응답 메시지를 보면 HTML 데이터가 들어있고, 클라이언트는 이를 렌더링하여 결과를 볼 수 있다.



