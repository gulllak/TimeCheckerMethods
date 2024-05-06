### TimeCheckerMethods - система асинхронного и синхронного учета времени выполнения методов с использованием Spring AOP.

#### Применение
_Используйте аннотации **@TrackTime** и **@TrackAsyncTime** над методами в коде своего приложения. Аспекты, будут автоматически обрабатывать ваши методы и сохранять информацию о времени их выполнения, для последуюзего анализа._


#### Сохраняются следующие данные о методе:
- Имя пакета и класс в котором вызван метод
- Имя метода
- Время за которое выполнился метод
- Дата и время выполнения метода

##### REST API для получения статистики по времени выполнения методов:

Для простоты тестирования api для получения статистики, в корневой директории проекта находится файл **_TimeChecker.postman_collection.json_**, который можно импортировать в Postman. 

1. Возможность получить среднее время выполнения конкретного метода.
Запрос можно выполнить по следующему эндпоинту:
```
/stats/average/method?className=ИмяКласса&methodName=ИмяМетода
```
Получаем ответ в следующем формате:
```
   {
    "methodName": "testTrackTimeAnnotation",
    "executionTimeAvg": 3503.3333333333335,
    "numberOfcalls": 6
}
   ```
2. Возможность получить среднее время выполнения всех методов определенного класса.
   Запрос можно выполнить по следующему эндпоинту:
```
/stats/average/class?className=имяКласса
```
Получаем ответ в следующем формате:
```
{
    "packageName": "ru.evgenii.timecheckermethods.service.impl",
    "className": "TestMethodsImpl",
    "methodStatistics": [
        {
            "methodName": "testTrackTimeAnnotation",
            "executionTimeAvg": 3503.3333333333335,
            "numberOfcalls": 6
        },
        {
            "methodName": "testTrackTimeAsyncAnnotationVoid",
            "executionTimeAvg": 3504.8333333333335,
            "numberOfcalls": 10
        },
        {
            "methodName": "testTrackTimeAsyncAnnotationReturn",
            "executionTimeAvg": 3504.8333333333335,
            "numberOfcalls": 41
        }
    ]
}
   ```

3. Возможность получить среднее время выполнения всех методов в классах определенного пакета.
   Запрос можно выполнить по следующему эндпоинту:
```
/stats/average/package?packageName=адресПакета
```
Получаем ответ в следующем формате:
```
[
    {
        "packageName": "ru.evgenii.timecheckermethods.service.impl",
        "className": "TestMethodsImpl",
        "methodStatistics": [
            {
                "methodName": "testTrackTimeAnnotation",
                "executionTimeAvg": 3503.3333333333335,
                "numberOfcalls": 10
            },
            {
                "methodName": "testTrackTimeAsyncAnnotationVoid",
                "executionTimeAvg": 3504.8333333333335,
                "numberOfcalls": 45
            },
            {
                "methodName": "testTrackTimeAsyncAnnotationReturn",
                "executionTimeAvg": 3504.8333333333335,
                "numberOfcalls": 21
            }
        ]
    },
    ...
]
   ```

## Как запустить

1. Склонировать проект.
    ```bash
   $ git clone https://github.com/gulllak/TimeCheckerMethods.git
   ```
2. Создать БД и прописать значиния в application.properties
    ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/***
   spring.datasource.username=***
   spring.datasource.password=***
   ```
3. Запустить проект локально.
4. Перейти в телеграм для тестирования бота.
5. Подробная документация по эндпоинтам доступна после запуска проекта локально по адресу http://localhost:8080/swagger-ui.html

### 🏄 Стек:
Java 21, SpringBoot 3, PostgreSQL, Liquibase, Maven, Spring AOP, SpringDoc OpenAPI.
