# Wallet-Service

### How use:

##### You need java version: 17

##### Postgres Docker Outer Port: 54320

##### Tomcat Docker Outer Port: 8081

> docker-compose build

> docker-compose up -d

#### Open in browser:

_**postman v2.1: postman/wallet_service.postman_collection.json**_

#### Login

> POST http://localhost:8081/api/login

json:
> {
> "userId": "123",
> "password": "password"
> }

#### Logout

> GET http://localhost:8081/api/login

#### User actions (logged user only)

> GET http://localhost:8081/api/action

#### User transaction (logged user only)

> GET http://localhost:8081/api/transaction

#### Get all users list (logged user only)

> GET http://localhost:8081/api/user/all

#### Get users information By ID (logged user only)

> GET http://localhost:8081/api/user/{ID}

#### Logged user show balance (logged user only)

> GET http://localhost:8081/api/wallet

#### Logged user PUT money (logged user only)

> PUT http://localhost:8081/api/wallet

json:
> {
> "type": "PUT",
> "sum": "100.12"
> }

#### Logged user GET money (logged user only)

> PUT http://localhost:8081/api/wallet

json:
> {
> "type": "GET",
> "sum": "100.12"
> }



Enter number for navigate menu.

## ДЗ №3

### Домашнее задание №3 - Wallet-Service

- Необходимо обновить сервис, который вы разработали в первом задании согласно следующим требованиям и ограничениям

### Требования:

- Все взаимодействие должно теперь осуществляться через отправку HTTP запросов
- Сервлеты должны принимать JSON и отдавать также JSON
- Использовать понятное именование эндпоинтов
- Возвращать разные статус-коды
- Добавить DTO (если ранее не было заложено)
- Для маппинга сущностей в дто использовать MapStruct
- Реализовать валидацию входящих ДТО
- Аудит переделать на аспекты
- Также реализовать на аспектах выполнение любого метода (с замером времени выполнения)
- Сервлеты должны быть покрыты тестами
- Метод логина должен выдавать JWT, остальные методы должны быть авторизационными и валидировать JWT
- (В требования добавляется выдача токена и авторизация последующих запросов.)

## ДЗ №2

Необходимо обновить сервис, который вы разработали в первом задании согласно следующим требованиям и ограничениям

### Требования:

- Репозитории теперь должны писать ВСЕ сущности в БД PostgreSQL
- Идентификаторы при сохранении в БД должны выдаваться через sequence
- DDL-скрипты на создание таблиц и скрипты на предзаполнение таблиц должны выполняться только инструментом миграции
  Liquibase
- Скрипты миграции Luiqbase должны быть написаны только в нотации XML
- Скриптов миграции должно быть несколько. Как минимум один на создание всех таблиц, другой - не предзаполнение данными
- Служебные таблицы должны быть в отдельной схеме
- Таблицы сущностей хранить в схеме public запрещено
- В тестах необходимо использовать test-containers
- В приложении должен быть docker-compose.yml, в котором должны быть прописаны инструкции для развертывания postgre в
  докере. Логин, пароль и база должны быть отличными от тех, что прописаны в образе по-умолчанию. Приложение должно
  работать с БД, развернутой в докере с указанными параметрами.
- Приложение должно поддерживать конфиг-файлы. Всё, что относится к подключению БД, а также к миграциям, должно быть
  сконфигурировано через конфиг-файл

## ДЗ №1

Необходимо создать сервис, который управляет кредитными/дебетовыми транзакциями от имени игроков.

### Описание

Денежный счет содержит текущий баланс игрока. Баланс можно изменить, зарегистрировав транзакции на счете, либо дебетовые
транзакции (удаление средств), либо кредитные транзакции (добавление средств). Создайте реализацию, которая
соответствует описанным ниже требованиям и ограничениям.

### Требования

- данные хранятся в памяти приложений
- приложение должно быть консольным (никаих спрингов, взаимодействий с БД и тд, только java-core и collections)
- регистрация игрока
- авторизация игрока
- Текущий баланс игрока
- Дебет/снятие средств для каждого игрока Дебетовая транзакция будет успешной только в том случае, если на счету
  достаточно средств (баланс - сумма дебета >= 0). - Вызывающая сторона предоставит идентификатор транзакции, который
  должен быть уникальным для всех транзакций. Если идентификатор транзакции не уникален, операция должна завершиться
  ошибкой.
- Кредит на игрока. Вызывающая сторона предоставит идентификатор транзакции, который должен быть уникальным для всех
  транзакций. Если идентификатор транзакции не уникален, операция должна завершиться ошибкой.
- Просмотр истории пополнения/снятия средств игроком
- Аудит действий игрока (авторизация, завершение работы, пополнения, снятия и тд)

## Нефункциональные требования

Unit-тестирование

добавил github workflows
добавил lombok
добавил postman test