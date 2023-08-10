import AddStatDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddStatRequest {
    private String statId;
    private Integer statPoint;

    public AddStatDto toDto() {
      return  AddStatDto.builder().statPoint(statPoint).statId(statId).build();
    }
}
