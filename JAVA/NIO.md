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
| long                  | size(...)                | 파일의 크기 리턴                       |
| Path                  | write(...)               | 파일에 바이트나 문자열을 저장          |



* 파일의 속성을 읽고 출력하는 예제

  ```java
  public class FileExample {
    public static void main(String[] args) throws Exception {
      Path path = Paths.get("src/sec02/exam03_file_directory/FileExample.java");
      System.out.println("디렉토리 여부 : " + Files.isDirectory(path));
      System.out.println("파일 여부 : " + Files.isRegularFile(path));
      System.out.println("마지막 수정 시간 : " + Files.getLastModifiedTime(path));
      System.out.println("파일 크기 : " + Files.size(path));
      System.out.println("소유자 : " + Files.getOwner(path).getName());
      System.out.println("숨김 파일 여부 : " + Files.isHidden(path));
      System.out.println("읽기 가능 여부 : " + Files.isReadable(path));
      System.out.println("쓰기 가능 여부 : " + Files.isWritable(path));
    }
  }
  ```



### 와치 서비스 (WatchService)

자바 7에서 처음 소개된 것으로 디렉토리 내부에서 파일 생성, 삭제, 수정 등의 내용 변화를 감시하는데 사용된다. 흔하게 볼 수 있는 와치 서비스의 적용 예는 에디터에서 파일을 편집하고 있을 때, 에디터 바깥에서 파일 내용을 수정하게 되면 파일 내용이 변경되었으니 파일을 다시 불러올 것인지를 묻는 대화상자를 띄우는 것이다. **와치 서비스는 일반적으로 파일 변경 통지 메커니즘으로 알려져 있다.** WatchService를 생성하려면 다음과 같이 FileSystem의 newWatchService()를 호출한다.



```java
WatchService watchService = FileSystem.getDefault().newWatchService();
```

생성했다면 감시가 필요한 디렉토리의 Path 객체에서 register() 메소드로 등록하면 된다. 이때 어떤 변화(생성 ,삭제 ,수정)를 감시할 것인지를 StandardWatchEventKinds 상수로 지정할 수 있다. 

```java
path.register(watchService.StandardWatchEventKinds.ENTRY_CREATE,
              						 StandardWatchEventKinds.ENTRY_MODIFY,
             							 StandarsWatchEventKinds.ENTRY_DELETE);
```

Path에 WatchService를 등록한 순간부터 디렉토리 내부에서 변경이 발생되면 와치 이벤트가 발생하고 WatchService는 해당이벤트 정보를 가진 WatchKey를 생성하여 큐에 넣어준다. 프로그램은 무한 루프를 돌면서 WatchService의 take() 메소드를 호출하여 WatchKey가 큐에 들어올 때 까지 대기하고 있다가 WatchKey가 큐에 들어오면 WatchKey를 얻어 처리하면 된다.

```java
while(true) {
	WatchKey watchKey = watchService.take();
}
```

Key를 얻었다면 pollEvents() 메소드를 호출해서 WatchEvent 리스트를 얻어낸다. 한개의 WatchEvent가 아니라 List<WatchEvent<?>>로 리턴하는 이유는 여러개의 파일이 동시에 삭제, 수정, 생성될 수 있기 때문이다. 참고로 WatchEvent는 파일당 하나씩 발생한다.

```java
List<WatchEvnet<?>> list = watchKey.pollEvents();
```

프로그램은 WatchEvent리스트에서 WatchEvent를 하나씩 꺼내어 이벤트의 종류와 Path 객체를 얻어낸 다음 적절히 처리하면 된다.

```java
for(WatchEvent watchEvent : list) {
  //이벤트 종류 얻기
  Kind kind = watchEvent.kind();
  // 감지된 Path 얻기 
  Path path = (Path) watchEvent.context();
  // 이벤트 종류별로 처리
  if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
    // 생성되었을 경우, 실행 코드
  } else if(kind == StandardWatchEventKinds.ENTRY_MODIFY) {
    // 수정되었을 경우, 실행 코드
  } else if(kind == StandardWatchEventKinds.ENTRY_DELETE) {
    // 삭제 되었을 경우 실행 코드
  } else if(kind == StandardWatchEventKinds.OVERFLOW) {
    ...
  }
}
```

`OVERFLOW` 이벤트는 운영체제에서 이벤트가 소실됐거나 버려진 경우에 발생하므로 별도의 처리 코드가 필요없다. 따라서 `CREATE`,`MODIFY`,`DELETE`이벤트만 처리하면 된다. 한 번 사용된  WatchKey는 reset() 메소드로 초기화해야 하는데, 새로운 WatchEvent가 발생하면 큐에 다시 들어가기 때문이다. 초기화 성공시 true를 리턴하지만 감시하는 디렉토리나 삭제 || 키가 유효하지 않을 경우 false를 리턴한다. WatchKey가 더 이상 유효하지 않게 되면 무한 루프를 빠져나와 WatchService의 close() 메소드를 호출하고 종료하면 된다.

```java
while(true) {
  WatchKey watchKey = watchService.take();
  List<WatchEvent<?>> list = watchKey.pollEvents();
  
  for(WatchEvent watchEvent : list) {
    ...
  }
  boolean valid = watchKey.reset();
  if(valid) break;
}
watchService.close();
```



## 버퍼 (Buffer)

NIO에서는 데이터를 입출력하기 위해서 항상 버퍼를 사용한다.  

버퍼는 읽고 쓰기가 가능한 메모리 배열이다. 버퍼를 이해하고 잘 사용할 수 있어야 NIO에서 제공하는 API를 올바르게 활용할 수 있다.



### 버퍼의 종류

Buffer는 저장되는 데이터 타입에 따라 분류될 수 있고, 어떤 메모리를 사용하느냐에 따라 다이렉트(Direct) 넌다이렉트(NonDirect)로 분류할 수도 있다.



#### 데이터 타입에 따른 버퍼

![스크린샷 2022-03-19 12.13.57](/Users/mac/Library/Application Support/typora-user-images/스크린샷 2022-03-19 12.13.57.png)

> 데이터 타입에 따라서 별도의 클래스로 제공된다. 이 버퍼 클래스들은 Buffer 추상 클래스를 상속한다.



버퍼 클래스의 이름을 보면 어떤 데이터가 저장되는 버퍼인지 쉽게 알 수있다. 이 중에 MappedByteBuffer는 ByteBuffer의 하위 클래스로 파일의 내용에 랜덤하게 접근하기 위해서 파일의 내용을 메모리와 맵핑시킨 버퍼이다.



#### 넌다이렉트와 다이렉트 버퍼

버퍼가 사용하는 메모리의 위치에 따라서 넌다이렉트 버퍼와 다이렉트 버퍼로 분류된다.

넌다이렉트 버퍼는 JVM이 관리하는 힙 메모리 공간을 이용하는 버퍼이고, 다이렉트 버퍼는 운영체제가 관리하는 메모리 공간을 사용하는 버퍼이다. 두 버퍼의 특징은 다음과 같다.

| 구분                 | 넌다이렉트 버퍼  | 다이렉트 버퍼                     |
| -------------------- | ---------------- | --------------------------------- |
| 사용하는 메모리 공가 | JVM 힙 메모리    | 운영체제의 메모리                 |
| 버퍼 생성시간        | 버퍼 생성이 빠름 | 버퍼 생성이 느림                  |
| 버퍼의 크기          | 작다             | 크다 (큰 데이터를 처리할 때 유리) |
| 입출력 성능          | 낮다.            | 높다(입출력이 빈번할 때 유리)     |

* 넌다이렉트 버퍼
  * JVM 힙 메모리를 사용하므로 버퍼 생성 시간이 빠르다.
  * JVM의 제한된 힙 메모리를 사용하므로 버퍼의 크기를 크게 잡을 수 없다.
  * 입출력을 하기 위해 임식 다이렉트 버퍼를 생성하고 넌다이렉트 버퍼에 있는 내용을 임시 다이렉트 버퍼에 복사한다. 그리고 나서 임시 다이렉트 버퍼를 사용해서 운영체제의 nativeI/O 기능을 수행한다. 따라서 입출력 성능이 상대적으로 낮다.
* 다이렉트 버퍼 
  * 운영체제의 메모리를 할당 받기 위해 운영체제의 네이티브(Native) C 함수를 호출해야 하고 여러가지 잡다한 처리를 해야하므로 상대적으로 버퍼 생성이 느리다. 따라서 **한번 생성한 후 재사용하는 것이 적합하다**
  * 운영체제가 관리하는 메모리를 사용하므로 운영체제가 허용하는 범위 내에서 대용량 버퍼를 생성시킬 수 있다. 



* 넌다이렉트 / 다이렉트 버퍼 성능 비교

```java
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class PerformanceExample {
    public static void main(String[] args) throws Exception{
        Path from = Paths.get("src/sec03/exma01_direct_Buffer/house.jpg");
        Path to1 = Paths.get("src/sec03/exma01_direct_Buffer/house2.jpg");
        Path to2 = Paths.get("src/sec03/exma01_direct_Buffer/house3.jpg");

        long size = Files.size(from);

        FileChannel fileChannel_from = FileChannel.open(from);
        FileChannel fileChannel_to1 = FileChannel.open(to1, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        FileChannel fileChannel_to2 = FileChannel.open(to2, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));

        ByteBuffer nonDirectBuffer = ByteBuffer.allocate((int) size);
        ByteBuffer directBuffer = ByteBuffer.allocateDirect((int) size);

        long start, end;

        start = System.nanoTime();

        for (int i = 0; i < 100; i++) {
            fileChannel_from.read(nonDirectBuffer);
            nonDirectBuffer.flip();
            fileChannel_to1.write(nonDirectBuffer);
            nonDirectBuffer.clear();
        }
        end = System.nanoTime();
        System.out.println("넌 다이렉트 :\t" + (end - start) + " ns");

        fileChannel_from.position(0);

        start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            fileChannel_from.read(directBuffer);
            directBuffer.flip();
            fileChannel_to2.write(directBuffer);
            directBuffer.clear();
        }
        end = System.nanoTime();
        System.out.println("다이렉트 :\t" + (end - start) + " ns");

        fileChannel_from.close();
        fileChannel_to1.close();
        fileChannel_to2.close();
    }
}
```



### Buffer 생성

> 각 데이터 타입별로 넌 다이렉트 버퍼를 생성하기 위해서는 각 Buffer 클래스의 allocate()와 wrap() 메소드를 호출하면 되고, 다이렉트 버퍼는 ByteBuffer의 allocateDirect() 메소드를 호출하면 된다. 



* **allocate() 메소드**
  JVM 힙 메모리에 넌다이렉트 버퍼를 생성한다. 다음은 데이터 타입별로 Buffer를 생성하는 allocate() 메소드이다.  매개값은 해당 데이터 타입의 저장 개수를 의미한다.

| 리턴 타입    | 메소드(매개 변수)                   | 설명                              |
| ------------ | ----------------------------------- | --------------------------------- |
| ByteBuffer   | ByteBuffer.allocate(int capacity)   | capacity개만큼의 byte 값을 저장   |
| CharBuffer   | CharBuffer.allocate(int capacity)   | capacity개만큼의 char 값을 저장   |
| DoubleBuffer | DoubleBuffer.allocate(int capacity) | capacity개만큼의 double 값을 저장 |
| FloatBuffer  | FloatBuffer.allocate(int capacity)  | capacity개만큼의 float 값을 저장  |
| IntBuffer    | IntBuffer.allocate(int capacity)    | capacity개만큼의 int 값을 저장    |
| LongBuffer   | LongBuffer.allocate(int capacity)   | capacity개만큼의 long 값을 저장   |
| ShortBuffer  | ShortBuffer.allocate(int capacity)  | capacity개만큼의 short 값을 저장  |



* 100개의 바이트를 저장하는 ByteBuffer 생성 및 100개의 문자를 저장하는 CharBuffer 생성

```java
ByteBuffer byteBuffer = ByteBuffer.allocate(100);
CharBuffer charBuffer = CharBuffer.allocate(100);
```



* **wrap() 메소드**
  각 타입별 Buffer 클래스는 모두 wrap() 메소드를 가지고 있는데, wrap() 메소드는 이미 생성되어 있는 자바 배열을 래핑해서 Buffer 객체를 생성한다. 자바배열은 JVM 힙 메모리에 생성되므로 wrap() 메소드는 넌다이렉트 버퍼를 생성한다. 다음은 길이가 100인 byte[] 를 이용해서 ByteBuffer를 생성하고, 길이가 100인 char[] 를 이용해서 CharBuffer를 생성한다.

```java
byte[] byteArr = new byte[100];
ByteBuffer byteBuffer = ByteBuffer.wrap(byteArr);

char[] charArr = new char[100];
CharBuffer charBuffer = CharBuffer.wrap(charArr);
```

 	일부 데이터만을 가지고 Buffer 객체를 생성할 수도 있다. 이 경우 시작 인덱스와 길이를 추가적으로 지정하면 된다. `0 ~ 50`개만 버퍼로 생성해보자.

```java
byte[] byteArr = new byte[100];
ByteBuffer byteBuffer = ByteBuffer.wrap(byteArr, 0, 50);

char[] charArr = new char[100];
CharBuffer charBuffer = CharBuffer.wrap(charArr, 0, 50);
```



* **allocateDirect() 메소드**
  ByteBuffer의 allocateDirect() 메소드는 JVM 힙 메모리 바깥쪽, 즉 운영체제가 관리하는 메모리에 다이렉트 버퍼를 생성한다. 이 메소드는 각 타입별 Buffer 클래스에는 없고, ByteBuffer에서만 제공된다. 각 타입별로 다이렉트 버퍼를 생성하고 싶다면 우선 ByteBuffer의 allocateDirect() 메소드로 버퍼를 생성한 다음 ByteBuffer의 asCharBuffer(), asFloatBuffer(), asDoubleBuffer() ... asIntBuffer() 메소드를 이용해서 해당 타입별 Buffer를 얻으면 된다.



* **byte 해석 순서(ByteOrder)**
  데이터를 처리할 때 바이트 처리 순서는 운영체제마다 차이가 있따. 이러한 차이는 데이터를 다른 운영체제로 보내거나 받을 때 영향을 미치기 때문에 데잍러르 다루는 버퍼도 이를 고려해야 한다. 앞쪽 바이트부터 먼저 처리하는 것을 **Big endian**이라고 하고, 뒤쪽 바이트부터 먼저 처리하는 것을 **Little endian**이라고 한다.

  * Big-endian

  ![image-20220319193207991](https://tva1.sinaimg.cn/large/e6c9d24egy1h0fdq3yjh4j21hc0u0dgy.jpg)

  * Little-endian

  ![image-20220319193252142](https://tva1.sinaimg.cn/large/e6c9d24egy1h0fdpybkewj21hc0u0dgy.jpg)

> Litte-endian으로 동작하는 운영체제에서 만든 데이터 파일을 Big-endian으로 동작하는 운영체제에서 읽는다면 ByteOrder 클래스로 데이터 순서를 맞춰야 한다. ByteOrder 클래스의 nativeOrder() 메소드는 현재 동작하고 있는 운영체제가 Big-endian인지 Little-endian인지 알려준다. **JVM도 일종의 독립된 운영체제이기 때문에** 이런 문제를 취급하는데, JRE가 설치된 어떤 환경이든 JVM은 무조건 Big-endian으로 동작하게 되어 있다. 다음 예제는 현재 컴퓨터의 운영체제 종류와 바이트를 해석하는 순서에 대해 출력한다.

* 현재 컴퓨터의 운영체제 종류와 바이트를 해석하는 순서에 대해 출력한다.

```java
import java.nio.ByteOrder;

public class ComputerByteOrderExample {
    public static void main(String[] args) {
        System.out.println("운영체제 종류 : " + System.getProperty("os.name"));
        System.out.println("네이티브의 바이트 해석 순서 : " + ByteOrder.nativeOrder());
    }
}
```

![스크린샷 2022-03-19 19.44.31](https://tva1.sinaimg.cn/large/e6c9d24egy1h0fdrtfchkj216k07kjs7.jpg)



운영체제와 JVM의 바이트 해석 순서가 다를 경우에는 JVM이 운영체제와 데이터를 교환할 때 자동적으로 처리해주기 때문에 문제는 없다. 하지만 다이렉트 버퍼일 경우 운영체제의 native I/O를 사용하므로 운영체제의 기본 해석 순서로 JVM의 해석 순서를 맞추는 것이 성능에 도움 된다. 다음과 같이 allocateDirect()로 버퍼를 생성한 후, order() 메소드를 호출해서 nativeOrder()의 리턴값으로 세팅해주면 된다.

```java
ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100).order(ByteOrder.nativeOrder());
```



### Buffer의 위치 속성

> * Buffer의 위치 속성 개념
> * 위치 속성이 언제 변경되는지에 대해 알고 있어야 한다.



* Buffer의 네 가지 위치 속성

| 속성     | 설명                                                         |
| -------- | ------------------------------------------------------------ |
| position | 현재 읽거나 쓰는 위치값<br />인덱스 값이기 때문에 0부터 시작하며 limit보다 큰 값을 가질 수 없다. 만약 position과 limit의 값이 같아진다면 더 이상 데이터를 쓰거나 읽을 수 없다는 뜻이 된다. |
| limit    | 버퍼에서 읽거나 쓸 수 있는 위치의 한게를 나타낸다. 이 값은 capacity보다 작거나 같은 값을 가진다. 최초에 버퍼를 만들었을 때는 capacity와 같은 값을 가진다. |
| capacity | 버퍼의 최대 데이터 개수(메모리 크기)를 나타낸다. 인덱스 값이 아니라 수량임 |
| mark     | reset() 메소드를 실행했을 때, 돌아오는 위치를 지정하는 인덱스로서 mark() 메소드로 지정할 수 있다. 주의할 점은 반드시 position 이하의 값으로 지정해주어야 한다. position이나 limit의 값이 mark 값보다 작은 경우, mark는 자동 제거된다. mark가 없는 상태에서 reset() 메소드를 호출하면 InvalidMarkException이 발생한다. |



position, limit, capacity, mark 속성의 크기 관계는 다음과 같다. mark는 position보다 클 수 없고, position은 limit보다 클 수 없으며, limit은 capacity보다 클 수 없다.

```
0 <= mark <= position <= limit <= capacity
```



### Buffer 메소드

Buffer를 생성한 후 사용할 때에는 Buffer가 제공하는 메소드를 잘 활용해야 한다. Buffer마다 공통적으로 사용하는 메소드들도 있고, 데이터 타입별로 Buffer가 개별적으로 가지고 있는 메소드들도 있다. 



* **공통 메소드**
  각 타입별 버퍼 클래스는 Buffer 추상 클래스를 상속하고 있다. Buffer 추상 클래스에는 모든 버퍼가 공통적으로 가져야 할 메소드들이 정의되어 있는데, 위치 속성을 변경하는 flip(), rewind(), clear(), mark(), reset()도 모두 Buffer 추상 클래스에 있다. 다음은 Buffer가 가지는 메소드들을 정리한 표이다.

| 리턴 타입 | 메소드(매개 변수)         | 설명                                                         |
| --------- | ------------------------- | ------------------------------------------------------------ |
| Object    | array()                   | 버퍼가 래핑(wrap)한 배열을 리턴                              |
| int       | arrayOffset()             | 버퍼의 첫 번째 요소가 있는 내부 배열의 인덱스를 리턴         |
| int       | capacity()                | 버퍼의 전체 크기를 리턴                                      |
| Buffer    | clear()                   | 버퍼의 위치 속성을 초기화(position = 0, limit = capacity)    |
| Buffer    | flip()                    | limit을 position으로, position을 0 인덱스로 이동             |
| boolean   | hasArray()                | 버퍼가 래핑한 배열을 가지고 있는지 여부                      |
| boolean   | hasRemaining()            | position과 limit 사이에 요소가 있는지 여부(position < limit) |
| boolean   | isDirect()                | 운영체제의 버퍼를 사용하는지 여부                            |
| boolean   | isReadOnly()              | 버퍼가 읽기 전용인지 여부                                    |
| int       | limit()                   | limit 위치를 리턴                                            |
| Buffer    | limit(int newLimit)       | newLimit으로 limit 위치를 설정                               |
| Buffer    | mark()                    | 현재 위치를 mark로 표시                                      |
| int       | position()                | position 위치를 리턴                                         |
| Buffer    | position(int newPosition) | newPosition으로 position 위치를 설정                         |
| int       | remaining()               | position과 limit 사이의 요소 개수                            |
| Buffer    | reset()                   | position을 mark위치로 이동                                   |
| Buffer    | rewind()                  | position을 0 인덱스로 이동                                   |



#### **데이터를 읽고 저장하는 메소드**

버퍼에 데이터를 저장하는 메소드는 put()이고 데이터를 읽는 메소드는 get()이다.

이 메소드들은 Buffer 추상 클래스에는 없고, 각 타입별 하위 Buffer 클래스가 가지고 있다. get(), put()은 상대적과 절대적으로 구분된다. **버퍼 내의 현재 위치 속성인 position에서 데이터를 읽고 저장할 경우는 상대적**이고 **position과 관계없이 주어진 인덱스에서 데이터를 읽고, 저장할 경우는 절대적**이다. 상대적 get()과 put() 메소드를 호출하면 position값은 증가하지만, 절대적 get(), put() 메소드를 호출하면 position의 값은 변하지 않는다. 만약 position값이 limit 값 까지 증가했는데도 상대적 get()을 사용하면 **`BufferUnderflowException`**이 발생하고, put()을 사용하면 **`BufferOverflowException`**이 발생한다.



* get() / put() 나열 표

| 구분  |        | ByteBuffer                                                   | CharBuffer                                                   |
| ----- | ------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| get() | 상대적 | get()<br />get(byte[] dst)<br />get(byte[] dst, int offset, int length)<br />getChar()<br />getDouble()<br />getFloat()<br />getInt()<br />getLong()<br />getShort() | get()<br />get(char[] dst)<br />get(char[] dst, int offset, int length) |
|       | 절대적 | get(int index)<br />getChar(int index)<br />getDouble(int index)<br />getFloat(int index)<br />getInt(int index)<br />getLong(int index)<br />getShort(int index) | get(int index)                                               |
| put() | 상대적 | put(byte b)<br />put(byte[] src)<br />put(byte[] src, int offset, int length)<br />put(ByteBuffer src)<br />putChar(char value)<br />putDouble(double value)<br />putFloat(float value)<br />putInt(int value)<br />putLong(long value)<br />putShort(short value) | put(char c)<br />put(char[] src)<br />put(char[] src, int offset, int length)<br />put(CharBuffer src)<br />put(String src)<br />put(String src, int start, int end) |
|       | 절대적 | put(int index, byte b)<br />putChar(int index, char value)<br />putDouble(int index, double value) <br />putFloat(int index, float value)<br />putInt(int index, int value)<br />putLong(int index, long value)<br />putShort(int index, short value) | put(int index, char c)                                       |

> 상대적 메소드와 절대적 메소드를 쉽게 구분하는 방법은 index 매개 변수가 없으면 상대적이고, index 매개 변수가 있으면 절대적이다.



* **버퍼 예외의 종류**
  버퍼 클래스에서 발생하는 예외를 살펴보자. 주로 버퍼가 다 찼을 때 데이터를 저장하려는 경우와 버퍼에서 더 이상 읽어올 데이터가 없을 때 데이터를 읽으려는 경우에 예외가 발생한다. 다음 표는 버퍼와 관련된 예외 클래스이다. 
  가장 흔한 예외는 **`BufferOverflowException`**,**`BufferUnderflowException`**이다.

| 예외                     | 설명                                                         |
| ------------------------ | ------------------------------------------------------------ |
| BufferOverflowException  | position이 limit에 도달했을 때 put() 을 호출하면 발생        |
| BufferUnderflowException | position이 limit에 도달했을 때 get()을 호출하면 발생         |
| InvalidMarkException     | mark가 없는 상태에서 reset() 메소드를 호출하면 발생          |
| ReadOnlyBufferException  | 읽기 전용 버퍼에서 put() 또는 compact() 메소드를 호출하면 발생 |



* 데이터를 버퍼에 쓰고 읽을 때, 위치 속성을 변경하는 메소드를 호출할 때 버퍼의 위치 속성값의 변화를 보여주는 예제

```java
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferExample {
    public static void main(String[] args) throws Exception {
        System.out.println("[7바이트 크기로 버퍼 생성]");
        ByteBuffer buffer = ByteBuffer.allocateDirect(7);
        printState(buffer);

        buffer.put((byte) 10);
        buffer.put((byte) 11);
        System.out.println("[2바이트 저장 후]");
        printState(buffer);

        buffer.put((byte) 12);
        buffer.put((byte) 13);
        buffer.put((byte) 14);
        System.out.println("[3바이트 저장 후]");
        printState(buffer);

        buffer.flip();
        System.out.println("[flip() 실행 후]");
        printState(buffer);

        buffer.get(new byte[3]);
        System.out.println("[3바이트 읽은 후]");
        printState(buffer);

        buffer.mark();
        System.out.println("---------[현재 위치를 마크 해놓음]");

        buffer.get(new byte[2]);
        System.out.println("[2바이트 읽은 후]");
        printState(buffer);

        buffer.reset();
        System.out.println("---------[position을 마크 위치로 옮김]");
        printState(buffer);

        buffer.rewind();
        System.out.println("[rewind() 실행 후]");
        printState(buffer);

        buffer.clear();
        System.out.println("[clear() 실행 후]");
        printState(buffer);
    }

    public static void printState(Buffer buffer) {
        System.out.print("\tposition : " + buffer.position() + ", ");
        System.out.println("\tlimit : " + buffer.limit() + ", ");
        System.out.println("\tcapacity : " + buffer.capacity());

    }
}
```

> 실행결과 :

![스크린샷 2022-03-19 22.22.54](https://tva1.sinaimg.cn/large/e6c9d24egy1h0ficqw6gaj20iw13m76q.jpg)

* compact() 메소드 호출 후, 변경된 버퍼의 내용과 position, limit의 위치를 보여주는 예제

```java
import java.nio.ByteBuffer;

public class CompactExample {
    public static void main(String[] args) {
        System.out.println("[7바이트 크기로 버퍼 생성]");
        ByteBuffer buffer = ByteBuffer.allocateDirect(7);
        buffer.put((byte) 10);
        buffer.put((byte) 11);
        buffer.put((byte) 12);
        buffer.put((byte) 13);
        buffer.put((byte) 14);
        // 데이터를 읽기 위해 위치 속성값 변경
        buffer.flip();

        buffer.get(new byte[3]);
        System.out.println("[3바이트 읽음]");

        // 읽지 않은 데이터는 0 인덱스부터 복사
        buffer.compact();
        System.out.println("[compact 실행 후]");
        printState(buffer);
    }

    public static void printState(ByteBuffer buffer) {
        System.out.print(buffer.get(0) + ", ");
        System.out.print(buffer.get(1) + ", ");
        System.out.print(buffer.get(2) + ", ");
        System.out.print(buffer.get(3) + ", ");
        System.out.println(buffer.get(4));
        System.out.print("position : " + buffer.position() + ", ");
        System.out.print("limit : " + buffer.limit() + ", ");
        System.out.println("capacity : " + buffer.capacity());
    }
}
```

> 실행 결과 : 

![스크린샷 2022-03-19 22.28.21](https://tva1.sinaimg.cn/large/e6c9d24egy1h0fiiatcocj20qs0bmgmf.jpg)



### Buffer 변환

채널이 데이터를 일고 쓰는 버퍼는 모두 ByteBuffer이다. 따라서 채널을 통해 읽은 데이터를 복원하려면 ByteBuffer를 문자열 또는 다른 타입 버퍼(CharBuffer, ShortBuffer, IntBuffer, LongBuffer, FloatBuffer, DoubleBuffer)로 변환해야 한다. 반대로 문자열 또는 다른 타입 버퍼의 내용을 채널을 통해 쓰고 싶다면 ByteBuffer로 변환해야 한다.



* **ByteBuffer <-> String**

  프로그램에서 가장 많이 처리되는 데이터는 String, 문자열이다. 채널을 통해 문자열을 파일이나 네트워크로 전송하려 면 특정 문자셋(UTF-8, EUC-KR)으로 인코딩해서 ByteBuffer로 변환해야 한다. 먼저 문자셋을 표현하는 `java.nio.charset.Charset`객체가 필요한데 다음 두 가지 방법으로 얻을 수 있다.

```java
Charset charset = Charset.forName("UTF-8"); // 매개값으로 주어진 문자셋
Charset charset = Charset.defaultCharset(); // 운영체제가 사용하는 디폴트 문자셋
```

> Charset을 이용해서 문자열을 ByteBuffer로 변환하려면 다음과 같이 encode() 메소드를 호출하면 된다.

```java
String data = ...;
ByteBuffer byteBuffer = charset.encode(data);
```

반대로 파일이나 네트워크로부터 읽은 ByteBuffer가 특정 문자셋(UTF-8, EUC-KR)으로 인코딩되어 있을 경우, 해당 문자셋으로 디코딩해야만 문자열로 복원할 수 있다. Charset은 ByteBuffer를 디코딩해서 CharBuffer로 변환시키는 decode() 메소드를 제공한다.

```java
ByteBuffer byteBuffre = ...;
String data = charset.decode(byteBuffer).toString();
```



* Encode / Decode 예제

```java
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferToStringExample {
    public static void main(String[] args) {
        Charset charset = Charset.forName("UTF-8");

        // 문자열 -> 인코딩 -> ByteBuffer
        String data = "안녕하세요";
        ByteBuffer buffer = charset.encode(data);

        // ByteBuffer -> 디코딩 -> String
        data = charset.decode(buffer).toString();
        System.out.println("[문자열 복원] : " + data);
    }
}
```



* **ByteBUffer <-> IntBuffer**
  int[] 배열을 생성하고 이것을 파일이나 네트워크로 출력하기 위해서는 int[] 배열 또는 IntBuffer로부터 ByteBuffer를 생성해야 한다. int 타입은 4byte 크기를 가지므로 int[] 배열 크기 또는 IntBuffer의 capacity보다 4배 큰 capacity를 가진 ByteBuffer를 생성하고, ByteBuffer의 putInt()메소드로 정수값을 하나씩 저장하면 된다. 다음은 int[] 배열을 IntBuffer로 래핑하고 4배 큰 ByteBuffer를 생성한 후 정수값을 저장한다.  
  putInt() 메소드는 position을 이동시키기 때문에 모두 저장한 후에 position을 0으로 되돌려 놓는 flip() 메소드를 호출해야 한다.

```java 
int[] data = new int[] {10, 20};
IntBuffer intBuffer = IntBuffer.wrap(data);
ByteBuffer byteBuffer = ByteBuffer.allocate(intBuffer.capacity()*4);
for(int i = 0; i < intBuffer.capacity(); i++) {
	byteBuffer.putInt(intBuffer.get(i));
}
byteBuffer.flip();
```



반대로 파일이나 네트워크로부터 입력된 ByteBuffer에 4바이트씩 연속된 int 데이터가 저장되어 있을 경우 int[] 배열로 복원이 가능하다. ByteBuffer의 asIntBuffer() 메소드로 IntBuffer를 얻고, IntBuffer의 capacity와 동일한 크기의 int[] 배열을 생성한다. 그리고 IntBuffer의 get() 메소드로 int값들을 배열에 저장하면 된다.

```java
ByteBuffer byteBuffer = ...;
IntBuffer = byteBuffer.asIntBuffer();

int[] data = new int[IntBuffer.capacity()];
intBuffer.get(data);
```

> ByteBuffer에서 asIntBuffer()로 얻은 IntBuffer에서는 array() 메소드를 사용해서 int[] 배열을 얻을 수 없다. array() 메소드는 래핑한 배열만 리턴하기 때문에, int[] 배열을 wrap()으로 래핑한 IntBuffer에서만 사용할 수 있다.



## 파일 채널

`java.nio.channels.FileChannel`을 이용하면 파일 읽기와 쓰기를 할 수 있다. FileChannel은 동기화 처리가 되어 있기 때문에 멀티 스레드 환경에서 사용해도 안전하다.



### FileChannel 생성과 닫기

정적 메소드인 open() 을 호출해서 얻을 수도 있지만, IO의 FileInputStream, FileOutputStream의 getChannel()을 통해서도 얻을 수 있다.

```java
FileChannel fileChannel = FileChannel.open(Paht path, OpenOption... options);
```

> path 매개값은 열거나 생성하고자 하는 파일의 경로를 Path 객체로 생성해서 지정하면 되고, 두 번째 options 매개값은 열기 옵션 값인데 StandardOpenOption의 다음 열거 상수를 나열해주면 된다. 



| 열거 상수         | 설명                                                         |
| ----------------- | ------------------------------------------------------------ |
| READ              | 읽기용으로 파일을 연다                                       |
| WRITE             | 쓰기용으로 파일을 연다                                       |
| CREATE            | 파일이 없다면 새 파일을 생성한다.                            |
| CREATE_NEW        | 새 파일을 만든다. 이미 파일이 있으면 예외와 함께 실패한다.   |
| APPEND            | 파일 끝에 데이터를 추가한다. WRITE나 CREATE와 함께 사용된다. |
| DELETE_ON_CLOSE   | 채널을 닫을 때 파일을 삭제한다(임시 파일을 삭제할 때 사용)   |
| TRUNCATE_EXISTING | 파일을 0바이트로 잘라낸다. (WRITE 옵션과 함께 사용됨)        |

```java
// 파일을 생성하고 내용을 작성
FIleChannel fileChannel = FileChannel.open(
	Paths.get("C:/Temp/file.txt"),
  StandardOpenOption.CREATE_NEW,
  StandardOpenOption.WRITE
);

// 파일을 읽고 작성
FIleChannel fileChannel = FileChannel.open(
	Paths.get("C:/Temp/file.txt"),
  StandardOpenOption.READ,
  StandardOpenOption.WRITE
);

// 작업 종료
fileChannel.close();
```



### 파일 쓰기와 읽기

파일에 바이트를 쓰려면 다음과 같이 FileChannel의 write() 메소드를 호출하면 된다. 매개값으로 ByteBuffer 객체를 주면 되는데, 파일에 쓰여지는 바이트는 ByteBuffer의 position부터 limit까지이다. position이 0이고 limit이 capacity와 동일하다면 ByteBuffer의 모든 바이트가 파일에 쓰여진다. write() 메소드의 리턴값은 ByteBuffer에서 파일로 쓰여진 바이트 수이다. 

```java
int bytesCount = fileChannel.write(ByteBuffer src);
```

* 파일 쓰기 예제

```java
public class FileChannelWriteExample {
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("C:/Temp/file.txt");
    Files.createDirectories(path.getParent());
    
    FileChannel fileChannel = FileChannel.open(
      path, StandardOpenOptions.CREATE, StandardOpenOption.WRITE);
    
    String data = "안녕하세요";
    Charset charset = Charset.defaultCharset();
    ByteBuffer byteBuffer = charset.encode(data);
    
    int byteCount = fileChannel.write(byteBuffer);
    System.out.println("file.txt : " + byteCount + "bytes written");
    
    fileChannel.close();
  }
}
```



파일로부터 바이트를 읽기 위해서는 다음과 같이 FileChannel의 read() 메소드를 호출하면 된다. 매개값으로 ByteBuffer 객체를 주면 된든데, 파일에서 읽혀지는 바이트는 ByteBuffer의 position부터 저장된다. position이 0이면 ByteBuffer의 첫 바이트부터 저장된다. read() 메소드의 리턴값은 파일에서 ByteBuffer로 읽혀진 바이트 수이다. 한 번 읽을 수 있는 최대 바이트 수는 ByteBuffer의 capacity까지므로 리턴되는 최대값은 capacity가 된다. 더 이상 읽을 바이트가 없다면 read()는 -1을 리턴한다.

버퍼에 한 바이트를 저장할 때마다 position이 1씩 증가하게 되는데, read() 메소드가 -1을 리턴할 때까지 버퍼에 저장한마지막 바이트의 위치는 position -1 인덱스이다.



* 파일 읽기 예제

```java
public class FileChannelReadExample {
	public static void main(String[] args) {
    Path path = Paths.get("C:/Temp/file.txt");
    
    FileChannel fileChannel = FileChannel.open(
    path, StandardOpenOption.READ);
    
    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
    
    Charset charset = Charset.defaultCharset();
    String data = "";
    int byteCount;
    
    while(true) {
      // 최대 100바이트를 읽는다.
      byteCount = fileChannel.read(byteBuffer);
      if(byteCount == -1) break;
      // limit을 현재 position으로 설정하고 position을 0으로 설정
      byteBuffer.flip();
      // 문자열 변환
      data += charset.decode(byteBuffer).toString();
      // position을 0번 인덱스로, limit을 capacity로 설정해서 ByteBuffer를 초기화
      byteBuffer.clear();
    }
    
    fileChannel.close();
    
    System.out.println("file.txt : " + data);
  }
}
```



### 파일 복사

파일 복사를 구현하기 위해서는 하나의 ByteBuffer를 사이에 두고, 파일 읽기용 FileChannel과 파일 쓰기용 FileChannel이 읽기와 쓰기를 교대로 번갈아 수행하도록 하면 된다.

![스크린샷 2022-03-20 18.03.42](https://tva1.sinaimg.cn/large/e6c9d24egy1h0gghad1o5j21d80kijsp.jpg)

  

* 예제

```java
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileCopyExample {
    public static void main(String[] args) throws Exception {

        Path from = Paths.get("src/exam02_file_copy/house1.jpg");
        Path to = Paths.get("src/exam04_file_copy/house2.jpg");

        FileChannel fileChannel_from = FileChannel.open(from, StandardOpenOption.READ);
        FileChannel fileChannel_to = FileChannel.open(to, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        int byteCount;
        while (true) {
            buffer.clear();
            byteCount = fileChannel_from.read(buffer);
            if (byteCount == -1) {
                break;
            }
            buffer.flip();
            fileChannel_to.write(buffer);

            fileChannel_from.close();
            fileChannel_to.close();
            System.out.println("복사 완료");
            
        }
    }
}
```

> 복사할 이미지가 존재하는 Path와 이미지를 복사할 Path를 생성하고, 각 Path의 FileChannel을 생성해준다. 그리고 다이렉트 버퍼를 생성하는데 채널에서 읽고 다시 채널로 쓰는 경우 다이렉트 버퍼가 좋은 성능을 내기 때문이다.



단순히 파일을 복사할 목적이라면 NIO의 FIles 클래스의 copy() 메소드를 사용하는 것이 더 편리하다.

```java
Path path = Files.copy(Path source, Path target, CopyOption ... options);
```

> 첫 번째 source 매개값에는 원본 파일의 Path 객체를 지정하고 두 번째 Path값에는 타겟 파일의 Path 객체를 지정하면 된다. 세 번째 매개값은 StandardCopyOption 열거 상수를 목적에 맞게 나열해주면 된다.



| 열거 상수         | 설명                                                         |
| ----------------- | ------------------------------------------------------------ |
| REPLACE_EXISTRING | 타겟 파일이 존재하면 대체한다.                               |
| COPY_ATTRIBUTES   | 파일의 속성까지도 복사한다.                                  |
| NOFOLLOW_LINKS    | 링크 파일일 경우 링크 파일만 복사하고 링크된 파일은 복사하지 않는다. |



* 예제 (Files.copy())

```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FilesCopyExample {
    public static void main(String[] args) throws Exception {
        Path from = Paths.get("src/exam02_file_copy/house1.jpg");
        Path to = Paths.get("src/exam04_file_copy/house2.jpg");

        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("파읿 복사 성공");
        
    }
}
```



## 파일 비동기 채널

FileChannel의 read() 와 write() 메소드는 파일 입출력 작업 동안 블로킹된다. 만약 UI 및 이벤트를 처리하는 스레드에서 이 메소드들을 호출하면 블로킹되는 동안에 UI 갱신이나 이벤트 처리를 할 수 없다. 따라서 별도의 작업 스레드를 생성해서 이 메소드들을 호출해야 한다. 만약 동시에 처리해야 할 파일 수가 많다면 스레드의 수도 증가하기 때문에 문제가 될 수 있다. 그래서 자바 NIO는 불특정 다수의 파일 및 대용량 파일의 입출력 작업을 위해서 비동기 파일 채널을 별도로 제공하고 있다.  

`AsyncronousFileChannel`의 특징은 파일의 데이터 입출력을 위해 read()와 write() 메소드를 호출하면 스레드풀에게 작업 처리를 요청하고 이 메소드를 즉시 리턴시킨다. 실질적인 입출력 작업 처리는 스레드풀의 작업 스레드가 담당한다. 작업 스레드가 파일 입출력을 완료하게 되면 콜백 메소드가 자동 호출되기 때문에 작업 완료 후 실행해야 할 코드가 있다면 콜백 메소드에 작성하면 된다.

### AsyncronousFileChannel 생성과 닫기

AsynchronousFileChannel은 두 가지 정적 메소드인 open()을 호출해서 얻을 수 있다. 첫 번째  open() 메소드는 다음과 같이 파일의 Path 객체와 열기 옵션 값을 매개값으로 받는다.

```java
AsychronousFileChannel fileChannel = AsynchronousFileChannel.open(
	Path file,
	OpenOption... options);
```

이렇게 생성된 AsynchronousFileChannel은 내부적으로 생성되는 기본 스레드풀을 사용해서 스레드를 관리한다.

기본 스레드풀의 최대 스레드 개수는 개발자가 지정할 수 없기 때문에 다음과 같이 두 번째 open() 메소드로 채널을 만들수도 있다.

```java
AsychronousFileChannel fileChannel = AsychronousFileChannel.open(
  Path file,
  Set<? extends OpenOptions> options,
  ExecutorService executor,
  FileAttribute<?> ... attrs
  );
```

> File 매개값은 파일의 Path 객체이고 options 매개값은 열기 옵션 값들이 저장도니 Set 객체이다. executor 매개값은 스레드풀인 ExecutorService 객체이다. attrs 매개값은 파일 생성 시 파일 속성값이 될 FileAttribute를 나열하면 된다. 예를 들어 C:/Temp/ifle.txt 파일에 대한 AsynchronousFileChannel은 다음과 같이 생성할 수 있다.

```java
ExecutorService service = ExecutorService.newFixedThreadPool(
  Runtime.getRuntime().availableProcessors()
);

AsychronousFileChannel fileChannel = AsychronousFileChannel.open(
	Paths.get("C:/Temp/file.txt"),
  EnumSet.of.(StandardOpenOption.CREATE, StandardOpenOptions.WRITE),
  service
);
                                                           
```

더 이상 채널을 사용하지 않을 경우 `fileChannel.close()`를 호출하여 채널을 닫아준다.

* availableProcessors() :  CPU의 코어수를 리턴한다. 쿼드 코어의 경우 4개를 리턴한다.

* EnumSet.of() : 매개값으로 나열된 열거 상수를 Set 객체에 담아 리턴한다



### 파일 읽기와 쓰기

채널이 생성되었다면 read(), write() 메소드를 이용해서 입출력할 수 있다.

```java
read(ByteBuffer dst, long position, A attachment, CompletionHandler<Integer, A> handler);
write(ByteBuffer src, long position, CompletionHandler<Integer, A> handler);
```

이 메소드들을 호출하면 즉시 리턴되고 스레드풀의 스레드가 입출력 작업을 진행한다. 

* `drt`, `src` : 파일을 읽거나 쓰기 위한 ByteBuffer

* `position` : 파일을 읽거나 쓸 때의 위치

* `attachment` : 콜백 메소드로 전달할 첨부 객체이다. 콜백 메소드에서 결과값 외에 추가적인 정보를 얻기 위해 사용한다. 첨부 객체가 필요없다면 `null`을 리턴해도 된다. 

* `handler` : CompletionHandler<Integer, A>의 구현 객체를 지정한다. `Integer`는 입출력의 결과 타입으로 read(), write() 메소드로 읽거나 쓴 바이트 수를 리턴한다. `A`는 첨부 객체 타입으로 개발자가 CompletionHandler 구현 객체를 작성할 때 임의로 지정이 가능하며 필요없다면 void가 된다.

* `CompletionHandler<Integer, A>` : 정상적 완료와 예외 발생시 호출할 두 가지 콜백 메소드를 가지고 있어야 한다.

  | 리턴 타입 | 메소드명 (매개 변수)                    | 설명                                |
  | --------- | --------------------------------------- | ----------------------------------- |
  | void      | completed(Integer result, A attachment) | 작업이 정상적으로 완료된 경우 콜백  |
  | void      | failed(Throwable exc, A attachment)     | 예외 때문에 작업이 실패된 경우 콜백 |

  * `completed()` : result 매개값은 작업 결과가 대입되는데  read(), write() 메소드의 읽거나 쓴 바이트 수이다. attachment는 read(), write() 호출 시 제공된 첨부 객체이다.
  * `failed()` : exc 매개값은 작업 처리 도중 발생한 예외이다. 주목할 점은 콜백 메소드를 실행하는 스레드는 read(), write()를 호출한 스레드가 아니고 스레드풀의 작업 스레드인 것이다.
  * CompletionHandler 구현 클래스 작성 방법

  ```java
  new CompletionHandler<Integer, A>() {
    @Override
    public void completed(Integer result, A attachment) { ... }
    @Override
    public void failed(Throwable exc, A attachment) { ... }
  }
  ```

  

## TCP 블로킹 채널

> NIO를 이용해서 TCP 서버/클라이언트 애플리케이션을 개발하려면 블로킹, 넌블로킹, 비동기 구현 방식 중에서 하나를 결정해야 한다. 이 결정에 따라 구현이 완전히 달라진다.



### 서버소켓 채널과 소켓 채널의 용도

> NIO에서 TCP 네트워크 통신을 위해 사용하는 채널은 `java.nio.channels.ServerSocketChannel`과 `java.nio.channels.SocketChannel`이다. 이 두 채널은 IO의 ServerSocket과 Socket에 대응되는 클래스로 IO가 버퍼를 사용하지 않고 블로킹 입출력 방식만 지원한다면 ServerSocketChannel, SocketChannel은 버퍼를 이용하고 블로킹과 넌블로킹 방식을 모두 지원한다. 사용 방법은 IO와 큰 차이점이 없는데, ServerSocket은 클라이언트 SocketChannel의 연결 요청을 수락하고 통신용 SocketChannel을 생성한다.



### 서버소켓 채널 생성과 연결 수락 

* 서버를 개발하기 위해선 ServerSocketChannel 객체를 얻어야 함
* ServerSockeChannel은 정적 메소드인 open()으로 생성하고, 블로킹 방식으로 동작시키기 위해 configureBlocking(true) 메소드를 호출함.
* 기본적으로 블로킹 방식으로 동작, 그러나 명시적으로 설정하는 이유는 넌블로킹과 구분하기 위함임.
* 포트에 바인딩 하기 위해서는  bind() 메소드가 호출되어야 함 :arrow_right: InetSocketAddress 객체를 매개값으로 주면 된다.

```java
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
serverSocketChannel.configureBlocking(true);
serverSocketChannel.bind(new InetSocketAddress(5001));
```

포트 바인딩까지 끝났다면 ServerSocketChannel은 클라이언트 연결 수락을 위해 accept() 메소드를 실행해야 한다.

* accept() : 클라이언트가 연결 요청을 하면 accept()는 클라이언트와 통신할 SocketChannel을 만들고 리턴함. 클라이언트가 요청을 하기 전까지 블로킹 됨, UI 및 이벤트를 처리하는 스레드에서  accept() 메소드를 호출하지 않도록 한다.

```java
SocketChannel socketChannel = serverSocketChannel.accept();
```

연결된 클라이언트의 IP와 포트 정보를 얻고 싶다면 SocketChannel의 getRemoteAddress()를 통해 얻으면 된다. 실제 리턴되는 것은 InetSocketAddress 인스턴스이므로 다음과 같이 캐스팅이 가능하다.

```java
InetSocketAddress socketAddress = (InetSocketAddress) serverSocket.getRemoteAddress();
```

`InetSocketAddress`에는 다음과 같이 IP, 포트 정보를 리턴하는 메소드들이 있다.

| 리턴 타입 | 메소드명 (매개 변수) | 설명                             |
| --------- | -------------------- | -------------------------------- |
| String    | getHostName()        | 클라이언트 IP리턴                |
| int       | getPort()            | PORT 번호 리턴                   |
| String    | toString()           | "IP 포트번호" 형태의 문자열 리턴 |



> 더 이상 클라이언트를 위해 연결 수락할 필요가 없다면 ServerSocketChannel의 close() 메소드를 호출시킨다. 이렇게 해야만 해당 포트를 재사용할 수 있다.



* 예제

```java
public class ServerExample {
  public static void main(String[] args) { 
  	ServerSocketChannel channel = null;
    try {
      channel = ServerSocketChannel.open();
      channel.configureBlocking(true);
      channel.bind(new InetSocketAddress(5001));
      
      while(true) {
        System.out.println("연결을 기다림");
        SocketChannel socketChannel = channel.aceept();
        InetSocketAddress isa = socketChanel.getRemoteAddress();
        System.out.println("연결을 수락함 : " + isa.getHostName());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    if(channel.isOpne()) {
      try {
        channel.close();
      } catch (IOException e) {
        e.printStackTrace(); 
      }
    }
  }
}
```



### 소켓 채널 생성과 연결 요청

* 클라이언트가 서버 연결 요청을 할 때에는 `java.nio.channels.SocketChannel`을 이용함.
* SocketChannel은 정적 메소드인 open() 으로 생성하고 블로킹 방식으로 동작 시키기 위해 configureBlocking(true) 메소드를 호출함. 기본적으로 블로킹 방식으로 동작하지만, 명시적으로 설정하는 이유는 넌블로킹과 구분을 위해서임.
* 서버 연결 요청은 connect()를 호출하는데, IP와 Port를 가진  InetSocketAddress를 매개값으로 줌.

```java
SocketChannel channel = SocketChannel.open();
channel.configureBlocking(true);
channel.connect(new InetSocketAddress(5001));
```

> connect() 메소드는 서버와 연결이 될 때까지 블로킹되므로 UI 및 이벤트를 처리하는 스레드에서는 사용하지 않도록 한다. 블로킹되면 UI 갱신, 이벤트 처리를 할 수 없기 때문이다.  
>
> 연결된 후 클라이언트 프로그램을 종료하거나 필요에 따라 연결을 끊고 싶다면 close() 메소드를 호출한다.

```java
channel.close();
```



* 연결 요청 예제

```java
public class ClientExample {
  public static void main(String[] args) {
    SocketChannel channel = null
    try {
    	channel = SocketChannel.open();
    	SocketChannel.configureBlocking(true);
	    System.out.println("연결 요청");
      channel.connect(new InetSocketAddress("localhost", 5001));
      System.out.println("연결 성공");
    } catch(Exception e) {
      e.printStackTrace();
    } 
	  if(channel.isOpen()) {
		try{
  	  channel.close();
  		} catch(IOException e) {
	    e.printStackTrace();
  	}
  } 
}
```



### 소켓 채널 데이터 통신

클라이언트가 연결 요청(connect())을 하고 서버가 연결 수락(accept())을 하면 양쪽 SocketChannel 객체의 read(), write() 메소드를 호출해서 데이터를 통신할 수 있다. 이 메소드들은 모두 버퍼를 사용하기 때문에 버퍼로 읽고 쓰는 작업을 해야 한다.



```java
// write() 메소드 이용 코드
Charset charset = Charset.forName("UTF-8"); 
ByteBuffer buffer = charset.encode("Hello world");
socketChannel.write(buffer);

// 다음은 SOcketChannel의 read() 를 이용하여 문자열을 받는 코드임
ByteBuffer readBuffer = ByteBuffer.allocate(100);
int byteCnt = socketChannel.reay(readBuffer);
readBuffer.flip();
Charset charset2 = Charset.forName("UTF-8");
String message = charset2.decode(readBuffer).toString();
```



