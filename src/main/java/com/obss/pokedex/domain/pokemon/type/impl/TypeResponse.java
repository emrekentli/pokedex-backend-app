import TypeDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TypeResponse {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;

    public static TypeResponse toResponse(TypeDto type) {
        return TypeResponse.builder()
                .id(type.getId())
                .created(type.getCreated())
                .modified(type.getModified())
                .name(type.getName())
                .build();
    }
}
