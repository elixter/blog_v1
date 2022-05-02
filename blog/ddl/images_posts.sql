create table images_posts
(
    image_id bigint not null,
    post_id  bigint not null,
    constraint images_posts_images_id_fk
        foreign key (image_id) references images (id)
            on update cascade on delete cascade,
    constraint images_posts_posts_id_fk
        foreign key (post_id) references posts (id)
            on update cascade on delete cascade
);