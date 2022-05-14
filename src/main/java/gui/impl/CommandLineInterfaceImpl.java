package gui.impl;

import exceptions.WalletException;
import gui.api.CommandLineInterface;
import model.Amount;
import services.api.WalletService;
import services.impl.WalletServiceImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

import static model.Currency.*;

public class CommandLineInterfaceImpl implements CommandLineInterface {

    private final WalletService walletService = WalletServiceImpl.getInstance();
    private Long userId;

    @Override
    public void run() throws WalletException {
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

    private void showCli(Scanner scanner) throws WalletException {
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

    private Double askDouble(Scanner scanner) {
        try {
            return scanner.nextDouble();
        } catch (InputMismatchException ex) {
            System.out.println("Пожалуйста введите число");
        }

        return null;
    }
}
