--DROPS--
DROP TABLE user_achievements;
DROP TABLE achievements;
DROP TABLE playerinfo;
DROP TABLE user_roles;
DROP TABLE roles;
DROP TABLE student_teacher;
DROP TABLE challenge_type;
DROP TABLE challenge_teacher;
DROP TABLE challenge_student;
DROP TABLE challenge_name;
DROP TABLE challenge_tips;
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

CREATE TABLE challenge_tips(
    challenge_id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
    challenge_tips VARCHAR(100),
    CONSTRAINT challenge_tips_pk PRIMARY KEY(challenge_id, challenge_tips)
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

--Achievements: Inneholder alle ach--
CREATE TABLE achievements(
	achievement_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), 
	achievement_name VARCHAR(40),
	achievement_text VARCHAR(60),
	achievement_img VARCHAR(30),
	achievement_value int

);

--Achievements: koblingstabell mellom user og ach--
CREATE TABLE user_achievements(
	user_id INTEGER,
	achievement_id INT,
	achievement_status int,
	achievement_commpleted SMALLINT,
	CONSTRAINT user_achievements_pk PRIMARY KEY(user_id, achievement_id)
);

--Fremmednøkler--
--Koblingstabeller--
--ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk1 FOREIGN KEY(username) REFERENCES users;--
ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk2 FOREIGN KEY(rolename) REFERENCES roles;
ALTER TABLE challenge_type ADD CONSTRAINT challenge_type_fk1 FOREIGN KEY(challenge_id) REFERENCES challenge;
ALTER TABLE challenge_type ADD CONSTRAINT challenge_type_fk2 FOREIGN KEY(challenge_type) REFERENCES challenge_name;
ALTER TABLE challenge_tips ADD CONSTRAINT challenge_tips_fk1 FOREIGN KEY(challenge_id) REFERENCES challenge;
ALTER TABLE challenge_teacher ADD CONSTRAINT challenge_teacher_fk1 FOREIGN KEY(challenge_id) REFERENCES challenge;
ALTER TABLE challenge_teacher ADD CONSTRAINT challenge_teacher_fk2 FOREIGN KEY(user_id) REFERENCES users;
ALTER TABLE student_teacher ADD CONSTRAINT student_teacher_fk1 FOREIGN KEY(user_id) REFERENCES users;
--ALTER TABLE student_teacher ADD CONSTRAINT student_teacher_fk2 FOREIGN KEY(username) REFERENCES challenges;--
ALTER TABLE challenge_student ADD CONSTRAINT challenge_student_fk1 FOREIGN KEY(challenge_id) REFERENCES
challenge;
ALTER TABLE challenge_student ADD CONSTRAINT challenge_student_fk2 FOREIGN KEY(user_id) REFERENCES users;

ALTER TABLE user_achievements ADD CONSTRAINT user_achievements_fk1 FOREIGN KEY(user_id) REFERENCES users;
ALTER TABLE user_achievements ADD CONSTRAINT user_achievements_fk2 FOREIGN KEY(achievement_id) REFERENCES achievements;

--STANDALONES!--
ALTER TABLE playerinfo ADD CONSTRAINT playerinfo_fk1 FOREIGN KEY(user_id) REFERENCES users;

--INSERTS--
--Legger til brukere--
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('supah', '1337','Mr.Cant Touch This');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('tomcat', 'tomcat','TOMCATZ CATZ');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('stian', 'hei', 'Stian Sorebo');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('rivertz', 'hei', 'Hans Jakob Rivertz');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('andriod', 'noob', 'Andrej Skog');
INSERT INTO USERS (USERNAME, PASSWORD, NAME) VALUES ('paal', 'paal', 'Pål Møst');
--Lager roller--
INSERT INTO ROLES (ROLENAME) VALUES ('admin');
INSERT INTO ROLES (ROLENAME) VALUES ('user');
INSERT INTO ROLES (ROLENAME) VALUES ('manager');
INSERT INTO ROLES (ROLENAME) VALUES ('superadmin');
--Binder brukere til roller--
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('supah', 'superadmin');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('tomcat', 'manager');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('stian', 'admin');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('rivertz', 'admin');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('andriod', 'user');
INSERT INTO USER_ROLES (USERNAME, ROLENAME)VALUES ('paal', 'user');
--Lager oppgavetyper--
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_NAME (challenge_TYPE)VALUES ('Division');

--Binder student til lerer--
INSERT INTO STUDENT_TEACHER (USER_ID, USERNAME)VALUES (3, 'andriod');
INSERT INTO STUDENT_TEACHER (USER_ID, USERNAME)VALUES (4, 'paal');

--Setter player score--
INSERT INTO playerinfo (USER_ID, ADDITION_SCORE, SUBTRACTION_SCORE, MULTIPLICATION_SCORE, DIVISION_SCORE, CURRENCY_SPENT) VALUES (5, 0, 0, 0, 0, 0);
INSERT INTO playerinfo (USER_ID, ADDITION_SCORE, SUBTRACTION_SCORE, MULTIPLICATION_SCORE, DIVISION_SCORE, CURRENCY_SPENT) VALUES (6, 1, 1, 1, 1, 2);

--Lager oppgaver--
--Binder oppgave til oppgavetype--
--Binder oppgave til l?rer--
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2+2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 5 og større enn 3');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2+4?', 6.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 4 + 2 = 6');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('1+19', 20.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 1 + 18 = 19');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('1+20', 21.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 21 - 1 = 20');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('1+25', 26.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mellom 25 og 30');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3-2?', 1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 3 - 1 = 2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6-2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 5 og større enn 3');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 111-2?', 109.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 110 og større enn 105');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 1111-2?', 1109.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 111 - 2 = 109');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4/2?', 2.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Det er mindre enn 4 og større enn 1');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 8/2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 10 / 2 = 5');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100/2?', 50.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 50 * 2 = 100');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 200/2?', 100.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Deler man en positivt tall på 2, blir tallet halvparten så stort');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Det er mindre enn 5 og større enn 3');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4*2?', 8.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Det er mindre enn 10 og større enn 6');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100*2?', 200.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 100 * 1 = 100');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 200*2?', 400.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Hvis man ganger et positivt tall med 2, blir tallet dobbelt så stort');

--subtraksjon, grad 1--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3-4?', -1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er negativt, det vil si mindre enn 0');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10-1?', 9.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4+5');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 8-4?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 7-3?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 3+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6-4?', 2.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 1+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9-3?', 6.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 3+3');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 8-1?', 7.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4+3');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6-5?', 1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er høyere enn 0, men lavere enn 2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 5-1?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 5-6?', -1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er negativt, det vil si mindre enn 0');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 7-4?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+1');

--Subtraksjon, grad 2--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 24-16?', 8.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4+4');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 39-13?', 26.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 23+3');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 21-7?', 14.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10+4');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 49-16?', 33.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 30+3');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 29-2?', 27.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 26+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 50-16?', 34.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 30+4');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 30-6?', 24.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som antall luker i julekalenderen');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 13-1?', 12.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som antall måneder i ett år');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 38-2', 36.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 36.0');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 32-16?', 16.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 8+8');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 45-15?', 30.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 15+15');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 24-16?', 8.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4+4');

--subtraksjon, grad3

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100-16?', 84.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er 84');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 82-11?', 71.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 70+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 99-100?', -1.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er negativt/mindre enn null');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 87-12?', 75.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 70+5');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 75-19?', 56.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er 56');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100-16?', 84.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er 84');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 67-2?', 65.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 64+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 93-16?', 77.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 76+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 70-10?', 60.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er 60');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 66-54?', 12.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 53-51?', 2.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Subtraction');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 1+1');

--Divisjon, grad 1--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9/3?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4-1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10/2?', 5.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 3+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6/2?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4/2?', 2.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4-2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6/1?', 6.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 6-0');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9/9?', 1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 8+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4/1?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4-0');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 5/1?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10/10?', 1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10-9');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9/1?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 5+4');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2/1?', 1.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 0+1');

--Divisjon, grad 2--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 44/2?', 21.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 20+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 22/11?', 2.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 1+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 44/22?', 2.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 1+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 50/10?', 5.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 3+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 27/3?', 9.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10-1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 33/3?', 11.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 45/15?', 3.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 18/6?', 3.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 44/2?', 21.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 20+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 36/4?', 9.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 8+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 48/4?', 12.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10+2');

--Divisjon, grad 3--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 63/7?', 9.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10-1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 88/11?', 8.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 4+4');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 72/6?', 12.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 8+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 80/8?', 10.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 9+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 99/33?', 3.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 7-4');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 88/22?', 4.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 2+2');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 77/7?', 11.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100/5?', 20.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 10+10');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 100/50?', 2.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 1+1');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 90/3?', 30.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 15+15');

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 88/44?', 2.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Division');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 1+1');

--Addisjon, grad 1--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2+7?', 9.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 10 og større enn 8');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 1+2?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 4 og større enn 1');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6+2?', 8.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 9 og større enn 7');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 7+2?', 9.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 10 og større enn 7');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er -2+2?', 0.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 2 og større enn -2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9+9?', 18.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 10 + 10 = 20');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 8+8?', 16.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 9 + 9 = 18');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 7+9?', 16.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 9 + 7 = 16');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 1+9?', 10.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 11 + 9 = 20');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10+9?', 19.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 8 + 11');

--Addisjon, grad 2 --

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 25+25?', 50.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 20');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 35+35?', 70.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 40 + 30');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 20+23?', 43.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 19 + 24');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3+31?', 34.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 4 + 30');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 25+1?', 26.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 20 + 6');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 49+37?', 86.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er 86!');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 43+43?', 86.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 42 + 42 = 84');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 31+11?', 42.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 30 + 10 = 40');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 44+44?', 88.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 80 + 8');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 19+19?', 38.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 8');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 45+15?', 60.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 30');

--Addisjon, grad 3--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 55+15?', 70.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 40');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 55+1?', 56.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 16 + 40');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 99+12?', 111.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 100 + 11');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 87+4?', 91.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 61');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 14+73?', 87.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 40 + 47');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 61+72?', 133.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 100 + 33');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 52+9?', 61.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 31');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 76+34?', 110.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 40 + 40');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 33+51?', 84.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 44 + 40');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 55+55?', 110.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 30 + 40 + 40');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 62+2?', 64.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 32 + 32');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 51+85?', 136.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Addition');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 36 + 40 + 50 + 10');

--Multiplikasjon, grad 1--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*1?', 2.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 2+0');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*2?', 4.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 2+2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*3?', 6.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 2+4');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*4?', 8.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 2+6');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 2*5?', 10.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 12-2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3*1?', 3.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 2+1');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4*1?', 2.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 4+0');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3*2?', 6.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 8-2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 4*4?', 16.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 18-2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 3*3?', 9.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Svaret er det samme som 9/1');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 5*5?', 25.0, 1);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: Antall øl i en kasse - 1');

--Multiplikasjon grad 2--

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6*10?', 60.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 3 * 20 = 60');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6*10?', 60.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 3 * 20 = 60');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 5*10?', 50.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 2 * 25 = 50');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6*6?', 36.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 5 * 6 = 30');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 6*7?', 42.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 7 * 5 = 35');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10*10?', 100.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 10 * 1 = 10');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 7*7?', 49.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 8 * 8 = 64');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9*9?', 81.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 84 og større enn 78');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 9*8?', 72.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 74 og større enn 70');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10*9?', 90.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 92 og større enn 88');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 1*9?', 9.0, 2);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 10 og større enn 7');

--Multiplikasjon, grad 3 --

INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 11*9?', 99.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 100 og større enn 97');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 12*9?', 108.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 110 og større enn 106');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 15*4?', 60.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 62 og større enn 58');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 15*15?', 225.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 230 og større enn 220');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 14*2?', 28.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er en kasse med øl + 4');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 13*3?', 39.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Eksempel: 14 * 4 = 43');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 12*12?', 144.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er 144');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 11*5?', 55.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 57-2');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 10*14?', 140.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er mindre enn 150 og større enn 130');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 13*13?', 169.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 158 + 11');
INSERT INTO challenge (challenge_TEXT, ANSWER, DIFFICULTY)VALUES ('Hva er 14*5?', 70.0, 3);
INSERT INTO challenge_TYPE (challenge_TYPE)VALUES ('Multiplication');
INSERT INTO challenge_TEACHER (USER_ID)VALUES (1);
INSERT INTO challenge_TIPS(challenge_tips)VALUES('Hjelp: Svaret er det samme som 40+30');

--Legger inn achievements--
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Addisjon 10 oppgaver', 'Du har løst 10 addisjonsoppgaver', 'add10', 10);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Addisjon 25 oppgaver', 'Du har løst 25 addisjonsoppgaver', 'add25', 25);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Addisjon 50 oppgaver', 'Du har løst 50 addisjonsoppgaver', 'add50', 50);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Addisjon 100 oppgaver', 'Du har løst 100 addisjonsoppgaver', 'add100', 100);

INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Subtraksjon 10 oppgaver', 'Du har løst 10 subtraksjonsoppgaver', 'sub10', 10);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Subtraksjon 25 oppgaver', 'Du har løst 25 subtraksjonsoppgaver', 'sub25', 25);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Subtraksjon 50 oppgaver', 'Du har løst 50 subtraksjonsoppgaver', 'sub50', 50);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Subtraksjon 100 oppgaver', 'Du har løst 100 subtraksjonsoppgaver', 'sub100', 100);

INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Multiplikasjon 10 oppgaver', 'Du har løst 10 multiplikasjonsoppgaver', 'mul10', 10);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Multiplikasjon 25 oppgaver', 'Du har løst 25 multiplikasjonsoppgaver', 'mul25', 25);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Multiplikasjon 50 oppgaver', 'Du har løst 50 multiplikasjonsoppgaver', 'mul50', 50);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Multiplikasjon 100 oppgaver', 'Du har løst 100 multiplikasjonsoppgaver', 'mul100', 100);

INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Divisjon 10 oppgaver', 'Du har løst 10 divisjonsoppgaver', 'div10', 10);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Divisjon 25 oppgaver', 'Du har løst 25 divisjonsoppgaver', 'div25', 25);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Divisjon 50 oppgaver', 'Du har løst 50 divisjonsoppgaver', 'div50', 50);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Divisjon 100 oppgaver', 'Du har løst 100 divisjonsoppgaver', 'div100', 100);

INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('10 på rad', 'Du har løst 10 oppgaver på rad', 'rad10', 10);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('25 på rad', 'Du har løst 25 oppgaver på rad', 'rad25', 25);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('50 på rad', 'Du har løst 50 oppgaver på rad', 'rad50', 50);
INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('100 på rad', 'Du har løst 100 oppgaver på rad', 'rad100', 100);

INSERT INTO achievements(achievement_name, achievement_text, achievement_img, achievement_value) VALUES ('Under opplæring', 'Du har løst 1 oppgave', '1opg', 5);