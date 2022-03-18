create table users
(
    ID            bigint auto_increment
        primary key,
    NAME          varchar(50)   not null,
    LOGIN_ID      varchar(50)   not null,
    LOGIN_PW      varbinary(72) not null,
    PROFILE_IMAGE text          null
);

create index USERS_ID_INDEX
    on users (ID);

