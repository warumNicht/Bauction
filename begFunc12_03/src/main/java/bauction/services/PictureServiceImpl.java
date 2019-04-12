package bauction.services;

import bauction.domain.entities.productRelated.Picture;
import bauction.domain.models.serviceModels.PictureServiceModel;
import bauction.repositories.PictureRepository;
import bauction.services.contracts.PictureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PictureServiceModel> findAllByProductId(String productId) {
        List<Picture> allByProductId = this.pictureRepository.findAllByProductId(productId);
        return allByProductId.stream()
                .map(p->this.modelMapper.map(p,PictureServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PictureServiceModel> findAllByWithoutMain(String productId) {
        List<Picture> allByProductIdWithoutMain = this.pictureRepository.findAllByProductIdWithoutMain(productId);
        return allByProductIdWithoutMain.stream()
                .map(p->this.modelMapper.map(p,PictureServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteImage(String imageId) {
        this.pictureRepository.deleteById(imageId);
    }

    @Override
    public void savePicture(PictureServiceModel pictureServiceModel) {
        this.pictureRepository.saveAndFlush(this.modelMapper.map(pictureServiceModel,Picture.class));
    }

}
