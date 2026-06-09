-- Role bazodanowe

create role db_procexecutor;
create role app_role;
create role dev_role;

-- Uprawnienia domyslne dla przyszlych tabel (tworzone przez Hibernate)
alter default privileges in schema public
    grant select, insert, update, delete on tables to app_role;

alter default privileges in schema public
    grant select on tables to dev_role;

alter default privileges in schema public
    grant usage on sequences to app_role;

alter default privileges in schema public
    grant execute on functions to db_procexecutor;

alter default privileges in schema public
    grant execute on functions to app_role;
