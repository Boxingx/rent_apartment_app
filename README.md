Приложение предназначено для аренды квартир посуточно. У пользователей есть возможность регистрироваться в статусе арендодателя(коммерческий статус), если он хочет сдавать свои апартаменты, а так же в качестве арендатора, если он хочет снять квартиру. Для арендатора есть возможность поиска свободных апартаментов по геолокации пользователя, возможность оставлять отзывы, а так же получать уведомления на почту с прикрепленными фотографиями квартиры, а для арендодателя есть возможность регистрировать новые квартиры под сдачу в аренду.

Приложение представляет собой Rest API с микросервисной архитектурой на базе Spring Boot, версия 3.1.3. Микросервисы зарегистрированы на Eureka Server(модуль - server_module), маршрутизация запросов производится через API Gateway(модуль proxy_module), в связи с этим остальные модули используют нулевые порты. Взаимодействие между микросервисами происходит с помощью RestTemplate, а также используется реляционная база данных Postgres. Начальная информация вносится в базу данных через SQL скрипты с помощью Flyway migration. Маппинг полей между Entity и DTO происходит с помощью mapstruct, также в микросервисах написани unit тесты.

