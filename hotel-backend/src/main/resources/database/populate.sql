
begin ;
insert into authority(name) values ('ADMIN');
insert into authority(name) values ('EMPLOYEE');
insert into authority(name) values ('CLIENT');
commit ;

begin ;
insert into identity(username, password, email, phone, registered_at, enabled, authority_id, image_id)
values ('admin', '$2a$10$JYue8cgXAyLYN2BMJcbyaObGtAe//WbjMKzheVHoImcL3Nz8xq/Ie', 'admin@gmail.com', '(903)-903-4567',
        '2025-03-05 14:00', true, 1, null);

insert into identity(username, password, email, phone, registered_at, enabled, authority_id, image_id)
values ('employee', '$2a$10$QsHJnLexr/b7UkClaQtjJuvzNBAjeIfzX4ZcKVTtmUFxlBc8G9GRy', 'employee@gmail.com', '(913)-913-8901',
        '2025-03-05 14:00', true, 2, null);

insert into identity(username, password, email, phone, registered_at, enabled, authority_id, image_id)
values ('client', '$2a$10$pC9z0BS.nkrrjW8lOeyAhelaQqhSmSO0QBc4JZfnzv.JFTzs322ZO', 'client@gmail.com', '(952)-952-1234',
        '2025-03-05 14:00', true, 3, null);
commit ;

begin ;
insert into status(rating) values ('I Звезда');
insert into status(rating) values ('II Звезды');
insert into status(rating) values ('III Звезды');
insert into status(rating) values ('IV Звезды');
insert into status(rating) values ('V Звезд');
commit ;

begin ;
insert into country(name, description) values ('Россия', 'Описание страны Российская Федерация');
insert into country(name, description) values ('Китай', 'Описание страны Китайская Народная Демократическая Республика');
insert into country(name, description) values ('США', 'Описание страны Соединенные Штаты Америки');
commit ;

begin ;
insert into city(name, description, country_id, capital) values ('Москва','Описание города Москва',1,true);
insert into city(name, description, country_id, capital) values ('Санкт-Петербург','Описание города Санкт-Петербург',1,false);
insert into city(name, description, country_id, capital) values ('Пекин','Описание города Пекин',2,true);
insert into city(name, description, country_id, capital) values ('Шанхай','Описание города Шанхай',2,false);
insert into city(name, description, country_id, capital) values ('Вашингтон','Описание города Вашингтон',3,true);
insert into city(name, description, country_id, capital) values ('Нью-Йорк','Описание города Нью-Йорк',3,false);
commit ;

begin ;
insert into hotel(name, status_id, description) values ('Long River',3,'Описание отеля Long River');
insert into hotel(name, status_id, description) values ('Marriot',4,'Описание отеля Marriot');
insert into hotel(name, status_id, description) values ('Golden Field',5,'Описание отеля Golden Fields');
commit ;

begin ;
insert into address(city_id, street, house, apartment) values (1,'Величаева','12/3',null);
insert into address(city_id, street, house, apartment) values (1,'Покровская','56а',null);
insert into address(city_id, street, house, apartment) values (2,'Мартышевская','89',null);
insert into address(city_id, street, house, apartment) values (2,'Картаева','34б',null);
insert into address(city_id, street, house, apartment) values (1,'Молчанова','154',null);
commit ;

begin ;
insert into hotel_backend.public.filial(hotel_id, address_id, lux_rooms, economy_rooms, image_id)
values (1,1,10,120, null);
insert into hotel_backend.public.filial(hotel_id, address_id, lux_rooms, economy_rooms, image_id)
values (2,2,15,150, null);
insert into hotel_backend.public.filial(hotel_id, address_id, lux_rooms, economy_rooms, image_id)
values (1,3,12,130, null);
insert into hotel_backend.public.filial(hotel_id, address_id, lux_rooms, economy_rooms, image_id)
values (2,4,17,170, null);
insert into hotel_backend.public.filial(hotel_id, address_id, lux_rooms, economy_rooms, image_id)
values (3,5,25,225, null);
commit ;

begin ;
insert into department(name, description) values ('Административная служба', 'Описание Административной службы');
insert into department(name, description) values ('Служба приема и размещения', 'Описание Службы приема и размещения');
insert into department(name, description) values ('Хозяйственная служба', 'Описание Хозяйственной службы');
insert into department(name, description) values ('Служба напитков и питания', 'Описание службы напитков и питания');
insert into department(name, description) values ('Служба безопасности', 'Описание службы безопасности');
insert into department(name, description) values ('Техническая служба', 'Описание Технической службы');
insert into department(name, description) values ('Коммерческая служба', 'Описание Коммерческой службы');
insert into department(name, description) values ('Служба консьержей', 'Описание Службы консьержей');
insert into department(name, description) values ('Служба анимации и развлечений', 'Описание Службы анимации и развлечений');
insert into department(name, description) values ('Банкетная служба', 'Описание Банкетной службы');
insert into department(name, description) values ('Финансовая служба', 'Описание Финансовой службы');
commit ;

begin ;
insert into filial_department(filial_id, department_id) values (1, 1);
insert into filial_department(filial_id, department_id) values (1, 2);
insert into filial_department(filial_id, department_id) values (1, 3);
insert into filial_department(filial_id, department_id) values (1, 4);
insert into filial_department(filial_id, department_id) values (1, 6);
insert into filial_department(filial_id, department_id) values (1, 11);

insert into filial_department(filial_id, department_id) values (2, 1);
insert into filial_department(filial_id, department_id) values (2, 2);
insert into filial_department(filial_id, department_id) values (2, 3);
insert into filial_department(filial_id, department_id) values (2, 4);
insert into filial_department(filial_id, department_id) values (2, 5);
insert into filial_department(filial_id, department_id) values (2, 6);
insert into filial_department(filial_id, department_id) values (2, 8);
insert into filial_department(filial_id, department_id) values (2, 11);

insert into filial_department(filial_id, department_id) values (3, 1);
insert into filial_department(filial_id, department_id) values (3, 2);
insert into filial_department(filial_id, department_id) values (3, 3);
insert into filial_department(filial_id, department_id) values (3, 4);
insert into filial_department(filial_id, department_id) values (3, 6);
insert into filial_department(filial_id, department_id) values (3, 11);

insert into filial_department(filial_id, department_id) values (4, 1);
insert into filial_department(filial_id, department_id) values (4, 2);
insert into filial_department(filial_id, department_id) values (4, 3);
insert into filial_department(filial_id, department_id) values (4, 4);
insert into filial_department(filial_id, department_id) values (4, 5);
insert into filial_department(filial_id, department_id) values (4, 6);
insert into filial_department(filial_id, department_id) values (4, 8);
insert into filial_department(filial_id, department_id) values (4, 11);

insert into filial_department(filial_id, department_id) values (5, 1);
insert into filial_department(filial_id, department_id) values (5, 2);
insert into filial_department(filial_id, department_id) values (5, 3);
insert into filial_department(filial_id, department_id) values (5, 4);
insert into filial_department(filial_id, department_id) values (5, 5);
insert into filial_department(filial_id, department_id) values (5, 6);
insert into filial_department(filial_id, department_id) values (5, 7);
insert into filial_department(filial_id, department_id) values (5, 8);
insert into filial_department(filial_id, department_id) values (5, 9);
insert into filial_department(filial_id, department_id) values (5, 10);
insert into filial_department(filial_id, department_id) values (5, 11);
commit ;

begin ;
insert into position(name, description, department_id) values ('Директор Отеля','Описание должности Директор Отеля',1);
insert into position(name, description, department_id) values ('Управляющий Отеля','Описание должности Управляющий Отеля',1);
insert into position(name, description, department_id) values ('Менеджер Отеля','Описание должности Менеджер Отеля',1);
insert into position(name, description, department_id) values ('Администратор Отеля','Описание должности Администратор Отеля',1);
insert into position(name, description, department_id) values ('Главный Швейцар','Описание должности Главный Швейцар',2);
insert into position(name, description, department_id) values ('Швейцар','Описание должности Швейцар',2);
insert into position(name, description, department_id) values ('Главная Горничная','Описание должности Главная Горничная',3);
insert into position(name, description, department_id) values ('Горничная','Описание должности Горничная',3);
insert into position(name, description, department_id) values ('Шеф-Повар','Описание должности Шеф-Повар',4);
insert into position(name, description, department_id) values ('Повар','Описание должности Повар',4);
insert into position(name, description, department_id) values ('Начальник Охраны','Описание должности Начальник Охраны',5);
insert into position(name, description, department_id) values ('Охранник','Описание должности Охранник',5);
insert into position(name, description, department_id) values ('Главный Инженер','Описание должности Главный Инженер',6);
insert into position(name, description, department_id) values ('Инженер','Описание должности Инженер',6);
insert into position(name, description, department_id) values ('Главный Консьерж','Описание должности Главный Консьерж',8);
insert into position(name, description, department_id) values ('Консьерж','Описание должности Консьерж',8);
insert into position(name, description, department_id) values ('Главный Бухгалтер','Описание должности Главный Бухгалтер',11);
insert into position(name, description, department_id) values ('Бухгалтер','Описание должности Бухгалтер',11);
commit ;

begin ;
insert into employee(name, surname, patronymic, about, passport, identity_id, filial_id, position_id, image_id)
values ('Олег','Вангаев','Игоревич','Краткое описание профиля личности','8459 349087',
        2,5,1, null);

insert into employee(name, surname, patronymic, about, passport, identity_id, filial_id, position_id, image_id)
values ('Оксана','Парикова','Валерьевна','Краткое описание профиля личности','5621 904578',
        null,5,1, null);
commit ;

begin ;
insert into room_type(type) values ('LUX');
insert into room_type(type) values ('ECONOMY');
commit ;

begin ;
insert into room(number, room_type_id, filial_id, description, rented, rented_from, rented_to, hour_price)
values (100, 1, 1, 'Hotel luxury room description',false, null, null, 2000);

insert into room(number, room_type_id, filial_id, description, rented, rented_from, rented_to, hour_price)
values (110, 2, 1, 'Hotel econom room description',false, null, null, 800);

insert into room(number, room_type_id, filial_id, description, rented, rented_from, rented_to, hour_price)
values (130, 1, 2, 'Hotel luxury room description',false, null, null, 3000);

insert into room(number, room_type_id, filial_id, description, rented, rented_from, rented_to, hour_price)
values (140, 2, 2, 'Hotel econom room description',false, null, null, 500);
commit ;