package financeplanner.planner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Main query for the dashboard
    List<Expense> findByUserUsername(String username);

    // ✅ THE SECURITY GOLD STANDARD:
    // Prevents IDOR (Insecure Direct Object Reference) by checking ownership
    Optional<Expense> findByIdAndUser(Long id, User user);

    // ✅ NEW: Professional Reporting Feature
    // Allows the AI to analyze specific leaks (e.g., "Show me all 'Dining' expenses for User X")
    List<Expense> findByUserUsernameAndCategoryName(String username, String categoryName);

    // Optional: Useful for internal admin or cleanup tasks
    List<Expense> findByUserId(Long userId);
}