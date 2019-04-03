package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.CategoryServiceModel;

import java.util.List;

public interface CategoryService {
    boolean addCategory(CategoryServiceModel categoryServiceModel);

    List<CategoryServiceModel> findAllCategories();

    CategoryServiceModel findByName(String name);
}
