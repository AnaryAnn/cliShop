package application.service.api;


import application.model.AmountDTO;
import application.model.WalletDTO;
import exceptions.WalletException;

/**
 * Интерфейс для работы с кошельком
 */
public interface WalletService {

    /**
     * Пополнение кошелька
     *
     * @param userId идентификатор пользователя
     * @param amount сумма и валюта пополнения
     */
    void deposit(Long userId, AmountDTO amount) throws WalletException;

    /**
     * Снятие денежных средств с кошелька
     *
     * @param userId идентификатор пользователя
     * @param amount сумма и валюта пополнения
     */
    void withdraw(Long userId, AmountDTO amount) throws WalletException;

    /**
     * Создание кошелька для пользователя
     *
     * @param userId идентификатор пользователя
     * @return кошелек
     */
    WalletDTO createWallet(Long userId);

    /**
     * Получение баланса кошелька
     *
     * @param userId идентификатор пользователя
     * @return сумма
     */
    AmountDTO getUserBalance(Long userId);


}
