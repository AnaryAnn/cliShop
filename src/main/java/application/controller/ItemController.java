package application.controller;

import application.model.CategoryDTO;
import application.model.ItemDTO;
import application.service.api.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/items")
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/category/{category}")
    public List<ItemDTO> findItemsByCategory(@PathVariable String category) {
        return itemService.findItemsByCategory(category);
    }

    @PostMapping("/items/add")
    public ResponseEntity addItem(@RequestBody List<ItemDTO> itemDto) {
        try {
            itemService.createItems(itemDto);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/items/category/add")
    public ResponseEntity addCategory(@RequestBody List<CategoryDTO> categoryDTO) {
        try {
            itemService.createCategories(categoryDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
