package gui.impl;

import exceptions.OrderException;
import exceptions.WalletException;
import gui.api.CommandLineInterface;
import model.Amount;
import model.Category;
import model.Item;
import model.Order;
import org.apache.commons.lang3.StringUtils;
import services.api.OrderService;
import services.api.StatisticService;
import services.api.WalletService;
import services.impl.OrderServiceImpl;
import services.impl.StatisticServiceImpl;
import services.impl.WalletServiceImpl;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static model.Currency.*;

public class CommandLineInterfaceImpl implements CommandLineInterface {

    private final WalletService walletService = WalletServiceImpl.getInstance();
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private final StatisticService statisticService = new StatisticServiceImpl();
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
        userId = Long.valueOf(askInt(scanner));
        walletService.createWallet(userId);
    }

    private void showCli(Scanner scanner) throws WalletException, OrderException {
        Integer intOption;
        do {
            intOption = null;

            System.out.println("----------------------------------");
            System.out.printf("Идентификатор пользователя: %s\n", userId);
            System.out.printf("Баланс кошелька: %s RUB\n" +
                            "%21s USD\n" +
                            "%21s EUR\n",
                    walletService.getBalance(userId).get(RUB),
                    walletService.getBalance(userId).get(USD),
                    walletService.getBalance(userId).get(EUR));
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

    private void statistic(Scanner scanner) {
        Integer intOption;
        do {
            intOption = null;

            System.out.println("Выберите раздел");
            System.out.println("1 - Топ продаж");
            System.out.println("2 - Популярная категория");
            System.out.println("3 - Оборот категорий");
            System.out.println("0 - Отмена");

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
                            .limit(10)
                            .forEach(entry -> System.out.printf("%s | %s \n", entry.getKey().getName(), entry.getValue()));
                    System.out.println();
                    break;
                case 2:
                    Map<Category, Long> bestSellersCategory = statisticService.getBestSellerCategory();
                    break;
                case 3:
                    Map<Category, Double> circulationMoney = statisticService.getCirculationMoney();
                    break;
                case 0:
                default:
                    break;
            }

        }
        while (intOption != 0);
    }


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
                Long orderId = null;
                System.out.println("Номер заказа |  Статус  | Сумма");

                for (Order order : orders) {
                    System.out.printf("%12s | %8s | %s %s\n", order.getId(), order.getStatus(),
                            order.getTotalAmount().getSum(), order.getTotalAmount().getCurrency());
                }

//                System.out.println("\nВведите номер заказа для просмотра списка товаров");
//
//                while (orderId == null) {
//                    orderId = askLong(scanner);
//                    scanner.nextLine();
//                }

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

    private void deposit(Scanner scanner) throws WalletException {

        Integer intOption = null;
        Double sum = null;

        System.out.println("Выберите валюту для пополнения");
        System.out.println("1 - RUB");
        System.out.println("2 - USD");
        System.out.println("3 - EUR");
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
