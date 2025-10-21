
    set client_min_messages = WARNING;

    alter table if exists budgets 
       drop constraint if exists FKn7qib00712y8dwelmqfwis6ka;

    alter table if exists budgets 
       drop constraint if exists FKln0tm5tgf3f9q3sp9sa5m8m7b;

    alter table if exists categories 
       drop constraint if exists FKghuylkwuedgl2qahxjt8g41kb;

    alter table if exists goals 
       drop constraint if exists FKb1mp6ulyqkpcw6bc1a2mr7v1g;

    alter table if exists transactions 
       drop constraint if exists FKsqqi7sneo04kast0o138h19mv;

    alter table if exists transactions 
       drop constraint if exists FKqwv7rmvc8va8rep7piikrojds;

    drop table if exists budgets cascade;

    drop table if exists categories cascade;

    drop table if exists goals cascade;

    drop table if exists transactions cascade;

    drop table if exists users cascade;
