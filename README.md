# Order Processing System

Учебный проект на Java — система обработки заказов интернет-магазина.

## Что реализовано

- Generics: параметризованный `Repository<T>`, утилитарный `CollectionUtils`
- Иерархия исключений: checked (`AppException`) и unchecked (`PaymentException`)
- Сущности: `Product`, `Order`, `OrderItem`, `OrderStatus`
- Сервисы: `ProductService`, `OrderService` с Dependency Injection

## Структура

```
src/shop/
├── interfaces/   — Identifiable
├── exceptions/   — иерархия исключений
├── model/        — сущности
├── repository/   — Repository<T>
├── utils/        — CollectionUtils
├── service/      — ProductService, OrderService
└── Main.java
```

## Запуск

Требуется Java 21+. Открыть в IntelliJ IDEA и запустить `Main.java`.
