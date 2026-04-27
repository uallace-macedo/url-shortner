create sequence url_seq start with 1 increment by 1;

create table urls (
    id bigint not null,
    active BOOLEAN default true,
    click_count bigint,
    created_at TIMESTAMPTZ not null,
    custom_slug varchar(255),
    expires_at TIMESTAMPTZ not null,
    max_click_count bigint,
    url TEXT,
    user_id uuid not null,

    primary key (id),
    constraint fk_urls_users foreign key (user_id) references users(id)
);