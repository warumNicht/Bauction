package bauction.services;

import bauction.constants.ErrorMessagesConstants;
import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.models.serviceModels.CategoryServiceModel;
import bauction.error.CategoryNotFoundException;
import bauction.error.DuplicatedCategoryException;
import bauction.repositories.CategoryRepository;
import bauction.services.contracts.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCategory(CategoryServiceModel categoryServiceModel) {
        if(this.categoryRepository.existsByName(categoryServiceModel.getName())){
            throw new DuplicatedCategoryException(String.format(ErrorMessagesConstants.DUPLICATED_CATEGORY_MESSAGE,
                    categoryServiceModel.getName()));
        }
        Category category = this.modelMapper.map(categoryServiceModel, Category.class);
        this.categoryRepository.saveAndFlush(category);
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        List<CategoryServiceModel> categoryServiceModels = this.categoryRepository.findAll().stream()
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .collect(Collectors.toList());
        return categoryServiceModels;
    }

    @Override
    public CategoryServiceModel findByName(String name) {
        Category category = this.categoryRepository.findByName(name)
                .orElseThrow(()->new CategoryNotFoundException(ErrorMessagesConstants.NOT_EXISTENT_CATEGORY_MESSAGE));
        return this.modelMapper.map(category,CategoryServiceModel.class);
    }
}
