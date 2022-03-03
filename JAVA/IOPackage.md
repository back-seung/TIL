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



