import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpritesDto {
    @JsonProperty("back_default")
    private String backDefault;

    @JsonProperty("back_female")
    private String backFemale;

    @JsonProperty("back_shiny")
    private String backShiny;

    @JsonProperty("back_shiny_female")
    private String backShinyFemale;

    @JsonProperty("front_default")
    private String frontDefault;

    @JsonProperty("front_female")
    private String frontFemale;

    @JsonProperty("front_shiny")
    private String frontShiny;

    @JsonProperty("front_shiny_female")
    private String frontShinyFemale;

    private OtherSprites other;

    @Data
    @Builder
    public static class OtherSprites {
        @JsonProperty("dream_world")
        private DreamWorldSprites dreamWorld;

        @JsonProperty("official-artwork")
        private OfficialArtworkSprites officialArtwork;
    }

    @Data
    @Builder
    public static class DreamWorldSprites {
        @JsonProperty("front_default")
        private String frontDefault;

        @JsonProperty("front_female")
        private String frontFemale;
    }

    @Data
    @Builder
    public static class OfficialArtworkSprites {
        @JsonProperty("front_default")
        private String frontDefault;

        @JsonProperty("front_female")
        private String frontFemale;
    }
}