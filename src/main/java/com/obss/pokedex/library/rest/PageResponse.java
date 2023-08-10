import org.springframework.data.domain.Page;

public class PageResponse<T> {
    private Page<T> items = Page.empty();

    public PageResponse() {
    }

    public PageResponse(Page<T> items) {
        this.items = items;
    }

    public Page<T> getItems() {
        return items;
    }

}
