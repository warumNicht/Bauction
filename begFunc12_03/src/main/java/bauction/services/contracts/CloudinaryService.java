package bauction.services.contracts;

import java.io.File;
import java.io.IOException;

public interface CloudinaryService {

    String uploadImage(File multipartFile) throws IOException;
}
