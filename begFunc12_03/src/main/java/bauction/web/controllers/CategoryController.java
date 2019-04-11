package bauction.web.controllers;

import bauction.services.contracts.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping(value = "/all", produces = "application/json")
    @ResponseBody
    public Object fetchCategories(){
        return this.categoryService.findAllCategories()
                .stream().map(c->c.getName());
    }
}
