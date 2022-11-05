## 프라미스와 await, async

> 출처 : [모던 JavaScript 튜토리얼](https://ko.javascript.info) 
>
> - 챕터마다 지정한 별(0 ~ 5)는 작성자(백승한)가 느끼는 난이도에 따라 지정되었습니다.
> - 어떤 챕터는 모르는 것, 헷갈리는 것 위주로만 정리하기도 하였습니다.



## 콜백 - ⭐

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



## 프라미스 - ⭐️⭐️

당신은 아주 유명한 가수이다(가정). 그리고 밤,낮으로 당신의 작업물을 기대하며 언제 나오는지 묻는 팬들이 있다. 

당신은 앨범이 출시되면 팬들이 자동으로 소식을 받을 수 있게 준비했다. 구독 리스트를 만들어 팬들에게 전달해 이메일 주소를 적게하고 앨범 준비가 완료되면 리스트의 팬들에게 메일을 보내 관련 소식을 전해줄 수 있게끔 말이다. 이렇게 하면 천재지변 등에 의한 앨범 취소 불상사 등에도 대비할 수 있다.

이 비유는 코드를 작성하면서 자주 만나는 상황을 실제 일어날 법한 일로 바꾼 것이다. 바로 아래와 같은 상황 말이다.

1. 제작 코드(producing code)는 원격에서 스크립트를 불러오는 등의 시간이 걸리는 일을 한다. 위 비유에서는 가수가 제작 코드에 해당한다.
2. 소비 코드(consuming code)는  제작 코드의 결과를 기다렸다가 이를 소비한다. 이때 소비 주체(함수)는 여럿이 될 수 있다. 위 비유에서 소비코드는 팬이다.
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

프라미스 객체는 executor와 결과나 에러를 받을 소비 함수를 이어주는 역할을 한다. 소비함수는 .then, .catch, .finally 메서드를 사용해 등록된다.

### then

.then은 프라미스에서 가장 중요하고 기본이 되는 메서드이다.

문법은 다음과 같다.

```javascript
promise.then(
	function(result) { /*결과를 다룬다.*/ },
  function(error) { /*에러를 다룬다*/ }   
);
```

`.then`의 첫 번째 인수는 프라미스가 이행되었을 때 실행되는 함수이고, 여기서 실행 결과를 받는다.

`.then`의 두 번째 인수는 프라미스가 거부되었을 때 실행되는 함수이고, 여기서 에러를 받는다.

아래 예시는 성공적으로 이행된 프라미스에 어떻게 반응하는지 보여준다.

```javascript
let promise = new Promise(function(resolve, reject) {
  setTimeout(() => resolve("완료!"), 1000);
});

// resolve 함수는 .then의 첫 번째 함수(인수)를 실행한다.
promise.then(
  result => alert(result), // 1초 후 "완료!"를 출력
  error => alert(error) // 실행되지 않음
);
```

첫 번째 함수가 실행되었다.

프라미스가 거부된 경우에는 아래와 같이 두 번째 함수가 실행된다.

```javascript
let promise = new Promise(function(resolve, reject) {
  setTimeout(() => reject(new Error("에러 발생!")), 1000);
});

// reject 함수는 .then의 두 번째 함수를 실행한다.
promise.then(
  result => alert(result), // 실행되지 않음
  error => alert(error) // 1초 후 "Error: 에러 발생!"을 출력
);
```

작업이 성공적으로 처리된 경우만 다루고 싶다면 `.then`에 인수를 하나만 전달하면 된다.

```javascript
let promise = new Promise(resolve => {
  setTimeout(() => resolve("완료!"), 1000);
});

promise.then(alert); // 1초 뒤 "완료!" 출력
```

### catch

에러가 발생한 경우만 다루고 싶다면 `.then(null, errorHandlingFunction)`같이 `null`을 첫 번째 인수로 전달하면 된다. `.catch(errorHandlingFunction)`를 써도 되는데, `.catch`는 `.then`에 `null`을 전달하는 것과 동일하게 작동한다.

```javascript
let promise = new Promise((resolve, reject) => {
  setTimeout(() => reject(new Error("에러 발생!")), 1000);
});

// .catch(f)는 promise.then(null, f)과 동일하게 작동한다
promise.catch(alert); // 1초 뒤 "Error: 에러 발생!" 출력
```

`.catch(f)`는 문법이 간결하다는 점만 빼고 `.then(null,f)`과 완벽하게 같다.

### finally

`try {...} catch {...}`에 finally 절이 있는 것처럼, 프라미스에도 `finally`가 있다.

프라미스가 처리되면(이행이나 거부) `f`가 항상 실행된다는 점에서 `.finally(f)` 호출은 `.then(f, f)`과 유사하다.

쓸모가 없어진 로딩 인디케이터(loading indicator)를 멈추는 경우같이, 결과가 어떻든 마무리가 필요하면 `finally`가 유용하다.

사용법은 아래와 같다.

```javascript
new Promise((resolve, reject) => {
  /* 시간이 걸리는 어떤 일을 수행하고, 그 후 resolve, reject를 호출함 */
})
  // 성공·실패 여부와 상관없이 프라미스가 처리되면 실행됨
  .finally(() => 로딩 인디케이터 중지)
  .then(result => result와 err 보여줌 => error 보여줌)
```

그런데 finally는 `.then(f, f)`과 완전히 같진 않다. 차이점은 다음과 같다.

1. `finally` 핸들러엔 인수가 없다. `finally`에선 프라미스가 이행되었는지, 거부되었는지 알 수 없다. `finally`에선 절차를 마무리하는 ‘보편적’ 동작을 수행하기 때문에 성공·실패 여부를 몰라도 된다.

2. `finally` 핸들러는 자동으로 다음 핸들러에 결과와 에러를 전달한다.

   result가 `finally`를 거쳐 `then`까지 전달되는 것을 확인해보자.

   ```javascript
   new Promise((resolve, reject) => {
     setTimeout(() => resolve("결과"), 2000)
   })
     .finally(() => alert("프라미스가 준비되었습니다."))
     .then(result => alert(result)); // <-- .then에서 result를 다룰 수 있음
   ```

   프라미스에서 에러가 발생하고 이 에러가 `finally`를 거쳐 `catch`까지 전달되는 것을 확인해보자

   ```javascript
   new Promise((resolve, reject) => {
     throw new Error("에러 발생!");
   })
     .finally(() => alert("프라미스가 준비되었습니다."))
     .catch(err => alert(err)); // <-- .catch에서 에러 객체를 다룰 수 있음
   ```

   `finally`는 프라미스 결과를 처리하기 위해 만들어 진 게 아니다. 프라미스 결과는 `finally`를 통과해서 전달된다. 이런 특징은 아주 유용하게 사용되기도 한다.

   프라미스 체이닝과 핸들러 간 결과 전달에 대해선 다음 챕터에서 더 이야기 나누도록 하겠다.

3. `.finally(f)`는 함수 `f`를 중복해서 쓸 필요가 없기 때문에 `.then(f, f)`보다 문법 측면에서 더 편리하다.

**처리된 프라미스의 핸들러는 즉각 실행된다.**

프라미스가 대기 상태일 때, `.then/catch/finally` 핸들러는 프라미스가 처리되길 기다린다. 반면, 프라미스가 이미 처리상태라면 핸들러가 즉각 실행된다.

```javascript
// 아래 프라미스는 생성과 동시에 이행됨.
let promise = new Promise(resolve => resolve("완료!"));

promise.then(alert); // 완료! (바로 출력됨)
```

가수와 팬, 구독리스트 시나리오보다 프라미스가 더 복잡하다고 말한 이유가 바로 이런 기능 때문이다. 가수가 신곡을 발표한 이후에 구독 리스트에 이름을 올리는 팬은 신곡 발표 여부를 알 수 없다. 구독 리스트에 이름을 올리는 것이 선행되어야 새로운 소식을 받을 수 있기 때문이다.

그런데 프라미스는 핸들러를 언제든 추가할 수 있다는 점에서 구독리스트 시나리오보다 더 유연하다. 결과가 나와 있는 상태에서도 핸들러를 등록하면 결과를 바로 받을 수 있다.

이제 실제 동작하는 예시를 보며 프라미스로 어떻게 비동기 동작을 처리하는지 살펴보자.

### 예시: loadScript

이전 챕터에서 스크립트 로딩에 사용되는 함수 `loadScript`를 작성해 보았다.

복습 차원에서 콜백 기반으로 작성한 함수를 다시 살펴보자.

```javascript
function loadScript(src, callback) {
  let script = document.createElement('script');
  script.src = src;

  script.onload = () => callback(null, script);
  script.onerror = () => callback(new Error(`${src}를 불러오는 도중에 에러가 발생함`));

  document.head.append(script);
}
```

이제 프라미스를 이용해 함수를 다시 작성해 보자.

새로운 함수에선 콜백 함수 대신, 스크립트 로딩이 완전히 끝났을 때 이행되는 프라미스 객체를 만들고, 이를 반환해 보겠다. 외부 코드에선 `.then`을 이용해 핸들러(구독 함수)를 더하겠다.

```javascript
function loadScript(src) {
  return new Promise(function(resolve, reject) {
    let script = document.createElement('script');
    script.src = src;

    script.onload = () => resolve(script);
    script.onerror = () => reject(new Error(`${src}를 불러오는 도중에 에러가 발생함`));

    document.head.append(script);
  });
}
```

사용법은 다음과 같다.

```javascript
let promise = loadScript("https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.11/lodash.js");

promise.then(
  script => alert(`${script.src}을 불러왔습니다!`),
  error => alert(`Error: ${error.message}`)
);

promise.then(script => alert('또다른 핸들러...'));
```

프라미스를 사용한 코드가 콜백 기반 코드보다 더 나은 점을 정리하면 다음과 같다.

| 프라미스                                                     | 콜백                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 프라미스를 이용하면 흐름이 자연스럽다. `loadScript(script)`로 스크립트를 읽고, 결과에 따라 그다음(`.then`)에 무엇을 할지에 대한 코드를 작성하면 된다. | `loadScript(script, callback)`를 호출할 때, 함께 호출할 `callback` 함수가 준비되어 있어야 한다. `loadScript`를 호출하기 *이전*에 호출 결과로 무엇을 할지 미리 알고 있어야 한다. |
| 프라미스에 원하는 만큼 `.then`을 호출할 수 있다. `.then`을 호출하는 것은 새로운 ‘팬’(새로운 구독 함수)을 '구독 리스트’에 추가하는 것과 같다. | 콜백은 하나만 가능하다.                                      |

프라미스를 사용하면 흐름이 자연스럽고 유연한 코드를 작성할 수 있다.



## 프라미스 체이닝 - ⭐️⭐️⭐️

콜백챕터에서 언급한 문제를 다시 짚어보자. 스크립트를 불러오는 것과 같이 순차적으로 처리해야 하는 비동기 작업이 여러 개 있다고 가정해 보면 어떻게 해야 이런 상황을 코드로 풀어낼 수 있을까?

프라미스를 사용하면 여러 가지 해결책을 만들 수 있다.

이번 챕터에선 프라미스 체이닝(promise chaining)을 이용한 비동기 처리에 대해 다룬다.

프라미스 체이닝은 아래와 같이 생겼다.

```javascript
new Promise(function(resolve, reject) {

  setTimeout(() => resolve(1), 1000); // (*)

}).then(function(result) { // (**)

  alert(result); // 1
  return result * 2;

}).then(function(result) { // (***)

  alert(result); // 2
  return result * 2;

}).then(function(result) {

  alert(result); // 4
  return result * 2;

});
```

프라미스 체이닝은 `result`가 `.then` 핸들러의 체인(사슬)을 통해 전달된다는 점에서 착안한 아이디어이다.

위 예시는 아래와 같은 순서로 실행된다.

1. 1초 후 최초 프라미스가 이행된다. – `(*)`
2. 이후 첫번째 `.then` 핸들러가 호출된다. –`(**)`
3. 2에서 반환한 값은 다음 `.then` 핸들러에 전달된다. – `(***)`
4. 이런 과정이 계속 이어진다.

`result`가 핸들러 체인을 따라 전달되므로, `alert` 창엔 `1`, `2`, `4`가 순서대로 출력된다.

프라미스 체이닝이 가능한 이유는 `promise.then`을 호출하면 프라미스가 반환되기 때문이다. 반환된 프라미스엔 당연히 `.then`을 호출할 수 있다.

한편 핸들러가 값을 반환할 때엔 이 값이 프라미스의 `result`가 된다. 따라서 다음 `.then`은 이 값을 이용해 호출된다.

**초보자는 프라미스 하나에 `.then`을 여러 개 추가한 후, 이를 체이닝이라고 착각하는 경우가 있다. 하지만 이는 체이닝이 아니다.**

예시:

```javascript
let promise = new Promise(function(resolve, reject) {
  setTimeout(() => resolve(1), 1000);
});

promise.then(function(result) {
  alert(result); // 1
  return result * 2;
});

promise.then(function(result) {
  alert(result); // 1
  return result * 2;
});

promise.then(function(result) {
  alert(result); // 1
  return result * 2;
});
```

예시의 프라미스는 하나인데 여기에 등록된 핸들러는 여러 개 입니다. 이 핸들러들은 `result`를 순차적으로 전달하지 않고 독립적으로 처리한다.

그림으로 표현하면 다음과 같다. 프라미스 체이닝을 묘사한 위 그림과 비교해 보자.

![스크린샷 2022-10-29 20.27.43](https://raw.githubusercontent.com/back-seung/Today_I_Learned/master/uPic/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-10-29%2020.27.43.png)

동일한 프라미스에 등록된 `.then` 모두는 동일한 결과(프라미스의 `result`)를 받는다. 따라서 위 예시를 실행하면 얼럿 창엔 전부 `1`이 출력되는 것이다.

이런 식으로 한 프라미스에 여러 개의 핸들러를 등록해서 사용하는 경우는 거의 없다. 프라미스는 주로 체이닝을 해서 쓴다.

### 프라미스 반환하기

`.then(handler)`에 사용된 핸들러가 프라미스를 생성하거나 반환하는 경우도 있다.

이 경우 이어지는 핸들러는 프라미스가 처리될 때까지 기다리다가 처리가 완료되면 그 결과를 받는다.

예시:

```javascript
new Promise(function(resolve, reject) {

  setTimeout(() => resolve(1), 1000);

}).then(function(result) {

  alert(result); // 1

  return new Promise((resolve, reject) => { // (*)
    setTimeout(() => resolve(result * 2), 1000);
  });

}).then(function(result) { // (**)

  alert(result); // 2

  return new Promise((resolve, reject) => {
    setTimeout(() => resolve(result * 2), 1000);
  });

}).then(function(result) {

  alert(result); // 4

});
```

예시에서 첫 번째 `.then`은 `1`을 출력하고 `new Promise(…)`를 반환(`(*)`)한다.
1초 후 이 프라미스가 이행되고 그 결과(`resolve`의 인수인 `result * 2`)는 두 번째 `.then`으로 전달된다. 두 번째 핸들러(`(**)`)는 `2`를 출력하고 동일한 과정이 반복된다.

따라서 얼럿 창엔 이전 예시와 동일하게 1, 2, 4가 차례대로 출력된다. 다만 얼럿 창 사이에 1초의 딜레이가 생긴다.

이렇게 핸들러 안에서 프라미스를 반환하는 것도 비동기 작업 체이닝을 가능하게 해준다.

### loadScript 예시 개선하기

```javascript
loadScript("/article/promise-chaining/one.js")
  .then(function(script) {
    return loadScript("/article/promise-chaining/two.js");
  })
  .then(function(script) {
    return loadScript("/article/promise-chaining/three.js");
  })
  .then(function(script) {
    // 불러온 스크립트 안에 정의된 함수를 호출해
    // 실제로 스크립트들이 정상적으로 로드되었는지 확인한다.
    one();
    two();
    three();
  });
```

화살표 함수를 사용하면 다음과 같이 코드를 줄일수도 있다.

```javascript
loadScript("/article/promise-chaining/one.js")
  .then(script => loadScript("/article/promise-chaining/two.js"))
  .then(script => loadScript("/article/promise-chaining/three.js"))
  .then(script => {
    // 스크립트를 정상적으로 불러왔기 때문에 스크립트 내의 함수를 호출할 수 있다.
    one();
    two();
    three();
  });
```

`loadScript`를 호출할 때마다 프라미스가 반환되고 다음 `.then`은 이 프라미스가 이행되었을 때 실행된다. 이후에 다음 스크립트를 로딩하기 위한 초기화가 진행된다. 스크립트는 이런 과정을 거쳐 순차적으로 로드된다.

체인에 더 많은 비동기 동작을 추가할 수도 있는데, 추가 작업이 많아져도 코드가 오른쪽으로 길어지지 않고 아래로만 증가해서 '멸망’의 피라미드가 만들어지지 않는다.

한편, 아래와 같이 각 `loadScript`에 `.then`을 바로 붙일 수도 있다.

```javascript
loadScript("/article/promise-chaining/one.js").then(script1 => {
  loadScript("/article/promise-chaining/two.js").then(script2 => {
    loadScript("/article/promise-chaining/three.js").then(script3 => {
      // 여기서 script1, script2, script3에 정의된 함수를 사용할 수 있다.
      one();
      two();
      three();
    });
  });
});
```

이렇게 `.then`을 바로 붙여도 동일한 동작(스크립트 세 개를 순차적으로 불러오는 작업)을 수행한다. 하지만 코드가 ‘오른쪽으로’ 길어지는데, 콜백에서 언급한 문제와 동일한 문제가 발생하고 있다.

프라미스를 이제 막 배우기 시작해 체이닝에 대해 잘 모른다면 위와같이 코드를 작성할 수 있다. 그러나 대개 체이닝이 선호된다.

중첩 함수에서 외부 스코프에 접근할 수 있기 때문에 `.then`을 바로 쓰는 게 괜찮은 경우도 있다. 위 예제에서 가장 깊은 곳에 있는 중첩 콜백은 `script1`, `script2`, `script3` 안에 있는 변수 모두에 접근할 수 있다. 이런 예외 상황이 있다는 정도만 알아두자

**thenable**

핸들러는 프라미스가 아닌 `thenable`이라 불리는 객체를 반환하기도 한다. `.then`이라는 메서드를 가진 객체는 모두 `thenable`객체라고 부르는데, 이 객체는 프라미스와 같은 방식으로 처리된다.

‘thenable’ 객체에 대한 아이디어는 서드파티 라이브러리가 ‘프라미스와 호환 가능한’ 자체 객체를 구현할 수 있다는 점에서 나왔다. 이 객체들엔 자체 확장 메서드가 구현되어 있겠지만 `.then`이 있기 때문에 네이티브 프라미스와도 호환 가능하다.

아래는 thenable 객체 예시이다.

```javascript
class Thenable {
  constructor(num) {
    this.num = num;
  }
  then(resolve, reject) {
    alert(resolve); // function() { 네이티브 코드 }
    // 1초 후 this.num*2와 함께 이행됨
    setTimeout(() => resolve(this.num * 2), 1000); // (**)
  }
}

new Promise(resolve => resolve(1))
  .then(result => {
    return new Thenable(result); // (*)
  })
  .then(alert); // 1000밀리 초 후 2를 보여줌
```

1. 자바스크립트는 `(*)`로 표시한 줄에서 `.then` 핸들러가 반환한 객체를 확인한다.
2. 이 객체에 호출 가능한 메서드 `then`이 있으면 `then`이 호출된다.
3.  `then`은 `resolve`와 `reject`라는 네이티브 함수를 인수로 받고(executor과 유사함), 둘 중 하나가 호출될 때까지 기다린다.
4. 위 예시에서 `resolve(2)`는 1초 후에 호출된다(`(**)`).
5. 호출 후 결과는 체인을 따라 아래로 전달된다.

이런 식으로 구현하면 `Promise`를 상속받지 않고도 커스텀 객체를 사용해 프라미스 체이닝을 만들 수 있다.

### fetch와 체이닝 함께 응용하기

프론트 단에선, 네트워크 요청 시 프라미스를 자주 사용한다. 이에 관련된 예시를 살펴보자.

예시에선 메서드 [fetch](https://ko.javascript.info/fetch)를 사용해 원격 서버에서 사용자 정보를 가져온다. `fetch`엔 다양한 선택 매개변수가 있다(자세한 내용은 [별도의 챕터](https://ko.javascript.info/fetch)에서 확인)

```javascript
let promise = fetch(url);
```

위 코드를 실행하면 `url`에 네트워크 요청을 보내고 프라미스를 반환한다. 원격 서버가 헤더와 함께 응답을 보내면, 프라미스는 `response` 객체와 함께 이행된다. 그런데 이때 *response 전체가 완전히 다운로드되기 전*에 프라미스는 이행 상태가 되어버린다.

응답이 완전히 종료되고, 응답 전체를 읽으려면 메서드 `response.text()`를 호출해야 한다. `response.text()`는 원격 서버에서 전송한 텍스트 전체가 다운로드되면, 이 텍스트를 `result` 값으로 갖는 이행된 프라미스를 반환한다.

아래 코드를 실행하면 `user.json`에 요청이 가고 서버에서 해당 텍스트를 불러온다.

```javascript
fetch('/article/promise-chaining/user.json')
  // 원격 서버가 응답하면 .then 아래 코드가 실행된다.
  .then(function(response) {
    // response.text()는 응답 텍스트 전체가 다운로드되면
    // 응답 텍스트를 새로운 이행 프라미스를 만들고, 이를 반환한다.
    return response.text();
  })
  .then(function(text) {
    // 원격에서 받아온 파일의 내용
    alert(text); // {"name": "Violet-Bora-Lee", "isAdmin": true}
  });
```

그런데 메서드 `response.json()` 를 쓰면 원격에서 받아온 데이터를 읽고 JSON으로도 파싱할 수 있다. 예시엔 이 메서드가 더 적합하므로 기존에 작성한 코드를 약간 변경하는데, 화살표 함수도 함께 써서 코드를 간결하게 해보겠다.

```javascript
// 위 코드와 동일한 기능을 하지만, response.json()은 원격 서버에서 불러온 내용을 JSON으로 변경해준다.
fetch('/article/promise-chaining/user.json')
  .then(response => response.json())
  .then(user => alert(user.name)); // Violet-Bora-Lee, 이름만 성공적으로 가져옴
```

불러온 사용자 정보를 가지고 무언가를 더 해보겠다.

GitHub에 요청을 보내 사용자 프로필을 불러오고 아바타를 출력해 보는 것같이 말이다.

```javascript
// user.json에 요청을 보낸다.
fetch('/article/promise-chaining/user.json')
  // 응답받은 내용을 json으로 불러온다.
  .then(response => response.json())
  // GitHub에 요청을 보낸다.
  .then(user => fetch(`https://api.github.com/users/${user.name}`))
  // 응답받은 내용을 json 형태로 불러온다.
  .then(response => response.json())
  // 3초간 아바타 이미지(githubUser.avatar_url)를 보여준다.
  .then(githubUser => {
    let img = document.createElement('img');
    img.src = githubUser.avatar_url;
    img.className = "promise-avatar-example";
    document.body.append(img);

    setTimeout(() => img.remove(), 3000); // (*)
  });
```

코드는 주석에 적은 대로 잘 동작한다. 그런데 위 코드엔 프라미스를 다루는데 서툰 개발자가 자주 저지르는 잠재적 문제가 내재돼 있다.

`(*)`로 표시한 줄을 보자. 만약 아바타가 잠깐 보였다가 사라진 *이후에* 무언가를 하고 싶으면 어떻게 해야 할까? 사용자 정보를 수정할 수 있게 해주는 폼을 보여주는 것 같은 작업을 추가하는 경우같이 말이다. 지금으로선 방법이 없다.

체인을 확장할 수 있도록 만들려면 아바타가 사라질 때 이행 프라미스를 반환해 줘야 한다.

- 체인 확장을 위한 이행 프라미스 반환

```javascript
fetch('/article/promise-chaining/user.json')
  .then(response => response.json())
  .then(user => fetch(`https://api.github.com/users/${user.name}`))
  .then(response => response.json())
  .then(githubUser => new Promise(function(resolve, reject) { // (*)
    let img = document.createElement('img');
    img.src = githubUser.avatar_url;
    img.className = "promise-avatar-example";
    document.body.append(img);

    setTimeout(() => {
      img.remove();
      resolve(githubUser); // (**)
    }, 3000);
  }))
  // 3초 후 동작함
  .then(githubUser => alert(`${githubUser.name}의 이미지를 성공적으로 출력하였습니다.`));
```

`(*)`로 표시한 곳의 `.then` 핸들러는 이제 `setTimeout`안의 `resolve(githubUser)`를 호출했을 때(`(**)`) 만 처리상태가 되는 `new Promise`를 반환한다. 체인의 다음 `.then`은 이를 기다린다.

비동기 동작은 항상 프라미스를 반환하도록 하는 것이 좋다. 지금은 체인을 확장할 계획이 없더라도 이렇게 구현해 놓으면 나중에 체인 확장이 필요한 경우 손쉽게 체인을 확장할 수 있다.

이제 코드를 재사용 가능한 함수 단위로 분리해 마무리하자.

```javascript
function loadJson(url) {
  return fetch(url)
    .then(response => response.json());
}

function loadGithubUser(name) {
  return fetch(`https://api.github.com/users/${name}`)
    .then(response => response.json());
}

function showAvatar(githubUser) {
  return new Promise(function(resolve, reject) {
    let img = document.createElement('img');
    img.src = githubUser.avatar_url;
    img.className = "promise-avatar-example";
    document.body.append(img);

    setTimeout(() => {
      img.remove();
      resolve(githubUser);
    }, 3000);
  });
}

// 함수를 이용하여 다시 동일 작업 수행
loadJson('/article/promise-chaining/user.json')
  .then(user => loadGithubUser(user.name))
  .then(showAvatar)
  .then(githubUser => alert(`Finished showing ${githubUser.name}`));
  // ...
```

### 요약

`.then` 또는 `.catch`, `.finally`의 핸들러(어떤 경우도 상관없음)가 프라미스를 반환하면, 나머지 체인은 프라미스가 처리될 때까지 대기한다. 처리가 완료되면 프라미스의 `result`(값 또는 에러)가 다음 체인으로 전달된다.

이를 그림으로 나타내면 아래와 같다.![스크린샷 2022-10-29 21.05.55](https://raw.githubusercontent.com/back-seung/Today_I_Learned/master/uPic/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-10-29%2021.05.55.png)



## 프라미스와 에러 핸들링 - ⭐️⭐️

프라미스가 거부되면 제어 흐름이 제일 가까운 rejection 핸들러로 넘어가기 때문에 프라미스 체인을사용하면 에러를 쉽게 처리할 수 있다. 이는 실무에서 아주 유용한 기능이다.

존재하지 않는 주소를 fetch에 넘겨주는 예시를 살펴보자. `.catch`에서 에러를 처리한다. 

```javascript
fetch('https://no-such-server.blabla') // 거부
.then(response => response.json())
.catch(err => alert(err)) // TypeError : failed to fetch(출력되는 내용은 다를 수 있음)
```

예시에서 보이듯, `.catch`는 첫번째 핸들러일 필요가 없고 하나 혹은 여러 개의 .then 뒤에 올 수 있다.

이번엔 사이트에는 아무런 문제가 없지만 응답으로 받은 JSON의 형식이 잘못된 경우를 살펴보자. 가장 쉬운 에러 처리 방법은 체인 끝에 `.catch`를 붙이는 것이다.

```javascript
fetch('/article/promise-chaining/user.json')
.then(response => response.json())
.then(user => fetch(`https://api.github.com/users/${user.name}`))
.then(response => response.json())
.then(githubUser => new Promise((resolve, reject) => {
  let img = document.createElement('img');
  img.src = githubUser.avatar_url;
  img.className = "promise-avatar-example";
  document.body.append(img);
  
  setTimeout(() => {
    img.remove();
    resolve(githubUser);
  }, 3000);
}))
.catch(error => alert(error.message));
```

정상적인 경우라면 .catch는 절대 트리거 되지 않는다. 그런데 네트워크 문제, 잘못된 형식의 JSON 등으로 인해 위쪽 프라미스 중 하나라도 거부되면 .catch에서 에러를 잡게 된다.

### 암시적 try..catch

프라미스 executor와 프라미스 핸들러 코드 주위엔 보이지 않는 암시적 try...catch가 존재한다. 예외가 발생하면 암시적 try..catch는 예외를 잡고 이를 reject처럼 다룬다.

- 예시

```javascript
new Promise((resolve, reject) => {
  throw new Error("에러 발생!");
}).catch(alert); // Error: 에러 발생!
```

위 예시는 아래 예시와 똑같이 동작한다.

```javascript
new Promise((resolve, reject) => {
  reject(new Error("에러 발생"));
}).catch(alert); // Error: 에러 발생
```

throw문이 만든 에러뿐만 아니라 모든 종류의 에러가 암시적 try..catch에서 처리된다. 암시적 try..catch가 프로그래밍 에러를 어떻게 처리하는지 살펴보자.

```javascript
new Promise((resolve, reject) => {
  resolve("OK");
}).then((result) => {
  blabla(); // 존재하지 않는 함수
}).catch(alert); // ReferenceError: blabla is not defined
```

마지막 `.catch`는 명시적 거부뿐만 아니라 핸들러 위쪽에서 발생한 비정상 에러 또한 잡는다. 

### 다시 던지기

체인 마지막의 `.catch`는 try..catch와 유사한 역할을 한다. `.then` 핸들러를 원하는 만큼 사용하다 마지막에 `.catch`하나만 붙이면 `.then`핸들러에서 발생한 모든 에러를 처리할 수 있다.

일반 try..catch에선 에러를 분석하고 처리할 수 없는 에러라 판단되면 에러를 다시 던질 때가 있다. 프라미스에도 유사한 일을 할 수 있다.

`.catch`안에서 `throw`를 사용하면 제어 흐름이 가장 가까운 곳에 있는 에러 핸들러로 넘어간다. 여기서 에러가 성공적으로 처리되면 가장 가까운 곳에 있는 `.then` 핸들러로 제어 흐름이 넘어가 실행이 이어진다.

아래 예시의 .catch는 에러를 성공적으로 처리한다. 

```javascript
// 실행 순서: catch -> then
new Promise((resolve, reject) => {
  throw new Error("에러 발생!");
}).catch(function(error) {
  alert("에러가 잘 처리되었습니다. 정상적으로 실행이 이어진다.");
}).then(() => alert("다음 핸들러가 실행된다."));
```

`.catch` 블록이 정상적으로 종료되었기 때문에 다음 성공 핸들러 `.then`이 호출된 것을 확인할 수가 있다.

`.catch`를 활용한 또 다른 사례를 살펴보자. (*)로 표시한 핸들러에서 에러를 잡는데 여기서는 에러를 처리하지 못하기 때문에 (URIError처리 방법만 알고 있음) 에러를 다시 던진다.

```javascript
// 실행 순서: catch -> catch
new Promise((resolve, reject) => {

  throw new Error("에러 발생!");

}).catch(function(error) { // (*)

  if (error instanceof URIError) {
    // 에러 처리
  } else {
    alert("처리할 수 없는 에러");

    throw error; // 에러 다시 던지기
  }

}).then(function() {
  /* 여기는 실행되지 않습니다. */
}).catch(error => { // (**)

  alert(`알 수 없는 에러가 발생함: ${error}`);
  // 반환값이 없음 => 실행이 계속됨

});
```

실행 흐름이 첫 번째 `.catch (*)`로 넘어갔다가 다음 `.catch (**)`로 이어지는 것을 확인할 수 있다.

### 처리되지 못한 거부

에러를 처리하지 못하면 무슨일이 생길까? 아래 예시처럼 체인 끝에 `.catch`를 추가하지 못하는 경우처럼 말이다.

```javascript
new Promise(function() {
  snoSuchFunction(); // 존재하지 않는 함수를 호출하기 때문에 에러가 발생함
}).then(() => {
  // 성공 상태의 프라미스를 처리하는 핸들러. 한 개 혹은 여러 개가 있을 수 있음
}); // 끝에 .catch가 존재하지 않음
```

에러가 발생하면 프라미스는 거부 상태가 되고 실행 흐름은 가장 가까운  rejection 핸들러로 넘어간다. 그런데 위 예시에서는 처리해 줄 핸들러가 없어서 에러가 갇혀버린다. 에러를 처리할 코드가 없기 때문이다. 

이런 식으로 코드에 처리하지 못한 에러가 남게 되면 실무에서는 끔찍한 일이 발생한다. 

일반적인 에러가 발생하고 이를 try..catch에서 처리하지 못하는 경우를 생각해보자. 스크립트가 죽고 콘솔 창에 메시지가 출력되겠다. 거부된 프라미스를 처리하지 못했을 때도 유사한 일이 발생한다.

자바스크립트 엔진은 프라미스 거부를 추적하다가 위와 같은 상황이 발생하면 전역 에러를 생성한다. 콘솔창을 열고 위 예시를 실행하면 전역 에러를 확인할 수 있다.

브라우저 환경에선 이런 에러를  unhandledrejection 이벤트로 처리할 수 있다.

```javascript
window.addEventListener('unhandledrejection', function(event) {
  // unhandledrejection 이벤트엔 두 개의 특수 프로퍼티가 있다.
  alert(event.promise); // [object Promise] - 에러를 생성하는 프라미스
  alert(event.reason); // Error: 에러 발생! - 처리하지 못한 에러 객체
});

new Promise(function() {
  throw new Error("에러 발생!");
}); // 에러를 처리할 수 있는 .catch 핸들러가 없음
```

 `unhandledrejection` 이벤트는 [HTML 명세서](https://html.spec.whatwg.org/multipage/webappapis.html#unhandled-promise-rejections)에 정의된 표준 이벤트이다.

브라우저 환경에선 에러가 발생했는데 `.catch`가 없으면 `unhandledrejection` 핸들러가 트리거 된다. `unhandledrejection` 핸들러는 에러 정보가 담긴 `event` 객체를 받기 때문에 이 핸들러 안에서 원하는 작업을 할 수 있다.

대개 이런 에러는 회복할 수 없기 때문에 개발자로서 할 수 있는 최선의 방법은 사용자에게 문제 상황을 알리고 가능하다면 서버에 에러 정보를 보내는 것이다.

Node.js같은 기타 호스트 환경에도 처리하지 못한 에러를 다루는 방법을 여러 가지 제공한다.



### 요약

- `.catch`는 프라미스에서 발생한 모든 에러를 다룬다. reject()가 호출되거나 에러가 던져지면 `.catch`에서 이를 처리한다.
- `.catch`는 에러를 처리하고 싶은 지점에 정확히 위치시켜야 한다. 물론 어떻게 에러를 처리할지 알고 있어야 한다. 핸들러에선 에러를 분석하고(커스텀 에러 클래스가 이 때 도움이 됨) 알 수 없는 에러(프로그래밍 실수일 확률이 높음)는 다시 던질 수 있다.
- 에러 발생시 회복할 방법이 없다면 `.catch`를 사용하지 않아도 괜찮다.
- `unhandledrejection`이벤트 핸들러를 사용해 처리되지 않은 에러를 추적하고 이를 사용자 (혹은 서버)에게 알려서 애플리케이션이 아무런 설명도 없이 그냥 죽는걸 방지하자. 브라우저 환경에선 예방에 unhandledrejection을 다른 환경에서는 유사한 핸들러를 사용할 수 있다.



## 프라미스 API - ⭐️⭐️

Promise 클래스에는 5가지 정적 메서드가 있다. 이번 챕터에선 다섯 메서드의 유스 케이스에 대해서 빠르게 알아본다.

### Promise.all

여러 개의 프라미스를 동시에 실행시키고 모든 프라미스가 준비될 때까지 기다린다고 가정해보자.

복수의 URL에 동시에 요청을 보내고 다운로드가 모두 완료된 후에 콘텐츠를 처리할 때 이런 상황이 발생한다.

`Promise.all`은 이럴 때 사용할 수 있다.

- 문법

```javascript
let promise = Promise.all([...promises...]);
```

`Promise.all`은 요소 전체가 프라미스인 배열(엄밀히 따지면 이터러블 객체이지만, 대개는 배열임)을 받고 새로운 프라미스를 반환한다.

배열 안 프라미스가 모두 처리되면 새로운 프라미스가 이행되는데, 배열 안 프라미스의 결괏값을 담은 배열이 새로운 프라미스의 `result`가 된다.

아래 `Promise.all`은 3초 후에 처리되고, 반환되는 프라미스의 `result`는 배열 `[1, 2, 3]`이 된다.

```javascript
Promise.all([
  new Promise(resolve => setTimeout(() => resolve(1), 3000)), // 1
  new Promise(resolve => setTimeout(() => resolve(2), 2000)), // 2
  new Promise(resolve => setTimeout(() => resolve(3), 1000))  // 3
]).then(alert); // 프라미스 전체가 처리되면 1, 2, 3이 반환된다. 각 프라미스는 배열을 구성하는 요소가 된다.
```

배열 `result`의 요소 순서는 `Promise.all`에 전달되는 프라미스 순서와 상응한다는 점에 주목하자.

Promise.all`의 첫 번째 프라미스는 가장 늦게 이행되더라도 처리 결과는 배열의 첫 번째 요소에 저장된다.

작업해야 할 데이터가 담긴 배열을 프라미스 배열로 매핑하고, 이 배열을 `Promise.all`로 감싸는 트릭은 자주 사용된다.

- URL이 담긴 배열을 `fetch`를 써서 처리하는 예시

```javascript
let urls = [
  'https://api.github.com/users/iliakan',
  'https://api.github.com/users/Violet-Bora-Lee',
  'https://api.github.com/users/jeresig'
];

// fetch를 사용해 url을 프라미스로 매핑한다.
let requests = urls.map(url => fetch(url));

// Promise.all은 모든 작업이 이행될 때까지 기다린다.
Promise.all(requests)
  .then(responses => responses.forEach(
    response => alert(`${response.url}: ${response.status}`)
  ));
```

GitHub 유저네임이 담긴 배열을 사용해 사용자 정보를 가져오는 예시를 살펴보자(실무에서 id를 기준으로 장바구니 목록을 불러올 때도 같은 로직을 사용할 수 있다).

```javascript
let names = ['iliakan', 'Violet-Bora-Lee', 'jeresig'];

let requests = names.map(name => fetch(`https://api.github.com/users/${name}`));

Promise.all(requests)
  .then(responses => {
    // 모든 응답이 성공적으로 이행되었다.
    for(let response of responses) {
      alert(`${response.url}: ${response.status}`); // 모든 url의 응답코드가 200.
    }

    return responses;
  })
  // 응답 메시지가 담긴 배열을 response.json()로 매핑해, 내용을 읽는다.
  .then(responses => Promise.all(responses.map(r => r.json())))
  // JSON 형태의 응답 메시지는 파싱 되어 배열 'users'에 저장된다.
  .then(users => users.forEach(user => alert(user.name)));
```

**`Promise.all`에 전달되는 프라미스 중 하나라도 거부되면, `Promise.all`이 반환하는 프라미스는 에러와 함께 바로 거부된다.**

- 예시

```javascript
Promise.all([
  new Promise((resolve, reject) => setTimeout(() => resolve(1), 1000)),
  new Promise((resolve, reject) => setTimeout(() => reject(new Error("에러 발생!")), 2000)),
  new Promise((resolve, reject) => setTimeout(() => resolve(3), 3000))
]).catch(alert); // Error: 에러 발생!
```

2초 후 두 번째 프라미스가 거부되면서 `Promise.all` 전체가 거부되고, `.catch`가 실행된다. 거부 에러는 `Promise.all` 전체의 결과가 된다.

**에러가 발생하면 다른 프라미스는 무시된다.**

프라미스가 하나라도 거부되면 `Promise.all`은 즉시 거부되고 배열에 저장된 다른 프라미스의 결과는 완전히 무시된다. 이행된 프라미스의 결과도 무시된다.

`fetch`를 사용해 호출 여러 개를 만들면, 그중 하나가 실패하더라도 호출은 계속 일어난다. 그렇더라도 `Promise.all`은 다른 호출을 더는 신경 쓰지 않는다. 프라미스가 처리되긴 하겠지만 그 결과는 무시된다.

프라미스에는 '취소’라는 개념이 없어서 `Promise.all`도 프라미스를 취소하지 않는다. [또 다른 챕터](https://ko.javascript.info/fetch-abort)에서 배울 `AbortController`를 사용하면 프라미스 취소가 가능하긴 하지만, `AbortController`는 프라미스 API는 아니다.

**`이터러블 객체`가 아닌 ‘일반’ 값도 `Promise.all(iterable)`에 넘길 수 있다.**

`Promise.all(...)`은 대개 프라미스가 요소인 이러터블 객체(대부분 배열)를 받는다. 그런데 요소가 프라미스가 아닌 객체일 경우엔 요소 ‘그대로’ 결과 배열에 전달된다.

아래 예시의 결과는 `[1, 2, 3]`이다.

```javascript
Promise.all([
  new Promise((resolve, reject) => {
    setTimeout(() => resolve(1), 1000)
  }),
  2,
  3
]).then(alert); // 1, 2, 3
```

이미 결과를 알고 있는 값은 이 특징을 이용해 `Promise.all`에 그냥 전달하면 된다.

### Promise.allSettled

**최근에 추가됨**

스펙에 추가된 지 얼마 안 된 문법이다. 구식 브라우저는 폴리필이 필요하다.

`Promise.all`은 프라미스가 하나라도 거절되면 전체를 거절한다. 따라서, 프라미스 결과가 *모두* 필요할 때같이 ‘모 아니면 도’ 일 때 유용하다.

```javascript
Promise.all([
  fetch('/template.html'),
  fetch('/style.css'),
  fetch('/data.json')
]).then(render); // render 메서드는 fetch 결과 전부가 있어야 제대로 동작한다.
```

반면, `Promise.allSettled`는 모든 프라미스가 처리될 때까지 기다린다. 반환되는 배열은 다음과 같은 요소를 갖는다.

* 응답이 성공할 경우 – `{status:"fulfilled", value:result}`
* 에러가 발생한 경우 – `{status:"rejected", reason:error}`

`fetch`를 사용해 여러 사람의 정보를 가져오고 있다고 가정해보자. 여러 요청 중 하나가 실패해도 다른 요청 결과는 여전히 필요하다.

이럴 때 `Promise.allSettled`를 사용할 수 있다.

```javascript
let urls = [
  'https://api.github.com/users/iliakan',
  'https://api.github.com/users/Violet-Bora-Lee',
  'https://no-such-url'
];

Promise.allSettled(urls.map(url => fetch(url)))
  .then(results => { // (*)
    results.forEach((result, num) => {
      if (result.status == "fulfilled") {
        alert(`${urls[num]}: ${result.value.status}`);
      }
      if (result.status == "rejected") {
        alert(`${urls[num]}: ${result.reason}`);
      }
    });
  });
```

`(*)`로 표시한 줄의 `results`는 다음과 같을 것이다.

```javascript
[
  {status: 'fulfilled', value: ...응답...},
  {status: 'fulfilled', value: ...응답...},
  {status: 'rejected', reason: ...에러 객체...}
]
```

`Promise.allSettled`를 사용하면 이처럼 각 프라미스의 상태와 `값 또는 에러`를 받을 수 있다.

### 폴리필

브라우저가 `Promise.allSettled`를 지원하지 않는다면 폴리필을 구현하면 된다.

```javascript
if(!Promise.allSettled) {
  Promise.allSettled = function(promises) {
    return Promise.all(promises.map(p => Promise.resolve(p).then(value => ({
      status: 'fulfilled',
      value
    }), reason => ({
      status: 'rejected',
      reason
    }))));
  };
}
```

여기서 `promises.map`은 입력값을 받아 `p => Promise.resolve(p)`로 입력값을 프라미스로 변화시킨다(프라미스가 아닌 값을 받은 경우). 그리고 모든 프라미스에 `.then` 핸들러가 추가된다.

`then` 핸들러는 성공한 프라미스의 결괏값 `value`를 `{status:'fulfilled', value}`로, 실패한 프라미스의 결괏값 `reason`을 `{status:'rejected', reason}`으로 변경한다. `Promise.allSettled`의 구성과 동일하게 말이다.

이렇게 폴리필을 구현하면 프라미스 일부가 거부되더라도 `Promise.allSettled`를 사용해 프라미스 *전체*의 결과를 얻을 수 있다.

### Promise.race

`Promise.race`는 `Promise.all`과 비슷하다. 다만 가장 먼저 처리되는 프라미스의 결과(혹은 에러)를 반환한다.

- 문법

```javascript
let promise = Promise.race(iterable);
```

아래 예시의 결과는 `1`이다.

```javascript
Promise.race([
  new Promise((resolve, reject) => setTimeout(() => resolve(1), 1000)),
  new Promise((resolve, reject) => setTimeout(() => reject(new Error("에러 발생!")), 2000)),
  new Promise((resolve, reject) => setTimeout(() => resolve(3), 3000))
]).then(alert); // 1
```

첫 번째 프라미스가 가장 빨리 처리상태가 되기 때문에 첫 번째 프라미스의 결과가 result 값이 된다. 이렇게 `Promise.race`를 사용하면 '경주(race)의 승자’가 나타난 순간 다른 프라미스의 결과 또는 에러는 무시된다.

### Promise.resolve와 Promise.reject

프라미스 메서드 `Promise.resolve`와 `Promise.reject`는 `async/await` 문법([뒤에서](https://ko.javascript.info/async-await) 다룸)이 생긴 후로 쓸모없어졌기 때문에 근래에는 거의 사용하지 않는다.

### Promise.resolve

`Promise.resolve(value)`는 **결괏값이 `value`인 이행 상태 프라미스를 생성한다.**

아래 코드와 동일한 일을 수행한다.

```javascript
let promise = new Promise(resolve => resolve(value));
```

`Promise.resolve`는 호환성을 위해 함수가 프라미스를 반환하도록 해야 할 때 사용할 수 있다.

아래 함수 `loadCached`는 인수로 받은 URL을 대상으로 `fetch`를 호출하고, 그 결과를 기억(cache)한다. 나중에 동일한 URL을 대상으로 `fetch`를 호출하면 캐시에서 호출 결과를 즉시 가져오는데, 이때 `Promise.resolve`를 사용해 캐시 된 내용을 프라미스로 만들어 반환 값이 항상 프라미스가 되게 한다.

```javascript
let cache = new Map();

function loadCached(url) {
  if (cache.has(url)) {
    return Promise.resolve(cache.get(url)); // (*)
  }

  return fetch(url)
    .then(response => response.text())
    .then(text => {
      cache.set(url,text);
      return text;
    });
}
```

`loadCached`를 호출하면 프라미스가 반환된다는 것이 보장되기 때문에 `loadCached(url).then(…)`을 사용할 수 있다. `loadCached` 뒤에 언제나 `.then`을 쓸 수 있게 된다. `(*)`로 표시한 줄에서 `Promise.resolve`를 사용한 이유가 바로 여기에 있다.

### Promise.reject

`Promise.reject(error)`는 결괏값이 `error`인 거부 상태 프라미스를 생성한다.

아래 코드와 동일한 일을 수행한다.

```javascript
let promise = new Promise((resolve, reject) => reject(error));
```

실무에서 이 메서드를 쓸 일은 거의 없다.

### 요약

`Promise` 클래스에는 5가지 정적 메서드가 있다.

1. `Promise.all(promises)` – 모든 프라미스가 이행될 때까지 기다렸다가 그 결괏값을 담은 배열을 반환한다. 주어진 프라미스 중 하나라도 실패하면 `Promise.all`는 거부되고, 나머지 프라미스의 결과는 무시된다.

2. ```javascript
   Promise.allSettled(promises)
   ```

    최근에 추가된 메서드로 모든 프라미스가 처리될 때까지 기다렸다가 그 결과(객체)를 담은 배열을 반환한다. 객체엔 다음과 같은 정보가 담긴다.

   * `status`: `"fulfilled"` 또는 `"rejected"`
   * `value`(프라미스가 성공한 경우) 또는 `reason`(프라미스가 실패한 경우)

3. `Promise.race(promises)` – 가장 먼저 처리된 프라미스의 결과 또는 에러를 담은 프라미스를 반환한다.

4. `Promise.resolve(value)` – 주어진 값을 사용해 이행 상태의 프라미스를 만든다.

5. `Promise.reject(error)` – 주어진 에러를 사용해 거부 상태의 프라미스를 만든다.

실무에선 다섯 메서드 중 `Promise.all`을 가장 많이 사용한다



## 프라미스화 - ⭐️

콜백을 받는 함수를 프라미스를 반환하는 함수로 바꾸는 것을 '프라미스화(promisification)'라고 한다.

기능을 구현 하다 보면 콜백보다는 프라미스가 더 편리하기 때문에 콜백 기반 함수와 라이브러리를 프라미스를 반환하는 함수로 바꾸는 게 좋은 경우가 종종 생길 것이다.

[콜백](https://ko.javascript.info/callbacks) 챕터에서 사용했던 `loadScript(src, callback)` 예시를 사용해 프라미스화에 대해 좀 더 자세히 알아보자.

```javascript
function loadScript(src, callback) {
  let script = document.createElement('script');
  script.src = src;

  script.onload = () => callback(null, script);
  script.onerror = () => callback(new Error(`${src}를 불러오는 도중에 에러가 발생함`));

  document.head.append(script);
}

// 사용법:
// loadScript('path/script.js', (err, script) => {...})
```

`loadScript(src, callback)`를 이제 프라미스화해보자. 새로운 함수 `loadScriptPromise(src)`는 `loadScript`와 동일하게 동작하지만 `callback`을 제외한 `src`만 인수로 받아야 하고, 프라미스를 반환해야 한다.

```javascript
let loadScriptPromise = function(src) {
  return new Promise((resolve, reject) => {
    loadScript(src, (err, script) => {
      if (err) reject(err)
      else resolve(script);
    });
  })
}

// 사용법:
// loadScriptPromise('path/script.js').then(...)
```

새롭게 구현한 `loadScriptPromise`는 프라미스 기반 코드와 잘 융화된다.

예시에서 볼 수 있듯, `loadScriptPromise`는 기존 함수 `loadScript`에 모든 일을 위임한다. `loadScript`의 콜백은 스크립트 로딩 상태에 따라 `이행` 혹은 `거부`상태의 프라미스를 반환한다.

그런데 실무에선 함수 하나가 아닌 여러 개의 함수를 프라미스화 해야 할 것이다. 헬퍼 함수를 만드는 게 좋을 것 같다. 프라미스화를 적용 할 함수 `f`를 받고 래퍼 함수를 반환하는 함수 `promisify(f)`를 만들어보자

`promisify(f)`가 반환하는 래퍼 함수는 위 예시와 동일하게 동작할 것이다. 프라미스를 반환하고 호출을 기존 함수 `f`에 전달하여 커스텀 콜백 내의 결과를 추적해야 한다.

```javascript
function promisify(f) {
  return function (...args) { // 래퍼 함수를 반환함
    return new Promise((resolve, reject) => {
      function callback(err, result) { // f에 사용할 커스텀 콜백
        if (err) {
          reject(err);
        } else {
          resolve(result);
        }
      }

      args.push(callback); // 위에서 만든 커스텀 콜백을 함수 f의 인수 끝에 추가한다.

      f.call(this, ...args); // 기존 함수를 호출한다.
    });
  };
};

// 사용법:
let loadScriptPromise = promisify(loadScript);
loadScriptPromise(...).then(...);
```

위 예시는 프라미스화 할 함수가 인수가 두 개(`(err, result)`)인 콜백을 받을 것이라 가정하고 작성되었다. 거의 이런 상황일 것이고, 커스텀 콜백은 이 상황에 딱 들어맞는다. `promisify`가 확실히 동작하게 될 것이다.

그런데 함수 `f`가 두 개를 초과하는 인수를 가진 콜백, `callback(err, res1, res2, ...)`을 받는다면 어떤 일이 발생할까?

이런 경우를 대비하여 좀 더 진화한 헬퍼 함수, `promisify`를 만들어 보자. 새롭게 만든 함수를 `promisify(f, true)`형태로 호출하면, 프라미스 결과는 콜백의 성공 케이스(`results`)를 담은 배열, `[res1, res2, ...]`이 된다.

```javascript
// 콜백의 성공 결과를 담은 배열을 얻게 해주는 promisify(f, true)
function promisify(f, manyArgs = false) {
  return function (...args) {
    return new Promise((resolve, reject) => {
      function callback(err, ...results) { // f에 사용할 커스텀 콜백
        if (err) {
          reject(err);
        } else {
          // manyArgs가 구체적으로 명시되었다면, 콜백의 성공 케이스와 함께 이행 상태가 된다.
          resolve(manyArgs ? results : results[0]);
        }
      }

      args.push(callback);

      f.call(this, ...args);
    });
  };
};

// 사용법:
f = promisify(f, true);
f(...).then(arrayOfResults => ..., err => ...)
```

`callback(result)`같이 `err`이 없는 형태나 지금까지 언급하지 않은 형태의 이색적인 콜백도 있을 수 있는데, 이런 경우엔 헬퍼 함수를 사용하지 않고 직접 프라미스화 하면 된다.

본 챕터에서 설명한 헬퍼 함수보다 더 유용한 형태의 프라미스화를 도와주는 함수를 제공하는 모듈도 많다. [es6-promisify](https://github.com/digitaldesignlabs/es6-promisify)가 대표적인 예시이다. Node.js에선 내장 함수 `util.promisify`를 사용해 프라미스화를 할 수 있다.

프라미스화는 곧 배우게 될 `async/await`와 함께 사용하면 더 좋다. 다만, 콜백을 완전히 대체하지는 못한다는 사실을 기억해 두길 바란다.

프라미스는 하나의 결과만 가질 수 있지만, 콜백은 여러 번 호출할 수 있기 때문이다.

따라서 프라미스화는 콜백을 단 한 번 호출하는 함수에만 적용하길 바란다. 프라미스화한 함수의 콜백을 여러 번 호출해도, 두 번째부터는 무시된다.



## 마이크로태스크 - ⭐️⭐️

프라미스 핸들러 `.then/catch/finally`는 항상 *비동기적*으로 실행된다.

프라미스가 즉시 이행되더라도 `.then/catch/finally` 아래에 있는 코드는 이 핸들러들이 실행되기 전에 실행된다.

```javascript
let promise = Promise.resolve();

promise.then(() => alert("프라미스 성공"));
alert("코드 종료"); // 제일 먼저 뜬다
```

위 예시를 실행해보면 프라미스가 즉시 이행되더라도  `"코드 종료"`가 가장 먼저 출력되고 `"프라미스 성공"`이 나중에 출력되는 것을 확인할 수 있다.

왜일까?

### 마이크로태스크 큐(Queue)

비동기 작업을 처리하려면 적절한 관리가 필요하다. 이를 위해 ECMA에서는 PromiseJobs라는 내부 큐(Internal Queue)를 명시한다. V8 엔진에서는 이를 마이크로태스크 큐라고 부르기 때문에 이 용어가 좀 더 선호된다.

명세서의 설명은 아래와 같다.

- 마이크로태스크 큐는 먼저 들어온 작업을 먼저 실행한다(First-In-First-Out)
- 실행할 것이 아무것도 남아있지 않을 때만 마이크로태스크 큐에 있는 작업이 실행되기 시작한다.

요약하자면, 어떤 프라미스가 준비되었을 때 이 프라미스의 `.then/catch/finally` 핸들러가 큐에 들어간다고 생각하면 된다. 이때 핸들러들은 실행되지 않는다. 현재 코드에서 모든 코드의 실행이 이뤄진 상태(자유로운 상태)가 되었을 때에서야 JS엔진은 큐에서 작업을 꺼내 실행하게 된다.

위 예시에서 코드 종료가 먼저 출력되는 이유는 아래 그림과 같다.

![스크린샷 2022-11-03 00.25.49](https://raw.githubusercontent.com/back-seung/Today_I_Learned/master/uPic/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202022-11-03%2000.25.49.png)

프라미스 핸들러는 항상 내부 큐를 통과하게 된다.

여러 개의 `.then/catch/finally`를 사용해 만든 체인의 경우, 각 핸들러는 비동기적으로 실행된다. 큐에 들어간 핸들러 각각은 현재 코드가 완료되고, 큐에 적재된 이전 핸들러의 실행이 완료되었을 때 실행된다.

**그럼, "프라미스 성공" 을 먼저 "코드 종료"를 나중에 출력되게 하려면 어떻게 해야 할까?** 실행 순서가 중요한 경우에는 이런 요구사항이 충족되도록 코드를 작성해야 한다.

방법은 꽤나 쉽다. `.then`을 사용해 큐에 넣으면 된다

```javascript
Promise.resolve()
.then(() => alert("프라미스 성공"))
.then(() => alert("코드 종료"));
```

이제 의도한 대로 코드가 작동된다.

### 처리되지 못한 거부

자바스크립트 엔진은 *프라미스와 에러 핸들링에서 학습한* `unhandledrejection` 이벤를 어떻게 찾는지 정확히 알 수 있다.

'처리되지 못한 거부'는 마이크로태스크 큐의 끝에서 프라미스 에러가 처리되지 못할 때 발생한다.

정상적인 경우, 개발자는 에러가 생길 것을 대비하여 프라미스 체인에 .catch를 추가해 에러를 처리한다.

```javascript
let promise = Promise.reject(new Error("프라미스 실패!"));
promise.catch(err => alert('잡았다!'));

// 에러가 잘 처리되었으므로 실행되지 않습니다.
window.addEventListener('unhandledrejection', event => alert(event.reason));
```

그런데 .catch를 추가해주는 걸 잊은 경우, 엔진은 마이크로태스크 큐가 빈 이후에 unhandledrejection 이벤트를 트리거한다.

```javascript
let promise = Promise.reject(new Error("프라미스 실패"));

// 프라미스 실패
window.addEventListener('unhandledrejection', event => alert(event.reason));
```

그런데 만약 아래와 같이 setTimeout을 이용해 에러를 나중에 처리하면 어떤 일이 생길까?

```javascript
let promise = Promise.reject(new Error("프라미스 실패));     
setTimeout(() => promise.catch(err => alert('잡았다')), 1000);                                       

// Error: 프라미스 실패
window.addEventListener('unhandledrejection', event => alert(event.reason));
```

예시를 실행하면 `프라미스 실패!`가 먼저, `잡았다!`가 나중에 출력되는 걸 확인할 수 있다.

마이크로태스크 큐에 대해 몰랐다면 "에러를 잡았는데도 왜 `unhandledrejection` 핸들러가 실행되는 거지?"라는 의문을 가졌을 것이다.

`unhandledrejection`은 마이크로태스크 큐에 있는 작업 모두가 완료되었을 때 생성된다. 엔진은 프라미스들을 검사하고 이 중 하나라도 ‘거부(rejected)’ 상태이면 `unhandledrejection` 핸들러를 트리거 한다. 이로써 앞선 의문이 자연스레 해결되었다.

위 예시를 실행하면 `setTimeout`을 사용해 추가한 `.catch` 역시 트리거 된다. 다만 `.catch`는 `unhandledrejection`이 발생한 이후에 트리거 되므로 `프라미스 실패!`가 출력된다.

### 요약

모든 프라미스 동작은 ‘마이크로태스크 큐’(ES8 용어)라 불리는 내부 ‘프라미스 잡(promise job)’ 큐에 들어가서 처리되기 때문에 프라미스 핸들링은 항상 비동기로 처리된다.

따라서 `.then/catch/finally` 핸들러는 항상 현재 코드가 종료되고 난 후에 호출된다.

어떤 코드 조각을 `.then/catch/finally`가 호출된 이후에 실행하고 싶다면 `.then`을 체인에 추가하고 이 안에 코드 조각을 넣으면 된다.

브라우저와 Node.js를 포함한 대부분의 자바스크립트 엔진에선 마이크로태스크가 '이벤트 루프(event loop)'와 '매크로태스크(macrotask)'와 깊은 연관 관계를 맺는다.



## async와 await - ⭐️⭐️

async와 await라는 특별한 문법을 사용하면 프라미스를 좀 더 편하게 사용할 수 있다. async, await는 놀라울 정도로 이해하기 쉽고 어렵지 않다.

### async 함수

async키워드부터 알아보자. async는 function앞에 위치한다

```javascript
async function f() {
  return 1;
}
```

function앞에 async를 붙이면 해당 함수는 항상 프라미스를 반환한다. 프라미스가 아닌 값을 반환하더라도 이행 상태의 프라미스로 값을 감싸 이행된 프라미스가 반환되도록 한다.

아래 예시의 함수를 호출하면 result가 1인 이행 프라미스가 반환된다. 직접 확인해보자.

```javascript
async function f() {
  return 1;
}

f().then(alert); // 1
```

명시적으로 프라미스를 반환하는 것도 가능한데, 결과는 동일하다.

```javascript
async function f() {
  return Promise.resolve(1);
}

f().then(alert());
```

async가 붙은 함수는 반드시 프라미스를 반환하고 프라미스가 아닌 것은 프라미스로 감싸 변환한다. 그런데 async가 제공하는 기능은 이뿐만이 아니다. 또 다른 키워드 await는 async 함수 안에서만 동작한다.



### await

await의 문법은 다음과 같다

```javascript
// await는 async 함수 안에서만 동작한다.
let value = await promise;
```

자바스크립트는 await 키워드를 만나면 프라미스가 처리될 때까지 기다린다. 결과는 그 이후 반환된다.

1초 후 이행되는 프라미스를 예시로 사용하여 await가 어떻게 동작하는지 살펴보자.

```javascript
async function f() {
  let promise = new Promise((revolve, reject) => {
	setTimetout(() => resolve("완료"), 1000)
  });
  
  let result = await promise; // 프라미스가 이행될 때까지 기다림(*)
  alert(result);
}

f();
```

함수를 호출하고 함수 본문이 실행되는 도중에 (*)로 표시한 줄에서 잠시 '중단'되었다가 프라미스가 처리되면 실행이 재개된다. 이때 프라미스 객체의 result값이 변수 result에 할당된다. 따라서 위 예시를 실행하면 1초 뒤에 완료가 출력된다.

await는 말 그대로 프라미스가 처리될 때까지 함수 실행을 기다리게 만든다. 프라미스가 처리되면 그 결과와 함께 실행이 재개된다. 프라미스가 처리되길 기다리는 동안엔 엔진이 다른 일(다른 스크립트를 실행, 이벤트 처리 등)을 할 수 있기 때문에 CPU리소스가 낭비되지 않는다.

await는 promise.then보다 좀 더 세련되게 프라미스의 result값을 얻을 수 있도록 해주는 문법이다.

promise.then보다 가독성도 좋고 쓰기도 쉽다.

> 일반 함수에는 await를 사용할 수 없다.
>
> async 함수가 아닌데  await를 사용하면 문법 에러가 발생한다.
>
> ```javascript
> function f() {
>   let promise = Promise.resolve(1);
>   let result = await promise; // Syntax Error
> }
> ```
>
> function 앞에 async를 붙이지 않으면 이런 에러가 발생할 수 있다. 앞서 설명한 await는 async안에서만 동작하는 것을 잊지 말자.

프라미스 체이닝 챕터의 showAvatar()예시를 async/await를 사용해 다시 작성해보자.

1. 먼저, .then호출을 await로 바꿔야 한다.
2. function앞에  async를 붙여  await를 사용할 수 있도록 한다.

```javascript
async function showAvatar() {

  // JSON 읽기
  let response = await fetch('/article/promise-chaining/user.json');
  let user = await response.json();

  // github 사용자 정보 읽기
  let githubResponse = await fetch(`https://api.github.com/users/${user.name}`);
  let githubUser = await githubResponse.json();

  // 아바타 보여주기
  let img = document.createElement('img');
  img.src = githubUser.avatar_url;
  img.className = "promise-avatar-example";
  document.body.append(img);

  // 3초 대기
  await new Promise((resolve, reject) => setTimeout(resolve, 3000));

  img.remove();

  return githubUser;
}

showAvatar();
```

코드가 깔끔해지고 읽기도 쉬워졌다. 프라미스를 사용해 .then().then()을 호출하는 것보다 훨씬 나은거 같다.

> await는 최상위 레벨 코드에서 작동하지 않는다.
>
> await를 이제 막 사용하기 시작한 사람은 최상위 레벨 코드에 await를 사용할 수 없다는 사실을 잊곤 한다. 아래와 같은 코드는 동작하지 않는다.
>
> ```javascript
> // 최상위 레벨 코드에선 문법 에러가 발생함
> let response = await fetch('/article/promise-chaining/user.json');
> let user = await response.json();
> ```
>
> 하지만 익명 async 함수로 코드를 감싸면 최상위 레벨 코드에도 await를 사용할 수 있다.
>
> ```javascript
> (asnyc () => {
>   let response = await fetch('/article/promise-chaining/user.json');
> 	let user = await response.json();
> })();
> ```

> await는 'thenable' 객체를 받는다.
>
> promise.then처럼 await에도 thenable 객체(then 메서드가 있는 호출 가능한 객체)를 사용할 수 있다. thenable 객체는 서드파티 객체가 프라미스가 아니지만 프라미스와 호환 가능한 객체를 제공할 수 있다는 점에서 생긴 기능이다. 서드 파티에서 받은 객체가 .then을 지원하면 이 객체를 await와 함께 사용할 수 있다.
>
> await는 데모용 클래스 Thenable의 인스턴스를 받을 수 있다.
>
> ```javascript
> class Thenable {
>   constructor(num) {
>     this.num = num;
>   }
>   then(reolve, reject) {
>     alert(resolve);
>     // 1000밀리초 후에 이행됨(result는  this.num*2)
>     setTimeout(() => resolve(this.num * 2), 1000); // (*)
>   }
> };
> 
> async function f() {
>   // 1초 후, 변수 result는 2가 됨
>   let result = await new Thenable(1)
>   alert(result);
> }
> 
> f();
> ```
>
> await는 .then이 구현되어 있으면서 프라미스가 아닌 객체를 받으면, 내장 함수 resolve와 reject를 인수로 제공하는 메서드인  .then을 호출한다.(일반 Promise executor가 하는 일과 동일) 그리고 나서 await는 resolve와 reject 중 하나가 호출되길 기다렸다가((*)로 표시한 줄 ) 호출 결과를 가지고 다음 일을 진행한다.

> async 클래스 메서드
>
> 메서드 이름 앞에 async를 추가하면 async 클래스 메서드를 선언할 수 있다.
>
> ```javascript
> class Waiter {
>   async wait() {
> 		return await Promise.reolve(1);
>   }
> }
> 
> new Waiter()
> 	.wait()
> 	.then(alert); //1
> ```
>
> asnyc 메서드와 async 함수는 프라미스를 반환하고 await를 사용할 수 있다는 점에서 동일하다.

### 에러 핸들링

프라미스가 정상적으로 이행되면 await promise는 프라미스 객체의  result에 저장된 값을 반환한다. 반면 프라미스가 거부되면 마치  throw문을 작성한 것처럼 에러가 던져진다.

- 예시

```javascript
async function f() {
	throw new Error("에러 발생");
}
```

실제 상황에선 프라미스가 거부되기전에 약간의 시간이 지체되는 경우가 있다. 이런 경우엔 await가 에러를 던지기 전에 지연이 발생한다.

await가 던진 에러는 throw가 던진 에러를 잡을 때처럼 try..catch를 사용해 잡을 수 있다.

```javascript
async function f() {
  try {
    let response = await fetch('http://유효하지-않은-주소');
  } catch(err) {
    alert(err);
  }
}

f(); // TypeError: failed to fetch
```

에러가 발생하면 제어 흐름이 catch 블록으로 넘어간다. 여러 줄의 코드를 try로 감싸는 것도 가능하다.

```javascript
async function f() {
  
  try {
    let response = await fetch('http://유효하지-않은-주소');
    let user = await response.json();
  } catch(err) {
    // fetch와 response.json()에서 발생한 에러 모두를 여기서 잡는다.
    alert(err);
  }
}

f();
```

try..catch가 없으면 아래 예시의  async 함수 f()를 호출해 만든 프라미스가 거부 상태가 된다. f()에 .catch를 추가하면 거부된 프라미스를 처리할 수 있다.

```javascript
asnyc function f() {
  let reponse = await fetch('http://유효하지-않은-주소');
}

// f()는 거부 상태의 프라미스가 된다.
f().catch(alert); // TypeError: failed to fetch // (*)
```

.catch를 추가하는 걸 잊으면 처리되지 않은 프라미스 에러가 발생한다.(콘솔에서 직접 확인해보자) 이런 에러는 프라미스와 에러 핸들링 챕터에서 설명한 한  unhandledrejection을 사용해 잡을 수 있다.

>async/await와 promise.then/catch
>
>async/await를 사용하면 await가 대기를 처리해주기 때문에 .then이 거의 필요하지 않다. 여기에 더하여 .catch 대신 일반 try..catch를 사용할 수 있다는 장점도 생긴다. 항상 그런건 아니지만 promise.then을 사용하는 것보다 async/await를 사용하는 것이 대게는 더 편리하다.

> async/await는 Promise.all과도 함께 쓸 수 있다.
>
> 여러 개의 프라미스가 모두 처리되길 기다려야 하는 상황이라면 이 프라미스들을 Promise.all로 감싸고 여기에 await를 붙여 사용할 수 있다.
>
> ```javascript
> // 프라미스 처리 결과가 담긴 배열을 기다린다.
> let reulsts = await Promise.all([
>   fetch(url1);
>   fetch(url2);
> 	...
> ]);
> ```
>
> 실패한 프라미스에서 발생한 에러는 보통 에러와 마찬가지로 Promise.all로 전파된다. 에러 때문에 생긴 예외는 try..catch로 감싸 잡을 수 있다.

### 요약

funciton앞에 async 키워드를 추가하면 두 가지 효과가 있다.

- 함수는 언제나 프라미스를 반환한다.
- 함수 안에서 await를 사용할 수 있다.

프라미스 앞에  await 키워드를 붙이면 자바스크립트는 프라미스가 처리될 때까지 대기한다. 처리가 완료되면 조건에 따라 아래와 같은 동작이 이어진다.

1. 에러 발생 - 예외가 생성된다.(에러가 발생한 장소에서 throw Error를 호출한 것과 동일)
2. 에러 미발생 - 프라미스 객체의 result 값을 반환

async/await를 함께 사용하면 읽고, 쓰기 쉬운 비동기 코드를 작성할 수 있다.

async/await를 사용하면 promise.then/catch가 거의 필요 없어진다. 하지만 가끔 바깥 스코프에서 비동기 처리가 필요할 때같이 promise.then/catch를 사용해야만 하는 경우가 생기기 때문에 async/await가 프라미스를 기반으로 한다는 사실도 기억해야 한다. 여러 작업이 있고, 이 작업들이 모두 완료될 때까지 기다리려면 Promise.all을 활용할 수 있다는 점도 알아두자.
