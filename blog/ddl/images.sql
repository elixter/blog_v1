-- auto-generated definition
create table images
(
    id          bigint auto_increment
        primary key,
    origin_name text                               not null,
    stored_name text                               not null,
    create_at   datetime default CURRENT_TIMESTAMP not null,
    status      varchar(10)                        not null
);

create index images_id_index
    on images (id);

