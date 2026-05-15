package financeplanner.planner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Standard constructor injection (Safe and clean)
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {
        // 1. Validation: Prevents two people from having the same username
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(username);

        // 2. Encryption: NEVER store plain-text passwords.
        // This uses BCrypt (from your SecurityConfig) to scramble the password.
        user.setPassword(passwordEncoder.encode(password));

        // 3. Persistence: Save the secure user to PostgreSQL
        return userRepository.save(user);
    }
}