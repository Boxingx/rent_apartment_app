Приложение предназначено для аренды квартир посуточно. У пользователей есть возможность регистрироваться в статусе арендодателя(коммерческий статус), если он хочет сдавать свои апартаменты, а так же в качестве арендатора, если он хочет снять квартиру. Для арендатора есть возможность поиска свободных апартаментов по геолокации пользователя, возможность оставлять отзывы, а так же получать уведомления на почту с прикрепленными фотографиями квартиры, а для арендодателя есть возможность регистрировать новые квартиры под сдачу в аренду.

Приложение представляет собой Rest API с микросервисной архитектурой на базе Spring Boot, версия 3.1.3. Микросервисы зарегистрированы на Eureka Server(модуль - server_module), маршрутизация запросов производится через API Gateway(модуль proxy_module), в связи с этим остальные модули используют нулевые порты. Взаимодействие между микросервисами происходит с помощью RestTemplate, а также используется реляционная база данных Postgres. Начальная информация вносится в базу данных через SQL скрипты с помощью Flyway migration. Маппинг полей между Entity и DTO происходит с помощью mapstruct, также в микросервисах написани unit тесты.

Приложение состоит из следующих модулей: 

1) server_module - в нем развернут Eureka Server.
2) proxy_module - в нем развернут API Gateway.
3) db_module - содержит скрипты, которые запускают flyway миграции и вносят начальные значения в БД такие как квартиры и их адреса, пользователи, история букинга, а также данные для интеграции со сторонними сервисами и тп.
4) rent_module - основной модуль который несет основной функционал приложения.
5) product_module - модуль для расчета определенной скидки(продукта), а так же для отправки уведомлений на электронную почту.

Процесс работы приложения состоит из нескольких этапов.

1 ЭТАП - регистрация пользователей, после которой пользователь получает временный токен.

Регистрация арендодателя

![1reg_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/73d751a2-318c-4dee-abcb-c60f1336d71d)

Регистрация арендатора

![2reg_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/27b384a5-46fc-48d7-9706-d0fe12f8842b)

Если такой пользователь уже существует получим сообщение об этом.

![3wrong_reg](https://github.com/Boxingx/rent_apartment_app/assets/130319720/e6ca6600-361b-42c9-8713-1f315dda1877)

2 ЭТАП - аутентификация пользователей

Аутентификация арендатора

![4auth_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/21b5b8a0-f587-4a8a-948e-01306ccb3cad)


Аутентификация арендатора

![5auth_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/e8b259a7-d145-4de9-b7a4-69f90da25bc4)

При неверном пароле получаем уведомление 

![wrong_pass](https://github.com/Boxingx/rent_apartment_app/assets/130319720/a89c86ad-4631-48c3-9e54-0b85e519b705)


3 ЭТАП - арендодатель добавляет квартиру которую хочет сдать в аренду

![6reg_new_apt_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/40170dc6-0c38-4f78-97a6-1d494f9b1657)

4 ЭТАП - арендодатель добавляет фотографии к квартире, которую сдает в аренду

Можно добавить до 5 фото, с ограниченным размером

![7add_photo_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/4a27e078-9748-47d5-8c46-ff603339d671)

5 ЭТАП - выгрузка квартир по определенным фильтрам, либо по локации

Фильруем квартиры по подходящим нам параметрам

![8apt_list1](https://github.com/Boxingx/rent_apartment_app/assets/130319720/c6e28921-f6aa-4006-a1a1-4b79ccc4868d)

Получаем такой ответ после выгрузки по определенным фильтрам

![9apt_list2](https://github.com/Boxingx/rent_apartment_app/assets/130319720/91a914ba-d365-4d0f-ab03-ebf10de200e1)

Для выгрузки квартир по локации просто передаем широту и долготу

![10location1](https://github.com/Boxingx/rent_apartment_app/assets/130319720/10d76e37-915a-4176-8267-a0596e97232f)

Получаем такой ответ после выгрузки по локации

![11location2](https://github.com/Boxingx/rent_apartment_app/assets/130319720/d753a21f-de02-4e16-9056-b0a7191afe25)

6 ЭТАП - бронирование подходящих апартаментов

Так выглядит запрос для бронирования

![12book_apt_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/b5b7b395-d54c-4597-9d6a-2251bafcff01)

7 ЭТАП - получение уведомления о бронировании на электронную почту

После бронирования сразу получаем об этом письмо на почту

![13book_mail_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/6acb7896-57f8-483d-8795-2d1dcae29b9e)

8 ЭТАП - получение электронного сообщения по окончании бронирования с просьбой оставить отзыв

![14review_mail_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/aac95def-426d-408e-a85b-b874a048c092)

Оставляем отзыв

![15review_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/eac6119e-3f5e-4680-8502-e13d845f46ee)



---------------------------------------------------------------------------------------------------------------
Данные которые хранятся в БД

Таблица квартир

![apt_db](https://github.com/Boxingx/rent_apartment_app/assets/130319720/dd88df12-7ad2-45dc-8aed-b18d525594fb)

Таблица пользователей

![users_db](https://github.com/Boxingx/rent_apartment_app/assets/130319720/e96d2d51-bbd8-487e-aaf1-85d18fb8ae54)

Таблица бронирований

![booking_history](https://github.com/Boxingx/rent_apartment_app/assets/130319720/73fc7498-eb70-4a36-8824-e9f67dd18814)

Таблица скидок

![product_db](https://github.com/Boxingx/rent_apartment_app/assets/130319720/9c972c4c-c4b9-4060-936a-24c3587f175b)

Таблица отзывов

![ratings_db](https://github.com/Boxingx/rent_apartment_app/assets/130319720/098a1dc3-7428-4e91-8295-21cbe2a58bc2)


EUREKA DASHBOARD SCREENSHOT

![eureka](https://github.com/Boxingx/rent_apartment_app/assets/130319720/eb403003-03c3-43ce-98fd-afc12926ce11)












