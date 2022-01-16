0114 Spring_Boot_RDS 접근 및 자동 배포

* 오늘은 프로젝트로 RDS에 접근하는 방법 +
* 24시간 배포환경을 구축한다. 



## 8.4 스프링 부트 프로젝트로 RDS 접근하기

> RDS는 MariaDB를 사용하기 때문에
>
> 1. 테이블 생성
> 2. 프로젝트 설정
> 3. EC2 설정이 필요하다.

### RDS 테이블 생성

* JPA가 사용될 Entity테이블과 스프링 세션이 사용될 테이블 2가지 종류를 생성한다.

  ![JPA](https://user-images.githubusercontent.com/84169773/149649926-be0b675d-f2b1-42b4-bd28-edfcbb6f8b13.png)


  > 예제를 따라서 성공적으로 만들었다



### 프로젝트 설정

* MariaDB를 build.gradle에 등록한다.

  > `compile('org.mariadb.jdbc:mariadb-java-client')`를 의존성에 추가한다.

* src/main/resources에 `application-real.properties`를 생성하고 다음과 같이 작성한다.

  ```properties
  spring.profiles.include=oauth,real-db
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
  spring.session.store-type=jdbc
  ```

  > 서버에서 구동될 환경을 구성하는 것이다.
  >
  > profile=real인 환경이 구성된다. 실제 운영될 환경이기 때문에 **보안/로그상 이슈가 될 만한 설정은 제거하고 RDS 환경 profile** 설정이 추가된다.



### EC2 설정

* OAuth과 마찬가지로 RDS 접속 정보도 보호해야 하므로, EC2 서버에 직접 설정 파일을 둔다. 

  > app 디렉토리에 `applicatio-real-db.properties`파일을 생성하고 다음과 같이 작성한다.

  ```sh
  spring.jpa.hibernate.ddl-auto=none
  spring.datasource.url=jdbc:mariadb://rds주소:포트명/database이름
  spring.datasource.username=db계정
  spring.datasource.password=db비번
  spring.datasource.driver-class-name=org.mariadb.jdbc.driver
  ```

* 그리고 `deploy.sh`를 수정한다.

  > `-Dspring.profiles.active=real`
  >
  > * `application-real.properties`를 활성화 시킨다. 해당 파일 내의 spring.profiles.include=oauth,real-db 옵션 때문에 real-db역시 활성화 대상에 포함된다.

* `curl localhost:8080`을 SSH에 입력하여 html코드가 정상적으로 보인다면 성공!



### EC2에서 소셜 로그인

* 이제 EC2에 서비스가 잘 배포되었다. AWS의 보안그룹 중 8080이 열려있는 것을 확인한다.
* 퍼블릭 DNS를 확인하고 도메인 주소 뒤에 :8080을 붙여 브라우저에 접속해본다.
* 소셜 로그인을 위해 EC2 도메인을 구글, 네이버에서 만든 애플리케이션 Redirect 도메인에 추가해준다. 



## Travis CI 배포 자동화

> 코드가 푸시되면 Test & Build 를 자동화 해준다.

* CI(Continuous Integration - 지속적 통합) : 코드 버전 관리를 하는 VCS 시스템에 PUSH가 되면 자동으로 테스트와 빌드가 수행되어 안정적인 배포 파일을 만드는 과정

  > CI는 다음과 같은 규칙이 필요하다
  >
  > 1. 모든 소스 코드가 현재 실행되고 누구든 현재의 소스에 접근할 수 있는 단일지점을 유지
  > 2. 빌드 프로세스 자동화를 통해 누구든 단일 명령어를 사용할 수 있게할 것
  > 3. 테스팅 자동화하여 언제든지 시스템에 건전한 테스트 수트를 실행할 수 있게할 것
  > 4. 누구나 현재 실행 파일을 얻을 때,  가장 완전한 파일이라고 확신하게 할 것
  >
  > : 프로젝트가 완전한 상태임을 보장하기 위함이다.

* CD(Continuous Deployment - 지속적 배포) : 빌드 결과를 자동으로 운영 서버에 무중단 배포까지 진행되는 과정



### Travis CI 연동

1. travis-ci.com에서 가입을 한뒤 깃허브 저장소를 활성화한다.

2. `travis.yml` : travis CI의 상세 설정을 할 수 있다. 이를 생성한다. (아래 파일은 p.383까지 진행한 내용이다. **참고하지 않는게 좋습니다!** )

   ```yml
   language: java
   jdk:
     - openjdk8
   
   branches:
     only:
       - master
   
   # Travis CI 서버의 Home
   cache:
     directories:
       - '$HOME/.m2/repository'
       - '$HOME/.gradle'
   
   script: "./gradlew clean build"
   
   before_deploy:
     - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
     - cp scripts/*.sh before-deploy/  
     - cp appspec.yml before-deploy/
     - cp build/libs/*.jar before-deploy/
     - cd before-deploy && zip -r before-deploy * # before-deploy로 이동 후 전체 압축
     - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동 후 deploy 디렉토리 생성
     - mv before-deploy/before-deploy.zip deploy/seung-springboot-webservice.zip # deploy로 zip파일 이동
   
   deploy:
     - provider: s3
       access_key_id: $AWS_ACCESS_KEY # Travis repo settings에 설정된 값
       secret_access_key: $AWS_SECRET_KEY # Travis repo settings에 설정된 값
       bucket: seung-springboot-webservice
       region: ap-northeast-2
       skip_cleanup: true
       acl: private # zip 파일 접근을 private으로
       local_dir: deploy # before_deploy에서 생성한 디렉토리
       wait-until-deployed: true
   
     - provider: codedeploy
       access_key_id: $AWS_ACCESS_KEY
       secret_access_key: $AWS_SECRET_KEY
       bucket: seung-springboot-webservice
       key: seung-springboot-webservice.zip # 빌드 파일을 압축해서 전달
       bundle_type: zip
       application: seung-springboot-webservice # 웹 콘솔에서 등록한 CodeDeploy 애플리케이션
       deployment_group: seung-springboot-webservice-group # 웹 콘솔에서 등록한 CodeDeploy 배포 그룹
       region: ap-northeast-2
       wait-until-deployed: true
   
   # CI 실행 완료 시 메일로 알람
   notifications:
     email:
       recipients:
         skud113@naver.com
   
   ```

   > * branches : master 브랜치에 푸시될 때만 travis-ci 수행 
   > * cache : 그레이들을 통해 의존성을 받게되면 해당 디렉토리에 cache하여 같은 의존성은 다음 배포엔 받지 않음
   > * script : push되면 수행하는 명령어 `gradlew clean build`
   > * notifications : 자동으로 알람이 옴

   

### Travis와 S3연동

* S3 : 일종의 파일 서버이다. 정적인 파일들을 관리하거나 배포 파일들을 관리하는 등의 기능을 지원한다.



1. `IAM`에서 AWS key를 발급 받는다 

   > 일반적으로 AWS 서비스에 외부 서비스가 접근할 수 없기 때문.

2. travis-ci에 키를 등록한다.

3. S3 버킷을 생성한다.

   > 버킷의 보안과 권한 설정에서 **모든 차단을 해야한다. ** Jar 파일이 퍼블릭일 경우 누구나 내려받을 수 있어 중요한 정보가 탈취될 수 있다.

4. travis.yml에 다음 코드를 추가한다.

   ```yml
   before_deploy:
     - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
     - cp scripts/*.sh before-deploy/  
     - cp appspec.yml before-deploy/
     - cp build/libs/*.jar before-deploy/
     - cd before-deploy && zip -r before-deploy * # before-deploy로 이동 후 전체 압축
     - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동 후 deploy 디렉토리 생성
     - mv before-deploy/before-deploy.zip deploy/seung-springboot-webservice.zip # deploy로 zip파일 이동
   
   deploy:
     - provider: s3
       access_key_id: $AWS_ACCESS_KEY # Travis repo settings에 설정된 값
       secret_access_key: $AWS_SECRET_KEY # Travis repo settings에 설정된 값
       bucket: seung-springboot-webservice
       region: ap-northeast-2
       skip_cleanup: true
       acl: private # zip 파일 접근을 private으로
       local_dir: deploy # before_deploy에서 생성한 디렉토리
       wait-until-deployed: true
   ```

5. 깃허브에 푸시하고 Travis CI에서 빌드가 성공했는지 확인한다.



## 알게된 점

* 당일날 한걸 몰아서 정리하려다가 실패했다. 분명 많은 실수가 있었는데 글로 작성하려니 생각이 안나고 16일인 지금 너무 후회된다. 383페이지까지 빠르게 정리하고 다음부터는 꼭 TIL이라는 주제에 맞게 글을 작성하겠다.

## 내일 할 일

* CodeDeploy 연동글 작성 + Nginx 연동글 작성

* 자바 공부

  

