package application.controller;

import exceptions.WalletException;
import model.Amount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.api.WalletService;
import services.impl.WalletServiceImpl;

@RestController
public class WalletController {

    private final WalletService walletService = WalletServiceImpl.getInstance();

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestParam Long userId, @RequestBody Amount amount) {
        try {
            walletService.deposit(userId, amount);
        } catch (WalletException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
        return ResponseEntity.ok(String.format("Баланс успешно пополнен на %s %s",
                amount.getSum(), amount.getCurrency()));
    }

    @GetMapping("/balance")
    public ResponseEntity getBalance(@RequestParam Long userId){
        return ResponseEntity.ok(walletService.getUserBalance(userId));
    }

}
