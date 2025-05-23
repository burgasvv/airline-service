
insert into authority(name) values ('ADMIN');
insert into authority(name) values ('USER');

insert into identity(username, password, email, phone, registered_at, enabled, authority_id)
values ('admin','$2a$10$KrTGHnyrCFcBfNtT/Ugr..L1JZyYKPhUwBxR457ZnZbdTcYjAMjeO',
        'admin@gmail.com','(903)-903-5465','2024-10-15',true,1);

insert into identity(username, password, email, phone, registered_at, enabled, authority_id)
values ('user','$2a$10$jS1grVIkmZsTqjYzoZfgyeuQ4nBxaU4zaWZRwDJD2b9oagV//wnni',
        'user@gmail.com','(913)-903-7834','2025-11-25',true,2);

insert into country(name, description, population)
values ('Россия', 'Описание Российской Федерации',143854325);
insert into country(name, description, population)
values ('Китай', 'Описание Китайской Национальной Демократической Республики',1411356721);
insert into country(name, description, population)
values ('США', 'Описание Соединенных Штатов Америки',340134768);

insert into city(name, description, population, country_id, capital)
values ('Москва', 'Описание Российского города Москва - столицы Российской Федерации', 13149803, 1, true);
insert into city(name, description, population, country_id, capital)
values ('Санкт-Петербург', 'Описание города Российской Федерации - Санкт-Петербурга', 5597763, 1, false);
insert into city(name, description, population, country_id, capital)
values ('Пекин', 'Описание города Китайской Национальной Демократической Республики - Пекина', 21893457, 2, true);
insert into city(name, description, population, country_id, capital)
values ('Шанхай', 'Описание города Китайской Национальной Демократической Республики - Шанхая', 24870895, 2, false);
insert into city(name, description, population, country_id, capital)
values ('Вашингтон', 'Описание города Соединенных Штатов Америки - Вашингтона', 678972, 3, true);
insert into city(name, description, population, country_id, capital)
values ('Нью-Йорк', 'Описание города Соединенных Штатов Америки - Нью-Йорка', 8258932, 3, false);

insert into language(name) values ('Russian');
insert into language(name) values ('English');
insert into language(name) values ('Chinese');

insert into sight(name, description, city_id)
values ('Красная Площадь', 'Интересно, что главная площадь России не всегда выглядела так. ' ||
                           'Долгое время на ней были торговые ряды, а сама она называлась Пожаром. ' ||
                           'Потом незастроенную часть площади стали называть Красной (то есть красивой), ' ||
                           'а затем это название перенеслось на всю площадь. Уникальный архитектурный ансамбль площади со Спасской башней, ' ||
                           'Историческим музеем, Храмом Василия Блаженного внесен в список всемирного наследия. ' ||
                           'Сейчас Красная площадь — самое посещаемое туристами место в Москве.', 1);

insert into sight(name, description, city_id)
values ('Храм Василия Блаженного',
        'Главное украшение Красной площади с его разноцветными маковками часто сравнивают с ' ||
        'букетом цветов или диковинным плодом. Покровский собор построили в середине 16 века ' ||
        'в честь победы Ивана Грозного над Казанским ханством. Его возвели вместо десятка небольших деревянных церквей, ' ||
        'которые были построены на этом месте в честь разных военных побед царя. ' ||
        'По факту Покровский собор — это 10 объединенных в один архитектурный ансамбль маленьких церквей. ' ||
        'Службы проходят только в одной из них — церкви Василия Блаженного, по названию которой часто называют и весь собор. ' ||
        'Остальная часть собора работает как музей.', 1);

insert into sight(name, description, city_id)
values ('Кремль', 'Сначала московский кремль был деревянным, потом белокаменным, затем несколько раз менял цвет ' ||
                  'с белого на красный и наоборот. В строительстве крепости в разное время участвовали как русские архитекторы, ' ||
                  'так и итальянские зодчие. Зубцы стен сделаны в виде ласточкиных хвостов — такие же есть у замков в Италии. ' ||
                  'Всего у кремля, построенного в виде неправильного треугольника, 20 башен, среди них больше всего известна ' ||
                  'Спасская — та самая, на которой куранты отбивают начало нового года.', 1);

insert into sight(name, description, city_id)
values ('Эрмитаж', 'ли бы мы выбирали самую важную и знаковую достопримечательность Санкт-Петербурга, ' ||
                   'то непременно бы отдали этот титул Эрмитажу. Это крупнейший историко-культурный музей России, ' ||
                   'занимающий одновременно 6 исторических зданий, главным из которых является Зимний дворец. ' ||
                   'Трудно представить, что 250 лет назад Эрмитаж представлял собой частную коллекцию Екатерины II, ' ||
                   'а сейчас музей посещают более 5 млн человек в год.', 2);

insert into sight(name, description, city_id)
values ('Исаакиевский собор', 'До современного Исаакиевского собора на его месте уже успели построить 3 храма: ' ||
                              'первый был слишком маленький, второй разместили на месте с плохим грунтом, третий и вовсе был так плох, ' ||
                              'что вызывал насмешки. В 1816 году Александр I выбирает архитектором храма юного ' ||
                              'Огюста Монферрана из Франции. Императору настолько понравился предложенный французом проект, ' ||
                              'что тот незамедлительно назначил его главным.', 2);

insert into sight(name, description, city_id)
values ('Петропавловская крепость',
        'Петропавловская крепость — это то место, откуда в 1703 году начал разрастаться Петербург.' ||
        ' Построенная для обороны, она не участвовала ни в одном сражении, благодаря чему прекрасно сохранилась. ' ||
        'Конечно, сейчас крепость выглядит иначе, чем 300 лет назад, но причиной тому стали постоянные ' ||
        'модернизации в связи с требованиями времени. Большую часть времени Петропавловская ' ||
        'крепость являлась тюрьмой. Её закрыли в 1920, а спустя 4 года здесь основали музей.', 2);

insert into sight(name, description, city_id)
values ('Запретный город', 'Закрытый город в Пекине — это бывший дворец китайских императоров, известный как Гугун, ' ||
                           'что означает «Бывший дворец». Он расположен в центре Пекина, к северу от главной площади ' ||
                           'Тяньаньмэнь и восточнее озёрного квартала. Этот дворцовый комплекс был построен в период ' ||
                           'с 1406 по 1420 годы и служил резиденцией для 24 императоров династий Мин и Цин.', 3);

insert into sight(name, description, city_id)
values ('Великая Китайская стена',
        'Крупнейший памятник архитектуры. Проходит по северному Китаю на протяжении 8851,9 км, а на участке ' ||
        'Бадалин проходит в непосредственной близости от Пекина.', 3);

insert into sight(name, description, city_id)
values ('Храм Неба в Пекине', 'Храмово-монастырский комплекс в центральном Пекине, включающий единственный храм ' ||
                              'круглой формы в городе — Храм Урожая. Занесен ЮНЕСКО в список всемирного наследия человечества.',
        3);

insert into sight(name, description, city_id)
values ('Сад Юй Юань',
        'Сад Юйюань в Шанхае — частный классический сад Китая. Он расположен в самом сердце старого города Наньши.', 4);

insert into sight(name, description, city_id)
values ('Храм Нефритового Будды', 'Буддийский храм в Шанхае. Как многие современные китайские буддийские храмы, ' ||
                                  'сегодня храм сочетает две буддийские школы буддизма Махаяны: Чистой Земли и чань.',
        4);

insert into sight(name, description, city_id)
values ('Лунхуасы', 'Лунхуасы — один из наиболее примечательных и самый большой буддийский храм Шанхая.', 4);

insert into sight(name, description, city_id)
values ('Белый Дом',
        'Вся национальная мощь и сила президентской власти сосредоточена в этом символе американской государственности. ' ||
        'Использование белого цвета в постройке имеет несколько вариантов объяснения, ' ||
        'в ходе экскурсии можно услышать интересные факты. Маршруты для туристов предполагают осмотр помещений ' ||
        'на 2-х этажах, экскурсии проводятся в период вторника-субботы.', 5);

insert into sight(name, description, city_id)
values ('Капитолий', 'На Капитолийском холме возвышается белоснежное здание с одноименным названием. ' ||
                     'Все гиды в Вашингтоне ставят его на первое место среди посещаемых, рассказывая туристам непростую ' ||
                     'и интересную историю строительства. Первый камень был заложен Джорджем Вашингтоном, на всех этапах ' ||
                     'возведения здания случались разной сложности передряги. Сегодня Капитолий гордо возвышается на холме, ' ||
                     'и туристы из специальных галерей могут видеть, как проходят заседания Сената и Конгресса. ' ||
                     'Также можно попасть в известную на весь мир Ротонду, где располагается коллекция скульптур и картин.',
        5);

insert into sight(name, description, city_id)
values ('Национальная аллея', 'Аллеей она называется, скорее, номинально. На самом деле это парковая зона ' ||
                              'большой протяженности с фонтанами, музеями и памятниками, которые являются национальным достоянием. ' ||
                              'Интересный факт: хотя мемориалы на аллее чтят память погибших, вокруг них царит обычный американский быт.',
        5);

insert into sight(name, description, city_id)
values ('Статуя Свободы', 'Это подарок Франции к столетней годовщине американской революции. ' ||
                          'Визитная карточка Нью-Йорка и символ США находится на острове Свободы, благодаря чему ' ||
                          'достопримечательность видна всем судам, которые направляются в порт Нью-Йорка.', 6);

insert into sight(name, description, city_id)
values ('Музей Соломона Гуггенхайма', 'Это здание выделяется на фоне высоток мегаполиса. ' ||
                                      'По форме оно напоминает спираль. Внутри музей не менее удивительный: ' ||
                                      'круглое пространство со стеклянным куполом состоит из нескольких этажей.', 6);

insert into sight(name, description, city_id)
values ('Остров Эллис', 'Историческое место, через которое прошли миллионы иммигрантов. ' ||
                        'Когда-то он был песчаной косой, куда во время строительства метрополитена решили свозить грунт. ' ||
                        'Так на реке и появился небольшой участок суши. В 1814 году сюда начали прибывать толпы переселенцев ' ||
                        'разных народностей, а в 1892 году на острове открыли государственный иммиграционный центр.', 6);

insert into guide(name, surname, patronymic, phone, about)
values ('Оксана', 'Параева', 'Сергеевна', '(943)-567-8326', 'Информация об Оксане Параевой - всемирном гиде из России');
insert into guide(name, surname, patronymic, phone, about)
values ('Синь', 'Юанье', 'Чжу', '(567)-385-6723', 'Информация об Синь Юанье - всемирном гиде из Китая');
insert into guide(name, surname, patronymic, phone, about)
values ('John', 'Parsons', 'Jack', '(451)-783-4590', 'Информация об John Parsons - всемирном гиде из США');

insert into guide_language(guide_id, language_id) values (1,1);
insert into guide_language(guide_id, language_id) values (1,2);
insert into guide_language(guide_id, language_id) values (2,1);
insert into guide_language(guide_id, language_id) values (2,2);
insert into guide_language(guide_id, language_id) values (2,3);
insert into guide_language(guide_id, language_id) values (3,2);

insert into excursion(name, description, guide_id, cost, starts, ends, in_progress, passed, image_id)
values ('Экскурсия по Санкт-Петербургу', 'Описание экскурсии по Санкт-Петербургу', 1, 15000,
        '2026-04-10 10:00', '2026-04-10 17:00', false, false, null);

insert into excursion(name, description, guide_id, cost, starts, ends, in_progress, passed, image_id)
values ('Экскурсия по Москве', 'Описание экскурсии по Москве', 1, 25000, '2026-05-02 12:00', '2026-05-02 18:00',
        false,false, null);

insert into excursion(name, description, guide_id, cost, starts, ends, in_progress, passed, image_id)
values ('Экскурсия по Пекину', 'Описание экскурсии по Пекину', 2, 35000, '2026-05-04 10:30', '2026-05-04 17:00',
        false,false, null);

insert into excursion(name, description, guide_id, cost, starts, ends, in_progress, passed, image_id)
values ('Экскурсия по Вашингтону', 'Описание экскурсии по Вашингтону', 3, 45000, '2026-05-06 11:00', '2026-05-06 19:00',
        false,false, null);

insert into excursion_sight(excursion_id, sight_id) values (1,4);
insert into excursion_sight(excursion_id, sight_id) values (1,5);
insert into excursion_sight(excursion_id, sight_id) values (1,6);

insert into excursion_sight(excursion_id, sight_id) values (2,1);
insert into excursion_sight(excursion_id, sight_id) values (2,3);

insert into excursion_sight(excursion_id, sight_id) values (3,7);
insert into excursion_sight(excursion_id, sight_id) values (3,8);

insert into excursion_sight(excursion_id, sight_id) values (4,13);
insert into excursion_sight(excursion_id, sight_id) values (4,14);
insert into excursion_sight(excursion_id, sight_id) values (4,15);

insert into excursion_identity(excursion_id, identity_id) values (1, 1);
insert into excursion_identity(excursion_id, identity_id) values (2, 1);

insert into excursion_identity(excursion_id, identity_id) values (2, 2);
insert into excursion_identity(excursion_id, identity_id) values (3, 2);