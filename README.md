# SafetyGPS-server
> 사용자 안전 귀갓길 제공 서비스

---

## 커밋 메시지 컨벤션

### 1. 커밋 유형 지정

> 커밋 유형은 **영어 대문자**로 작성합니다.

| 커밋 유형 | 의미 |
|------------|------|
| **Feat** | 새로운 기능 추가 |
| **Fix** | 버그 수정 |
| **Docs** | 문서 수정 |
| **Style** | 코드 포맷팅, 세미콜론 누락 등 코드 로직에 영향이 없는 경우 |
| **Refactor** | 코드 리팩토링 |
| **Test** | 테스트 코드, 리팩토링 테스트 코드 추가 |
| **Chore** | 패키지 매니저 수정, 설정 파일 변경 (예: `.gitignore`) |
| **Design** | CSS 등 사용자 UI 디자인 변경 |
| **Comment** | 필요한 주석 추가 및 변경 |
| **Rename** | 파일 또는 폴더 이름 수정 및 이동 |
| **Remove** | 파일 삭제 작업 수행 |
| **BREAKING CHANGE** | 커다란 API 변경이 있을 경우 |
| **HOTFIX** | 급하게 치명적인 버그를 고쳐야 하는 경우 |

---

### 2. 커밋 메시지 작성 규칙

- 제목은 명확하고 간결하게, **한글로 작성**
- 제목과 본문은 **빈 줄**로 구분
- 본문에는 **무엇을 / 왜 변경했는지**를 구체적으로 작성  
- 모든 커밋 메시지는 다음 포맷을 따릅니다.

[타입] 제목
본문 (변경 이유 및 상세 설명)

#### ✍ 예시
[Feat] 로그인 기능 추가

JWT 토큰을 이용한 로그인 기능을 추가했습니다.
- 토큰 발급 로직 구현
- 로그인 성공/실패 시 상태 코드 반환

---

## 브랜치 협업 전략 (Branch Strategy)

### 1. 브랜치 구조

main
 ┣ develop
 ┣ feature/{기능명}
 ┣ hotfix/{이슈명}
 ┗ release/{버전명}

| 브랜치명 | 용도 |
|-----------|------|
| **main** | 실제 운영(배포) 브랜치 |
| **develop** | 개발 단계의 통합 브랜치 (기능 병합 전 테스트용) |
| **feature/** | 개별 기능 개발용 브랜치 (예: `feature/login`) |
| **hotfix/** | 긴급 수정용 브랜치 (예: `hotfix/token-error`) |
| **release/** | 배포 전 안정화 브랜치 (예: `release/v1.0.0`) |

---

### 2. 브랜치 생성 규칙

- 새 기능 개발 시 `develop` 브랜치에서 **feature 브랜치 생성**
- 브랜치 이름은 **소문자-하이픈(-)** 으로 구분
- 기능 완료 후 `develop` 브랜치로 **Pull Request(PR)** 생성

# 브랜치 생성
git checkout develop
git pull origin develop
git checkout -b feature/login-api

# 개발 완료 후 push
git add .
git commit -m "[Feat] 로그인 API 구현"
git push origin feature/login-api
---

### 3. Pull Request(PR) 규칙

1. **기능 단위로 PR 생성**  
   - 너무 많은 변경을 한 번에 올리지 않기  
2. **PR 제목 규칙**  
   - `[타입] 기능명 / 간단한 요약` 형식으로 작성  
     예시 → `[Feat] 로그인 API / JWT 기반 로그인 로직 추가`
3. **리뷰 필수**  
   - 최소 1인 이상 코드 리뷰 승인 후 `develop`에 병합
4. **PR 본문에 포함될 내용**  
   - 변경 요약  
   - 테스트 방법  
   - 관련 이슈 번호(있을 경우)

### 4. 브랜치 예시 흐름

main
 ┗ develop
     ┣ feature/login
     ┣ feature/map-api
     ┣ feature/user-auth
     ┗ hotfix/token-expire
---

## 유의사항

- 항상 **`develop` 기준으로 브랜치를 파생**
- **커밋 단위는 작게**, 기능별로 나눠서 관리
- 커밋 전 `git pull origin develop`으로 **최신 상태 유지**
- 충돌 발생 시, **로컬에서 해결 후 push**
- 코드 리뷰 시 **가독성과 효율성 중심 피드백**

---

## 예시 워크플로우 요약

# 1. develop에서 브랜치 생성
git checkout develop
git checkout -b feature/route-service

# 2. 작업 후 커밋
git add .
git commit -m "[Feat] 경로 안내 API 추가"

# 3. 원격에 push
git push origin feature/route-service

# 4. PR 생성 → 리뷰 → develop 병합

---

## 권장 규칙 요약

| 항목 | 규칙 |
|------|------|
| 커밋 메시지 | `[타입] 제목` + 본문 (한글 작성) |
| 브랜치명 | `feature/login`, `hotfix/token-error` 형식 |
| 병합 방식 | feature → develop : squash / develop → main : merge |
| 리뷰 정책 | 최소 1인 이상 승인 후 병합 |
| 코드 스타일 | 일관성 유지, 불필요한 로그/주석 금지 |
