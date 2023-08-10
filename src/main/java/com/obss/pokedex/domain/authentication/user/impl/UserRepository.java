import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUserName(String userName);

    Optional<User> findUserByUserName(String username);
    @Query("SELECT u FROM User u " +
            "WHERE (:userName IS NULL OR u.userName LIKE %:userName%) " +
            "AND (:fullName IS NULL OR u.fullName LIKE %:fullName%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%)")
    Page<User> filterUser(
            @Param("userName") String userName,
            @Param("fullName") String fullName,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            Pageable pageable
    );

    Optional<User> findByEmail(String userName);
}
