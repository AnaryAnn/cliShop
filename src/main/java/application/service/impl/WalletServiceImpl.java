package application.service.impl;

import application.data.Wallet;
import application.model.AmountDTO;
import application.model.Currency;
import application.model.WalletDTO;
import application.repository.WalletRepository;
import application.service.api.WalletService;
import exceptions.WalletException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

import static application.model.Currency.RUB;

@Service
public class WalletServiceImpl implements WalletService {

    private final Function<Wallet, WalletDTO> MAPPER_TO_DTO = entity -> WalletDTO.builder()
            .setUserId(entity.getUserId())
            .setBalance(entity.getBalance())
            .setCurrency(Currency.valueOf(entity.getCurrency()))
            .build();

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void deposit(Long userId, AmountDTO amount) throws WalletException {

        if (!isAmountPositive(amount)) {
            throw new WalletException("Сумма не может быть отрицательной");
        }

        if (amount.getCurrency() != RUB) {
            throw new WalletException("Поддерживаются только рубли");
        }

        if (amount.getSum() > Double.MAX_VALUE) {
            throw new WalletException(String.format("Баланс не может быть больше %s", Double.MAX_VALUE));
        }

        WalletDTO userWallet = findWallet(userId).orElseGet(() -> createWallet(userId));
        update(WalletDTO.builder()
                .setCurrency(amount.getCurrency())
                .setUserId(userId)
                .setBalance(userWallet.getBalance() + amount.getSum())
                .build());
    }

    private WalletDTO update(WalletDTO walletDTO){
        return MAPPER_TO_DTO.apply(walletRepository.save(modelMapper.map(walletDTO, Wallet.class)));
    }

    private Optional<WalletDTO> findWallet(long userId) {
        return Optional.ofNullable(walletRepository.findByUserId(userId))
                .map(MAPPER_TO_DTO);
    }

    @Override
    public void withdraw(Long userId, AmountDTO amount) throws WalletException {
        if (!isAmountPositive(amount)) {
            throw new WalletException("Сумма не может быть отрицательной");
        }

        if (amount.getCurrency() != RUB) {
            throw new WalletException("Поддерживаются только рубли");
        }

        Optional<WalletDTO> userWalletOptional = findWallet(userId);
        if (!userWalletOptional.isPresent()) {
            throw new WalletException("Кошелек не найден");
        }

        WalletDTO userWallet = userWalletOptional.get();

        if (!isEnoughMoneyForWithdraw(userWallet, amount)) {
            throw new WalletException("Недостаточно средств на счете");
        }

        update(WalletDTO.builder()
                .setBalance(userWallet.getBalance() - amount.getSum())
                .setUserId(userId)
                .setCurrency(amount.getCurrency())
                .build());
    }

    private boolean isEnoughMoneyForWithdraw(WalletDTO userWallet, AmountDTO amount) {
        return userWallet.getBalance() >= amount.getSum();
    }

    private boolean isAmountPositive(AmountDTO amount) {
        return amount != null && amount.getSum() >= 0;
    }

    @Override
    public WalletDTO createWallet(Long userId) {
        return MAPPER_TO_DTO.apply(walletRepository.save(new Wallet(null, userId, 0.0, RUB.toString())));
    }

    @Override
    public AmountDTO getUserBalance(Long userId) {
        return findWallet(userId)
                .map(walletDTO -> new AmountDTO(walletDTO.getCurrency(), walletDTO.getBalance()))
                .orElseGet(() -> new AmountDTO(RUB, 0.0));

    }
}
