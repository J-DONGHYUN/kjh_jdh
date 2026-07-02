# **목표**

테스트는 변경을 안전하게 만들기 위한 문서이자 자동 검증 수단이다.

좋은 테스트는 다음 조건을 만족한다.

- 테스트 이름만 읽어도 어떤 동작을 검증하는지 알 수 있다.
- 실패했을 때 원인을 빠르게 좁힐 수 있다.
- 구현 세부사항보다 외부로 드러나는 행위를 검증한다.
- 테스트 데이터가 의도를 흐리지 않는다.

## **테스트 계층**

| **테스트** | **대상** | **목적** |
| --- | --- | --- |
| Unit Test | Domain, Implement 단위 클래스 | 빠른 피드백, 정책/계산/검증 확인 |
| Service Test | Service 유스케이스 | 비즈니스 흐름과 트랜잭션 경계 확인 |
| Repository Test | Infra | 쿼리, 매핑, 영속성 확인 |
| Controller Test | Presentation | 요청/응답, 검증, 상태 코드 확인 |
| Integration Test | 여러 레이어 결합 | 주요 시나리오 회귀 방지 |

## **네이밍 규칙**

테스트 메서드는 한글 또는 영어 중 팀이 선택한 하나를 일관되게 사용한다.

권장 형식:

```java
// 테스트 메서드
public void pay() {}

@DisplayName("결제에 성공한다")
@Test
void pay() {
}

@DisplayName("포인트가 부족하면 결제에 실패한다")
@Test
void pay_pointIsNotEnough() {
}
```

규칙은 다음과 같다.

- `@DisplayName`은 사용자 행위 또는 비즈니스 규칙 중심으로 작성한다.
- 메서드명은 영어를 기본으로 하고, 빌드 도구와 IDE에서 검색하기 쉬운 형태를 사용한다.
- 하나의 테스트는 하나의 이유로 실패해야 한다.
- 실패하는 테스트 코드의 이름은 {메서드명}_{실패이유} 의 형식으로 작성한다.

## **Given-When-Then**

테스트 본문은 given, when, then 구역으로 나눈다.

```java
@DisplayName("포인트를 사용해 결제하면 사용 포인트가 차감된다")
@Test
void pay() {
    // given
    User user = userFixture.normalUser();
    Store store = storeFixture.openStore();
    PaymentCommand command = new PaymentCommand(user.id(), store.id(), Money.wons(10_000), Point.wons(1_000));

    // when
    PaymentResult result = paymentService.pay(command);

    // then
    assertThat(result.status()).isEqualTo(PaymentStatus.PAID);
    assertThat(pointReader.read(user.id()).amount()).isEqualTo(Point.wons(0));
}
```

규칙은 다음과 같다.

- given은 테스트 조건을 만든다.
- when은 검증 대상 행위를 한 번만 실행한다.
- then은 결과를 검증한다.
- **테스트를 이해하는 데 필요 없는 값은 fixture나 builder로 숨긴다.**

## **Fixture 규칙**

Fixture는 테스트의 의도를 가리는 중복을 줄이기 위해 사용한다.

```java
public class UserFixture {

    public static User normalUser() {
        return User.create("user-1", UserGrade.NORMAL);
    }
}
```

규칙은 다음과 같다.

- 모든 필드를 받는 거대한 fixture 메서드는 만들지 않는다.
- 테스트에서 중요한 값은 테스트 본문에 드러낸다.
- 중요하지 않은 기본값은 fixture 내부에 둔다.
- fixture는 운영 코드에 의존하지 않는다.

### **중요한 값을 주입하는 Fixture Builder 규칙**

테스트에서 특정 값이 검증 의도에 중요하다면, fixture 메서드 인자를 계속 늘리지 않고 테스트 전용 builder를 사용한다.

권장 형식:

```java
public class UserFixture {

    public static UserBuilder user() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private String id = "user-1";
        private UserGrade grade = UserGrade.NORMAL;
        private Point point = Point.wons(10_000);

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder grade(UserGrade grade) {
            this.grade = grade;
            return this;
        }

        public UserBuilder point(Point point) {
            this.point = point;
            return this;
        }

        public User build() {
            return User.create(id, grade, point);
        }
    }
}
```

사용 예시:

```java
@DisplayName("보유 포인트가 결제 금액보다 적으면 결제에 실패한다")
@Test
void pay_pointIsNotEnough() {
    // given
    User user = UserFixture.user()
        .point(Point.wons(500))
        .build();

    PaymentCommand command = PaymentCommandFixture.paymentCommand()
        .amount(Money.wons(10_000))
        .usePoint(Point.wons(1_000))
        .build();

    // when & then
    assertThatThrownBy(() -> paymentService.pay(command))
        .isInstanceOf(NotEnoughPointException.class);
}
```

규칙은 다음과 같다.

- builder의 기본값은 정상 케이스를 만들 수 있는 값으로 둔다.
- builder 메서드는 체이닝을 위해 자기 자신을 반환한다.
- build()는 항상 유효한 테스트 객체를 반환해야 한다.
- 모든 필드를 한 번에 받는 create(id, name, age, status, ...) 형태의 fixture 메서드는 만들지 않는다.
- 테스트마다 값이 자주 바뀌는 필드만 builder 메서드로 노출한다.
- builder는 src/test 아래 테스트 코드에서만 사용한다.

## **Assertion 규칙**

테스트 검증문은 JUnit 기본 assertion보다 AssertJ를 우선 사용한다.

권장한다.

```java
assertThat(result.status()).isEqualTo(PaymentStatus.PAID);
assertThat(result.amount()).isEqualByComparingTo(Money.wons(10_000));
assertThatThrownBy(() -> paymentService.pay(command))
    .isInstanceOf(PaymentException.class)
    .hasMessage(PAYMENT_NOT_ENOUGH_POINT.getMessage());
```

지양한다.

```java
assertEquals(PaymentStatus.PAID, result.status());
assertThrows(NotEnoughPointException.class, () -> paymentService.pay(command));
```

규칙은 다음과 같다.

- 기본 검증은 assertThat(actual).isEqualTo(expected) 형식을 사용한다.
- 예외 검증은 assertThatThrownBy 또는 assertThatExceptionOfType을 사용한다.
- 컬렉션 검증은 containsExactly, extracting, filteredOn 등 AssertJ의 표현력 있는 메서드를 사용한다.
- JUnit assertion은 테스트 생명주기와 관련된 특수 상황이 아니면 사용하지 않는다.

## **Mock 사용 규칙**

Mock은 외부 협력 객체의 결과를 통제해야 할 때만 사용한다.

허용한다.

- 외부 API 호출 대체
- 메시지 발행 대체
- 시간, UUID, 랜덤값 같은 비결정 요소 대체
- 실패 상황 강제

지양한다.

- 도메인 모델 mock
- 단순 값 객체 mock
- 같은 모듈의 작은 객체까지 과도하게 mock 처리
- 실제 검증보다 verify() 호출 수에만 집중한 테스트

## DB 테스트 독립 환경 설정

각 테스트의 독립적인 환경을 위한 롤백 처리 방식으로 직접 `DBCleaner` 를 만들어 사용한다.

권장한다.

```java
@Component
public class DbCleaner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public void clean() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        List<String> tables = jdbcTemplate.queryForList(
                """
                        SELECT table_name
                        FROM information_schema.tables
                        WHERE table_schema = DATABASE()
                          AND table_type = 'BASE TABLE'
                        """,
                String.class
        );
        tables.forEach(table ->
                jdbcTemplate.execute("TRUNCATE TABLE `" + table + "`")
        );
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        if (redisTemplate != null && redisTemplate.getConnectionFactory() != null) {
            redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        }
    }
}

@BeforeEach
void init() {
	dbCleaner.clean();
}
```

지양한다.

```java
@Transactional
@DisplayName("결제에 성공한다")
@Test
void pay() {}
```

## 계층별 테스트

### **Controller 테스트**

Controller 테스트는 HTTP 계약을 검증한다.

검증 대상:

- URL
- HTTP Method
- Request Body 검증
- Response Body 형식
- HTTP Status
- 인증/인가 실패

비즈니스 로직 자체는 Controller 테스트에서 깊게 검증하지 않는다.

Controller 테스트는 RestAssuredMockMvc를 표준으로 사용한다.

권장 형식:

```java
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @DisplayName("결제를 생성하면 201 Created를 응답한다")
    @Test
    void createPayment() {
        CreatePaymentRequest request = new CreatePaymentRequest(1L, 10_000L);

        given(paymentService.pay(any()))
            .willReturn(new PaymentResult(1L, PaymentStatus.PAID));

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/v1/payments")
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", equalTo(1))
            .body("status", equalTo("PAID"));
    }
}
```

규칙은 다음과 같다.

- Controller 테스트의 요청/응답 검증은 RestAssuredMockMvc를 사용한다.
- 단순 MockMvc.perform() 방식은 새 테스트에서 사용하지 않는다.
- HTTP Method, URL, Header, Query Parameter, Request Body, Status Code, Response Body를 API 계약 관점에서 검증한다.
- Service는 mock 처리하고 Controller의 HTTP 계약에 집중한다.
- 복잡한 비즈니스 성공/실패 조합은 Service 테스트에서 검증한다.
- RestAssuredMockMvc 설정은 공통 테스트 베이스 클래스 또는 @BeforeEach에서 초기화한다.

### Service 테스트

Service 테스트는 가능하면 실제와 가까운 환경에서 검증한다.

권장한다.

```java
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public abstract class IntegrationTestSupport {

    @MockitoSpyBean
    protected JwtProvider jwtProvider;

    @Autowired
    private DbCleaner dbCleaner;

    @BeforeEach
    void setUp() {
        dbCleaner.clean();
    }
}
```

### **Repository 테스트**

Repository 테스트는 실제 DB와 가까운 환경에서 검증한다.

```java
@ActiveProfiles("test")
@Import({DbCleaner.class, JpaConfiguration.class})
@DataJpaTest
public abstract class RepositoryTestSupport {

    @Autowired
    protected TestEntityManager em;
    @Autowired
    private DbCleaner dbCleaner;

    @BeforeEach
    void setUp() {
        dbCleaner.clean();
    }
}
```

권장한다.

- Testcontainers 또는 프로젝트 표준 테스트 DB 사용
- 쿼리 조건과 정렬 검증
- Entity 매핑 검증
- N+1이나 fetch join이 중요한 조회 테스트

지양한다.

- H2와 운영 DB 문법 차이를 무시한 테스트
- 단순 Spring Data 메서드까지 과도하게 테스트

## **테스트 데이터 정리**

- 테스트는 서로 독립적이어야 한다.
- 실행 순서에 의존하지 않는다.
- DB 테스트는 트랜잭션 롤백 또는 명시적 cleanup 전략을 사용한다.

## **리뷰 체크리스트**

- 테스트 이름이 비즈니스 행위를 설명하는가?
- 실패 케이스가 함께 검증되는가?
- 테스트가 구현 세부사항에 과하게 결합되어 있지 않은가?
- Fixture가 테스트 의도를 가리지 않는가?
- 통합 테스트와 단위 테스트의 역할이 분리되어 있는가?
