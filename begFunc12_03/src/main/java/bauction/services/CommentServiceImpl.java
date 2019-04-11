package bauction.services;

import bauction.domain.entities.Comment;
import bauction.domain.models.serviceModels.deals.CommentServiceModel;
import bauction.repositories.CommentRepository;
import bauction.services.contracts.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentServiceModel registerComment(CommentServiceModel commentServiceModel) {
        try {
            Comment comment = this.modelMapper.map(commentServiceModel, Comment.class);
            return this.modelMapper.map(this.commentRepository.saveAndFlush(comment),CommentServiceModel.class);
        }catch (Exception e){
            return null;
        }
    }
}
