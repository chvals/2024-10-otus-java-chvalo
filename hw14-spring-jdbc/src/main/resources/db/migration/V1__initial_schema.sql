create table client
(
    clientid   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    addressid   bigserial not null primary key,
    clientid    bigint,
    street varchar(50)
);

create table phone
(
    phoneId   bigserial not null primary key,
    number varchar(50),
    clientid bigint
);
