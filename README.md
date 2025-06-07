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
<summary><strong>📌 FRONTEND - 기능 시연</strong></summary>

<details>
<summary>가맹점</summary>
<ul>
  <li>가맹점 수정 <img src="이미지링크" /></li>
  <li>가맹점 등록 <img src="이미지링크" /></li>
  <li>가맹점 삭제 <img src="이미지링크" /></li>
  <li>전체 가맹점 위치 조회 <img src="이미지링크" /></li>
  <li>가맹점 요약 조회 <img src="이미지링크" /></li>
  <li>특정 가맹점 조회 <img src="이미지링크" /></li>
  <li>가맹점 전체 조회 <img src="이미지링크" /></li>
</ul>
</details>

<details>
<summary>가맹점 메뉴</summary>
<ul>
  <li>가맹점 전용 메뉴 추가, 조회 <img src="https://github.com/user-attachments/assets/9fa01b54-1b91-49ff-b866-68cd4c14d808" /></li>
  <li>가맹점 전용 메뉴 삭제 <img src="https://github.com/user-attachments/assets/1143ba36-879f-4c17-8e3b-1e6236f75951"/></li>
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
  <li>가맹점 매출 분석 페이지 <img src="https://github.com/user-attachments/assets/3ec9b535-74ac-4525-9c0e-d7f1d25f425d" /></li>
  <li>지역별 매출 순위 페이지 <img src="https://github.com/user-attachments/assets/9e36a632-ab97-4f93-9588-fc8dd0e9ad88" /></li>
  <li>pdf 다운로드 <img src="https://github.com/user-attachments/assets/fa048233-6afa-4a94-8598-f87c7c435c49" /></li>
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

<!-- ------------- -->

<details>
<summary><strong>📌 BACKEND - 기능 시연</strong></summary>
<details>
<summary>가맹점</summary>
<ul>
  <li>가맹점 수정 
  
  
  </li>
  <li>가맹점 등록 
    
  
  </li>
  <li>가맹점 삭제 
    
  
  </li>
  <li>전체 가맹점 위치 조회 
    
  
  </li>
  <li>가맹점 요약 조회 
    
  
  </li>
  <li>특정 가맹점 조회 
    
  
  </li>  
  <li>가맹점 전체 조회
   
  
  </li>
</ul>
</details>

<details>
<summary>가맹점 메뉴</summary>
<ul>
  <li>가맹점 메뉴 등록 
    <img width="1342" alt="image" src="https://github.com/user-attachments/assets/040fe9f4-88d2-40fb-b647-b4d37be27a26" />
    <img width="1307" alt="image" src="https://github.com/user-attachments/assets/a45572d7-4368-4f5e-806b-d61aa5a38974" />
  </li>
  <li>가맹점 메뉴 삭제 
    <img src="https://github.com/user-attachments/assets/65911ea5-a89a-4148-ab4e-0363ea78fdfc" />
    <img src="https://github.com/user-attachments/assets/7bb3ff4b-1d44-4b97-b1f5-f5bb1cfdf023" />
  </li>
  <li>카테고리별 가맹점 메뉴 조회 
    <img width="1330" alt="image" src="https://github.com/user-attachments/assets/7a4cf8b0-43d8-43be-8395-74397b1fe058" />
    <img width="1290" alt="image" src="https://github.com/user-attachments/assets/beb1904a-b0a2-4226-a41f-2b3e8b4f3229" />
  </li>
  <li>특정 가맹점 메뉴 조회(본사 전용) 
    <img width="1321" alt="image" src="https://github.com/user-attachments/assets/b118a52e-35a4-4dca-92ca-f4f0c24db928" />
    <img width="1266" alt="image" src="https://github.com/user-attachments/assets/e91b093b-098b-425c-a632-16ca344b01bd" />
  </li>
  <li>가맹점 메뉴 단건 조회 
    <img width="1329" alt="image" src="https://github.com/user-attachments/assets/79b7f801-b27f-46e6-9ea6-d9a863a3fddd" />
    <img width="1321" alt="image" src="https://github.com/user-attachments/assets/78b1980a-0f4d-448c-9b78-723139a40049" />
  </li>
  <li>본인 가맹점의 메뉴 전체 조회 
    <img src="https://github.com/user-attachments/assets/79b81e91-b34b-4795-adcc-08a6a150bce3" />
    <img width="1068" alt="image" src="https://github.com/user-attachments/assets/124dcd96-6c2d-4513-89cc-f40f778029ac" />
  </li>
</ul>
</details>

<details>
<summary>가맹점 재고</summary>
<ul>
  <li>가맹점 재고 단건 조회 
    <img width="1335" alt="image" src="https://github.com/user-attachments/assets/461f4758-6b87-446e-a572-d96e5acddabf" />
    <img width="1307" alt="image" src="https://github.com/user-attachments/assets/ff0b94d7-53aa-4ff5-b042-3289dcecc7fc" />
  </li>
  <li>가맹점 재고 전체 조회 
    <img width="1334" alt="image" src="https://github.com/user-attachments/assets/902a9654-3fba-4f59-8365-0d88f3bcdd34" />
    <img width="1321" alt="image" src="https://github.com/user-attachments/assets/84922525-73bf-44ae-b36c-8baaeb1efdd4" />
  </li>
</ul>
</details>

<details>
<summary>가맹점 주문 내역</summary>
<ul>
  <li>재고 신청 내역 단건 조회 
    <img width="1328" alt="image" src="https://github.com/user-attachments/assets/670496c0-02f4-4b77-9d03-999b27285cc2" />
    <img width="1325" alt="image" src="https://github.com/user-attachments/assets/ba8b19f8-aa41-42e8-9d21-923874e9cc20" />
  </li>
  <li>가맹점 재고 신청 내역 전체 조회 (가맹점용)
    <img width="1328" alt="image" src="https://github.com/user-attachments/assets/465016fb-c5ad-465b-91b8-eee996cc7718" />
    <img width="1324" alt="image" src="https://github.com/user-attachments/assets/1bac4ada-b06b-4044-b775-90837e7b815a" />
  </li>
  <li>가맹점 재고 신청 내역 전체 조회 (본사용) 
    <img width="1345" alt="image" src="https://github.com/user-attachments/assets/b7cb64c6-087b-4853-ab1d-23eb89a6a206" />
    <img width="1287" alt="image" src="https://github.com/user-attachments/assets/ccb57e59-674d-4e8d-bb57-e734b160dc89" />
  </li>
  <li>재고 신청 삭제
    <img width="1320" alt="image" src="https://github.com/user-attachments/assets/cd18779f-e364-45d9-b011-27abaccd29d6" />
  </li>
  <li>재고 신청 수정 
    <img width="1334" alt="image" src="https://github.com/user-attachments/assets/de949741-0758-4d26-a34b-9347ba3b6e2a" />
    <img width="1327" alt="image" src="https://github.com/user-attachments/assets/b65e3f01-830c-4361-8a4a-5ea67998cfcf" />
  </li>
  <li>재고 신청 
    <img width="1349" alt="image" src="https://github.com/user-attachments/assets/e8bde47a-f524-4f82-9f07-d0437c58eaf4" />
    <img width="1328" alt="image" src="https://github.com/user-attachments/assets/31236694-50be-4a9c-b96c-cf33687ffd6c" />
  </li>
</ul>
</details>

<details>
<summary>게시판</summary>
<ul>
  <li>게시글 등록 
    <img width="1330" alt="image" src="https://github.com/user-attachments/assets/0bbabe08-bb01-46aa-bb87-c287c54d376a" />
    <img width="1305" alt="image" src="https://github.com/user-attachments/assets/ecc994ab-5eb0-47d9-9cd2-e84f6096a765" />
  </li>
  <li>게시글 전체 조회 
    <img width="1340" alt="image" src="https://github.com/user-attachments/assets/b374f0b5-2d87-4d46-8eef-6b2b648bc4ed" />
    <img width="1288" alt="image" src="https://github.com/user-attachments/assets/92d9727e-9792-4f68-b0a0-52e78d5975bf" />
  </li>
  <li>게시글 단건 조회 
    <img width="1325" alt="image" src="https://github.com/user-attachments/assets/549441c5-3d14-4389-b3c1-c21089ee59aa" />
    <img width="1341" alt="image" src="https://github.com/user-attachments/assets/9c77a617-56ad-4dd7-9ac5-f801b8592e55" />
  </li>
  <li>게시글 수정 
    <img width="1328" alt="image" src="https://github.com/user-attachments/assets/3098c76d-9f47-47b0-b55a-e66acbbdb8ad" />
    <img width="1297" alt="image" src="https://github.com/user-attachments/assets/1bb966d9-17ab-43c7-a4e9-c0e5f08f042a" />
  </li>
  <li>게시글 삭제 
    <img width="1325" alt="image" src="https://github.com/user-attachments/assets/3dd3593d-fe95-4744-a390-87bdf8ce9790" />
    <img width="1322" alt="image" src="https://github.com/user-attachments/assets/5cd5ad29-77ee-4da6-8e47-db7432fecee9" />
  </li>
  <li>문자 일괄 전송 
    <img width="1332" alt="image" src="https://github.com/user-attachments/assets/b53d1e1c-bdc1-4a46-9627-225b986ecfaf" />
    <img width="1306" alt="image" src="https://github.com/user-attachments/assets/4af67bec-edc6-4a08-9cba-e001895c633d" />
    <img width="1306" alt="image" src="https://github.com/user-attachments/assets/0222c9eb-0de8-4dd5-beac-91d53e3b6580" />

  </li>
  
</ul>
</details>

<details>
<summary>매출</summary>
<ul>
  <li>전체 가맹점 기간 매출 조회
  
  
  </li>
  <li>전체 가맹점 매출 순위 조회 
  
  
  </li>
  <li>전체 가맹점 메뉴 판매 순위 조회 
  
  
  </li>
  <li>전체 가맹점 예상 매출 조회 
  
  
  </li>
  <li>가맹점 기간별 예상 매출 조회 
  
  
  </li>
  <li>가맹점 기간별 매출 조회 
  
  
  </li>
  <li>가맹점 메뉴별 판매 순위 조회 
  
  
  </li>
  <li>지역별 매출 순위 조회 
  
  
  </li>
  <li>가맹점 일 매출 조회 
  
  
  </li>
</ul>
</details>

<details>
<summary>메뉴</summary>
<ul>
  <li>메뉴 등록 
    <img width="1321" alt="image" src="https://github.com/user-attachments/assets/059ceb0b-25ac-43ca-b353-a79e08f62925" />
    <img width="1303" alt="image" src="https://github.com/user-attachments/assets/e732d541-5b93-4a65-bc41-e15a538582bf" />
  </li>
  <li>메뉴 수정 
    <img width="1342" alt="image" src="https://github.com/user-attachments/assets/5bcde91d-2769-452a-8f8e-36c29727b945" />
    <img width="1327" alt="image" src="https://github.com/user-attachments/assets/8c8ecce2-4c04-4ea7-bf3a-85e2f2fde200" />
  </li>
  <li>카테고리로 메뉴 조회 
    <img width="1343" alt="image" src="https://github.com/user-attachments/assets/877a822c-0938-4354-9bb5-9e14e3a19685" />
    <img width="1283" alt="image" src="https://github.com/user-attachments/assets/2c0d5f99-9c9a-423f-9cc3-7b389f1ba555" />
  </li>
  <li>메뉴 삭제 
    <img width="1334" alt="image" src="https://github.com/user-attachments/assets/678ec452-a313-4d2a-940f-74fd28fb3db3" />
    <img width="1260" alt="image" src="https://github.com/user-attachments/assets/0a383bd7-8b45-4c14-aa6e-003f3658c250" />
  </li>
  <li>메뉴 단일 조회 
    <img width="1335" alt="image" src="https://github.com/user-attachments/assets/f9b4ad70-8a85-4770-9479-d5de21697b73" />
    <img width="1315" alt="image" src="https://github.com/user-attachments/assets/8532696c-b010-48fe-94b0-16bc52320eb0" />
  </li>
  <li>메뉴 전체 조회
    <img width="1337" alt="image" src="https://github.com/user-attachments/assets/c4bd1ad6-e02b-42f6-b4c6-9a0fd49f19a3" />
    <img width="1354" alt="image" src="https://github.com/user-attachments/assets/e7139f52-26f6-442d-9b3d-4492f2631620" />
  </li>
</ul>
</details>

<details>
<summary>본사</summary>
<ul>
  <li>본사 조회
    <img width="1323" alt="image" src="https://github.com/user-attachments/assets/06b343a7-ae7b-4810-b4d2-8e5a54afbee7" />
    <img width="1295" alt="image" src="https://github.com/user-attachments/assets/f7fc0a87-c968-4269-a5e5-fce93efd8126" />
  </li>
  <li>본사 정보 수정
    <img width="1339" alt="image" src="https://github.com/user-attachments/assets/e1809bf5-21b4-4223-b942-3bf7068d1845" />
    <img width="1318" alt="image" src="https://github.com/user-attachments/assets/d4a36648-02e3-4b7e-911c-b001a71f3da0" />
  </li>
</ul>
</details>

<details>
<summary>본사 재고</summary>
<ul>
  <li>본사의 재료 목록 조회
    <img width="1337" alt="image" src="https://github.com/user-attachments/assets/81d0a9a1-f5e0-43f6-93b0-b687cf0f6450" />
    <img width="1328" alt="image" src="https://github.com/user-attachments/assets/200f6f2d-b122-4a26-ac41-f2fa858fd941" />
  </li>
  <li>본사 재고 등록 
    <img width="1326" alt="image" src="https://github.com/user-attachments/assets/8d451b05-d2cd-4020-9a19-bd17c4da5fa1" />
    <img width="1308" alt="image" src="https://github.com/user-attachments/assets/847d27d1-df9e-4cfe-a9e0-389609a4c513" />
  </li>
  <li>본사 재고 수정
    <img width="1306" alt="image" src="https://github.com/user-attachments/assets/035fb225-eaf5-4c9b-b89a-ad876a8e826f" />
    <img width="1325" alt="image" src="https://github.com/user-attachments/assets/8c5c2ffd-2f09-424d-b13c-235c6999fbaa" />
  </li>
  <li>본사 재고 삭제
    <img width="1342" alt="image" src="https://github.com/user-attachments/assets/45f2d9f9-a588-485e-913d-d5b1f2748c69" />
    <img width="1319" alt="image" src="https://github.com/user-attachments/assets/53e1f84a-4393-4131-b5c5-b0d9a38fd71d" />
  </li>
  <li>본사 재고 전체 조회 
    <img width="1324" alt="image" src="https://github.com/user-attachments/assets/4774fae9-195b-488b-8660-a8990b91ce66" />
    <img width="1308" alt="image" src="https://github.com/user-attachments/assets/31f7978d-5870-4584-afe6-bc2ee60a9dc9" />
  </li>
  <li>본사 재고 단건 조회 
    <img width="1316" alt="image" src="https://github.com/user-attachments/assets/f12e2c32-575c-4ca0-b1be-9d48a8a103fe" />
    <img width="1299" alt="image" src="https://github.com/user-attachments/assets/052608a9-64d8-4c78-a197-975df458e174" />
  </li>
</ul>
</details>

<details>
<summary>재료</summary>
<ul>
  <li>재료 전체 조회 
    <img width="1310" alt="image" src="https://github.com/user-attachments/assets/9cd2f7b6-f0d9-4ae4-ae1e-47559701fb0d" />
    <img width="1316" alt="image" src="https://github.com/user-attachments/assets/81034ad0-7080-4389-81ef-e7637da84db7" />
  </li>
</ul>
</details>

<details>
<summary>지역</summary>
<ul>
  <li>구/군 목록 조회
  
  
  </li>
  <li>시/도 목록 조회 
  
  
  </li>
  <li>지역별 가맹점 조회 
  
  
  </li>
</ul>
</details>

<details>
<summary>카테고리</summary>
<ul>
  <li>카테고리 전체 조회 
    <img width="1332" alt="image" src="https://github.com/user-attachments/assets/c917e280-5008-4b4f-90e7-811e4a8041fe" />
    <img width="1295" alt="image" src="https://github.com/user-attachments/assets/7ae88b1f-3a14-4dc0-8ea8-b96b9bc2573c" />
  </li>
  <li>카테고리의 메뉴 전체 조회 
    <img width="1324" alt="image" src="https://github.com/user-attachments/assets/a1ae6587-3691-4199-a0aa-797104c8bc04" />
    <img width="1318" alt="image" src="https://github.com/user-attachments/assets/eb40c785-7796-4837-b60a-1c4749201fc9" />
  </li>
</ul>
</details>

<details>
<summary>회원</summary>
<ul>
  <li>가맹점주 계정 목록 확인
  
  
  </li>
  <li>본사 등록 계정 수정 
  
  
  </li>
  <li>가맹점 담당자 계정 목록 확인 
  
  
  </li>
  <li>본사 가맹점주 계정 등록
  
  
  </li>
  <li>본사 등록 계정 조회
  
  
  </li>
  <li>본사 생성 계정 전체 조회
  
  
  </li>
  <li>개인 계정 비밀번호 변경 
  
  
  </li>
  <li>개인 계정 정보 변경
  
  
  </li>
  <li>개인 계정 조회
  
  
  </li>
  <li>본사 가맹점 담당자 계정 등록
  
  
  </li>
  <li>AccessToken 재발급
  
  
  </li>
  <li>로그아웃
  
  
  </li>
  <li>로그인
    <img width="1339" alt="image" src="https://github.com/user-attachments/assets/00884264-d258-48b2-9516-69c7060d600d" />
    <img width="1303" alt="image" src="https://github.com/user-attachments/assets/fa289af6-d953-4817-a8fa-688c826aa602" />  
  </li>
  <li>본사 등록 계정 삭제 
    
  
  </li>
</ul>
</details>
