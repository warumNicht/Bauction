package bauction.services;

import bauction.services.contracts.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(File imageFile) throws IOException {
        Map map = this.cloudinary.uploader().upload(imageFile, new HashMap());
        String imageUrl = map.get("url").toString();
        return imageUrl;
    }
}
