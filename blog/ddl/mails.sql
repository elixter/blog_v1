-- auto-generated definition
create table mails
(
    id       bigint auto_increment,
    sender   varchar(50)  not null,
    receiver varchar(50)  not null,
    title    varchar(100) not null,
    content  text         null,
    status   varchar(10)  not null,
    constraint mails_id_uindex
        unique (id)
);

alter table mails
    add primary key (id);

