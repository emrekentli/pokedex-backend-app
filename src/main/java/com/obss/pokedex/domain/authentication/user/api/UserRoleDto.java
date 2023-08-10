import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleDto {
    private final String userId;
    private final String roleId;
}
