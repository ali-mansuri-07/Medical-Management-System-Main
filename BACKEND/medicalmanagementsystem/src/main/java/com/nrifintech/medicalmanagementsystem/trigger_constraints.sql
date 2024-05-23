CREATE OR REPLACE TRIGGER patient_insert_trigger
BEFORE INSERT ON patient
FOR EACH ROW
BEGIN
    IF :NEW.birth_year >= EXTRACT(YEAR FROM SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20001, 'The attribute must be less than the current date.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER doctor_insert_trigger
BEFORE INSERT ON doctor
FOR EACH ROW
BEGIN
    IF :NEW.experience_start >= EXTRACT(YEAR FROM SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20001, 'The attribute must be less than the current date.');
    END IF;
END;
/



ALTER TABLE PATIENT 
ADD CONSTRAINT PATIENT_EMAIL_CHECK
CHECK (REGEXP_LIKE(email, '[a-zA-Z0-9._%-]+@[a-zA-Z0-9._%-]+\.[a-zA-Z]{2,4}'));

ALTER TABLE DOCTOR 
ADD CONSTRAINT DOCTOR_EMAIL_CHECK
CHECK (REGEXP_LIKE(email, '[a-zA-Z0-9._%-]+@[a-zA-Z0-9._%-]+\.[a-zA-Z]{2,4}'));

-- ALTER TABLE PATIENT 
-- ADD CONSTRAINT PATIENT_CONTACT_CHECK
-- CHECK (REGEXP_LIKE(contact_number, '^(\\+\\d{1,3})?\\s*(\\d{1,4})?\\s*(\\d{1,4})$'));


CREATE INDEX doctor_name_index
ON doctor(name);
CREATE INDEX specialization_name_index
ON specialization(name);
CREATE INDEX doctor_fees_index
ON doctor(fees);
CREATE INDEX doctor_rating_index
ON doctor(rating);
CREATE BITMAP INDEX doctor_gender_index
ON doctor(gender);
CREATE BITMAP INDEX doctor_status_index
ON doctor(status);

CREATE INDEX appointment_date_index
ON appointment(app_date);


CREATE BITMAP INDEX appointment_status_index
ON appointment(app_status);

CREATE BITMAP INDEX appointment_slot_index
ON appointment(slot);


CREATE BITMAP INDEX user_username_password_index
ON "user"(user_name,password);


INSERT INTO "MMS_DB"."ROLE" (ID, ROLE_NAME) VALUES ('1', 'ROLE_ADMIN');
INSERT INTO "MMS_DB"."ROLE" (ID, ROLE_NAME) VALUES ('2', 'ROLE_PATIENT');
INSERT INTO "MMS_DB"."ROLE" (ID, ROLE_NAME) VALUES ('3', 'ROLE_DOCTOR');


-- http://localhost:8083/swagger-ui.html     (API ENDPOINT TO VIEW SWAGGER)



-- write return types for EACH methods
-- maintain scope specifier for each methods
-- view child annotation
-- view child variables
-- inout variables
-- output variables
-- public variables without scope specifier
--declare argument type
-- private variables
-- constructur
-- ngoninit
--commit message should be short and concise and meaning ful
--ss during pull request

-- Return type for each method must be specified
-- Scope specifier for each method
-- Order : ViewChild, Input, Output, public, private, constructor, ngOnInit
-- Dependency Injection through constructor
-- Max length of characters in one line : 130
-- for methods not used from anywhere else make it private
-- 1 line gap between all functions
-- declare type for function parameters


-- add JIRA story before commit message
-- add screenshot of feature while raising PR
 /*AVERAGE OF RATING OF DOCOTRS INVOLVED IN APP BETWEEN START AND END
        use based on feedback
        SELECT AVG(a.doctor.rating),a.* FROM APPOINTMENT a JOIN FETCH a.doctor.id WHERE a.app_date BETWEEN startDate AND endDate group by a.doctor.id;
          */

         /*DISTRIBUTION OF APPOINTMENT STATUSES DURING THE PERIOD 
         OLD:
         SELECT app_date AS Time_Period,
         COUNT(CASE WHEN app_status = 'Pending' THEN 1 END) AS Pending,
         COUNT(CASE WHEN app_status = 'Cancelled' THEN 1 END) AS Cancelled,
         COUNT(CASE WHEN app_status = 'Completed' THEN 1 END) AS Completed,
         FROM APPOINTMENT
         WHERE
         app_date BETWEEN startDate AND endDate 
         GROUP BY
         YEAR(app_date), MONTH(app_date)
         ORDER BY
         YEAR(app_date), MONTH(app_date)
         ___________________________________________________________________
        NEW:
         SELECT a.app_date AS Time_Period,
         COUNT(CASE WHEN app_status LIKE 'PENDING' THEN 1 END) AS Pending,
         COUNT(CASE WHEN app_status LIKE 'CANCELLED' THEN 1 END) AS Cancelled,
         COUNT(CASE WHEN app_status LIKE 'COMPLETED' THEN 1 END) AS Completed
         FROM APPOINTMENT a
         WHERE
         app_date BETWEEN '11-MAR-12' AND '11-MAR-25' 
         GROUP BY
         app_date
         ORDER BY
         app_date;
         ;
          */
         /*
         DISTRIBUTION OF APPOINTMENT STATUSES DURING THE PERIOD WITH DOCTOR SPECIALIZATION
         OLD:
         SELECT 
         s.name AS Specializations,
         COUNT(CASE WHEN app_status = 'Pending' THEN 1 END) AS Pending,
         COUNT(CASE WHEN app_status = 'Cancelled' THEN 1 END) AS Cancelled,
         COUNT(CASE WHEN app_status = 'Completed' THEN 1 END) AS Completed,
         FROM APPOINTMENT a
         JOIN DOCTOR d ON a.doctor_id=d.id
         JOIN SPECIALIZATION s ON d.specialization_id=s.id
         WHERE
         app_date BETWEEN startDate AND endDate 
         GROUP BY s.name
         ORDER BY s.name;
         __________________________________________________________________________________
         NEW:
         SELECT 
         s.name AS Specializations,
         COUNT(CASE WHEN app_status = 'PENDING' THEN 1 END) AS Pending,
         COUNT(CASE WHEN app_status = 'CANCELLED' THEN 1 END) AS Cancelled,
         COUNT(CASE WHEN app_status = 'COMPLETED' THEN 1 END) AS Completed
         FROM APPOINTMENT a
         JOIN DOCTOR d ON a.d_id=d.id
         JOIN SPECIALIZATION s ON d.specialization_id=s.id
         WHERE
         app_date BETWEEN '11-MAR-12' AND '11-MAR-25' 
         GROUP BY s.name
         ORDER BY s.name;
         */

        /*iDENTIFY TOP TIME SLOTS WITH HIGHEST NUMBER OF APPOINTMENTS, INCLUDE SPECIALTIES ASSOCIATED AND CALCULATE RATING OF DOCTOR FOR EACH TIME SLOT ON EACH DAY
        OLD:
        WITH RankedAppointments AS (
                SELECT
                TO_CHAR(a.app_date, 'Day') AS day_of_week,
                a.slot AS Appointment_time,
                s.name AS Specialization,
                DENSE_RANK() OVER (PARTITION BY TO_CHAR(a.app_date, 'Day'), a.app_slot ORDER BY COUNT (*) DESC) AS ranked,
                COUNT(*) AS Appointment_count,
                AVG(d.rating) AS Average_doctor_rating
                FROM
                APPOINTMENT a
                JOIN
                DOCTOR d ON a.doctor_id=d.id
                JOIN 
                SPECIALIZATION s ON d.specialization_id=s.id
                WHERE 
                a.app_date BETWEEN startDate AND endDate
                GROUP BY
                TO_CHAR(a.app_date, 'Day'), a.app_slot, s.id
        )
        SELECT 
        day_of_week,
        Appointment_time,
        Specialization,
        Appointment_count,
        Average_doctor_rating
        FROM 
        RankedAppointments
        WHERE
        ranking = 1
        ORDER BY
        day_of_week, appointment_count DESC, appointment_time, doctor_specialty;

        _______________________________________________________________________________________________
        NEW:
        WITH RankedAppointments AS (
                SELECT
                TO_CHAR(a.app_date, 'Day') AS day_of_week,
                a.slot AS Appointment_time,
                s.name AS Specialization,
                DENSE_RANK() OVER (PARTITION BY TO_CHAR(a.app_date, 'Day'), a.slot ORDER BY COUNT (*) DESC) AS ranked,
                COUNT(*) AS Appointment_count,
                AVG(d.rating) AS Average_doctor_rating
                FROM
                APPOINTMENT a
                JOIN
                DOCTOR d ON a.d_id=d.id
                JOIN 
                SPECIALIZATION s ON d.specialization_id=s.id
                WHERE 
                a.app_date BETWEEN '11-MAR-12' AND '11-MAR-25'
                GROUP BY
                TO_CHAR(a.app_date, 'Day'), a.slot, s.name
        )
        SELECT 
        day_of_week,
        Appointment_time,
        Specialization,
        Appointment_count,
        Average_doctor_rating
        FROM 
        RankedAppointments
        WHERE
        ranked = 1
        ORDER BY
        day_of_week, appointment_count DESC, appointment_time, Specialization;
        */


       /*list the doctors with the highest and lowest appointment counts during the specified dates
        SELECT d.*,COUNT(*) FROM APPOINTMENT a
        JOIN DOCTOR d ON a.doctor_id=d.id
        WHERE 
        a.app_date BETWEEN startDate AND endDate
        GROUP BY d.id;
        ________________________________________________________________________________________
        SELECT d.name,COUNT(*) app_count FROM APPOINTMENT a
        JOIN DOCTOR d ON a.d_id=d.id
        WHERE 
        a.app_date BETWEEN '11-MAR-12' AND '11-MAR-25'
        GROUP BY d.name order by app_count desc;

       */


      /*
      bREAK DOWN TOTAL APPOINTMENTS BY DEPARMETN, DEFINE POPULARITY OF DIFF MEDICAL SPECIALTIES. 
      IDENTIFY DOCTOR WITH HIGHEST AVERAGE RATING WITHIN EACH DEPARTMENT

      WITH DepartmentRating AS (
        SELECT
        d.name AS Department_name,
        COUNT(*) AS Total_appointments,
        AVG(d.rating) AS Avg_doctor_rating,
        MAX(d.name) KEEP (DENSE_RANK() FIRST ORDER BY d.rating DESC) AS Top_rated_doctor
        FROM
                APPOINTMENT a
                JOIN
                DOCTOR d ON a.doctor_id=d.id
                JOIN 
                SPECIALIZATION s ON d.specialization_id=s.id
                WHERE 
                a.app_date BETWEEN startDate AND endDate
                GROUP BY s.name;
      )
      SELECT Department_name,
      Total_appointments,
        Avg_doctor_rating,
        Top_rated_doctor,
        FROM
        DepartmentRating
        ORDER BY Total_appointments DESC;


      */

     /*
     ANALYZE THE AGE DISTRIBUTION, GENDER DISTRIBUTION AND BLOOD GROUP OF PATIENTS WITHIN TIME FRAME

     SELECT patient_distribution.age_group, 
     patient_distribution.gender, 
     patient_distribution.blood_group, 
     patient_distribution.num_appointments, 
     patient_distribution.avg_age
     FROM (SELECT 
                FLOOR((EXTRACT(YEAR FROM SYSDATE) - p.birth_year) / 10) * 10 as age_group,
                p.gender,
                p.blood_group,
                COUNT(*) AS num_appointments,
                ROUND(AVG(EXTRACT(YEAR FROM SYSDATE) - p.birth_year), 1) AS avg_age
                FROM 
                APPOINTMENT a
                JOIN 
                PATIENT p ON a.patient_id=p.id
                WHERE 
                a.app_date BETWEEN startDate AND endDate
                GROUP BY FLOOR((EXTRACT(YEAR FROM SYSDATE) - p.birth_year) / 10) * 10, p.gender, p.blood_group
                ) AS patient_distribution
     ORDER BY  patient_distribution.age_group, patient_distribution.gender, patient_distribution.blood_group;         

        
 */

/* doctor name, doctor specialty, fee, highest fee, average fee per specialty, lowest fee
select 
d.name,
s.name AS doctor_specialty,
d.fees,
avg(d.fees) over partition */

/*select
b.category,
b.total_budget,
coalesce(sum(e.amount), 0) as total_spent,
b.total_budget - coalesce(sum(e.amount), 0) as remaining_budget,
(coalesce(sum(e.amount),0) / b.total_budget) * 100 as percent_spent,
rank() over (order by coalesce(sum(e.amount), 0) desc) as rank_highest_spending,
rank() over (order by coalesce(sum(e.amount), 0) asc) as rank_lowest_spending
from
budgetcategories b
left join
expenses e on b.category_id = e.category_id
where
e.expense_date between to_date('start_date', 'YYYY-MM-DD') and to_date('end_date', 'YYYY-MM-DD')
group by
b.category, b.total_budget
order by b.category */

-- INSERT INTO "MMS_DB"."ROLE" (ID, ROLE_NAME) VALUES ('2', 'ROLE_PATIENT')
-- ORA-12899: value too large for column "MMS_DB"."ROLE"."ROLE_NAME" (actual: 12, maximum: 10)
-- ORA-06512: at line 1


-- One error saving changes to table "MMS_DB"."ROLE":
-- Row 2: ORA-12899: value too large for column "MMS_DB"."ROLE"."ROLE_NAME" (actual: 12, maximum: 10)
-- ORA-06512: at line 1

-- INSERT INTO "MMS_DB"."ROLE" (ID, ROLE_NAME) VALUES ('2', 'ROLE_PAT')

-- Commit Successful


-- INSERT INTO "MMS_DB"."ROLE" (ID, ROLE_NAME) VALUES ('3', 'ROLE_DOC')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('11', '2', 'adam', 'aaaaa')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('51', '2', 'kyle', 'bbbbb')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('45', '2', 'fdfdf', 'vcccc')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('12', '2', 'shygf', 'dfdfdf')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('56', '2', 'fhssreff', 'erdvfhgf')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('34', '3', 'sdfdfbb', 'gfgfe')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('44', '3', 'llllll', 'lllll')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('55', '3', 'kkkkkk', 'kkkkk')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('89', '3', 'hhhhh', 'hhhh')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('98', '3', 'ppppp', 'pppp')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('67', '3', 'uuuuu', 'uuuu')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('53', '3', 'tttt', 'tttt')

-- Commit Successful


-- INSERT INTO "MMS_DB"."user" (ID, ROLE_ID, USER_NAME, PASSWORD) VALUES ('60', '3', 'yyyyy', 'yyyy')

-- Commit Successful


-- INSERT INTO "MMS_DB"."SPECIALIZATION" (ID, NAME) VALUES ('1', 'CARDIOLOGY')

-- Commit Successful


-- INSERT INTO "MMS_DB"."SPECIALIZATION" (ID, NAME) VALUES ('2', 'OSTEOPOROSIS')

-- Commit Successful


-- INSERT INTO "MMS_DB"."SPECIALIZATION" (ID, NAME) VALUES ('3', 'ORTHODONTIST')

-- Commit Successful


-- INSERT INTO "MMS_DB"."SPECIALIZATION" (ID, NAME) VALUES ('4', 'DENTIST')

-- Commit Successful


-- INSERT INTO "MMS_DB"."DOCTOR" (FEES, GENDER, RATING, ID, SPECIALIZATION_ID, USER_ID, STATUS, EMAIL, NAME, QUALIFICATION, EXPERIENCE_START) VALUES ('1233', 'M', '3', '1', '1', '60', 'ACTIVE', 'ffff@gmail.com', 'llll', 'sdsdsds', '2000')

-- Commit Successful


-- INSERT INTO "MMS_DB"."DOCTOR" (FEES, GENDER, RATING, ID, SPECIALIZATION_ID, USER_ID, STATUS, EMAIL, NAME, QUALIFICATION, EXPERIENCE_START) VALUES ('6777', 'M', '2', '2', '2', '34', 'ACTIVE', 'huyfvu@gmail.com', 'kkkk', 'uuguh', '1980')

-- Commit Successful


-- INSERT INTO "MMS_DB"."DOCTOR" (FEES, GENDER, RATING, ID, SPECIALIZATION_ID, USER_ID, STATUS, EMAIL, NAME, QUALIFICATION, EXPERIENCE_START) VALUES ('5000', 'F', '4', '3', '3', '44', 'ACTIVE', 'kiji@gmail.com', 'oooo', 'ugbj', '1990')

-- Commit Successful


-- INSERT INTO "MMS_DB"."DOCTOR" (FEES, GENDER, RATING, ID, SPECIALIZATION_ID, USER_ID, STATUS, EMAIL, NAME, QUALIFICATION, EXPERIENCE_START) VALUES ('3000', 'F', '5', '4', '3', '55', 'ACTIVE', 'yuyjh@gmail.com', 'yyyy', 'fgvbcv', '1997')

-- Commit Successful


-- INSERT INTO "MMS_DB"."DOCTOR" (FEES, GENDER, RATING, ID, SPECIALIZATION_ID, USER_ID, STATUS, EMAIL, NAME, QUALIFICATION, EXPERIENCE_START) VALUES ('2222', 'M', '4', '5', '2', '89', 'ACTIVE', 'kj@gmail.com', 'uuuu', 'kytg', '1998')

-- Commit Successful


-- INSERT INTO "MMS_DB"."DOCTOR" (FEES, GENDER, RATING, ID, SPECIALIZATION_ID, USER_ID, STATUS, EMAIL, NAME, QUALIFICATION, EXPERIENCE_START) VALUES ('5721', 'F', '2', '6', '1', '98', 'ACTIVE', 'uybhj@gmail.com', 'jihk', 'erdg', '2011')



-- INSERT INTO "MMS_DB"."PATIENT" (BLOOD_GROUP, GENDER, ID, USER_ID, CONTACT_NUMBER, EMAIL, NAME, BIRTH_YEAR) VALUES ('Ap', 'M', '1', '21', '4564343', 'dsd@gmail.com', 'ttttt', '1990')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2024-03-11 08:43:33', 'YYYY-MM-DD HH24:MI:SS'), '11', '1', '1', '1', 'PENDING')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2022-05-12 08:44:01', 'YYYY-MM-DD HH24:MI:SS'), '10', '2', '2', '1', 'PENDING')

-- Commit Successful



-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2024-03-14 08:44:40', 'YYYY-MM-DD HH24:MI:SS'), '5', '3', '3', '1', 'PENDING')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2018-09-02 08:50:02', 'YYYY-MM-DD HH24:MI:SS'), '11', '4', '4', '1', 'COMPLETED')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2022-06-26 09:04:37', 'YYYY-MM-DD HH24:MI:SS'), '12', '2', '5', '1', 'COMPLETED')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2023-02-14 09:05:10', 'YYYY-MM-DD HH24:MI:SS'), '11', '2', '6', '1', 'PENDING')

-- Commit Successful


-- INSERT INTO "MMS_DB"."PATIENT" (BLOOD_GROUP, GENDER, ID, USER_ID, CONTACT_NUMBER, EMAIL, NAME, BIRTH_YEAR) VALUES ('An', 'F', '2', '45', '4546222', 'yahoo@yahoo.com', 'yuyuyuy', '2010')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2019-06-28 09:54:42', 'YYYY-MM-DD HH24:MI:SS'), '10', '5', '7', '2', 'PENDING')

-- Commit Successful


-- INSERT INTO "MMS_DB"."APPOINTMENT" (APP_DATE, SLOT, D_ID, ID, P_ID, APP_STATUS) VALUES (TO_DATE('2020-08-04 10:28:00', 'YYYY-MM-DD HH24:MI:SS'), '6', '4', '8', '2', 'CANCELLED')

-- Commit Successful