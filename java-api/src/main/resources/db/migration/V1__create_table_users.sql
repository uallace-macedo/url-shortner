create table users (
    id uuid not null,
    created_at date not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),

    primary key (id)
);