# IO 패키지

> * 목표 : 자바의 입력과 출력 패키지에 대해 공부한다.



## 입력 스트림과 출력 스트림

> 프로그램이 출발지냐 도착지냐에 따라 스트림의 종류가 결정되는데, 데이터를 입력 받을 땐 **입력 스트림**, 데이터를 보낼 때에는 **출력 스트림**이라고 부른다. 입력과 출력의 기준은 항상 프로그램이다. 또한 스트림은 단방향이기 때문에 입력과 출력 스트림은 각각 따로 필요하다. 하나의 스트림으로 입/출력을 할 수는 없다.  
>
> JAVA의 기본적 데이터 입출력 API는 `java.io` 패키지에서 제공한다.



| java.io 패키지 주요 클래스                                   | 설명                                                  |
| ------------------------------------------------------------ | ----------------------------------------------------- |
| File                                                         | 파일 시스템의 파일 정보를 얻기 위한 클래스            |
| Console                                                      | 콘솔로부터 문자를 입출력하기 위한 클래스              |
| InputStream / OutputStream                                   | 바이트 단위 입출력을 위한 최상위 입출력 스트림 클래스 |
| FileInputStream / FileOutputStream<br />DataInputStream / DataOutputStream<br />ObjectInputStream / ObjectOutputStream<br />PrintStream<br />BufferedInputStream / BufferedWriter | 바이트 단위 입출력을 위한 하위 스트림 클래스          |
| Reader / Writer                                              | 문자 단위 입출력을 위한 최상위 입출력 스트림 클래스   |
| FileReader / FileWriter<br />InputStreamReader / OutputStreamReader<br />PrintWriter<br />BufferedReader / BufferedWriter | 문자 단위 입출력을 위한 하위 스트림 클래스            |

스트림 클래스는 크게 두 종류로 구분된다.

1. 바이트 기반 스트림 : 그림, 멀티미디어, 문자 등 모든 종류의 데이터를 받고 보낼 수 있다.
2. 문자 기반 스트림 : 문자만 받고 보낼 수 있도록 특화 되었다.

| 구분          | 바이트기반 스트림                     |                                         | 문자 기반 스트림            |                             |
| ------------- | ------------------------------------- | --------------------------------------- | --------------------------- | --------------------------- |
|               | **입력 스트림**                       | **출력 스트림**                         | **입력 스트림**             | **출력 스트림**             |
| 최상위 클래스 | InputStream                           | OutputStream                            | Reader                      | Writer                      |
| 최하위 클래스 | XXXInputStream<br />(FileInputStream) | XXXOutputStream<br />(FileOutputStream) | XXXReader<br />(FileReader) | XXXWriter<br />(FileWriter) |

* InputStream / OutputStream : 바이트 기반 입/출력 최상위 클래스. 이 클래스들을 상속받는 하위 클래스는 접미사로 InputStream || OutputStream이 붙는다.
* Reader / Writer : 문자 기반 입/출력 최상위 클래스. 이 클래스들을 상속받는 하위 클래스는 접미사로 Reader || Writer가 붙는다.



### InputStream

#### read() 메소드

#### read(byte[] b) 메소드

#### read(byte[]b, int off, int len) 메소드

#### close() 메소드



### OutputStream

#### write(int b) 메소드

#### write(byte[] b) 메소드

#### write(byte[] b, int off, int len) 메소드

#### flush()와 close() 메소드



### Reader

#### read() 메소드

#### read(char[] cbuf) 메소드

#### read(char[] cbuf, int off, int len) 메소드

#### close() 메소드 



### Writer

#### write(int c) 메소드

#### write(char[] cbuf) 메소드

#### write(char[]) cbuf, int off, int len) 메소드

#### write(String str)과 write(String str, int off, int len) 메소드



## 콘솔 입출력

### System.in 필드

### System.out 필드

### Console 클래스

### Scanner 클래스 



## 파일 입출력

### File 클래스

### FileInputStream

### FileOutputStream

### FileReader

### FileWriter



## 보조 스트림

### 문자 변환 스트림

#### InputStreamReader

#### OutputStreamReader



### 성능 향상 보조 스트림

#### BufferedInputStream과 BufferedReader

#### BufferedOutputStream과 BufferedWriter



### 기본 타입 입출력 보조 스트림



### 프린터 보조 스트림



### 객체 입출력 보조 스트림

#### ObjectInputStream, ObjectOutputStream

#### 직렬화가 가능한 클래스(Seriallizable)

#### serialVersionUID

#### writeObject()와 readObject() 메소드



## 네트워크 기초

### 서버와 클라이언트

### IP주소와 포트

### InetAddress로 IP주소 얻기



## TCP 네트워킹

### ServerSocket과 Socket의 용도

### ServerSocket 생성과 연결 수락

### Socket 생성과 연결 요청

### Socket 데이터 통신

### 스레드 병렬 처리



### 채팅 서버 구현

#### 서버 클래스 구조

#### startServer() 메소드

#### stopServer() 메소드

#### Client 클래스

#### UI 생성 코드



### 채팅 클라이언트 구현

#### 클라이언트 클래스 구조

#### startClient() 메소드

#### stopClient() 메소드

#### recieve() 메소드

#### send(String data) 메소드

#### UI 생성 코드



## UDP 네트워킹

### 발신자 구현

### 수신자 구현 
