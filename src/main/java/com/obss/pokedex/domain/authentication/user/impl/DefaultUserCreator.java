import RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@RequiredArgsConstructor
public class DefaultUserCreator implements CommandLineRunner {

    @Value("${default.user.email}")
    private String email;
    @Value("${default.user.username}")
    private String username;
    @Value("${default.user.password}")
    private String password;

    private final UserRepository repository;
    private final RoleServiceImpl service;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if (repository.findUserByUserName(username).isEmpty()) {
           var role =  service.getRolesByRoleNames(Set.of("ROLE_ADMIN"));
            User user = new User();
            user.setUserName(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setFullName("Admin");
            user.setRoles(role);
            user.setActivity(true);
            user.setPhoneNumber("1234567890");
            user.setActivity(true);
            repository.save(user);

        }

    }
}