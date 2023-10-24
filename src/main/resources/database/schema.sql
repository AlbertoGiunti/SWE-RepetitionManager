-- Schema that represents the database structure
-- Syntax: SQLite

-- Drop tables if they already exist
DROP TABLE IF EXISTS tutors;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS lessonsTags;

-- Table: tutors
CREATE TABLE IF NOT EXISTS tutors
(
    cf          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    iban        TEXT NOT NULL
);

-- Table: students
CREATE TABLE IF NOT EXISTS students
(
    cf          TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    level       TEXT NOT NULL
);

-- Table: lessons
CREATE TABLE IF NOT EXISTS lessons
(
    idLesson        INTEGER PRIMARY KEY AUTOINCREMENT,
    tutorCF         TEXT NOT NULL,
    title           TEXT NOT NULL,
    description     TEXT,
    startTime       TEXT NOT NULL,
    endTime         TEXT NOT NULL,
    price           FLOAT NOT NULL CHECK(price >= 0),
    state           TEXT NOT NULL,
    stateExtraInfo  TEXT,
    FOREIGN KEY (tutorCF) REFERENCES tutors (cf) ON UPDATE CASCADE ON DELETE CASCADE
);


-- Table: tags
CREATE TABLE IF NOT EXISTS tags
(
    idTag       INTEGER PRIMARY KEY AUTOINCREMENT,
    tag         TEXT NOT NULL,
    tagType     TEXT NOT NULL
);

-- Table: lessonsTags
CREATE TABLE IF NOT EXISTS lessonsTags
(
    idTag       INTEGER NOT NULL,
    idLesson    INTEGER NOT NULL,
    PRIMARY KEY (idTag, idLesson),
    FOREIGN KEY (idTag) REFERENCES tags (idTag) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (idLesson) REFERENCES lessons (idLesson) ON UPDATE CASCADE ON DELETE CASCADE
);