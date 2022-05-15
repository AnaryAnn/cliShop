package services.api;

import model.Item;

import java.util.List;

public interface ItemService {

    /**
     * Получение списка всех товаров в магазине
     *
     * @return
     */
    List<Item> getItems();

}
