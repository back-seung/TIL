## 리눅스 & 유닉스 계열의 디렉토리 구조

### `/` - 최상위 디렉토리

/는 최상위 디렉토리(루트)를 의미한다.

```sh
# 최상위 디렉토리로 이동
cd /
```

 

### 최상위 디렉토리 기준 구조

```sh
/
 ㄴ /bin
 ㄴ /sbin
 ㄴ /etc
 ㄴ /dev
 ㄴ /proc
 ㄴ /var
 ㄴ /tmp
 ㄴ /usr
 ㄴ /home
 ㄴ /boot
 ㄴ /lib
 ㄴ /opt
 ㄴ /mnt
 ㄴ /media
 ㄴ /src
 	
```



### `/bin`

**User Binaries**.  

실행 가능한 프로그램을 바이너리라고도 부른다. 이를 줄여서 `bin`으로 표현한 것. 사용자들이 사용하는 명령들이 위치하고 있다.



### `/sbin`

**System Binaries**

시스템 관리자(루트 유저)가 사용하는 프로그램이 위치하고 있다. 



### `/etc`

**Configuration Files**

대부분의 프로그램의 설정 파일이 위치하고 있다. 프로그램들은 이 설정 파일을 참고하여 어떻게 실행될 것인지 정해진다.



### `/var`

**Variable Files**

내용 또는 용량이 변경될 수 있는 파일들이 위치하고 있다. 

 

### `/tmp`

**Temp Files**

 임시파일들이 위치하고 있다. 임시파일이므로 컴퓨터를 재부팅하면 내용이 사라진다. 



### `/home`

**Home Directories**

사용자들의 디렉토리이다. 운영체제의 사용자(나 = `seung`)의 디렉토리가 위치하고 있다.



### `/lib`

**System Libraries**

bin과 sbin이 공통으로 사용하는 라이브러리가 위치한다.



### `/opt`

**Optional add-on Applications**

 어떠한 소프트웨어를 설치할 때 그 소프트웨어를 특정 디렉토리에 지정해야 될 때 opt에 설치하는 것이 좋은 방법이다.



### `/usr`

**User Programs**

설치하는 프로그램들은  /usr 밑에 설치가 되고, 기본적으로 Unix 계열에 설치가 되어서 bundle의 형태로 제공되는 앱은 /bin /sbin에 설치 된다. 





