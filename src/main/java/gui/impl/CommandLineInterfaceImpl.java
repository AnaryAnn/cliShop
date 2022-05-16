package gui.impl;

import exceptions.OrderException;
import exceptions.WalletException;
import gui.api.CommandLineInterface;
import model.Currency;
import model.*;
import services.api.ItemService;
import services.api.OrderService;
import services.api.StatisticService;
import services.api.WalletService;
import services.impl.ItemServiceImpl;
import services.impl.OrderServiceImpl;
import services.impl.StatisticServiceImpl;
import services.impl.WalletServiceImpl;

import java.util.*;

import static model.Currency.*;

//todo: [Review] У классов (это относится и к сервисам) и методов не хватает комментов
public class CommandLineInterfaceImpl implements CommandLineInterface {

    private final WalletService walletService = WalletServiceImpl.getInstance();
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private final StatisticService statisticService = new StatisticServiceImpl();
    private final ItemService itemService = new ItemServiceImpl();
    private Long userId;


    @Override
    public void run() throws WalletException, OrderException {
        try (Scanner scanner = new Scanner(System.in)) {
            //todo: [Review] Если ввести что то вместо цифры летит эксепшн и завершается работа
            auth(scanner);
            showCli(scanner);
        }
    }

    //todo: [Review] Ошибка в этой функции не дает юзеру шансов вернуться в магазин
    private void auth(Scanner scanner) {
        System.out.println("Пожалуйста, введите идентификатор пользователя");
        //todo: [Review] NPE если ввести что то кроме числа
        //todo: [Review] отрицательные числа прокатывают, это норм?
        userId = Long.valueOf(askInt(scanner));
        walletService.createWallet(userId);
    }

    //todo: [Review] OrderException нужен?
    private void showCli(Scanner scanner) throws WalletException, OrderException {
        Integer intOption;
        do {
            intOption = null;

            System.out.println("----------------------------------");
            System.out.printf("Идентификатор пользователя: %s\n", userId);
            System.out.println("Баланс кошелька: ");
            Arrays.stream(values())
                    .forEach(currency -> System.out.printf("%21s %s\n",
                            getBalance(currency), currency));
            System.out.println("----------------------------------");
            //todo: [Review] "Введите номер товара для покупки в диапазоне от 1 до 26", это работает до тех пор пока у тебя не удалится какой-то товар, ведь первая цифра это идентификатор товара, а не порядковый номер.
            //todo: [Review] 0 кстати прокатывает, несмотря на сообщение выше
            System.out.println("1 - Список доступных товаров");
            System.out.println("2 - Возврат заказа");
            //todo: [Review] Нужна сортировка
            System.out.println("3 - История покупок");
            //todo: [Review] Если нет никакой информации по статистике, то лучше отрисовывоть что типо "Нет данных для сбора статистики", или что то в этом роде, как ты сделала с историей заказов
            System.out.println("4 - Статистика");
            //todo: [Review] Допускается пополнение кошелька на 0 рублей
            //todo: [Review] Допускается пополнение кошелька на 9999999999999999999999999999999999999999999999999999999999999999999999999999999999... рублей и как итог Infinity на счету
            System.out.println("5 - Пополнение кошелька");
            //todo: [Review] [Bug100500%] Можно зайти под другим юзером и сделать возврат всех заказаов другого юзера
            System.out.println("6 - Логаут");
            System.out.println("0 - Выход");

            while (intOption == null) {
                intOption = askInt(scanner);
                scanner.nextLine();
            }
            switch (intOption) {

                case 1: {
                    getItems(scanner);
                }
                break;

                case 2: {
                    refund(scanner);
                }
                break;

                case 3: {
                    history(scanner);
                }
                break;

                case 4: {
                    statistic(scanner);
                }
                break;

                case 5: {
                    deposit(scanner);
                }
                break;

                case 6: {
                    auth(scanner);
                }
                break;

                case 0:
                default:
                    break;
            }
        }
        while (intOption != 0);
    }

    //todo: [Review] может getCurrentUserBalance?
    private double getBalance(Currency currency) {
        Double result = walletService.getBalance(userId).get(currency);
        return result == null ? 0 : result;
    }

    //todo: [Review] Метод, который называется getItems сразу подразумевает, что он должен вернуть какие то значения, тут скорее всего лучше юзать, что то типа showAllItems
    private void getItems(Scanner scanner) {
        Integer intOption;
        do {
            intOption = null;

            //todo: [Review] Если товара нет, то почему break сразу? Скорее всего нужен какой-то мессадж об этом
            List<Item> items = itemService.getItems();
            if (items.isEmpty()) {
                break;
            }

            System.out.println("№ | Название товара | Цена");
            items.forEach(item -> System.out.printf("%s | %s | %s %s\n", item.getId(), item.getName(),
                    item.getAmount().getSum(), item.getAmount().getCurrency()));

            //todo: [Review] То самое место, где 0 прокатывает
            while (intOption == null || intOption < 0 || intOption > items.size()) {
                System.out.printf("\nВведите номер товара для покупки в диапазоне от %s до %s\n", 1, items.size());
                System.out.println("0 - Назад");
                intOption = askInt(scanner);
                scanner.nextLine();
            }

            if (intOption != 0) {
                Long orderId = null; //todo: [Review] Тут даже идея подсказывает, что инициализация не нужна
                try {
                    orderId = orderService.createOrder(userId, Collections.singleton(items.get(intOption - 1)));
                    orderService.payment(userId, orderId);
                } catch (OrderException | WalletException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                System.out.println("Заказ №" + orderId + " успешно оплачен");
            }
        }//todo: [Review] тут ниже зачем пропуск?

        while (intOption != 0);
    }

    //todo: [Review] название метода вообще не говорящее, должно начинаться с глагола
    private void statistic(Scanner scanner) {
        Integer intOption;
        do {
            intOption = null;

            System.out.println("Выберите раздел");
            System.out.println("1 - Топ продаж");
            System.out.println("2 - Популярная категория");
            System.out.println("3 - Оборот категорий");
            System.out.println("0 - Назад");

            while (intOption == null) {
                intOption = askInt(scanner);
                scanner.nextLine();
            }

            switch (intOption) {
                case 1:
                    Map<Item, Long> bestSellers = statisticService.getBestSellers();
                    System.out.println("Название товара | Кол-во продаж");
                    bestSellers.entrySet().stream()
                            .sorted(Map.Entry.<Item, Long>comparingByValue().reversed())
                            .limit(10) //todo: [Review] не самое хорошее решение обрезать это тут, лучше запрашивать уже с лимитов в 10 у сервиса, UI должен только отрисовать список, который ему прислали
                            .forEach(entry -> System.out.printf("%s | %s \n", entry.getKey().getName(), entry.getValue()));
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Категория | Кол-во продаж");
                    Map<Category, Long> bestSellersCategory = statisticService.getBestSellerCategory();
                    bestSellersCategory.entrySet().stream()
                            .sorted(Map.Entry.<Category, Long>comparingByValue().reversed())
                            .limit(10) //todo: [Review] аналогично с тем, что выше
                            .forEach(entry -> System.out.printf("%s | %s \n", entry.getKey().getName(), entry.getValue()));
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Категория | Оборот RUB");
                    Map<Category, Double> circulationMoney = statisticService.getCirculationMoney();
                    circulationMoney.entrySet().stream()
                            .sorted(Map.Entry.<Category, Double>comparingByValue().reversed())
                            .limit(10) //todo: [Review] аналогично с тем, что выше
                            .forEach(entry -> System.out.printf("%s | %s \n", entry.getKey().getName(), entry.getValue()));
                    System.out.println();
                    break;
                case 0:
                default:
                    break;
            }

        }
        while (intOption != 0);
    }


    //todo: [Review] нейминг и пропуски лишние выше
    private void history(Scanner scanner) {
        Integer intOption;

        do {
            intOption = null;
            System.out.println("История заказов:");
            Set<Order> orders = statisticService.getOrderHistory(userId);
            if (orders.size() == 0) {
                System.out.println("\nУ пользователя нет заказов");
                System.out.println("\n0 - Назад");

                while (intOption == null) {
                    intOption = askInt(scanner);
                    scanner.nextLine();
                }
            } else {
                Long orderId = null; //todo: [Review] это нужно?
                System.out.println("Номер заказа |  Статус  | Сумма");

                for (Order order : orders) {
                    System.out.printf("%12s | %8s | %s %s\n", order.getId(), order.getStatus(),
                            order.getTotalAmount().getSum(), order.getTotalAmount().getCurrency());
                }

                System.out.println("\n0 - Назад");
                while (intOption == null) {
                    intOption = askInt(scanner);
                    scanner.nextLine();
                }
            }

        }
        while (intOption != 0);

    }

    //todo: [Review] нейминг
    private void refund(Scanner scanner) throws WalletException {
        Long orderId;

        do {
            orderId = null;
            System.out.println("Введите номер заказа для возврата");
            System.out.println("0 - Назад");
            while (orderId == null) {
                orderId = askLong(scanner);
                scanner.nextLine();
            }
            if (orderId != 0) {
                try {
                    orderService.refund(userId, orderId);
                } catch (OrderException exception) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(exception.getMessage());
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        }
        while (orderId != 0);

    }

    //todo: [Review] Нужно не ловишь WalletException (Как пример: Сумма не может быть отрицательной)
    private void deposit(Scanner scanner) throws WalletException {

        Integer intOption = null;
        Double sum = null;

        System.out.println("Выберите валюту для пополнения");
        System.out.println("1 - RUB"); //todo: [Review] Тут нужно выводить с помощью перечисления по енаму, иначе, добавив новую валюту, у нас тут она не отрисуется
        System.out.println("2 - USD");
        System.out.println("3 - EUR");
        System.out.println("0 - Отмена");

        while (intOption == null) {
            intOption = askInt(scanner);
            scanner.nextLine();
        }

        System.out.println("Введите сумму");
        while (sum == null) {
            sum = askDouble(scanner); //todo: [Review] по хорошему на фронте обычно стараются пресечь невалидные кейсы до передачи беку, как тут, когда мы могли пресечь отрицательно число (было бы не критично, если бы мы хотя бы эксепшн ловили)
            scanner.nextLine();
        }

        switch (intOption) {
            case 1: {
                walletService.deposit(userId, new Amount(RUB, sum));
            }
            break;

            case 2: {
                walletService.deposit(userId, new Amount(USD, sum));
            }
            break;

            case 3: {
                walletService.deposit(userId, new Amount(EUR, sum));
            }
            break;

            case 0:
            default:
                break;
        }

    }

    private Integer askInt(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("Пожалуйста введите целое число");
        }

        return null;
    }

    private Long askLong(Scanner scanner) {
        try {
            return scanner.nextLong();
        } catch (InputMismatchException ex) {
            System.out.println("Пожалуйста введите целое число");
        }

        return null;
    }

    private Double askDouble(Scanner scanner) {
        try {
            return scanner.nextDouble();
        } catch (InputMismatchException ex) {
            System.out.println("Пожалуйста введите число");
        }

        return null;
    }
}
