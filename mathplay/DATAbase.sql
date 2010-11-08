--DROPS--
DROP TABLE playerinfo;
DROP TABLE user_roles;
DROP TABLE roles;
DROP TABLE student_teacher;
DROP TABLE challenge_type;
DROP TABLE challenge_teacher;
DROP TABLE challenge_student;
DROP TABLE challenge_name;
DROP TABLE challenge;
DROP TABLE users;



--Brukertabell: brukernavn, passord--
CREATE TABLE users(
    user_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    username VARCHAR(20),
    password VARCHAR(20),
    name VARCHAR(50)
);

--Rolletabell: forskjellige roller--
CREATE TABLE roles(
    rolename VARCHAR(20) PRIMARY KEY
);

--Bruker/Roller: koblingstabell--
CREATE TABLE user_roles(
    username VARCHAR(20),
    rolename VARCHAR(20),
    CONSTRAINT user_roles_pk PRIMARY KEY(username, rolename)
);

--Student/L?rer: koblingstabell--
CREATE TABLE student_teacher(
    user_id INT,
    username VARCHAR(20),
    CONSTRAINT student_teacher_pk PRIMARY KEY(user_id, username)
);

--Oppgavetabell: oppgaveinfo--
CREATE TABLE challenge(
    challenge_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    challenge_text VARCHAR(20),
    answer DOUBLE,
    difficulty INT
);

--Oppgavetype: forskjellige type oppgaver--
CREATE TABLE challenge_name(
    challenge_type VARCHAR(20) PRIMARY KEY
);

--Oppgave/Type: koblingstabell--
CREATE TABLE challenge_type(
    challenge_id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    challenge_type VARCHAR(20),
    CONSTRAINT challenge_type_pk PRIMARY KEY(challenge_id, challenge_type)
);

--Oppgave/L?rer: koblingstabell--
CREATE TABLE challenge_teacher(
    challenge_id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    user_id INT,
    CONSTRAINT challenge_teacher_pk PRIMARY KEY(challenge_id, user_id)
);

--Oppgave/Student: koblingstabell--
CREATE TABLE challenge_student(
    challenge_id INT,
    user_id INT,
    CONSTRAINT challenge_student_pk PRIMARY KEY(challenge_id, user_id)
);

--Spillerinto: Valutta osv--
CREATE TABLE playerinfo(
    user_id INT,
    addition_score INT,
    subtraction_score INT,
    multiplication_score INT,
    division_score INT,
    currency_spent INT
);

--Fremmedn?kler--
--Koblingstabeller--
--ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk1 FOREIGN KEY(username) REFERENCES users;--
ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk2 FOREIGN KEY(rolename) REFERENCES roles;
ALTER TABLE challenge_type ADD CONSTRAINT challenge_type_fk1 FOREIGN KEY(challenge_id) REFERENCES challenge;
ALTER TABLE challenge_type ADD CONSTRAINT challenge_type_fk2 FOREIGN KEY(challenge_type) REFERENCES 
challenge_name;
ALTER TABLE challenge_teacher ADD CONSTRAINT challenge_teacher_fk1 FOREIGN KEY(challenge_id) REFERENCES 
challenge;
ALTER TABLE challenge_teacher ADD CONSTRAINT challenge_teacher_fk2 FOREIGN KEY(user_id) REFERENCES users;
ALTER TABLE student_teacher ADD CONSTRAINT student_teacher_fk1 FOREIGN KEY(user_id) REFERENCES users;
--ALTER TABLE student_teacher ADD CONSTRAINT student_teacher_fk2 FOREIGN KEY(username) REFERENCES challenges;--
ALTER TABLE challenge_student ADD CONSTRAINT challenge_student_fk1 FOREIGN KEY(challenge_id) REFERENCES 
challenge;
ALTER TABLE challenge_student ADD CONSTRAINT challenge_student_fk2 FOREIGN KEY(user_id) REFERENCES users;
--STANDALONES!--
ALTER TABLE playerinfo ADD CONSTRAINT playerinfo_fk1 FOREIGN KEY(user_id) REFERENCES users;

--INSERTS--
--Legger til brukere--
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('stian', 'hei', 'Stian Sorebo');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('andriod', 'noob', 'Andrej Skog');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('tomcat', 'tomcat','TOMCATZ CATZ');
--Lager roller--
INSERT INTO ROLES (ROLENAME) VALUES ('admin');
INSERT INTO ROLES (ROLENAME) VALUES ('user');
INSERT INTO ROLES (ROLENAME) VALUES ('manager');
--Binder brukere til roller--
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('stian', 'admin');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('andriod', 'user');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('tomcat', 'manager');
--Lager oppgavetyper--
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Division');
--Lager oppgaver--
--Binder oppgave til oppgavetype--
--Binder oppgave til l?rer--
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2+2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2+4?', 6.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('1+19', 20.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('1+20', 21.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('1+25', 26.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3-2?', 1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6-2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 111-2?', 109.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 1111-2?', 1109.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4/2?', 2.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 8/2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100/2?', 50.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 200/2?', 100.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4*2?', 8.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100*2?', 200.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 200*2?', 400.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);


--Binder oppgave til student og setter gjort--
INSERT INTO challenge_STUDENT (challenge_id, user_id) VALUES (1, 1);
--Binder L?rer til student--
INSERT INTO STUDENT_TEACHER (USER_ID, USERNAME)VALUES (1, 'andriod');
--Setter player score--
INSERT INTO playerinfo (USER_ID, ADDITION_SCORE, SUBTRACTION_SCORE, MULTIPLICATION_SCORE, DIVISION_SCORE, CURRENCY_SPENT) VALUES (2, 0, 0, 0, 0, 0);