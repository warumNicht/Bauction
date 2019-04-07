package beginfunc.repositories;

import beginfunc.domain.entities.productRelated.Picture;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture,String> {

    @Query(value = "SELECT p FROM Picture p " +
            "WHERE p.product.id LIKE :productId")
    List<Picture> findAllByProductId(@Param("productId") String productId);

    @Query(value = "SELECT p FROM Picture p " +
            "WHERE p.product.id LIKE :productId " +
            "AND p.id NOT LIKE p.product.mainPicture.id " +
            "OR p.product.mainPicture IS NULL ")
    List<Picture> findAllByProductIdWithoutMain(@Param("productId") String productId);

//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM Picture p " +
//            "WHERE p.product.id LIKE :productId ")
//    void deleteAllByProductId(@Param("productId") String productId);
}
