-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    clientid   bigint not null primary key,
    name varchar(50),
    addressid bigint
);

create table address
(
    addressid   bigserial not null primary key,
    street varchar(50)
);

create table phone
(
    phoneId   bigserial not null primary key,
    number varchar(50),
    clientid bigint
);
