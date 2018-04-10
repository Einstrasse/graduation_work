# sqlite-test
sqlite DBMS를 테스트하기 위한 디렉토리입니다.

## 1. sqlite-cli
다음 명령어로 sqlite-cli를 실행할 수 있다. 다만 db파일에 대한 읽기 쓰기 권한이 있어야지 필요한 SQL 쿼리를 모두 사용할 수 있다.

```
sqlite3 [DB 파일명]
```
데이터베이스 리스트는 다음 명령어로 확인 가능하다.
```
sqlite> .databases
```

테이블 리스트는 다음 명령어로 확인 가능하다.
```
sqlite> .tables
```

도움말은 다음 명령어로 확인 가능하다.
```
sqlite> .help
```

SQL 쿼리를 입력해서 결과를 바로 확인 가능하다.

## 2. 테이블 설계
사용되는 테이블의 생성 SQL 구문은 다음과 같다.
```

CREATE TABLE temporal_alarm (
    id      INTEGER PRIMARY KEY AUTOINCREMENT
                    NOT NULL,
    tag     VARCHAR,
    hour    INT     NOT NULL
                    DEFAULT (8),
    min     INT     NOT NULL
                    DEFAULT (0),
    enabled CHAR    NOT NULL
                    DEFAULT (1) 
);

CREATE TABLE weekly_alarm (
    id      INTEGER PRIMARY KEY AUTOINCREMENT
                    NOT NULL,
    name    VARCHAR,
    hour    INTEGER NOT NULL
                    DEFAULT (8),
    min     INTEGER NOT NULL
                    DEFAULT (0),
    day     INTEGER NOT NULL
                    DEFAULT (31),
    enabled CHAR    NOT NULL
                    DEFAULT (1) 
);

```
temporal_alarm 테이블은 일시적으로 울리는 알람에 대한 테이블이다.
weekly_alarm 테이블은 매 주 특정 요일에 대하여 울리는 알람에 대한 테이블이다. day 값이 어떤 요일에 울릴지를 나타내는 0~127 까지의 값이며, 1이 월요일, 2가 화요일 4가 수요일 8이 목요일 16이 금요일 32가 토요일 64가 일요일을 나타낸다.