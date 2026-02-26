package web.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.repository.UserRepository;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner dataLoader() {
        return args -> {

            Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");
            roleRepository.save(userRole);
            roleRepository.save(adminRole);


            User user = new User("Test", "User", "user@example.com",
                    passwordEncoder.encode("password"));
            user.addRole(userRole);
            userRepository.save(user);

            User admin = new User("Admin", "Admin", "admin@example.com",
                    passwordEncoder.encode("admin"));
            admin.addRole(adminRole);
            userRepository.save(admin);
        };
    }
}