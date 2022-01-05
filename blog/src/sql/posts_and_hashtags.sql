create table posts_and_hashtags
(
    post_id    bigint not null,
    hashtag_id bigint not null,
    constraint posts_and_hashtags_hashtags_id_fk
        foreign key (hashtag_id) references hashtags (id)
            on update cascade on delete cascade,
    constraint posts_and_hashtags_posts_id_fk
        foreign key (post_id) references posts (id)
            on update cascade on delete cascade
);