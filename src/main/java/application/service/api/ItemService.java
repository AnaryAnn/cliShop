package application.service.api;

import application.model.CategoryDTO;
import application.model.ItemDTO;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс товаров магазина
 */
public interface ItemService {

    /**
     * Получение списка всех товаров в магазине
     *
     * @return Список всех товаров магазина
     */
    List<ItemDTO> getAllItems();

    /**
     * Получение товара по id
     *
     * @param id - идентификатор товара
     * @return Товар
     */
    Optional<ItemDTO> findItemById(Long id);

    List<ItemDTO> findItemsByCategory(String category);

    void createItems(List<ItemDTO> itemDto);

    void createCategories(List<CategoryDTO> categoryDTO);
}
