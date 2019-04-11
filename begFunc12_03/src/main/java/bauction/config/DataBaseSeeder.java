package bauction.config;


import bauction.domain.entities.Role;
import bauction.domain.entities.auctionRelated.Category;
import bauction.repositories.CategoryRepository;
import bauction.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class DataBaseSeeder {
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DataBaseSeeder(RoleRepository userRoleRepository, CategoryRepository categoryRepository) {
        this.roleRepository = userRoleRepository;
        this.categoryRepository = categoryRepository;
    }


    @PostConstruct
    public void seed() {
        if (this.roleRepository.count()==0) {
            Role root = new Role();
            root.setAuthority("ROLE_ROOT");

            Role admin = new Role();
            admin.setAuthority("ROLE_ADMIN");

            Role user = new Role();
            user.setAuthority("ROLE_USER");

            Role moderator = new Role();
            moderator.setAuthority("ROLE_MODERATOR");

            this.roleRepository.saveAndFlush(root);
            this.roleRepository.saveAndFlush(admin);
            this.roleRepository.saveAndFlush(moderator);
            this.roleRepository.saveAndFlush(user);
        }

        if(this.categoryRepository.count()==0){
            this.categoryRepository.saveAndFlush(new Category("Books"));
            this.categoryRepository.saveAndFlush(new Category("AutoParts"));
            this.categoryRepository.saveAndFlush(new Category("Allgemein"));
            this.categoryRepository.saveAndFlush(new Category("Coins"));
            this.categoryRepository.saveAndFlush(new Category("Banknotes"));
        }
    }
}
