package bauction.repositories;

import bauction.domain.entities.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal,String> {

    @Query(value = "SELECT d FROM Deal d " +
            "WHERE d.buyer.id LIKE :id OR d.seller.id LIKE :id " +
            "AND d.dateTime>= :deadline " +
            "ORDER BY d.dateTime DESC")
    List<Deal> allRecentDealsOfUser(@Param(value = "id")String userId,@Param(value = "deadline") Date date);


//    @Query(value = "SELECT d FROM Deal d " +
//            "WHERE d.buyer.id LIKE :id OR d.seller.id LIKE :id " +
//            "AND ( d.buyerComment IS NOT NULL OR d.sellerComment IS NOT NULL) " +
//            "AND (d.buyerComment.author.id NOT LIKE :id OR d.sellerComment.author.id NOT LIKE :id) " +
//            "ORDER BY d.dateTime DESC")


    @Query(value = "SELECT d FROM Deal d " +
            "JOIN Comment c ON c.id=d.buyerComment.id OR c.id=d.sellerComment.id " +
            "WHERE (d.buyer.id LIKE :id OR d.seller.id LIKE :id) " +
            "AND (c.author.id NOT LIKE :id) " +
            "GROUP BY d.id " +
            "ORDER BY d.dateTime DESC ")
    List<Deal> allDealsWithCommentOfUser(@Param(value = "id")String userId);
}
