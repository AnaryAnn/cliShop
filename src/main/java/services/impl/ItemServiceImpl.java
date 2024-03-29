package services.impl;

import model.Amount;
import model.Category;
import model.Item;
import services.api.ItemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.Currency.RUB;

public class ItemServiceImpl implements ItemService {

    private final List<Item> itemsList = new ArrayList<>();
    private final Map<String, Category> categorySet = new HashMap<>(); //todo: [Review] вроде это мапа уже, а не Set
    private Long itemSequence = 0L;
    private Long categorySequence = 0L;


    public ItemServiceImpl() {
        addCategory("Молочное");
        addCategory("Молоко", categorySet.get("Молочное"));
        addCategory("Сливки", categorySet.get("Молочное"));
        addCategory("Творог", categorySet.get("Молочное"));
        addCategory("Питьевой йогурт", categorySet.get("Молочное"));

        addItem("Молоко Простоквашино пастеризованное, 2,5%, 930 мл", categorySet.get("Молоко"), new Amount(RUB, 83d));
        addItem("Молоко Домик в деревне ультрапастеризованное, 3,2%, 925 мл", categorySet.get("Молоко"), new Amount(RUB, 133d));
        addItem("Молоко Parmalat ультрапастеризованное, 1,8%, 1 л", categorySet.get("Молоко"), new Amount(RUB, 105d));

        addItem("Сливки Домик в деревне стерилизованные, 10%, 480 г", categorySet.get("Сливки"), new Amount(RUB, 155d));
        addItem("Сливки Parmalat 35%, идеально для взбивания, 500 г", categorySet.get("Сливки"), new Amount(RUB, 296d));

        addItem("Творог Свитлогорье 9%, 200 г", categorySet.get("Творог"), new Amount(RUB, 119d));
        addItem("Творог Свитлогорье обезжиренный, 0,5%, 200 г", categorySet.get("Творог"), new Amount(RUB, 89d));
        addItem("Творог рассыпчатый, 5%, 200 г", categorySet.get("Творог"), new Amount(RUB, 88d));

        addItem("Биойогурт Активиа питьевой, со злаками, 2,2%, 260 г", categorySet.get("Питьевой йогурт"), new Amount(RUB, 72d));
        addItem("Йогуртный смузи тропик, 1,3%, 290 г", categorySet.get("Питьевой йогурт"), new Amount(RUB, 89d));

        addCategory("Хлеб");
        addCategory("Выпечка, сдоба", categorySet.get("Хлеб"));
        addCategory("Хлебцы, сухарики", categorySet.get("Хлеб"));
        addCategory("Батон", categorySet.get("Хлеб"));

        addItem("Кекс \"Творожный\" семейный, 300 г", categorySet.get("Выпечка, сдоба"), new Amount(RUB, 175d));
        addItem("Кекс \"Творожный\", 100 г", categorySet.get("Выпечка, сдоба"), new Amount(RUB, 97d));
        addItem("Кекс полбяной с семенами чиа, 150 г", categorySet.get("Выпечка, сдоба"), new Amount(RUB, 15d));

        addItem("Хлебцы томатные, 70 г", categorySet.get("Сливки"), new Amount(RUB, 81d));
        addItem("Хлебцы \"Гречневые\" с морской солью, 60 г", categorySet.get("Сливки"), new Amount(RUB, 51d));

        addItem("Батон \"Молодежный\", нарезка, 350 г", categorySet.get("Батон"), new Amount(RUB, 37d));
        addItem("Батон нарезной, половинка, 200 г", categorySet.get("Батон"), new Amount(RUB, 26d));
        addItem("Изделие цельнозер. без муки \"Житный\", нарезка, 300 г", categorySet.get("Батон"), new Amount(RUB, 77d));

        addCategory("Напитки");
        addCategory("Морсы", categorySet.get("Напитки"));
        addCategory("Лимонад", categorySet.get("Напитки"));
        addCategory("Напитки растительные", categorySet.get("Напитки"));

        addItem("Морс облепиховый, 1000 мл", categorySet.get("Морсы"), new Amount(RUB, 158d));
        addItem("Морс клюквенный ПЭТ, 1000 мл", categorySet.get("Морсы"), new Amount(RUB, 158d));
        addItem("Морс черничный, 1000 мл", categorySet.get("Морсы"), new Amount(RUB, 163d));

        addItem("Напиток \"Лимонад\", 1 л, 1000 мл", categorySet.get("Лимонад"), new Amount(RUB, 110d));
        addItem("Напиток \"Тархун\", 1 л, 1000 мл", categorySet.get("Лимонад"), new Amount(RUB, 106d));

        addItem("Напиток растительный кедровый с медом, 200 мл", categorySet.get("Напитки растительные"), new Amount(RUB, 198d));
        addItem("Напиток растительный \"Овсяный\", 500 мл", categorySet.get("Напитки растительные"), new Amount(RUB, 97d));
        addItem("Напиток \"Молоко рисовое\", 500 мл", categorySet.get("Напитки растительные"), new Amount(RUB, 87d));

    }

    @Override
    public List<Item> getItems() {
        return itemsList;
    }

    private void addItem(String name, Category category, Amount amount) {
        itemsList.add(Item.builder()
                .setId(++itemSequence)
                .setName(name)
                .setCategory(category)
                .setAmount(amount)
                .build());
    }
    //todo: [Review] а нам точно нужны оба метода addCategory?
    private void addCategory(String name, Category parent) {
        categorySet.put(name, //todo: [Review] тут зря переход на след строчку сделала
                Category.builder()
                        .setId(++categorySequence)
                        .setName(name)
                        .setParent(parent)
                        .build());
    }

    private void addCategory(String name) {
        addCategory(name, null);
    }
}
