## 0110 Spring_Boot_AWS

* 오늘은 AWS 클라우드 서비스를 통해 서버 배포를 진행한다.



## AWS(Amazon Web Services)란

* 아마존 닷컴에서 개발한 **클라우드** 컴퓨팅 플랫폼.
* 네트워킹 기반의 가상 컴퓨터 스토리지, 인프라 등 다양한 서비스를 제공하여 개발자가 직접 할 일을 AWS가 전부 지원한다.

### 클라우드 컴퓨팅이란?

> 물리적인 컴퓨팅 리소스를 네트워킹 기반의 서비스 형태로 제공하는 것.

### 클라우드의 형태 (3가지)

1. IaaS(Infrastructure as a Service) 

   > * 기존 물리 장비를 미들웨어와 함께 묶어둔 추상화 서비스
   > * 가상머신, 스토리지, 네트워크, 운영체제 등 클라우드 IT의 기본 인프라 대여 서비스
   > * AWS의 EC2, S3 등이 해당된다.

2. PaaS(Platform as a Service)

   > * IaaS를 한 번 더 추상화한 서비스(DB || Application 서버 등), 많은 기능이 자동화 되어있음.
   > * AWS의 Beanstalk, Heroku 등이 해당된다.

3. SaaS(Software as a Service)

   > * 소프트웨어 서비스
   > * 구글 드라이브, Dropbox, 와탭 등이 해당



## AWS 클라우드 컴퓨팅 장점

1. 비용이 저렴하다.

   > 첫가입 1년 동안은 대부분의 서비스가 무료(**약간의 제한은 존재**)이다. 

2. 속도 및 민첩성이 개선된다.

   > 언어 및 운영체제에 걸림돌이 없는 플랫폼이기 때문이다.

3. 설치가 빠르고 관리가 편하다.



## AWS 회원가입 

> 본격적으로 AWS 회원가입을 진행한다.
> 준비물로 **Visa** 혹은 **MasterCard**가 있어야한다.



1. [AWS공식사이트](https://aws.amazon.com/ko/) 로 이동한다.
2. AWS 가입 버튼을 클릭하면 회원가입 폼이 나온다 순서대로 입력한다.
3. 계정 생성을 위해 **영문 주소가**필요하니 참고하자.
4. 준비한 카드의 정보를 등록한다.
5. SMS 인증을 한다.
6. Support 플랜 중 **기본 지원** 플랜을 선택한다.
7. 등록완료![AWSRegistration](https://user-images.githubusercontent.com/84169773/148743512-740866cf-3715-4d6d-9105-08b5aaf8fb9e.png)



## EC2 인스턴스 생성

* EC2란?

  > AWS에서 제공하는 성능, 용량 등을 유동적으로 사용할 수 있는 서버
  >
  > 흔히 AWS에서 Linux, Windows 서버를 사용한다고 말하는 것이 EC2이다.

* 제한

  > 사양이 t2.micro만 가능하다.
  >
  > 월 750시간의 제한이 있다. 초과시 비용이 부과된다.

* 만들기 전 **리전**을 체크한다.

  > 리전이란, AWS의 서비스가 구동될 지역을 말한다.
  >
  > AWS는 도시별 클라우드 센터에서 구축된 가상머신을 사용한다. 따라서 서울로 리전을 바꾸어준다.

* 리전까지 체크가 되었으면 검색창에 EC2를 검색하여 서비스를 클릭한 뒤 **인스턴스 생성**버튼을 누른다.



### AMI(Amazon Machine Image) 선택 및 인스턴스 생성까지 순서

* AMI란?

  > EC2 인스턴스를 시작하는데 필요한 정보를 이미지(**가상머신에 운영체제 등을 설치할 수 있게**)로 만들어 둔 것.

* 책에서는 Amazon Linux 1버전을 사용하라고 적혀져 있지만 22년 1월 시점에서는 해당 버전을 지원하지 않아 제일 먼저 보이는 **Amazon Linux 2 AMI (HVM) - Kernel 5.10, SSD Volume Type**을 선택하여 인스턴스 유형을 **t2.micro**로 선택했다.

* t2.micro란?

  > 요금 타입(t2), 사양(micro)이다.
  > T 시리즈는 범용 시리즈라고 불리기도 한다.

* 다음 버튼을 누르면 인스턴스 세부정보 탭이 나오는데 1대의 서버만 사용하니 기본값으로 놔두고 스토리지로 넘어간다.

* 스토리지에서 크기의 기본값은 8GB인데 30GB까지는 프리티어로 지원이 된다. 따라서 30GB로 바꾸어준다.
  ![StoragePretier](https://user-images.githubusercontent.com/84169773/148743546-85b01e66-d0de-4fc2-a615-a6f8de003f49.png)

* 다음은 태그이다. SNS에서의 태그와 동일한 역할을 한다고 생각하면 되는데, 해당 인스턴스를 표현하는 여러 이름으로 사용될 수 있다.

* 그리고 중요한 보안그룹 설정이다.
  ![securityGroup](https://user-images.githubusercontent.com/84169773/148743567-1a30d6ef-ef9b-4cd6-9359-f989dd491f36.png)

  > * 유형이 SSH이고 포트가 22인 경우는 AWS EC2 터미널로 접속할 때이다.
  > *  **pem**키가 없으면 접속이 안되니 전체 오픈(0.0.0.0/0, ::/0)하는 경우가 종종 발견되는데, 실수로 pem키가 노출되는 순간 서버에서 가상화폐가 채굴된다. 
  > * 집 IP를 기본적으로 추가하고 집 외에 다른 공간에서는 해당 장소 IP를 SSH 규칙에 추가해야 안전하다.
  > * 8080은 열여놔도 위험한 일이 아니니 괜찮다.

* 마지막으로 pem키를 설정하면 인스턴스가 생성된다.

  > * pem 키는 비밀키이다. 절대 노출되서는 안되고 디렉토리에 절대 잃어버리지 않게 잘 보관한다.



### EIP 할당

> 인스턴스는 결국 하나의 서버이기 때문에 IP가 존재한다.
>
> **인스턴스 생성**  || **인스턴스 재시작**이 될 때마다 새 IP가 할당되기 때문에 IP 주소를 접근할 때마다 확인해야 되는 번거로움이 있다. 그렇기 때문에 EIP를 할당한다.
>
> 🧐 AWS의 고정 IP를 Elastic IP(탄력적 IP)라고 한다.

* 탄력적 IP 탭에서 새주소 할당 버튼을 클릭하여 새 주소를 할당한다.
* 할당된 EIP를 인스턴스와 꼭 연결한다( 연결하지 않으면 비용이 부과된다. 만약 사용할 인스턴스가 없을 때도 EIP를 삭제한다.)



## 로컬 PC에서 EC2 서버에 접속하기

> 난 윈도우를 사용하기 때문에 책 설명의 윈도우 파트를 따라 했다.

1. 별도의 ssh 접속 클라이언트- [putty 사이트](https://www.putty.org/)에 접속하여 실행 파일을 내려받는다.

2. puttygen.exe와 putty.exe를 다운로드 받고 puttygen을 실행하여 pem키를 ppk 변환하여 파일로 생성한다.

3. 이후 putty.exe를 실행시켜 Host Name 폼에 `ec2-user@public_ip`를 입력한다.

   > Amazon Linux에서 생성한 ec2-user가 username이다.
   >
   > public_ip 에는 아까 할당 받은 탄력적 주소를 입력한다. 

4. 그리고 Connection/SSH/Auth 탭에 차례대로 접근하여 puttygen.exe에서 변환된 pem키인 ppk파일을 불러온다.

5. 다시 Session탭으로 이동하여 저장할 이름을 입력하고 save 버튼을 클릭한 뒤, open 한다.

6. 경고창이 뜰텐데, Accept를 클릭한다.

7. 다음과 같이 SSH 접속이 성공하였다.
   ![SSHSuccess](https://user-images.githubusercontent.com/84169773/148743599-d85ff3dc-05f4-4a2a-a4fd-5828988173f5.png)

> 이제 리눅스 운영서버에서 해야 할 설정을 진행한다.



## 아마존 리눅스 서버 생성시 꼭 해야 할 설정

> * JAVA 8 설치 : 프로젝트 버전에 맞게 8버전을 설치한다.
>
>   1. `sudo yum install -y java-1.8.0-openjdk-devel.x86_64 `
>   2. `sudo /usr/sbin/alternatives --config java`
>   3. 설치된 버전의 번호 입력
>   4. `java -version` 버전 확인
>
>   
>
> * 타임존 변경 : 기본 서버 시간은 미국 시간대이다. 한국으로 바꿔야 한다.
>
>   1. `sudo rm /etc/localtime`
>      `sudo In -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime`
>   2. `date`를 입력하여 타임존이 KST로 변경된 것을 확인한다.
>
>   
>
> * 호스트네임 변경 : 접속한 서버의 별명 등록한다(IP만으로는 어떤 서버가 어떤 역할을 하는지 알 수가 없기 때문) 
>
>   1. `sudo vim /etc/sysconfig/network`를 입력해 편집 파일을 연다.
>   2. HOSTNAME으로 되어있는 부분을 원하는 서비스 명으로 변경.
>
>   를 하려고 했는데 1버전과 2버전은 설정법이 다르더라. 바로 구글링 했다.
>
>   > [출처 - 꾸준하게 차근차근 블로그](https://bbeomgeun.tistory.com/157)
>
>   1. `sudo hostnamectl set-hostname 원하는이름.localdomain`으로 입력하고 재부팅하면 호스트 네임이 변경된다.
>   2. 한가지 더 작업을 해야하는데 호스트 주소를 찾을 때 가장 먼저 검색해보는 `/etc/hosts`에 변경한 호스트네임을 등록한다. 다음과 같이 입력 `sudo vim /etc/hosts`
>   3. vim 편집기에서 등록한 호스트네임을 입력한다.
>   4. `curl 등록한 호스트 이름` 으로 적용이 되었는지 확인한다. 아래와 같이 떠야 정상
>      `curl: (7) Failed to connect to seung-springboot-webservice port 80: Connection refused`  - 80 포트로 실행된 서비스가 없음을 의미한다.



## 오늘 배운 것

* AWS와 클라우드 서비스란?
* AWS의 관리형 가상 서버인 EC2 생성 방법
* EC2 서버에 EIP(탄력적 IP) 할당 방법
* 인스턴스 접근을 위한 pem키 사용 방법
* 리눅스 서버 생성시 해야할 리눅스 설정



## 내일 할 일

* AWS에 데이터베이스 환경 만들기 - AWS RDS

