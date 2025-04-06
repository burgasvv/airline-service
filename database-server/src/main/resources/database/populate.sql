
create table if not exists image (
    id bigint generated by default as identity unique not null ,
    name varchar not null ,
    data bytea not null
);

create table if not exists authority (
    id bigint generated by default as identity unique not null,
    name varchar not null unique
);

create table if not exists identity (
    id bigint generated by default as identity unique not null ,
    authority_id bigint references authority(id) on delete set null on update cascade ,
    username varchar not null unique ,
    password varchar not null ,
    email varchar not null unique ,
    phone varchar not null unique ,
    registered_at timestamp not null ,
    enabled boolean not null ,
    image_id bigint unique references image(id) on delete set null on update cascade
);

create table if not exists restore_token (
    id bigint generated by default as identity unique not null ,
    value uuid not null unique ,
    identity_id bigint unique references identity(id) on delete cascade on update cascade
);

create table if not exists country (
    id bigint generated by default as identity unique not null ,
    name varchar not null unique
);

create table if not exists city (
    id bigint generated by default as identity unique not null ,
    name varchar not null unique ,
    latitude float not null ,
    longitude float not null
);

create table if not exists address (
    id bigint generated by default as identity unique not null ,
    country_id bigint references country(id) on delete set null on update cascade ,
    city_id bigint references city(id) on delete set null on update cascade,
    street varchar not null ,
    house varchar not null ,
    apartment varchar
);

create table if not exists airport (
    id bigint generated by default as identity unique not null ,
    name varchar not null unique ,
    address_id bigint references address(id) on delete set null on update cascade ,
    opens_at time not null ,
    closes_at time not null ,
    opened boolean not null
);

create table if not exists filial (
    id bigint generated by default as identity unique not null ,
    name varchar not null unique ,
    address_id bigint references address(id) on delete set null on update cascade ,
    opens_at time not null ,
    closes_at time not null ,
    opened boolean not null
);

create table if not exists department (
    id bigint generated by default as identity unique not null ,
    name varchar not null unique ,
    description text not null ,
    filial_id bigint references filial(id) on delete set null on update cascade
);

create table if not exists branch_department (
    id bigint generated by default as identity unique not null ,
    filial_id bigint references filial(id) on delete cascade on update cascade ,
    department_id bigint references department(id) on delete cascade on update cascade
);

create table if not exists position (
    id bigint generated by default as identity unique not null ,
    name varchar not null unique ,
    department_id bigint references department(id) on delete cascade on update cascade
);

create table if not exists employee (
    id bigint generated by default as identity unique not null ,
    identity_id bigint references identity(id) on delete set null on update cascade ,
    name varchar not null ,
    surname varchar not null ,
    patronymic varchar not null ,
    about text not null ,
    address_id bigint references address(id) on delete set null on update cascade ,
    department_id bigint references department(id) on delete set null on update cascade ,
    branch_department_id bigint references branch_department(id) on delete set null on update cascade
);

insert into authority(name) values ('ADMIN');
insert into authority(name) values ('EMPLOYEE');
insert into authority(name) values ('USER');

insert into identity(username, authority_id, password, email, phone, registered_at, enabled)
values ('admin', 1,'$2a$10$xqNA2bsDcXWux3tL.7ENbeKn/CTBp6itTsxLU4.kOkYzM/ss15r9S','admin@gmail.com',
        '9139548877','2024-10-15 18:18:20',true);

insert into identity(username, authority_id, password, email, phone, registered_at, enabled)
values ('employee',2,'$2a$10$ydEQ30SULqR1B7eOLWy8Ne6vHuhpeiDvVz1bRkn3Hb0t9DsiPTKoW','employee@gmail.com',
        '9529061546','2024-12-25 15:25:20',true);

insert into identity(username, authority_id, password, email, phone, registered_at, enabled)
values ('user', 3,'$2a$10$wRI7Kctz5iz7Fr0sHbw6LeB1UPO8ASmBU/cL2sQ4x7RNV3h6tQSo.','user@gmail.com',
        '9135471624','2025-03-13 10:24:12',true);