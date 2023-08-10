import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByName(String roleName);

    Set<Role> findByNameIn(Set<String> roleNames);

    List<Role> findByIdIn(List<String> roleIds);
}
