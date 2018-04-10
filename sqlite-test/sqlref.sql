
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
