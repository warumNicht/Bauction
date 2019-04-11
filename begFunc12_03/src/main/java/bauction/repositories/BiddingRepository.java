package bauction.repositories;

import bauction.domain.entities.auctionRelated.Bidding;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BiddingRepository extends JpaRepository<Bidding, String> {
    @Query(value = "SELECT b FROM Bidding b " +
            "WHERE b.auction.id LIKE :id " +
            "ORDER BY b.submittedOn DESC")
    List<Bidding> findAllBiddingsOfAuction(@Param(value = "id")String auctionId);

    @Query(value = "SELECT COUNT(b) FROM Bidding b " +
            "WHERE b.auction.id LIKE :id " +
            "GROUP BY b.auction.id")
    Long getAuctionBiddingCount(@Param(value = "id") String id);

    @Query(value = "SELECT b FROM Bidding b " +
            "WHERE b.auction.id= :id " +
            "ORDER BY b.reachedPrice DESC "
            )
    List<Bidding> findHighestBiddingOfAuction(@Param(value = "id")String id, Pageable limit);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Bidding b " +
            "WHERE b.auction.id LIKE :id")
    void deleteBiddingsOfAuctionById(@Param(value = "id") String auctionId);
}
