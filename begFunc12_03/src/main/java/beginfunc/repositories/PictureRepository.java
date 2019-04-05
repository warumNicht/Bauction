package beginfunc.repositories;

import beginfunc.domain.entities.productRelated.Picture;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture,String> {

    @Query(value = "SELECT p FROM Picture p " +
            "WHERE p.product.id LIKE :productId")
    List<Picture> findAllByProductId(@Param("productId") String productId);
}
