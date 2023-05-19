# JPA_1

:fire: 인프런 강의 [실전! 스프링 부트와 JPA 활용1 ] 정리 및 실습 :fire:

<br>

## 도메인 분석 설계

<br>

**기능 목록**

- 회원기능(등록, 조회)
- 상품기능(등록, 수정, 조회)
- 주문기능(상품 주문, 주문 내역 조회, 주문 취소)
- 기타 요구사항(상품 재고 관리, 상품 종류(도서, 음반, 영화), 상품 카테고리 구분)
- 상품 주문시 배송 정보 입력

<br>

**도메인 모델과 테이블 설계**

[<img width="500" alt="image" src="https://github.com/alswo471/JPA_1/assets/92145785/8f9234a6-e47a-4a62-874c-e8e50a981c96">](https://github.com/alswo471/JPA_1/blob/master/image/%EB%8F%84%EB%A9%94%EC%9D%B8%20%EB%AA%A8%EB%8D%B8.png)<br>

<br>

> **회원, 주문, 상품의 관계**
>
> > 회원은 여러 상품 주문 가능하고 한 번 주문할 때 여러 상품 선택할 수 있으므로 주문과 상품은 **다대다 관계** 이지만 다대다 관계는 최대한 사용해선 안되므로 **다대일 관계로** 풀어낸 그림

<br>
<br>

<img width="500" alt="image" src="https://github.com/alswo471/JPA_1/blob/master/image/%ED%85%8C%EC%9D%B4%EB%B8%94%20%EC%84%A4%EA%B3%84.png">

<br>
<br>

> **참고**
>
> > 현업에서는 주문이 회원을 참조하는 것으로 충분,
> > 테이블명이 **ORDERS** 인 것은 **order by** 때문에 **예약어**로 잡고
> > 있는 경우가 많다. 그래서 관례상 **ORDERS** 를 많이 사용

<br>

## 연관관계 매핑 분석

<br>

**회원/주문 :**

일대다 다대일 양방향 관계, 연관관계 주인은 외래키가 있는 주문으로 정하는 것이 좋음

<br>

**주문상품/주문 :**

다대일 양방향 관계, 주문상품이 연관관계 주인

<br>

**주문상품/상품 :**

다대일 단방향 관계

<br>

**주문/배송 :**

일대일 양방향 관계

<br>

**카테고리/상품 :**

@ManyToMany 를 사용해서 매핑한다.(실무에서 @ManyToMany는 사용하면 안됌)

<br>

> **참고**
>
> > 외래 키가 있는 곳을 연관관계의 주인으로 정해야한다. 비지니스상 우위에 있다고 연관관계 주인으로 정하면 안되고, 일대다 관계에서 항상 다쪽을 주인으로 정하면 된다.
> > 그러지 않으면 유지보수가 어렵고 추가적으로 별도의 업데이트 쿼리가 발생하는 성능 문제도있다.

<br>

## 엔티티 클래스 개발

[소스코드](https://github.com/alswo471/JPA_1/tree/master/jpashop/src/main/java/jpabook/jpashop/domain)

**회원 엔티티 (Member)**

> : 엔티티의 식별자는 id 를 사용하고 PK 컬럼명은 member_id 를 사용, 테이블은 관례상 테이블명 + id 를 많이 사용, 중요한 것은 일관성

<br>

> **참고**
>
> > 실무에서는 가급적 Setter는 필요한 경우에만 사용, 왜냐하면 Setter를 막 열어두면 차후에
> > 엔티티에가 도대체 왜 변경되는지 추적하기 점점 힘들어진다. 그래서 엔티티를 변경할 때는 Setter 대신에
> > 변경 지점이 명확하도록 변경을 위한 비즈니스 메서드를 별도로 제공해야 한다

<br>

> **참고**
>
> > 주소 값 타입 클래스를 보면 값 타입은 변경 불가능 해야한다. @Setter 를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들면 된다. JPA 스펙상
> > 엔티티나 임베디드 타입( @Embeddable )은 자바 기본 생성자(default constructor)를 public 또는
> > protected 로 설정해야 한다. public 으로 두는 것 보다는 protected 로 설정하는 것이 그나마 더
> > 안전하여 protected로 설정한다.

<br>

### 엔티티 설계시 주의점

<br>

**모든 연관관계는 지연로딩으로 설정**

> 실무에서 모든 연관관계는 지연로딩( **LAZY** )으로 설정해야 한다. 왜냐하면 즉시로딩( **EAGER** )은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1
> 문제가 자주 발생, 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용, **@XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야
> 한다.**

<br>

**컬렉션은 필드에서 초기화 하자.**

> 우선 null 문제에서 안전 그리고 만약에 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부
> 메커니즘에 문제가 발생해서 필드레벨에서 생성하는 것이 가장 안전하다.

<br>

## 회원 레포지토리 개발

[레포지토리](https://github.com/alswo471/JPA_1/tree/master/jpashop/src/main/java/jpabook/jpashop/repository)

[서비스](https://github.com/alswo471/JPA_1/tree/master/jpashop/src/main/java/jpabook/jpashop/service)

<br>

**@Repository :**

- 컴포넌트 대상이 되서 스프링 빈에 자동등록된다.

<br>

**@Transactional(readOnly = true) :**

- readOnly = true => 읽기 전용에만 (읽기가 아닌 쓰기에 넣으면 변경이 안되서 넣으면 안됌)

<br>

**@RequiredArgsConstructor**

- 파이널 있는 필드만 가지고 생성자를 만들어준다.

## 상품 도메인 개발

[레포지토리](https://github.com/alswo471/JPA_1/blob/master/jpashop/src/main/java/jpabook/jpashop/repository/ItemRepository.java)

[서비스](https://github.com/alswo471/JPA_1/blob/master/jpashop/src/main/java/jpabook/jpashop/service/ItemService.java)

## 스프링 부트 라이브러리

<br>

**목록**

- spring-boot-starter-web
- spring-boot-starter-tomcat: 톰캣 (웹서버)
- spring-webmvc: 스프링 웹 MVC
- spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View)
- spring-boot-starter-data-jpa
- spring-boot-starter-aop
- spring-boot-starter-jdbc
- HikariCP 커넥션 풀
- hibernate + JPA: 하이버네이트 + JPA
- spring-data-jpa: 스프링 데이터 JPA
- spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅
- spring-boot
- spring-core
- spring-boot-starter-logging
- logback, slf4j
- 테스트 라이브러리
- spring-boot-starter-test
- junit: 테스트 프레임워크
- mockito: 목 라이브러리
- assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
- spring-test: 스프링 통합 테스트 지원
- 핵심 라이브러리
- 스프링 MVC
- 스프링 ORM
- JPA, 하이버네이트
- 스프링 데이터 JPA
- 기타 라이브러리
- H2 데이터베이스 클라이언트
- 커넥션 풀: 부트 기본은 HikariCP
- WEB(thymeleaf)
- 로깅 SLF4J & LogBack
- 테스트
