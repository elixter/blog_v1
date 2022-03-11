create table images
(
    id          bigint auto_increment
        primary key,
    origin_name text        not null,
    url         text        not null,
    status      varchar(20) not null
);

create index images_id_index
    on images (id);
