
 ЗАДАНИЕ
========================
 
 Разработать cli-магазин для покупки товаров
-------------------------

 Какие сущности должны быть созданы:

Класс "Order" - заказ.

Параметры:
1. ID - идентификатор заказa
2. Items - список товаров заказа.
3. Total amount - общая сумма заказа. ( выделить в отдельный класс)

Класс "Item" - товар

Параметры:
1. ID - идентификатор товара
2. Name - наименование товара
3. builder.Category - категория товаров. ( выделить в отдельный класс)
3. builder.Amount - стоимость и валюта. ( выделить в отдельный класс)

  Какие задачи необходимо сделать
-------------------------

1. Сделать cli с помощью которого можно производить покупку товара в магазине.
   - Пользователь должен иметь возможность проверить баланс своего кошелька.
   - Перейти к списку доступных для покупки товаров
   - Покупка товаров. (В случае, если у пользователя не хватает денег, нужно показать ему ошибку об этом)

2. Добавить раздел "Возврат товара", где пользователь может произвести возврат товара, который купил.
3. Добавить раздел "История покупок", где будет отражен список всех товаров, которые пользователь купил или вернул.
   - Добавить возможность выгрузить историю покупок в отдельный файл.
4. Добавить раздел "Статистика"
   - Добавить раздел "Топ продаж" - список best sellers по всем товарам
   - Добавить раздел "Популярная категория" - список best sellers по категориям
   - Добавить раздел "Оборот категорий" - итоговые суммы оборотов по каждой категории
5. Добавить раздел "Пополнение кошелька"
6. Заполнить магазин товарами.

  ! Заметки !
-------------------------

 Каждый основной раздел ("Покупка товаров", "Возврат товаров", ...) необходимо реализовать отдельным классом сервисом с интрфейсами. И использовать в работе строго интерфейсы.

 Что необходимо обязательно применить в ходе разработки:
-------------------------

1. Абстрактные классы и интерфейсы.
2. Паттерн "Builder" (хороший ресурс https://refactoring.guru/ (работает только с vpn))
3. Custom Exception. Т.е. пишем свое исключение, которое выбрасываем и где то ловим. (Хорошо подойдет для ошибки о недостатке денег на счету для покупки товара)
4. Stream Api.
5. Паттерн "Синглтон" (можно использовать для сервисов)
