package beginfunc.util;

import beginfunc.domain.models.serviceModels.PictureServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ImageStorageUtil {

    void setAuctionImagesTempFolderPath(String category);

    boolean createAuctionImagesTempFolder();

    boolean renameAuctionImagesTempFolder(String category,String auctionFolderNameById);

    PictureServiceModel createProductMainPicture(File mainPictureFile);

    List<PictureServiceModel> createProductPictures(File[] files);

    void deleteTempFolder(String categoryName);
}
