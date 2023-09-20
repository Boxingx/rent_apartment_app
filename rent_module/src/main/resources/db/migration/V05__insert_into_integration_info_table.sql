INSERT INTO integration_info
VALUES
(2, 'X-Yandex-API-Key', 'yandex_weather', '37d2acfc-db96-4819-aa55-2602620f9305', 'https://api.weather.yandex.ru/v2/forecast?lat=%s&lon=%s'),
(3, 'GEO-02', 'opencagedata', '399c11cca2064008ba42c301af98c906', 'https://api.opencagedata.com/geocode/v1/json?q=%s,+%s&no_annotations=1&key=%s')