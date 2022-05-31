package application.controller;

import application.model.OrderDTO;
import application.service.api.OrderService;
import exceptions.OrderException;
import exceptions.WalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity addOrder(@RequestParam Long userId, @RequestBody List<Long> itemId) {
        OrderDTO order;
        try {
            order = orderService.createOrder(userId, itemId);
        } catch (OrderException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/payment")
    public ResponseEntity payment(@RequestParam Long userId, @RequestParam Long orderId) {
        OrderDTO order;
        try {
            orderService.payment(userId, orderId);
        } catch (OrderException | WalletException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok(String.format("Заказ №%s успешно оплачен", orderId));
    }

    @PostMapping("/refund")
    public ResponseEntity refund(@RequestParam Long userId, @RequestParam Long orderId) {
        OrderDTO order;
        try {
            orderService.refund(userId, orderId);
        } catch (OrderException | WalletException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok(String.format("Средства по заказу №%s успешно возвращены\n", orderId));
    }

}
