package beginfunc.web.controllers;

import beginfunc.constants.AppConstants;
import beginfunc.domain.models.bindingModels.CategoryBindingModel;
import beginfunc.domain.models.serviceModels.CategoryServiceModel;
import beginfunc.services.contracts.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create/category")
    public ModelAndView addCategory(@ModelAttribute(name = "categoryBindingModel")CategoryBindingModel model,
                                    ModelAndView modelAndView){
        modelAndView.addObject("categoryBindingModel", model);
        modelAndView.setViewName("add-category");
        return modelAndView;
    }

    @PostMapping("/create/category")
    public ModelAndView addCategoryPost(@Valid @ModelAttribute(name = "categoryBindingModel")CategoryBindingModel model,
                                        BindingResult bindingResult, ModelAndView modelAndView){
        if(bindingResult.hasErrors()){
            modelAndView.addObject("categoryBindingModel", model);
            modelAndView.setViewName("add-category");
            return modelAndView;
        }
        CategoryServiceModel categoryServiceModel = this.modelMapper.map(model, CategoryServiceModel.class);
        if(!this.categoryService.addCategory(categoryServiceModel)){
            throw new IllegalArgumentException("Category creation failed");
        }
        modelAndView.setViewName("redirect:/admin/create/category");
        return modelAndView;
    }
}
