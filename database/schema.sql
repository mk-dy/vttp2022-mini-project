create schema vttpproject;

use vttpproject;

create table user (
user_id int not null auto_increment,
username varchar(64) not null,
email varchar(128) not null,
name varchar(64),
password varchar(256) not null,

primary key (user_id)
);
