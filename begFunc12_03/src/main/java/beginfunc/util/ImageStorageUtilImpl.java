package beginfunc.util;

import beginfunc.constants.AppConstants;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImageStorageUtilImpl implements ImageStorageUtil {
    private String TEMP_FOLDER_PATH;
    private String TEMP_FOLDER_URL_PATH;

    private final ModelMapper modelMapper;

    public ImageStorageUtilImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void setAuctionImagesTempFolderPath(String category) {
        String tempFolderPath = String.format("%s/%s/%s",
                AppConstants.UPLOAD_FOLDER_PATH, category, AppConstants.AUCTION_PICTURES_TEMP_FOLDER_NAME);
        this.TEMP_FOLDER_PATH=tempFolderPath;
        this.TEMP_FOLDER_URL_PATH=AppConstants.UPLOAD_FOLDER_URL_NAME+"/"+category;
    }

    @Override
    public boolean createAuctionImagesTempFolder() {
        boolean created = new File(this.TEMP_FOLDER_PATH).mkdir();
        return created;
    }

    @Override
    public boolean renameAuctionImagesTempFolder(String category, String auctionFolderNameById) {
        String pathToTempFolder = String.format("%s/%s/%s",
                AppConstants.UPLOAD_FOLDER_PATH, category, AppConstants.AUCTION_PICTURES_TEMP_FOLDER_NAME);

        String newPath = String.format("%s/%s/%s",
                AppConstants.UPLOAD_FOLDER_PATH, category, auctionFolderNameById);

        File tempFolder = new File(pathToTempFolder);
        boolean isRenamed = tempFolder.renameTo(new File(newPath));
        return isRenamed;
    }

    @Override
    public PictureServiceModel createProductMainPicture(File mainPictureFile) {
        try {
            return this.createPicture(mainPictureFile);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<PictureServiceModel> createProductPictures(File[] files) {
        List<PictureServiceModel> pictures=new ArrayList<>();
        try {
            for (File file : files) {
                pictures.add(this.createPicture(file));
            }
            return pictures;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void deleteTempFolder(String categoryName) {
        String pathToTempFolder = String.format("%s/%s/%s",
                AppConstants.UPLOAD_FOLDER_PATH, categoryName, AppConstants.AUCTION_PICTURES_TEMP_FOLDER_NAME);
        File tempFolder = new File(pathToTempFolder);
        for (File file : tempFolder.listFiles()) {
            file.delete();
        }
        tempFolder.delete();
    }

    private PictureServiceModel createPicture(File file) throws IOException {
        byte[] content = Files.readAllBytes(file.toPath());
        double size = content.length / 1000.0;
        String contentType = file.getAbsolutePath();
        String originalFilename = file.getName();
        String fileExtension = originalFilename.substring(originalFilename.indexOf(".") + 1);
        String currentPicturePath = this.TEMP_FOLDER_PATH + "/" + originalFilename;
        String currentPictureUrlPath = String.format("%s/%s/%s",
                this.TEMP_FOLDER_URL_PATH, AppConstants.AUCTION_PICTURES_TEMP_FOLDER_NAME, originalFilename);

        PictureServiceModel currentPicture = new PictureServiceModel();

        Path pictureNameAndPath = Paths.get(currentPicturePath);
        Files.write(pictureNameAndPath, content);
        return currentPicture;
    }


}
