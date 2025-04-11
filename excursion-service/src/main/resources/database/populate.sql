
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
    enabled boolean not null
);