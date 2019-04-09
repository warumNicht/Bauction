package beginfunc.web.controllers;

import beginfunc.domain.models.bindingModels.CategoryBindingModel;
import beginfunc.domain.models.serviceModels.CategoryServiceModel;
import beginfunc.domain.models.serviceModels.users.RoleServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.domain.models.viewModels.users.UserPermissionAllViewModel;
import beginfunc.domain.models.viewModels.users.UserPermissionEditViewModel;
import beginfunc.error.DuplicatedCategoryException;
import beginfunc.services.contracts.CategoryService;
import beginfunc.services.contracts.RoleService;
import beginfunc.services.contracts.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
    private final CategoryService categoryService;
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(CategoryService categoryService, UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create/category")
    public ModelAndView addCategory(@ModelAttribute(name = "categoryBindingModel")CategoryBindingModel model,
                                    ModelAndView modelAndView){
        modelAndView.addObject("categoryBindingModel", model);
        modelAndView.setViewName("admin/add-category");
        return modelAndView;
    }

    @PostMapping("/create/category")
    public ModelAndView addCategoryPost(@Valid @ModelAttribute(name = "categoryBindingModel")CategoryBindingModel model,
                                        BindingResult bindingResult, ModelAndView modelAndView){
        if(bindingResult.hasErrors()){
            modelAndView.addObject("categoryBindingModel", model);
            modelAndView.setViewName("admin/add-category");
            return modelAndView;
        }
        CategoryServiceModel categoryServiceModel = this.modelMapper.map(model, CategoryServiceModel.class);
        this.categoryService.addCategory(categoryServiceModel);

        modelAndView.setViewName("redirect:/admin/create/category");
        return modelAndView;
    }

    @GetMapping("/users/all")
    public ModelAndView allUsersRoles(ModelAndView modelAndView){
        String loggedInUserId = super.getLoggedInUserId();
        List<UserPermissionAllViewModel> permissionViewModels =
                this.userService.findAllUsersWithoutTheLoggedIn(loggedInUserId).stream()
                        .map(u -> {
                            UserPermissionAllViewModel permissionViewModel = this.modelMapper.map(u, UserPermissionAllViewModel.class);
                            permissionViewModel.setAuthority(u.getPrimordialRole());
                            return permissionViewModel;
                        })
                        .collect(Collectors.toList());
        modelAndView.addObject("allOtherUsers",permissionViewModels);
        modelAndView.setViewName("admin/users-permissions");
        return modelAndView;
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView edit(@PathVariable(name = "id") String id, ModelAndView modelAndView){
        UserServiceModel toEdit = this.userService.findUserById(id);
        UserPermissionEditViewModel editViewModel =
                this.modelMapper.map(toEdit, UserPermissionEditViewModel.class);
        List<String> authorities = toEdit.getAuthorities().stream()
                .map(r -> r.getAuthority().replace("ROLE_",""))
                .collect(Collectors.toList());

        editViewModel.setAuthorities(authorities);
        modelAndView.addObject("userToEdit",editViewModel);
        modelAndView.setViewName("admin/edit-user");
        return modelAndView;
    }

    @PostMapping("/users/{id}/roles/remove")
    public ModelAndView removeRole(@PathVariable(name = "id") String id,
                                   @RequestParam(name = "role") String role, ModelAndView modelAndView){
        UserServiceModel toEdit = this.userService.findUserById(id);
        toEdit.getAuthorities()
                .removeIf(r -> r.getAuthority().equals("ROLE_" + role));

        UserServiceModel updated = this.userService.updateUser(toEdit);
        if(updated==null){
            throw new IllegalArgumentException("Removing role failed!");
        }
        modelAndView.setViewName("redirect:/admin/users/edit/" + id);
        return modelAndView;
    }

    @PostMapping("/users/{id}/roles/add")
    public ModelAndView addRole(@PathVariable(name = "id") String id,
                                @RequestParam(name = "role") String role, ModelAndView modelAndView){
        UserServiceModel toEdit = this.userService.findUserById(id);
        RoleServiceModel newRoleToAdd = this.roleService.findByAuthority("ROLE_" + role);
        toEdit.getAuthorities().add(newRoleToAdd);

        UserServiceModel updated = this.userService.updateUser(toEdit);
        if(updated==null){
            throw new IllegalArgumentException("Adding role failed!");
        }
        modelAndView.setViewName("redirect:/admin/users/edit/" + id);
        return modelAndView;
    }

    @ExceptionHandler({DuplicatedCategoryException.class})
    public ModelAndView handleDuplicatedUsername( DuplicatedCategoryException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }

}
