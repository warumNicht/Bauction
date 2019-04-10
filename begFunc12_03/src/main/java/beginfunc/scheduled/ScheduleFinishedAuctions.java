package beginfunc.scheduled;

import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.deals.DealServiceModel;
import beginfunc.domain.models.serviceModels.participations.BiddingServiceModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.BiddingService;
import beginfunc.services.contracts.DealService;
import beginfunc.services.contracts.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
