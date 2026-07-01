## 🔗 관련 이슈
- Closes #

### 📂 주요 변경 파일
<!-- 주요 로직 변경 파일명을 적어주세요 (단순 오타, 줄 바꿈 등 제외) -->
-

## ✨ 작업 내용
<!-- 어떤 변경 사항이 있었는지 주요 내용을 적어주세요 -->
-

## 🤔 고민한 부분 (선택)
<!-- 고민했던 부분이 있다면 작성해주세요 -->
-

## ✅ 체크리스트
- [ ] 코드 리뷰 요청 완료
- [ ] 커밋 메시지 컨벤션 준수
- [ ] 불필요한 코드/로그 제거
- [ ] 문서 업데이트 (필요 시)
- [ ] 테스트 코드 작성

## 🔍 셀프 코드 검증 (백엔드 헥사고날)

**도메인**
- [ ] `create()` / `restore()` 모든 파라미터 null 체크
- [ ] 상태 전이 메서드에서 선행 상태 + 파라미터 유효성 검증
- [ ] 도메인 예외는 `IllegalArgumentException` / `IllegalStateException` 사용

**서비스**
- [ ] 도메인 예외(`IllegalArgumentException` / `IllegalStateException`) → `BusinessException` 래핑
- [ ] check-then-act 패턴 사용 시: DB UNIQUE 제약 + `RepositoryImpl` 예외 변환 처리 여부
- [ ] 타 도메인 존재 확인 시 상태(DELETED 등) 필터 조건 포함 여부

**인프라**
- [ ] `save()` vs `saveAndFlush()` 선택 근거 확인
- [ ] `DataIntegrityViolationException` 처리 필요한 UNIQUE 제약 있는지 확인
- [ ] `from()` 에서 `entity.id = domain.getId()` 설정 (UPDATE 지원)

**테스트**
- [ ] 서비스 테스트: `ArgumentCaptor`로 저장값 필드 수준 검증
- [ ] 컨트롤러 테스트: PathVariable → Command 매핑 케이스 포함
- [ ] 예외 케이스별 HTTP 상태코드 검증 완료
