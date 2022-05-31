package application.service.impl;

import application.data.Category;
import application.data.Item;
import application.model.AmountDTO;
import application.model.CategoryDTO;
import application.model.Currency;
import application.model.ItemDTO;
import application.repository.CategoryRepository;
import application.repository.ItemRepository;
import application.service.api.ItemService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    public static final Function<Item, ItemDTO> MAPPER_TO_DTO = entity -> ItemDTO.builder()
            .setId(entity.getId())
            .setName(entity.getName())
            .setAmount(new AmountDTO(Currency.valueOf(entity.getCurrency()), entity.getSum()))
            .setCategory(CategoryDTO.builder()
                    .setId(entity.getId())
                    .setName(entity.getCategory().getName())
                    .build())
            .build();

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ItemServiceImpl() {

    }

    @Override
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(MAPPER_TO_DTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDTO> findItemById(Long id) {
        return itemRepository.findById(id)
                .map(MAPPER_TO_DTO);
    }

    @Override
    public List<ItemDTO> findItemsByCategory(String category) {
        return itemRepository.findItemsByCategory(category).stream()
                .map(MAPPER_TO_DTO)
                .collect(Collectors.toList());
    }

    @Override
    public void createItems(List<ItemDTO> itemsDTO) {
        mapperDtoToEntity();
        for (ItemDTO itemDTO: itemsDTO){
            itemRepository.save(modelMapper.map(itemDTO, Item.class));
        }
    }

    @Override
    public void createCategories(List<CategoryDTO> categoriesDTO) {
        for (CategoryDTO categoryDTO: categoriesDTO){
            categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
        }
    }

    private void mapperDtoToEntity(){
        TypeMap<ItemDTO, Item> propertyMapper = modelMapper.getTypeMap(ItemDTO.class, Item.class);
        if (propertyMapper == null) {
            propertyMapper = modelMapper.createTypeMap(ItemDTO.class, Item.class);
            propertyMapper.addMappings(
                    mapper -> mapper.map(src -> src.getAmount().getCurrency(), Item::setCurrency)
            );
            propertyMapper.addMappings(
                    mapper -> mapper.map(src -> src.getAmount().getSum(), Item::setSum)
            );
        }
    }


}
