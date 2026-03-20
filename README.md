# On-Device News Recommendation App

추천 서버 없이, 온디바이스에서 사용자 행동 기반 개인화 추천을 수행하는 뉴스 앱입니다.
외부 API로 데이터를 수집하고, 추천 로직은 디바이스 내에서 처리합니다.

---

## 📌 핵심 목표

* 추천 서버 없이 온디바이스 개인화 추천 구현
* 사용자 행동 기반 추천 (클릭, 체류시간, 스크롤)
* 단순하지만 확장 가능한 MVP 설계

---

## 🏗 아키텍처

**MVVM + Clean Architecture (Lite)**

```
presentation (UI)
    ↓
domain (추천 로직, 비즈니스 규칙)
    ↓
data (API, DB)
```

### 설계 의도

* 추천 로직을 domain 레이어로 분리
* UI와 비즈니스 로직 완전 분리
* 온디바이스 환경에서 테스트 및 확장 용이성 확보

---

## 📦 예상 패키지 구조

```
com.djyoo.smartnews

├── presentation
│   ├── home
│   ├── detail
│   └── common
│
├── domain
│   ├── model
│   ├── usecase
│   ├── recommender
│   └── repository
│
├── data
│   ├── remote
│   ├── local
│   └── repository
│
└── core
    ├── keyword
    ├── util
    └── config
```

---

## 🛠 기술 스택

### UI

* Jetpack Compose

### Architecture

* MVVM
* Clean Architecture (Lite)

### Async / Reactive

* Kotlin Coroutines
* Kotlin Flow

### Network

* Retrofit2
* OkHttp

### Image Loading

* Glide

### Local Storage

* Room Database

### Dependency Injection

* Hilt (Dagger)

---

## 📡 데이터 수집

* 네이버 뉴스 API 사용
* query: `"뉴스"` (광범위 키워드)
* 최신순 기준 100개 조회
* 스크롤 시 페이징

---

## 💾 저장 구조

### 뉴스

* 최대 100개 유지
* title, description, link, pubDate
* 전처리된 keywords 포함

### 유저 프로파일

* 키워드 기반 관심도
* (keyword, score)
* Top-K (최대 20개)

### 사용자 행동

* 클릭 여부
* 체류시간
* 스크롤 여부

---

## 🔑 키워드 처리 전략

### 추출

* title + description 사용
* 공백 기준 split

### 정규화

* 조사 제거 (삼성이 → 삼성)
* 특수문자 제거
* 소문자 변환

### 필터링

* 2글자 이상
* 불용어 제거

### 제한

* 기사당 최대 5개 키워드만 사용

---

## 👤 유저 프로파일

* 키워드 기반 관심도 관리
* 최대 20개 유지 (Top-K)
* 점수 낮은 키워드 제거
* decay로 오래된 관심사 감소

---

## ⏳ Decay (시간 기반 관심도 감소)

사용자의 관심사는 시간에 따라 변화하기 때문에,
오래된 관심사가 계속 추천에 영향을 주지 않도록 decay를 적용합니다.

---

### 개념

* 키워드 점수는 시간이 지남에 따라 점진적으로 감소
* 최근 행동일수록 더 큰 영향을 가지도록 설계

---

### 예시

```
아이폰: 100 → 90 → 81 → 72 ...
```

---

### 적용 방식

```
keywordScore = keywordScore * decayRate
```

* decayRate: 0.9 ~ 0.95 권장

---

### 적용 시점

* 앱 실행 시
* 사용자 행동 반영 전
* (선택) 일정 주기

---

### 설계 의도

* 오래된 관심사가 과도하게 유지되는 문제 방지
* 최신 사용자 행동을 더 강하게 반영
* Top-K 구조와 결합하여 자동으로 관심사 정리

---

## 🧠 추천 시스템

### 추천 구성

* 총 20개
* 70% 개인화 + 30% 랜덤

---

### 점수 계산

```
score = click + dwellTime + scroll
```

* dwellTime이 가장 중요한 신호

---

### 기사 점수

```
articleScore = Σ(userKeywordScore)
```

---

### fallback

* 개인화 데이터 부족 시 랜덤 추천

---

## 🔄 추천 갱신 시점

* 앱 진입 시
* 사용자 새로고침
* 사용자 행동 발생 시 (권장)

---

## 📄 전체 리스트

* DB 100개 + API 페이징 데이터
* 추천 리스트 제외

---

## 🎯 설계 포인트

* 카테고리 없이 키워드 기반 유사도 사용
* Top-K + decay로 노이즈 자동 제거
* 추천 로직을 독립적인 도메인으로 분리
* 온디바이스 환경 최적화

---

## ⚠️ 한계

* 형태소 분석 미사용 → 정확도 제한
* 동의어 처리 제한
* 단순 키워드 기반 추천

---

## 🚀 향후 개선

* 동의어 사전 확장
* dwellTime 로그 스케일 적용
* 키워드 clustering
* lightweight embedding 도입
* 서버 기반 추천 시스템 확장

---

## 💡 핵심 요약

```
추천 서버 없이,
온디바이스에서 유저 행동 기반으로
개인화 추천을 수행하는 구조
```
