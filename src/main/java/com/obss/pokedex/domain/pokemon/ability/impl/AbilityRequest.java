import com.fasterxml.jackson.annotation.JsonProperty;
import AbilityDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbilityRequest {
    private final String name;
    public AbilityRequest(@JsonProperty("name") String name) {
        this.name = name;
    }
    public AbilityDto toDto() {
        return AbilityDto.builder()
                .name(name)
                .build();
    }
}
