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

import static model.Currency.values;

public class CommandLineInterfaceImpl implements CommandLineInterface {

    private final WalletService walletService = WalletServiceImpl.getInstance();
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private final StatisticService statisticService = new StatisticServiceImpl();
    private final ItemService itemService = new ItemServiceImpl();
    private Long userId;

    @Override
    public void run() throws WalletException, OrderException {
        try (Scanner scanner = new Scanner(System.in)) {
            auth(scanner);
            showCli(scanner);
        }
    }

    private void auth(Scanner scanner) {
        System.out.println("Пожалуйста, введите идентификатор пользователя");

        Integer intOption = null;
        while (intOption == null) {
            intOption = askInt(scanner);
            scanner.nextLine();
        }
        userId = Long.valueOf(intOption);
        walletService.createWallet(userId);
    }

    private void showCli(Scanner scanner) throws WalletException {
        Integer intOption;
        do {
            intOption = null;

            System.out.println("----------------------------------");
            System.out.printf("Идентификатор пользователя: %s\n", userId);
            System.out.println("Баланс кошелька: ");
            Arrays.stream(values())
                    .forEach(currency -> System.out.printf("%21s %s\n",
                            getCurrentUserBalance(currency), currency));
            System.out.println("----------------------------------");
            System.out.println("1 - Список доступных товаров");
            System.out.println("2 - Возврат заказа");
            System.out.println("3 - История покупок");
            System.out.println("4 - Статистика");
            System.out.println("5 - Пополнение кошелька");
            System.out.println("6 - Логаут");
            System.out.println("0 - Выход");

            while (intOption == null) {
                intOption = askInt(scanner);
                scanner.nextLine();
            }
            switch (intOption) {

                case 1: {
                    showAllItems(scanner);
                }
                break;

                case 2: {
                    refund(scanner);
                }
                break;

                case 3: {
                    showHistory(scanner);
                }
                break;

                case 4: {
                    showStatistic(scanner);
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

    private double getCurrentUserBalance(Currency currency) {
        Double result = walletService.getUserBalance(userId).get(currency);
        return result == null ? 0 : result;
    }

    private void showAllItems(Scanner scanner) {
        Integer intOption;
        do {
            intOption = null;

            List<Item> items = itemService.getAllItems();
            if (items.isEmpty()) {
                System.out.println("Извините, список товаров пуст");
                break;
            }

            System.out.println("№ | Название товара | Цена");
            items.forEach(item -> System.out.printf("%s | %s | %s %s\n", item.getId(), item.getName(),
                    item.getAmount().getSum(), item.getAmount().getCurrency()));

            while (intOption == null) {
                System.out.printf("\nВведите номер товара для покупки\n");
                System.out.println("0 - Назад");
                intOption = askInt(scanner);
                scanner.nextLine();
            }

            if (intOption > 0) {
                Long orderId;
                Long itemId = Long.valueOf(intOption);
                try {
                    Optional<Item> itemOptional = itemService.findItemById(itemId);
                    if (itemOptional.isEmpty()) {
                        throw new OrderException(String.format("Товар №%d не найден\n", itemId));
                    }
                    Item item = itemOptional.get();
                    orderId = orderService.createOrder(userId, Collections.singleton(item)).getId();
                    orderService.payment(userId, orderId);
                } catch (OrderException | WalletException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                System.out.printf("Заказ №%d успешно оплачен\n", orderId);
            } else {
                System.out.println("Номер товара должен быть целым положительным числом");
            }
        }
        while (intOption != 0);
    }

    private void showStatistic(Scanner scanner) {
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
                    if (bestSellers.isEmpty()){
                        System.out.println("Недостаточно данных для сбора статистики");
                        break;
                    }
                    System.out.println("Название товара | Кол-во продаж");
                    bestSellers.entrySet().stream()
                            .sorted(Map.Entry.<Item, Long>comparingByValue().reversed())
                            .forEach(entry -> System.out.printf("%s | %s \n", entry.getKey().getName(), entry.getValue()));
                    System.out.println();
                    break;
                case 2:
                    Map<Category, Long> bestSellCategory = statisticService.getBestSellCategory();
                    if (bestSellCategory.isEmpty()){
                        System.out.println("Недостаточно данных для сбора статистики");
                        break;
                    }
                    System.out.println("Категория | Кол-во продаж");
                    bestSellCategory.entrySet().stream()
                            .sorted(Map.Entry.<Category, Long>comparingByValue().reversed())
                            .forEach(entry -> System.out.printf("%s | %s \n", entry.getKey().getName(), entry.getValue()));
                    System.out.println();
                    break;
                case 3:
                    Map<Category, Double> financialIncome = statisticService.getFinancialIncome();
                    if (financialIncome.isEmpty()){
                        System.out.println("Недостаточно данных для сбора статистики");
                        break;
                    }
                    System.out.println("Категория | Оборот RUB");
                    financialIncome.entrySet().stream()
                            .sorted(Map.Entry.<Category, Double>comparingByValue().reversed())
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

    private void showHistory(Scanner scanner) {
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
                System.out.println("Номер заказа |  Статус  | Сумма");
                orders.stream()
                        .sorted(Comparator.comparing(Order::getId))
                        .forEach(order -> System.out.printf("%12s | %8s | %s %s\n", order.getId(), order.getStatus(),
                                order.getTotalAmount().getSum(), order.getTotalAmount().getCurrency()));

                System.out.println("\n0 - Назад");
                while (intOption == null) {
                    intOption = askInt(scanner);
                    scanner.nextLine();
                }
            }
        }
        while (intOption != 0);
    }

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

    private void deposit(Scanner scanner) {

        Integer intOption = null;
        Double sum = null;

        System.out.println("Выберите валюту для пополнения");
        Arrays.stream(values())
                .forEach(currency -> System.out.printf("%s - %s\n",
                        currency.ordinal() + 1, currency));
        System.out.println("0 - Отмена");

        while (intOption == null) {
            intOption = askInt(scanner);
            scanner.nextLine();
        }

        System.out.println("Введите сумму");
        while (sum == null) {
            sum = askDouble(scanner);
            scanner.nextLine();
        }
        try {
            switch (intOption) {
                case 1:
                case 2:
                case 3: {
                    walletService.deposit(userId, new Amount(Currency.values()[intOption - 1], sum));
                }
                break;

                case 0:
                default:
                    break;
            }
        } catch (WalletException exception) {
            System.out.println(exception.getMessage());
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
            double number = scanner.nextDouble();
            if (number <= 0 || number > Double.MAX_VALUE) {
                throw new IndexOutOfBoundsException(String.format("Значение должно быть в диапазоне (0, %s]", Double.MAX_VALUE));
            }
            return number;
        } catch (InputMismatchException ex) {
            System.out.println("Пожалуйста введите число");
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
