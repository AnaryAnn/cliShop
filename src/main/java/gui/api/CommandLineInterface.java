package gui.api;

import exceptions.OrderException;
import exceptions.WalletException;

public interface CommandLineInterface {
    /**
     * Интерфейс работы с коммандной строкой
     * @throws WalletException - исключения кошелька
     * @throws OrderException - исключения заказа
     */
    void run() throws WalletException, OrderException;
}
