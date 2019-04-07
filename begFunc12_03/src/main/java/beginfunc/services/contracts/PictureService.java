package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.PictureServiceModel;

import java.util.List;

public interface PictureService {
    List<PictureServiceModel> findAllByProductId(String productId);

    List<PictureServiceModel> findAllByWithoutMain(String productId);

    void renamePicture(PictureServiceModel pictureServiceModel);

    void deleteImage(String imageId);

    void savePicture(PictureServiceModel pictureServiceModel);

}
