package bauction.services.contracts;

import bauction.domain.models.serviceModels.deals.CommentServiceModel;

public interface CommentService {

    CommentServiceModel registerComment(CommentServiceModel commentServiceModel);
}
