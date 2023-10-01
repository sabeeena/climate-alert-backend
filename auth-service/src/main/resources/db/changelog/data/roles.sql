--liquibase formatted sql
--changeset Sabina:roles.sql
INSERT INTO "role"(id, name, role_code) VALUES (nextval('role_seq'), 'Administrator', 'ADMIN');
INSERT INTO "role"(id, name, role_code) VALUES (nextval('role_seq'), 'Authority', 'SUPERVISOR');
INSERT INTO "role"(id, name, role_code) VALUES (nextval('role_seq'), 'Community', 'USER');

