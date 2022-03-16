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

> 문자 기반 출력 스트림의 최상위 클래스로 추상 클래스이다. 모든 문자 출력 스트림 클래스는 이 클래스를 상속받아서 만들어진다.  
>
> 주요 메소드를 보자.

| 리턴 타입 | 메소드                               | 설명                                                         |
| --------- | ------------------------------------ | ------------------------------------------------------------ |
| void      | write(int c)                         | 출력 스트림으로 주어진 한 문자를 보낸다.(c의 끝 2바이트)     |
| void      | write(char[] cbuf)                   | 출력 스트림으로 주어진 문자 배열 cbuf의 모든 문자를 보낸다.  |
| void      | write(char[] cbuf, inf off, int len) | 출력 스트림으로 주어진 문자 배열 cbuf[off]부터 len개의 문자를 보낸다. |
| void      | write(String str)                    | 출력 스트림으로 주어진 문자열을 전부 보낸다.                 |
| void      | write(String str, int off, int len)  | 출력 스트림으로 주어진 문자열 off순번부터 len개까지의 문자를 보낸다. |
| void      | flush()                              | 버퍼에 잔류하는 모든 문자열을 출력한다.                      |
| void      | close()                              | 시스템 자원을 반납하고 출력 스트림을 닫는다.                 |

  

#### write(int c) 메소드

> int값을 제공을 하게되면 int에서 끝 2바이트에 있는 문자 정보를 출력 스트림으로 보낸다. (4바이트 전부가 보내지는 것이 아니다.)

* 예제

```java
Writer writer = new FileWriter("C:/Temp/test.txt");
char[] data = "홍길동".toCharArray();
for(int i = 0; i < data.length; i++) {
  writer.write(data[i]); // 홍 길 동 출력
}
writer.flush();
writer.close();
```



#### write(char[] cbuf) 메소드

> 매개값으로 주어진 char[] 배열의 모든 문자를 출력 스트림으로 보낸다.

```java
Writer writer = new FileWriter("C:/Temp/test.txt");
char[] data = "홍길동".toCharArray();
writer.write(data); // 홍길동 모두 출력
writer.flush();
writer.close();
```



#### write(char[]) cbuf, int off, int len) 메소드

> cbuf[off]의 위치부터 len개 까지의 문자를 출력 스트림으로 보낸다.

```java
Writer writer = new FileWriter("C:/Temp/test.txt");
char[] data = "홍길동".toCharArray();
writer.write(data, 1, 2); // 길 동 출력
writer.flush();
writer.close();
```



#### write(String str)과 write(String str, int off, int len) 메소드

> Writer는 좀 더 쉽게 문자열을 내보내기 위해서 매개값이 String타입인 write(String str)메소드와 write(String str, int off, int len) 메소드를 가진다. 

## 콘솔 입출력

> 콘솔은 시스템을 사용하기 위해 키보드로 입력을 받고 화면으로 출력하는 소프트웨어를 말한다. 
>
> unix/linux 운영체제에서는 terminal이 있고 windows 운영체제는 명령프롬프트가 있다.  
>
> 자바는 콘솔로부터 데이터를 입력받을 떄 `System.in`, 콘솔에 출력할 때 `System.out`을, 에러를 출력할 때 `System.err`을 사용한다.



### System.in 필드

> System 클래스에는 정적 필드 `in`이 있다. `System.in`은 InputStream 타입의 필드이므로 다음과 같이 변수로 참조가 가능하다. `InputStream is = System.in;`  
>
> 키보드로부터 어떤 키가 입력되었는지 확인하려면 read() 메소드로 한 바이트를 읽으면 된다.  
>
> `int asciiCode = is.read();`  
>
> 컴퓨터는 0과 1만을 이해할 수 있다. 그리고 아스키코드는 1byte로 표현되는 256가지의 숫자에 영어 알파벳, 아라비아 숫자, 특수 기호를 매칭하고 있다. 이러한 숫자로 된 아스키코드 대신 문자를 직접 얻고 싶다면 read() 메소드로 얻은 아스키 코드를  char로 형변환 하면 된다.  
>
> `char inputChar = (char) is.read();`



### System.out 필드

> 콘솔로 데이터를 출력하기 위해서는 System 클래스의  `out` 정적 필드를 사용한다. `out`은 PrintStream 타입의 필드이다.  
>
> `OutputStream os = System.out;`  
>
> 콘솔로 1개의 바이트를 출력하려면  OutputStream의 write(int b)메소드를 이용하면 된다. 이때 바이트 값은 아스키코드인데, write() 메소드는 아스키 코드를 문자로 콘솔에 출력한다. 예를 들어 아스키 코드 97번을 write(int b)로 출력하면 'a'가 출력된다.

* System 클래스의 out 필드를 OutputStream으로 변환해서 사용하는 것은 그리 편하지 않다. 따라서 PrintStream의 print(), println() 등을 사용하여 좀 더 쉬운 방법으로 다양한 타입의 데이터를 콘솔에 출력할 수 있다.



### Console 클래스

> 자바 6부터 콘솔에서 입력받은 문자열을 쉽게 읽을 수 있도록 `java.io.Console` 클래스를 제공하고 있다. Console객체는 `System.console()`로 호출하여 얻으면 된다.

* 이클립스에서 실행하면 null을 리턴하기 때문에 명령 프롬프트에서 실행해야 한다.
* Console 클래스 읽기 메소드 정리

| 리턴 타입 | 메소드         | 설명                                                  |
| --------- | -------------- | ----------------------------------------------------- |
| String    | readLine()     | Enter키를 입력하기 전의 모든 문자열을 읽음            |
| char[]    | readPassword() | 키보드 입력 문자를 콘솔에 보여주지 않고 문자열을 읽음 |

* 예제

```java
public class ConsoleExample {
  public static void main(String[] args) {
    Console console = System.console();
    
    System.out.println("아이디 : ");
    String id = console.readLine();
    
    System.out.println("비밀번호 : ");
    char[] charPass = console.readPassword();
    String pw = new String(charPass);
    
    System.out.println(id);
    System.out.println(pw);
  }
}
```



### Scanner 클래스 

> Console 클래스는 문자열은 읽을 수 있지만, 정수, 실수 값은 바로 읽을 수 없다. `java.io` 패키지의 클래스는 아니지만 `java.util` 패키지의 Scanner 클래스를 이용하면 콘솔로부터 기본 타입의 값을 바로 읽을 수 있다. **콘솔에서만 Scanner 클래스가 사용되는 것은 아니고 File, InputStream, Path 등과 같이 다양한 입력 소스를 지정할 수도 있다.**
>
> `Scanner sc = new Scanner(System.in);` 

## 파일 입출력

### File 클래스

> IO 패키지에서 제공하는 File 클래스는 파일크기, 속성, 이름 등의 정보를 얻어내는 기능과 파일 생성 및 삭제 기능을 제공하고 있다. 그리고 디렉토리를 생성하고 디렉토리에 존재하는 파일 리스트를 얻어내는 기능도 있다. 그러나 파일의 데이터를 읽고 쓰는 기능은 지원하지 않는다. 파일의 입출력은 Stream을 사용한다.
>
> `File file = new File("C:/file/test.txt");`
>
> `File file = new File("C:\\file\\test.txt");`
>
> * 구분자 : 디렉터리 구분자는 운영체제마다 조금씩 다르다. 윈도우의 경우 `/`, `\`를 사용할 수 있고, 유닉스나 리눅스는 `/`를 사용한다.  `File.seperator`를 출력하면 해당 운영체제에서 사용하는 구분자를 확인할 수 있다.
>
> 또한 단지 File 객체를 생성 및 초기화 했다고 해서 파일 || 디렉토리가 생성된 것은 아니다. 해당 객체로 실제 파일이나 디렉토리가 있는지 확인하려면 boolean타입의 `exist()`를 호출할 수 있다.

* exist()의 리턴값이 false라면 이러한 메소드를 사용할 수 있다.

| 리턴 타입 | 메소드          | 설명                            |
| --------- | --------------- | ------------------------------- |
| boolean   | createNewFile() | 새로운 파일을 생성              |
| boolean   | mkdir()         | 새로운 디렉토리를 생성          |
| boolean   | mkdirs()        | 경로상에 없는 디렉토리들을 생성 |
| boolean   | delete()        | 파일 또는 디렉토리 삭제         |

* 파일 또는 디렉토리가 존재할 경우에는 다음 메소드를 사용할 수 있다.

| 리턴 타입 | 메소드                          | 설명                                                         |
| --------- | ------------------------------- | ------------------------------------------------------------ |
| boolean   | canExcute()                     | 실행할 수 있는 파일인지 여부                                 |
| boolean   | canRead()                       | 읽을 수 있는 파일인지 여부                                   |
| boolean   | canWrite()                      | 수정 및 저장할 수 있는 파일인지 여부                         |
| String    | getName()                       | 파일의 이름을 리턴                                           |
| String    | getParent()                     | 파일 상위 디렉토리의 이름을 리턴                             |
| File      | getParentFile()                 | 부모 디렉토리를 File객체로 생성 후 리턴                      |
| String    | getPath()                       | 전체 경로를 리턴                                             |
| boolean   | isDirectory()                   | 디렉토리인지 여부                                            |
| boolean   | isFile()                        | 파일인지 여부                                                |
| boolean   | isHidden()                      | 숨겨진 파일인지 여부                                         |
| long      | lastModified()                  | 마지막 수정 날짜 및 시간을 리턴                              |
| long      | length()                        | 파일의 크기를 리턴                                           |
| String[]  | list()                          | 디렉토리에 포함된 파일 및 서브디렉토리 목록 전부를 String 배열로 리턴 |
| String[]  | list(FilenameFilter filter)     | 디렉토리에 포함된 파일 및 서브디렉토리 목록 중에 FilenameFileter에 맞는 것만 String 배열로 리턴 |
| File[]    | listFiles()                     | 디렉토리에 포함된 파일 및 서브 디렉토리 목록 전부를 File 배열로 리턴 |
| File[]    | listFiles(FilnameFilter filter) | 디렉토리에 포함된 파일 및 서브디렉토리 목록 중에 FilenameFileter에 맞는 것만 File 배열로 리턴 |



### FileInputStream

> 파일로부터 바이트 단위로 읽어들일 때 사용하는 바이트 기반 입력 스트림이다. 바이트 단위로 읽기 때문에 그림, 오디오, 비디오, 텍스트 파일 등 모든 종류의 파일을 읽을 수 있다. 다음은 FileInputStream을 생성하는 두 가지 방법을 보여준다.

```java
// # 1
FileInputStream fis = new FileInputStream("C:/temp/test.txt");

// # 2
File file = new File("C:/temp/test.txt")
FileInputStream fis = new FIleInputStream(file);
```

> 만약 파일이 존재하지 않을시  FileNotFoundException을 발생시킨다. 따라서 try-catch로 예외 처리를 해야한다.

### FileOutputStream

> 바이트 단위로 데이터를 파일에 저장할 때 사용하는 바이트 기반의 출력 스트림이다. 바이트 단위로 저장하기 때문에 그림, 오디오, 비디오 등 모든 종류의 데이터를 파일로 저장할 수 있다. 

```java
// # 1
FileOutputStream fis = new FileOutputStream("C:/temp/test.txt");

// # 2
File file = new File("C:/temp/test.txt")
FileOutputStream fis = new FileOutputStream(file);
```

> 파일이 이미 존재할 경우 데이터를 출력하면 파일을 덮어쓰게 되므로 기존의 파일 내용은 사라지게 된다. 기존의 파일 내용 끝에 데이터를 추가할 경우에는 생성자의 두 번째 매개값으로 `true`를 주면 된다.

```java
// # 1
FileOutputStream fis = new FileOutputStream("C:/temp/test.txt", true);

// # 2
File file = new File("C:/temp/test.txt")
FileOutputStream fis = new FileOutputStream(file, true);
```

  

### FileReader

> 텍스트 파일을 프로그램으로 읽어들일 때 사용하는 문자 기반 스트림이다. 문자 단위로 읽기 때문에 텍스트가 아닌 그림, 오디오, 비디오 등은 읽을 수 없다. FileReader를 생성하는 두가지 방법을 보자

```java
// # 1
FileReader reader = new FileReader("C:/temp/test.txt");

// # 2
File file = new File("C:/temp/test.txt");
FileReader reader = new FileReader(file);
```

> 객체가 생성될 때 파일과 직접 연결이 되는데, 만약 파일이 존재하지 않으면 `FileNotFoundException`을 발생시키므로 try-catch로 감싸줘야 한다. 

### FileWriter

> 텍스트 데이터를 파일에 저장할 때 사용하는 문자 기반 스트림이다. 문자 단위로 저장하기 때문에 텍스트 외의 다른 파일 등은 저장할 수 없다. FileWriter를 생성하는 두가지 방법을 보자

```java
// # 1
FileWriter writer = new FileWriter("C:/temp/test.txt");

// # 2
File file = new File("C:/temp/test.txt");
FileWriter writer = new FileWriter(file);
```

> 주의할 점으로 경로에 해당 파일이 이미 존재할 경우 덮어쓰여지므로 원래 파일의 내용은 사라지게 된다. 기존의 파일 내용 끝에 데이터를 추가하는 경우에는 생성자 두 번째 매개값으로 `true`를 주자.



## 보조 스트림

> 다른 스트림과 연결되어 여러 가지 편리한 기능을 제공해주는 스트림을 말한다. 보조 스트림을 필터 스트림이라고도 하는데, 이는 보조 스트림의 일부가 FileInputStream, FileOutputStream의 하위 클래스이기 때문이다. 하지만 다른 보조 스트림은 이 클래스를 상속받지 않는다.  
>
> 보조 스트림은 자체적으로 입출력을 수행할 수 없기 때문에 입력 소스와 바로 연결되는 InputStream, FileInputStream, Reader, FileReader, 출력 소스와 바로 연결되는 OutputStream, FileOutputStream, Writer, FileWriter 등에 연결해서 입출력을 수행한다.  보조 스트림은 문자 변환, 입출력 성능 향상, 기본 데이터 타입 입출력, 객체 입출력 등의 기능을 제공한다.

* 보조 스트림을 생성할 떄에는 자신이 연결될 스트림을 다음과 같이 생성자의 매개값으로 받는다.

```java
보조스트림 변수 = new 보조스트림(연결스트림);
```

* 예를 들어 콘솔 입력 스트림을 문자 변환 보조 스트림인 InputStreamReader에 연결하는 코드는 다음과 같다.

```java
InputStream is = System.in;
InputStreamReader reader = new InputStreamReader(is);
```

* 문자 변환 보조 스트림인 InputStreamReader를 다시 성능 향상 보조 스트림인 BufferedReader에 연결하는 코드는 다음과 같다.

```java
InputStream is = System.in;
InputStreamReader reader = new InputStreamReader(is);
BufferedReader br = new BufferedReader(reader);
```



### 문자 변환 보조 스트림

> 소스 스트림이 바이트 기반 스트림(InputStream, OutputStream, FileInputStream, FileOutputStream)이면서 입출력 데이터가 문자라면  Reader와 Writer로 변환해서 사용하는 것을 고려해야 한다.  
>
> 그 이유는 Reader와 Writer는 문자 단위로 입출력하기 때문에 바이트 기반 스트림보다는 편하고 문자셋의 종류를 지정할 수 있기 때문에 다양한 문자를 입출력할 수 있다.

#### InputStreamReader

> 바이트 입력 스트림에 연결되어 문자 입력 스트림인 Reader로 변환시키는 보조 스트림이다.

```java
Reader reader = new InputStreamReader(바이트입력스트림);
```

* 예를 들어 콘솔 입력을 위한 문자 변환 보조 스트림인 InputStream을 다음과 같이 Reader 타입으로 변환할 수 있다.

```java
InputSteream is = System.in;
Reader reader = new InputStream(is);
```

* 파일 입력을 위한 FileInputStream도 다음과 같이 Reader 타입으로 변환시킬 수 있다.

```java
FileInputStream fis = new FileInputStream("C:/Temp/file.txt");
Reader reader = new InputStreamReader(fis);
```

> FileInputStream에 InputStreamReader를 연결하지 않고 FileReader를 직접 생성할 수도 있다. FileReader는  InputStreamReader의 하위 클래스이다. 이것은 FileReader가 내부적으로 FileInputStream에 InputStreamReader 보조 스트림을 연결한 것이라고 볼 수 있다.

#### OutputStreamReader

> 바이트 출력 스트림에 연결되어 문자 출력 스트림인  Writer로 변환시키는 보조 스트림이다. 

```java
Writer writer = new OutputStreamWriter(바이트출력스트림);
```

* 예를 들어 파일 출력을 위한 FileOutputStream을 다음과 같이 Writer 타입으로 변환할 수 있다.

```java
FileOutputStream fos = new FileOutputStream("C:/Temp/test.txt");
Writer writer = new OutputStreamWriter(fos);
```

> FileOutputStream에 OutputStreamWriter를 연결하지 않고 FileWriter를 직접 생성할 수도 있다.  
>
> FileWriter는 OutputStreamWriter의 하위 클래스이다. 이것은 FileWriter가 내부적으로 FileOutputStream에 OutputStreamWriter 보조 스트림을 연결한 것으로 볼 수 있다. 

### 성능 향상 보조 스트림

> 프로그램의 실행 성능은 입출력이 가장 늦은 장치를 따라간다. CPU와 메모리가 아무리 뛰어나도 하드 디스크의 입출력이 늦어지면 프로그램의 실행 성능은 하드 디스크의 처리 속도에 맞춰진다. 네트워크로 데이터를 전송할 때도 마찬가지다. 이러한 문제에 대한 완전한 해결책은 될 수 없지만, 프로그램이 입출력 소스와 직접 작업하지 않고 중간에 메모리 버퍼와 작업함으로써 실행 성능을 향상시킬 수 있따. 예를 들어 프로그램은 직접 하드 디스크에 데이터를 보내지 않고 메모리 버퍼에 데이터를 보냄으로써 쓰기 속도가 향상된다. **버퍼는 데이터가 쌓이길 기다렸다가 꽉 차게 되면 데이터를 한 번에 하드디스크로 보냄으로써 출력 횟수를 줄여준다.**  
>
> 보조 스트림 중에서는 위와 같이 **메모리 버퍼를 제공하여 프로그램의 성능을 향상시키는 것**들이 있다.



#### BufferedInputStream과 BufferedReader

> `BufferedInputStream` : 바이트 입력 스트림에 연결되어 버퍼를 제공해주는 보조 스트림이다. **최대 8192 바이트**
>
> `BufferedReader` : 문자 입력 스트림에 연결되어 버퍼를 제공해주는 보조 스트림이다. **최대 8192 문자**
>
> 둘 다 입력 소스로부터 자신의 내부 버퍼 크기만큼 데이터를 미리 읽고 버퍼에 저장해둔다. 프로그램은 외부의 입력 소스로부터 직접 읽는 대신 버퍼로부터 읽음으로써 읽기 성능이 향상된다.  
>
> `BufferedInputStream`과 `BufferedReader` 보조 스트림은 다음과 같이 생성자의 매개값으로 준 입력스트림과 연결되어 8192 내부 버퍼 사이즈를 가지게 된다. 
>
> 데이터를 읽어들이는 방법은 InputStream || Reader와 동일하다.

```java
BufferedInputStream bis = new BufferedInputStream(바이트 입력 스트림);
BufferedReader br = new BufferedReader(문자 입력 스트림);
```

#### BufferedOutputStream과 BufferedWriter

> `BufferedOutputStream` : 바이트 출력 스트림에 연결되어 버퍼를 제공해주는 보조 스트림이다. **최대 8192 바이트**
>
> `BufferedWriter` : 문자 출력 스트림에 연결되어 버퍼를 제공해주는 보조 스트림이다. **최대 8192 문자**
>
> 둘 다 프로그램에서 전송한 데이터를 내부 버퍼에 쌓아두었다가 꽉 차면 버퍼의 모든 데이터를 한꺼번에 보낸다. 프로그램 입장에서 보면 직접 데이터를 보내는 것이 아니라, 메모리 버퍼로 데이터를 고속 전송하기 때문에 성능이 향상되는 효과를 얻게 된다.  
>
> 데이터를 출력하는 방법은  OutputStream || Writer와 동일하다.

```java
BufferedOutputStream bos = new BufferedOutputStream(바이트 출력 스트림);
BufferedWriter bw = new BufferedWriter(문자 출력 스트림);
```



### 기본 타입 입출력 보조 스트림

> 바이트 스트림은 바이트 단위로 입출력하기 때문에 자바의 기본 데이터 타입인 boolean, char, short, int, long, float, double 단위로 입출력할 수 없다. 그러나 `DataInputStream`과 `DataOutputStream` 보조 스트림을 연결하면 기본 데이터 타입으로 입출력이 가능하다. 객체 생성 방법에 대해 알아보자.

```java
DataInputStream dis = new DataInputStream(바이트 입력 스트림);
DataOutputStream dos = new DataOutputStream(바이트 출력 스트림);
```

* `DataInputStream`과 `DataOutputStream`이 가지는 메소드

| DataInputStream |               | DataOutputStream |                         |
| --------------- | ------------- | ---------------- | ----------------------- |
| boolean         | readBoolean() | void             | writeBoolean(boolean v) |
| byte            | readByte()    | void             | writeByte(int v)        |
| char            | readChar()    | void             | writeChar(char v)       |
| double          | readDouble()  | void             | writeDouble(double v)   |
| float           | readFloat()   | void             | writeFloat(float v)     |
| int             | readInt()     | void             | writeInt()              |
| long            | readLong()    | void             | writeLong()             |
| short           | readShort()   | void             | writeShort()            |
| String          | readString()  | void             | writeString()           |

이 메소드들로 입출력할 떄의 주의점은 **데이터 타입의 크기가 모두 다르므로 DataOutputStream으로 출력한 데이터를 다시  DataInputStream으로 읽어올 때는 출력한 순서와 동일한 순서로 읽어야 한다는 점**이다.

예를 들어 int ▶️ boolean ▶️ double의 출력 순서를 가지고 있다면 입력 순서도 int ▶️ boolean ▶️ double의 순서를 지켜야 한다는 것이다.

### 프린터 보조 스트림

> `PrintStream`과 `PrintWriter`는 프린터와 유사하게 출력하는 print(), println() 메소드를 가지고 있는 보조 스트림이다. System.out이 바로 PrintStream 타입이기 때문에 print(), println() 메소드를 사용할 수 있었다. PrintStream은 바이트 출력 스트림과 연결되고, PrintWriter는 문자 출력 스트림으로 연결된다. 둘 다 거의 같은 기능을 가지고 있다.

```java
PrintStream ps = new PrintStream(바이트출력스트림);
PrintWriter pw = new PrintWriter(문자출력스트림);
```

* print()와 println()은 다음과 같이 오버로딩이 되어 있다.(데이터 타입에 따라)

| PrintStream | PrintWriter      |      |                    |
| ----------- | ---------------- | ---- | ------------------ |
| void        | print(boolean b) | void | println(boolean b) |
| void        | print(char c)    | void | println(char c)    |
| void        | print(double d)  | void | println(double d)  |
| void        | print(float f)   | void | println(float f)   |
| void        | print(long l)    | void | println(long l)    |
| void        | print(int i)     | void | println(int i)     |
| void        | print(Object o)  | void | println(Object o)  |
| void        | print(String s)  | void | println(String s)  |

> 그 외에 printf()도 제공한다. printf()는 형식화된 문자열을 출력할 수 있도록 하기 위해 자바 5부터 추가된 메소드이다.  
>
> 첫 번째 매개값으로 형식화된 문자열을 지정하고, 두 번째 매개값부터 형식화된 문자열에 들어갈 값을 나열해주면 된다.  
>
> `printf(String format, Object ... args)` 

* `format` :
  * %[argument_index$] [flags] [width] [.precision] converison
  * `  매개값의 순번`, `-,0`, `전체 자릿수`, `소수 자릿수`, `변환 문자`
  * 형식화된 문자열에서 %와 conversion은 필수적으로 작성하고 그 이외의 항목은 생략할 수 있다. `argument_index$`는 **적용할 매개값의 순번**인데 `1$`는 첫 번째 매개값, `2$`는 두 번째 매개값을 말한다. `flags`는 빈공간을 채우는 방법인데, 생략되면 **왼쪽이 공백으로 채워지고, `-`는 오른쪽이 공백으로 채워진다. **`width`는 **전체 자릿수**이며, `precision`은 **소수자릿수**를 뜻한다. 변환문자에는 정수(d), 실수(f), 문자열(s)과 시간과 관련된 문자가 와서 매개값을 해당 타입으로 출력한다. 형식화된 문자열에 대한 자세한 내용은  Java API 도큐먼트에서 `java.util.Formatter` 클래스의 Format String syntax 부분을 읽어보면 된다.
  * 형식화된 문자열에서 자주 사용되는 것

| 형식화된 문자 |                                                              | 설명                                                         | 출력형태                                                     |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 정수          | %d<br />%6d<br />%-6d<br />%06d                              | 정수<br />6자리 정수, 왼쪽 빈자리 공백<br />6자리 정수, 오른쪽 빈자리 공백<br />6자리 정수, 왼쪽 빈자리 0채움 | 123<br />_______123<br />123\_______<br />000123             |
| 실수          | %10.2f<br />%-10.2f<br />%010.2f                             | 소수점 이상 7자리, 소수점 이하 2자리, 왼쪽 빈자리 공백<br />소주점 이상 7자리, 소수점 이하 2자리, 오른쪽 빈자리 공백<br />소수점이상 7자리, 소수점 이하 2자리, 왼쪽 빈자리 0으로 채움 | _______123.45<br />123.45___<br />0000123.45                 |
| 문자열        | %s<br />%6s<br />%-6s                                        | 문자열<br />6자리 문자열 왼쪽 빈자리 공백<br />6자리 문자열 오른쪽 빈자리 공백 | abc<br />_______abc<br />abc_______                          |
| 날짜          | %tF<br />%tY<br />%ty<br />%tm<br />%td<br />%tH<br />%tl<br />%tM<br />%tS | %tY-%tm-%td<br />4자리 년<br />2자리 년<br />2자리 월<br />2자리 일<br />2자리 시(0 ~ 23)<br />시 (0 ~ 12)<br />2자리 분<br />2자리 초 | 2010-01-06<br />2010<br />10<br />01<br />06<br />08<br />8<br />06<br />24 |
| 특수문자      | \t<br />\n<br />%%                                           | 탭 (tab)<br />줄바꿈<br />%                                  | <br /><br />%                                                |

### printf() 예제

```java
import java.util.Date;

public class PrintExample {
    public static void main(String[] args) {
        System.out.printf("상품의 가격 : %d원\n", 123);
        System.out.printf("상품의 가격 : %6d원\n", 123);
        System.out.printf("상품의 가격 : %-6d원\n", 123);
        System.out.printf("상품의 가격 : %06d원\n", 123);

        System.out.printf("반지름이 %d인 원의 넓이 : %10.2f\n", 10, Math.PI * 10 * 10);

        System.out.printf("%6d | %-10s | %10s\n", 1, "홍길동", "도적");

        Date now = new Date();
        System.out.printf("오늘은 %tY년 %tm월 %td일 입니다\n", now, now, now);
        System.out.printf("오늘은 %1$tY년 %1$tm월 %1$td일 입니다\n", now);
        System.out.printf("현재 %1$tH시 %1$tM분 %1$tS초 입니다\n", now);
    }
}
```

​	

### 객체 입출력 보조 스트림

> 자바는 메모리에 생성된 객체를 파일 또는 네트워크로 출력할 수가 있다. 객체는 문자가 아니기 때문에 바이트 기반 스트림으로 출력해야 한다. 객체를 출력하기 위해서는 객체의 데이터를 일렬로 늘어선 연속적인 바이트로 변경해야 하는데, 이것을 **객체 직렬화(Serialization)라고 한다.** 반대로 파일에 저장되어 있거나 네트워크에서 전송된 객체를 읽을수도 있는데 입력 스트림으로부터 읽어들인 연속적인 바이트를 객체로 복원하는 것을 **역직렬화(deserialization)**이라고 한다.



#### ObjectInputStream, ObjectOutputStream

> * ObjectOutputStream : 바이트 출력 스트림과 연결되어 객체를 직렬화하는 역할을 한다.
> * ObjectInputStream : 바이트 입력 스트림과 연결되어 객체로 역직렬화 하는 역할을 한다.

```java
// # 생성하는 방법
ObjectInputStream ois = new ObjectInputStream(바이트입력스트림);
ObjectOutputStream oos = new ObjectOutputStream(바이트출력스트림);

// ObjectOutputStream으로 객체를 직렬화하기 위해서는 writeObject() 메소드를 사용한다.
oss.writeObejct(객체);

// 반대로 ObjectInputStream의 readObject() 메소드는 입력 스트림에서 읽은 바이트를 역직렬화해서 객체로 생성한다. readObject()의 리턴 타입은 Object이기 때문에 객체 원래의 타입으로 캐스팅 해주어야 한다.
객체타입 변수 = (객체타입) ois.readObject();
```

#### 예제

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectInputOutputStreamExample {
    public static void main(String[] args) throws Exception {
        String path = "/Object.dat";
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(new Integer(10));
        oos.writeObject(new Double(3.14));
        oos.writeObject(new int[]{1, 2, 3});
        oos.writeObject(new String("홍길동"));

        oos.flush();
        oos.close();
        fos.close();

        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Integer obj1 = (Integer) ois.readObject();
        Double obj2 = (Double) ois.readObject();
        int[] obj3 = (int[]) ois.readObject();
        String obj4 = (String) ois.readObject();

        ois.close();
        fis.close();

        System.out.println(obj1);
        System.out.println(obj2);
        System.out.println(obj3[0] + "" + obj3[1] + "" + obj3[2]);
        System.out.println(obj4);

    }
}
```



#### 직렬화가 가능한 클래스(Seriallizable)

> 자바는 Serializable 인터페이스를 구현한 클래스만 직렬화가 가능하도록 제한하고 있다. Serializable 인터페이스는 필드나 메소드가 없는 빈 인터페이스지만, 객체를 직렬화할 때 private 필드를 포함한 모든 필드를 바이트로 변환해도 좋다는 표시 역할을 한다.

```java
public class XXX implements Serializable { }
```

> 객체를 직렬화하면 바이트로 변환되는 것은 필드들이고 생성자 및 메소드는 직렬화에 포함되지 않는다. 따라서 역직렬화할 때에는 필드의 값만 복원된다. 하지만 모든 필드가 직렬화 대상이 되는 것은 아니다. 필드 선언에 static 또는 는  transient가 붙어 있을 경우에는 직렬화가 되지 않는다.

```java
public class XXX implements Serializable {
  public int field1;
  public int field2;
  int field3;
  private int field4;
  public static int field5; // static 키워드가 붙어 직렬화가 되지 않음
  transient int field6; // transient 키워드가 붙어 직렬화가 되지 않음
}
```



#### serialVersionUID 필드

>  직렬화된 객체를 역직렬화 할 때는 직렬화했을 때와 같은 클래스를 사용해야 한다. 클래스의 이름이 같더라도 클래스의 내용이 변경되면 역직렬화는 실패하며 다음과 같은 예외가 발생한다.

`java.io.InvalidClassException: XXX; local class incompatible: stream classdesc
serialVersionUID = -975398275932579136, local class serialVersionUID = -12131123948237592845`

: 직렬화 할 때와 역직렬화 할 때 사용된 클래스의  serialVersionUID가 다르다는 것이다. serialVersionUID는 같은 클래스임을 알려주는 식별자 역할을 하는데, Serializable 인터페이스를 구현한 클래스를 컴파일하면 자동적으로 이 정적 필드가 추가 된다. 문제는 재 컴파일하면 이 UID값이 달라진다는 것이다. 네트워크로 객체를 직렬화하여 전송하는 경우, 보내는 쪽과 받는 쪽이 모두 같은 serialVersionUID를 갖는 클래스를 가지고 있어야 하는데, 한 쪽에서 클래스를 변경해서 재컴파일하면 다른 serialVerisonUID를 가지게 되므로 역직렬화에 실패한다.

* SerialVersionUID 변경에 따른 예외 발생 예제

1. 직렬화가 가능한 클래스

```java
public class ClassC implements Serializable {
    // 직렬화가 가능한 클래스
    int field1;
}
```

2. 객체 직렬화 수행

```java
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerialVerionUIDExample1 {
    // 직렬화 수행
    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("C:/Temp/Object.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        ClassC classC = new ClassC();
        classC.field1 = 1;

        oos.writeObject(classC);
        oos.flush();
        oos.close();
        fos.close();
    }
}
```

3. 객체 역직렬화 수행

```java
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SerialVersionUIDExample2 {
    // 역직렬화 수행
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("C:/Temp/Object.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        ClassC classC = (ClassC) ois.readObject();
        System.out.println("field1 : " + classC.field1);
    }
}
```

4. 1.의 필드 수정 - serialVersionUID 변경됨

```java
import java.io.Serializable;

public class ClassC implements Serializable {
    // 직렬화가 가능한 클래스
    int field1;
    // 필드 수정 - serialVersionUID 변경됨
    int field2;
}
```

> 이후 파일에 저장된 ClassC 객체를 복원하기 위해 역직렬화를 수행하면  serialVersionUID가 다르기 때문에 예외가 발생한다.
>
> 만일 불가피하게 클래스의 수정이 필요할 때는 명시적으로 필드에 serialVersionUID를 선언하면 된다.

```java
import java.io.Serializable;

public class ClassC implements Serializable {
	static final long serialVersionUID = 정수값;
}
```



#### writeObject()와 readObject() 메소드

> 두 클래스가 상속 관계에 있을 때를 가정할 때, 부모 클래스가  Serializable 인터페이스를 구현하고 있으면 자식 클래스는 이를 구현하지 않아도 자식 객체를 직렬화하면 부모 필드 및 자식 필드가 모두 직렬화 된다. 하지만 그 반대로 부모 클래스가 Serializable을 구현하지 않고, 자식 클래스만 구현하고 있다면 자식 객체를 직렬화할 때 **부모 클래스의 필드는 직렬화에서 제외된다.**
>
>   
>
> 이럴 경우 부모 클래스의 필드를 직렬화하고 싶다면 다음 2가지 방법 중 택 1을 해야한다.
>
> * 부모 클래스가 Serializable 인터페이스를 구현하도록 한다.
> * 자식 클래스에서 writeObject()와 readObject() 메소드를 선언해서 부모 객체의 필드를 직접 출력시킨다.  
>
>   
>
> 물론 부모 클래스를 수정하면 좋겠지만, 수정할 수 없을 경우에는 후자의 방법을 사용해야 한다.

* writeObject()  : 직렬화될 때 자동적으로 호출된다.
* readObject() : 역직렬화될 때 자동적으로 호출된다.
* 

```java
// writeObject() 선언 방법
private void writeObject(ObjectOutputStream out) throws IOException {
  out.writeXXX(부모필드); // 부모 객체의 필드값을 출력함
  .
  .
 	out.defaultWriteObject(); // 자식 객체의 필드값을 직렬화
}
```

```java
// readObject() 선언 방법
private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
  부모필드 = in.readXXX(); // 부모 객체의 필드값을 읽어옴
  .
  .
  in.defaultReadObject(); // 자식 객체의 필드값을 역직렬화
}
```

> 주의할 점은 두 메소드의 접근 제한자가 `private`가 아니면 자동 호출되지 않기 때문에 반드시 private을 적어줘야 한다는 점이다.
>
> 메소드의 매개값인 `ObjectInputStream`, `ObjectOutputStream`은 다양한 종류의 `readXXX()`, `writeXXX()`를 제공하기 때문에 부모 필드 타입에 맞는 것을 선택해서 사용하면 된다.



## 네트워크 기초

> **네트워크** : 여러 대의 컴퓨터를 통신 회선으로 연결한 것을 말한다.
>
> **지역 네트워크** : 회사 건물, 특정 영역 에 존재하는 컴퓨터를 통신 회선으로 연결한 것을 말한다.
>
> **홈 네트워크** : 집에 방마다 컴퓨터가 있고, 이 컴퓨터들을 유/무선 등의 통신 회선으로 연결했다면 홈네트워크가 형성된 것.
>
> **인터넷** : 지역 네트워크를 통신 회선으로 연결한 것.



### 서버와 클라이언트

> 컴퓨터가 인터넷에 연결되어 있다면 실제로 데이터를 주곱다는 행위는 프로그램들이 한다. 
>
> 서비스를 제공하는 프로그램을 **서버**라고 부르고, 서비스를 받는 프로그램을 **클라이언트**라고 부른다.  
>
> 두 프로그램이 통신하기 위해서는 연결을 요청하는 역할과 수락하는 역할이 필요하다. 클라이언트는 서비스를 받기 위한 연결을 요청하고, 서버는 연결을 수락하여 서비스를 제공해준다. 
>
>   
>
> 서버는 클라이언트가 **요청(Request)**하는 내용을 처리해주고, **응답(Response)**을 클라이언트로 보낸다.
>
>   
>
> 클라이언트/서버 모델은 한 개의 서버와 다수의 클라이언트로 구성되는 것이 보통이나, **두 개의 프로그램이 서버인 동시에 클라이언트 역할을 하는 P2P** 모델도 있다. P2P는 먼저 접속을 시도한 컴퓨터가 클라이언트가 된다. 

### IP주소와 포트

> 모든 컴퓨터에는 고유한 주소가 있다. 이것이 바로 **IP(Internet Protocol)**이다. IP 주소는 네트워크 어댑터(랜카드) 마다 할당되는데, 한 개의 컴퓨터에 두 개의 네트워크 어댑터가 장착되어 있다면 2개의 IP 주소를 할당할 수 있다. 
>
> IP주소는 xxx.xxx.xxx.xxx와 같은 형식으로 표현된다. 여기서 xxx는 부호가 없는 0~255 사이의 정수이다. 연결할 상대의 IP주소를 모르면 프로그램들은 통신할 수 없다. 대중에게 서비스를 제공하는 대부분의 서버는 도메인 이름을 가지고 있는데 다음과 같이  DNS에 도메인 이름으로 IP를 등록해 놓는다.

```tex
[DNS]								도메인 이름		:		등록된 IP 주소
								www.naver.com	  :		222.122.195.5
```

> 숫자보다는 도메인 이름을 더 쉽게 기억하기 때문에 도메인 이름을 사용한다.  
>
>   
>
> 한 대의 컴퓨터에는 다양한 서버 프로그램들이 실행될 수 있다. 예를 들어 웹서버, 데이터 베이스 관리시스템(DBMS), FTP 서버 등이 하나의 IP주소를 갖는 컴퓨터에서 동시 실행될 수 있다. 이 경우 클라이언트는 어떤 서버와 통신해야 할지 결정해야 한다. IP는 컴퓨터의 네트워크 어댑터까지만 갈 수 있는 정보이기 때문에 접근하고자하는 내부의 서버를 선택하기 위해서는 추가적인 정보가 필요한데 이를 **포트(port)**라고 한다.  
>
> 서버는 시작할 때 고유한 포트를 가지고 실행하는데, 이것을 **포트 바인딩(Port Binding)**이라고 한다.  
>
> 클라이언트도 서버에서 보낸 정보를 받기 위해 포트 번호가 필요한데, 서버와 같이 고정적인 포트번호가 아니라 운영체제가 자동으로 부여하는 동적 포트 번호를 사용한다. 이 동적 포트 번호는 클라이언트가 서버로 연결 요청을 할 때 전송되어 서버가 클라이언트로 데이터를 보낼 때 사용된다. 총 범위는 `0 ~ 65535`인데 다음과 같이 세 가지 범위로 구분된다.

| 구분명                          | 범위          | 설명                                                         |
| ------------------------------- | ------------- | ------------------------------------------------------------ |
| Well Know Port Numbers          | 0 ~ 1023      | 국제 인터넷 주소 관리 기구가 특정 애플리케이션용으로 미리 예약한 포트 |
| Registered Port Numbers         | 1024 ~ 49151  | 회사에서 등록해서 사용할 수 있는 포트                        |
| Dynamic Or Private Port Numbers | 49152 ~ 65535 | 운영체제가 부여하는 동적 포트 또는 개인적인 목적으로 사용할 수 있는 포트 |



### InetAddress로 IP주소 얻기

> 자바는  IP주소를 `java.net.InetAddress` 객체로 표현한다. InetAddress는 로컬 컴퓨터의 IP주소 뿐만 아니라 도메인 이름을 DNS에서 검색한 후 IP 주소를 가져오는 기능을 제공한다. 로컬 컴퓨터의 InetAddress를 얻고 싶다면 IntetAddress.getLocalHost() 메소드를 다음과 같이 호출한다.

```java
InetAddress ia = InetAddress.getLocalHost()
  
// 외부 컴퓨터의 도메인 이름을 알고 있다면 다음 두 개의 메소드를 사용하여 InetAddress객체를 얻으면된다.
InetAddress ia = InetAddress.getByName(String host);
InetAddress[] iaArr = InetAddress.getAllByName(String host);

// InetAddress 객체에서 IP 주소를 얻기 위해서는 다음과 같이 호출하면 된다. 리턴값은 문자열이다.
String IPAddress = InetAddress.getHostAddress();
```



## TCP 네트워킹

> TCP(Tranmission Control Protocol)는 연결 지향적 프로토콜이다. 연결 지향 프로토콜이란 클라이언트와 서버가 연결된 상태에서 데이터를 주고받는 프로토콜을 말한다. 클라이언트가 연결 요청을 하고, 서버가 연결을 수락하면 통신 선로가 고정되고, 모든 데이터는 고정된 통신 선로를 통해서 순차적으로 전달된다. 그렇기 때문에 TCP는 데이터를 정확하고 안정적으로 전달한다.  
>
> * 단점 :
>   * 데이터를 보내기 전에 반드시 연결이 형성되어야 한다. (**가장 시간이 많이 걸리는 작업이다.**)
>   * 고정된 통신 선로가 최단선이 아닐경우 상대적으로 UDP보다 데이터 전송 속도가 느릴수 있다.  
> * 자바에서의 TCP
>   * `java.net.ServerSocket`
>   * `java.net.Socket`



### ServerSocket과 Socket의 용도

> TCP 서버의 역할은 두 가지로 볼 수 있다. 하나는 클라이언트가 연결 요청을 해오면 연결을 수락하는 것이고, 다른 하나는 연결된 클라이언트와 통신하는 것이다. 자바에서는 이 두 역할별로 별도의 클래스를 제공하고 있다.
>
> * `java.net.ServerSocket` : 클라이언트의 연결 요청을 기다리면서 연결 수락을 담당
> * `java.net.Socket` : 연결된 클라이언트와 통신을 담당\
>
>   
>
> 클라이언트가 연결 요청을 해오면 ServerSocket은 연결을 수락하고 통신용 Socket을 만든다.  
>
> 서버는 클라이언트가 접속할 포트를 가지고 있어야 하는데 이 포트를 **바인딩 포트(Binding Port)** 라고 한다. 서버는 고정도니 포트 번호에 바인딩해서 실행하므로, ServerSocket을 생성할 때 포트 번호를 하나 지정해야 한다. 서버가 실행되면 클라이언트는 서버의 IP주소와 바인딩 포트 번호로 Socket을 생성해서 연결 요청을 할 수 있다. ServerSocket은 클라이언트가 연결 요청을 해오면 accept() 메소드로 연결 수락을 하고 통신용 Socket을 생성한다. 그 후 각각의 Socket을 이용해서 데이터를 주고 받게 된다.



### ServerSocket 생성과 연결 수락

> ServerSocket은 서버를 개발하기 위한 객체이다. ServerSocket을 얻는 가장 간단한 방법은 생성자에 바인딩 포트를 대입하고 객체를 생성하는 것이다. 

```java
ServerSocket serverSocket = new ServerSocket(5001); // 5001번의 바인딩 포트를 가지는 서버소켓 생성
```

> 다른 방법은 디폴트 생성자로 객체를 생성하고 포트 바인딩을 위해 bind() 메소드를 호출하는 것이다.  bind()  메소드의 매개값은 포트 정보를 가진 InetSocketAddress이다.

```java
ServerSocket serverSocket = new ServerSocket();
serverSocket.bind(new InetSocketAddress(5001));
```

> 만약 서버 PC에 멀티 IP가 할당되어 있을 경우, 특정 IP로 접속할 때만 연결 수락을 하고 싶다면 다음과 같이 작성하고 `localhost`에 정확한 IP를 준다.

```java
ServerSocket serverSocket = new ServerSocket();
serverSocket.bind(new InetSocketAddress("IP 주소", 5001));
```



* `BindException` : ServerSocket을 생성할 때 바인딩 포트가 사용 중이라면 발생한다.  이 경우에는 다른 포트로 바인딩하거나 다른 프로그램을 종료하고 다시 실행한다.
* 바인딩 수행이 끝났을 때 : ServerSocket은 클라이언트 연결 수락을 위해 `accept()` 메소드를 실행해야 된다 .`accept()`는  클라이언트가 연결 요청하기 전까지 블로킹 되는데, 블로킹이란 스레드가 대기상태가 된다는 뜻이다. 그렇기 때문에 UI를 생성하는 스레드나, 이벤트를 처리하는 스레드에서 accept(() 메소드르 호출하지 않도록 한다. 블로킹이 되면 UI갱신이나 이벤트 처리를 할 수 없기 떄문이다. 클라이언트가 연결 요청을 하면 accept()는 클라이언트와 통신할 Socket을 만들고 리턴한다. 이것이 `연결 수락`이다.  
* accept()에서 블로킹 되었을 때 : ServerSocket을 닫기 위해 close() 메소드를 호출하면 SocketException이 발생한다. 예외 처리가 필요하다.

```java
try {
  Socket socket = serverSocket.accept();
} catch (SocketException e) {
  e.printStackTrace();
}
```

> 연결된 클라이언트 IP, PORT를 알고 싶다면 `getRemoteSocketAddress`를 호출해서 SocketAddress를 얻으면 된다.  
>
> 실제로 리턴되는 것은 InetSocketAddress이므로 다음과 같이 타입 변환을 할 수 있다.

```java
InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteAddress();
```

* InetSocketAddress IP, PORT 리턴 메소드

| 리턴 타입 | 메소드 (매개 변수) | 설명                             |
| --------- | ------------------ | -------------------------------- |
| String    | getHostName()      | 클라이언트 IP리턴                |
| int       | getPort()          | 클라이언트 포트 번호 리턴        |
| String    | toString()         | "IP 포트번호" 형태의 문자열 리턴 |

* 언바인딩 : 더 이상 클라이언트 연결 수락이 필요없을 떄는 ServerSocket의 close() 메소드를 호출해서 포트를 언바인딩 시켜야 한다. 

```java
serverSocket.close();
```



#### accept() 메소드 호출하여 다중 클라이언트 연결을 수락하는 예제

```java
package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerExample {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));

            while (true) {
                System.out.println(" [연결 기다림]");
                Socket socket = serverSocket.accept();
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("[연결 수락]" + isa.getHostName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```



### Socket 생성과 연결 요청

> 클라이언트가 서버에 연결 요청을 하려면 `java.net.Socket`을 이용해야 한다. Socket 객체를 생성함과 동시에 연결 요청을 하려면 생성자의 매개값으로 서버의 IP주소와 바인딩 포트 번호를 제공하면 된다. 

```java
try {
  Socket socket = new Socket("localhost", 5001); // 방법 1
  Socket socket = new Socket( new InetSocketAddress ("localhost", 5001)); // 방법 2
} catch (UnknownHostException e) {
  // IP 표기 방법이 잘못되었을 경우
} catch (IOException e) {
  // 해당 포트의 서버에 연결할 수 없는 경우
}
```

> 외부 서버에 접속하려면 localhost 대신 정확한 IP를 입력하면 된다. 만약 IP대신 도메인만 알고 있다면, 도메인 이름을 IP주소로 번역해야 하므로 InetSocketAddress 객체를 이용하는 방법을 사용해야 한다.  
>
> Socket 객체를 생성과 동시에 연결을 요청하지 않고 기본 생성자로 Socket을 생성한 후, connect() 메소드로 연결 요청을 할 수도 있다.

```java
socket = new Socekt();
socket.connect(new InetSocketAddress("localhost", 5001));
```

* 연결 요청시 발생 예외 2가지
  * `UnknownHostException` : 잘못 표기된 IP주소를 입력했을 경우 발생
  * `IOException` : 주어진 포트로 접속할 수 없을 때 발생.
* 두가지 예외를 처리하는 방법
  * Socket 생성자 및 connect() 메소드는 서버와 연결이 될 때까지 블로킹되기 때문에 UI를 생성하는 스레드나 이벤트를 처리하는 스레드에서 Socket 생성자 및 connect()를 호출하지 않도록 한다. 블로킹시 UI 갱신 || 이벤트 처리를 할 수 없다.
  * 연결된 후, 클라이언트 프로그램을 종료하거나 강제적으로 연결을 끊고 싶다면 Socket의 close() 메소드를 호출한다. close() 메소드 또한 IOException이 발생할 수 있기 때문에 예외 처리가 필요하다.

* localhost 5001 포트 요청 예제

```java
package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientExample {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost",5001);
            System.out.println("[연결 요청]");
            socket.connect(new InetSocketAddress("localhost", 5001));
            System.out.println("[연결 성공]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
```



### Socket 데이터 통신

> 클라이언트가 연결 요청(`connect()`)하고 서버가 연결 수락(`accept()`)했다면, 양쪽의 Socket 객체로부터 각각 입력 스트림(InputStream)과 출력 스트림(OutputStream)을 얻을 수 있다.

```java
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();
```

* 상대에게 데이터를 보낼 때 : byte[] 배열로 생성하고 이것을 매개값으로 해서 `OutputStream`의 `write()`메소드를 호출하면 된다.

```java
String data = "보낼 데이터";
byte[] byteArr = data.getBytes("UTF-8");
OutputStream os = socket.getOutputStream();
os.write(byteArr);
os.flush();
```

* 상대방이 보낸 데이터를 받을 떄 : byte[] 배열로 하나 생성하고, 이것을 매개값으로 해서 `InputStream`의  `read()` 메소드를 호출하면 된다. 읽은데이터를 byte[] 배열에 저장하고 읽은 바이트 수를 리턴한다. 

```java
byte[] byteArr = new byte[100];
InputStream is = socket.getInputStream();
int readByteCount = is.read(byteArr);
String data = new String(byteArr, 0, readByteCount, "UTF-8");
```



* 데이터 보내고 받기 예제

```java
package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientExample2 {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket();
            System.out.println("[연결 요청]");
            socket.connect(new InetSocketAddress("localhost", 5001));
            System.out.println("[연결 성공]");

            byte[] bytes = null;
            String message = null;

            OutputStream os = socket.getOutputStream();
            message = "Hello Server";
            bytes = message.getBytes("UTF-8");
            os.write(bytes);
            os.flush();
            System.out.println("[데이터 보내기 성공");

            InputStream is = socket.getInputStream();
            bytes = new byte[100];
            int readByteCount = is.read(bytes);
            message = new String(bytes, 0, readByteCount, "UTF-8");
            System.out.println("[데이터 받기 성공]");

            os.close();
            is.close();

        } catch (Exception e) {

        }

        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```



* 데이터 받고 보내기

```java
package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerExample2 {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 5001));
            while (true) {
                System.out.println("[연결 기다림]");
                Socket socket = serverSocket.accept();
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();

                byte[] bytes = null;
                String message = null;

                InputStream is = socket.getInputStream();
                bytes = new byte[100];
                int readByteCount = is.read(bytes);
                message = new String(bytes, 0, readByteCount, "UTF-8");
                System.out.println("[데이터 받기 성공]");

                OutputStream os = socket.getOutputStream();
                message = "Hello Server";
                bytes = message.getBytes("UTF-8");
                os.write(bytes);
                os.flush();

                is.close();
                os.close();

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```



> 데이터를 받기 위해 InputStream의 `read()` 메소드를 호출하면 상대방이 데이터를 보내기 전까지는 블로킹 되는데, `read()` 메소드가 블로킹 해제되고 리턴되는 경우는 세가지이다.

| 블로킹이 해제되는 경우 3가지                | 리턴값           |
| ------------------------------------------- | ---------------- |
| 상대방이 데이터를 보냄                      | 읽은 바이트 수   |
| 상대방이 정상적으로 Socket의 close()를 호출 | -1               |
| 상대방이 비정상적으로 종료                  | IOException 발생 |

> 상대방이 정상적으로 Socket의 close() 메소드를 호출하고 연결을 끊었을 경우와 비정상적으로 종료했을 경우 모두 예외 처리를 해서 이쪽도 Socket을 닫기 위해 close() 메소드를 호출해야 한다.



### 스레드 병렬 처리

> 연결 수락을 위해 ServerSocket의 accept()를 실행하거나, 서버 연결 요엉을 위해 Socket을 생성자 또는 connect()를 실행할 경우에는 해당 작업이 완료되기 전까지 블로킹된다. 
>
> 데이터 통신을 할 때에도 마찬가지인데 InputStream의 read() 메소드는 상대방이 데이터를 보내기 전까지 블로킹되고, OutputStream의 wirte() 메소드는 데이터를 완전하게 보내기 전까지 블로킹된다.  결론적으로 말해서 ServerSocket과 Socket은 동기 방식으로 구동된다. 
>
> 만약 서버를 실행시키는 main 스레드가 직접 입출력 작업을 담당하게 되면 입출력이 완료될 때까지 다른 작업을 할 수 없는 상태가 된다. 서버 애플리케이션은 지속적으로 클라이언트의 연결 수락 기능을 수행해야 되는데, 입출력에서 이 작업을 할 수 없게 된다.  또한 클라이언트1과 입출력하는 동안에는 클라이언트2와 입출력을 할 수 없게 된다. 그렇기 때문에 accept(), connect(), read(), write()는 별도의 작업 스레드를 생성하고, 다중 클라이언트와 병렬적으로 통신하는 모습을 보여준다.

* 다중 클라이언트와 병렬적으로 통신하는 모습![KakaoTalk_Image_2022-03-14-23-40-25](/Users/mac/Downloads/KakaoTalk_Image_2022-03-14-23-40-25.jpeg)

  : 스레드로 병렬처리를 할 경우, 수천 개의 클라이언트가 동시에 연결되면 서버에서 수천 개의 스레드가 생성되기 때문에 서버 성능이 급격하게 저하된다. 클라이언트 폭증의 이슈를 방지하려면 스레드풀을 사용하는 것이 바람직하다.

* 스레드풀을 이용한 서버 구현 방식![KakaoTalk_Image_2022-03-14-23-40-31](/Users/mac/Downloads/KakaoTalk_Image_2022-03-14-23-40-31.jpeg)

  : 클라이어늩가 연결 요청을 하면 서버의 스레드풀에서 연결 수락을 하고 Socket을 생성한다. 클라이언트가 작업 처리 요청을 하면 서버의 스레드 풀에서 요청을 처리하고 응답을 클라이언트로 보낸다. 스레드풀은 스레드 수를 제한해서 사용하기 때문에 갑작스런 클라이언트의 폭증은 작업 큐의 작업량만 증가시킬 뿐, 스레드의 수는 변함이 없다. 따라서 서버 성능은 완만히 저하되지만 클라이언트가 응답을 받는 시간이 조금 더 늦춰질 수는 있다.

  

## UDP 네트워킹

> **UDP** (User Datagram Protocol)는 비연결 지향적 프로토콜이다. 비연결 지향적이란 데이터를 주고받을 때 연결 절차를 거치지 않고, 발신자가 일방적으로 데이터를 발신하는 방식이다. **연결 과정이 없기 때문에 TCP보다는 빠른 전송을 할 수 있지만, 데이터 전달의 신뢰성은 떨어진다.**  
>
> UDP는 발신자가 데이터 패킷을 순차적으로 보내더라도 이 패킷들은 서로 다른 통신 선로를 통해 전달될 수 있다. 먼저 보낸 패킷이 느린 선로를 통해 전송될 경우 나중에 보낸 패킷보다 늦게 도착할 수 있다. 또한 일부 패킷은 잘못된 선로로 전송되어 잃어버릴 수도 있다.
>
> UDP는 편지에 비유할 수 있다. 발신자는 봉투(**패킷**)에 수신자의 주소(**IP와 Port**)와 발신자의 주소(**로컬 IP와 Port**)를 쓴다. 그리고 봉투 안에 편지(**전송할 데이터**)를 넣고 편지를 보낸다.  
>
> 발신자는 수신자가 편지를 받았는지의 여부는 모른다. 게다가 최근에 보낸 편지가 일찍 보내질 수도 있고, 보내지지 않았을 수도 있다. 
>
> 일반적으로 데이터 전달의 신뢰성보다는 속도가 중요한 프로그램에서는 UDP를 사용하고 신뢰성이 중요한 프로그램에서는 TCP를 사용한다. 자바에서는 UDP 프로그래밍을 위해 `java.net.DatagramSocket`과 `java.net.DatagramPacket`을 제공한다. 
>
> * **DatagramSocket** : 발신점과 수신점에 해당하는 클래스
> * **DatagramPacket** : 주고 받는 패킷 클래스



### 발신자 구현

```java
DatagramSocket datagramSocket = new DatagramSocket();
```

> 보내고자 하는 데이터를 byte[] 배열로 생성하는데, 문자열인 경우 다음과 같이 UTF-8로 인코딩해서 byte[] 배열을 얻으면 된다.

```java
byte[] byteArr = data.getBytes("UTF-8");
```

> 데이터와 수신자 정보를 담고 있는 `DatagramPacket`을 생성해야 하는데, `DatagramPacket` 생성자의 **첫 번째 매개값**은 보낼 데이터 `byte[]`이고 **두 번째 매개값**은 byte[] 배열에서 보내고자하는 항목 수이다. 전체 항목을 보내려면 length 값으로 대입하면 된다. **세 번째 매개값은** 수신자 IP와 Port를 가지고 있는 **SocketAddress**이다. SocketAddress는 추상 클래스이므로 하위 클래스인 InetSocketAddress를 생성해서 대입한다. 



* 예제

```java
DatagramSocket datagramSocket = new DatagramSocket();

byte[] byteArr = data.getBytes("UTF-8");
DatagramPacket packet = new Datagram(
  byteArr,
  byteArr.length,
  new InetSocketAddress("localhost", 5001)
  );

datagramSocket.send(packet);
datagramSocket.close();
```



### 수신자 구현 

```java
DatagramSocket datagramSocket = new DatagramSocket();
```

> `**receive()** 메소드를 사용해서 패킷을 읽을 준비를 한다. 패킷을 받을 때까지 블로킹되고, 도착하면 매개값으로 주어진 DatagramPacket에 패킷 내용을 저장한다.

```java
datagramSocket.receive(datagramPacket);
```

> 패킷의 내용을 저장할 DatagramPacket 객체는 다음과 같이 생성한다. **첫 번째 매개값**은 읽은 패킷 데이터를 저장할 바이트 배열이고, **두번 째 매개값**은 읽을 수 있는 최대 바이트 수로 첫 번째 바이트 배열의 크기와 같거나 작아야 한다. 일반적으로 첫 번째 바이트배열의 크기를 준다.

```java
DatagramPacket datagramPacket = new DatagramPacket(new byte[100], 100);
```

> receive() 메소드가 패킷을 읽었다면 `DatagramPacket`의 getData()로 데이터가 저장된 바이트 배열을 얻어낼 수 있다. 그리고 getLength()를 호출해서 읽은 바이트 수를 얻을 수 있다. 받은 데이터가 인코딩된 문자열이라면 다음과 같이 디코딩해서 문자열을 얻는다.

```java
String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), "UTF-8");
```

> 수신자가 패킷을 받고나서 발신자에게 응답 패킷을 보내고 싶다면 발신자의 IP와 Port를 알아야 하는데 DatagramPacket의 getSocketAddress()를 통해 발신자의 SocketAddress를 얻을 수 있어 send()메소드에서 이용할 수 있다.  
>
> 수신자는 항상 데이터를 받을 준비를 해야 하므로 작업 스레드를 생성하여 receive() 메소드를 반복적으로 호출해야 한다. 작업 스레드를 종료 시키는 방법은 receive() 메소드가 블로킹 된 상태에서 DatagramSocket의 close()를 호출하면 된다. 이 경우 receive()에서 `SocketException`이 발생하고, 예외 처리 코드에서 작업 스레드를 종료시킨다.



* 예제

```java
public class UdpReceiveExample extends Thread {

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5001);

        Thread thread = new Thread() {

            @Override
            public void run() {
                System.out.println("[수신 시작]");
                try {
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(new byte[100], 100);
                        socket.receive(packet);

                        String data = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                        System.out.println("[받은 내용 :" + packet.getSocketAddress() + " ] " + data);

                    }
                } catch (Exception e) {
                    System.out.println("[수신 종료]");

                }
            }
        };
        thread.start();

        Thread.sleep(10000);
        socket.close();
    }
}
```





