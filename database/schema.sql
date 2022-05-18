create schema vttpproject;

use vttpproject;

-- user table --
create table user (
    user_id int not null auto_increment,
    username varchar(64) not null,
    email varchar(128) not null,
    name varchar(64),
    password varchar(256) not null,

    primary key (user_id)
);

-- likes table --
create table likes (
    like_id int not null auto_increment,
    recipeid varchar(64) not null,
    recipename varchar(64) not null,
    image varchar(512),
    user_id int,
    primary key (like_id),

    constraint fk_user_id
        foreign key(user_id) 
        references user(user_id)
        on delete cascade
    
)