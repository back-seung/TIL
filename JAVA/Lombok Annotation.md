## Lombok Annotation



### Lombok이란

> lombok은 자바에서 Model Object(VO, DTO, Domain)를 만들 때, 멤버필드에 대한 Getter/Setter, ToString과 멤버필드에 주입하는 생성자를 만드는 코드 등 불필요하게 반복적으로 만드는 코드를 어노테이션을 통해 줄여 주는 라이브러리, 프로젝트이다.



### Annotations

> 자주 사용되는 어노테이션.

#### @NonNull

* Null값이 될 수 없다는 것을 명시한다. NullPointerException에 대한 대비책이 될 수 있다.

#### CleanUp

* 자동으로 close() 메소드를 호출한다.

#### @Getter/@Setter

* 코드가 컴파일 될 때, Getter/Setter 메소드를 생성한다.

#### @ToString

* toString() 메소드를 생성한다. @ToString(exclude={"제외값"})처럼 원하지 않는 속성은 제외시킬 수 있다.

#### @EqualsAndHashCode

* 해당 객체의 `equals()`와 `hashCode()` 메소드를 생성한다.

#### @NoArgsConstructor

* 파라미터를 받지 않는 생성자를 만들어준다.

#### @RequiredArgsConstructor

* 지정된 속성들에 대해서만 생성자를 만든다.

#### @AllArgsConstructor

* 모든 속성에 대해서 생성자를 만들어 낸다.

#### @Data

* `@ToString()`, `@EqualsAndHashCode`, `@Getter`, `@Setter`, `@RequiredArgsConstructor`를 합쳐 둔 어노테이션이다.

#### @Value

* 불변 클래스를 생성할 때 사용한다.

#### @Builder

* 빌더 패턴을 사용할 수 있도록 코드를 생성한다.

#### @SneakyThrows

* 예외 발생 시 Throwable 타입으로 반환한다.

#### @Synchronized

* 메소드에서 동기화를 설정한다.

#### @Getter(lazy=true)

* 동기화를 이용해서 최초 한번만 getter를 호출한다.





### 실무에서 Lombok 사용법

> [출처 - popit.kr/실무에서-lombok-사용법 ](https://www.popit.kr/%EC%8B%A4%EB%AC%B4%EC%97%90%EC%84%9C-lombok-%EC%82%AC%EC%9A%A9%EB%B2%95/)

#### @Data는 지양하자

> @Data는 @ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor를 한번에 사용하는 강력한 어노테이션이다.

```java
@Entity
@Table(name ="member")
@Data
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column( name = "email", nullable = false )
	private String email;
	@Column( name = "name", nullable = false )
	private String name;
	@Column( name = "create_at", nullable = false, updatable = false )
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(name = "update_at", nullable = false )
    private LocalDateTime updateat;
}
```

* 무분별한 Setter 남용의 문제가 있다.

@Data를 사용하면 Setter를 자동으로 Setter를 지원하게 된다. Setter는 그 의도가 분명하지 않고 객체를 언제든지 변경할 수 있는 상태가 되어서 객체의 안전성이 보장받기 힘들다.

위 코드에서 email의 변경 기능이 제공되지 않는다고 가정한다면 email 관련된 setter도 제공되지 않는것이 안전하다.



* ToString으로 인한 양방향 연관관게시 순환 참조의 문제

```java
@Entity
@Table(name = "member")
@Data
public class Member {
    ....
    @OneToMany
    @JoinColumn(name = "coupon_id")
    private List<Coupon> coupons = new ArrayList<>();
}
@Entity
@Table(name = "coupon")
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Member member;
    public Coupon(Member member) {
        this.member = member;
    }
```

위 코드처럼 Member 객체와 Coupon 객체가 양방향 연관관계일 경우 ToString을 호출하면 `무한 순환 참조가 발생`한다. JPA를 사용하다보면 객체를 Json으로 직렬화 하는 과정에서 발생하는 문제와 동일한 문제이다. 무분별하게 @Data를 사용하게 되면 이런 문제를 만나기 쉽다.

문제의 쉬운 해결 방법으로는 @ToString(exclude="제외할 Column")을 적용하여 항목에서 제외하는 것이다.

> ToString(exclude="") 적용 예제

```java
@ToString(exclude = "coupons")
public class Member { ... }
```



### 바람직한 lombok 사용법

```java
@Entity
@Table(name = "member")
@ToString(exclude = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "email"})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;
    @OneToMany
    @JoinColumn(name = "coupon_id")
    private List<Coupon> coupons = new ArrayList<>();
    @Builder
    public Member(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
```

* NoArgsConstructor 접근을 최소화하자

기본 생성자를 public으로 열어두면 안정성이 저하된다. `@NoArgsConstructor(acess = AccessLeve.PROTECTED)`를 사용하여 외부에서의 접근을 막아 객체 생성시 안정성을 보장해주자.

```java
@Builder
public class Member { ... }
```

클래스 위에 @Builder를 사용한다면 @AllArgsConstructor 어노테이션을 붙인 효과를 발생시켜 **모든 멤버 필드에 대해서 매개변수를 받는 기본 생성자를 만든다.**

> `createAt`, `updateAt` : @CreationTimestamp, @UpdateTimestamp 각각의 어노테이션이 해당 일을 담당한다. 객체 생성시 받지 않아도 될 데이터까지 접근할 수 있게 된다.

따라서 아래 코드처럼 원하는 데이터만 받아올 수 있는 생성자를 따로 생성하여 @Builder를 붙히는게 바람직하다.

```java
public class Member {

	@Builder
    public Member (String email, String name) {
        this.email = email;
        this.name = name;
    }
}
```

