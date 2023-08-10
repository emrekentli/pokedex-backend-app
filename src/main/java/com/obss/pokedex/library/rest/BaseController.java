import org.springframework.data.domain.Page;

import java.util.List;

public class BaseController {

    public <T> Response<DataResponse<T>> respond(List<T> items) {
        return ResponseBuilder.build(items);
    }

    public <T> Response<PageResponse<T>> respond(Page<T> items) {
        return ResponseBuilder.build(items);
    }

    public <T> Response<DataResponse<T>> respond(List<T> items, int page, int size, Long totalSize, int totalPage) {
        return ResponseBuilder.build(items, page, size, totalSize, totalPage);
    }

    protected <T> Response<T> respond(T item) {
        return ResponseBuilder.build(item);
    }

    protected Response<MetaResponse> respond(MetaResponse metaResponse) {
        return ResponseBuilder.build(metaResponse);
    }
}
