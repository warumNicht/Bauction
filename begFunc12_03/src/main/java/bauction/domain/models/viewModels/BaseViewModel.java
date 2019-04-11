package bauction.domain.models.viewModels;

public abstract class BaseViewModel {
    private String id;

    public BaseViewModel() {
    }

    public BaseViewModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
