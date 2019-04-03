import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        Cloudinary cloudinary = new Cloudinary();
        File file = new File("D:\\7_Java_WEB_Spring_MVC_Framework\\finProject\\droppppbox\\src\\main\\Pleiades_large.jpg");
        Map map = new LinkedHashMap();
        map.put("cloud_name","bauction");
        map.put("api_key","717629339892318");
        map.put("api_secret","ajQLECgEd89GEvtU2JuaS526H68");
        Map uploadResult = cloudinary.uploader().upload(file,map );
        System.out.println();


        String dirPath="D:\\7_Java_WEB_Spring_MVC_Framework\\finProject\\begFunc12_03\\src\\main\\files";
        File folder = new File(dirPath);
        folder.renameTo(new File("D:\\7_Java_WEB_Spring_MVC_Framework\\finProject\\begFunc12_03\\src\\main\\files2"));
        System.out.println();
    }
}
