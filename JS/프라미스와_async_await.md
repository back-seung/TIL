## 프라미스와 await, async

> 출처 : [모던 JavaScript 튜토리얼](https://ko.javascript.info) 
>
> - 챕터마다 지정한 별(0 ~ 5)는 작성자(백승한)가 느끼는 난이도에 따라 지정되었습니다.
> - 어떤 챕터는 모르는 것, 헷갈리는 것 위주로만 정리하기도 하였습니다.



## 콜백 - 

> - 주의, 브라우저 메서드를 사용한다.
>
> 콜백, 프라미스 등을 어떻게 사용하는지 보여주기 위해 본 챕터에서는 브라우저 전용 메서드가 나온다. 스크립트를 불러오고 완성된 문서에 간단한 조작을 하는 예시에서 특히 브라우저 메서드가 사용될 것이다.
>
> 브라우저 메서드가 익숙하지 않아 예시가 잘 이해되지 않는다면 문서 [객체 모델의 챕터](https://ko.javascript.info/document) 몇개를 읽어보길 튜토리얼은 권한다.

자바스크립트 호스트 환경이 제공하는 여러 함수를 사용하면 비동기(asynchronous) 동작을 스케줄링 할 수 있다. 원하는 때에 동작이 시작하도록 할 수 있는 것이다.

setTimeout은 스케줄링에 사용되는 가장 대표적인 함수이다.

실무에서 맞닥뜨리는 비동기 동작은 아주 다양하다. 스크립트나 모듈을 로딩하는 것 또한 비동기 동작이다.(이 예시는 뒤에서 구체적으로 다룰 예정)

src에 있는 스크립트를 읽어오는 함수 loadScript(src)를 예시로 비동기 동작 처리가 어떻게 일어나는지 살펴보자.

```javascript
function loadScript(src) {
  // <script> 태그를 만들고 페이지에 태그를 추가한다.
  // 태그가 페이지에 추가되면 src에 있는 스크립트를 로딩하고 실행한다.
  let script = document.createElement('script');
  script.src = src;
  document.head.append(script);
}
```

함수 loadScript(src)는 \<script src="...">를 동적으로 만들고 이를 문서에 추가한다. 브라우저는 자동으로 태그에 있는 스크립트를 불러오고 로딩이 완료되면 스크립트를 실행한다.

loadScript(src) 사용법은 다음과 같다.

```javascript
// 해당 경로에 위치한 스크립트를 불러오고 실행함
loadScript('/my/script.js');
```

그런데 이 때 스크립트는 '비동기적으로' 실행된다. 로딩은 지금 당장 시작되더라도 실행은 함수가 끝난 후에야 되기 때문이다. 

따라서 loadScript(...) 아래에 있는 코드들은 스크립트 로딩이 종료되는 걸 기다리지 않는다.

```javascript
loadScript('/my/script.js');
// loadScript 아래의 코드는 스크립트 로딩이 끝날 때 까지 기다리지 않는다.
// ..
```

스크립트 로딩이 끝나자마자 이 스크립트를 사용해 무언가를 해야 한다고 가정해보자. 스크립트 안에 다양한 함수가 정의되어 있고 우리는 이 함수를 실행하길 원하는 상황이다.

그런데 loadScript('...')를 호출하자마자 내부 함수를 호출하면 원하는대로 동작하지 않는다.

```javascript
loadScript('/my/script.js'); // script.js엔 fuinction new Function() {...}이 있음
newFunction(); // 함수가 존재하지 않는다는 에러가 발생한다.
```

에러는 브라우저가 스크립트를 읽어올 수 있는 시간을 충분히 확보하지 못했기 때문에 발생한다. 그런데 현재로서는 함수 loadScript에서 스크립트 로딩이 완료되었는지 알 방법이 없다. 언젠간 스크립트가 로드되고 실행도 되겠지만 그게 다이다. 원하는 대로 스크립트 안의 함수나 변수를 사용하려면 스크립트 로딩이 끝났는지 여부를 알 수 있어야 한다.

loadScript의 두 번째 인수로 스크립트 로딩이 끝난 후 실행될 함수인 콜백(callback) 함수를 추가해보자.

```javascript
function loadScript(src, callback) {
  let script = document.createElement('script');
  script.src = src;
  
  script.onload = () => callback(script);
 
  document.head.append(script);
}
```

새롭게 불러온 스크립트에 있는 함수를 콜백 함수 안에서 호출하면 원하는 대로 외부 스크립트 안의 함수를 사용할 수 있다.

```javascript
loadScript('/my/script.js', function() {
  // 콜백 함수는 스크립트 로드가 끝나면 실행된다.
  newFunction(); // 이제 함수 호출이 제대로 동작한다.
});
```

이렇게 두번째 인수로 전달된 함수(대개 익명 함수)는 원하는 동작(위 예제에선 외부 스크립트를 불러옴)이 완료되었을 때 실행된다.

아래는 실제 존재하는 스크립트를 이용해 만든 예시이다

```javascript
function loadScript(src, callback) {
  let script = document.createElement('script');
  script.src = src;
  script.onload = () => callback(script);
  documnet.head.append(script);
}

loadScript('https://cdnjs.cloudflare.com/ajax/libs/lodash.js/3.2.0/lodash.js', script => {
  alert(`${script.src}가 로드되었습니다.`);
  alert( _ ); // 스크립트에 정의된 함수
});
```

**이런 방식을 콜백 기반 비동기 프로그래밍이라고 한다.** 무언가를 비동기적으로 수행하는 함수는 함수 내 동작이 모두 처리된 후 실행되어야 하는 함수가 들어갈 콜백을 인수로 반드시 제공해야 한다.

위 예시에선 loadScript의 인수로 콜백을 제공해 주었는데 이렇게 콜백을 사용한 방식은 비동기 프로그래밍의 일반적인 접근법이다.

### 콜백 속 콜백

스크립트가 두 개 있는 경우, 어떻게하면 두 스크립트를 순차적으로 불러올 수 있을까? 두 번째 스크립트 로딩은 첫 번째 스크립트의 로딩이 끝난 이후가 됟길 원한다면 말이다.

가장 자연스러운 해결법은 아래와 같이 콜백 함수 안에서 두 번째 loadScript를 호출하는 것이다.

```javascript
loadScript('/my/script.js', function(script) {
  alert(`${script.src}을 로딩했습니다. 이젠, 다음 스크립트를 로딩합니다`);
  
  loadScript('/my/script2.js', function(script) {
    alert(`두 번째 스크립트를 성공적으로 로딩했습니다.`);
  });
  
});
```

이렇게 중첩 콜백을 만들면 바깥에 위치한 loadScript가 완료된 후, 안쪽 loadScript가 실행된다. 여기에 하나를 더 추가하고 싶다면 어떻게 하면 될까

```javascript
loadScript('/my/script.js', function(script) {

  loadScript('/my/script2.js', function(script) {

    loadScript('/my/script3.js', function(script) {
      // 세 스크립트 로딩이 끝난 후 실행됨
    });

  })

});
```

위와 같이 모든 새로운 동작이 콜백 안에 위치하게 작성하면 된다. 그런데 이렇게 콜백안에 콜백을 넣는 것은 수행하려는 동작이 단 몇개 뿐이라면 문제가 없지만 동작이 많은 경우엔 좋지 않다. 다른 방식으로 코드를 작성하는 방법은 이따가 알아보자.

### 에러 핸들링

지금까지 살펴본 예시들은 스크립트 로딩이 실패하는 경우 등의 에러를 고려하지 않고 작성되었다. 그런데 스크립트 로딩이 실패할 가능성은 언제나 있다. 물론 콜백 함수는 이런 에러를 핸들링 할 수 있어야 한다.

loadScript에서 로딩 에러를 추적할 수 있게 기능을 개선해보자.

```javascript
function loadScript(src, callback) {
  let script = document.createElement('script');
  script.src = src;
  
  script.onload = () => callback(null, script);
  script.onerror = () => callback(new Error(`${src}를 불러오는 도중에 에러가 발생했습니다.`));
  
  document.head.append(script);
}
```

이제 loadScript는 스크립트 로딩에 성공하면 callback(null, script)를, 실패하면 callback(error)를 호출한다.

개선된 loadScript의 사용법은 다음과 같다.

```javascript
loadScript('/my/script.js', function(error, script) {
  if(error) {
    // 에러 처리
  } else {
    // 스크립트 로딩이 성공적으로 끝남
  }
});
```

이렇게 에러를 처리하는 방식은 흔히 사용되는 패턴이다. 이런 패턴은 '오류 우선 콜백(error-first callback)'이라고 불린다.

오류 우선 콜백은 다음 관례를 따른다.

1. callback의 첫 번째 인수는 에러를 위해 남겨둔다. 에러가 발생하면 이 인수를 이용해 callback(err)이 호출된다. 
2. 두 번째 인수(필요시 추가 가능)는 에러가 발생하지 않았을 때를 위해 남겨준다. 원하는 동작이 성공한 경우엔 callback(null, result1, result2)이 호출된다.

오류 우선 콜백 스타일을 사용하면 단일 콜백 함수에서 에러 케이스와 성공 케이스 모두를 처리할 수 있다.

### 멸망의 피라미드

콜백 기반 비동기 처리는 언뜻 보면 꽤 쓸만해보이고, 실제로도 그렇다. 한 개 혹은 두 개의 중첩 호출이 있는 경우는 보기에도 나쁘지 않다.

하지만 꼬리에 꼬리를 무는 비동기 동작이 많아지면 아래와 같은 코드 작성이 불가피해진다.

```javascript
loadScript('1.js', function(error, script) {
  if(error) {
    handleError(error);
  } else {
   	loadScript('2.js', function(error, script) {
      if(error) {
        handleError(error);
      } else {
        loadScript('3.js', function(error, script) {
          if(error) {
            handleError(error);
          } else {
            // 모든 스크립트가 로딩된 후, 실행 흐름이 이어진다.
          }
        });
        
      }
    });
  }
});
```

위 코드는 다음과 같이 동작한다.

1. 1.js를 로드한다. 그 후 에러가 없으면
2. 2.js를 로드한다. 그 후 에러가 없으면
3. 3.js를 로드한다. 그 후 에러가 없으면 또 다른 작업을 수행한다.

호출이 계속 중첩되면서 코드가 깊어지고 있다. 본문 중간중간 반복문, 조건문이 들어가면 관리는 특히 더 힘들어 질 것이다.

이렇게 깊은 중첩 코드가 만들어내는 패턴은 소위 '*콜백 지옥*' 혹은 '*멸망의 피라미드*'라고 불린다.

비동기 동작이 하나씩 추가될 떄마다 중첩 호출이 만들어내는 '피라미드'는 오른쪽으로 점점 커진다. 곧 손 쓸 수 없는 지경이 되어버린다.

그래서 이런 코딩 방식은 좋지 않다.

각 동작을 독립적 함수로 만들어 위와 같은 문제를 완화해보자.

```javascript
loadScript('1.js', step1);

function step1(error, script) {
  if (error) {
    handleError(error);
  } else {
    // ...
    loadScript('2.js', step2);
  }
}

function step2(error, script) {
  if (error) {
    handleError(error);
  } else {
    // ...
    loadScript('3.js', step3);
  }
}

function step3(error, script) {
  if (error) {
    handleError(error);
  } else {
    // 모든 스크립트가 로딩되면 다른 동작을 수행합니다. (*)
  }
};
```

새롭게 작성한 코드는 각 동작을 분리해 최상위 레벨의 함수로 만들었기 때문에 깊은 중첩이 없다. 그리고 콜백 기반 스타일 코드와 동일하게 동작한다.

그런데 이렇게 작성하면 동작상 문제는 없지만 코드가 종잇조각 같아 보인다는 문제가 생긴다. 읽는 것이 어려워진다.

게다가 step*N*이라고 명명한 함수들은 늘어난 depth를 피하려는 목적으로만 만들었기 때문에 재사용이 불가능하다. 그 누구도 연쇄 동작이 이뤄지는 코드 밖에서 함수들을 재활용하지 않을 것이다.

운 좋게도 이렇게 끊임없이 늘어나는 depth를 피할 방법이 몇가지 존재한다.

그 중 하나는  promise를 사용하는 것이다.



## 프라미스 -

당신은 아주 유명한 가수이다(가정). 그리고 밤,낮으로 당신의 작업물을 기대하며 언제 나오는지 묻는 팬들이 있다. 

당신은 앨범이 출시되면 팬들이 자동으로 소식을 받을 수 있게 준비했다. 구독 리스트를 만들어 팬들에게 전달해 이메일 주소를 적게하고 앨범 준비가 완료되면 리스트의 팬들에게 메일을 보내 관련 소식을 전해줄 수 있게끔 말이다. 이렇게 하면 천재지변 등에 의한 앨범 취소 불상사 등에도 대비할 수 있다.

이 비유는 코드를 작성하면서 자주 만나는 상황을 실제 일어날 법한 일로 바꾼 것이다. 바로 아래와 같은 상황 말이다.

1. 제작 코드(producing code)는 원격에서 스크립트를 불러오는 등의 시간이 걸리는 일을 한다. 위 비유에서는 가수가 제작 코드에 해당한다.
2. 소비 코드(ocnsuming code)는  제작 코드의 결과를 기다렸다가 이를 소비한다. 이때 소비 주체(함수)는 여럿이 될 수 있다. 위 비유에서 소비코드는 팬이다.
3. 프라미스(promise)는 제작 코드, 소비 코드를 연결해 주는 특별한 자바스크립트 객체이다. 위 비유에서 프라미스는 '구독 리스트'이다. 프라미스는 시간이 얼마나 걸리는지에 상관 없이 약속한 결과를 만들어내는 제작 코드가 준비 되었을 때 모든 소비 코드가 결과를 사용할 수 있도록 해준다.

사실 프라미스는 구독 리스트보다 훨씬 복잡하기 때문에 위 비유가 완벽히 들어맞지는 않는다. 프라미스엔 더 많은 기능, 한계가 있다.

promise 객체는 아래와 같은 문법으로 만들 수 있다.

```javascript
let promise = new Promise(function(resolve, reject) {
  // executor (제작코드, '가수');
});
```

`new Promise`에 전달되는 함수는 executor라고 부른다. executor는 new Promise가 만들어질 때 자동으로 실행되는데 결과를 최종적으로 만들어내는 제작 코드를 포함한다. 위 비유에서 가수가 바로 executor이다.

executor의 인수 resolve, reject는 자바스크립트 자체 콜백이다. 개발자는 resolve, reject를 신경쓰지 않고 executor 코드만 작성하면 된다.

대신 executor에서는 결과를 즉시 또는 늦게 얻는 것에 상관없이 상황에 따라 인수로 넘겨준 콜백 중 하나를 반드시 호출해야 한다.

- resolve(value) - 일이 성공적으로 끝난 경우 그 결과를 나타내는 value와 함께 호출
- reject(error) - 에러 발생 시 에러 객체를 나타내는 error와 함께 호출

요약하면 다음과 같다.  executor는 자동으로 실행되는데 여기서 원하는 일이 처리된다. 처리가 끝나면 executor는 처리 성공 여부에 따라 resolve나 reject를 호출한다.

한편, new Promise 생성자가 반환하는 promise 객체는 다음과 같은 내부 프로퍼티를 갖는다. 

- state - 처음엔  pending(보류)였다가 resolve가 호출되면 fulfilled, reject가 호출되면 rejected로 변한다.
- result - 처음엔 undefined였다가 resolve가 호출되면 value, reject(error)가 호출되면  error로 변한다.

따라서 executor는 아래 그림과 같이 promise의 상태를 둘 중 하나로 변화시킨다.

![스크린샷 2022-10-26 00.44.24](https://raw.githubusercontent.com/back-seung/Today_I_Learned/master/uPic/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-10-26%2000.44.24.png)

'팬들'이 어떻게 이 변화를 구독할 수 있는지에 대해서는 조금 후에 살펴보자.

그 전에 promise 생성자와 간단한 executor 함수로 만든 예시를 살펴보자. setTimeout을 이용해  executor 함수는 약간의 시간이 걸리도록 구현했다.

```javascript
let promise = new Promise(function(resolve, reject) {
  // 프라미스가 만들어지면 executor 함수는 자동으로 실행된다.
  
  // 1초 뒤에 일이 성공적으로 끝났다는 신호가 전달되며 result는 '완료'가 된다.
  setTimeout(() => resolve('완료'), 1000);
});
```

위 예시를 통해 우리가 알 수 있는 것은 두 가지이다.

1. executor는 new Promise에 의해 자동으로 그리고 즉각적으로 호출된다.
2. executor는 인자로 resolve와 reject 함수를 받는다. 이 함수들은 자바스크립트 엔진이 미리 정의한 함수이므로 개발자가 따로 만들 필요가 없다. 다만  resolve, reject 중 하나는 무조건 호출해야 한다.

executor '처리'가 시작된 지 1초 후, resolve('완료')가 호출되고 결과가 만들어진다. 이 때 promise 객체의 상태는 다음과 같이 변화된다.

![스크린샷 2022-10-26 00.48.37](https://raw.githubusercontent.com/back-seung/Today_I_Learned/master/uPic/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-10-26%2000.48.37.png)

이처럼 일이 성공적으로 처리되었을 때의 프라미스는 fulfilled promise(약속이 이행된 프라미스)라고 불린다.

이번에는 executor가 에러와 함께 약속한 작업을 거부하는 경우에 대해 살펴보자.

```javascript
let promise = new Promise(function(resolve, reject) {
  // 1초 뒤에 에러와 함께 실행이 종료되었다는 신호를 보낸다.
  setTimeout(() => reject(new Error('에러 발생!')), 1000);
});
```

1초 후 reject(...)가 호출되면 promise의 상태가 rejected로 변한다.

![스크린샷 2022-10-26 00.51.07](https://raw.githubusercontent.com/back-seung/Today_I_Learned/master/uPic/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-10-26%2000.51.07.png)

executor는 보통 시간이 걸리는 일을 수행한다. 일이 끝나면  resolve, reject 함수를 호출하는데 이때 프라미스 객체의 상태가 변화한다.

이행 혹은 거부 상태의 프라미스는 처리된(settled) 프라미스라고 부른다. 반대되는 프라미스로 대기(pending) 상태의 프라미스가 있다. 

> - **프라미스는 성공, 실패 둘 중 하나만 한다.**
>
> executor는 resolve, reject 중 하나를 반드시 호출해야 한다. 이때 변경된 상태는 더 이상 변하지 않는다.
>
> 처리가 끝난 프라미스에 resolve, reject를 호출하면 무시된다.
>
> ```javascript
> let promise = new Promise(function(resolve, reject) {
>   resolve('완료');
>   
>   reject(new Error('...')); // 무시된다.
>   setTimeout(() => resolve('완료')); // 무시된다. 
> });
> ```
>
> 이렇게 executor에 의해 처리가 끝난 일은 결과 혹은 에러만 가질 수 있다.
>
> 여기에 더하여 resolve, reject는 인수를 하나만 받고(혹은 아무것도 받지 않음) 그 이외의 인수는 무시한다는 특성도 있다.

> - **`Error` 객체와 함께 거부하기**
>
> 무언가 잘못된 경우, executor는 `reject`를 호출해야 한다. 이때 인수는 `resolve`와 마찬가지로 어떤 타입도 가능하지만 `Error` 객체 또는 `Error`를 상속받은 객체를 사용할 것을 추천한다. 

> - **`resolve`·`reject` 함수 즉시 호출하기**
>
> executor는 대개 무언가를 비동기적으로 수행하고, 약간의 시간이 지난 후에 `resolve`, `reject`를 호출하는데, 꼭 이렇게 할 필요는 없다. 아래와 같이 `resolve`나 `reject`를 즉시 호출할 수도 있다.
>
> ```javascript
> let promise = new Promise(function(resolve, reject) {
>   // 일을 끝마치는 데 시간이 들지 않음
>   resolve(123); // 결과(123)를 즉시 resolve에 전달함
> });
> ```
>
> 어떤 일을 시작했는데 알고 보니 일이 이미 끝나 저장까지 되어있는 경우, 이렇게 `resolve`나 `reject`를 즉시 호출하는 방식을 사용할 수 있다.
>
> 이렇게 하면 프라미스는 즉시 이행 상태가 된다.

> - **`state`와 `result`는 내부에 있습니다.**
>
> 프라미스 객체의 `state`, `result` 프로퍼티는 내부 프로퍼티이므로 개발자가 직접 접근할 수 없다. `.then`/`.catch`/`.finally` 메서드를 사용하면 접근 가능하다



### 소비자: then, catch, finally

