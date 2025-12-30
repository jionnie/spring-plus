# 📝 API 명세서

## 1. USER API
- 모든 API는 JWT 인증 필요
    - `/users`: 일반 사용자 권한 필요
    - `/admin/users`: 관리자 권한 필요
- 비밀번호 규칙: 최소 8자, 숫자 포함, 대문자 포함

### ✅ 사용자 정보 조회

- **URL**: `/users/{userId}`
- **Method**: `GET`
- **Auth**: 필요
- **Description**: 특정 사용자의 상세 정보를 조회하는 API

### Request

| Path Variable | Type | Description       |
|---------------|------|-----------------|
| userId        | Long | 조회할 사용자 ID |

### Response (200 OK)

```json
{
  "id": 1,
  "email": "example@example.com"
}
```
| Field | Type   | Description |
| ----- | ------ |-------------|
| id    | Long   | 사용자 ID      |
| email | String | 사용자 이메일     |

| Code | Description  |
| ---- | ------------ |
| 200  | 조회 성공        |
| 404  | 사용자가 존재하지 않음 |

### ✅ 비밀번호 변경
- **URL**: ```/users```

- **Method**: ```PUT```

- **Auth**: 필요

- **Description**: 특정 사용자의 비밀번호를 변경하는 API

### Request Body
```json
{
  "oldPassword": "OldPassword1234",
  "newPassword": "NewPassword1234"
}
```
| Field       | Type   | Description                |
| ----------- | ------ | -------------------------- |
| oldPassword | String | 기존 비밀번호                    |
| newPassword | String | 새 비밀번호 (8자 이상, 숫자와 대문자 포함) |

### Response
- **Body**: 없음

| Code | Description                             |
| ---- | --------------------------------------- |
| 200  | 비밀번호 변경 성공                              |
| 400  | 비밀번호 검증 실패 (규칙 불일치, 기존과 동일, 잘못된 비밀번호 등) |
| 404  | 사용자 없음                                  |

### ✅ 닉네임 검색
- **URL**: ```/users```

- **Method**: ```GET```

- **Auth**: 필요

- **Description**: 특정 닉네임을 포함하는 사용자 리스트를 조회하는 API

### Request Parameters
| Query Parameter | Type   | Description |
| --------------- | ------ | ----------- |
| nickName        | String | 검색할 닉네임 키워드 |


### Response (200 OK)
```json
[
  {
    "id": 1,
    "email": "example1@example.com"
  },
  {
    "id": 2,
    "email": "example2@example.com"
  }
]
```
| Field | Type   | Description |
| ----- | ------ | ----------- |
| id    | Long   | 사용자 ID      |
| email | String | 사용자 이메일     |

| Code | Description |
| ---- | ----------- |
| 200  | 조회 성공       |
| 404  | 사용자 없음      |

### ✅ 사용자 권한 변경 (Admin 전용)
- **URL**: ```/admin/users/{userId}```

- **Method**: ```PATCH```

- **Auth**: 관리자 권한 필요

- **Description**: 특정 사용자의 권한을 변경하는 API

### Request
| Path Variable | Type | Description |
| ------------- | ---- | ----------- |
| userId        | Long | 변경할 사용자 ID  |


### Request Body
```json
{
  "role": "ADMIN"
}
```
| Field | Type   | Description           |
| ----- | ------ | --------------------- |
| role  | String | 변경할 역할 (ADMIN / USER) |


### Response
- **Body**: 없음

| Code | Description   |
| ---- | ------------- |
| 200  | 권한 변경 성공      |
| 400  | 유효하지 않은 역할 요청 |
| 404  | 사용자 존재하지 않음   |

## 2. TODO API

- 모든 API는 JWT 인증 필요

### ✅ Todo 등록

- **URL:** `/todos`
- **Method:** `POST`
- **Auth:** 필요
- **Description:** 새로운 Todo를 등록하는 API

### Request Body
```json
{
  "title": "오늘 할 일",
  "contents": "코드 리뷰하기"
}
```
| Field    | Type   | Description |
| -------- | ------ | ----------- |
| title    | String | Todo 제목     |
| contents | String | Todo 내용     |

### Response (200 OK)
```json
{
    "id": 1,
    "title": "오늘 할 일",
    "contents": "코드 리뷰하기",
    "weather": "맑음",
    "user": {
        "id": 1,
        "email": "example@example.com"
    }
}
```
| Field      | Type   | Description |
| ---------- | ------ | ----------- |
| id         | Long   | Todo ID     |
| title      | String | Todo 제목     |
| contents   | String | Todo 내용     |
| weather    | String | 등록 시 날씨 정보  |
| user       | Object | Todo 작성자 정보 |
| user.id    | Long   | 작성자 ID      |
| user.email | String | 작성자 이메일     |

| Code | Description |
| ---- | ----------- |
| 200  | 등록 성공       |
| 400  | 유효하지 않은 요청  |

### ✅ Todo 리스트 조회

- **URL**: ```/todos```

- **Method**: ```GET```

- **Auth**: 필요

- **Description**: Todo 목록을 페이징 조회하는 API

### Request Parameters
| Query Parameter | Type     | Description          |
| --------------- | -------- | -------------------- |
| page            | int      | 페이지 번호 (default 1)   |
| size            | int      | 페이지 사이즈 (default 10) |
| weather         | String   | 날씨 필터 (optional)     |
| start           | DateTime | 조회 시작 시간 (optional)  |
| end             | DateTime | 조회 종료 시간 (optional)  |

### Response (200 OK)
```json
{
    "content": [
        {
        "id": 1,
        "title": "오늘 할 일",
        "contents": "코드 리뷰하기",
        "weather": "맑음",
        "user": {
            "id": 1,
            "email": "example@example.com"
        },
        "createdAt": "2025-12-29T09:00:00",
        "modifiedAt": "2025-12-29T10:00:00"
        }
    ],
    "pageable": {},
    "totalPages": 5,
    "totalElements": 50
}
```
| Field      | Type     | Description |
| ---------- | -------- | ----------- |
| content    | Array    | Todo 리스트    |
| id         | Long     | Todo ID     |
| title      | String   | Todo 제목     |
| contents   | String   | Todo 내용     |
| weather    | String   | 등록 시 날씨 정보  |
| user.id    | Long     | 작성자 ID      |
| user.email | String   | 작성자 이메일     |
| createdAt  | DateTime | 등록 시간       |
| modifiedAt | DateTime | 수정 시간       |

### ✅ Todo 단일 조회

- **URL**: ```/todos/{todoId}```

- **Method**: ```GET```

- **Auth**: 필요

- **Description**: 특정 Todo의 상세 정보를 조회하는 API

### Request
| Path Variable | Type | Description |
| ------------- | ---- | ----------- |
| todoId        | Long | 조회할 Todo ID |

### Response (200 OK)
```json
{
    "id": 1,
    "title": "오늘 할 일",
    "contents": "코드 리뷰하기",
    "weather": "맑음",
    "user": {
        "id": 1,
        "email": "example@example.com"
    },
    "createdAt": "2025-12-29T09:00:00",
    "modifiedAt": "2025-12-29T10:00:00"
}
```
| Code | Description |
| ---- | ----------- |
| 200  | 조회 성공       |
| 404  | Todo 없음     |

### ✅ Todo 리스트 조회
- **URL**: ```/todos/search```

- **Method**: ```GET```

- **Auth**: 필요

- **Description**: 제목, 매니저 닉네임, 생성일 기준으로 Todo를 페이징 조회하는 API

### Request Parameters
| Query Parameter | Type     | Description          |
|-----------------| -------- | -------------------- |
| keyword         | String   | 제목 키워드 (optional)    |
| nickname        | String   | 매니저 닉네임 (optional)   |
| startDate       | DateTime | 조회 시작 시간 (optional)  |
| endDate         | DateTime | 조회 종료 시간 (optional)  |
| page            | int      | 페이지 번호 (default 1)   |
| size            | int      | 페이지 사이즈 (default 10) |

### Response (200 OK)
```json
{
  "content": [
    {
      "title": "오늘 할 일",
      "managerCount": 2,
      "commentCount": 3
    }
  ],
  "pageable": {},
  "totalPages": 5,
  "totalElements": 50
}
```
| Field        | Type   | Description |
| ------------ | ------ | ----------- |
| title        | String | Todo 제목     |
| managerCount | Long   | 담당 매니저 수    |
| commentCount | Long   | 댓글 수        |

| Code | Description |
| ---- | ----------- |
| 200  | 조회 성공       |
| 404  | Todo 없음     |

## 3. MANAGER API

- 모든 API는 JWT 인증 필요


### ✅ 담당자 등록

- **URL:** `/todos/{todoId}/managers`
- **Method:** `POST`
- **Auth:** 필요
- **Description:** 특정 Todo에 담당자를 등록하는 API

### Request

| Path Variable | Type | Description       |
|---------------|------|-----------------|
| todoId        | Long | 담당자를 등록할 Todo ID |

### Request Body
```json
{
  "managerUserId": 2
}
```
| Field         | Type | Description   |
| ------------- | ---- | ------------- |
| managerUserId | Long | 등록할 담당자 유저 ID |

### Response (200 OK)
```json
{
  "id": 1,
  "user": {
    "id": 2,
    "email": "manager@example.com"
  }
}
```
| Field      | Type   | Description    |
| ---------- | ------ | -------------- |
| id         | Long   | 등록된 Manager ID |
| user.id    | Long   | 담당자 유저 ID      |
| user.email | String | 담당자 이메일        |

| Code | Description               |
| ---- | ------------------------- |
| 200  | 담당자 등록 성공                 |
| 400  | Todo 작성자가 아니거나 유효하지 않은 요청 |
| 404  | Todo 또는 등록 대상 유저가 존재하지 않음 |

### ✅ 담당자 조회

- **URL**: `/todos/{todoId}/managers`

- **Method**: `GET`

- **Auth**: 필요

- **Description**: 특정 Todo에 등록된 담당자 리스트를 조회하는 API

### Request
| Path Variable | Type | Description |
| ------------- | ---- | ----------- |
| todoId        | Long | 조회할 Todo ID |

### Response(200 Ok)
```json
[
  {
    "id": 1,
    "user": {
      "id": 2,
      "email": "manager1@example.com"
    }
  },
  {
    "id": 2,
    "user": {
      "id": 3,
      "email": "manager2@example.com"
    }
  }
]
```
| Field      | Type   | Description |
| ---------- | ------ | ----------- |
| id         | Long   | Manager ID  |
| user.id    | Long   | 담당자 유저 ID   |
| user.email | String | 담당자 이메일     |

| Code | Description |
| ---- | ----------- |
| 200  | 조회 성공       |
| 404  | Todo 없음     |

### ✅ 담당자 삭제

- **URL**: `/todos/{todoId}/managers/{managerId}`

- **Method**: `DELETE`

- **Auth**: 필요

- **Description**: 특정 Todo에 등록된 담당자를 삭제하는 API

### Request
| Path Variable | Type | Description |
| ------------- | ---- | ----------- |
| todoId        | Long | Todo ID     |
| managerId     | Long | 삭제할 담당자 ID  |

### Response

- **Body**: 없음

| Code | Description               |
| ---- | ------------------------- |
| 200  | 담당자 삭제 성공                 |
| 400  | Todo 작성자가 아니거나 유효하지 않은 요청 |
| 404  | Todo 또는 Manager 존재하지 않음   |

## 4. COMMENT API
- 모든 API는 JWT 인증 필요
- 댓글은 특정 Todo에 대해 작성 및 조회 가능

### ✅ 댓글 작성
- **URL:** `/todos/{todoId}/comments`
- **Method:** `POST`
- **Auth:** 필요
- **Description:** 로그인 된 사용자가 특정 Todo에 댓글을 작성하는 API

### Request

| Path Variable | Type | Description    |
|---------------|------|----------------|
| todoId        | Long | 댓글 작성할 Todo ID |

### Request Body
```json
{
  "contents": "댓글 내용"
}
```

| Field    | Type   | Description |
| -------- | ------ | ----------- |
| contents | String | 댓글 내용 (필수)  |

### Response (200 OK)
```json
{
  "id": 1,
  "contents": "댓글 내용",
  "user": {
    "id": 1,
    "email": "example@example.com"
  }
}
```

| Field      | Type   | Description |
| ---------- | ------ | ----------- |
| id         | Long   | 댓글 ID       |
| contents   | String | 댓글 내용       |
| user       | Object | 댓글 작성자 정보   |
| user.id    | Long   | 사용자 ID      |
| user.email | String | 사용자 이메일     |

| Code | Description |
| ---- | ----------- |
| 200  | 댓글 작성 성공    |
| 404  | Todo 없음     |

### ✅ 댓글 조회

- **URL**: `/todos/{todoId}/comments`

- **Method**: `GET`

- **Auth**: 필요

- **Description**: 특정 Todo에 작성된 댓글 리스트를 조회하는 API

### Request

| Path Variable | Type | Description |
| ------------- | ---- | ----------- |
| todoId        | Long | 조회할 Todo ID |

### Response (200 OK)
```json
[
  {
    "id": 1,
    "contents": "댓글 내용1",
    "user": {
      "id": 1,
      "email": "example1@example.com"
    }
  },
  {
    "id": 2,
    "contents": "댓글 내용2",
    "user": {
      "id": 2,
      "email": "example2@example.com"
    }
  }
]
```
| Field      | Type   | Description |
| ---------- | ------ | ----------- |
| id         | Long   | 댓글 ID       |
| contents   | String | 댓글 내용       |
| user.id    | Long   | 작성자 ID      |
| user.email | String | 작성자 이메일     |

| Code | Description |
| ---- | ----------- |
| 200  | 댓글 조회 성공    |
| 404  | Todo 없음     |

## 5. AUTH API
- 회원가입 및 로그인 기능 제공
- JWT 토큰 발급

### ✅ 회원가입
- **URL:** `/auth/signup`
- **Method:** `POST`
- **Auth:** 불필요
- **Description:** 신규 사용자를 가입시키고 JWT 토큰을 발급하는 API

### Request Body
```json
{
  "nickName": "사용자 닉네임",
  "email": "example@example.com",
  "password": "Password123",
  "userRole": "USER"
}
```
| Field    | Type   | Description         |
| -------- | ------ | ------------------- |
| nickName | String | 사용자 닉네임 (필수)        |
| email    | String | 사용자 이메일 (필수, 중복 불가) |
| password | String | 비밀번호 (필수)           |
| userRole | String | 역할 (USER / ADMIN)   |

### Response (200 OK)
```json
{
  "bearerToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| Field       | Type   | Description |
| ----------- | ------ | ----------- |
| bearerToken | String | 발급된 JWT 토큰  |

| Code | Description |
| ---- | ----------- |
| 200  | 회원가입 성공     |
| 400  | 이미 존재하는 이메일 |

### ✅ 로그인

- **URL**: `/auth/signin`

- **Method**: `POST`

- **Auth**: 불필요

- **Description**: 이메일과 비밀번호로 로그인하고 JWT 토큰을 발급하는 API

### Request Body
```json
{
  "nickName": "사용자 닉네임",
  "email": "example@example.com",
  "password": "Password123"
}
```
| Field    | Type   | Description  |
| -------- | ------ | ------------ |
| nickName | String | 사용자 닉네임 (필수) |
| email    | String | 사용자 이메일 (필수) |
| password | String | 비밀번호 (필수)    |

### Response (200 OK)
```json
{
  "bearerToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
| Field       | Type   | Description |
| ----------- | ------ | ----------- |
| bearerToken | String | 발급된 JWT 토큰  |

| Code | Description |
| ---- | ----------- |
| 200  | 로그인 성공      |
| 401  | 비밀번호 불일치    |
| 404  | 가입되지 않은 유저  |


## 6. LOG API

- 담당자 등록 요청 시 로그 기록 기능 포함
- 로그는 DB에 저장되고 응답은 없음

### ✅ 로그 기록

- **사용처:** 매니저 등록 요청 시 자동 기록
- **Description:** 특정 사용자와 Todo에 대한 액션 로그를 기록하는 API

### 저장되는 로그 정보
boolean success, Long userId, String requestUrl, String httpMethod, String clientIp

| Field      | Type          | Description    |
|------------|---------------|----------------|
| id         | Long          | 로그 ID          |
| success    | boolean       | API 호출 성공 여부   |
| requestUrl | String        | 요청 URL         |
| httpMethod | String        | Http Method 종류 |
| clientIp   | String        | 요청 클라이언트 IP    |
| createdAt  | LocalDateTime | 로그 생성 시각       |
| modifiedAt | LocalDateTime | 로그 수정 시각       |

## 7. HEALTH CHECK API

- **URL:** `/health-check`
- **Method:** `GET`
- **Auth:** 불필요
- **Description:** 서버가 정상적으로 동작하는지 확인하는 API

### Request
- **Body:** 없음
- **Query Parameter:** 없음
- **Path Variable:** 없음

### Response (200 OK)
```JSON
{
  "status": "UP"
}
```

# ERD 설계


# AWS 연동

## Health Check API 동작 확인

<img width="1485" height="624" alt="image" src="https://github.com/user-attachments/assets/fa3995c5-16cf-4799-bf9f-c495602eb6c1" />

## AWS EC2 설정

<img width="1619" height="618" alt="aws ec2-1" src="https://github.com/user-attachments/assets/d5bd4c35-d90b-4e54-b52d-dd17e5ab2b09" />

<img width="1611" height="688" alt="aws ec2-2" src="https://github.com/user-attachments/assets/f5c539c6-ff53-4e93-9419-c54cb804cd3b" />

## AWS RDS 설정

<img width="1608" height="775" alt="rds-1" src="https://github.com/user-attachments/assets/7eb18af6-bb01-49ea-a8db-49bed59bda4d" />

<img width="1593" height="317" alt="rds-2" src="https://github.com/user-attachments/assets/0dfa8e89-25d0-4c04-b0dd-19ca3b8e00fc" />

<img width="1591" height="799" alt="rds-3" src="https://github.com/user-attachments/assets/68da7732-dc92-4daa-ac21-bf9ce81ffb14" />
