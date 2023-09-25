create table token(
                      id int primary key,
                      token varchar(256),
                      token_type varchar(64),
                      revoked boolean,
                      expired boolean,
                      user_id bigint
)

CREATE SEQUENCE hibernate_sequence START 1;


insert into role (id, name, role_code) values (0, 'user', 0);
insert into organization (id, full_name, bin, address, email, phone, created, modified) values (0, 'organization', '57779', 'asdfas', 'asdfasd','fdsadf', now(), now());