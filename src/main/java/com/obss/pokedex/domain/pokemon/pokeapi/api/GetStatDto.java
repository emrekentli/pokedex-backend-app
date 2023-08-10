import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetStatDto {
    @JsonProperty("base_stat")
    private int baseStat;
    private int effort;
    private PokeNameDto stat;
}
