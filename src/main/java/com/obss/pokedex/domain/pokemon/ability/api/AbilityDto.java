import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AbilityDto {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;
}
