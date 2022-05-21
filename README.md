# Учебный проект на платформе hyperskill.org

## Learning outcomes
In this project, you will create a simple Spring REST service that will help you manage a small movie theatre.
Handle HTTP requests in controllers, create services and respond with JSON objects.

## Цель проекта создать простое приложение со следующим REST API:
* Получение списка свободных (доступных для продажи) мест:
  `curl --request GET --url http://localhost:28852/seats`
* Покупка места:
```
curl --request POST \
  --url http://localhost:28852/purchase \
  --header 'Content-Type: application/json' \
  --data '{
  "row": 1,
  "column": 1
  }'
  ```
* Возврат билета:
```
curl --request POST \
  --url http://localhost:28852/return \
  --header 'Content-Type: application/json' \
  --data '{
    "token": "ce84837c-080e-4467-9da7-b24d8083cf83"
}'
```
* Просмотр информации о состоянии зала:
```
curl --request POST \
  --url 'http://localhost:28852/stats?password=super_secret'
```

## Состав проекта:
* dt/edu/cinemaroomrestservice/model
    * CinemaHall - объект "зал кинотеатра"
    * HallPlace - место в зале
    * PlacePosition - позиция места в зале
    * Исключения:
        * PlaceNotFoundException - заданное место не найдено в зале
        * PlaceNotSoldException - указанное место не продано
        * PlaceSoldException - указанное место продано
        * WrongTokenException - не верный токен проданного билета (не найден)
* dt/edu/cinemaroomrestservice/controllers/rest
    * SeatsController - REST контроллер с требуемыми по заданиям операциям
    * AuthException - исключение авторизации
    * BadRequestException - исключение неверного формата запроса


