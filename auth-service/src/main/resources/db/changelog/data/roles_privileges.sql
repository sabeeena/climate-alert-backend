--liquibase formatted sql
--changeset Sabina:roles_privileges.sql
INSERT INTO role_privilege(role_id, privilege_id) VALUES ((SELECT r.id FROM "role" r WHERE role_code = 'ADMIN'),
                                                          (SELECT p.id FROM "privilege" p WHERE privilege_code = 'ADMIN_PRIVILEGE')),
                                                         ((SELECT r.id FROM "role" r WHERE role_code = 'SUPERVISOR'),
                                                          (SELECT p.id FROM "privilege" p WHERE privilege_code = 'BASIC_PRIVILEGE')),
                                                         ((SELECT r.id FROM "role" r WHERE role_code = 'SUPERVISOR'),
                                                          (SELECT p.id FROM "privilege" p WHERE privilege_code = 'REPORTS_PRIVILEGE')),
                                                         ((SELECT r.id FROM "role" r WHERE role_code = 'SUPERVISOR'),
                                                          (SELECT p.id FROM "privilege" p WHERE privilege_code = 'NOTIFICATION_PRIVILEGE')),
                                                         ((SELECT r.id FROM "role" r WHERE role_code = 'USER'),
                                                          (SELECT p.id FROM "privilege" p WHERE privilege_code = 'BASIC_PRIVILEGE')),
                                                         ((SELECT r.id FROM "role" r WHERE role_code = 'USER'),
                                                          (SELECT p.id FROM "privilege" p WHERE privilege_code = 'NOTIFICATION_PRIVILEGE'));