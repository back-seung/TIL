## instanceOf

강제 타입 변환에 따른 ClassCastException 예외 발생을 피하기 위해 사용한다. 우선 ClassCastException을 먼저 알아보자. 



### ClassCastException

> *Thrown to indicate that the code has attemped to cast an object to a subclass of which it is not an instanace*
>
> 객체의 형을 변환할 때 객체 타입 변환이 적절하지 않을시 발생

```java
// 상속관계
java.lang.Object
 ㄴjava.lang.Throwable
   ㄴjava.lang.Exception
     ㄴjava.lang.RuntimeException
  		 ㄴjava.lang.ClassCastException
```

객체 타입 지정 및 캐스팅 시 자주 발생되는 오류로 Integer값을 boolean에 담는다던지 변환될 수 없는 객체를 타입 변환(캐스팅)하였을 시에 발생한다.



### instanceOf 사용에 따른 ClassCastException 예외 피하기

