package bauction.repositories;

import bauction.domain.entities.auctionRelated.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer,String> {

    @Query(value = "SELECT o FROM Offer o " +
            "WHERE o.auction.id LIKE :id " +
            "ORDER BY o.submittedOn DESC")
    List<Offer> findAllOffersOfAuction(@Param(value = "id")String auctionId);

    @Query(value = "SELECT COUNT(o) FROM Offer o " +
            "WHERE o.auction.id LIKE :id " +
            "GROUP BY o.auction.id")
    Long getAuctionOfferCount(@Param(value = "id") String id);

    @Query(value = "SELECT o FROM Offer o " +
            "WHERE o.auction.seller.id LIKE :id " +
            "AND o.expirationTime> current_time " +
            "ORDER BY o.auction.product.name, o.offeredPrice DESC,o.submittedOn")
    List<Offer> findAllActiveOffersToUser(@Param(value = "id") String userId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Offer o " +
            "SET o.isValid=false " +
            "WHERE o.auction.id LIKE :id")
    void invalidateOffersOfAuctionById(@Param(value = "id") String auctionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Offer o " +
            "WHERE o.auction.id LIKE :id")
    void deleteOffersOfAuctionById(@Param(value = "id") String auctionId);
}
