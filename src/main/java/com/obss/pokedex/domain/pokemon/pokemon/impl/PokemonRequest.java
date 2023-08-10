import PokemonDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonRequest {
    private String name;
    private Integer baseExperience;
    private Double height;
    private Double weight;
    private String imageUrl;

    public PokemonDto toDto() {
        return PokemonDto.builder()
                .name(name)
                .baseExperience(baseExperience)
                .height(height)
                .weight(weight)
                .imageUrl(imageUrl)
                .build();
    }
}
