import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPokemonDto {
    private int count;
    private String next;
    private String previous;
    private List<PokeNameDto> results;
}
