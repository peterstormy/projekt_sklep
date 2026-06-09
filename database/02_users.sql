-- Uzytkownicy bazodanowi

-- Administrator - wlasciciel bazy danych
create user admin_db with password 'Admin_2026!' createdb createrole;
grant all privileges on database sklep to admin_db;
grant all privileges on schema public to admin_db;

-- ApplicationIdentity - konto aplikacji Spring Boot (READ, WRITE, EXECUTE)
create user app_identity with password 'App_2026!';
grant connect on database sklep to app_identity;
grant usage on schema public to app_identity;
grant create on schema public to app_identity;
grant app_role to app_identity;
grant db_procexecutor to app_identity;

-- Konta deweloperskie (tylko READ)
create user dev_student with password 'Dev_2026!';
grant connect on database sklep to dev_student;
grant usage on schema public to dev_student;
grant dev_role to dev_student;

-- Dodaj kolejnych deweloperow wedlug potrzeb:
-- create user dev_imie with password 'Dev_2025!';
-- grant connect on database sklep to dev_imie;
-- grant usage on schema public to dev_imie;
-- grant dev_role to dev_imie;
