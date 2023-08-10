import com.fasterxml.jackson.annotation.JsonProperty;
import TypeDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeRequest {
    private final String name;

    public TypeRequest(@JsonProperty("name") String name) {
        this.name = name;
    }

    public TypeDto toDto() {
        return TypeDto.builder()
                .name(name)
                .build();
    }
}
