-- auto-generated definition
create table users
(
    id             bigint auto_increment
        primary key,
    name           varchar(50)                          not null,
    login_id       varchar(50)                          not null,
    login_pw       varbinary(72)                        not null,
    email          varchar(100)                         not null,
    profile_image  text                                 null,
    create_at      timestamp  default CURRENT_TIMESTAMP not null,
    email_verified tinyint(1) default 0                 null,
    status         varchar(10)                          not null,
    constraint users_email_uindex
        unique (email),
    constraint users_login_id_uindex
        unique (login_id)
);

create index USERS_ID_INDEX
    on users (id);

