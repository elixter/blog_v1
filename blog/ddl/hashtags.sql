-- auto-generated definition
create table hashtags
(
    id      bigint auto_increment
        primary key,
    tag     varchar(50) not null,
    post_id bigint      not null,
    status  varchar(10) not null,
    constraint hashtags_posts_id_fk
        foreign key (post_id) references posts (id)
);

