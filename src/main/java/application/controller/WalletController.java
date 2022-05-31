package application.controller;

import application.model.AmountDTO;
import application.service.api.WalletService;
import exceptions.WalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestParam Long userId, @RequestBody AmountDTO amount) {
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
