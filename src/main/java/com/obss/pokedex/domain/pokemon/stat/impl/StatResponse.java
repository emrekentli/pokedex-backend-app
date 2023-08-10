import StatDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StatResponse {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;

    public static StatResponse toResponse(StatDto stat) {
        return StatResponse.builder()
                .id(stat.getId())
                .created(stat.getCreated())
                .modified(stat.getModified())
                .name(stat.getName())
                .build();
    }
}
