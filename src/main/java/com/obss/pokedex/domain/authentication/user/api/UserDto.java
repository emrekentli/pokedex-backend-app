import RoleDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;
@Data
@Builder
public class UserDto {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String userName;
    private final String password;
    private final Boolean activity;
    private final Set<RoleDto> roles;
    private final String fullName;
    private final String email;
    private final String phoneNumber;
    private final List<String> catchList;
    private final List<String> wishList;
}
