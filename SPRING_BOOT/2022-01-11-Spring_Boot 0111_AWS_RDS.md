## 0111 Spring_Boot_AWS_RDS

* 오늘은 DB를 구축하고 EC2와 연동한다.



## RDS란

* AWS에서 지원하는 클라우드 기반 관계형 데이터베이스
* 하드웨어 프로비저닝, DB설정, 패치 및 백업 등의 잦은 운영작업을 자동화해준다.



## RDS 연동하기

* RDS를 이용하여 나만의 DB 환경을 구축한다.

  

### RDS 인스턴스 생성하기

1. 검색창에 RDS를 검색하여 클릭하고, `데이터베이스 생성` 버튼을 클릭한다.

2. 엔진유형에는 MySQL, MariaDB, Oracle, PostgreSQL, Microsoft SQL Server 등이 있는데, **MariaDB**를 통해 실습을 진행한다.

   > 1. RDS의 가격은 라이센스 비용 영향을 받기 때문(그래서 오픈 소스인 MariaDB를 선택함)
   > 2. Amazon Aurora 교체 용이성 때문

3. 템플릿은 프리티어로 설정한다.

4. DB인스턴스 및 마스터 사용자 정보를 설정한다. 

   > 마스터 사용자 정보는 실제 데이터베이스에 접근하게 되는 용도이다.

5. 이후 보안그룹에서 지정된 IP만 접근하도록 막기 위해 퍼블릭 엑세스 기능을 `예`로 체크한다.



## RDS 본격 설정

* 몇가지 필수 설정을 해야한다. 우선 아래 3가지를 진행한다.

  > 1. 타임존
  > 2. Character Set
  > 3. Max Connection



1. 파라미터 그룹 생성

   > 세부 정보에 DB엔진을 선택하는 항목에서 생성했을 때의 MariaDB와 버전을 맞춰야한다.

2. `타임존` : 편집 버튼을 클릭하고 `time_zone`을 검색한 뒤, `Asia/Seoul`을 선택한다.

3. 다음은 `Charactoer Set`이다. 설정해야 할 것들이 많다.

   > 3-1. character_set_client
   >
   > 3-2. character_set_connection
   >
   > 3-3. character_set_database
   >
   > 3-4. character_set_filesystem
   >
   > 3-5. character_set_results
   >
   > 3-6. character_set_server
   >
   > 3-7. collation_connection
   >
   > 3-8. collation_server
   >
   > 
   >
   > * character 항목은 `utf8mb4`로 collation 항목은 `utf8mb4_general_ci`로 변경한다.
   > * utf8과 utf8mb4의 차이는 이모지 저장 가능 여부이다.

4. `Max Connection`  : 인스턴스 사양에 따라 자동으로 정해지지만 150개로 넉넉하게 늘려준다.

5. 데이터베이스 탭에서 식별자를 클릭하고 수정 버튼을 누른뒤, 파라미터 그룹을 방금 생성한 파라미터 그룹으로 변경하고 저장을 누른다.

6. 수정 예약 폼에서 `즉시 적용`을 클릭한다.



## 내 PC에서 RDS 접속해보기

> 로컬 PC에서 RDS로 접근하기 위해서 RDS의 보안 그룹에 내 PC의 IP를 추가한다.

* RDS의 보안 그룹 탭에서 EC2에 사용된 그룹 ID를 복사한 뒤, ID와 내 IP를 보안 그룹의 인바운드로 추가한다.



### Database 플러그인 설치

1. IntelliJ에 플러그인`DB Browser`을 설치한다. (설치전 RDS 정보에서 엔드포인트를 확인하고 복사한다.)

   > 왜인진 모르겠으나 `DB Browser` 플러그인으로는 테스트 커넥션을 성공하지 못했다. 그래서 IntelliJ 화면 오른쪽에 있는 `DATA BASE`탭에서 연결을 시도해봤는데 됐다.
   > ![SchemaInit](https://user-images.githubusercontent.com/84169773/148897017-6f0c06b9-e624-4f65-81b4-bda8586e9c95.png)


2. 콘솔을 열어 스키마 중 생성한 RDS 스키마를 사용하기 위해 `use schema_name;`을 입력후 실행한다.

3. character_set, collation 설정을 확인한다.

   ```mysql
   1. 확인
   SHOW variables like 'c%';
   
   2. 수정
   ALTER DATABASE DB_명
   CHARACTER SET = 'utf8mb4'
   COLLATE = 'utf8mb4_general_ci';
   
   3. 재확인
   SHOW variables like 'c%';
   
   4. 타임존 확인
   SELECT @@time_zone, now();
   ```

4. 확인이 다 됐으면, 간단한 테이블 생성 및 insert 쿼리를 실행한다.

   ```mysql
   CREATE TABLE test (
   	id bigint(20) NOT NULL AUTO_INCREMENT,
       content varchar(255) DEFAULT NULL,
       PRIMARY KEY(id)
   ) ENGINE=InnoDB;
   
   INSERT INTO test(content) values ('테스트');
   
   SELECT * FROM test;
   ```

5. 다행히 정상 작동한다. 이제 EC2와 연동되는지 확인해본다.



## EC2에서 RDS 접근 확인

> SSH에 접속해서 mysql을 설치하고 접속한 뒤, 데이터베이스를 조회 한다.

1. putty 실행 및 `sudo yum install mysql` 입력
2. `mysql -u seung -p -h seung-springboot2-webservice.cffzgf4jtipz.ap-northeast-2.rds.amazonaws.com`를 입력하고 비밀번호를 입력한다음 성공적으로 접속했다.
3. `show databases;` 를 입력하니 아래와 같이 `seung_springboot_webservice`가 조회된다.
   ![successDB](https://user-images.githubusercontent.com/84169773/148897042-965ec6ec-eac0-4180-93cc-815cee6d8701.png)




## 오늘 배운 것

* AWS 관리형 DB서비스인 RDS에 대한 소개와 생성 방법
* RDS 필요 파라미터 설정 
* 인텔리제이에서 DB를 다루는 방법
* EC2 RDS간 연동 방법



## 내일 할 일

* EC2 서버에 프로젝트를 배포해보자
