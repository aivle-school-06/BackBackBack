# DART 기업 목록 동기화 요구사항

## 1. 목적
- DART corpCode ZIP/XML을 이용해 기업 목록을 초기 적재하고, 이후 변경분만 갱신한다.

## 2. 확정 요구사항
- 초기 적재 후 **변경분 갱신** 전략을 사용한다.
- 스케줄러는 **기본 OFF**, 필요 시 활성화한다.
- **관리자용 수동 실행 API**를 제공한다. (스케줄링과 독립)
- API Key 환경변수 이름은 **DART_API_KEY**로 통일한다.

## 3. 동기화 범위
- 대상 엔드포인트: `/api/corpCode.xml` (ZIP 내 XML)
- 매핑 대상 컬럼: `corp_code`, `corp_name`, `corp_eng_name`, `stock_code`, `modify_date`
- 식별자 기준: `corp_code` 유니크

## 4. 후속 작업(요약)
- Batch Job/Step 설계 및 스캐폴딩
- 변경분 갱신(업서트) 로직 구현
- 수동 실행 API + 스케줄 토글 구성
- 테스트/운영 가이드 정리
