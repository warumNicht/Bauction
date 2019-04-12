package bauction.services.contracts;

import bauction.domain.models.serviceModels.PictureServiceModel;

import java.util.List;

public interface PictureService {
    List<PictureServiceModel> findAllByProductId(String productId);

    List<PictureServiceModel> findAllByWithoutMain(String productId);

    void deleteImage(String imageId);

    void savePicture(PictureServiceModel pictureServiceModel);

}
