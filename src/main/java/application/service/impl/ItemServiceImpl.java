package application.service.impl;

import application.repository.ItemRepository;
import application.service.api.ItemService;
import model.Amount;
import model.Category;
import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static model.Currency.RUB;

@Service
public class ItemServiceImpl implements ItemService {

    private final List<Item> itemsList = new ArrayList<>();
    private final Map<String, Category> categoryMap = new HashMap<>();
    private Long itemSequence = 0L;
    private Long categorySequence = 0L;

    @Autowired
    private ItemRepository itemRepository;

    public ItemServiceImpl() {
        addCategory("Молочное", null);
        addCategory("Молоко", categoryMap.get("Молочное"));
        addCategory("Сливки", categoryMap.get("Молочное"));
        addCategory("Творог", categoryMap.get("Молочное"));
        addCategory("Питьевой йогурт", categoryMap.get("Молочное"));

        addItem("Молоко Простоквашино пастеризованное, 2,5%, 930 мл", categoryMap.get("Молоко"), new Amount(RUB, 83d));
        addItem("Молоко Домик в деревне ультрапастеризованное, 3,2%, 925 мл", categoryMap.get("Молоко"), new Amount(RUB, 133d));
        addItem("Молоко Parmalat ультрапастеризованное, 1,8%, 1 л", categoryMap.get("Молоко"), new Amount(RUB, 105d));

        addItem("Сливки Домик в деревне стерилизованные, 10%, 480 г", categoryMap.get("Сливки"), new Amount(RUB, 155d));
        addItem("Сливки Parmalat 35%, идеально для взбивания, 500 г", categoryMap.get("Сливки"), new Amount(RUB, 296d));

        addItem("Творог Свитлогорье 9%, 200 г", categoryMap.get("Творог"), new Amount(RUB, 119d));
        addItem("Творог Свитлогорье обезжиренный, 0,5%, 200 г", categoryMap.get("Творог"), new Amount(RUB, 89d));
        addItem("Творог рассыпчатый, 5%, 200 г", categoryMap.get("Творог"), new Amount(RUB, 88d));

        addItem("Биойогурт Активиа питьевой, со злаками, 2,2%, 260 г", categoryMap.get("Питьевой йогурт"), new Amount(RUB, 72d));
        addItem("Йогуртный смузи тропик, 1,3%, 290 г", categoryMap.get("Питьевой йогурт"), new Amount(RUB, 89d));

        addCategory("Хлеб", null);
        addCategory("Выпечка, сдоба", categoryMap.get("Хлеб"));
        addCategory("Хлебцы, сухарики", categoryMap.get("Хлеб"));
        addCategory("Батон", categoryMap.get("Хлеб"));

        addItem("Кекс \"Творожный\" семейный, 300 г", categoryMap.get("Выпечка, сдоба"), new Amount(RUB, 175d));
        addItem("Кекс \"Творожный\", 100 г", categoryMap.get("Выпечка, сдоба"), new Amount(RUB, 97d));
        addItem("Кекс полбяной с семенами чиа, 150 г", categoryMap.get("Выпечка, сдоба"), new Amount(RUB, 15d));

        addItem("Хлебцы томатные, 70 г", categoryMap.get("Сливки"), new Amount(RUB, 81d));
        addItem("Хлебцы \"Гречневые\" с морской солью, 60 г", categoryMap.get("Сливки"), new Amount(RUB, 51d));

        addItem("Батон \"Молодежный\", нарезка, 350 г", categoryMap.get("Батон"), new Amount(RUB, 37d));
        addItem("Батон нарезной, половинка, 200 г", categoryMap.get("Батон"), new Amount(RUB, 26d));
        addItem("Изделие цельнозер. без муки \"Житный\", нарезка, 300 г", categoryMap.get("Батон"), new Amount(RUB, 77d));

        addCategory("Напитки", null);
        addCategory("Морсы", categoryMap.get("Напитки"));
        addCategory("Лимонад", categoryMap.get("Напитки"));
        addCategory("Напитки растительные", categoryMap.get("Напитки"));

        addItem("Морс облепиховый, 1000 мл", categoryMap.get("Морсы"), new Amount(RUB, 158d));
        addItem("Морс клюквенный ПЭТ, 1000 мл", categoryMap.get("Морсы"), new Amount(RUB, 158d));
        addItem("Морс черничный, 1000 мл", categoryMap.get("Морсы"), new Amount(RUB, 163d));

        addItem("Напиток \"Лимонад\", 1 л, 1000 мл", categoryMap.get("Лимонад"), new Amount(RUB, 110d));
        addItem("Напиток \"Тархун\", 1 л, 1000 мл", categoryMap.get("Лимонад"), new Amount(RUB, 106d));

        addItem("Напиток растительный кедровый с медом, 200 мл", categoryMap.get("Напитки растительные"), new Amount(RUB, 198d));
        addItem("Напиток растительный \"Овсяный\", 500 мл", categoryMap.get("Напитки растительные"), new Amount(RUB, 97d));
        addItem("Напиток \"Молоко рисовое\", 500 мл", categoryMap.get("Напитки растительные"), new Amount(RUB, 87d));

    }

    @Override
    public List<Item> getAllItems() {
        return itemsList;
    }

    @Override
    public List<application.data.Item> getAllItemsDB() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> findItemById(Long id) {
        return itemsList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    private void addItem(String name, Category category, Amount amount) {
        itemsList.add(Item.builder()
                .setId(++itemSequence)
                .setName(name)
                .setCategory(category)
                .setAmount(amount)
                .build());
    }

    private void addCategory(String name, Category parent) {
        categoryMap.put(name, Category.builder()
                .setId(++categorySequence)
                .setName(name)
                .setParent(parent)
                .build());
    }

}
