package financeplanner.planner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a user by username (for login)
    Optional<User> findByUsername(String username);

    // Checks if username is taken (for registration)
    boolean existsByUsername(String username);

    // ✅ ADD THIS LINE: Checks if email is taken (for registration)
    boolean existsByEmail(String email);
}