# 📌 My Select Shop

이 프로젝트는 **네이버 API**를 통해 상품을 검색하고, 관심 상품을 등록한 후 **나만의 보관 장소**에서 폴더를 생성하여 관리하는 **개인화된 쇼핑 관리 시스템**입니다. 사용자는 관심 있는 상품을 모아 자신만의 상품 목록을 구성하고, 폴더별로 쉽게 관리할 수 있습니다.

---

## 🚀 주요 기능

### ✅ 상품 검색
- **네이버 API**를 통한 상품 검색
- 상품 이름, 가격, 이미지 등을 포함한 상세 정보 제공

### 💖 관심 상품 관리
- **관심 상품 등록** (사용자가 관심 있는 상품을 추가)
- 등록된 관심 상품 **목록 조회** 및 **삭제** 기능

### 📂 폴더 관리
- **폴더 생성** 및 **폴더별 관리** (상품을 카테고리별로 분류)
- **폴더 내 상품 관리** (상품 추가/삭제)
- **폴더 목록 조회** 및 관리

### 🔐 사용자 관리
- **회원가입 및 로그인 (Spring Security 기반, OAuth2 및 JWT 인증/인가)**
- **회원별 관심 상품** 및 **폴더 관리**

### 🕒 가격 자동 업데이트
- **스케줄러** 를 사용하여 매일 새벽 1시에 상품 가격을 자동으로 업데이트
- **네이버 API** 를 통해 가격을 재조회하여 최신 정보로 갱신

### ⏰ API 사용 시간 추적
-	AOP를 사용하여 상품, 폴더, 네이버 API의 사용 시간을 추적
-	로그인된 사용자에 대해 API 사용 시간을 DB에 기록



---

## 🔍 기술 스택

- **Backend**: Java, Spring Boot
- **Database**: MySQL / H2
- **ORM**: JPA (Hibernate)
- **API**: Naver Shopping API
- **Security**: Spring Security (JWT 인증/인가)
- **Build Tool**: Gradle
- **Version Control**: Git, GitHub
- **Frontend Support**: Sparta(부트캠프)에서 지원한 Frontend 코드
---


## 🏗️ 폴더 구조

```bash
src/
 ├── main/
 │   ├── java/com/sparta/myselectshop/
 │   │   ├── controller/      # 컨트롤러 레이어 (API 엔드포인트 처리)
 │   │   ├── service/         # 비즈니스 로직 (상품 검색, 폴더 관리 등)
 │   │   ├── repository/      # 데이터 접근 레이어 (JPA Repository)
 │   │   ├── entity/          # JPA 엔티티 (상품, 관심 상품, 폴더 등)
 │   │   ├── dto/             # 데이터 전송 객체 (API 응답 모델 등)
 │   │   ├── config/          # 설정 파일 (Spring Security 설정 등)
 │   │   ├── security/        # Spring Security 관련 파일 (JWT 인증/인가)
 │   │   ├── aop/             # Spring AOP 관련 파일
 │   │   ├── naver/           # Naver Api 요청
 │   │   ├── exception/       # Global 예외처리
 │   ├── resources/
 │   │   ├── application.properties  # 환경설정 파일 (DB 연결 설정, API 키 등)
 ├── test/                   # 테스트 코드
     ├── controller/          # 컨트롤러 테스트
     ├── service/             # 서비스 테스트
     ├── repository/          # 레포지토리 테스트
