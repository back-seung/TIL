## NIO

> 자바 4부터 새로운 입출력이라는 뜻에서 `java.nio`패키지가 포함되었다.
>
> 자바 7로 버전업하면서 자바 IO와 NIO 사이의 일관성 없는 클래스 설계를 바로잡고 비동기 채널 등의 네트워크 지원을 대폭 강화한 NIO.2 API가 추가되었다. NIO.2는 java.nio2 패키지로 제공되지 않고 기존 java.nio의 하위 패키지 (`java.nio.channels` , `java.nio.charset`, `java.nio.file`)에 통합되어 있다.



### NIO에서 제공하는 패키지 간략 소개

| NIO 패키지              | 포함되어 있는 내용                                   |
| ----------------------- | ---------------------------------------------------- |
| java.nio                | 다양한 버퍼 클래스                                   |
| java.nio.channels       | 파일 채널, TCP 채널, UDP 채널 등의 클래스            |
| java.nio.channels.spi   | java.nio.channels 패키지를 위한 서비스 제공자 클래스 |
| java.nio.charset        | 문자셋, 인코더, 디코더 API                           |
| java.nio.charset.spi    | java.nio.charset 패키지를 위한 서비스 제공자 클래스  |
| java.nio.file           | 파일 및 파일 시스템에 접근하기 위한 클래스           |
| java.nio.file.attribute | 파일 및 파일 시스템의 속성에 접근하기 위한 클래스    |
| java.nio.file.spi       | java.nio.file 패키지를 위한 서비스 제공자 클래스     |



## IO와 NIO의 차이점

IO와 NIO는 데이터를 입출력한다는 목적은 동일하지만, 방식에 있어서 크게 차이가 난다. 아래 표는 IO와 NIO의 차이점을 정리한 것이다.



| 구분                   | IO                 | NIO                              |
| ---------------------- | ------------------ | -------------------------------- |
| 입출력 방식            | 스트림 방식        | 채널 방식                        |
| 버퍼 방식              | 넌버퍼(non-buffer) | 버퍼(buffer)                     |
| 비동기 방식            | 지원 안 함         | 지원                             |
| 블로킹 / 넌블로킹 방식 | 블로킹 방식만 지원 | 블로킹 / 넌블로킹 방식 모두 지원 |



### 스트림 vs 채널

**IO는 스트림 기반이다.** 스트림은 입력 스트림과 출력 스트림으로 구분되어 있기 때문에 데이터를 읽기 위해서는 입력 스트림을 생성해야 하고, 데이터를 출력하기 위해서는 출력 스트림을 생성해야 한다.  

예를 들어 하나의 파일을 읽고 저장하는 작업을 모두해야 한다고 했을 때 `fileInputStream`,`FileOutputStream`을 별도로 생성해야 한다.  



**NIO는 채널 기반이다**. 채널은 스트림과 달리 양방향으로 입력과 출력이 가능하다. 그렇기 때뭔에 입력과 출력을 위한 별도의 채널을 만들 필요가 없다.  

예를 들어 하나의 파일을 읽고 저장하는 작업을 모두해야 한다고 했을 때 `FileChannel`만 있으면 된다.



### 넌버퍼 vs 버퍼

* **IO에서는 출력 스트림이 1바이트를 쓰면 입력 스트림이 1바이트를  읽는다.** 이런 시스템은 대체로 느리다.  이것보다는 버퍼를 사용해서 복수 개의 바이트를 한꺼번에 입력받고 출력하는 것이 빠른 성능을 낸다. 그래서 IO는 버퍼를 제공해주는 보조 스트림은 `BufferedInputStream`, `BufferedOutputStream`을 연결해서 사용하기도 한다.

* **IO는 스트림에서 읽은 데이터를 즉시 처리한다.** 그렇기 때문에 스트림에서 입력된 전체 데이터를 별도로 저장하지 않으면, 입력된 데이터의 위치를 이동해가면서 자유롭게 이용할 수 없다.



* **NIO는 기본적으로 버퍼를 사용해서 입출력을 하기 때문에 IO보다는 입출력 성능이 좋다.** 채널은 버퍼에 저장된 데이터를 출력하고 입력된 데이터를 버퍼에 저장한다.

* **NIO는 읽은 데이터를 무조건 버퍼에 저장하기 때문에** 버퍼 내에서 데이터의 위치를  이동해 가면서 필요한 부분만 읽고 쓸 수 있다.



### 블로킹 vs 넌블로킹

* **IO는 블로킹 된다.** 입력 스트림의 read() 메소드를 호출하면 데이터가 입력되기 전까지 스레드는 블로킹 된다. 마찬가지로 출력 스트림의 write()메소드를 호출하면 데이터가 출력되기 전까지 스레드는 블로킹된다. IO 스레드가 블로킹되면 다른 일을 할 수 없고, 블로킹을 빠져 나오기 위해 인터럽트도 할 수 없다. 빠져나오는 유일한 방법은 스트림을 닫는 것이다.



* **NIO는 블로킹과 넌블로킹 특징을 모두 가지고 있다.** IO와 차이점은 NIO 블로킹은 스레드를 인터럽트함으로써 빠져나올 수가 있다는 것이다. 블로킹의 반대 개념이 넌블로킹인데, 입출력 작업 시 스레드가 블로킹되지 않는 것을 말한다. NOI의 넌블로킹은 입출력 작업 준비가 완료된 채널만 선택해서 작업 스레드가 처리하기 때문에 작업 스레드가 블로킹되지 않는다. 여기서 작업 준비가 완료되었다는 뜻은 지금 바로 읽고 쓸 수 있는 상태를 말한다. **NIO 넌블로킹의 핵심 객체는 멀티플렉서인 셀렉터(Selector)이다.** 셀렉터는 복수 개의 채널 중에서 준비 완료된 채널을 선택하는 방법을 제공해준다.



### IO와 NIO의 선택

* **NIO 선택**
  불특정 다수의 클라이언트 연결 또는 멀티 파일들을 넌블로킹이나 비동기로 처리할 수 있기 때문에  과도한 스레드 생성을 피하고 스레드를 효과적으로 재사용한다는 점이 있다. 또한 운영체제의 버퍼를 이용한 입출력이 가능하기 때문에 입출력 성능이 향상된다.  
  연결 클라이언트 수가 많고, 하나의 입출력 처리 작업이 오래 걸리지 않는 경우에 사용하는 것이 좋다. 
* **IO 선택**
  스레드에서 입출력 처리가 오래 걸린다면 대기하는 작업의 수가 늘어나기 때문에 제한된 스레드로 처리하는 것이 오히려 불편할 수 있다. 대용량 데이터를 처리할 경우에는 IO가 더 유리한데 NIO 버퍼의 할당 크기도 문제가 되고, 모든 입출력 작업에 버퍼를 무조건 사용하므로 받은 즉시 처리하는 IO보다 좀 더 복잡하다. 연결 클라이언트 수가 적고, 전송되는 데이터가 대용량 && 순차적 처리 필요성이 있을 경우 IO로 구현하는 것이 좋다.



## 파일과 디렉토리

> IO는 파일의 속성 정보를 읽기 위해 File 클래스만 제공하지만, NIO는 좀 더 다양한 파일의 속성 정보를 제공해주는 클래스와 인터페이스를 `java.nio.file`, `java.nio.attribute` 패키지에서 제공하고 있다.



### 경로 정의 (Path)

**NIO에서 제일 먼저 살펴봐야 할 API는 `java.nio.file.Path`인터페이스다.** Path는 IO의 `java.io.File` 클래스에 대응되는 NIO 인터페이스이다. NIO의 API에서 파일의 경로를 지정하기 위해 Path를 사용하기 때문에 Path 사용 방법을 잘 익혀두어야 한다.  Path 구현 객체를 얻기 위해서는 `java.nio.file.Paths` 클래스의 정적 메소드인 get() 메소드를 호출하면 된다.

```java
Path path = Paths.get(String first, String ... more);
Path path = Paths.get(URI uri);
```

* get() : 매개값은 파일의 경로이다. 문자열로 지정할 수도 있고, URI 객체를 지정할 수도 있다. 문자열로 지정할 경우 전체 경로를 한꺼번에 저장해도 좋고, 상위와 하위 디렉토리를 따로 나열해도 된다. 

```java
Path path = Paths.get("C:/Temp/dir/file.txt");
Path path = Paths.get("C:/Temp/dir/", "file.txt");
Path path = Paths.get("C:", "Temp/dir", "file.txt");
```



* Path 인터페이스 파일 경로 정보 제공 메소드

| 리턴 타입      | 메소드 (매개 변수)    | 설명                                                         |
| -------------- | --------------------- | ------------------------------------------------------------ |
| int            | compareTo(Path other) | 파일 경로가 동일하면 0을 리턴,<br />상위 경로면 음수,<br />하위 경로면 양수를 리턴,<br />음수와 양수 값의 차이나는 문자열의 수 |
| Path           | getFileName()         | 부모 경로를 제외한 파일 또는 디렉토리 이름만 가진 Path 리턴  |
| FileSystem     | getFileSystem()       | FileSystem 객체 리턴                                         |
| Path           | getName(int index)    | C:/Temp/dir/file.txt일 경우<br />index가 0이면 "Temp"의 Path 객체 리턴<br />index가 1이면 "dir"의 Path 객체 리턴<br />index가 2이면 "file.txt"의 Path 객체 리턴 |
| int            | getNameCount()        | 중첩 경로 수, C:/Temp/dir/file.txt일 경우 3을 리턴           |
| Path           | getParent()           | 바로 위 부모 폴더의 Path 리턴                                |
| Path           | getRoot()             | 루트 디렉토리의 Path 리턴                                    |
| Iterator<Path> | iterator()            | 경로에 있는 모든 디렉토리와 파일을 Path객체로 생성하고 반복자를 리턴 |
| Path           | normalize()           | 상대 경로로 표기할 때 불필요한 요소를 제거                   |
| WatchKey       | register(...)         | WatchService를 등록                                          |
| File           | toFile()              | java.io.File 객체로 리턴                                     |
| String         | toString()            | 파일 경로를 문자열로 리턴                                    |
| URI            | toUri()               | 파일 경로를 URI 객체로 리턴                                  |



### 파일 시스템 정보(FileSystem)

운영체제의 파일 시스템은 FileSystem 인터페이스를 통해서 접근할 수 있다. FileSystem 구현 객체는 FileSystem의 정적 메소드인 getDefault()를 통해 얻을 수 있다.

```java
FileSystem fileSystem = FileSystems.getDefault();
```

* 제공 메소드

| 리턴 타입           | 메소드               | 설명                                         |
| ------------------- | -------------------- | -------------------------------------------- |
| Iterable<FileStore> | getFileStores()      | 드라이버 정보를 가진 FileStore 객체들을 리턴 |
| Iterable<Path>      | getRootDirectories() | 루트 디렉토리 정보를 가진 Path 객체들을 리턴 |
| String              | getSeparator()       | 디렉토리 구분자 리턴                         |

FileStore는 드라이버를 표현한 객체로 다음과 같은 메소드를 제공한다.

| 리턴 타입 | 메소드 (매개 변수)    | 설명                                                     |
| --------- | --------------------- | -------------------------------------------------------- |
| long      | getTotlaSpace()       | 드라이버 전체 공간 크리(단위: 바이트) 리턴               |
| long      | getUnallocatedSpace() | 할당되지 않은 공간 크기(단위 : 바이트) 리턴              |
| long      | getUsableSpace()      | 사용 가능한 공간 크기, getUnallocatedSpace()와 동일한 값 |
| boolean   | isReadOnly()          | 읽기 전용 여부                                           |
| String    | name()                | 드라이버명 리턴                                          |
| String    | type()                | 파일 시스템 종류                                         |



* 예제

```java
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FileSystemExample {
    public static void main(String[] args) throws Exception {
        FileSystem fileSystem = FileSystems.getDefault();
        for (FileStore store : fileSystem.getFileStores()) {
            System.out.println("드라이버 명 : " + store.name());
            System.out.println("파일시스템 : " + store.type());
            System.out.println("전체 공간 : " + store.getTotalSpace() + "바이트");
            System.out.println("사용 중인 공간 : " + (store.getTotalSpace() - store.getUnallocatedSpace()) + "바이트");
            System.out.println("사용 가능한 공간 : " + store.getUsableSpace() + "바이트");
            System.out.println();
        }

        System.out.println("파일 구분자 : " + fileSystem.getSeparator());
        System.out.println();

        for (Path path : fileSystem.getRootDirectories()) {
            System.out.println(path.toString());
        }
    }
}
```



### 파일 속성 읽기 및 파일, 디렉토리 생성/삭제

>  `java.nio.fiole.Files` 클래스는 파일과 디렉토리의 생성 및 삭제, 그리고 이들의 속성을 읽는 메소드를 제공한다. 여기서 속성이란 파일이나 디렉토리가 숨김인지, 디렉토리인지, 크기가 어떻게 되는지, 소유자가 누구인지에 대한 정보를 말한다.



* Files 정적 메소드

| 리턴 타입             | 메소드(매개 변수)        | 설명                                   |
| --------------------- | ------------------------ | -------------------------------------- |
| long \|\| Path        | copy(..)                 | 복사                                   |
| Path                  | createDirectories(...)   | 모든 부모 디렉토리 생성                |
| Path                  | createDirectory(...)     | 경로의 마지막 디렉토리만 생성          |
| Path                  | createFile(...)          | 파일 생성                              |
| void                  | delete(...)              | 삭제                                   |
| boolean               | deleteifExists(...)      | 존재한다면 삭제                        |
| boolean               | exists(...)              | 존재 여부                              |
| FilesStore            | getFileStore(...)        | 파일이 위치한 FileStore(드라이브) 리턴 |
| FileTime              | getLastModifiedTime(...) | 마지막 수정 시간을 리턴                |
| UserPrincipal         | getOwner(...)            | 소유자 정보를 리턴                     |
| boolean               | isDirectory(...)         | 디렉토리인지 여부                      |
| boolean               | isExecutable(...)        | 실행 가능 여부                         |
| boolean               | isHidden(...)            | 숨김 여부                              |
| boolean               | isReadable(...)          | 읽기 가능 여부                         |
| boolean               | isRegularFile(...)       | 일반 파일인지 여부                     |
| boolean               | isSameFile(...)          | 같은 파일인지 여부                     |
| boolean               | isWritable(...)          | 쓰기 가능 여부                         |
| Path                  | move(...)                | 파일 이동                              |
| BufferedReader        | newBufferedReader(...)   | 텍스트 파일을 읽는 BufferedReader 리턴 |
| BufferedWriter        | newBufferedWrtier(...)   | 텍스트 파일에 쓰는 BufferedWriter 리턴 |
| SeekableByteChannel   | newByteChannel(...)      | 디렉토리의 모든 내용을 스트림으로 리턴 |
| DirectoryStream<Path> | newDirectoryStream...)   | 디렉토리의 모든 내용을 스트림으로 리턴 |
| InputStream           | newInputStream(...)      | 파일의 InputStream 리턴                |
| OutputStream          | newOutputStream(...)     | 파일의 OutputStream 리턴               |
| boolean               | notExists(...)           | 존재하지 않는지 여부                   |
| String                | probeContentType(...)    | 파일의 MIME 타입을 리턴                |
| byte[]                | readAllBytes(...)        | 파일의 모든 바이트를 읽고 배열로 리턴  |
| List<String>          | readAllLines(...)        | 텍스트 파일의 모든 라인을 읽고 리턴    |
|                       |                          |                                        |

