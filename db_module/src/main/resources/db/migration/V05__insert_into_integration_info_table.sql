INSERT INTO integration_info
VALUES
(2, 'X-Yandex-API-Key', 'yandex_weather', 'NjNmM2E2MWMtYzhiMC00MTMwLWI2NDQtMGI5YWJhZDI0Zjcz', 'https://api.weather.yandex.ru/v2/forecast?lat=%s&lon=%s'),
(3, 'GEO-02', 'opencagedata', 'Mzk5YzExY2NhMjA2NDAwOGJhNDJjMzAxYWY5OGM5MDY=', 'https://api.opencagedata.com/geocode/v1/json?q=%s,+%s&no_annotations=1&key=%s')