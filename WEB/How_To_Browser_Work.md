## 브라우저의 동작 원리

> 출처 : (https://d2.naver.com/helloworld/59361)
> 		   (https://www.boostcourse.org/web316/lecture/254256?isDesc=false)



### 브라우저의 기본 구조

1. 사용자 인터페이스

   > 주소 표시줄, 이전 / 다음 버, 북마크 메뉴 등 요청한 페이지를 보여주는 창을 제외한 나머지 모든 부분

2. 브라우저 엔진

   >사용자 인터페이스와 렌더링 엔진 사이의 동작 제어

3. 렌더링 엔진

   > 요청한 컨텐츠 표시, 예를 들어 HTML을 요청하면 HTML & CSS 파싱하여 화면에 표시한다.

4. 통신

   > HTTP 요청과 같은 네트워크 호출에 사용된다. 이것은 플랫폼 독립적인 인터페이스이고 각 플랫폼 하부에서 실행된다. 

5. UI 백엔드

   > 콤보 박스 ,창 등의 기본적 장치를 그린다. 플랫폼에서 명시하지 않은 일반적인 인터페이스로서 OS 사용자 인터페이스 체계 사용

6. 자바스크립트 해석기

   > 자바스크립트 코드 해석 및 실행

7. 자료 저장소

   > 쿠키 저장과 같이 모든 종류의 자원을 하드 디스크에 저장할 필요가 있다. HTML5 명세에는 브라우저가 지원하는 `웹 데이터 베이스`가 정의되어 있다.

   

### 구조를 그림으로 표현

![image-20220127163407878](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20220127163407878.png)



* 렌더링 엔진 

  > 렌더링 엔진은 HTML, XML, 이미지 등 요청받은 내용을 브라우저 화면에 표시하는 엔진이다.
  >
  > 각각의 브라우저는 각자의 렌더링 엔진을 가지고 있는데 예를 들어
  >
  > * Safari : WebKit
  > * Firefox : Gecko
  > * Chrome & Opera : Blink 등이 있다.

  

* 렌더링 엔진의 Main Flow :

  1. Parsing HTML to contruct the DOM tree (* DOM = Document Object Model)

     > 구조화된 HTML의 태그들을 해석하고 일종의 트리 구조의 형태로 데이터를 다시 가지고 있게 된다.

  2. Render tree construction

     > 외부 CSS 파일과 함께 포함된 스타일 요소를 파싱한다. 

  3. Layout of the render tree

     > DOM 트리를 기준으로 2.의 결과물을 합쳐 렌더 트리를 배치한다.

  4. Painting the render tree

     > 렌더 트리 각 노드에 대해 화면 상에서 배치할 곳을 결정한다.

  5. 

     > UI 백엔드에서 렌더 트리의 각 노드를 그린다.

  

  

  

