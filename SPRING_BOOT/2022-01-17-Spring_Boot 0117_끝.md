## 0117 ~ 18 Spring_Boot_Nginx

* 오늘은 드디어 책의 끝까지 실습을 완료했다.



## 무중단 배포를 위한 profile 추가 && Nginx 설정

> src/main/resources에 proifle 2개를 추가한다.

1. application-real1.properties

   ```properties
   server.port=8081
   spring.profiles.include=oauth,real-db
   spring.jpa.properties.hiberante.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
   spring.session.store-type=jdbc
   ```

2. application-real2.properties

   ```properties
   server.port=8082
   spring.profiles.include=oauth,real-db
   spring.jpa.properties.hiberante.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
   spring.session.store-type=jdbc
   ```



> 엔진엑스 설정 수정 : 프록시 설정을 교체한다.

1. sudo vim /etc/nginx/conf.d/service-url.inc를 통해 파일을 생성한다. 그리고 아래와 같이 작성한다

   ```sh
   set $service_url http://127.0.0.1:8080;
   ```

2. 저장 및 종료(:wq)한 뒤 `nginx.conf`를 연다.

3. location / 부분에서 다음과 같이 변경한다.

   ```sh
   include /etc/nginx/conf.d/service-url.inc
   
   location / {
   				proxy_pass $service_url;
   }
   ```

4. 저장하고 종료한 뒤 재시작한다.

   `sudo service nginx restart`



> 배포 스크립트들 작성

* step2와 중복되지 않게 step3와 step/zip 디렉토리를 생성한다.

  `mkdir ~/app/step3/ && mkdir ~/app/step3/zip`

1. 무중단 배포 디렉토리를 변경한다 `appspec.yml ` 를 수정.

   ```yaml
   .
   .
   .
   destination: /hone/ec2-user/app/step3/zip/
   ```

2. 무중단 배포를 진행할 스크립트를 총 5개를 만든다.

   * stop.sh : 기존 엔진엑스와 연결되어 있지 안힞만 실행중이던 스프링 부트 종료
   * start.sh : 배포할 신규 버전 스프링 부트 프로젝트를 stop.sh로 종료한 profile로 실행
   * health.sh : 'start.sh'로 실행시킨 프로젝트가 정상적으로 실행됐는지 체크
   * switch.sh : 엔진엑스가 바라보는 스프링 부트를 최신버전으로 변경
   * profile.sh : 앞선 4개 스크립트 파일에서 공용으로 사용할 'profile'과 포트 체크 로직

   

3. appspec.yml에 앞선 스크립트를 사용하도록 설정

   ```yaml
   hooks:
     AfterInstall:
       - location: stop.sh # 엔진 엑스에 연결되어 있지 않은 스프링부트 종료
         timeout: 60
         runas: ec2-user
     ApplicationStart:
       - location: start.sh # 엔진엑스와 연결되어 있지 않은 Port로 새 버전의 스프링부트 시작
         timeout: 60
         runas: ec2-user
     ValidateService:
       - location: health.sh # start.sh로 실행시킨 프로젝트가 정상 실행했는지 확인
         timeout: 60
         runas: ec2-user
   ```

4. 그 외 나머지 스크립트

* `profile.sh`

```sh
#!/usr/bin/env bash

# 쉬고 있는 profile 찾기 : real1이 사용중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ] # 400보다 크면 즉 , 40x/50x 에러 모두 포함

  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"
}

#쉬고 이쓴ㄴ profile의 port 찾기
function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}
```



* `start.sh`

```sh
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=Spring_BootPJ_booboo

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE로 실행합니다."
nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=$IDLE_PROFILE \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
```



* `stop.sh`

```sh
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT에서 구동 중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
```

* `switch.sh`

```sh
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 Port : $IDLE_PORT"
  echo "> Port 전환"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  echo "> 엔진엑스 Reload"
  sudo service nginx reload
}
```

* `health.sh`

```sh
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then # UP_COUNT >= 1 ("real1" 문자열이 있는지 검증)
      echo "> Health Check 성공"
      switch_proxy
      break
  else
      echo "> Health Check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
      echo "> Health Check : ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health Check 실패. "
    echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health Check 연결 실패. 재시도..."
  sleep 10

done

```



### 무중단 배포 테스트

> 잦은 배포로 Jar 파일명이 겹칠 경우를 대비해 자동으로 버전값이 변경될 수 있도록 build.gradle을 수정하여 조치

```groovy
version '1.0.1-SNAPSHOT-'+new Date().format("yyyyMMddHHmmsss")
```



마지막으로 푸쉬 한다.



## 내일 할 일

* 내일은 면접보고 총 정리를 해야겠다.
