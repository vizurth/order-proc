# Практическое задание: Система обработки заказов

## Цель

Закрепить на практике: checked и unchecked исключения, собственные исключения, цепочки исключений, finally, multi-catch, generics — параметризованные классы и методы.

---

## Контекст

Ты пишешь backend для интернет-магазина. Есть склад, каталог товаров и система заказов. Всё это должно корректно обрабатывать ошибочные ситуации и быть типобезопасным через generics.

---

## Часть 1 — Generics

### `Repository<T>`

Напиши параметризованный класс `Repository<T>` — универсальное хранилище объектов.

Поля:
- `private final List<T> items`

Методы:
- `void add(T item)`
- `T findById(int id)` — предполагается что у `T` есть метод `getId()`. Подумай как это выразить через ограничение типа `T extends Identifiable`
- `List<T> findAll()`
- `void remove(int id)`

### Интерфейс `Identifiable`

```java
public interface Identifiable {
    int getId();
}
```

Все сущности которые хранятся в `Repository` должны реализовывать этот интерфейс.

### Generic-метод

Напиши отдельный утилитарный класс `CollectionUtils` с методом:

```java
public static <T extends Comparable<T>> T findMax(List<T> list)
```

Метод находит максимальный элемент в списке. Если список пустой — бросает исключение.

---

## Часть 2 — Исключения

Реализуй следующую иерархию исключений:

```
AppException (checked, extends Exception)
├── ProductNotFoundException
├── OutOfStockException
└── InvalidOrderException
    └── EmptyOrderException

PaymentException (unchecked, extends RuntimeException)
├── InsufficientFundsException
└── PaymentTimeoutException
```

Каждый класс должен иметь:
- Конструктор с `message`
- Конструктор с `message` и `cause` — для цепочки исключений

---

## Часть 3 — Сущности

### `Product implements Identifiable`

Поля: `id`, `name`, `price`, `int stock` — количество на складе.

### `OrderItem`

Поля: `Product product`, `int quantity`.

Метод `double totalPrice()` — цена позиции с учётом количества.

### `Order implements Identifiable`

Поля: `id`, `List<OrderItem> items`, `OrderStatus status`.

`OrderStatus` — enum с состояниями: `PENDING`, `CONFIRMED`, `CANCELLED`.

Метод `double totalPrice()` — сумма всех позиций.

---

## Часть 4 — Сервисы

### `ProductService`

Использует `Repository<Product>` внутри.

Методы:
- `Product getProduct(int id) throws ProductNotFoundException` — если не найден бросай исключение
- `void reduceStock(int productId, int quantity) throws ProductNotFoundException, OutOfStockException` — уменьшает остаток на складе. Если товара нет — `ProductNotFoundException`. Если недостаточно на складе — `OutOfStockException`

### `OrderService`

Методы:
- `Order createOrder(List<OrderItem> items) throws InvalidOrderException` — если список пустой бросай `EmptyOrderException`. Для каждой позиции вызывай `reduceStock` у `ProductService` — пробрасывай исключения выше
- `void processPayment(Order order, double availableFunds) throws PaymentException` — если средств недостаточно бросай `InsufficientFundsException`. Симулируй таймаут через `Random` с вероятностью 10% — бросай `PaymentTimeoutException`

---

## Часть 5 — shop.Main

Покрой все следующие сценарии:

**Сценарий 1** — успешный заказ. Создай заказ, проведи оплату, выведи итог.

**Сценарий 2** — товар не найден. Попробуй получить несуществующий товар, поймай `ProductNotFoundException`.

**Сценарий 3** — недостаточно товара на складе. Попробуй заказать больше чем есть, поймай `OutOfStockException`.

**Сценарий 4** — пустой заказ. Передай пустой список в `createOrder`, поймай `EmptyOrderException` через `catch (InvalidOrderException e)` — это демонстрация полиморфизма исключений.

**Сценарий 5** — multi-catch. В одном блоке поймай `ProductNotFoundException | OutOfStockException` и обработай одинаково.

**Сценарий 6** — цепочка исключений. В `processPayment` поймай низкоуровневое исключение и оберни в `PaymentException` передав оригинал как `cause`. В `main` выведи `e.getCause()`.

**Сценарий 7** — `finally`. Оберни любую операцию в `try/finally` и покажи что блок `finally` выполняется всегда — и при успехе и при исключении. Можно симулировать закрытие ресурса.

---

## Требования к качеству

Никогда не глотай исключения пустым `catch`. Сообщения в исключениях должны быть информативными — указывай конкретные значения, например `"Product with id=5 not found"`. Checked исключения используй для ситуаций которые вызывающий код должен обработать. Unchecked — для ошибок программиста или непредвиденных сбоев.

---

## Структура пакетов

```
src/
└── shop/
    ├── interfaces/
    │   └── Identifiable.java
    ├── shop.exceptions/
    │   ├── AppException.java
    │   ├── ProductNotFoundException.java
    │   ├── OutOfStockException.java
    │   ├── InvalidOrderException.java
    │   ├── EmptyOrderException.java
    │   ├── PaymentException.java
    │   ├── InsufficientFundsException.java
    │   └── PaymentTimeoutException.java
    ├── shop.model/
    │   ├── Product.java
    │   ├── OrderItem.java
    │   ├── Order.java
    │   └── OrderStatus.java
    ├── shop.repository/
    │   └── Repository.java
    ├── shop.utils/
    │   └── CollectionUtils.java
    ├── service/
    │   ├── ProductService.java
    │   └── OrderService.java
    └── shop.Main.java
```

---

## Порядок реализации

Интерфейс `Identifiable` → иерархия исключений → модели → `Repository<T>` → `CollectionUtils` → сервисы → `shop.Main`.