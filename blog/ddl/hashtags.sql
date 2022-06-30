create table hashtags
(
    id      bigint auto_increment
        primary key,
    tag     varchar(50) not null,
    post_id bigint      not null,
    status  tinyint     not null,
    constraint hashtags_posts_id_fk
        foreign key (post_id) references posts (id)
);
