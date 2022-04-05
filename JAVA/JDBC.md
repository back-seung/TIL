## JDBC



### JDBC란

> 자바 프로그램이 데이터베이스와 연결되어 데이터를 주고 받을 수 있게 해주는 프로그래밍 인터페이스, `java.sql`패키지에 존재함

자바 언어로 다양한 종류의 관계형 데이터베이스에 접속하고 SQL문을 수행하여 처리하고자 할 때 데이터베이스를 조작하는 `표준 SQL 인터페이스 API`

* `단점` : DB에서 정보를 가져올 때마다 DB Connection, Disconnection을 해야하고 **서버 과부하, 속도 저하의 문제가 있음** :arrow_right: 단점을 극복하기 위해 `JNDI` 사용 



### 동작 순서

1. JDBC 드라이버 로드

    * `jdbc.drivers` 라는 시스템 환경변수에 등록된 내용으로 하는 방법
    * `Class.forName()` 메소드를 이용해서 직접 해당 클래스를 로드하는 방법 (:thumbsup:)
2. 데이터베이스 연결

    * `java.sql.Connection`에 정보를 입력하여 연결 (아래 예시)

    ```properties
    spring.datasource.driverClassName=oracle.jdbc.OracleDriver
    spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl
    spring.datasource.username=[DataBase 아이디]
    spring.datasource.password=[DataBase 비밀번호]
    ```
3. Statement 생성

    : 데이터베이스 연결로부터 SQL문을 수행할 수 있도록 해주는 클래스

    * `Statement` : 데이터베이스 연결로부터 SQL문을 수행할 수 있도록 해주는 클래
    * `PreparedStatement` : SQL문을 미리 만들어두고 변수를 따로 입력하는 방식(효율성, 유지보수 측면 :thumbsup:)
4. SQL문 전송 (데이터 조회시 결과에 대한 반환값은 ResultSet으로 받음)

    * `Statement.executeQuery()` :arrow_right: SELECT 문 수행
    * `Statement.executeUpdate()` :arrow_right: INSERT / UPDATE / DELETE 문 수행
5. 연결 해제

    * `Connection.close`

