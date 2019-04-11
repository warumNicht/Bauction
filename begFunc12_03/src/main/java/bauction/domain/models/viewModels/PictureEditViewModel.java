package bauction.domain.models.viewModels;

public class PictureEditViewModel extends BaseViewModel {
    private String path;

    public PictureEditViewModel() {
    }

    public PictureEditViewModel(String id, String path) {
        super(id);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
