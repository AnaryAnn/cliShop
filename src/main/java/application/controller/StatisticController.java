package application.controller;

import application.service.api.StatisticService;
import application.service.impl.StatisticServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticController {

    private final StatisticService statisticService = new StatisticServiceImpl();

    @GetMapping("/statistic/history")
    public ResponseEntity getHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(statisticService.getOrderHistory(userId));
    }

    @GetMapping("/statistic/bestSellers")
    public ResponseEntity getBestSellers() {
        return ResponseEntity.ok(statisticService.getBestSellers());
    }

    @GetMapping("/statistic/bestSellCategory")
    public ResponseEntity getBestSellCategory() {
        return ResponseEntity.ok(statisticService.getBestSellCategory());
    }

    @GetMapping("/statistic/financialIncome")
    public ResponseEntity getFinancialIncome() {
        return ResponseEntity.ok(statisticService.getFinancialIncome());
    }
}
