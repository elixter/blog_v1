create table post
(
    id        bigint auto_increment
        primary key,
    title     varchar(50) not null,
    content   text        not null,
    thumbnail text        null
);
