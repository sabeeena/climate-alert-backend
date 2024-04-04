--liquibase formatted sql
--changeset Sabina:privileges.sql
INSERT INTO "privilege"(id, name, privilege_code) VALUES (nextval('privilege_seq'), 'READ, WRITE, DELETE (LIMITED)', 'BASIC_PRIVILEGE'),
                                                       (nextval('privilege_seq'), 'READ ONLY', 'READ_ONLY_PRIVILEGE'),
                                                       (nextval('privilege_seq'), 'USER_MANAGEMENT', 'USER_MANAGEMENT_PRIVILEGE'),
                                                       (nextval('privilege_seq'), 'REPORTS', 'REPORTS_PRIVILEGE'),
                                                       (nextval('privilege_seq'), 'NOTIFICATION', 'NOTIFICATION_PRIVILEGE'),
                                                       (nextval('privilege_seq'), 'CONTENT_MANAGEMENT', 'CONTENT_MANAGEMENT_PRIVILEGE'),
                                                       (nextval('privilege_seq'), 'ADMIN', 'ADMIN_PRIVILEGE');