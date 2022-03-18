create table users
(
    ID            bigint auto_increment
        primary key,
    name          varchar(50)   not null,
    login_id      varchar(50)   not null,
    login_pw      varbinary(72) not null,
    profile_image text          null,
    status        varchar(10)   not null,
    constraint users_login_id_uindex
        unique (login_id)
);

create index USERS_ID_INDEX
    on users (ID);
