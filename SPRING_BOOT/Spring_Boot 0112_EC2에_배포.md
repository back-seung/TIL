## 0112 Spring_Boot_EC2서버에_프로젝트\_배포

* 오늘은 EC2 서버에 프로젝트를 배포한다.



## EC2에 프로젝트 클론하기

1. SSH를 열고 git을 설치한 다음, 설치 상태를 확인한다.

   > 근데 오늘은 평소 가던 카페가 아닌 다른 카페를 가서 SSH에 접속이 되지 않았다.
   >
   > 해결 : EC2에 현재 카페의 IP를 추가했다.

   `sudo yum install git`

   `git --version`

2. 프로젝트를 clone할 디렉토리를 생성하고 해당 디렉토리로 이동하여 프로젝트를 clone한다.

   `mkdir ~/app && ~app/step1` : app 및 app/step1 디렉토리 생성

   `cd ~/app/step1` : 생성된 디렉토리 이동

   `git clone 프로젝트_https` : 프로젝트 clone

3. `./gradlew test`를 통해 코드들이 잘 수행되는지 확인한다.

   > 난 Permission denied 가 떴다. 실행권한이 없다는 뜻이다. 그래서 `chmod +x ./gradlew`을 입력해 권한을 추가한뒤 다시 수행했다.

4. 배포를 위한 쉘 스크립트인 `deploy.sh`를 생성하고 코드를 추가했다.

   ```shell
   #!/bin/bash
   
   REPOSITORY=/home/ec2-user/app/step1
   PROJECT_NAME=Spring_BootPJ_booboo
   
   cd $REPOSITORY/$PROJECT_NAME/
   
   echo "> Git pull"
   
   git pull
   
   echo "> 프로젝트 빌드 시작"
   
   ./gradlew build
   
   echo "> step1 디렉토리로 이동"
   
   cd $REPOSITORY
   
   echo "> Build 파일 복사"
   
   cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/
   
   echo "> 현재 구동중인 애플리케이션 pid 확인"
   
   CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)
   
   echo "현재 구동중인 애플리케이션 : $CURRENT_PID"
   
   if [ -z "$CURRENT_PID" ]; then
           echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
   else
           echo "> kill -15 $CURRENT_PID"
           kill -15 $CURRNET_PID
           sleep 5
   fi
   
   echo "> 새 애플리케이션 배포"
   
   JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)
   
   echo "> JAR Name : $JAR_NAME"
   
   nohup java -jar -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties $REPOSITORY/$JAR_NAME 2>&1 &
   
   ```

   > vim을 사용해 본 경험이 적어서 좀 고생하긴 했는데 결국 잘 작성했다. 
   >
   > **그리고 Vim에서 띄어쓰기가 얼마나 중요한지도 알게 됐다,,,** 

5. chmod를 통해 권한을 추가하고 쉘스크립트를 실행한다.

   > `chmod +x ./deploy.sh`
   >
   > `./deploy.sh`

6. nohup.out을 통해 애플리케이션에서 출력되는 내용을 확인한다.

   > ClientResistraionRepository 에러가 발생한다. 공개된 저장소에 올린 코드에는 application.properties가 ignore되어 클라이언트 ID와 SECRET을 모르기 때문이다.
   >
   > app 디렉토리에 application-oauth.properties 를 생성하여 로컬의 코드를 그대로 복붙한 뒤, deploy.sh를 수정한다. 위의 코드에 이미 수정되어있다!

7. 다시 ./deploy.sh를 통해 실행한다.

   > 여기서 진짜 고생했다. 다행히 1시간도 안걸린 것 같은데
   >
   > 톰캣에서 8080포트를 이미 실행중이라고 하길래 윈도우 cmd를 키고 8080포트를 찾아서 죽이려니 없었다. 근데 여기서 "없는걸 어떻게 죽여?" 라는 생각만 하고 있었다. 내 잘못이다. 리눅스 포트를 확인했어야 되는데 멍청하게 계속 윈도우 cmd에서 찾았다.
   >
   > 그래도 결국 해결했다.

 

## 오늘 배운점

* 내가 지금 쓰고 있는 운영체제를 헷갈려하지 말자
* 결국 나는 실수와 잘못을 한다. 결과에 대해 내가 뭘 잘못했는지 다시 생각해보자



## 내일 할 일

* 내일 면접보러 간다. 그래서 오늘은 책 내용을 많이 따라가진 못하고 면접에 대해서 열심히 준비할 생각이다. (붙었으면 좋겠다)

