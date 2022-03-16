create table USERS
(
    ID            BIGINT auto_increment,
    NAME          CHARACTER not null,
    LOGIN_ID      CHARACTER not null,
    LOGIN_PW      BINARY    not null,
    PROFILE_IMAGE CHARACTER,
    constraint USERS_PK
        primary key (ID)
);

create index USERS_ID_INDEX
    on USERS (ID);