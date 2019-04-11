package bauction.domain.models.bindingModels;

import bauction.annotations.EnumValidator;
import bauction.domain.entities.enums.AuctionType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class AuctionEditBindingModel {
    @Size(min = 2)
    private String name;

    @NotNull
    @NotEmpty
    @EnumValidator(enumClass = AuctionType.class)
    private String type;

    @NotNull
    @NotEmpty
    private String category;

    @Size(max = 500)
    @Pattern(regexp = "^(?=.*\\S).+$|^$",flags = Pattern.Flag.DOTALL,
            message = "Description of whitespaces is not valid!")
    private String description;

    @NotNull
    @NotEmpty
    @Size(min = 2,max = 40)
    @Pattern(regexp = "^[A-ZА-Я].*$",message = "Town must begin with uppercase letter!")
    @Pattern(regexp = "^[a-zA-Z\\s-\\u0400-\\u04FF]+$",message = "Town contains only letters, digits, spaces, or - !")
    private String town;

    @DecimalMin(value = "0.00", message = "Non negative value expected!")
    private BigDecimal wantedPrice;

    private boolean startLater;
    private MultipartFile mainImage;
    private MultipartFile[] images;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town.replaceAll("\\s+", " ").trim();;
    }

    public BigDecimal getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(BigDecimal wantedPrice) {
        this.wantedPrice = wantedPrice;
    }

    public boolean isStartLater() {
        return startLater;
    }

    public void setStartLater(boolean startLater) {
        this.startLater = startLater;
    }

    public MultipartFile getMainImage() {
        return mainImage;
    }

    public void setMainImage(MultipartFile mainImage) {
        this.mainImage = mainImage;
    }

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }
}
