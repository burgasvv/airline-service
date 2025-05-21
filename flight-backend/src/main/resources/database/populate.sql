
insert into authority(name) values ('ADMIN');
insert into authority(name) values ('EMPLOYEE');
insert into authority(name) values ('USER');

insert into identity(username, authority_id, password, email, phone, registered_at, enabled)
values ('admin', 1,'$2a$10$xqNA2bsDcXWux3tL.7ENbeKn/CTBp6itTsxLU4.kOkYzM/ss15r9S','admin@gmail.com',
        '(913)-954-8877','2024-10-15 18:18:20',true);

insert into identity(username, authority_id, password, email, phone, registered_at, enabled)
values ('employee',2,'$2a$10$ydEQ30SULqR1B7eOLWy8Ne6vHuhpeiDvVz1bRkn3Hb0t9DsiPTKoW','employee@gmail.com',
        '(952)-906-1546','2024-12-25 15:25:20',true);

insert into identity(username, authority_id, password, email, phone, registered_at, enabled)
values ('user', 3,'$2a$10$wRI7Kctz5iz7Fr0sHbw6LeB1UPO8ASmBU/cL2sQ4x7RNV3h6tQSo.','user@gmail.com',
        '(913)-547-1624','2025-03-13 10:24:12',true);

insert into country(name) values ('Россия');
insert into country(name) values ('Китай');
insert into country(name) values ('США');

insert into city(name, country_id) values ('Москва', 1);
insert into city(name, country_id) values ('Санкт-Петербург', 1);
insert into city(name, country_id) values ('Пекин', 2);
insert into city(name, country_id) values ('Шанхай', 2);
insert into city(name, country_id) values ('Нью-Йорк', 3);
insert into city(name, country_id) values ('Вашингтон', 3);

insert into address(city_id, street, house) values (1,'Калужская','12');
insert into address(city_id, street, house) values (1,'Дмитриева','34а');
insert into address(city_id, street, house) values (1,'Столярова','12/2');
insert into address(city_id, street, house) values (2,'Кирпичная','56');
insert into address(city_id, street, house) values (2,'Мостовая','89/1');

insert into airport(name, address_id, opened) values ('Шереметьево',1,true);
insert into airport(name, address_id, opened) values ('Домодедово',2,true);
insert into airport(name, address_id, opened) values ('Внуково',3,true);
insert into airport(name, address_id, opened) values ('Пулково',4,true);
insert into airport(name, address_id, opened) values ('Левашово',5,true);

insert into address(city_id, street, house, apartment) values (1,'Комарова','64/1','156');
insert into address(city_id, street, house, apartment) values (1,'Рубцова','97b','324');
insert into address(city_id, street, house, apartment) values (2,'Мельницкая','90','221');
insert into address(city_id, street, house) values (2,'Акриловая','85/6');

insert into filial(name, address_id, opens_at, closes_at, opened)
values ('Филиал на Комарова',6,'08:00','22:00',true);
insert into filial(name, address_id, opens_at, closes_at, opened)
values ('Филиал на Рубцова',7,'07:00','21:00',true);
insert into filial(name, address_id, opens_at, closes_at, opened)
values ('Филиал на Мельницкой',8,'08:00','21:00',true);
insert into filial(name, address_id, opens_at, closes_at, opened)
values ('Филиал на Акриловой',9,'07:00','22:00',true);

insert into department(name, description)
values ('Департамент управления безопасностью полетов', 'Описание департамента по управлению безопасности полетов');
insert into department(name, description)
values ('Отдел режима и спецсвязи', 'Описание отдела для режима и спецсвязи');
insert into department(name, description)
values ('Департамент общественных связей', 'Описание департамента общественных связей');
insert into department(name, description)
values ('Департамент внутреннего аудита', 'Описание департамента внутреннего аудита');
insert into department(name, description)
values ('Департамент управления персоналом', 'Описание департамента по управлению персоналом компании');
insert into department(name, description)
values ('Департамент коммерческих систем', 'Описание департамента коммерческих систем');
insert into department(name, description)
values ('Юридический департамент', 'Описание департамента по юриспруденции и физических лиц');
insert into department(name, description)
values ('Департамент управления данными и архитектурой', 'Описание департамента по управлению данными и архитектурой');

insert into filial_department(filial_id, department_id) values (1, 1);
insert into filial_department(filial_id, department_id) values (1, 2);
insert into filial_department(filial_id, department_id) values (1, 3);
insert into filial_department(filial_id, department_id) values (1, 4);
insert into filial_department(filial_id, department_id) values (1, 5);
insert into filial_department(filial_id, department_id) values (1, 6);
insert into filial_department(filial_id, department_id) values (1, 7);
insert into filial_department(filial_id, department_id) values (1, 8);

insert into filial_department(filial_id, department_id) values (2, 1);
insert into filial_department(filial_id, department_id) values (2, 2);
insert into filial_department(filial_id, department_id) values (2, 3);
insert into filial_department(filial_id, department_id) values (2, 4);
insert into filial_department(filial_id, department_id) values (2, 5);
insert into filial_department(filial_id, department_id) values (2, 6);
insert into filial_department(filial_id, department_id) values (2, 7);
insert into filial_department(filial_id, department_id) values (2, 8);

insert into filial_department(filial_id, department_id) values (3, 1);
insert into filial_department(filial_id, department_id) values (3, 2);
insert into filial_department(filial_id, department_id) values (3, 3);
insert into filial_department(filial_id, department_id) values (3, 4);
insert into filial_department(filial_id, department_id) values (3, 5);
insert into filial_department(filial_id, department_id) values (3, 6);
insert into filial_department(filial_id, department_id) values (3, 7);
insert into filial_department(filial_id, department_id) values (3, 8);

insert into filial_department(filial_id, department_id) values (4, 1);
insert into filial_department(filial_id, department_id) values (4, 2);
insert into filial_department(filial_id, department_id) values (4, 3);
insert into filial_department(filial_id, department_id) values (4, 4);
insert into filial_department(filial_id, department_id) values (4, 5);
insert into filial_department(filial_id, department_id) values (4, 6);
insert into filial_department(filial_id, department_id) values (4, 7);
insert into filial_department(filial_id, department_id) values (4, 8);

insert into position(name, description, department_id) values ('Главный Пилот','Описание должности Главный пилот',1);
insert into position(name, description, department_id) values ('Второй Пилот','Описание должности Второй пилот',1);
insert into position(name, description, department_id) values ('Главный бортпроводник','Описание должности Главный бортпроводник',1);
insert into position(name, description, department_id) values ('Бортпроводник','Описание должности Бортпроводник',1);

insert into address(city_id, street, house, apartment) values (1,'Местническая','56/2', '25');

insert into employee(name, surname, patronymic, about, passport, identity_id, address_id, position_id,
                     filial_department_id)
values ('Игорь', 'Буртаев', 'Сергеевич', 'Информация об Игоре', '8060 258914', 2, 10, 1, 1);

insert into plane(number, model, business_class, economy_class, free, in_service)
values ('k4j6lk3465','Boeing-574', 70, 150, true, true);

insert into plane(number, model, business_class, economy_class, free, in_service)
values ('2j5op6i2h6','Boeing-664',90, 180, true, true);

insert into plane(number, model, business_class, economy_class, free, in_service)
values ('2h54kj62hk','Boeing-747',100, 200, true, true);

insert into cabin_type(name) values ('BUSINESS');
insert into cabin_type(name) values ('ECONOMY');
