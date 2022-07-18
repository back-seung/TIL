# Error 정리



## 송수신 메시지 조회 - 연계 에러

###  I/O error on POST request for "http://xxx.x.x.x:xxxxx/xxxxx/api/XX_XXX_001": Unexpected end of file from server; nested exception is java.net.SocketException: Unexpected

* 참고 : https://stackoverflow.com/questions/19824339/java-simple-code-java-net-socketexception-unexpected-end-of-file-from-server

> `예상하지 않은 파일의 끝`은 원격 서버가 응답을 보내지 않고 연결을 수락하고 닫았음을 의미한다
>
> 1. 서버가 너무 바빠 요청을 처리할 수 없었음
> 2. 연결을 임의로 삭제하는 네트워크 버그가 있을 수 있음



## IMC_Renewal 



### Java compiler level does not match the version of the installed Java project facet (Java Version Mismatch) - 22/04/20 

```xml
<!-- java compile version -->
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-compiler-plugin</artifactId>
	<version>3.1</version>
	<configuration>
		<source>1.7</source>
		<target>1.7</target>
	</configuration>
</plugin>
```

> 프로젝트의 자바 컴파일러와 `pom.xml`의 plugins 정보가 서로 맞지 않은게 문제였다. source와 target의 버전이 1.6으로 되어 있었고, 이 때문에 
> `Dynamic Web Module 3.1 requires Java 1.7 or newer` 에러까지 발생하게 되었다. 
>
> **source와 target을 1.7로 수정하면서 해결.**



### Could not initialize class org.apache.maven.plugins.war.util.WebappStructureSerializer - 22/04/20

```xml
<!-- dynamic web module version -->
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-war-plugin</artifactId>
	<version>3.0.0</version>
	<configuration>
		<warSourceDirectory>target/build/production/IndigoESBWebConsole</warSourceDirectory>
	</configuration>
</plugin>
```

> `pom.xml`에 등록한 dependency `maven-war-plugin`의 버전이 원래는 2.5로 되어있었다.
>
> **버전을 3.2.2버전으로 올려서 다시 `maven -> update Project`를 통해 빌드하여 해결. **



### 'dependencies.dependency.systemPath' for ~~~ must specify an absolute path but is ${basedir/webapp/WEB-INF/lib/ojdbc6.jar

> pom.xml에서 systemPath로 저장해둔 `${basedir}`을 maven이 인식을 못해서 생기는 에러였다.
>
> 구글링을 열심히 해보다가 **프로젝트의 JRE System Library를 Execution environment에서 Workspace default JRE로 바꿔주니 해결됐다**.
>
> 정확한 해결 방법인지는 확실하지 않다.



