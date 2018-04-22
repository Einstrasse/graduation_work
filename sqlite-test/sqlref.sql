# Table Creation

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

# weekly_alarm for Lecture Insert

INSERT INTO weekly_alarm(`name`, `hour`, `min`, `day`)
VALUES("THU, FRI Lecture", 8, 0, 24),
("TUE Lecture", 9, 30, 2),
("MON Lecture", 12, 0, 1),
("WED Lecture", 11, 0, 4);

# POST Request
4 params
- name
- hour
- min
- day

# weekly_alarm update sql query
UPDATE weekly_alarm
SET `name`="asdf", `hour`=8
WHERE `id`=5;

# PUT Request
6 params
- name
- hour
- min
- day
- m_id
- enabled

# Select Today's weekly alaram

SELECT `hour`, `min` FROM weekly_alarm WHERE enabled=1 AND (day & 1) <> 0;
SELECT * FROM weekly_alarm WHERE enabled=1 AND (day & 1) <> 0;