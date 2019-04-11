package bauction.repositories;

import bauction.domain.entities.productRelated.products.BaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseProductRepository extends JpaRepository<BaseProduct, String> {
}
