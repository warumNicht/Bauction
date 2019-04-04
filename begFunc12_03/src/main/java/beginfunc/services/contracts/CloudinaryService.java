package beginfunc.services.contracts;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface CloudinaryService {

    String uploadImage(File multipartFile) throws IOException;
}
