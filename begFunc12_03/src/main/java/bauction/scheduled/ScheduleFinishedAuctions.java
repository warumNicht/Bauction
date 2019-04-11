package bauction.scheduled;

import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.participations.BiddingServiceModel;
import bauction.services.contracts.AuctionService;
import bauction.services.contracts.BiddingService;
import bauction.services.contracts.DealService;
import bauction.services.contracts.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleFinishedAuctions {
    private final AuctionService auctionService;
    private final DealService dealService;
    private final OfferService offerService;
    private final BiddingService biddingService;

    @Autowired
    public ScheduleFinishedAuctions(AuctionService auctionService, DealService dealService, OfferService offerService, BiddingService biddingService) {
        this.auctionService = auctionService;
        this.dealService = dealService;
        this.offerService = offerService;
        this.biddingService = biddingService;
    }

//    @Scheduled(fixedRate = 5000)
    private void terminateUnfinishedAuctions(){
        List<AuctionServiceModel> auctions = this.auctionService.findAllActivesAuctionsExceedingEndDate();
        for (AuctionServiceModel auction : auctions) {
            this.finishAuction(auction);
        }
    }

    private void finishAuction(AuctionServiceModel auction) {
        String type = auction.getType().name();

        if(type.equals("Standard")||type.equals("Preserved_Price")){

            BiddingServiceModel highestBidding= this.biddingService.findHighestBiddingOfAuction(auction.getId());
            if(highestBidding!=null) {
                if (type.equals("Standard")||
                        highestBidding.getReachedPrice().compareTo(auction.getWantedPrice())>=0){

                    DealServiceModel deal=new DealServiceModel(new Date(),highestBidding.getReachedPrice(),
                            auction.getSeller(),highestBidding.getParticipant(),auction);
                    this.dealService.registerDeal(deal);
                    auction.setBuyer(highestBidding.getParticipant());
                }
            }
        }else {
            this.offerService.invalidateOffersOfAuction(auction.getId());
        }
        auction.setStatus(AuctionStatus.Finished);
        this.auctionService.updateAuction(auction);
    }
}
