                   _____                          ______             __         
                  / __(_)__  ___ ____  _______   /_  __/______ _____/ /_____ ____
                 / _// / _ \/ _ `/ _ \/ __/ -_)   / / / __/ _ `/ __/  '_/ -_) __/
                /_/ /_/_//_/\_,_/_//_/\__/\__/   /_/ /_/  \_,_/\__/_/\_\\__/_/  

## Базовый функционал

- Команды: (для подробного описания используйте команду help)
    - Контоль аккаунтов
    - Контроль категорий
    - Контроль операций
    - Импорт/экспорт в json и "таблицы" (как в sql-клиентах)
    - Аналитика аккаунта, трат и поступлений

## Пример использования

```shell
create-account demo
```

```shell
add-category salary
```

```shell
add-income --amount 30000000000 --accountId <айди аккаунта> --categoryId <id созданной категории>
```

```shell
export-json --output ./test.json
```

## SOLID в проекте:

| Принцип | Описание                                                              | Где используется                                                                             |
|---------|-----------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| SRP     | Класс должен иметь только одну зону ответственности                   | *Factory - cоздают инстансы *Repository - взаимодействуют с БД                               |
| OCP     | Классы должны быть открыты для расширения, но закрыты для модификации | Command<T> AbstractExportVisitor AbstractDataImporter                                        |
| LSP     | Подтипы должны быть взаимозаменяемы со своими базовыми типами         | Иерархия команд (Command - общий предок,  затем разбиты по категориям)                       |
| ISP     | Много специализированных интерфейсов лучше, чем один общий            | Для каждой из сущностей (BankAccount, Category, Operation свои фабрики, репозитории, фасады) |
| DIP     | Зависимость от абстракций, а не от конкретных типов                   | Весь проект на spring IoC)                                                                   |

## Паттерны и GRASP в проекте:

| Паттерн                 | Описание                                                                   | Вид           | GRASP                             | В проекте                     |
|-------------------------|----------------------------------------------------------------------------|---------------|-----------------------------------|-------------------------------|
| Factory Method          | Определяет интерфейс для создания объекта                                  | Порождающий   | Creator                           | \*Factory.create\*()          |
| Builder                 | Конструирует сложный объект                                                | Порождающий   | Creator                           | TableBuilder                  |
| Facade                  | Предоставляет инкапсулированный интерфейс к сложной бизнес-логике          | Структурный   | Low coupling                      | *Facade                       |
| Proxy                   | Предоставляет заместителя другого объекта для декорирования доступа к нему | Структурный   | Indirection                       | spring @Transaction           |
| Chain of Responsibility | Позволяет передавать запросы по цепочке обработчиков                       | Поведенческий | Indirection                       | DSLContext из библиотеки JOOQ |
| Visitor                 | Позволяет добавлять новые операции к объектам без изменения их классов     | Поведенческий | Protected Variations,Polymorphism | *ExportVisitor                |

## В проекте я использовал:

- Spring как корень проекта и источник таких крутых штук как @Bean, @Service, @Repository
- JOOQ как ORM для взаимодействия с postgres
- jacoco для анализа покрытия тестами

## Для локального запуска надо:

- Поднять postgres и прокинуть его креды в application.properties
- Возможно инициализировать таблицы из schema.sql (если JOOQ по какой-то причине не сделает этого сам)
- Установить зависимости
- Сгенерировать JOOQ-файлы
- Запустить

## Внимание! Ковы:

- ![car1](owlcat1.webp)
- ![car2](owlcat2.jpg)
- ![car3](owlcat3.jpg)
- ![car4](owlcat4.jpg)
