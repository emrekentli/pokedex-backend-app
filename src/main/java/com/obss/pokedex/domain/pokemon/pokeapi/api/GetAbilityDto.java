import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAbilityDto {
    @JsonProperty("is_hidden")
    private boolean isHidden;
    private int slot;
    private PokeNameDto ability;
}
