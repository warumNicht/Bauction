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
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository

public interface AuctionRepository extends JpaRepository<Auction,String> {

    List<Auction> findAllByStatus(AuctionStatus status);

    @Query(value = "SELECT a FROM Auction a " +
            "WHERE a.status='Active' " +
            "AND a.endDate < current_time")
    List<Auction> findAllActivesAuctionsExceedingEndDate();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Auction a " +
            "SET a.views=a.views+1 " +
            "WHERE a.id LIKE :id")
    void increaseViews(@Param(value = "id") String id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Auction a " +
            "SET a.status= :status " +
            "WHERE a.id LIKE :id")
    void updateStatus(@Param(value = "id") String id, @Param(value = "status") AuctionStatus status);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Auction a " +
            "SET a.reachedPrice=a.reachedPrice+ :biddingStep " +
            "WHERE a.id LIKE :id")
    void increaseCurrentPrice(@Param(value = "id") String id,
                              @Param(value = "biddingStep")  BigDecimal biddingStep);

    @Query(value = "SELECT a FROM Auction a " +
            "WHERE a.seller.id LIKE :id AND a.status='Waiting'")
    List<Auction> getWaitingAuctionsOfUser(@Param(value = "id") String userId);

    @Query(value = "SELECT a FROM Auction a " +
            "WHERE a.status='Active' " +
            "AND a.seller.id LIKE :id")
    List<Auction>  getActiveAuctionsOfUser(@Param(value = "id") String userId);

    @Query(value = "SELECT a FROM Auction a " +
            "WHERE a.status='Finished' " +
            "AND a.seller.id LIKE :id " +
            "AND a.buyer IS NOT NULL ")
    List<Auction> getFinishedAuctionsOfUserWithDeal(@Param(value = "id")String userId);

    @Query(value = "SELECT a FROM Auction a " +
            "WHERE a.status='Finished' " +
            "AND a.seller.id LIKE :id " +
            "AND a.buyer IS NULL ")
    List<Auction> getFinishedAuctionsOfUserWithoutDeal(@Param(value = "id") String userId);

    @Query(value = "SELECT a FROM Auction a " +
            "WHERE a.status='Finished' " +
            "AND a.buyer IS NULL ")
    List<Auction> findAllFinishedAuctionsWithoutDeal();
}
