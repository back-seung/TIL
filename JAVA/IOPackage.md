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

![inputStream](https://t1.daumcdn.net/cfile/tistory/9961443C5C1E016C2B)

​	출처 코딩팩토리  : https://coding-factory.tistory.com/281

> `InputStream`이란 **바이트 기반 입력 스트림의 최상위 추상클래스이다.** 모든 바이트 기반 입력 스트림은 이 클래스를 상속받는다.  
>
> InputStream은 읽기에 대한 다양한 추상 메소드를 정의해 두었다. 이 추상메소드를 오버라이딩하여 목적에 따라 데이터를 입력 받을 수 있다. 주요 메소드를 보자

| 리턴 타입 | 메소드         | 설명                                                         |
| --------- | -------------- | ------------------------------------------------------------ |
| int       | read()         | 입력 스트림으로 부터 1 바이트를 읽고 읽은 바이트를 리턴한다. |
| int       | read(byte[] b) | 입력 스트림으로부터 읽은 바이트들을 매개값으로 주어진 바이트 배열 b에 저장하고 실제로 읽은 바이트 수를 리턴한다. |
| void      | close()        | 사용한 시스템 자원을 반납하고 입력 스트림을 닫는다.          |

#### read() 메소드

> 입력 스트림으로부터 1바이트를 읽고, 4바이트 int타입으로 리턴한다. 따라서 리턴된 4바이트 중 끝의 1바이트에만 데이터가 들어있다. 입력 스트림에서 5개의 바이트가 들어온다면 다음과 같이 read() 메소드로 1바이트씩 5번(int) 읽을 수 있다.  
>
> 더 이상 입력 스트림으로부터 바이트를 읽을 수 없다면 read() 메소드는 -1을 리턴한다. 이것을 이용하면 읽을 수 있는 마지막 바이트까지 루프를 돌려 한 바이트씩 읽을 수 있다.

```java
InputStream is = new FileInputStream("C:/Users/test.jpg");
int readByte; 
while((readByte = is.read()) != -1) { ... }
```

  

#### read(byte[] b) 메소드

> 입력 스트림으로부터 매개값으로 주어진 바이트 배열의 길이만큼 바이트를 읽고 배열에 저장한다. 그리고 읽은 바이트 수를 리턴한다. 실제로 읽은 바이트 수가 배열의 길이보다 작을 경우 읽은 수만큼만 리턴한다. 길이가 3인 바이트 배열에 5개의 스트림이 들어온다면 2번 읽을 수 있다. raed(byte[] b) 메소드 또한 읽을 값이 없다면 -1을 리턴한다. 이를 통해 루프를 만들 수 있다.

```java
InputStream is = new FileInputStream("C:/Users/test.jpg");
int readByteNo;
byte[] readBytes = new byte[100];
while((readByteNo = is.read(readBytes)) != -1) { ... } 
```

위 코드는 많은 양의 바이트를 읽을 때 사용하면 좋다. 기존 read() 메소드보다 현저하게 루핑하는 횟수가 줄기 때문이다.

  

#### read(byte[]b, int off, int len) 메소드

> 입력 스트림으로부터 len개의 바이트만큼 읽고, 매개값으로 주어진 바이트 배열 b[off]부터 len개 까지 저장한다. 그리고 읽은 바이트 수인 len개를 리턴한다. 실제로 읽은 바이트 수가 len개 보다 작을 경우 읽은 수만큼 리턴한다.  
>
> read(byte[] b) 메소드와 차이점은 한 번에 읽어들이는 바이트 수를 len 매개값으로 조절할 수 있고, 배열에서 저장이 시작되는 인덱스를 지정할 수 있다는 점이다.

  

#### close() 메소드

> InputStream을 더이상 사용하지 않을 경우에는 close() 메소드를 통해 InputStream에서 사용했던 시스템 자원을 풀어준다.

  

### OutputStream

![outputStream](https://t1.daumcdn.net/cfile/tistory/99C0C7335C1E049323)

​	출처 코딩 팩토리 : https://coding-factory.tistory.com/281

> 바이트 기반 출력 스트림의 최상위 추상클래스이다.  
>
> 모든 바이트 기반 출력 스트림 클래스는 이 클래스를 상속 받아 기능을 재정의 한다. 주요 메소드를 보자



#### write(int b) 메소드

> 매개 변수로 주어진 b 값에서 끝에 있는 1바이트만 출력 스트림으로 보낸다. 매개 변수가 int 타입이므로 4바이트 모두를 보내는 것으로 오해할 수 있다.

```java
OutputStream os = new FileOutputStream("C:/test.txt");
byte[] data = "ABC".getBytes();
for (int i = 0; i < data.length; i++) {
  os.write(data[i]); // A, B, C를 하나씩 출력
}
```

  

#### write(byte[] b) 메소드

> 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 내보낸다.

```java
OutputStream os = new FileOutputStream("C:/test.txt");
byte[] data = "ABC".getBytes();
os.write(data);
```

  

#### write(byte[] b, int off, int len) 메소드

> b[off]부터 len개의 바이트를 출력 스트림으로 보낸다.

```java
OutputStream os = new FileOutputStream("C:/test.txt");
byte[] data = "ABC".getBytes();
os.write(data, 1, 2); // B,C만 출력
```

  

#### flush()와 close() 메소드

> 출력 스트림은 내부에 작은 buffer가 있어서 데이터가 출력되기 전에 버퍼가 쌓여있다가 순서대로 출력된다. flush() 메소드는 버퍼에 잔류하고 있는 데이터를 모두 출력시키고 버퍼를 비우는 역할을 한다. 프로그램에서 더 이상 출력할 데이터가 없다면 flush() 메소드를 마지막으로 호출하여 버퍼에 잔류하는 모든 데이터가 출력되도록 해야 한다. OutputStream을 더 이상 사용하지 않을 경우에는 close() 메소드를 통해 OutputStream에서 사용했던 시스템 자원을 풀어준다.  



### Reader

> 문자 기반 입력 스트림의 최상위 클래스로 추상 클래스이다.  모든 문자 기반 입력 스트림은 이 클래스를 상속 받아서 만들어진다.  
>
> Reader 클래스에는 문자 기반 입력 스트림이 기본적으로 가져야 할 메소드가 정의되어 있다. 주요 메소드를 보자

| 리턴 타입 | 메소드                              | 설명                                                         |
| --------- | ----------------------------------- | ------------------------------------------------------------ |
| int       | read()                              | 입력 스트림으로부터 한 개의 문자를 읽고 리턴한다.            |
| int       | read(char[] cbuf)                   | 입력 스트림으로부터 읽은 문자들을 매개값으로 주어진 문자 배열 cbuf에 저장하고 실제로 읽은 문자 수를 리턴한다. |
| int       | read(char[] cbuf, int off, int len) | 입력 스트림으로부터 len개의 문자를 읽고 매개값으로 주어진 문자 배열 cbuf[off]부터 len개까지 저장한다. 그리고 실제로 읽은 문자 수인 len개를 리턴한다. |
| void      | close()                             | 사용한 시스템의 자원을 반납하고 입력스트림을 닫는다.         |

  

#### read() 메소드

> 입력 스트림으로부터 한 개의 문자(2byte)를 읽고 4바이트 int 타입으로 리턴한다. 따라서 리턴된 4바이트 중 끝에 있는 2바이트에 문자 데이터가 들어 있다. 더 이상 입력스트림으로부터 문자를 읽을 수 없다면 -1을 리턴한다.

```java
Reader reader = new FileReader("C:/test.txt");
int readData;

while((readData = reader.read()) != -1) { 
	char charData = (char) readData;
}
```



#### read(char[] cbuf) 메소드

> 입력 스트림으로부터 매개값으로 주어진 문자 배열의 길이만큼 문자를 읽고 배열에 저장한다. 그리고 읽은 문자 수를 리턴한다. 실제로 읽은 문자 수가 배열의 길이보다 작을 경우 읽은 수만큼만 리턴한다. 더 이상 입력스트림으로부터 문자를 읽을 수 없다면 -1을 리턴한다.

```java
Reader reader = new FileReader("C:/test.txt");
int readCharNo;
char[] cbuf = new char[2];
while((readCharNo = reader.read(cbuf)) != -1 ) { ... }
```

  

#### read(char[] cbuf, int off, int len) 메소드

> 입력 스트림으로부터 len개의 문자만큼 읽고 매개값으로 주어진 문자 배열 cbuf[off]부터 len개까지 저장한다. 그리고 읽은 문자 수인 len개를 리턴한다. 실제로 읽은 문자 수가 len개 보다 작을 경우 읽은 문자 수만큼 리턴한다.

  

#### close() 메소드 

> Reader를 사용하지 않을 경우에는 close() 메소드를 통해 시스템 자원을 풀어준다.

  

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



