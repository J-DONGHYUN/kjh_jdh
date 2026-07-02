# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

이커머스 애플리케이션. Spring Boot 기반 백엔드이며, 현재는 Spring Initializr 스타터 상태로 도메인 코드가 아직 없습니다 (진입점 `IbidApplication`만 존재). 새 기능은 대부분 밑바닥부터 작성하게 됩니다.

- **artifact / root project 이름**: `ibid` (`settings.gradle`, `application.properties`의 `spring.application.name`)
- **base 패키지**: `project.kjhjdh.ibid`
- **스택**: Spring Boot 3.5.16, Java 21 (toolchain), Gradle 8.14.5 (wrapper), Spring MVC + Spring Data JPA, Lombok

## 명령어

Gradle wrapper(`./gradlew`)를 사용합니다. 별도 설치 불필요.

```bash
./gradlew build              # 컴파일 + 테스트 + jar 빌드
./gradlew test               # 전체 테스트 (Docker 필요, 아래 참고)
./gradlew bootRun            # 앱 실행 (운영/일반 DB 설정 사용)
./gradlew classes            # 프로덕션 소스만 컴파일 (빠른 확인용)
```

단일 테스트 실행:

```bash
./gradlew test --tests 'project.kjhjdh.ibid.IbidApplicationTests'
./gradlew test --tests '*.SomeClass.someMethod'   # 메서드 단위
```

로컬 개발 실행 (Testcontainers로 MySQL을 자동 기동):

```bash
./gradlew bootTestRun    # TestIbidApplication을 실행 — 앱 + Testcontainers MySQL
```

## DB / 테스트 아키텍처

DB 구성이 코드에 아직 명시돼 있지 않다는 점이 이 저장소의 핵심 함정입니다.

- **런타임 의존성**으로 H2(embedded)와 MySQL connector가 **둘 다** 들어 있습니다. `application.properties`에는 datasource 설정이 없으므로, `bootRun`으로 어떤 DB를 쓸지는 접속 정보를 추가해야 결정됩니다.
- **테스트와 로컬 개발 실행은 Testcontainers MySQL(`mysql:latest`)에 의존**합니다 (`TestcontainersConfiguration`). 따라서 `./gradlew test`, `./gradlew bootTestRun`을 돌리려면 **Docker 데몬이 실행 중이어야** 합니다. Docker가 없으면 테스트가 실패합니다.
- `TestcontainersConfiguration#mysqlContainer`는 `@ServiceConnection`으로 Spring Boot에 자동 연결됩니다 — 테스트에서 datasource URL/자격증명을 직접 지정할 필요 없음.
- `TestIbidApplication`은 프로덕션 `IbidApplication`을 Testcontainers 설정과 함께 띄우는 개발 전용 진입점입니다 (`SpringApplication.from(...).with(...)`).

새 기능 작업 시 JPA 엔티티/리포지토리는 `project.kjhjdh.ibid` 하위에 도메인별로 배치하세요. 컨트롤러/서비스/리포지토리 레이어 구조는 아직 정해진 것이 없으므로 처음 만드는 패턴이 컨벤션이 됩니다.

## 금지사항

- **기능 구현 시 주석을 달지 마세요.** 코드로 의도가 드러나도록 작성하고, 설명이 필요하면 주석 대신 메서드/변수 이름으로 표현하세요. (테스트의 given/when/then 구역 주석은 예외)

## 협업 컨벤션 (`.github/`)

- 팀은 한국어로 협업하며 이슈/PR 템플릿이 정의돼 있습니다.
- PR 체크리스트가 **커밋 메시지 컨벤션 준수**와 **테스트 코드 작성**을 요구합니다 — 변경 시 함께 지키세요.
- 작업은 이슈(`enhancement` 라벨) → PR(`Closes #이슈번호`) 흐름을 따릅니다.

## 참고 문서

- ./docs/TEST.md : 테스트 관련
