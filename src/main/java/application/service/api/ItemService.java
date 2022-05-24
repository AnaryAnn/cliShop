package application.service.api;

import model.Item;

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
    List<Item> getAllItems();
    
    List<application.data.Item> getAllItemsDB();

    /**
     * Получение товара по id
     * @param id - идентификатор товара
     * @return Товар
     */
    Optional<Item> findItemById(Long id);

}
