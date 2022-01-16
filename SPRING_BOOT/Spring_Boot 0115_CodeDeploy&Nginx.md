0114 Spring_Boot_RDS 접근 및 자동 배포

* 오늘은 CodeDeploy 연동글 작성 + Nginx 연동글 작성, 자바 공부를 한다.



## 9.4 Travis CI와 AWS S3, CodeDeploy 연동하기

* [CodeDeploy](https://docs.aws.amazon.com/ko_kr/codedeploy/latest/userguide/welcome.html) : AWS의 배포 시스템

* 그 외 배포 역할을 하는 서비스들

  * Code Commit

    : 깃허브와 같은 코드 저장소의 역할, 프라이빗을 지원하는 강점이 있지만 깃허브에서 무료로 지원하고 있어서 거의 사용되지 않음 

  * Code Build 

    : Travis CI와 마찬가지로 빌드용 서비스, 멀티 모듈을 배포하는 경우 사용해 볼만하지만 규모가 있는 곳에선 **젠킨스, 팀시티**등을 이용하여 거의 사용되지 않음

    

### EC2에 IAM 역할 추가하기

* IAM은 AWS에서 제공하는 서비스의 접근 방식과 권한을 관리한다. 따라서 S3와 마찬가지로 IAM을 탭으로 이동한다.
* **사용자가 아닌 역할 -> 역할 만들기 버튼을 누른다.**  사용자와 역할의 차이는 다음과 같다. 



* 사용자 

  > * AWS 서비스 외에 사용할 수 있는 권한
  > * 로컬 PC, IDC 서버 등

* 역할

  > * AWS 서비스에만 할당할 수 있는 권한
  > * EC2, CodeDeploy, SQS 등



* 서비스 선택에서 `AWS 서비스 ▶ EC2` 탭을 차례대로 클릭
* 정책에서는 `AmazonEC2RoleforAWSCodeDeploy`를 선택
* 태그네임은 원하는대로 지어주면 된다. 난 예제를 따라 똑같이 Name이라고 지었다.

* 이제 생성된 역할을 적용할 차례다.`인스턴스 우클릭 ▶ IAM 역할 연결/바꾸기`를 선택하여 방금 생성한 역할을 적용한 뒤, 재부팅을 해준다.

  > 재부팅을 해야만 역할이 정상적으로 작동된다.



### CodeDeploy 에이전트 설치

> * CodeDeploy의 요청을 받을 수 있게 EC2 서버에 접속하여 다음 명령어를 차례로 입력한다.
>
> ```shell
> # 에이전트 설치
> aws s3 cp s3://aws-codedeploy-ap-northeast-2/latest/instatll . --region ap-northeast-2
> # 설치 완료시 출력문
> > download: s3://aws-codedeploy-ap-northeast-2/latest/instatll to ./install 
> # insatll 파일 실행 권한 추가
> chmod +x ./install
> # install 파일로 설치 진행
> sudo ./install auto
> # Agent 정상 실행 상태 확인
> sudo service codedeploy-agent status
> # 아래와 같이 출력되면 정상 !
> > The AWS CodeDeploy agent is running as PID XXX
> ```
>
> * 난 Ruby가 설치되어있지 않아 `sudo yum install ruby`를 통해 ruby를 설치했다. **안되는 사람은 참고!**



### CodeDeploy를 위한 권한 생성

> * EC2에 접근하려면 마찬가지로 권한이 필요하다. IAM 역할을 생성하자.
> * `AWS서비스 ▶ CodeDeploy` 선택
> * 서비스와 사용사례에서는 CodeDeploy를 선택한다.
> * 태그 네임을 짓고 생성을 완료한다.



## CodeDeploy 생성

>  깃허브와 Travis가 각각 커밋, 빌드의 역할을 하고 있으니 배포를 위해 CodeDeploy를 생성한다.

* `애플리케이션 생성` ▶ `컴퓨팅 플랫폼 - EC2/온프레미스` ▶ `배포 그룹 생성` ▶ `배포 그룹 이름 입력, 서비스 역할 - IAM 역할 선택` ▶ `배포 유형 - 현재위치`▶ `환경구성 - Amazon EC2 인스턴스` ▶ `생성완료`



### Travis CI, S3, CodeDeploy 연동

* EC2 서버에 디렉토리를 생성한다. - `mkdir ~/app/step2 && mkdir ~/app/step2/zip`

  > Travis CI의 Build가 끝나면 S3에 zip 파일이 전송되고 이 zip파일을 위 디렉토리에 복사되어 압축을 풀 예정이다.

* Travis의 설정은 `travis.yml`, CodeDeploy의 설정은 `appspec.yml`에서 진행한다.

* `appspec.yml` - 다음과 같이 작성

  ```yaml
  version: 0.0
  # codedeploy의 버전, 프로젝트 버전이 아니므로 0.0 사용
  os: linux
  files:
  	- source:	/
  	# codedeploy에서 전달해 준 파일 중 destination으로 이동시킬 대상 지정
  	destination: /home/ec2-user/app/step2/zip
  	# source에서 지정된 파일을 받을 위치, Jar를 실행하는 등을 여기서 옮긴 파일들로 진행
  	overwrite: yes
  	# 덮어쓰기 결정 Yes
  ```

* `.travis.yml` - 코드 추가

  ```yaml
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
  ```

  > S3의 옵션과 유사하다. 다른 부분은 CodeDeploy의 애플리케이션 이름과 배포 그룹명을 지정하는 것이다.

* 커밋 후 푸시한다.

* `cd /home/ec2-user/step2/zip`으로 이동하여 파일이 잘 왔는지 확인한다.

* 연동이 완료됐다



## 배포 자동화 구성

> 연동까지 구현되었으니, 실제로 Jar를 배포하여 실행까지 진행한다.



### deploy.sh 파일 추가

* 프로젝트에 `scripts/deploy.sh` 추가하고 다음과 같이 작성한다.

  ```sh
  #!/bin/bash
  
  REPOSITORY=/home/ec2-user/app/step2
  PROJECT_NAME=Spring_BootPJ_booboo
  
  echo "> Build 파일 복사"
  
  cp $REPOSITORY/zip/*.jar $REPOSITORY
  
  echo "> 현재 구동중인 애플리케이션 pid 확인"
  
  CURRENT_PID=$(pgrep -fl Spring_BootPJ_booboo | grep jar | awk '{print $1}')
  # 현재 수행 중인 스프링 부트 애플리케이션의 PID를 찾는다. 실행 중이면 종료
  echo "현재 구동중인 애플리케이션 : $CURRENT_PID"
  
  if [ -z "$CURRNET_PID" ]; then
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
  fi
  
  echo "> 새 애플리케이션 배포"
  
  JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
  
  echo "> JAR Name : $JAR_NAME"
  
  echo "> $JAR_NAME 에 실행권한 추가"
  
  chmod +x $JAR_NAME
  # jar 파일에 nohup으로 실행할 수 있게 실행권한을 추가한다.
  echo "> $JAR_NAME 실행"
  
  nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
    # nohup 실행시 CodeDeploy가 무한대기하는 이슈를 해결하기 위해 사용
  
  ```

* `.travis.yml` 수정 - 다음과 같이 수정

  ```yaml
  before_deploy:
    - mkdir -p before-deploy # zip에 포함시킬 파일들을 담을 디렉토리 생성
    # S3에서는 디렉토리 단위로만 업로드가 가능하기 떄문에 디렉토리를 항상 생성한다.
    - cp scripts/*.sh before-deploy/  
    # zip 파일에 포함시킬 파일들을 저장한다.
    - cp appspec.yml before-deploy/
    - cp build/libs/*.jar before-deploy/
    - cd before-deploy && zip -r before-deploy * # before-deploy로 이동 후 전체 압축
    # zip -r 명령어를 통해 디렉토리 전체 파일을 압축한다.
    - cd ../ && mkdir -p deploy # 상위 디렉토리로 이동 후 deploy 디렉토리 생성
    - mv before-deploy/before-deploy.zip deploy/seung-springboot-webservice.zip # deploy로 zip파일 이동
  ```

* `appspec.yml` 수정 - 다음과 같이 수정

  ```yaml
  permissions:
    - object: /
      pattern: "**"
      owner: ec2-user
      group: ec2-user
  
  hooks:
  # CodeDeploy에서 실행할 명령어 지정 ApplicationStart에서 deploy.sh를 ec2-user 권한으로 실행하게함
    ApplicationStart:
      - location: deploy.sh
        timeout: 12000
        runas: ec2-user
  ```

  > 난 노트북 사양이 안좋아서 timeout을 12000까지 늘렸다 ㅜㅜ.. 원랜 60이다

  
