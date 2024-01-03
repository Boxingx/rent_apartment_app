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
![регистрация арендодателя](https://github.com/Boxingx/rent_apartment_app/assets/130319720/f86dd16c-3e65-4881-9b02-23c659d7d730)
![регистрация арендатора](https://github.com/Boxingx/rent_apartment_app/assets/130319720/f12cafdc-0c12-4088-a32c-49cd9c5899de)
![если такой пользователь уже существует](https://github.com/Boxingx/rent_apartment_app/assets/130319720/35a2e2d2-f94c-41ce-92bf-2e0dc942ba6e)

2 ЭТАП - аутентификация пользователей
![4auth_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/27af5f45-1aab-41e4-b89b-d59ebe1eed68)
![5auth_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/fedd1ee8-7e93-469d-84a3-75cd28424849)



3 ЭТАП - арендодатель добавляет квартиру которую хочет сдать в аренду
![6reg_new_apt_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/d835e2f2-e6c4-4472-b53f-7d3032726362)


4 ЭТАП - арендодатель добавляет фотографии к квартире(до 5 фото, с ограниченным размером), которую сдает в аренду
![7add_photo_alex](https://github.com/Boxingx/rent_apartment_app/assets/130319720/84592a0c-0f89-4de1-be22-a011dfadf533)

5 ЭТАП - выгрузка квартир по определенным фильтрам, либо по локации
![8apt_list1](https://github.com/Boxingx/rent_apartment_app/assets/130319720/80c001df-c543-4a05-84ae-8e23a3ef3696)
![9apt_list2](https://github.com/Boxingx/rent_apartment_app/assets/130319720/d4d95dea-a1ee-47c1-b40d-e6e7b4d3929a)
![10location1](https://github.com/Boxingx/rent_apartment_app/assets/130319720/efb2f121-6863-4088-b2fd-a9770c9212a1)
![11location2](https://github.com/Boxingx/rent_apartment_app/assets/130319720/48d1159f-3f20-4827-86b6-4337f5e3720b)

6 ЭТАП - бронирование подходящих апартаментов
![12book_apt_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/7bc65bdf-e6f3-44bb-953a-1a8a62a81fee)

7 ЭТАП - получение уведомления о бронировании на электронную почту
![13book_mail_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/f990a882-157e-4f30-a69b-e3f19a848b3e)

8 ЭТАП - получение электронного сообщения по окончании бронирования с просьбой оставить отзыв
![14review_mail_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/ef89c6c3-ea86-4951-bbd0-b903459f7930)
![15review_ivan](https://github.com/Boxingx/rent_apartment_app/assets/130319720/a3c69053-7675-47fc-99a2-2f9215f065a3)






