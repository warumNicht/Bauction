package beginfunc.web.controllers;

import beginfunc.services.contracts.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/images")
public class ImageController {
    private final PictureService pictureService;

    @Autowired
    public ImageController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @ResponseBody
    @GetMapping(value = "/fetch/product/{id}", produces = "application/json")
    public Object getImagesOfAuction(@PathVariable(name = "id") Integer id){
        List<String> urls = this.pictureService.findAllByProductId(id).stream()
                .map(image -> image.getPath())
                .collect(Collectors.toList());
        return urls;
    }
}
