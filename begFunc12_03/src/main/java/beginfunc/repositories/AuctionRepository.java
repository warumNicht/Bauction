package beginfunc.repositories;

import beginfunc.domain.entities.auctionRelated.Auction;
import beginfunc.domain.entities.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository

public interface AuctionRepository extends JpaRepository<Auction,Integer> {

    List<Auction> findAllByStatusIsLikeOrStatusIsLike(AuctionStatus statusActive,AuctionStatus statusFinished);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Auction a " +
            "SET a.views=a.views+1 " +
            "WHERE a.id LIKE :id")
    void increaseViews(@Param(value = "id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Auction a " +
            "SET a.status= :status " +
            "WHERE a.id LIKE :id")
    void updateStatus(@Param(value = "id") Integer id, @Param(value = "status") AuctionStatus status);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Auction a " +
            "SET a.reachedPrice=a.reachedPrice+ :biddingStep " +
            "WHERE a.id LIKE :id")
    void increaseCurrentPrice(@Param(value = "id") Integer id,
                              @Param(value = "biddingStep")  BigDecimal biddingStep);
}
