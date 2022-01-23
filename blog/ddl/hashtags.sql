create table hashtags
(
    id      bigint auto_increment
        primary key,
    value   varchar(50) not null,
    post_id bigint      not null,
    constraint hashtags_posts_id_fk
        foreign key (post_id) references posts (id)
            on update cascade on delete cascade
);