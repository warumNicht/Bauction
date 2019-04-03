package beginfunc.repositories;

import beginfunc.domain.entities.auctionRelated.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BiddingRepository extends JpaRepository<Bidding, Integer> {
    @Query(value = "SELECT b FROM Bidding b " +
            "WHERE b.auction.id LIKE :id " +
            "ORDER BY b.submittedOn DESC")
    List<Bidding> findAllBiddingsOfAuction(@Param(value = "id")Integer auctionId);

    @Query(value = "SELECT COUNT(b) FROM Bidding b " +
            "WHERE b.auction.id LIKE :id " +
            "GROUP BY b.auction.id")
    Long getAuctionBiddingCount(@Param(value = "id") Integer id);
}
