package services.api;

import model.Item;

import java.util.List;
//todo: [Review] комменты у классов, на всякий случай тут тож пишу
public interface ItemService {

    /**
     * Получение списка всех товаров в магазине
     *
     * @return //todo: [Review] кто то тут поленился писать?
     */
    List<Item> getItems(); //todo: [Review] я бы назвал его getAllItems, мы же все прям позвращается

}
