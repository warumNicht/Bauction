package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.PictureServiceModel;

import java.util.List;

public interface PictureService {
    List<PictureServiceModel> findAllByProductId(Integer productId);

    void renamePicture(PictureServiceModel pictureServiceModel);
}
