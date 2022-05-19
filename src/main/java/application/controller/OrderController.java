package application.controller;

import exceptions.OrderException;
import exceptions.WalletException;
import model.Item;
import model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.api.ItemService;
import services.api.OrderService;
import services.impl.ItemServiceImpl;
import services.impl.OrderServiceImpl;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService = OrderServiceImpl.getInstance();
    private ItemService itemService = new ItemServiceImpl();

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/order")
    public ResponseEntity addOrder(@RequestParam Long userId, @RequestBody List<Long> itemId) {
        Order order;
        try {
            order = orderService.createOrder(userId, itemId);
        } catch (OrderException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/payment")
    public ResponseEntity payment(@RequestParam Long userId, @RequestParam Long orderId) {
        Order order;
        try {
            orderService.payment(userId, orderId);
        } catch (OrderException | WalletException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok(String.format("Заказ №%s успешно оплачен", orderId));
    }

    @PostMapping("/refund")
    public ResponseEntity refund(@RequestParam Long userId, @RequestParam Long orderId) {
        Order order;
        try {
            orderService.refund(userId, orderId);
        } catch (OrderException | WalletException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok(String.format("Средства по заказу №%s успешно возвращены\n", orderId));
    }

}
