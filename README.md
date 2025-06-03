# 📄 bonbon - 영업 관리 시스템

## 📚 목차

1. [프로젝트 개요](#1-프로젝트-개요)  
2. [팀원 소개](#2-팀원-소개)  
3. [주요 기능 구성](#3-주요-기능-구성)  
4. [기술 스택](#4-기술-스택)  
5. [문서 리스트](#5-문서-리스트)  

## 1. 프로젝트 개요
> **BonBon - 본사-가맹점 영업 관리 시스템**

**본사와 가맹점 간의 운영을 효율화하기 위해 개발된 통합 영업 관리 시스템입니다.**  
**Spring Boot 기반의 REST API 서버로, 본사와 가맹점 간의 재고 흐름, 메뉴 지정, 가맹점 주문, 공지사항 관리, 가맹점별 메뉴 관리 등 다양한 비즈니스 로직을 처리합니다.**

### 🛠 개발 배경
- 프랜차이즈 본사는 전국 가맹점의 재고, 주문, 매출 등 다양한 운영 정보를 실시간으로 파악하고 관리해야 합니다.
- 기존 시스템은 엑셀/수기 방식에 의존하고 있어 업무 누락, 시간 낭비, 정보 비대칭 등의 문제가 발생합니다.
- 본사와 가맹점 간의 원활한 **데이터 공유**와 **운영 효율화**가 가능한 시스템이 필요했습니다.

### 🎯 서비스 목표
- 본사와 가맹점 간의 실시간 연동 기능 구현
- 본사 메뉴 지정 → 가맹점 선택적 판매 구조 확립
- 재고 흐름 자동화 및 신청 이력 관리
- 메뉴/공지사항 일괄 전파 시스템
- 데이터 기반의 매출 분석과 가맹점 운영 인사이트 제공

### 🚀 기대 효과
- 본사 운영 효율 증가 및 관리 체계화
- 가맹점별 메뉴/재고/매출 현황 가시화
- 공지사항/메뉴 전달의 일원화
- 가맹점주에게 실시간 운영 데이터 제공으로 주체적인 경영 가능
- 수작업 최소화 → 인력 및 시간 비용 절감

<br>

## 2. 팀원 소개

<div align="center">

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/9e6b2726-8b4d-4077-8980-6a49cb7b7125" width="100"  height="100"><br>
      <b><a href="https://github.com/kimdoyun0806">김도윤</a></b><br>팀원
    </td>
     <td align="center">
      <img src="https://github.com/user-attachments/assets/6835cd71-cc90-41ed-ae2d-f8064b82b3a2" width="100"  height="100"><br>
      <b><a href="https://github.com/mlnstone">김민석</a></b><br>팀원
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/225eeac9-508b-443a-b6f5-f3128892e9d8" width="100" height="100"><br>
      <b>🏆 <a href="https://github.com/namoo36">이승용</a></b><br><b>팀장</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/74ac18a5-b79a-47b1-8b3a-e22f1dbc886c" width="100"  height="100"><br>
      <b><a href="https://github.com/jelee55">이제경</a></b><br>팀원
    </td>
  </tr>
</table>

</div>

<br>

## 3. 주요 기능 구성

**BonBon 시스템은 사용자 유형에 따라 본사와 가맹점으로 구분되며, 각 역할에 맞춘 기능이 나누어져 있습니다.** <br>
**본사**는 전체 관리와 통계 분석, 메뉴 일괄 등록 등 전반적인 운영을 담당하고, **가맹점**은 재고 신청, 매출 확인, 메뉴 선택 등 가맹점 운영에 필요한 기능에 집중되어 있습니다.

### 🏢 본사 기능

| 기능 카테고리       | 주요 기능 설명                                                                 |
|--------------------|---------------------------------------------------------------------------------|
| 📋 계정 관리        | 가맹점주/담당자 계정 등록 및 관리                                                |
| 🍽️ 메뉴 관리        | 메뉴 등록/수정/삭제, 필수 판매 메뉴 지정, 오더 스톱 처리                          |
| 📊 매출 분석        | 지점별 매출/마진/납부액 자동 계산 및 시각화                                       |
| 🧭 가맹점 요약 확인   | 가맹점 목록/위치, 정보 요약, 메뉴 판매 순위 등                                     |
| 🗂️ 가맹점 상세 관리  | 가맹점 등록/삭제, 정보 입력 및 메모 작성                                           |
| 📢 공지사항 게시판   | 공지 등록/수정/삭제 및 가맹점 알림                                               |
| 📦 재고 관리        | 재고 목록, 단건조회, 등록/수정/삭제, 가맹점 재고 신청 내역 조회                    |
| 🏢 본사 관리        | 본사 정보 조회 및 수정, 본사별 메뉴 카테고리 조회                                  |
| 🧾 체크리스트        | 위생, 소독, 서비스 등 체크리스트 등록/수정/삭제/조회                              |
| 🌍 지역 관리        | 시/도, 구/군 목록 조회 및 지역별 가맹점 조회                                       |

---

### 🏪 가맹점 기능
  
| 기능 카테고리       | 주요 기능 설명                                                                 |
|--------------------|---------------------------------------------------------------------------------|
| 🍽️ 메뉴 관리        | 본사 메뉴 조회 및 판매 선택 가능                                                |
| 📊 가맹점 요약       | 본인 매출 차트, 인기 메뉴 확인, 인근 가맹점 비교                                  |
| 🗂️ 가맹점 관리       | 점주/가게 정보 관리, 판매방식/배달 플랫폼 등록 및 조회                           |
| 📢 공지사항 게시판   | 본사 공지사항 및 이벤트 게시글 조회                                              |
| 📦 재고 관리        | 가맹점 재고 조회/수정/삭제                                                      |
| 📦 주문 관리        | 재고 신청 등록/수정/삭제 및 신청 내역 단건·전체 조회                             |

<br>

## 4. 기술 스택
### 🌐 Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white"> ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) ![QueryDSL](https://img.shields.io/badge/QueryDSL-005571?style=for-the-badge&logo=hibernate&logoColor=white)

### 🌐 Frontend
<img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=white"> <img src="https://img.shields.io/badge/chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white"> <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white"> <img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white"> <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=Vue.js&logoColor=white"> <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">

### 🗃️ DB
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> 

### ⚙️ Tools
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Figma-9C29B1?style=for-the-badge&logo=Figma&logoColor=white"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"> <img src="https://img.shields.io/badge/erdCloud-0097A7?style=for-the-badge&logo=erdCloud&logoColor=white">

### 🚀 CI/CD
<img src="https://img.shields.io/badge/AWS EC2-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> <img src="https://img.shields.io/badge/DockerHub-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=Kubernetes&logoColor=white"> <img src="https://img.shields.io/badge/ArgoCD-F5503C?style=for-the-badge&logo=argo&logoColor=white"> <img src="https://img.shields.io/badge/Route53-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white"> <img src="https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/ELB-8C4FFF?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white">

### 💬 Communication
<img src="https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=Jira&logoColor=white"> <img src="https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=Discord&logoColor=white"> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">

<br>


## 5. 문서 리스트

<details>
<summary><strong>📌 프로젝트 기획서</strong></summary>

- 링크: [프로젝트 기획서](https://docs.google.com/document/d/1WR15hpaJhKwKJS-IyoIicgbIqbv5x12jx_sFvAZhgKI/edit?usp=sharing)

</details>

<details>
<summary><strong>📌 요구사항 정의서</strong></summary>

- 링크: [요구사항 정의서](https://docs.google.com/spreadsheets/d/1DiH1bHHJueDsMxrl0_vGZ8jhjAMejhCEK-gJhL4AF-4/edit?gid=1152197925#gid=1152197925)

</details>

<details>
<summary><strong>📌 시스템 아키텍처</strong></summary>

![Image](https://github.com/user-attachments/assets/eb46fcf7-f32c-4a81-92bf-62812368c142)

</details>

<details>
<summary><strong>📌 WBS</strong></summary>

- 링크: [WBS](https://docs.google.com/spreadsheets/d/1DiH1bHHJueDsMxrl0_vGZ8jhjAMejhCEK-gJhL4AF-4/edit?gid=0#gid=0)

</details>

<details>
<summary><strong>📌 ERD</strong></summary>

- 링크 : [ERD](https://www.erdcloud.com/d/58wZNJdygPpztALBK)
![ERD - bonbonCafe](https://github.com/user-attachments/assets/616a765a-0b08-470e-9cc9-f4404c1139ba)

</details>

<details>
<summary><strong>📌 화면설계서 (Figma)</strong></summary>

- 링크: [화면설계서(Figma)](https://www.figma.com/design/mpyMKrXy8SDofcHFK5FtMN/beyond-3team-fin?node-id=0-1&p=f&t=SriIXOUKBkT1eIof-0)

</details>

<details>
<summary><strong>📌 API 명세서 </strong></summary>

- 링크: [API 명세서](https://www.notion.so/playdatacademy/API-1d6d943bcac28104835dd13b87578046?pvs=4)

</details>

<details>
<summary><strong>📌 단위 테스트 결과서</strong></summary>

- 링크: [단위 테스트 결과서](https://docs.google.com/spreadsheets/d/1DiH1bHHJueDsMxrl0_vGZ8jhjAMejhCEK-gJhL4AF-4/edit?gid=417184159#gid=417184159)

</details>

<details>
<summary><strong>📌 UI/UX 단위 테스트 결과서</strong></summary>

- 링크: [UI/UX 단위 테스트 결과서](https://docs.google.com/spreadsheets/d/1DiH1bHHJueDsMxrl0_vGZ8jhjAMejhCEK-gJhL4AF-4/edit?gid=1228868911#gid=1228868911)

</details>

<details>
<summary>📌 UX/UI 테스트</summary>

<details>
<summary>가맹점</summary>
<ul>
  <li>가맹점 수정 <img src="이미지링크" /></li>
  <li>가맹점 등록 <img src="이미지링크" /></li>
  <li>가맹점 삭제 <img src="이미지링크" /></li>
  <li>전체 가맹점 위치 조회 <img src="이미지링크" /></li>
  <li>가맹점 요약 조회 <img src="이미지링크" /></li>
  <li>특정 가맹점 조회 <img src="이미지링크" /></li>
</ul>
</details>

<details>
<summary>가맹점 메뉴</summary>
<ul>
  <li>가맹점 전용 메뉴 추가, 조회 <img src="https://github.com/user-attachments/assets/9fa01b54-1b91-49ff-b866-68cd4c14d808" /></li>
  <li>가맹점 전용 메뉴 삭제 <img src="" /></li>
</ul>
</details>

<details>
<summary>재고</summary>
<ul>
  <li>가맹점 재고 주문 및 조회, 본사 주문 처리 <img src="https://github.com/user-attachments/assets/672c15fa-ed4f-4474-bc01-d2d4965f490d" /></li>
  <li>본사 재고 등록<img src="https://github.com/user-attachments/assets/9df1248f-6a0d-4b06-bf34-f5e7043abd83" /></li>
  <li>본사 재고 삭제<img src="https://github.com/user-attachments/assets/c72a9cdc-5b86-41bd-8d1f-ecb24ecf7eac" /></li>
</ul>
</details>

<details>
<summary>게시판</summary>
<ul>
  <li>게시글 등록, 수정, 삭제 <img src="https://github.com/user-attachments/assets/d95e03e3-20ae-4fcf-8b93-57c095f01489" /></li>
  <li>문자 일괄 전송 <img src="https://github.com/user-attachments/assets/7c784913-b19b-4b4a-8efb-b4bf1f3e7622" /></li>
</ul>
</details>

<details>
<summary>매출</summary>
<ul>
  <li>전체 가맹점 기간 매출 조회 <img src="이미지링크" /></li>
  <li>전체 가맹점 매출 순위 조회 <img src="이미지링크" /></li>
  <li>전체 가맹점 메뉴 판매 순위 조회 <img src="이미지링크" /></li>
  <li>전체 가맹점 예상 매출 조회 <img src="이미지링크" /></li>
  <li>가맹점 기간별 예상 매출 조회 <img src="이미지링크" /></li>
  <li>가맹점 기간별 매출 조회 <img src="이미지링크" /></li>
  <li>가맹점 메뉴별 판매 순위 조회 <img src="이미지링크" /></li>
  <li>지역별 매출 순위 조회 <img src="이미지링크" /></li>
  <li>가맹점 일 매출 조회 <img src="이미지링크" /></li>
</ul>
</details>

<details>
<summary>메뉴</summary>
<ul>
  <li>메뉴 등록 <img src="https://github.com/user-attachments/assets/4ec94851-8e31-4235-bb08-6b8ab9a76cb7" /></li>
  <li>메뉴 조회, 수정, 판매중인 가맹점 조회 <img src="https://github.com/user-attachments/assets/bd02f042-b2a1-406c-a6a7-27bfdd141640" /></li>
  <li>메뉴 삭제 <img src="https://github.com/user-attachments/assets/0034f171-e940-436e-bd89-75ea5a5c5acd" /></li>
  <li>카테고리별 메뉴 조회 <img src="https://github.com/user-attachments/assets/af65c697-8bf1-455a-924a-c9b61fa86862" /></li>

</ul>
</details>

<details>
<summary>본사</summary>
<ul>
  <li>본사 정보 조회, 수정 <img src="https://github.com/user-attachments/assets/5bce534a-b048-4815-b5c8-6d8d8ed88904" /></li>
</ul>
</details>

<details>
<summary>재료</summary>
<ul>
  <li>재료 전체 조회 <img src="이미지링크" /></li>
</ul>
</details>

<details>
<summary>지역</summary>
<ul>
  <li>구/군 목록 조회 <img src="이미지링크" /></li>
  <li>시/도 목록 조회 <img src="이미지링크" /></li>
  <li>지역별 가맹점 조회 <img src="이미지링크" /></li>
</ul>
</details>

<details>
<summary>카테고리</summary>
<ul>
  <li>카테고리의 메뉴 전체 조회 <img src="이미지링크" /></li>
  <li>카테고리 전체 조회 <img src="이미지링크" /></li>
</ul>
</details>

<details>
<summary>회원</summary>
<ul>
  <li>가맹점주 계정 목록 확인 <img src="이미지링크" /></li>
  <li>본사 등록 계정 수정 <img src="이미지링크" /></li>
  <li>가맹점 담당자 계정 목록 확인 <img src="이미지링크" /></li>
  <li>본사 가맹점주 계정 등록 <img src="이미지링크" /></li>
  <li>본사 등록 계정 조회 <img src="이미지링크" /></li>
  <li>본사 생성 계정 전체 조회 <img src="이미지링크" /></li>
  <li>개인 계정 비밀번호 변경 <img src="이미지링크" /></li>
  <li>개인 계정 정보 변경 <img src="이미지링크" /></li>
  <li>개인 계정 조회 <img src="이미지링크" /></li>
  <li>본사 가맹점 담당자 계정 등록 <img src="이미지링크" /></li>
  <li>AccessToken 재발급 <img src="이미지링크" /></li>
  <li>로그아웃 <img src="이미지링크" /></li>
  <li>로그인 <img src="이미지링크" /></li>
  <li>본사 등록 계정 삭제 <img src="이미지링크" /></li>
</ul>
</details>

</details>
