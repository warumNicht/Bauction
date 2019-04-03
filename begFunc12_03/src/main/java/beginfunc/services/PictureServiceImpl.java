package beginfunc.services;

import beginfunc.domain.entities.productRelated.Picture;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import beginfunc.repositories.PictureRepository;
import beginfunc.services.contracts.PictureService;
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
    public List<PictureServiceModel> findAllByProductId(Integer productId) {
        List<Picture> allByProductId = this.pictureRepository.findAllByProductId(productId);
        return allByProductId.stream()
                .map(p->this.modelMapper.map(p,PictureServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void renamePicture(PictureServiceModel pictureServiceModel) {
        Picture picture = this.modelMapper.map(pictureServiceModel, Picture.class);
        this.pictureRepository.save(picture);
    }
}
