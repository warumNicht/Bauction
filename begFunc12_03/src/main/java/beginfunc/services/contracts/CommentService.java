package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.deals.CommentServiceModel;

public interface CommentService {

    CommentServiceModel registerComment(CommentServiceModel commentServiceModel);
}
