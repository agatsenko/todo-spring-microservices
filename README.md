# todo-spring-microservices

Простая реализация микросервисов на основе [Spring Cloud](https://spring.io/projects/spring-cloud).

## Структура проекта

  * libs - содержит библиотечные модули
    * todo-util - содержит реализацию общих базовых утилитных компонентов
    * todo-test - содержит реализацию общих и базовых компонентов для тестов
    * todo-service-common - содержит реализация общих и базовых компонентов для микросервисов
  * infrastructure - содержит инфраструтурные модули
    * todo-mongo - содержит конфигурацию Mongo DB
    * todo-config-server - модуль конфигурации сервисов
([Spring Cloud Config](https://spring.io/projects/spring-cloud-config))
    * todo-eureka-server - модуль сервера обнаружения сервисов
([Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix#overview))
    * todo-zuul-server - модуль роутера микросервисов
([Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix#overview))
  * services - содержит модули микросервисов
    * todo-auth-service - модуль аутентификации и авторизации пользователей. Используется OAuth2 аутентиикация с
поддержкой JWT токенов
    * todo-auth-service-client - Feign-клиент микросервиса todo-auth-service
    * todo-list-service - CRUD модуль TODO списков

## Сборка и запуск проекта

Для сборки необходимо предустановить следующее инструменты:
  * [Java JDK 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot)
  * [Gradle v. 5.5+](https://gradle.org)
  * Docker v. 2.x+
 
Для сборки проекта необходимо выполнить следующую команду:

```
gradle clean build dockerBuildImage
```

Для запуска проекта необходимо выполнить следующую команду:

```
gradle composeUp
```

Для остановки проекта необхоидмо выполнить следующую команду:

```
gradle composeDown
```

## Мануальное тестирование

### Получение Oauth2 токена

Запрос:

```
curl -i -X POST --user "browser:" -d "grant_type=password&username=test_user&password=test_user&scope=ui" http://localhost:8000/uaa/oauth/token
```

Ответ:

```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoiZjc2MzAwMDUtOTM0Mi00ODlkLWE4Y2EtYWFhMjViMmFkMzVkIiwic2NvcGUiOlsidWkiXSwiZXhwIjoxNTY5Mjc0MzQzLCJ0b2tlbl90eXBlIjoiYWNjZXNzIiwianRpIjoiNWNlZmViYjItYzFiNy00MGFhLWFhMGYtMzExYmM0MDBhNzkxIiwiY2xpZW50X2lkIjoiYnJvd3NlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJ1c2VybmFtZSI6InRlc3RfdXNlciJ9.YWBu-NufGXGGJc81ds70vKesE5xnpwKOhtRiOSwXnrfFyKpCEcjgqOx96R5yrwg3oZ8OuAoOy_jV3Dpm3k7nfRtTyXRd9BCdbRYa5FqkAWVnIoEXOR8jCh_22qHngUPx_nYIMfP0k3iLPX5XyusJ91gu7vWc6mkV6KazKqyrNI5_eb2F5Pg1hWcBMMlwz4cbcW-mj0eO-eYtFYIZK7Erxt2uViOTAIjob4YeRJyM42gekz9tD2iqVLd-EilY0v4Q7WYyBII-ClEhF-u-bdvXX7W3TwSMCeogXu7t25CwRFNAl4BjaF1GqeBChCh-eFl-dqWeBS9wun1DuZas0Y89gA",
  "token_type": "Bearer",
  "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoiZjc2MzAwMDUtOTM0Mi00ODlkLWE4Y2EtYWFhMjViMmFkMzVkIiwic2NvcGUiOlsidWkiXSwiZXhwIjoxNTY5MzU3MTQzLCJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImp0aSI6IjY1MDUxM2I4LWVhODctNDhkNi1iYTM4LTU1YTY3ZGUxN2Y4MiIsImNsaWVudF9pZCI6ImJyb3dzZXIiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIifQ.Pu2iCJBhi5FCa8KTA8Ppy1fiki3XTeF8ZkQgCA-VXWatOd7XETwTETyEoA7ArWHvXb6JS0WWbj8F5S7yMRCiUnusBJpKjGezaDLaYxywTzkXoTAMD5bqMOTyKEoWpb_xghqXWS0RSbbmEQ0rHY86N9e0fi_cE26ZYSLNQV3rNLBgFiiwC82WH1HyK_Oo_KRtAAPVglvs4qhq_LGL0n_LT-m23M9tb4Ag1Dzf6cU0pm3UoZiDlf6TBUAdTm_mC1WufOYJqc2wypvVlU9AO9jjFCCEW9KWFg_8dgQvtNvkZH2n2FoCoAziyw9bQY3-smN13LlfkDrXbi9ztNS-1Lrgwg",
  "expires_in": 3136,
  "scope": "ui"
}
```

В последующих запросах будет использоваться "access_token", значение которого будет заменено на $TOKEN.

### Получение всех TODO списков

Запрос:

```
curl -i -H "Authorization: Bearer $TOKEN" -X GET http://localhost:8000/list/api/task-list
```

Ответ:

```json
[
  {
    "id": "414df218-a828-40fe-ac83-e084bce8b26e",
    "version": 1,
    "name": "list 1",
    "tasks": [
      {
        "id": "9ef9b8f0-09b8-4a74-8cb0-667b24ab6cc5",
        "description": "task 1.2",
        "completed": true,
        "order": 2
      },
      {
        "id": "43d6154c-c145-442d-b701-8bb496a440f4",
        "description": "task 1.1",
        "completed": false,
        "order": 1
      },
      {
        "id": "72297878-a155-426f-b9da-a7e1fe8281d5",
        "description": "task 1.3",
        "completed": false,
        "order": 3
      }
    ]
  },
  {
    "id": "60e6cea2-6927-443c-8471-5dbaac971da6",
    "version": 1,
    "name": "empty list",
    "tasks": []
  },
  {
    "id": "565f38d8-d7b6-4d58-bef1-a655387fca47",
    "version": 1,
    "name": "list 2",
    "tasks": [
      {
        "id": "d138b9a1-a666-44fc-b52e-43f502e825a9",
        "description": "task 2.2",
        "completed": true,
        "order": 2
      },
      {
        "id": "19a49a63-620c-485d-889e-27ca1ef6080c",
        "description": "task 2.1",
        "completed": false,
        "order": 1
      },
      {
        "id": "e8697b01-35f6-48b6-baee-ea1b32efb9ff",
        "description": "task 2.3",
        "completed": false,
        "order": 3
      }
    ]
  },
  {
    "id": "9a703409-2688-4aec-858b-0600f5fdc6bc",
    "version": 3,
    "name": "list 3",
    "tasks": [
      {
        "id": "24731d17-b227-4fb1-a437-0b72685bce0b",
        "description": "task 3.1",
        "completed": false,
        "order": 1
      },
      {
        "id": "bc553d64-2f48-4573-b66d-b72d526f49aa",
        "description": "task 3.3",
        "completed": false,
        "order": 3
      },
      {
        "id": "afe7e603-ca20-49c6-a50a-1bf6142b9375",
        "description": "task 3.2",
        "completed": true,
        "order": 2
      }
    ]
  }
]
```

### Получение TODO списка по заданному ID списка

Запрос:

```
curl -i -H "Authorization: Bearer $TOKEN" -X GET http://localhost:8000/list/api/task-list/565f38d8-d7b6-4d58-bef1-a655387fca47
```

Ответ:

```json
{
  "id": "565f38d8-d7b6-4d58-bef1-a655387fca47",
  "version": 1,
  "name": "list 2",
  "tasks": [
    {
      "id": "d138b9a1-a666-44fc-b52e-43f502e825a9",
      "description": "task 2.2",
      "completed": true,
      "order": 2
    },
    {
      "id": "19a49a63-620c-485d-889e-27ca1ef6080c",
      "description": "task 2.1",
      "completed": false,
      "order": 1
    },
    {
      "id": "e8697b01-35f6-48b6-baee-ea1b32efb9ff",
      "description": "task 2.3",
      "completed": false,
      "order": 3
    }
  ]
}
```

### Добавлние нового TODO списка

Запрос:

```
curl -i -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"name":"list 5","tasks":[{"description":"task 5.1","completed":false,"order":1}]}' -X POST http://localhost:8000/list/api/task-list
```

Ответ:

```json
{
  "id": "316aae8e-7004-4f31-9e09-4641774412b8",
  "version": 1,
  "name": "list 5",
  "tasks": [
    {
      "id": "667f602e-0432-439f-9213-10a33d81b154",
      "description": "task 5.1",
      "completed": false,
      "order": 1
    }
  ]
}
```

### Обновление TODO списка

Запрос:

```
curl -i \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '
{
  "id": "565f38d8-d7b6-4d58-bef1-a655387fca47",
  "version": 1,
  "name": "list 2 updated",
  "newTasks": [
    {
      "description": "task 2.new.1"
    },
    {
      "description": "task 2.new.2",
      "completed": true,
      "order": 741
    }
  ],
  "updateTasks": [
    {
        "id": "e8697b01-35f6-48b6-baee-ea1b32efb9ff",
        "description": "task 2.3.updated",
        "completed": true,
        "order": -3
    }
  ],
  "removeTasks": [
    {
      "id": "d138b9a1-a666-44fc-b52e-43f502e825a9"
    }
  ]
}
     ' \
     -X PUT http://localhost:8000/list/api/task-list
```

Ответ:

```json
{
  "id": "565f38d8-d7b6-4d58-bef1-a655387fca47",
  "version": 2,
  "name": "list 2 updated",
  "tasks": [
    {
      "id": "6e6e27c2-d4db-4c85-b6f3-7a9d0fdb2328",
      "description": "task 2.new.2",
      "completed": true,
      "order": 741
    },
    {
      "id": "e8697b01-35f6-48b6-baee-ea1b32efb9ff",
      "description": "task 2.3.updated",
      "completed": true,
      "order": -3
    },
    {
      "id": "a95dddca-910a-4149-a2bb-10c16300d066",
      "description": "task 2.new.1",
      "completed": false,
      "order": 2147483647
    },
    {
      "id": "19a49a63-620c-485d-889e-27ca1ef6080c",
      "description": "task 2.1",
      "completed": false,
      "order": 1
    }
  ]
}
```

### Удаление TODO списка

Запрос:

```
LIST_ID=565f38d8-d7b6-4d58-bef1-a655387fca47
```

```
LIST_VERSION=2
```

```
curl -i -H "Authorization: Bearer $TOKEN" -X DELETE http://localhost:8000/list/api/task-list/$LIST_ID/$LIST_VERSION
```

Ответ:

```
HTTP/1.1 204
```
