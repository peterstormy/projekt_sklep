-- Audyt bazy danych
-- Skrypt wykonywany automatycznie przez Spring Boot po starcie (AuditSetup.java)

create table if not exists audit_log (
    id          bigserial primary key,
    table_name  varchar(50)  not null,
    operation   varchar(10)  not null,
    user_name   varchar(50)  not null,
    timestamp   timestamp    not null default current_timestamp,
    old_data    jsonb,
    new_data    jsonb
);

create or replace function audit_trigger_func()
returns trigger as $$
begin
    if tg_op = 'INSERT' then
        insert into audit_log(table_name, operation, user_name, new_data)
        values (tg_table_name, 'INSERT', current_user, row_to_json(new)::jsonb);
        return new;

    elsif tg_op = 'UPDATE' then
        insert into audit_log(table_name, operation, user_name, old_data, new_data)
        values (tg_table_name, 'UPDATE', current_user, row_to_json(old)::jsonb, row_to_json(new)::jsonb);
        return new;

    elsif tg_op = 'DELETE' then
        insert into audit_log(table_name, operation, user_name, old_data)
        values (tg_table_name, 'DELETE', current_user, row_to_json(old)::jsonb);
        return old;
    end if;

    return null;
end;
$$ language plpgsql;

create or replace trigger audit_product
    after insert or update or delete on product
    for each row execute function audit_trigger_func();

create or replace trigger audit_category
    after insert or update or delete on category
    for each row execute function audit_trigger_func();

create or replace trigger audit_orders
    after insert or update or delete on orders
    for each row execute function audit_trigger_func();

create or replace trigger audit_users
    after insert or update or delete on users
    for each row execute function audit_trigger_func();

grant select on audit_log to dev_role;
grant select, insert on audit_log to app_role;
