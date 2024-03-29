-- auto-generated definition
create table posts
(
    id        bigint auto_increment
        primary key,
    title     varchar(50)                         not null,
    content   text                                not null,
    thumbnail text                                null,
    category  varchar(20)                         not null,
    status    varchar(10)                         not null,
    create_at timestamp default CURRENT_TIMESTAMP not null,
    update_at timestamp default CURRENT_TIMESTAMP not null
);

