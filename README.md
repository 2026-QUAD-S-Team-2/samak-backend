## 사막 ㅣ AI 기반 채용 사기 예방 플랫폼
<img width="800" alt="3" src="https://github.com/user-attachments/assets/01f0207e-6e5e-40d4-bec3-7fc5555da327" />

### Tech Stack

#### Frontend
<img src="https://img.shields.io/badge/Flutter-02569B?style=for-the-badge&logo=Flutter&logoColor=white">

#### Backend
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 

#### AI / LLM
<img src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white"> <img src="https://img.shields.io/badge/FastAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white"> <img src="https://img.shields.io/badge/Gemini API-8E75B2?style=for-the-badge&logo=google-gemini&logoColor=white">

#### Database
<img src="https://img.shields.io/badge/Google%20Cloud%20SQL-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
#### Infrastructure
<img src="https://img.shields.io/badge/Google%20Compute%20Engine-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white"> <img src="https://img.shields.io/badge/Google%20Cloud%20Storage-AECBFA?style=for-the-badge&logo=googlecloud&logoColor=black"> <img src="https://img.shields.io/badge/Google%20Cloud%20Pub%2FSub-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white"> <img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white">


### Service Architecture
<img width="600" alt="아키텍처" src="https://github.com/user-attachments/assets/13572f50-1068-4140-b925-4db2a793619f" />

### Key Features 
#### 🧠 통합 채용 공고 검증
- AI 기반 공고 신뢰도 분석 및 의심 패턴 탐지 및 분석 메시지 제공
- 유사 피해 사례 연계
- 해당 국가 평균 소득 관련 통계 제공

#### 🛡️ 사기 관련 정보 제공
- 사기 수법 및 대응 가이드 제공
- 최신 사기 뉴스 제공
- 사기 예방을 위한 데일리 퀴즈 제공

#### 📁 피해 사례 통합 관리 시스템
- 직접 겪은 피해 사례 등록
- 국가, 회사명, 전화번호, 텔레그램 ID 등 식별자 기반 검색

### Key Contributions
- 비동기 메시징 시스템 도입을 통해 채용 공고 분석 API를 비동기 처리 구조로 개선하여 17초 지연 문제 해결
- 댓글 및 대댓글 조회 API N+1 문제를 해결하여 DB 쿼리 수를 1+2N+M → 2개로 최적화
- Jenkins 기반 CI/CD 파이프라인을 구축 및 Blue/Green 배포 전략을 적용을 통한 무중단 배포 환경 구성
- OAuth2 기반의 구글 로그인 기능을 통한 사용자 경험 향상
