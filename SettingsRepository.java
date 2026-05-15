package financeplanner.planner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository // Added for consistency with your other repositories
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    // Standard CRUD operations are sufficient for storing global/user settings
    Optional<Settings> findByUserId(Long userId);
}